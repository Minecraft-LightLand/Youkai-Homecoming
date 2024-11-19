
package dev.xkmc.youkaishomecoming.compat.jei;

import dev.xkmc.l2library.serial.recipe.BaseRecipeCategory;
import dev.xkmc.youkaishomecoming.content.item.fluid.SakeFluid;
import dev.xkmc.youkaishomecoming.content.pot.ferment.SimpleFermentationRecipe;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.data.YHLangData;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class FermentRecipeCategory extends BaseRecipeCategory<SimpleFermentationRecipe, FermentRecipeCategory> {
	protected static final ResourceLocation BG = YoukaisHomecoming.loc("textures/gui/ferment.png");

	public FermentRecipeCategory() {
		super(YoukaisHomecoming.loc("ferment"), SimpleFermentationRecipe.class);
	}

	public FermentRecipeCategory init(IGuiHelper guiHelper) {
		this.background = guiHelper.createDrawable(BG, 0, 0, 144, 54);
		this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, YHBlocks.FERMENT.asStack());
		return this;
	}

	public Component getTitle() {
		return YHLangData.JEI_FERMENT.get();
	}

	public void setRecipe(IRecipeLayoutBuilder builder, SimpleFermentationRecipe recipe, IFocusGroup focuses) {
		int n = 0;
		for (var stack : recipe.ingredients) {
			if (stack.isEmpty()) continue;
			int y = n / 3 * 18 + 1;
			int x = n % 3 * 18 + 1;
			builder.addSlot(RecipeIngredientRole.INPUT, x, y).addIngredients(stack);
			n++;
		}
		if (!recipe.inputFluid.isEmpty()) {
			int y = n / 3 * 18 + 1;
			int x = n % 3 * 18 + 1;
			builder.addSlot(RecipeIngredientRole.INPUT, x, y).addIngredients(ForgeTypes.FLUID_STACK, List.of(recipe.inputFluid));
		}
		n = 0;
		for (var stack : recipe.results) {
			if (stack.isEmpty()) continue;
			int y = n / 3 * 18 + 1;
			int x = n % 3 * 18 + 91;
			builder.addSlot(RecipeIngredientRole.INPUT, x, y).addItemStack(stack);
			n++;
		}
		if (!recipe.outputFluid.isEmpty()) {
			int y = n / 3 * 18 + 1;
			int x = n % 3 * 18 + 91;
			if (recipe.outputFluid.getFluid() instanceof SakeFluid sake) {
				builder.addSlot(RecipeIngredientRole.OUTPUT, x, y).addItemStack(sake.type.asStack(sake.type.count()));
				builder.addSlot(RecipeIngredientRole.INPUT, 64, 1).addItemStack(new ItemStack(sake.type.getContainer(), sake.type.count()));
			} else {
				if (!recipe.defaultContainer.isEmpty() && !recipe.defaultBottle.isEmpty()) {
					builder.addSlot(RecipeIngredientRole.OUTPUT, x, y).addItemStack(recipe.defaultBottle);
					builder.addSlot(RecipeIngredientRole.INPUT, 64, 1).addItemStack(recipe.defaultContainer);
				} else {
					builder.addSlot(RecipeIngredientRole.OUTPUT, x, y).addIngredients(ForgeTypes.FLUID_STACK, List.of(recipe.outputFluid));
				}
			}
		}
	}
}
