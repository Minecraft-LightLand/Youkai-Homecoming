package dev.xkmc.youkaishomecoming.content.item.fluid;

import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import org.jetbrains.annotations.NotNull;

public class SlipFluidWrapper implements IFluidHandlerItem {

	protected ItemStack container;

	public SlipFluidWrapper(ItemStack container) {
		this.container = container;
	}

	@NotNull
	@Override
	public ItemStack getContainer() {
		return container;
	}

	@NotNull
	public FluidStack getFluid() {
		var ans = YHItems.FLUID.get(container);
		if (ans == null) return FluidStack.EMPTY;
		return ans.fluid().copy();
	}

	protected void setFluid(@NotNull FluidStack fluidStack) {
		if (fluidStack.isEmpty()) {
			container.remove(YHItems.FLUID);
		} else {
			YHItems.FLUID.set(container, new FluidWrapper(fluidStack));
		}
	}

	@Override
	public int getTanks() {
		return 1;
	}

	@NotNull
	@Override
	public FluidStack getFluidInTank(int tank) {
		return getFluid().copy();
	}

	@Override
	public int getTankCapacity(int tank) {
		return 1000;
	}

	@Override
	public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
		return stack.isEmpty() || stack.getFluid() instanceof YHFluid;
	}

	@Override
	public int fill(FluidStack resource, FluidAction action) {
		if (resource.isEmpty()) return 0;
		var current = getFluid();
		if (current.isEmpty() || FluidStack.isSameFluidSameComponents(current, resource)) {
			int toFill = Math.min(getTankCapacity(0) - current.getAmount(), resource.getAmount());
			if (action.execute()) {
				var copy = resource.copy();
				copy.setAmount(toFill + current.getAmount());
				setFluid(copy);
			}
			return toFill;
		}
		return 0;
	}

	@NotNull
	@Override
	public FluidStack drain(FluidStack resource, FluidAction action) {
		if (resource.isEmpty()) return FluidStack.EMPTY;
		var current = getFluid();
		if (current.isEmpty()) return FluidStack.EMPTY;
		if (FluidStack.isSameFluidSameComponents(current, resource)) {
			return drain(resource.getAmount(), action);
		}
		return FluidStack.EMPTY;
	}

	@NotNull
	@Override
	public FluidStack drain(int maxDrain, FluidAction action) {
		var current = getFluid();
		if (current.isEmpty()) return FluidStack.EMPTY;
		int toDrain = Math.min(current.getAmount(), maxDrain);
		var ans = current.copy();
		ans.setAmount(toDrain);
		if (action.execute()) {
			var copy = current.copy();
			copy.setAmount(current.getAmount() - toDrain);
			setFluid(copy);
		}
		return ans;
	}

}
