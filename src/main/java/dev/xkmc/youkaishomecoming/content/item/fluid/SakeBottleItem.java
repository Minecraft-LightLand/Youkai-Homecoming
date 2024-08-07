package dev.xkmc.youkaishomecoming.content.item.fluid;

import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class SakeBottleItem extends Item {

	public SakeBottleItem(Supplier<SakeFluid> supplier, Item.Properties builder) {
		super(builder);
		this.fluidSupplier = supplier;
	}

	private final Supplier<SakeFluid> fluidSupplier;

	public SakeFluid getFluid() {
		return fluidSupplier.get();
	}

}
