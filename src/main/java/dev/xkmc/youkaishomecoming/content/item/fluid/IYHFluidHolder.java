package dev.xkmc.youkaishomecoming.content.item.fluid;

import com.tterrag.registrate.util.entry.FluidEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public interface IYHFluidHolder {

	int getColor();

	Item getContainer();

	ItemStack asStack(int count);

	ItemEntry<?> item();

	FluidEntry<? extends YHFluid> fluid();

	default int count() {
		return 4;
	}

	default int amount() {
		return 250;
	}

}
