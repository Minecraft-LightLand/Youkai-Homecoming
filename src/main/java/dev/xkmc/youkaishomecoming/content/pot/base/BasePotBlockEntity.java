package dev.xkmc.youkaishomecoming.content.pot.base;

import com.google.common.collect.Lists;
import dev.xkmc.l2serial.util.Wrappers;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Component.Serializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeCraftingHolder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;
import vectorwing.farmersdelight.common.block.CookingPotBlock;
import vectorwing.farmersdelight.common.block.entity.CookingPotBlockEntity;
import vectorwing.farmersdelight.common.block.entity.HeatableBlockEntity;
import vectorwing.farmersdelight.common.block.entity.SyncedBlockEntity;
import vectorwing.farmersdelight.common.block.entity.inventory.CookingPotItemHandler;
import vectorwing.farmersdelight.common.item.component.ItemStackWrapper;
import vectorwing.farmersdelight.common.registry.ModDataComponents;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.registry.ModParticleTypes;
import vectorwing.farmersdelight.common.utility.ItemUtils;
import vectorwing.farmersdelight.common.utility.TextUtils;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public abstract class BasePotBlockEntity<T extends BasePotRecipe> extends SyncedBlockEntity
		implements MenuProvider, HeatableBlockEntity, Nameable, RecipeCraftingHolder, ContainerData {
	public static final int MEAL_DISPLAY_SLOT = 4;
	public static final int CONTAINER_SLOT = 5;
	public static final int OUTPUT_SLOT = 6;
	public static final int INVENTORY_SIZE = 7;
	private final ItemStackHandler inventory = this.createHandler();
	private final IItemHandler inputHandler;
	private final IItemHandler outputHandler;
	private int cookTime;
	private int cookTimeTotal;
	private ItemStack mealContainerStack;
	private Component customName;
	private final Object2IntOpenHashMap<ResourceLocation> usedRecipeTracker;
	private final RecipeManager.CachedCheck<RecipeWrapper, T> quickCheck;

	public BasePotBlockEntity(BlockEntityType<? extends BasePotBlockEntity> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		this.inputHandler = new CookingPotItemHandler(this.inventory, Direction.UP);
		this.outputHandler = new CookingPotItemHandler(this.inventory, Direction.DOWN);
		this.mealContainerStack = ItemStack.EMPTY;
		this.usedRecipeTracker = new Object2IntOpenHashMap<>();
		this.quickCheck = RecipeManager.createCheck(getRecipeType());
	}

	public abstract RecipeType<T> getRecipeType();

	public static ItemStack getMealFromItem(ItemStack stack) {
		if (!(stack.getItem() instanceof BasePotItem)) return ItemStack.EMPTY;
		return stack.getOrDefault(ModDataComponents.MEAL, ItemStackWrapper.EMPTY).getStack();
	}

	public void loadAdditional(CompoundTag compound, HolderLookup.Provider registries) {
		super.loadAdditional(compound, registries);
		this.inventory.deserializeNBT(registries, compound.getCompound("Inventory"));
		this.cookTime = compound.getInt("CookTime");
		this.cookTimeTotal = compound.getInt("CookTimeTotal");
		this.mealContainerStack = ItemStack.parseOptional(registries, compound.getCompound("Container"));
		if (compound.contains("CustomName", 8)) {
			this.customName = Serializer.fromJson(compound.getString("CustomName"), registries);
		}

		CompoundTag recipes = compound.getCompound("RecipesUsed");
		for (String key : recipes.getAllKeys()) {
			this.usedRecipeTracker.put(ResourceLocation.parse(key), recipes.getInt(key));
		}

	}

	public void saveAdditional(CompoundTag compound, HolderLookup.Provider registries) {
		super.saveAdditional(compound, registries);
		compound.putInt("CookTime", this.cookTime);
		compound.putInt("CookTimeTotal", this.cookTimeTotal);
		compound.put("Container", this.mealContainerStack.saveOptional(registries));
		if (this.customName != null) {
			compound.putString("CustomName", Serializer.toJson(this.customName, registries));
		}

		compound.put("Inventory", this.inventory.serializeNBT(registries));
		CompoundTag compoundRecipes = new CompoundTag();
		this.usedRecipeTracker.forEach((recipeId, craftedAmount) -> {
			compoundRecipes.putInt(recipeId.toString(), craftedAmount);
		});
		compound.put("RecipesUsed", compoundRecipes);
	}

	private CompoundTag writeItems(CompoundTag compound, HolderLookup.Provider registries) {
		super.saveAdditional(compound, registries);
		compound.put("Container", this.mealContainerStack.saveOptional(registries));
		compound.put("Inventory", this.inventory.serializeNBT(registries));
		return compound;
	}

	public CompoundTag writeMeal(CompoundTag compound, HolderLookup.Provider registries) {
		if (this.getMeal().isEmpty()) {
			return compound;
		} else {
			ItemStackHandler drops = new ItemStackHandler(9);

			for (int i = 0; i < 9; ++i) {
				drops.setStackInSlot(i, i == 6 ? this.inventory.getStackInSlot(i) : ItemStack.EMPTY);
			}

			if (this.customName != null) {
				compound.putString("CustomName", Serializer.toJson(this.customName, registries));
			}

			compound.put("Container", this.mealContainerStack.save(registries));
			compound.put("Inventory", drops.serializeNBT(registries));
			return compound;
		}
	}

	public ItemStack getAsItem() {
		ItemStack stack = new ItemStack((ItemLike) ModItems.COOKING_POT.get());
		stack.applyComponents(this.collectComponents());
		return stack;
	}

	public static <T extends BasePotRecipe> void cookingTick(Level level, BlockPos pos, BlockState state, BasePotBlockEntity<T> cookingPot) {
		boolean isHeated = cookingPot.isHeated(level, pos);
		boolean didInventoryChange = false;
		if (isHeated && cookingPot.hasInput()) {
			Optional<RecipeHolder<T>> recipe = cookingPot.getMatchingRecipe(new RecipeWrapper(cookingPot.inventory));
			if (recipe.isPresent() && cookingPot.canCook(recipe.get().value())) {
				didInventoryChange = cookingPot.processCooking(recipe.get());
			} else {
				cookingPot.cookTime = 0;
			}
		} else if (cookingPot.cookTime > 0) {
			cookingPot.cookTime = Mth.clamp(cookingPot.cookTime - 2, 0, cookingPot.cookTimeTotal);
		}

		ItemStack mealStack = cookingPot.getMeal();
		if (!mealStack.isEmpty()) {
			if (!cookingPot.doesMealHaveContainer(mealStack)) {
				cookingPot.moveMealToOutput();
				didInventoryChange = true;
			} else if (!cookingPot.inventory.getStackInSlot(7).isEmpty()) {
				cookingPot.useStoredContainersOnMeal();
				didInventoryChange = true;
			}
		}

		if (didInventoryChange) {
			cookingPot.inventoryChanged();
		}

	}

	public static void animationTick(Level level, BlockPos pos, BlockState state, BasePotBlockEntity<?> cookingPot) {
		if (cookingPot.isHeated(level, pos)) {
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
				level.addParticle((ParticleOptions) ModParticleTypes.STEAM.get(), x, y, z, 0.0, motionY, 0.0);
			}
		}

	}

	private Optional<RecipeHolder<T>> getMatchingRecipe(RecipeWrapper inventoryWrapper) {
		if (this.level == null) {
			return Optional.empty();
		} else {
			return this.hasInput() ? this.quickCheck.getRecipeFor(inventoryWrapper, this.level) : Optional.empty();
		}
	}

	public ItemStack getContainer() {
		ItemStack mealStack = this.getMeal();
		return !mealStack.isEmpty() && !this.mealContainerStack.isEmpty() ? this.mealContainerStack : mealStack.getCraftingRemainingItem();
	}

	private boolean hasInput() {
		for (int i = 0; i < 6; ++i) {
			if (!this.inventory.getStackInSlot(i).isEmpty()) {
				return true;
			}
		}

		return false;
	}

	protected boolean canCook(T recipe) {
		if (level != null && this.hasInput()) {
			ItemStack resultStack = recipe.getResultItem(this.level.registryAccess());
			if (resultStack.isEmpty()) {
				return false;
			} else {
				ItemStack storedMealStack = this.inventory.getStackInSlot(6);
				if (storedMealStack.isEmpty()) {
					return true;
				} else if (!ItemStack.isSameItem(storedMealStack, resultStack)) {
					return false;
				} else if (storedMealStack.getCount() + resultStack.getCount() <= this.inventory.getSlotLimit(6)) {
					return true;
				} else {
					return storedMealStack.getCount() + resultStack.getCount() <= resultStack.getMaxStackSize();
				}
			}
		} else {
			return false;
		}
	}

	protected boolean processCooking(RecipeHolder<T> recipe) {
		if (this.level == null) {
			return false;
		} else {
			++this.cookTime;
			this.cookTimeTotal = recipe.value().getCookTime();
			if (this.cookTime < this.cookTimeTotal) {
				return false;
			} else {
				this.cookTime = 0;
				this.mealContainerStack = recipe.value().getOutputContainer();
				ItemStack resultStack = recipe.value().getResultItem(this.level.registryAccess());
				ItemStack storedMealStack = this.inventory.getStackInSlot(6);
				if (storedMealStack.isEmpty()) {
					this.inventory.setStackInSlot(6, resultStack.copy());
				} else if (ItemStack.isSameItem(storedMealStack, resultStack)) {
					storedMealStack.grow(resultStack.getCount());
				}

				setRecipeUsed(recipe);

				for (int i = 0; i < 6; ++i) {
					ItemStack slotStack = this.inventory.getStackInSlot(i);
					if (slotStack.hasCraftingRemainingItem()) {
						this.ejectIngredientRemainder(slotStack.getCraftingRemainingItem());
					} else if (CookingPotBlockEntity.INGREDIENT_REMAINDER_OVERRIDES.containsKey(slotStack.getItem())) {
						this.ejectIngredientRemainder((CookingPotBlockEntity.INGREDIENT_REMAINDER_OVERRIDES.get(slotStack.getItem())).getDefaultInstance());
					}

					if (!slotStack.isEmpty()) {
						slotStack.shrink(1);
					}
				}

				return true;
			}
		}
	}

	protected void ejectIngredientRemainder(ItemStack remainderStack) {
		Direction direction = ((Direction) this.getBlockState().getValue(CookingPotBlock.FACING)).getCounterClockWise();
		double x = (double) this.worldPosition.getX() + 0.5 + (double) direction.getStepX() * 0.25;
		double y = (double) this.worldPosition.getY() + 0.7;
		double z = (double) this.worldPosition.getZ() + 0.5 + (double) direction.getStepZ() * 0.25;
		ItemUtils.spawnItemEntity(this.level, remainderStack, x, y, z, (double) ((float) direction.getStepX() * 0.08F), 0.25, (double) ((float) direction.getStepZ() * 0.08F));
	}

	public void setRecipeUsed(@Nullable RecipeHolder<?> recipe) {
		if (recipe != null) {
			ResourceLocation recipeID = recipe.id();
			this.usedRecipeTracker.addTo(recipeID, 1);
		}

	}

	@Nullable
	public RecipeHolder<?> getRecipeUsed() {
		return null;
	}

	public void awardUsedRecipes(Player player, List<ItemStack> items) {
		List<RecipeHolder<T>> usedRecipes = this.getUsedRecipesAndPopExperience(player.level(), player.position());
		player.awardRecipes(Wrappers.cast(usedRecipes));
		this.usedRecipeTracker.clear();
	}

	public List<RecipeHolder<T>> getUsedRecipesAndPopExperience(Level level, Vec3 pos) {
		List<RecipeHolder<T>> list = Lists.newArrayList();

		for (var e : this.usedRecipeTracker.object2IntEntrySet()) {
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
		return this.level != null && this.isHeated(this.level, this.worldPosition);
	}

	public ItemStackHandler getInventory() {
		return this.inventory;
	}

	public ItemStack getMeal() {
		return this.inventory.getStackInSlot(6);
	}

	public NonNullList<ItemStack> getDroppableInventory() {
		NonNullList<ItemStack> drops = NonNullList.create();

		for (int i = 0; i < 9; ++i) {
			if (i != 6) {
				drops.add(this.inventory.getStackInSlot(i));
			}
		}

		return drops;
	}

	private void moveMealToOutput() {
		ItemStack mealStack = this.inventory.getStackInSlot(6);
		ItemStack outputStack = this.inventory.getStackInSlot(8);
		int mealCount = Math.min(mealStack.getCount(), mealStack.getMaxStackSize() - outputStack.getCount());
		if (outputStack.isEmpty()) {
			this.inventory.setStackInSlot(8, mealStack.split(mealCount));
		} else if (outputStack.getItem() == mealStack.getItem()) {
			mealStack.shrink(mealCount);
			outputStack.grow(mealCount);
		}

	}

	private void useStoredContainersOnMeal() {
		ItemStack mealStack = this.inventory.getStackInSlot(6);
		ItemStack containerInputStack = this.inventory.getStackInSlot(7);
		ItemStack outputStack = this.inventory.getStackInSlot(8);
		if (this.isContainerValid(containerInputStack) && outputStack.getCount() < outputStack.getMaxStackSize()) {
			int smallerStackCount = Math.min(mealStack.getCount(), containerInputStack.getCount());
			int mealCount = Math.min(smallerStackCount, mealStack.getMaxStackSize() - outputStack.getCount());
			if (outputStack.isEmpty()) {
				containerInputStack.shrink(mealCount);
				this.inventory.setStackInSlot(8, mealStack.split(mealCount));
			} else if (outputStack.getItem() == mealStack.getItem()) {
				mealStack.shrink(mealCount);
				containerInputStack.shrink(mealCount);
				outputStack.grow(mealCount);
			}
		}

	}

	public ItemStack useHeldItemOnMeal(ItemStack container) {
		if (this.isContainerValid(container) && !this.getMeal().isEmpty()) {
			container.shrink(1);
			return this.getMeal().split(1);
		} else {
			return ItemStack.EMPTY;
		}
	}

	private boolean doesMealHaveContainer(ItemStack meal) {
		return !this.mealContainerStack.isEmpty() || meal.hasCraftingRemainingItem();
	}

	public boolean isContainerValid(ItemStack containerItem) {
		if (containerItem.isEmpty()) {
			return false;
		} else {
			return !this.mealContainerStack.isEmpty() ? ItemStack.isSameItem(this.mealContainerStack, containerItem) : ItemStack.isSameItem(this.getMeal(), containerItem);
		}
	}

	public Component getName() {
		return (Component) (this.customName != null ? this.customName : TextUtils.getTranslation("container.cooking_pot", new Object[0]));
	}

	public Component getDisplayName() {
		return this.getName();
	}

	@Nullable
	public Component getCustomName() {
		return this.customName;
	}

	public abstract AbstractContainerMenu createMenu(int id, Inventory player, Player entity);

	public void setRemoved() {
		super.setRemoved();
	}

	public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
		return this.writeItems(new CompoundTag(), registries);
	}

	protected void applyImplicitComponents(BlockEntity.DataComponentInput data) {
		super.applyImplicitComponents(data);
		this.customName = data.get(DataComponents.CUSTOM_NAME);
		this.getInventory().setStackInSlot(6, data.getOrDefault(ModDataComponents.MEAL, ItemStackWrapper.EMPTY).getStack());
		this.mealContainerStack = data.getOrDefault(ModDataComponents.CONTAINER, ItemStackWrapper.EMPTY).getStack();
	}

	protected void collectImplicitComponents(DataComponentMap.Builder data) {
		super.collectImplicitComponents(data);
		data.set(DataComponents.CUSTOM_NAME, this.customName);
		if (!this.getMeal().isEmpty()) {
			data.set(ModDataComponents.MEAL, new ItemStackWrapper(this.getMeal()));
		}

		if (!this.getContainer().isEmpty()) {
			data.set(ModDataComponents.CONTAINER, new ItemStackWrapper(this.getContainer()));
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
		return new ItemStackHandler(9) {
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
