package dev.xkmc.youkaihomecoming.compat.jei;

import dev.xkmc.youkaihomecoming.content.pot.MokaRecipe;
import dev.xkmc.youkaihomecoming.init.registrate.YHBlocks;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.world.item.ItemStack;

public class MokaRecipeCategory extends BasePotRecipeCategory<MokaRecipe> {

	public MokaRecipeCategory(IGuiHelper helper) {
		super(helper, "moka", YHBlocks.MOKA.asStack());
	}

	@Override
	public RecipeType<MokaRecipe> getRecipeType() {
		return YHJeiPlugin.MOKA;
	}

}
