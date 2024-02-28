package dev.xkmc.youkaishomecoming.content.pot.rack;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;

public record DryingRackWrapper(DryingRackBlockEntity be) implements IItemHandlerModifiable {

	@Override
	public void setStackInSlot(int slot, @NotNull ItemStack stack) {
		be.getInventory().setStackInSlot(slot, stack);
	}

	@Override
	public int getSlots() {
		return be.getInventory().getSlots();
	}

	@Override
	public @NotNull ItemStack getStackInSlot(int slot) {
		return be.getInventory().getStackInSlot(slot);
	}

	@Override
	public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
		var r = be.getCookableRecipe(stack);
		if (r.isPresent()) {
			ItemStack ans = stack.copy();
			int empty = 0;
			for (int i = 0; i < be.getInventory().getSlots(); i++) {
				if (be.getInventory().getStackInSlot(i).isEmpty()) {
					empty++;
				}
			}
			if (empty == 0) return stack;
			if (simulate) {
				ans.split(1);
			} else {
				be.placeFood(ans, r.get().getCookingTime());
			}
			return ans;
		}
		return stack;
	}

	@Override
	public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
		return ItemStack.EMPTY;
	}

	@Override
	public int getSlotLimit(int slot) {
		return 1;
	}

	@Override
	public boolean isItemValid(int slot, @NotNull ItemStack stack) {
		return be.getCookableRecipe(stack).isPresent();
	}
}
