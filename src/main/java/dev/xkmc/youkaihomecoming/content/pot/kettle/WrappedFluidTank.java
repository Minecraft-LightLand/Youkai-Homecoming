package dev.xkmc.youkaihomecoming.content.pot.kettle;

import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

public record WrappedFluidTank(KettleBlockEntity be) implements IFluidHandler {

	@Override
	public int getTanks() {
		return 1;
	}

	@Override
	public @NotNull FluidStack getFluidInTank(int tank) {
		return new FluidStack(Fluids.WATER, be.getWater());
	}

	@Override
	public int getTankCapacity(int tank) {
		return KettleBlockEntity.WATER_BUCKET;
	}

	@Override
	public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
		return stack.getFluid() == Fluids.WATER;
	}

	@Override
	public int fill(FluidStack resource, FluidAction action) {
		if (!isFluidValid(0, resource)) return 0;
		int ans = Math.min(resource.getAmount(), getTankCapacity(0) - be.getWater());
		if (action.execute()) {
			be.addWater(ans);
		}
		return ans;
	}

	@Override
	public @NotNull FluidStack drain(FluidStack resource, FluidAction action) {
		if (!isFluidValid(0, resource)) return FluidStack.EMPTY;
		int ans = Math.min(resource.getAmount(), be.getWater());
		if (action.execute()) {
			be.addWater(-ans);
		}
		return new FluidStack(Fluids.WATER, ans);
	}

	@Override
	public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
		int ans = Math.min(maxDrain, be.getWater());
		if (action.execute()) {
			be.addWater(-ans);
		}
		return new FluidStack(Fluids.WATER, ans);
	}

}
