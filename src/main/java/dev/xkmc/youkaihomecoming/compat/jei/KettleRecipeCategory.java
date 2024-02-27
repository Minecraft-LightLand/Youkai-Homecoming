package dev.xkmc.youkaihomecoming.compat.jei;

import dev.xkmc.youkaihomecoming.content.pot.kettle.KettleRecipe;
import dev.xkmc.youkaihomecoming.init.registrate.YHBlocks;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;

public class KettleRecipeCategory extends BasePotRecipeCategory<KettleRecipe> {

	public KettleRecipeCategory(IGuiHelper helper) {
		super(helper, "kettle", YHBlocks.KETTLE.asStack());
	}

	@Override
	public RecipeType<KettleRecipe> getRecipeType() {
		return YHJeiPlugin.KETTLE;
	}

}
