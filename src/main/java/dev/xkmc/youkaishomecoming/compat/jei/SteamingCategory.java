package dev.xkmc.youkaishomecoming.compat.jei;

import dev.xkmc.youkaishomecoming.content.pot.steamer.SteamingRecipe;
import dev.xkmc.youkaishomecoming.init.data.YHLangData;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.Minecraft;

public class SteamingCategory extends AbstractCookingCategory<SteamingRecipe> {

	public SteamingCategory(IGuiHelper guiHelper) {
		super(guiHelper, YHJeiPlugin.STEAM, YHBlocks.STEAMER_RACK.get(), YHLangData.JEI_STEAM.key(), 100, 82, 44);
	}

	public void setRecipe(IRecipeLayoutBuilder builder, SteamingRecipe recipe, IFocusGroup focuses) {
		builder.addSlot(RecipeIngredientRole.INPUT, 1, 1).setStandardSlotBackground().addIngredients(recipe.getIngredients().get(0));
		builder.addSlot(RecipeIngredientRole.OUTPUT, 61, 9).setOutputSlotBackground().addItemStack(recipe.getResultItem(Minecraft.getInstance().level.registryAccess()));
	}

	public void createRecipeExtras(IRecipeExtrasBuilder builder, SteamingRecipe recipe, IFocusGroup focuses) {
		int cookTime = recipe.getCookingTime();
		if (cookTime <= 0) {
			cookTime = this.regularCookTime;
		}
		builder.addAnimatedRecipeArrow(cookTime).setPosition(26, 7);
		this.addCookTime(builder, recipe);
	}

}
