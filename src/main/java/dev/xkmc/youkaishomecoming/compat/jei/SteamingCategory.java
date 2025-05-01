package dev.xkmc.youkaishomecoming.compat.jei;

import dev.xkmc.youkaishomecoming.content.pot.steamer.SteamingRecipe;
import dev.xkmc.youkaishomecoming.init.data.YHLangData;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.crafting.RecipeHolder;

public class SteamingCategory extends AbstractCookingCategory<SteamingRecipe> {

	public SteamingCategory(IGuiHelper guiHelper) {
		super(guiHelper, YHJeiPlugin.STEAM, YHBlocks.STEAMER_RACK.get(), YHLangData.JEI_STEAM.key(), 100, 82, 44);
	}

	public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<SteamingRecipe> recipeHolder, IFocusGroup focuses) {
		SteamingRecipe recipe = recipeHolder.value();
		builder.addInputSlot(1, 1).setStandardSlotBackground().addIngredients(recipe.getIngredients().getFirst());
		builder.addOutputSlot(61, 9).setOutputSlotBackground().addItemStack(recipe.getResultItem(Minecraft.getInstance().level.registryAccess()));
	}

	public void createRecipeExtras(IRecipeExtrasBuilder builder, RecipeHolder<SteamingRecipe> recipeHolder, IFocusGroup focuses) {
		SteamingRecipe recipe = recipeHolder.value();
		int cookTime = recipe.getCookingTime();
		if (cookTime <= 0) {
			cookTime = this.regularCookTime;
		}

		builder.addAnimatedRecipeArrow(cookTime).setPosition(26, 7);
		builder.addAnimatedRecipeFlame(300).setPosition(1, 20);
		this.addCookTime(builder, recipeHolder);
	}

}
