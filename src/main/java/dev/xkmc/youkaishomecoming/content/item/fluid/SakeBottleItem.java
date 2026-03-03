package dev.xkmc.youkaishomecoming.content.item.fluid;

import dev.xkmc.youkaishomecoming.content.item.food.YHDrinkItem;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class SakeBottleItem extends YHDrinkItem {

	public SakeBottleItem(Supplier<YHFluid> supplier, Item.Properties builder) {
		super(builder);
		this.fluidSupplier = supplier;
		SakeFluidWrapper.add(this);
	}

	private final Supplier<YHFluid> fluidSupplier;

	public YHFluid getFluid() {
		return fluidSupplier.get();
	}

}
