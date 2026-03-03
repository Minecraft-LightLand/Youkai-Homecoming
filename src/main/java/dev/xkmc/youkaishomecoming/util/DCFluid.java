package dev.xkmc.youkaishomecoming.util;

import net.neoforged.neoforge.fluids.FluidStack;

public final class DCFluid {
    private final FluidStack stack;
    private final int hashCode;

    public DCFluid(FluidStack stack) {
        this.stack = stack;
        this.hashCode = stack.hashCode();
    }

    public FluidStack stack() {
        return this.stack;
    }

    public int hashCode() {
        return this.hashCode;
    }

    public boolean equals(Object obj) {
        if (obj instanceof DCFluid s) {
			return this.hashCode == s.hashCode && this.stack.equals(s.stack);
        }
        return false;
    }
}
