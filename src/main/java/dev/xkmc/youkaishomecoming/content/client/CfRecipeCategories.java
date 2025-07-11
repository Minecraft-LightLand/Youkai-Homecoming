package dev.xkmc.youkaishomecoming.content.client;

import com.google.common.base.Suppliers;
import dev.xkmc.youkaishomecoming.init.registrate.CoffeeBlocks;
import net.minecraft.client.RecipeBookCategories;

import java.util.function.Supplier;

public class CfRecipeCategories {

	public static final Supplier<RecipeBookCategories> MOKA = Suppliers.memoize(() ->
			RecipeBookCategories.create("MOKA", CoffeeBlocks.MOKA.asStack()));

}
