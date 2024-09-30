package dev.xkmc.youkaishomecoming.compat.jei;

import dev.xkmc.youkaishomecoming.content.pot.rack.DryingRackRecipe;
import dev.xkmc.youkaishomecoming.init.data.YHLangData;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.library.plugins.vanilla.cooking.AbstractCookingCategory;
import mezz.jei.library.util.RecipeUtil;
import net.minecraft.world.item.crafting.RecipeHolder;

public class DryingRackCategory extends AbstractCookingCategory<DryingRackRecipe> {

	public DryingRackCategory(IGuiHelper guiHelper) {
		super(guiHelper, YHJeiPlugin.RACK, YHBlocks.RACK.get(), YHLangData.JEI_RACK.key(), 200, 82, 44);
	}

	public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<DryingRackRecipe> recipeHolder, IFocusGroup focuses) {
		DryingRackRecipe recipe = recipeHolder.value();
		builder.addInputSlot(1, 1).setStandardSlotBackground().addIngredients(recipe.getIngredients().getFirst());
		builder.addOutputSlot(61, 9).setOutputSlotBackground().addItemStack(RecipeUtil.getResultItem(recipe));
	}

	public void createRecipeExtras(IRecipeExtrasBuilder builder, RecipeHolder<DryingRackRecipe> recipeHolder, IRecipeSlotsView recipeSlotsView, IFocusGroup focuses) {
		DryingRackRecipe recipe = recipeHolder.value();
		int cookTime = recipe.getCookingTime();
		if (cookTime <= 0) {
			cookTime = this.regularCookTime;
		}
		builder.addAnimatedRecipeArrow(cookTime, 26, 7);
		builder.addAnimatedRecipeFlame(300, 1, 20);
		this.addCookTime(builder, recipeHolder);
	}

}
