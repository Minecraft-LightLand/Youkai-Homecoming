package dev.xkmc.youkaishomecoming.content.pot.table.recipe;

import dev.xkmc.l2library.serial.recipe.BaseRecipeBuilder;
import dev.xkmc.youkaishomecoming.content.pot.table.item.VariantTableItemBase;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public class UnorderedRecipeBuilder extends BaseRecipeBuilder<
		UnorderedRecipeBuilder, UnorderedCuisineRecipe, CuisineRecipe<?>, CuisineInv> {

	public UnorderedRecipeBuilder(VariantTableItemBase base, ItemStack result) {
		super(YHBlocks.CUISINE_UNORDER.get());
		recipe.base = base.id();
		recipe.result = result;
	}

	public UnorderedRecipeBuilder(VariantTableItemBase base, ItemLike result, int count) {
		this(base, new ItemStack(result, count));
	}

	public UnorderedRecipeBuilder(VariantTableItemBase base, ItemLike result) {
		this(base, new ItemStack(result));
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
