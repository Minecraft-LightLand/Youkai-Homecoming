package dev.xkmc.youkaishomecoming.compat.jei;

import dev.xkmc.youkaishomecoming.content.pot.rack.DryingRackRecipe;
import dev.xkmc.youkaishomecoming.init.data.YHLangData;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.crafting.RecipeHolder;

public class DryingRackCategory extends AbstractCookingCategory<DryingRackRecipe> {

	public DryingRackCategory(IGuiHelper guiHelper) {
		super(guiHelper, YHJeiPlugin.RACK, YHBlocks.RACK.get(), YHLangData.JEI_RACK.key(), 200, 82, 44);
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<DryingRackRecipe> recipe, IFocusGroup focuses) {
		builder.addSlot(RecipeIngredientRole.INPUT, 1, 9).setStandardSlotBackground().addIngredients(recipe.value().getIngredients().get(0));
		builder.addSlot(RecipeIngredientRole.OUTPUT, 61, 9).setOutputSlotBackground().addItemStack(recipe.value().getResultItem(Minecraft.getInstance().level.registryAccess()));
	}

	public void createRecipeExtras(IRecipeExtrasBuilder builder, RecipeHolder<DryingRackRecipe> recipe, IFocusGroup focuses) {
		int cookTime = recipe.value().getCookingTime();
		if (cookTime <= 0) {
			cookTime = this.regularCookTime;
		}

		builder.addAnimatedRecipeArrow(cookTime).setPosition(26, 9);
		this.addCookTime(builder, recipe);
	}

}
