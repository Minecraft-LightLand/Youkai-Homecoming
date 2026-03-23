package dev.xkmc.youkaishomecoming.compat.jei;

import dev.xkmc.l2core.compat.jei.BaseRecipeCategory;
import dev.xkmc.youkaishomecoming.content.item.fluid.IYHFluidItem;
import dev.xkmc.youkaishomecoming.content.item.fluid.YHFluidHandler;
import dev.xkmc.youkaishomecoming.content.pot.basin.SimpleBasinRecipe;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.data.YHLangData;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
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
		if (YHFluidHandler.of(recipe.output) instanceof IYHFluidItem sake) {
			int amount = recipe.output.getAmount();
			int count = sake.amount() / amount;
			var list = new ArrayList<ItemStack>();
			for (var e : recipe.input.getItems()) {
				list.add(e.copyWithCount(count));
			}
			builder.addSlot(RecipeIngredientRole.INPUT, 1, 1)
					.setStandardSlotBackground()
					.addItemStacks(list);
			builder.addSlot(RecipeIngredientRole.OUTPUT, 56, 1)
					.setStandardSlotBackground()
					.addItemStack(sake.asStack(1));
		} else {
			builder.addSlot(RecipeIngredientRole.INPUT, 1, 1)
					.setStandardSlotBackground()
					.addIngredients(recipe.input);
			builder.addSlot(RecipeIngredientRole.OUTPUT, 56, 1)
					.setStandardSlotBackground()
					.addIngredients(NeoForgeTypes.FLUID_STACK, List.of(recipe.output));
		}
	}

	public void draw(SimpleBasinRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
		IDrawableStatic recipeArrow = guiHelper.getRecipeArrow();
		recipeArrow.draw(guiGraphics, 27, (18 - recipeArrow.getHeight()) / 2);
	}

}
