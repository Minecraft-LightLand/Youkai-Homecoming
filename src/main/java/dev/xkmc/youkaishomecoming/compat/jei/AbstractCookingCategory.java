//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package dev.xkmc.youkaishomecoming.compat.jei;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.placement.HorizontalAlignment;
import mezz.jei.api.gui.placement.VerticalAlignment;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.level.block.Block;

public abstract class AbstractCookingCategory<T extends AbstractCookingRecipe> extends AbstractRecipeCategory<T> {
	protected final int regularCookTime;

	public AbstractCookingCategory(IGuiHelper guiHelper, RecipeType<T> recipeType, Block icon, String translationKey, int regularCookTime) {
		this(guiHelper, recipeType, icon, translationKey, regularCookTime, 82, 54);
	}

	public AbstractCookingCategory(IGuiHelper guiHelper, RecipeType<T> recipeType, Block icon, String translationKey, int regularCookTime, int width, int height) {
		super(recipeType, Component.translatable(translationKey), guiHelper.createDrawableItemLike(icon), width, height);
		this.regularCookTime = regularCookTime;
	}

	public void setRecipe(IRecipeLayoutBuilder builder, T recipe, IFocusGroup focuses) {
		builder.addInputSlot(1, 1).setStandardSlotBackground().addIngredients(recipe.getIngredients().get(0));
		builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 1, 37).setStandardSlotBackground();
		builder.addOutputSlot(61, 19).setOutputSlotBackground().addItemStack(recipe.getResultItem(Minecraft.getInstance().level.registryAccess()));
	}

	public void createRecipeExtras(IRecipeExtrasBuilder builder, T recipe, IFocusGroup focuses) {
		int cookTime = recipe.getCookingTime();
		if (cookTime <= 0) {
			cookTime = this.regularCookTime;
		}

		builder.addAnimatedRecipeArrow(cookTime).setPosition(26, 17);
		builder.addAnimatedRecipeFlame(300).setPosition(1, 20);
		this.addExperience(builder, recipe);
		this.addCookTime(builder, recipe);
	}

	protected void addExperience(IRecipeExtrasBuilder builder, T recipe) {
		float experience = recipe.getExperience();
		if (experience > 0.0F) {
			Component experienceString = Component.translatable("gui.jei.category.smelting.experience", experience);
			builder.addText(experienceString, this.getWidth() - 20, 10)
					.setPosition(0, 0, this.getWidth(), this.getHeight(), HorizontalAlignment.RIGHT, VerticalAlignment.TOP)
					.setTextAlignment(HorizontalAlignment.RIGHT).setColor(-8355712);
		}

	}

	protected void addCookTime(IRecipeExtrasBuilder builder, T recipe) {
		int cookTime = recipe.getCookingTime();
		if (cookTime <= 0) {
			cookTime = this.regularCookTime;
		}

		if (cookTime > 0) {
			int cookTimeSeconds = cookTime / 20;
			Component timeString = Component.translatable("gui.jei.category.smelting.time.seconds", cookTimeSeconds);
			builder.addText(timeString, this.getWidth() - 20, 10)
					.setPosition(0, 0, this.getWidth(), this.getHeight(), HorizontalAlignment.RIGHT, VerticalAlignment.BOTTOM)
					.setTextAlignment(HorizontalAlignment.RIGHT).setTextAlignment(VerticalAlignment.BOTTOM).setColor(-8355712);
		}

	}

	public boolean isHandled(T recipe) {
		return !recipe.isSpecial();
	}

	public ResourceLocation getRegistryName(T recipe) {
		return recipe.getId();
	}
}
