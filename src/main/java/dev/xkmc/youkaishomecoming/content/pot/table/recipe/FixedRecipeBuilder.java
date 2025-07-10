package dev.xkmc.youkaishomecoming.content.pot.table.recipe;

import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.resources.ResourceLocation;

public class FixedRecipeBuilder extends CuisineRecipeBuilder<FixedCuisineRecipe, FixedRecipeBuilder> {

	public FixedRecipeBuilder(ResourceLocation base) {
		super(YHBlocks.CUISINE_FIXED.get(), base);
	}

}
