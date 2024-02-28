package dev.xkmc.youkaishomecoming.content.pot.rack;

import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
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
	private final RecipeManager.CachedCheck<Container, DryingRackRecipe> quickCheck = RecipeManager.createCheck(YHBlocks.RACK_RT.get());

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
					Container container = new SimpleContainer(itemstack);
					ItemStack result = quickCheck.getRecipeFor(container, level)
							.map((r) -> r.assemble(container, level.registryAccess())).orElse(itemstack);
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

	public void load(CompoundTag pTag) {
		super.load(pTag);
		list.clear();
		ContainerHelper.loadAllItems(pTag, list);
		if (pTag.contains("CookingTimes", 11)) {
			int[] times = pTag.getIntArray("CookingTimes");
			System.arraycopy(times, 0, cookingProgress, 0, Math.min(cookingTime.length, times.length));
		}

		if (pTag.contains("CookingTotalTimes", 11)) {
			int[] times = pTag.getIntArray("CookingTotalTimes");
			System.arraycopy(times, 0, cookingTime, 0, Math.min(cookingTime.length, times.length));
		}

	}

	protected void saveAdditional(CompoundTag pTag) {
		super.saveAdditional(pTag);
		ContainerHelper.saveAllItems(pTag, list, true);
		pTag.putIntArray("CookingTimes", cookingProgress);
		pTag.putIntArray("CookingTotalTimes", cookingTime);
	}

	public CompoundTag getUpdateTag() {
		CompoundTag compoundtag = new CompoundTag();
		ContainerHelper.saveAllItems(compoundtag, list, true);
		return compoundtag;
	}

	public Optional<DryingRackRecipe> getCookableRecipe(ItemStack pStack) {
		assert level != null;
		return list.stream().noneMatch(ItemStack::isEmpty) ? Optional.empty() :
				quickCheck.getRecipeFor(new SimpleContainer(pStack), level);
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

	@Override
	public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction dire) {
		if (cap == ForgeCapabilities.ITEM_HANDLER) {
			if (dire == Direction.DOWN)
				return LazyOptional.empty();
			return LazyOptional.of(() -> handler).cast();
		}
		return super.getCapability(cap);
	}

}