package dev.xkmc.youkaishomecoming.content.pot.cooking.core;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import dev.xkmc.l2core.serial.recipe.BaseRecipe;
import dev.xkmc.l2core.serial.recipe.BaseRecipeBuilder;
import net.minecraft.world.level.ItemLike;

public abstract class CookingRecipeBuilder<T extends PotCookingRecipe<T>, B extends CookingRecipeBuilder<T, B>>
		extends BaseRecipeBuilder<B, T, PotCookingRecipe<?>, CookingInv> {

	public CookingRecipeBuilder(BaseRecipe.RecType<T, PotCookingRecipe<?>, CookingInv> type, ItemLike result, int time) {
		super(type, result.asItem());
		recipe.time = time;
		recipe.result = result.asItem().getDefaultInstance();
	}

	public void save(RegistrateRecipeProvider pvd) {
		var item = recipe.result.getItem();
		var path = item.builtInRegistryHolder().unwrapKey().orElseThrow().location();
		save(pvd, pvd.safeId(path));
	}

}
