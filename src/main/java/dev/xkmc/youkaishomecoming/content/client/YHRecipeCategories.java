package dev.xkmc.youkaishomecoming.content.client;

import com.google.common.base.Suppliers;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.client.RecipeBookCategories;

import java.util.function.Supplier;

public class YHRecipeCategories {

	public static final Supplier<RecipeBookCategories> MOKA = Suppliers.memoize(() ->
			RecipeBookCategories.create("MOKA", YHBlocks.MOKA.asStack()));

	public static final Supplier<RecipeBookCategories> KETTLE = Suppliers.memoize(() ->
			RecipeBookCategories.create("KETTLE", YHBlocks.KETTLE.asStack()));

}
