package dev.xkmc.youkaishomecoming.content.pot.table.recipe;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import dev.xkmc.l2library.serial.recipe.BaseRecipe;
import dev.xkmc.l2library.serial.recipe.BaseRecipeBuilder;
import dev.xkmc.l2serial.util.Wrappers;
import dev.xkmc.youkaishomecoming.content.pot.table.food.FoodModelHelper;
import dev.xkmc.youkaishomecoming.content.pot.table.food.YHRolls;
import dev.xkmc.youkaishomecoming.content.pot.table.item.VariantTableItemBase;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public abstract class CuisineRecipeBuilder<T extends BaseCuisineRecipe<T>, B extends CuisineRecipeBuilder<T, B>>
		extends BaseRecipeBuilder<B, T, CuisineRecipe<?>, CuisineInv> {

	public CuisineRecipeBuilder(BaseRecipe.RecType<T, CuisineRecipe<?>, CuisineInv> type, VariantTableItemBase base) {
		super(type);
		recipe.base = base.id();
	}

	public CuisineRecipeBuilder(BaseRecipe.RecType<T, CuisineRecipe<?>, CuisineInv> type, Item base) {
		super(type);
		if (FoodModelHelper.find(base.getDefaultInstance()) == null)
			throw new IllegalStateException("Base item must correspond to a model");
		recipe.base = base.builtInRegistryHolder().unwrapKey().orElseThrow().location();
	}

	public B result(ItemStack stack) {
		recipe.result = stack;
		return Wrappers.cast(this);
	}

	public B result(ItemLike item, int count) {
		return result(new ItemStack(item, count));
	}

	public B result(ItemLike item) {
		return result(item, 1);
	}

	public B result(YHRolls maki) {
		return result(maki.item);
	}

	public void save(RegistrateRecipeProvider pvd) {
		var item = recipe.result.getItem();
		var path = item.builtInRegistryHolder().unwrapKey().orElseThrow().location();
		save(pvd, pvd.safeId(path));
	}

}
