package dev.xkmc.youkaishomecoming.content.pot.table.recipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

import java.util.List;

public record CuisineInv(
		ResourceLocation base, List<ItemStack> list, int start, boolean isComplete
) implements RecipeInput {

	@Override
	public int size() {
		return list.size() - start;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public ItemStack getItem(int i) {
		return list.get(i + start);
	}

}
