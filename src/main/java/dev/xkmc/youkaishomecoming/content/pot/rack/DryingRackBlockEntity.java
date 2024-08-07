package dev.xkmc.youkaishomecoming.content.pot.rack;

import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.block.entity.SyncedBlockEntity;

import java.util.Optional;

public class DryingRackBlockEntity extends SyncedBlockEntity {

	private static final int NUM_SLOTS = 4;
	private final NonNullList<ItemStack> list = NonNullList.withSize(NUM_SLOTS, ItemStack.EMPTY);
	private final ItemStackHandler items = new ItemStackHandler(list);
	private final DryingRackWrapper handler = new DryingRackWrapper(this);
	private final int[] cookingProgress = new int[NUM_SLOTS];
	private final int[] cookingTime = new int[NUM_SLOTS];
	private final RecipeManager.CachedCheck<SingleRecipeInput, DryingRackRecipe> quickCheck = RecipeManager.createCheck(YHBlocks.RACK_RT.get());

	public DryingRackBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public static void cookTick(Level level, BlockPos pos, BlockState state, DryingRackBlockEntity be) {
		if (!level.canSeeSky(pos)) return;
		if (!level.isDay()) return;
		if (level.isRainingAt(pos)) return;
		be.cookTick();
	}

	private void cookTick() {
		if (level == null) return;
		BlockPos pos = getBlockPos();
		for (int i = 0; i < items.getSlots(); ++i) {
			ItemStack itemstack = items.getStackInSlot(i);
			if (!itemstack.isEmpty()) {
				cookingProgress[i]++;
				if (cookingProgress[i] >= cookingTime[i]) {
					SingleRecipeInput container = new SingleRecipeInput(itemstack);
					ItemStack result = quickCheck.getRecipeFor(container, level)
							.map((r) -> r.value().assemble(container, level.registryAccess())).orElse(itemstack);
					if (result.isItemEnabled(level.enabledFeatures())) {
						Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), result);
						items.setStackInSlot(i, ItemStack.EMPTY);
						inventoryChanged();
						level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(getBlockState()));
					}
				}
			}
		}
	}

	public NonNullList<ItemStack> getItems() {
		return list;
	}

	public void loadAdditional(CompoundTag pTag, HolderLookup.Provider pvd) {
		super.loadAdditional(pTag, pvd);
		list.clear();
		ContainerHelper.loadAllItems(pTag, list, pvd);
		if (pTag.contains("CookingTimes", 11)) {
			int[] times = pTag.getIntArray("CookingTimes");
			System.arraycopy(times, 0, cookingProgress, 0, Math.min(cookingTime.length, times.length));
		}

		if (pTag.contains("CookingTotalTimes", 11)) {
			int[] times = pTag.getIntArray("CookingTotalTimes");
			System.arraycopy(times, 0, cookingTime, 0, Math.min(cookingTime.length, times.length));
		}

	}

	protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pvd) {
		super.saveAdditional(pTag, pvd);
		ContainerHelper.saveAllItems(pTag, list, true, pvd);
		pTag.putIntArray("CookingTimes", cookingProgress);
		pTag.putIntArray("CookingTotalTimes", cookingTime);
	}

	public CompoundTag getUpdateTag(HolderLookup.Provider pvd) {
		CompoundTag compoundtag = new CompoundTag();
		ContainerHelper.saveAllItems(compoundtag, list, true, pvd);
		return compoundtag;
	}

	public Optional<RecipeHolder<DryingRackRecipe>> getCookableRecipe(ItemStack pStack) {
		assert level != null;
		return list.stream().noneMatch(ItemStack::isEmpty) ? Optional.empty() :
				quickCheck.getRecipeFor(new SingleRecipeInput(pStack), level);
	}

	public boolean placeFood(ItemStack stack, int time) {
		assert level != null;
		for (int i = 0; i < items.getSlots(); ++i) {
			ItemStack itemstack = items.getStackInSlot(i);
			if (itemstack.isEmpty()) {
				cookingTime[i] = time;
				cookingProgress[i] = 0;
				items.setStackInSlot(i, stack.split(1));
				inventoryChanged();
				return true;
			}
		}
		return false;
	}

	public ItemStackHandler getInventory() {
		return items;
	}

	@Nullable
	public IItemHandler getItemHandler(@Nullable Direction dire) {
		return dire == Direction.DOWN ? null : handler;
	}
}