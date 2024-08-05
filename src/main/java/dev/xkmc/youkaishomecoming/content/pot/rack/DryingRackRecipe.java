package dev.xkmc.youkaishomecoming.content.pot.rack;

import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class DryingRackRecipe extends AbstractCookingRecipe {

	public DryingRackRecipe(String group, CookingBookCategory category, Ingredient ingredient, ItemStack result, float experience, int cookingTime) {
		super(YHBlocks.RACK_RT.get(), group, category, ingredient, result, experience, cookingTime);
	}

	public ItemStack getToastSymbol() {
		return YHBlocks.RACK.asStack();
	}

	public RecipeSerializer<?> getSerializer() {
		return YHBlocks.RACK_RS.get();
	}
}