package dev.xkmc.youkaishomecoming.content.pot.table.recipe;

import dev.xkmc.youkaishomecoming.content.pot.table.item.VariantTableItemBase;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;

public class MixedRecipeBuilder extends CuisineRecipeBuilder<MixedCuisineRecipe, MixedRecipeBuilder> {

	public MixedRecipeBuilder(VariantTableItemBase base) {
		super(YHBlocks.CUISINE_MIXED.get(), base);
	}

	public MixedRecipeBuilder(Item base) {
		super(YHBlocks.CUISINE_MIXED.get(), base);
	}

	public MixedRecipeBuilder addOrdered(Ingredient item) {
		recipe.first.add(item);
		return this;
	}

	public MixedRecipeBuilder addOrdered(Item item) {
		recipe.first.add(Ingredient.of(item));
		return this;
	}

	public MixedRecipeBuilder addOrdered(TagKey<Item> item) {
		recipe.first.add(Ingredient.of(item));
		return this;
	}

	public MixedRecipeBuilder addUnordered(Ingredient item) {
		recipe.second.add(item);
		return this;
	}

	public MixedRecipeBuilder addUnordered(Item item) {
		recipe.second.add(Ingredient.of(item));
		return this;
	}

	public MixedRecipeBuilder addUnordered(TagKey<Item> item) {
		recipe.second.add(Ingredient.of(item));
		return this;
	}

}
