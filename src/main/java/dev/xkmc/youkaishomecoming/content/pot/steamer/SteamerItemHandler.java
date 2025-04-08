package dev.xkmc.youkaishomecoming.content.pot.steamer;

import dev.xkmc.youkaishomecoming.content.item.food.FoodSaucerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;

public record SteamerItemHandler(
		SteamerBlockEntity be, Level level, RackData rack
) implements IItemHandlerModifiable {

	@Override
	public void setStackInSlot(int index, @NotNull ItemStack stack) {
		if (index < 0 || index >= 4) return;
		var item = rack.list[index];
		if (item == null) rack.list[index] = item = new RackItemData();
		item.setStack(be, stack);
	}

	@Override
	public int getSlots() {
		return 4;
	}

	@Override
	public @NotNull ItemStack getStackInSlot(int index) {
		if (index < 0 || index >= 4) return ItemStack.EMPTY;
		var item = rack.list[index];
		return item == null ? ItemStack.EMPTY : item.stack;
	}

	@Override
	public @NotNull ItemStack insertItem(int index, @NotNull ItemStack stack, boolean simulate) {
		if (index < 0 || index >= 4) return stack;
		if (stack.getItem() instanceof FoodSaucerItem) {
			if (index != 0) return stack;
			for (int i = 0; i < 4; i++) {
				if (rack.list[index] != null && !rack.list[index].stack.isEmpty())
					return stack;
			}
		}
		var item = rack.list[index];
		if (item == null) rack.list[index] = item = new RackItemData();
		if (!item.stack.isEmpty()) return stack;
		if (!simulate) {
			item.setStack(be, stack.copyWithCount(1));
		}
		return stack.copyWithCount(stack.getCount() - 1);
	}

	@Override
	public @NotNull ItemStack extractItem(int index, int amount, boolean simulate) {
		if (index < 0 || index >= 4 || amount <= 0) return ItemStack.EMPTY;
		var item = rack.list[index];
		if (item == null || item.stack.isEmpty()) return ItemStack.EMPTY;
		if (!item.mayExtract()) return ItemStack.EMPTY;
		ItemStack ans = item.stack.copy();
		if (!simulate) {
			item.setStack(be, ItemStack.EMPTY);
		}
		return ans;
	}

	@Override
	public int getSlotLimit(int slot) {
		return 1;
	}

	@Override
	public boolean isItemValid(int slot, @NotNull ItemStack stack) {
		return RackData.isValid(level, stack);
	}

}
