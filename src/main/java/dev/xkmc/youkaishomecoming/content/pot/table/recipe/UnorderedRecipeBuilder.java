package dev.xkmc.youkaishomecoming.content.pot.table.recipe;

import dev.xkmc.youkaishomecoming.content.pot.table.item.VariantTableItemBase;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;

public class UnorderedRecipeBuilder extends CuisineRecipeBuilder<UnorderedCuisineRecipe, UnorderedRecipeBuilder> {

	public UnorderedRecipeBuilder(VariantTableItemBase base) {
		super(YHBlocks.CUISINE_UNORDER.get(), base);
	}

	public UnorderedRecipeBuilder(Item base) {
		super(YHBlocks.CUISINE_UNORDER.get(), base);
	}

	public UnorderedRecipeBuilder add(Ingredient item) {
		recipe.input.add(item);
		return this;
	}

	public UnorderedRecipeBuilder add(Item item) {
		recipe.input.add(Ingredient.of(item));
		return this;
	}

	public UnorderedRecipeBuilder add(TagKey<Item> item) {
		recipe.input.add(Ingredient.of(item));
		return this;
	}

}
