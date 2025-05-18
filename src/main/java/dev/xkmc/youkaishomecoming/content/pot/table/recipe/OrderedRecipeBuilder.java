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

public class OrderedRecipeBuilder extends BaseRecipeBuilder<
		OrderedRecipeBuilder, OrderedCuisineRecipe, CuisineRecipe<?>, CuisineInv> {

	public OrderedRecipeBuilder(VariantTableItemBase base, ItemStack result) {
		super(YHBlocks.CUISINE_ORDER.get());
		recipe.base = base.id();
		recipe.result = result;
	}

	public OrderedRecipeBuilder(VariantTableItemBase base, ItemLike result, int count) {
		this(base, new ItemStack(result, count));
	}

	public OrderedRecipeBuilder(VariantTableItemBase base, ItemLike result) {
		this(base, new ItemStack(result));
	}

	public OrderedRecipeBuilder add(Ingredient item) {
		recipe.input.add(item);
		return this;
	}

	public OrderedRecipeBuilder add(Item item) {
		recipe.input.add(Ingredient.of(item));
		return this;
	}

	public OrderedRecipeBuilder add(TagKey<Item> item) {
		recipe.input.add(Ingredient.of(item));
		return this;
	}

	public void save(RegistrateRecipeProvider pvd) {
		var item = recipe.result.getItem();
		var path = item.builtInRegistryHolder().unwrapKey().orElseThrow().location();
		save(pvd, pvd.safeId(path));
	}

}
