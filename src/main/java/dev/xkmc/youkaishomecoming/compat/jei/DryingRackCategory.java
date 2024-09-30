package dev.xkmc.youkaishomecoming.compat.jei;

import dev.xkmc.youkaishomecoming.content.pot.rack.DryingRackRecipe;
import dev.xkmc.youkaishomecoming.init.data.YHLangData;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.library.plugins.vanilla.cooking.AbstractCookingCategory;
import mezz.jei.library.util.RecipeUtil;
import net.minecraft.client.gui.GuiGraphics;

public class DryingRackCategory extends AbstractCookingCategory<DryingRackRecipe> {

	public DryingRackCategory(IGuiHelper guiHelper) {
		super(guiHelper, YHBlocks.RACK.get(), YHLangData.JEI_RACK.key(), 200, 82, 44);
	}

	@Override
	public RecipeType<DryingRackRecipe> getRecipeType() {
		return YHJeiPlugin.RACK;
	}

	public void draw(DryingRackRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
		this.animatedFlame.draw(guiGraphics, 1, 20);
		this.drawCookTime(recipe, guiGraphics, 35);
	}

	public void setRecipe(IRecipeLayoutBuilder builder, DryingRackRecipe recipe, IFocusGroup focuses) {
		builder.addSlot(RecipeIngredientRole.INPUT, 1, 1).setStandardSlotBackground().addIngredients(recipe.getIngredients().get(0));
		builder.addSlot(RecipeIngredientRole.OUTPUT, 61, 9).setOutputSlotBackground().addItemStack(RecipeUtil.getResultItem(recipe));
	}

	public void createRecipeExtras(IRecipeExtrasBuilder acceptor, DryingRackRecipe recipe, IFocusGroup focuses) {
		acceptor.addWidget(this.createCookingArrowWidget(recipe, 26, 7));
	}

}
