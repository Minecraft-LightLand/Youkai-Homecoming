package dev.xkmc.youkaishomecoming.content.item.fluid;

import dev.xkmc.youkaishomecoming.content.item.food.YHDrinkItem;
import dev.xkmc.youkaishomecoming.content.item.food.YHFoodItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import vectorwing.farmersdelight.common.Configuration;

import java.util.List;
import java.util.function.Supplier;

public class SakeBottleItem extends YHDrinkItem {

	public SakeBottleItem(Supplier<SakeFluid> supplier, Item.Properties builder) {
		super(builder);
		this.fluidSupplier = supplier;
	}

	private final Supplier<SakeFluid> fluidSupplier;

	public SakeFluid getFluid() {
		return fluidSupplier.get();
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext level, List<Component> list, TooltipFlag flag) {
		if (Configuration.FOOD_EFFECT_TOOLTIP.get())
			YHFoodItem.getFoodEffects(stack, list);
	}

}
