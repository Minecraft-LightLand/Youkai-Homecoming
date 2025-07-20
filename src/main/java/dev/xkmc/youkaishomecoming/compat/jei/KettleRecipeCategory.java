package dev.xkmc.youkaishomecoming.compat.jei;

import dev.xkmc.l2library.serial.recipe.BaseRecipeCategory;
import dev.xkmc.youkaishomecoming.content.item.fluid.YHFluid;
import dev.xkmc.youkaishomecoming.content.pot.kettle.KettleRecipe;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.data.YHLangData;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class KettleRecipeCategory extends BaseRecipeCategory<KettleRecipe, KettleRecipeCategory> {

	protected static final ResourceLocation BG = YoukaisHomecoming.loc("textures/gui/kettle.png");

	protected IDrawable heatIndicator;
	protected IDrawable water;
	protected IDrawableAnimated arrow;

	public KettleRecipeCategory() {
		super(YoukaisHomecoming.loc("kettle"), KettleRecipe.class);
	}

	public KettleRecipeCategory init(IGuiHelper helper) {
		this.background = helper.createDrawable(BG, 29, 16, 116, 56);
		this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, YHBlocks.KETTLE.asStack());
		this.heatIndicator = helper.createDrawable(BG, 176, 0, 17, 10);
		this.arrow = helper.drawableBuilder(BG, 176, 15, 35, 17)
				.buildAnimated(200, IDrawableAnimated.StartDirection.LEFT, false);
		this.water = helper.createDrawable(BG, 176, 32, 32, 5);

		return this;
	}

	public Component getTitle() {
		return YHLangData.JEI_KETTLE.get();
	}

	public void setRecipe(IRecipeLayoutBuilder builder, KettleRecipe recipe, IFocusGroup focuses) {
		int n = 0;
		for (var stack : recipe.input) {
			if (stack.isEmpty()) continue;
			int y = n / 2 * 18 + 2;
			int x = n % 2 * 18 + 6;
			builder.addSlot(RecipeIngredientRole.INPUT, x, y).addIngredients(stack);
			n++;
		}
		if (!recipe.result.isEmpty()) {
			int y = 11;
			int x = 92;
			if (recipe.result.getFluid() instanceof YHFluid sake) {
				builder.addSlot(RecipeIngredientRole.OUTPUT, x, y).addItemStack(sake.type.asStack(sake.type.count()));
			} else {
				builder.addSlot(RecipeIngredientRole.OUTPUT, x, y).addIngredients(ForgeTypes.FLUID_STACK, List.of(recipe.result));
			}

		}
	}

	public void draw(KettleRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
		this.arrow.draw(guiGraphics, 45, 11);
		this.heatIndicator.draw(guiGraphics, 15, 48);
		this.water.draw(guiGraphics, 7, 40);
		super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
	}

}
