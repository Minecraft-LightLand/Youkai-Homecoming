package dev.xkmc.youkaishomecoming.content.item.fluid;

import net.neoforged.neoforge.fluids.FluidStack;

public class FluidWrapper {

	private final FluidStack stack;
	private final int hashcode;

	public FluidWrapper(FluidStack stack) {
		this.stack = stack;
		this.hashcode = stack.hashCode();
	}

	public FluidStack fluid() {
		return stack;
	}

	@Override
	public int hashCode() {
		return hashcode;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof FluidWrapper other)) return false;
		return FluidStack.matches(stack, other.stack);
	}

}
