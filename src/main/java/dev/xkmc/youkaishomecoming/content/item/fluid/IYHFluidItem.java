package dev.xkmc.youkaishomecoming.content.item.fluid;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

public interface IYHFluidItem {

	int getColor();

	Item getContainer();

	ItemStack asStack(int count);

	default ItemStack toStack(FluidStack fluid) {
		return asStack(fluid.getAmount() / amount());
	}

	default int count() {
		return 4;
	}

	default int amount() {
		return 250;
	}

}
