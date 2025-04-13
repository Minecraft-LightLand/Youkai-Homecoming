package dev.xkmc.youkaishomecoming.content.item.fluid;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SlipFluidWrapper implements IFluidHandlerItem, ICapabilityProvider {

	private final LazyOptional<IFluidHandlerItem> holder = LazyOptional.of(() -> this);

	protected ItemStack container;

	public SlipFluidWrapper(ItemStack container) {
		this.container = container;
	}

	@NotNull
	@Override
	public ItemStack getContainer() {
		return container;
	}

	public boolean canFillFluidType(FluidStack fluid) {
		return fluid.getFluid() instanceof SakeFluid;
	}

	@NotNull
	public FluidStack getFluid() {
		var root = container.getTag();
		if (root == null || !root.contains("SakeFluid", Tag.TAG_COMPOUND)) return FluidStack.EMPTY;
		return FluidStack.loadFluidStackFromNBT(root.getCompound("SakeFluid"));
	}

	protected void setFluid(@NotNull FluidStack fluidStack) {
		if (fluidStack.isEmpty()) {
			if (container.getTag() == null) return;
			container.getOrCreateTag().remove("SakeFluid");
			if (container.getOrCreateTag().isEmpty()) {
				container.setTag(null);
			}
		}
		container.getOrCreateTag().put("SakeFluid", fluidStack.writeToNBT(new CompoundTag()));
	}

	@Override
	public int getTanks() {
		return 1;
	}

	@NotNull
	@Override
	public FluidStack getFluidInTank(int tank) {
		return getFluid();
	}

	@Override
	public int getTankCapacity(int tank) {
		return 1000;
	}

	@Override
	public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
		return stack.isEmpty() || stack.getFluid() instanceof SakeFluid;
	}

	@Override
	public int fill(FluidStack resource, FluidAction action) {
		if (resource.isEmpty()) return 0;
		var current = getFluid();
		if (current.isEmpty() || current.isFluidEqual(resource)) {
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
		if (current.isFluidEqual(resource)) {
			return drain(resource.getAmount(), action);
		}
		return FluidStack.EMPTY;
	}

	@NotNull
	@Override
	public FluidStack drain(int maxDrain, FluidAction action) {
		var current = getFluid();
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

	@Override
	@NotNull
	public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction facing) {
		return ForgeCapabilities.FLUID_HANDLER_ITEM.orEmpty(capability, holder);
	}

}
