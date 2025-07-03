package dev.xkmc.youkaishomecoming.content.pot.table.item;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class SearHelper {

	public static boolean isFireSource(ItemStack stack) {
		return stack.is(Items.FLINT_AND_STEEL);
	}
}
