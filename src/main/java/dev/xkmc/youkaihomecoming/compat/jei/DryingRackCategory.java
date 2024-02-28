package dev.xkmc.youkaihomecoming.compat.jei;

import dev.xkmc.youkaihomecoming.content.pot.rack.DryingRackRecipe;
import dev.xkmc.youkaihomecoming.init.data.YHLangData;
import dev.xkmc.youkaihomecoming.init.registrate.YHBlocks;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.common.Constants;
import mezz.jei.library.plugins.vanilla.cooking.AbstractCookingCategory;
import mezz.jei.library.util.RecipeUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.level.block.Blocks;

public class DryingRackCategory extends AbstractCookingCategory<DryingRackRecipe> {

	private final IDrawable background;

	public DryingRackCategory(IGuiHelper guiHelper) {
		super(guiHelper, YHBlocks.RACK.get(), YHLangData.JEI_RACK.key(), 200);
		background = guiHelper.drawableBuilder(Constants.RECIPE_GUI_VANILLA, 0, 186, 82, 34)
				.addPadding(0, 10, 0, 0)
				.build();
	}

	@Override
	public RecipeType<DryingRackRecipe> getRecipeType() {
		return YHJeiPlugin.RACK;
	}

	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public void draw(DryingRackRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
		animatedFlame.draw(guiGraphics, 1, 20);
		IDrawableAnimated arrow = getArrow(recipe);
		arrow.draw(guiGraphics, 24, 8);
		drawCookTime(recipe, guiGraphics, 35);
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, DryingRackRecipe recipe, IFocusGroup focuses) {
		builder.addSlot(RecipeIngredientRole.INPUT, 1, 1)
				.addIngredients(recipe.getIngredients().get(0));

		builder.addSlot(RecipeIngredientRole.OUTPUT, 61, 9)
				.addItemStack(RecipeUtil.getResultItem(recipe));
	}

}
