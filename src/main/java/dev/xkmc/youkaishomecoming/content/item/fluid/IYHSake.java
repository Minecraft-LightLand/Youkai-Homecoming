package dev.xkmc.youkaishomecoming.content.item.fluid;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public interface IYHSake {

	int getColor();

	int count();

	Item getContainer();

	ItemStack asStack(int count);

	int amount();

}
