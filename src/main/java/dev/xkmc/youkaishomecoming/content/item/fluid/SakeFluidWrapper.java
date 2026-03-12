package dev.xkmc.youkaishomecoming.content.item.fluid;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SakeFluidWrapper implements IFluidHandlerItem {

	public static final List<Item> LIST = new ArrayList<>();

	public static synchronized void add(Item item) {
		LIST.add(item);
	}

	protected ItemStack container;

	public SakeFluidWrapper(ItemStack container) {
		this.container = container;
	}

	public SakeFluidWrapper(ItemStack container, @Nullable Void v) {
		this.container = container;
	}

	@NotNull
	@Override
	public ItemStack getContainer() {
		return container;
	}

	public boolean canFillFluidType(FluidStack fluid) {
		return fluid.getFluid() instanceof YHFluid sake &&
				sake.type.getContainer() == container.getItem();
	}

	@NotNull
	public FluidStack getFluid() {
		Item item = container.getItem();
		if (item instanceof SakeBottleItem sake)
			return new FluidStack(sake.getFluid(), sake.getFluid().type.amount());
		return FluidStack.EMPTY;
	}

	protected void setFluid(@NotNull FluidStack fluidStack) {
		if (fluidStack.isEmpty()) {
			container = container.getCraftingRemainingItem();
		} else if (fluidStack.getFluid() instanceof YHFluid sake) {
			container = sake.type.toStack(fluidStack);
		}
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
		return 250;
	}

	@Override
	public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
		return stack.isEmpty() || stack.getFluid() instanceof YHFluid sake && sake.type.getContainer() == container.getItem();
	}

	@Override
	public int fill(FluidStack resource, FluidAction action) {
		int cap = getTankCapacity(0);
		if (resource.getAmount() < cap ||
				!getFluid().isEmpty() ||
				!canFillFluidType(resource)) {
			return 0;
		}

		if (action.execute()) {
			setFluid(resource);
		}

		return cap;
	}

	@NotNull
	@Override
	public FluidStack drain(FluidStack resource, FluidAction action) {
		int cap = getTankCapacity(0);
		if (container.getCount() != 1 || resource.getAmount() < cap) {
			return FluidStack.EMPTY;
		}
		FluidStack stack = getFluid();
		if (!stack.isEmpty() && FluidStack.isSameFluidSameComponents(stack, resource)) {
			if (action.execute()) {
				setFluid(FluidStack.EMPTY);
			}
			return stack;
		}

		return FluidStack.EMPTY;
	}

	@NotNull
	@Override
	public FluidStack drain(int maxDrain, FluidAction action) {
		int cap = getTankCapacity(0);
		if (container.getCount() != 1 || maxDrain < cap) {
			return FluidStack.EMPTY;
		}
		FluidStack fluidStack = getFluid();
		if (!fluidStack.isEmpty()) {
			if (action.execute()) {
				setFluid(FluidStack.EMPTY);
			}
			return fluidStack;
		}

		return FluidStack.EMPTY;
	}

}
