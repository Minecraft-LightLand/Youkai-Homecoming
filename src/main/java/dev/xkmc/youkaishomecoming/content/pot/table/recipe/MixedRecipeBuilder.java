package dev.xkmc.youkaishomecoming.content.pot.table.recipe;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import dev.xkmc.l2library.serial.recipe.BaseRecipeBuilder;
import dev.xkmc.youkaishomecoming.content.pot.table.item.VariantTableItemBase;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public class MixedRecipeBuilder extends BaseRecipeBuilder<
		MixedRecipeBuilder, MixedCuisineRecipe, CuisineRecipe<?>, CuisineInv> {

	public MixedRecipeBuilder(VariantTableItemBase base, ItemStack result) {
		super(YHBlocks.CUISINE_MIXED.get());
		recipe.base = base.id();
		recipe.result = result;
	}

	public MixedRecipeBuilder(VariantTableItemBase base, ItemLike result, int count) {
		this(base, new ItemStack(result, count));
	}

	public MixedRecipeBuilder(VariantTableItemBase base, ItemLike result) {
		this(base, new ItemStack(result));
	}

	public MixedRecipeBuilder addSauce(Ingredient item) {
		recipe.sauce.add(item);
		return this;
	}

	public MixedRecipeBuilder addSauce(Item item) {
		recipe.sauce.add(Ingredient.of(item));
		return this;
	}

	public MixedRecipeBuilder addSauce(TagKey<Item> item) {
		recipe.sauce.add(Ingredient.of(item));
		return this;
	}

	public MixedRecipeBuilder addIngredient(Ingredient item) {
		recipe.ingredient.add(item);
		return this;
	}

	public MixedRecipeBuilder addIngredient(Item item) {
		recipe.ingredient.add(Ingredient.of(item));
		return this;
	}

	public MixedRecipeBuilder addIngredient(TagKey<Item> item) {
		recipe.ingredient.add(Ingredient.of(item));
		return this;
	}

	public void save(RegistrateRecipeProvider pvd) {
		var item = recipe.result.getItem();
		var path = item.builtInRegistryHolder().unwrapKey().orElseThrow().location();
		save(pvd, pvd.safeId(path));
	}

}
