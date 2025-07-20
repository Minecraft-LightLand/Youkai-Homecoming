package dev.xkmc.youkaishomecoming.content.pot.cooking.core;

import dev.xkmc.youkaishomecoming.init.food.YHBowl;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public class UnorderedPotRecipeBuilder extends CookingRecipeBuilder<UnorderedCookingRecipe, UnorderedPotRecipeBuilder> {

	public UnorderedPotRecipeBuilder(ItemLike base, int time) {
		super(YHBlocks.COOKING_UNORDER.get(), base, time);
	}

	public UnorderedPotRecipeBuilder add(Ingredient item) {
		recipe.input.add(item);
		return this;
	}

	public UnorderedPotRecipeBuilder add(ItemLike item) {
		recipe.input.add(Ingredient.of(item));
		return this;
	}

	public UnorderedPotRecipeBuilder add(TagKey<Item> item) {
		recipe.input.add(Ingredient.of(item));
		return this;
	}

}
