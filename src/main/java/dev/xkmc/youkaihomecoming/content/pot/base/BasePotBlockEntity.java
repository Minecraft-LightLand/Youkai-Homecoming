package dev.xkmc.youkaihomecoming.content.pot.base;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
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
import net.minecraft.world.inventory.RecipeHolder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import vectorwing.farmersdelight.common.block.CookingPotBlock;
import vectorwing.farmersdelight.common.block.entity.HeatableBlockEntity;
import vectorwing.farmersdelight.common.block.entity.SyncedBlockEntity;
import vectorwing.farmersdelight.common.block.entity.inventory.CookingPotItemHandler;
import vectorwing.farmersdelight.common.mixin.accessor.RecipeManagerAccessor;
import vectorwing.farmersdelight.common.registry.ModParticleTypes;
import vectorwing.farmersdelight.common.utility.ItemUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public abstract class BasePotBlockEntity extends SyncedBlockEntity
		implements MenuProvider, HeatableBlockEntity, Nameable, RecipeHolder, ContainerData {
	public static final int MEAL_DISPLAY_SLOT = 4;
	public static final int CONTAINER_SLOT = 5;
	public static final int OUTPUT_SLOT = 6;
	public static final int INVENTORY_SIZE = 7;
	private final ItemStackHandler inventory = createHandler();
	private final LazyOptional<IItemHandler> inputHandler = LazyOptional.of(() -> new CookingPotItemHandler(inventory, Direction.UP));
	private final LazyOptional<IItemHandler> outputHandler = LazyOptional.of(() -> new CookingPotItemHandler(inventory, Direction.DOWN));
	private int cookTime;
	private int cookTimeTotal;
	private ItemStack mealContainerStack;
	private Component customName;
	private final Object2IntOpenHashMap<ResourceLocation> usedRecipeTracker;
	private ResourceLocation lastRecipeID;
	private boolean checkNewRecipe;

	public BasePotBlockEntity(BlockEntityType<? extends BasePotBlockEntity> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		mealContainerStack = ItemStack.EMPTY;
		usedRecipeTracker = new Object2IntOpenHashMap<>();
		checkNewRecipe = true;
	}

	public abstract RecipeType<? extends BasePotRecipe> getRecipeType();

	public static ItemStack getMealFromItem(ItemStack pot) {
		if (pot.getItem() instanceof BasePotItem) {
			CompoundTag compound = pot.getTagElement("BlockEntityTag");
			if (compound != null) {
				CompoundTag inventoryTag = compound.getCompound("Inventory");
				if (inventoryTag.contains("Items", Tag.TAG_LIST)) {
					ItemStackHandler handler = new ItemStackHandler();
					handler.deserializeNBT(inventoryTag);
					return handler.getStackInSlot(MEAL_DISPLAY_SLOT);
				}
			}
		}
		return ItemStack.EMPTY;
	}

	public void load(CompoundTag compound) {
		super.load(compound);
		inventory.deserializeNBT(compound.getCompound("Inventory"));
		cookTime = compound.getInt("CookTime");
		cookTimeTotal = compound.getInt("CookTimeTotal");
		mealContainerStack = ItemStack.of(compound.getCompound("Container"));
		if (compound.contains("CustomName", Tag.TAG_STRING)) {
			customName = Serializer.fromJson(compound.getString("CustomName"));
		}

		CompoundTag compoundRecipes = compound.getCompound("RecipesUsed");

		for (String key : compoundRecipes.getAllKeys()) {
			usedRecipeTracker.put(new ResourceLocation(key), compoundRecipes.getInt(key));
		}

	}

	public void saveAdditional(CompoundTag compound) {
		super.saveAdditional(compound);
		compound.putInt("CookTime", cookTime);
		compound.putInt("CookTimeTotal", cookTimeTotal);
		compound.put("Container", mealContainerStack.serializeNBT());
		if (customName != null) {
			compound.putString("CustomName", Serializer.toJson(customName));
		}

		compound.put("Inventory", inventory.serializeNBT());
		CompoundTag recipe = new CompoundTag();
		usedRecipeTracker.forEach((recipeId, craftedAmount) ->
				recipe.putInt(recipeId.toString(), craftedAmount));
		compound.put("RecipesUsed", recipe);
	}

	protected CompoundTag writeItems(CompoundTag compound) {
		super.saveAdditional(compound);
		compound.put("Container", mealContainerStack.serializeNBT());
		compound.put("Inventory", inventory.serializeNBT());
		return compound;
	}

	public CompoundTag writeMeal(CompoundTag compound) {
		if (getMeal().isEmpty()) return compound;
		ItemStackHandler drops = new ItemStackHandler(INVENTORY_SIZE);
		for (int i = 0; i < INVENTORY_SIZE; ++i) {
			drops.setStackInSlot(i, i == MEAL_DISPLAY_SLOT ? inventory.getStackInSlot(i) : ItemStack.EMPTY);
		}
		if (customName != null) {
			compound.putString("CustomName", Serializer.toJson(customName));
		}
		compound.put("Container", mealContainerStack.serializeNBT());
		compound.put("Inventory", drops.serializeNBT());
		return compound;

	}

	public static void cookingTick(Level level, BlockPos pos, BlockState state, BasePotBlockEntity pot) {
		pot.cookingTick();
	}

	protected void cookingTick() {
		BlockPos pos = getBlockPos();
		if (level == null) return;
		boolean heated = isHeated(level, pos);
		boolean change = false;
		if (heated && hasInput()) {
			var recipe = getMatchingRecipe(new RecipeWrapper(inventory));
			if (recipe.isPresent() && canCook(recipe.get())) {
				change = processCooking(recipe.get());
			} else {
				cookTime = 0;
			}
		} else if (cookTime > 0) {
			cookTime = Mth.clamp(cookTime - 2, 0, cookTimeTotal);
		}

		ItemStack meal = getMeal();
		if (!meal.isEmpty()) {
			if (!doesMealHaveContainer(meal)) {
				moveMealToOutput();
				change = true;
			} else if (!inventory.getStackInSlot(CONTAINER_SLOT).isEmpty()) {
				useStoredContainersOnMeal();
				change = true;
			}
		}

		if (change) {
			inventoryChanged();
		}
	}

	public static void animationTick(Level level, BlockPos pos, BlockState state, BasePotBlockEntity pot) {
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

	private Optional<? extends BasePotRecipe> getMatchingRecipe(RecipeWrapper inventoryWrapper) {
		if (level == null)
			return Optional.empty();

		if (lastRecipeID != null) {
			Recipe<RecipeWrapper> recipe = ((RecipeManagerAccessor) level.getRecipeManager()).getRecipeMap(getRecipeType()).get(lastRecipeID);
			if (recipe instanceof BasePotRecipe) {
				if (recipe.matches(inventoryWrapper, level)) {
					return Optional.of((BasePotRecipe) recipe);
				}

				if (ItemStack.isSameItem(recipe.getResultItem(level.registryAccess()), getMeal())) {
					return Optional.empty();
				}
			}
		}

		if (checkNewRecipe) {
			Optional<? extends BasePotRecipe> recipe = level.getRecipeManager().getRecipeFor(getRecipeType(), inventoryWrapper, level);
			if (recipe.isPresent()) {
				ResourceLocation newRecipeID = (recipe.get()).getId();
				if (lastRecipeID != null && !lastRecipeID.equals(newRecipeID)) {
					cookTime = 0;
				}

				lastRecipeID = newRecipeID;
				return recipe;
			}
		}

		checkNewRecipe = false;
		return Optional.empty();
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

	protected boolean canCook(BasePotRecipe recipe) {
		if (level == null) return false;
		if (!hasInput()) return false;
		ItemStack resultStack = recipe.getResultItem(level.registryAccess());
		if (resultStack.isEmpty()) return false;
		ItemStack storedMealStack = inventory.getStackInSlot(MEAL_DISPLAY_SLOT);
		if (storedMealStack.isEmpty()) return true;
		if (!ItemStack.isSameItem(storedMealStack, resultStack)) return false;
		if (storedMealStack.getCount() + resultStack.getCount() <= inventory.getSlotLimit(MEAL_DISPLAY_SLOT))
			return true;
		return storedMealStack.getCount() + resultStack.getCount() <= resultStack.getMaxStackSize();
	}

	protected boolean processCooking(BasePotRecipe recipe) {
		if (level == null) return false;
		++cookTime;
		cookTimeTotal = recipe.getCookTime();
		if (cookTime < cookTimeTotal) return false;
		cookTime = 0;
		mealContainerStack = recipe.getOutputContainer();
		ItemStack resultStack = recipe.getResultItem(level.registryAccess());
		ItemStack storedMealStack = inventory.getStackInSlot(MEAL_DISPLAY_SLOT);
		if (storedMealStack.isEmpty()) {
			inventory.setStackInSlot(MEAL_DISPLAY_SLOT, resultStack.copy());
		} else if (ItemStack.isSameItem(storedMealStack, resultStack)) {
			storedMealStack.grow(resultStack.getCount());
		}

		setRecipeUsed(recipe);

		for (int i = 0; i < MEAL_DISPLAY_SLOT; ++i) {
			ItemStack slotStack = inventory.getStackInSlot(i);
			if (slotStack.hasCraftingRemainingItem()) {
				Direction direction = getBlockState().getValue(CookingPotBlock.FACING).getCounterClockWise();
				double x = worldPosition.getX() + 0.5 + direction.getStepX() * 0.25;
				double y = worldPosition.getY() + 0.7;
				double z = worldPosition.getZ() + 0.5 + direction.getStepZ() * 0.25;
				ItemUtils.spawnItemEntity(level, inventory.getStackInSlot(i).getCraftingRemainingItem(), x, y, z,
						direction.getStepX() * 0.08F, 0.25, direction.getStepZ() * 0.08F);
			}

			if (!slotStack.isEmpty()) {
				slotStack.shrink(1);
			}
		}

		return true;
	}

	public void setRecipeUsed(@Nullable Recipe<?> recipe) {
		if (recipe != null) {
			ResourceLocation recipeID = recipe.getId();
			usedRecipeTracker.addTo(recipeID, 1);
		}

	}

	@Nullable
	public Recipe<?> getRecipeUsed() {
		return null;
	}

	public void awardUsedRecipes(Player player, List<ItemStack> items) {
		List<Recipe<?>> usedRecipes = getUsedRecipesAndPopExperience(player.level(), player.position());
		player.awardRecipes(usedRecipes);
		usedRecipeTracker.clear();
	}

	public List<Recipe<?>> getUsedRecipesAndPopExperience(Level level, Vec3 pos) {
		List<Recipe<?>> list = Lists.newArrayList();
		for (Object2IntMap.Entry<ResourceLocation> ent : usedRecipeTracker.object2IntEntrySet()) {
			level.getRecipeManager().byKey(ent.getKey()).ifPresent((recipe) -> {
				list.add(recipe);
				splitAndSpawnExperience((ServerLevel) level, pos, ent.getIntValue(), ((BasePotRecipe) recipe).getExperience());
			});
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

	public void setCustomName(Component name) {
		customName = name;
	}

	public abstract AbstractContainerMenu createMenu(int id, Inventory player, Player entity);

	@Nonnull
	public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
		if (cap.equals(ForgeCapabilities.ITEM_HANDLER)) {
			return side != null && !side.equals(Direction.UP) ? outputHandler.cast() : inputHandler.cast();
		} else {
			return super.getCapability(cap, side);
		}
	}

	public void setRemoved() {
		super.setRemoved();
		inputHandler.invalidate();
		outputHandler.invalidate();
	}

	public CompoundTag getUpdateTag() {
		return writeItems(new CompoundTag());
	}

	private ItemStackHandler createHandler() {
		return new ItemStackHandler(INVENTORY_SIZE) {
			protected void onContentsChanged(int slot) {
				if (slot >= 0 && slot < MEAL_DISPLAY_SLOT) {
					BasePotBlockEntity.this.checkNewRecipe = true;
				}
				BasePotBlockEntity.this.inventoryChanged();
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
