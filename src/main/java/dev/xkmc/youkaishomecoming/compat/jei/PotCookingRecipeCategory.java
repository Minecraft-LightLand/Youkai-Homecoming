package dev.xkmc.youkaishomecoming.compat.jei;

import dev.xkmc.l2library.serial.recipe.BaseRecipeCategory;
import dev.xkmc.l2serial.util.Wrappers;
import dev.xkmc.youkaishomecoming.content.pot.cooking.core.PotCookingRecipe;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.data.YHLangData;
import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;

public class PotCookingRecipeCategory extends BaseRecipeCategory<PotCookingRecipe<?>, PotCookingRecipeCategory> {

	private IGuiHelper guiHelper;

	public PotCookingRecipeCategory() {
		super(YoukaisHomecoming.loc("cooking"), Wrappers.cast(PotCookingRecipe.class));
	}

	public PotCookingRecipeCategory init(IGuiHelper guiHelper) {
		this.guiHelper = guiHelper;
		this.background = guiHelper.createBlankDrawable(18 * 5 + 62, 36);
		this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, YHItems.IRON_BOWL.asStack());
		return this;
	}

	public Component getTitle() {
		return YHLangData.JEI_COOKING.get();
	}

	public void createRecipeExtras(IRecipeExtrasBuilder builder, PotCookingRecipe<?> recipe, IFocusGroup focuses) {
		int cookTime = recipe.getProcessTime();
		if (cookTime <= 0) {
			cookTime = 200;
		}
		int n = recipe.getInput().size() ;
		int width = n <= 5 ? n : (n + 1) / 2;
		builder.addAnimatedRecipeArrow(cookTime).setPosition(98 - (5 - width) * 9, 6);
		builder.addAnimatedRecipeFlame(300).setPosition(102 - (5 - width) * 9, 22);
	}

	public void setRecipe(IRecipeLayoutBuilder builder, PotCookingRecipe<?> recipe, IFocusGroup focuses) {
		int x = 0;
		int y = 0;
		var list = new ArrayList<>(recipe.getInput());
		int n = list.size();
		int width = n <= 5 ? n : (n + 1) / 2;
		int xff = 1 + (5 - width) * 9;
		int yff = n <= 5 ? 10 : 1;
		for (var ing : list) {
			builder.addSlot(RecipeIngredientRole.INPUT, x * 18 + xff, y * 18 + yff)
					.setStandardSlotBackground()
					.addIngredients(ing);
			x++;
			if (x >= width) {
				x = 0;
				y++;
				xff = (width - n + 5) * 9 + 1;
			}
		}

		builder.addSlot(RecipeIngredientRole.OUTPUT, 131 - (5 - width) * 9, 10)
				.setOutputSlotBackground()
				.addItemStack(recipe.getResult());
	}

}
