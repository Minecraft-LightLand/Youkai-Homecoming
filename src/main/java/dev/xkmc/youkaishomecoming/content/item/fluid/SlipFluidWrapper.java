package dev.xkmc.youkaishomecoming.content.item.fluid;

import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
import dev.xkmc.youkaishomecoming.util.DCFluid;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SlipFluidWrapper implements IFluidHandlerItem {

	public static final List<Item> LIST = new ArrayList<>();

	public static synchronized void add(Item item) {
		LIST.add(item);
	}

	protected ItemStack container;

	public SlipFluidWrapper(ItemStack container) {
		this.container = container;
	}

	public SlipFluidWrapper(ItemStack container, @Nullable Void v) {
		this.container = container;
	}


	@NotNull
	@Override
	public ItemStack getContainer() {
		return container;
	}

	@NotNull
	public FluidStack getFluid() {
		if (container.getItem() instanceof BucketBottleItem bucket) {
			return new FluidStack(bucket.fluid.source(), getTankCapacity(0));
		}
		var ans = container.get(YHItems.DC_FLUID);
		return ans == null ? FluidStack.EMPTY : ans.stack().copy();
	}

	protected void setFluid(@NotNull FluidStack fluidStack) {
		if (fluidStack.getAmount() == getTankCapacity(0)) {
			if (YHFluidHandler.of(fluidStack) instanceof IYHFluidBottled fluid) {
				if (fluid.bottleSet() instanceof BottledDrinkSet set) {
					container = set.bottle.asStack();
					return;
				}
			}
		}
		if (fluidStack.isEmpty()) {
			var ans = container.get(YHItems.DC_FLUID);
			if (ans == null) {
				container = YHItems.SAKE_BOTTLE.asStack();
				return;
			}
			container.remove(YHItems.DC_FLUID);
		}
		if (!container.is(YHItems.SAKE_BOTTLE.get())) {
			container = YHItems.SAKE_BOTTLE.asStack();
		}
		container.set(YHItems.DC_FLUID, new DCFluid(fluidStack.copy()));
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
		return true;
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
