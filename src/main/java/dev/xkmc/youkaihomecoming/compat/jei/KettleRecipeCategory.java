package dev.xkmc.youkaihomecoming.compat.jei;

import dev.xkmc.youkaihomecoming.content.pot.kettle.KettleRecipe;
import dev.xkmc.youkaihomecoming.init.YoukaiHomecoming;
import dev.xkmc.youkaihomecoming.init.data.YHLangData;
import dev.xkmc.youkaihomecoming.init.registrate.YHBlocks;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class KettleRecipeCategory extends BasePotRecipeCategory<KettleRecipe> {

	protected final IDrawable heatIndicator;
	protected final IDrawableAnimated arrow;

	public KettleRecipeCategory(IGuiHelper helper) {
		super(helper, "kettle", YHBlocks.KETTLE.asStack());
		ResourceLocation backgroundImage = new ResourceLocation(YoukaiHomecoming.MODID, "textures/gui/kettle.png");
		this.heatIndicator = helper.createDrawable(backgroundImage, 176, 0, 17, 10);
		this.arrow = helper.drawableBuilder(backgroundImage, 176, 15, 35, 17)
				.buildAnimated(200, IDrawableAnimated.StartDirection.LEFT, false);
	}

	@Override
	public RecipeType<KettleRecipe> getRecipeType() {
		return YHJeiPlugin.KETTLE;
	}

	@Override
	public Component getTitle() {
		return YHLangData.JEI_KETTLE.get();
	}

	public void draw(KettleRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
		this.arrow.draw(guiGraphics, 45, 11);
		this.heatIndicator.draw(guiGraphics, 15, 48);
		super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
	}

}
