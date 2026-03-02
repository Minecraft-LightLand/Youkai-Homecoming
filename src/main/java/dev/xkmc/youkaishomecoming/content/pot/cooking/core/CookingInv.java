package dev.xkmc.youkaishomecoming.content.pot.cooking.core;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

import java.util.List;

public record CookingInv(Item container, List<ItemStack> list, boolean isComplete) implements RecipeInput {

	@Override
	public int size() {
		return list.size();
	}

	@Override
	public boolean isEmpty() {
		for (ItemStack stack : list) {
			if (!stack.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public ItemStack getItem(int i) {
		return list.get(i);
	}

}
