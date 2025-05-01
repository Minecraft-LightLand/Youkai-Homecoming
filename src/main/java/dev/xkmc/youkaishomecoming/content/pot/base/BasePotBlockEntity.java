package dev.xkmc.youkaishomecoming.content.pot.base;

import com.google.common.collect.Lists;
import dev.xkmc.l2serial.util.Wrappers;
import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Component.Serializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeCraftingHolder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;
import vectorwing.farmersdelight.common.block.CookingPotBlock;
import vectorwing.farmersdelight.common.block.entity.CookingPotBlockEntity;
import vectorwing.farmersdelight.common.block.entity.HeatableBlockEntity;
import vectorwing.farmersdelight.common.block.entity.SyncedBlockEntity;
import vectorwing.farmersdelight.common.block.entity.inventory.CookingPotItemHandler;
import vectorwing.farmersdelight.common.item.component.ItemStackWrapper;
import vectorwing.farmersdelight.common.registry.ModDataComponents;
import vectorwing.farmersdelight.common.registry.ModParticleTypes;
import vectorwing.farmersdelight.common.utility.ItemUtils;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public abstract class BasePotBlockEntity<T extends BasePotRecipe> extends SyncedBlockEntity
		implements MenuProvider, HeatableBlockEntity, Nameable, RecipeCraftingHolder, ContainerData {
	public static final int MEAL_DISPLAY_SLOT = 4;
	public static final int CONTAINER_SLOT = 5;
	public static final int OUTPUT_SLOT = 6;
	public static final int INVENTORY_SIZE = 7;
	private final ItemStackHandler inventory = createHandler();
	private final IItemHandler inputHandler;
	private final IItemHandler outputHandler;
	private int cookTime;
	private int cookTimeTotal;
	private ItemStack mealContainerStack;
	private Component customName;
	private final Object2IntOpenHashMap<ResourceLocation> usedRecipeTracker;
	private final RecipeManager.CachedCheck<RecipeWrapper, T> quickCheck;

	private boolean wasHeated = false;

	public BasePotBlockEntity(BlockEntityType<? extends BasePotBlockEntity> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		inputHandler = new CookingPotItemHandler(inventory, Direction.UP);
		outputHandler = new CookingPotItemHandler(inventory, Direction.DOWN);
		mealContainerStack = ItemStack.EMPTY;
		usedRecipeTracker = new Object2IntOpenHashMap<>();
		quickCheck = RecipeManager.createCheck(getRecipeType());
	}

	public abstract RecipeType<T> getRecipeType();

	public static ItemStack getMealFromItem(ItemStack stack) {
		if (!(stack.getItem() instanceof BasePotItem)) return ItemStack.EMPTY;
		return stack.getOrDefault(ModDataComponents.MEAL, ItemStackWrapper.EMPTY).getStack();
	}

	public void loadAdditional(CompoundTag compound, HolderLookup.Provider registries) {
		super.loadAdditional(compound, registries);
		inventory.deserializeNBT(registries, compound.getCompound("Inventory"));
		cookTime = compound.getInt("CookTime");
		cookTimeTotal = compound.getInt("CookTimeTotal");
		mealContainerStack = ItemStack.parseOptional(registries, compound.getCompound("Container"));
		if (compound.contains("CustomName", 8)) {
			customName = Serializer.fromJson(compound.getString("CustomName"), registries);
		}

		CompoundTag recipes = compound.getCompound("RecipesUsed");
		for (String key : recipes.getAllKeys()) {
			usedRecipeTracker.put(ResourceLocation.parse(key), recipes.getInt(key));
		}

	}

	public void saveAdditional(CompoundTag compound, HolderLookup.Provider registries) {
		super.saveAdditional(compound, registries);
		compound.putInt("CookTime", cookTime);
		compound.putInt("CookTimeTotal", cookTimeTotal);
		compound.put("Container", mealContainerStack.saveOptional(registries));
		if (customName != null) {
			compound.putString("CustomName", Serializer.toJson(customName, registries));
		}

		compound.put("Inventory", inventory.serializeNBT(registries));
		CompoundTag compoundRecipes = new CompoundTag();
		usedRecipeTracker.forEach((recipeId, craftedAmount) -> {
			compoundRecipes.putInt(recipeId.toString(), craftedAmount);
		});
		compound.put("RecipesUsed", compoundRecipes);
	}

	private CompoundTag writeItems(CompoundTag compound, HolderLookup.Provider registries) {
		super.saveAdditional(compound, registries);
		compound.put("Container", mealContainerStack.saveOptional(registries));
		compound.put("Inventory", inventory.serializeNBT(registries));
		return compound;
	}

	public CompoundTag writeMeal(CompoundTag compound, HolderLookup.Provider registries) {
		if (getMeal().isEmpty()) {
			return compound;
		} else {
			ItemStackHandler drops = new ItemStackHandler(INVENTORY_SIZE);

			for (int i = 0; i < INVENTORY_SIZE; ++i) {
				drops.setStackInSlot(i, inventory.getStackInSlot(i));
			}

			if (customName != null) {
				compound.putString("CustomName", Serializer.toJson(customName, registries));
			}

			compound.put("Container", mealContainerStack.save(registries));
			compound.put("Inventory", drops.serializeNBT(registries));
			return compound;
		}
	}

	public ItemStack addItem(ItemStack stack) {
		var sim = level == null || level.isClientSide;
		return ItemHandlerHelper.insertItem(inventory, stack, sim);
	}

	public boolean isGridEmpty() {
		for (int i = 0; i < MEAL_DISPLAY_SLOT; i++) {
			var stack = inventory.getStackInSlot(i);
			if (!stack.isEmpty()) return false;
		}
		return true;
	}

	public ItemStack getAsItem() {
		ItemStack stack = new ItemStack(getBlockState().getBlock());
		stack.applyComponents(collectComponents());
		return stack;
	}

	public void popAll() {
		if (level == null) return;
		var pos = this.getBlockPos().above();
		for (int i = 0; i < MEAL_DISPLAY_SLOT; i++) {
			var stack = inventory.getStackInSlot(i);
			inventory.setStackInSlot(i, ItemStack.EMPTY);
			Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stack);
		}
	}

	public static <T extends BasePotRecipe> void cookingTick(Level level, BlockPos pos, BlockState state, BasePotBlockEntity<T> pot) {
		pot.cookingTick(1, false);
	}

	public void cookingTick(int tick, boolean override) {
		BlockPos pos = getBlockPos();
		if (level == null) return;
		boolean isHeated = override || isHeated(level, pos);
		boolean didInventoryChange = false;
		if (isHeated && hasInput()) {
			Optional<RecipeHolder<T>> recipe = getMatchingRecipe(new RecipeWrapper(inventory));
			if (recipe.isPresent() && canCook(recipe.get().value())) {
				didInventoryChange = processCooking(tick, recipe.get());
			} else {
				cookTime = 0;
			}
		} else if (cookTime > 0 && !wasHeated) {
			cookTime = Mth.clamp(cookTime - 2, 0, cookTimeTotal);
		}
		wasHeated = override;

		ItemStack mealStack = getMeal();
		if (!mealStack.isEmpty()) {
			if (!doesMealHaveContainer(mealStack)) {
				moveMealToOutput();
				didInventoryChange = true;
			} else if (!inventory.getStackInSlot(CONTAINER_SLOT).isEmpty()) {
				useStoredContainersOnMeal();
				didInventoryChange = true;
			}
		}

		if (didInventoryChange) {
			inventoryChanged();
		}

	}

	public static void animationTick(Level level, BlockPos pos, BlockState state, BasePotBlockEntity<?> pot) {
		if (pot.isHeated(level, pos)) {
			RandomSource random = level.random;
			double x;
			double y;
			double z;
			if (random.nextFloat() < 0.2F) {
				x = (double) pos.getX() + 0.5 + (random.nextDouble() * 0.6 - 0.3);
				y = (double) pos.getY() + 0.7;
				z = (double) pos.getZ() + 0.5 + (random.nextDouble() * 0.6 - 0.3);
				level.addParticle(ParticleTypes.BUBBLE_POP, x, y, z, 0.0, 0.0, 0.0);
			}

			if (random.nextFloat() < 0.05F) {
				x = (double) pos.getX() + 0.5 + (random.nextDouble() * 0.4 - 0.2);
				y = (double) pos.getY() + 0.5;
				z = (double) pos.getZ() + 0.5 + (random.nextDouble() * 0.4 - 0.2);
				double motionY = random.nextBoolean() ? 0.015 : 0.005;
				level.addParticle(ModParticleTypes.STEAM.get(), x, y, z, 0.0, motionY, 0.0);
			}
		}

	}

	private Optional<RecipeHolder<T>> getMatchingRecipe(RecipeWrapper inventoryWrapper) {
		if (level == null) {
			return Optional.empty();
		} else {
			return hasInput() ? quickCheck.getRecipeFor(inventoryWrapper, level) : Optional.empty();
		}
	}

	public ItemStack getContainer() {
		ItemStack mealStack = getMeal();
		return !mealStack.isEmpty() && !mealContainerStack.isEmpty() ? mealContainerStack : mealStack.getCraftingRemainingItem();
	}

	private boolean hasInput() {
		for (int i = 0; i < MEAL_DISPLAY_SLOT; ++i) {
			if (!inventory.getStackInSlot(i).isEmpty()) {
				return true;
			}
		}

		return false;
	}

	protected boolean canCook(T recipe) {
		if (level != null && hasInput()) {
			ItemStack resultStack = recipe.getResultItem(level.registryAccess());
			if (resultStack.isEmpty()) {
				return false;
			} else {
				ItemStack storedMealStack = inventory.getStackInSlot(MEAL_DISPLAY_SLOT);
				if (storedMealStack.isEmpty()) {
					return true;
				} else if (!ItemStack.isSameItem(storedMealStack, resultStack)) {
					return false;
				} else if (storedMealStack.getCount() + resultStack.getCount() <= inventory.getSlotLimit(MEAL_DISPLAY_SLOT)) {
					return true;
				} else {
					return storedMealStack.getCount() + resultStack.getCount() <= resultStack.getMaxStackSize();
				}
			}
		} else {
			return false;
		}
	}

	protected boolean processCooking(int tick, RecipeHolder<T> recipe) {
		if (level == null) {
			return false;
		} else {
			cookTime += tick;
			cookTimeTotal = recipe.value().getCookTime();
			if (cookTime < cookTimeTotal) {
				return false;
			} else {
				cookTime = 0;
				mealContainerStack = recipe.value().getOutputContainer();
				ItemStack resultStack = recipe.value().getResultItem(level.registryAccess());
				ItemStack storedMealStack = inventory.getStackInSlot(MEAL_DISPLAY_SLOT);
				if (storedMealStack.isEmpty()) {
					inventory.setStackInSlot(MEAL_DISPLAY_SLOT, resultStack.copy());
				} else if (ItemStack.isSameItem(storedMealStack, resultStack)) {
					storedMealStack.grow(resultStack.getCount());
				}

				setRecipeUsed(recipe);
				int[] consume = recipe.value().getConsumption(new RecipeWrapper(inventory));

				for (int i = 0; i < MEAL_DISPLAY_SLOT; ++i) {
					if (consume[i] == 0) continue;
					ItemStack slotStack = inventory.getStackInSlot(i);
					if (slotStack.hasCraftingRemainingItem()) {
						var cont = slotStack.getCraftingRemainingItem();
						cont.setCount(consume[i]);
						ejectIngredientRemainder(cont);
					} else if (CookingPotBlockEntity.INGREDIENT_REMAINDER_OVERRIDES.containsKey(slotStack.getItem())) {
						ejectIngredientRemainder((CookingPotBlockEntity.INGREDIENT_REMAINDER_OVERRIDES.get(slotStack.getItem())).getDefaultInstance());
					}

					if (!slotStack.isEmpty()) {
						slotStack.shrink(consume[i]);
					}
				}

				return true;
			}
		}
	}

	protected void ejectIngredientRemainder(ItemStack remainderStack) {
		Direction direction = getBlockState().getValue(CookingPotBlock.FACING).getCounterClockWise();
		double x = (double) worldPosition.getX() + 0.5 + (double) direction.getStepX() * 0.25;
		double y = (double) worldPosition.getY() + 0.7;
		double z = (double) worldPosition.getZ() + 0.5 + (double) direction.getStepZ() * 0.25;
		ItemUtils.spawnItemEntity(level, remainderStack, x, y, z, (float) direction.getStepX() * 0.08F, 0.25, (float) direction.getStepZ() * 0.08F);
	}

	public void setRecipeUsed(@Nullable RecipeHolder<?> recipe) {
		if (recipe != null) {
			ResourceLocation recipeID = recipe.id();
			usedRecipeTracker.addTo(recipeID, 1);
		}

	}

	@Nullable
	public RecipeHolder<?> getRecipeUsed() {
		return null;
	}

	public void awardUsedRecipes(Player player, List<ItemStack> items) {
		List<RecipeHolder<T>> usedRecipes = getUsedRecipesAndPopExperience(player.level(), player.position());
		player.awardRecipes(Wrappers.cast(usedRecipes));
		usedRecipeTracker.clear();
	}

	public List<RecipeHolder<T>> getUsedRecipesAndPopExperience(Level level, Vec3 pos) {
		List<RecipeHolder<T>> list = Lists.newArrayList();

		for (var e : usedRecipeTracker.object2IntEntrySet()) {
			var opt = level.getRecipeManager().byKey(e.getKey());
			if (opt.isEmpty()) continue;
			RecipeHolder<T> recipe = Wrappers.cast(opt.get());
			list.add(recipe);
			splitAndSpawnExperience((ServerLevel) level, pos, e.getIntValue(), recipe.value().getExperience());
		}
		return list;
	}

	private static void splitAndSpawnExperience(ServerLevel level, Vec3 pos, int craftedAmount, float experience) {
		int expTotal = Mth.floor((float) craftedAmount * experience);
		float expFraction = Mth.frac((float) craftedAmount * experience);
		if (expFraction != 0.0F && Math.random() < (double) expFraction) {
			++expTotal;
		}

		ExperienceOrb.award(level, pos, expTotal);
	}

	public boolean isHeated() {
		return level != null && isHeated(level, worldPosition);
	}

	public ItemStackHandler getInventory() {
		return inventory;
	}

	public ItemStack getMeal() {
		return inventory.getStackInSlot(MEAL_DISPLAY_SLOT);
	}

	public NonNullList<ItemStack> getDroppableInventory() {
		NonNullList<ItemStack> drops = NonNullList.create();

		for (int i = 0; i < INVENTORY_SIZE; ++i) {
			if (i != MEAL_DISPLAY_SLOT) {
				drops.add(inventory.getStackInSlot(i));
			}
		}

		return drops;
	}

	private void moveMealToOutput() {
		ItemStack mealStack = inventory.getStackInSlot(MEAL_DISPLAY_SLOT);
		ItemStack outputStack = inventory.getStackInSlot(OUTPUT_SLOT);
		int mealCount = Math.min(mealStack.getCount(), mealStack.getMaxStackSize() - outputStack.getCount());
		if (outputStack.isEmpty()) {
			inventory.setStackInSlot(OUTPUT_SLOT, mealStack.split(mealCount));
		} else if (outputStack.getItem() == mealStack.getItem()) {
			mealStack.shrink(mealCount);
			outputStack.grow(mealCount);
		}

	}

	private void useStoredContainersOnMeal() {
		ItemStack mealStack = inventory.getStackInSlot(MEAL_DISPLAY_SLOT);
		ItemStack containerInputStack = inventory.getStackInSlot(CONTAINER_SLOT);
		ItemStack outputStack = inventory.getStackInSlot(OUTPUT_SLOT);
		if (isContainerValid(containerInputStack) && outputStack.getCount() < outputStack.getMaxStackSize()) {
			int smallerStackCount = Math.min(mealStack.getCount(), containerInputStack.getCount());
			int mealCount = Math.min(smallerStackCount, mealStack.getMaxStackSize() - outputStack.getCount());
			if (outputStack.isEmpty()) {
				containerInputStack.shrink(mealCount);
				inventory.setStackInSlot(OUTPUT_SLOT, mealStack.split(mealCount));
			} else if (outputStack.getItem() == mealStack.getItem()) {
				mealStack.shrink(mealCount);
				containerInputStack.shrink(mealCount);
				outputStack.grow(mealCount);
			}
		}

	}

	public ItemStack useHeldItemOnMeal(ItemStack container) {
		if (isContainerValid(container) && !getMeal().isEmpty()) {
			container.shrink(1);
			return getMeal().split(1);
		} else {
			return ItemStack.EMPTY;
		}
	}

	private boolean doesMealHaveContainer(ItemStack meal) {
		return !mealContainerStack.isEmpty() || meal.hasCraftingRemainingItem();
	}

	public boolean isContainerValid(ItemStack containerItem) {
		if (containerItem.isEmpty()) {
			return false;
		} else {
			return !mealContainerStack.isEmpty() ? ItemStack.isSameItem(mealContainerStack, containerItem) : ItemStack.isSameItem(getMeal(), containerItem);
		}
	}

	public Component getName() {
		return customName != null ? customName : getBlockState().getBlock().getName();
	}

	public Component getDisplayName() {
		return getName();
	}

	@Nullable
	public Component getCustomName() {
		return customName;
	}

	public abstract AbstractContainerMenu createMenu(int id, Inventory player, Player entity);

	public void setRemoved() {
		super.setRemoved();
	}

	public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
		return writeItems(new CompoundTag(), registries);
	}

	protected void applyImplicitComponents(BlockEntity.DataComponentInput data) {
		super.applyImplicitComponents(data);
		customName = data.get(DataComponents.CUSTOM_NAME);
		getInventory().setStackInSlot(MEAL_DISPLAY_SLOT, data.getOrDefault(ModDataComponents.MEAL, ItemStackWrapper.EMPTY).getStack());
		mealContainerStack = data.getOrDefault(ModDataComponents.CONTAINER, ItemStackWrapper.EMPTY).getStack();
		var contents = data.get(YHItems.ITEMS);
		if (contents != null) {
			for (int i = 0; i < contents.getSlots(); i++) {
				if (i != MEAL_DISPLAY_SLOT) {
					inventory.setStackInSlot(i, contents.getStackInSlot(i));
				}
			}
		}

	}

	protected void collectImplicitComponents(DataComponentMap.Builder data) {
		super.collectImplicitComponents(data);
		data.set(DataComponents.CUSTOM_NAME, customName);
		if (!getMeal().isEmpty()) {
			data.set(ModDataComponents.MEAL, new ItemStackWrapper(getMeal()));
		}
		if (!getContainer().isEmpty()) {
			data.set(ModDataComponents.CONTAINER, new ItemStackWrapper(getContainer()));
		}

		NonNullList<ItemStack> drops = NonNullList.create();
		boolean hasAnything = false;
		for (int i = 0; i < INVENTORY_SIZE; ++i) {
			if (i != MEAL_DISPLAY_SLOT) {
				var stack = inventory.getStackInSlot(i);
				if (!stack.isEmpty()) {
					drops.add(stack);
					hasAnything = true;
					continue;
				}
			}
			drops.add(ItemStack.EMPTY);
		}
		if (hasAnything) {
			data.set(YHItems.ITEMS, ItemContainerContents.fromItems(drops));
		}
	}

	public void removeComponentsFromTag(CompoundTag tag) {
		tag.remove("CustomName");
		tag.remove("meal");
		tag.remove("container");
	}

	public IItemHandler getItemHandler(@Nullable Direction side) {
		return side != null && side.equals(Direction.UP) ? outputHandler : inputHandler;
	}

	private ItemStackHandler createHandler() {
		return new ItemStackHandler(INVENTORY_SIZE) {
			protected void onContentsChanged(int slot) {
				inventoryChanged();
			}
		};
	}

	public int get(int index) {
		if (index == 0) return cookTime;
		if (index == 1) return cookTimeTotal;
		return 0;
	}

	public void set(int index, int value) {
		if (index == 0) cookTime = value;
		if (index == 1) cookTimeTotal = value;
	}

	public int getCount() {
		return 2;
	}

}
