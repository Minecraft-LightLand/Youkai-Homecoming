package dev.xkmc.youkaihomecoming.compat.jei;

import dev.xkmc.youkaihomecoming.content.pot.moka.MokaRecipe;
import dev.xkmc.youkaihomecoming.init.YoukaiHomecoming;
import dev.xkmc.youkaihomecoming.init.registrate.YHBlocks;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class MokaRecipeCategory extends BasePotRecipeCategory<MokaRecipe> {

	protected final IDrawable heatIndicator;
	protected final IDrawableAnimated arrow;

	public MokaRecipeCategory(IGuiHelper helper) {
		super(helper, "moka", YHBlocks.MOKA.asStack());
		ResourceLocation backgroundImage = new ResourceLocation(YoukaiHomecoming.MODID, "textures/gui/moka.png");
		this.heatIndicator = helper.createDrawable(backgroundImage, 176, 0, 17, 15);
		this.arrow = helper.drawableBuilder(backgroundImage, 176, 15, 38, 17)
				.buildAnimated(200, IDrawableAnimated.StartDirection.LEFT, false);
	}

	@Override
	public RecipeType<MokaRecipe> getRecipeType() {
		return YHJeiPlugin.MOKA;
	}

	public void draw(MokaRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
		this.arrow.draw(guiGraphics, 45, 11);
		this.heatIndicator.draw(guiGraphics, 15, 41);
		super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
	}

}
