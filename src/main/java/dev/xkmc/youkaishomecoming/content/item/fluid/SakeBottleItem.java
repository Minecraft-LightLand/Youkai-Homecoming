package dev.xkmc.youkaishomecoming.content.item.fluid;

import dev.xkmc.youkaishomecoming.content.item.food.YHDrinkItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class SakeBottleItem extends YHDrinkItem {

	public SakeBottleItem(Supplier<SakeFluid> supplier, Item.Properties builder) {
		super(builder);
		this.fluidSupplier = supplier;
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
		if (this instanceof SakeBottleItem)
			return new SakeFluidWrapper(stack);
		else return super.initCapabilities(stack, nbt);
	}

	private final Supplier<SakeFluid> fluidSupplier;

	public SakeFluid getFluid() {
		return fluidSupplier.get();
	}

}
