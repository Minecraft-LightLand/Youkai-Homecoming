package dev.xkmc.youkaishomecoming.compat.jei;

import dev.xkmc.l2library.serial.recipe.BaseRecipeCategory;
import dev.xkmc.youkaishomecoming.content.item.fluid.YHFluid;
import dev.xkmc.youkaishomecoming.content.pot.basin.SimpleBasinRecipe;
import dev.xkmc.youkaishomecoming.content.pot.table.recipe.CuisineRecipe;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.data.YHLangData;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.List;

public class BasinRecipeCategory extends BaseRecipeCategory<SimpleBasinRecipe, BasinRecipeCategory> {

	private IGuiHelper guiHelper;

	public BasinRecipeCategory() {
		super(YoukaisHomecoming.loc("basin"), SimpleBasinRecipe.class);
	}

	public BasinRecipeCategory init(IGuiHelper guiHelper) {
		this.guiHelper = guiHelper;
		this.background = guiHelper.createBlankDrawable(18 + 62, 18);
		this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, YHBlocks.BASIN.asStack());
		return this;
	}

	public Component getTitle() {
		return YHLangData.JEI_BASIN.get();
	}

	public void setRecipe(IRecipeLayoutBuilder builder, SimpleBasinRecipe recipe, IFocusGroup focuses) {
		builder.addSlot(RecipeIngredientRole.INPUT, 1, 1)
				.setStandardSlotBackground()
				.addIngredients(recipe.input);
		if (recipe.output.getFluid() instanceof YHFluid sake) {
			builder.addSlot(RecipeIngredientRole.OUTPUT, 56, 1).addItemStack(sake.type.asStack(sake.type.count()));
		} else {
			builder.addSlot(RecipeIngredientRole.OUTPUT, 56, 1).addIngredients(ForgeTypes.FLUID_STACK, List.of(recipe.output));
		}
	}

	public void draw(SimpleBasinRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
		IDrawableStatic recipeArrow = guiHelper.getRecipeArrow();
		recipeArrow.draw(guiGraphics, 27, (18 - recipeArrow.getHeight()) / 2);
	}

}
