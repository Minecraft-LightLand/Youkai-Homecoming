package dev.xkmc.youkaishomecoming.content.item.fluid;

import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
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

	public boolean canFillFluidType(FluidStack fluid) {
		return fluid.getFluid() instanceof YHFluid;
	}

	@NotNull
	public FluidStack getFluid() {
		if (container.getItem() instanceof BucketBottleItem bucket) {
			return new FluidStack(bucket.fluid.source(), getTankCapacity(0));
		}
		var root = container.getTag();
		if (root == null || !root.contains("SakeFluid", Tag.TAG_COMPOUND)) return FluidStack.EMPTY;
		return FluidStack.loadFluidStackFromNBT(root.getCompound("SakeFluid"));
	}

	protected void setFluid(@NotNull FluidStack fluidStack) {
		if (fluidStack.getAmount() == getTankCapacity(0)) {
			if (fluidStack.getFluid() instanceof YHFluid fluid) {
				if (fluid.type.bottleSet() instanceof BottledDrinkSet set) {
					container = set.bottle.asStack();
					return;
				}
			}
		}
		if (fluidStack.isEmpty()) {
			if (container.getTag() == null) {
				container = YHItems.SAKE_BOTTLE.asStack();
				return;
			}
			container.getOrCreateTag().remove("SakeFluid");
			if (container.getOrCreateTag().isEmpty()) {
				container.setTag(null);
			}
		}
		if (!container.is(YHItems.SAKE_BOTTLE.get())) {
			container = YHItems.SAKE_BOTTLE.asStack();
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
		if (stack.isEmpty()) return true;
		if (!(stack.getFluid() instanceof YHFluid fluid)) return false;
		if (fluid.type instanceof BottledFluid<?> bottle) {
			return bottle.bottleSet() != null;
		}
		return fluid.type.asStack(1).getFoodProperties(null) != null;
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
