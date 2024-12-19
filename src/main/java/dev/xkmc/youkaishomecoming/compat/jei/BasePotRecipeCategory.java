package dev.xkmc.youkaishomecoming.compat.jei;

import dev.xkmc.youkaishomecoming.content.pot.base.BasePotRecipe;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.common.utility.ClientRenderUtils;
import vectorwing.farmersdelight.common.utility.RecipeUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class BasePotRecipeCategory<T extends BasePotRecipe> implements IRecipeCategory<T> {
	protected final IDrawable timeIcon;
	protected final IDrawable expIcon;
	private final IDrawable background;
	private final IDrawable icon;

	public BasePotRecipeCategory(IGuiHelper helper, String path, ItemStack icon) {

		ResourceLocation backgroundImage = YoukaisHomecoming.loc("textures/gui/" + path + ".png");
		this.background = helper.createDrawable(backgroundImage, 29, 16, 116, 56);
		this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, icon);
		this.timeIcon = helper.createDrawable(backgroundImage, 176, 32, 8, 11);
		this.expIcon = helper.createDrawable(backgroundImage, 176, 43, 9, 9);
	}

	public abstract RecipeType<T> getRecipeType();

	public IDrawable getBackground() {
		return this.background;
	}

	public IDrawable getIcon() {
		return this.icon;
	}

	public void setRecipe(IRecipeLayoutBuilder builder, T recipe, IFocusGroup focusGroup) {
		NonNullList<Ingredient> recipeIngredients = recipe.getIngredients();
		ItemStack resultStack = RecipeUtils.getResultItem(recipe);
		ItemStack containerStack = recipe.getOutputContainer();
		int borderSlotSize = 18;

		for (int row = 0; row < 2; ++row) {
			for (int column = 0; column < 2; ++column) {
				int inputIndex = row * 2 + column;
				if (inputIndex < recipeIngredients.size()) {
					builder.addSlot(RecipeIngredientRole.INPUT, column * borderSlotSize + 6, row * borderSlotSize + 2)
							.addItemStacks(Arrays.asList(recipeIngredients.get(inputIndex).getItems()));
				}
			}
		}

		builder.addSlot(RecipeIngredientRole.OUTPUT, 92, 11).addItemStack(resultStack);
		if (!containerStack.isEmpty()) {
			builder.addSlot(RecipeIngredientRole.CATALYST, 60, 38).addItemStack(containerStack);
		}

		builder.addSlot(RecipeIngredientRole.OUTPUT, 92, 38).addItemStack(resultStack);
	}

	public void draw(T recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
	}

	public List<Component> getTooltipStrings(CookingPotRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
		if (ClientRenderUtils.isCursorInsideBounds(61, 2, 22, 28, mouseX, mouseY)) {
			List<Component> tooltipStrings = new ArrayList<>();
			int cookTime = recipe.getCookTime();
			if (cookTime > 0) {
				int cookTimeSeconds = cookTime / 20;
				tooltipStrings.add(Component.translatable("gui.jei.category.smelting.time.seconds", cookTimeSeconds));
			}

			float experience = recipe.getExperience();
			if (experience > 0.0F) {
				tooltipStrings.add(Component.translatable("gui.jei.category.smelting.experience", experience));
			}

			return tooltipStrings;
		} else {
			return Collections.emptyList();
		}
	}

}
