package dev.xkmc.youkaishomecoming.content.pot.table.recipe;

import dev.xkmc.youkaishomecoming.content.pot.table.item.VariantTableItemBase;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public class OrderedRecipeBuilder extends CuisineRecipeBuilder<
		OrderedCuisineRecipe, OrderedRecipeBuilder> {

	public OrderedRecipeBuilder(VariantTableItemBase base) {
		super(YHBlocks.CUISINE_ORDER.get(), base);
	}

	public OrderedRecipeBuilder(Item base) {
		super(YHBlocks.CUISINE_ORDER.get(), base);
	}

	public OrderedRecipeBuilder add(Ingredient item) {
		recipe.input.add(item);
		return this;
	}

	public OrderedRecipeBuilder add(ItemLike item) {
		recipe.input.add(Ingredient.of(item));
		return this;
	}

	public OrderedRecipeBuilder add(TagKey<Item> item) {
		recipe.input.add(Ingredient.of(item));
		return this;
	}

}
