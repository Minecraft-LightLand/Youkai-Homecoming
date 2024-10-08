package dev.xkmc.youkaishomecoming.compat.jei;

import com.mojang.serialization.Codec;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.placement.HorizontalAlignment;
import mezz.jei.api.gui.placement.VerticalAlignment;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.ICodecHelper;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.IRecipeManager;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.Block;

public abstract class AbstractCookingCategory<T extends AbstractCookingRecipe> extends AbstractRecipeCategory<RecipeHolder<T>> {
	protected final int regularCookTime;

	public AbstractCookingCategory(IGuiHelper guiHelper, RecipeType<RecipeHolder<T>> recipeType, Block icon, String translationKey, int regularCookTime) {
		this(guiHelper, recipeType, icon, translationKey, regularCookTime, 82, 54);
	}

	public AbstractCookingCategory(IGuiHelper guiHelper, RecipeType<RecipeHolder<T>> recipeType, Block icon, String translationKey, int regularCookTime, int width, int height) {
		super(recipeType, Component.translatable(translationKey), guiHelper.createDrawableItemLike(icon), width, height);
		this.regularCookTime = regularCookTime;
	}

	public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<T> recipeHolder, IFocusGroup focuses) {
		T recipe = recipeHolder.value();
		builder.addInputSlot(1, 1).setStandardSlotBackground().addIngredients(recipe.getIngredients().getFirst());
		builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 1, 37).setStandardSlotBackground();
		builder.addOutputSlot(61, 19).setOutputSlotBackground().addItemStack(recipe.getResultItem(Minecraft.getInstance().level.registryAccess()));
	}

	public void createRecipeExtras(IRecipeExtrasBuilder builder, RecipeHolder<T> recipeHolder, IFocusGroup focuses) {
		T recipe = recipeHolder.value();
		int cookTime = recipe.getCookingTime();
		if (cookTime <= 0) {
			cookTime = this.regularCookTime;
		}

		builder.addAnimatedRecipeArrow(cookTime).setPosition(26, 17);
		builder.addAnimatedRecipeFlame(300).setPosition(1, 20);
		this.addExperience(builder, recipeHolder);
		this.addCookTime(builder, recipeHolder);
	}

	protected void addExperience(IRecipeExtrasBuilder builder, RecipeHolder<T> recipeHolder) {
		T recipe = recipeHolder.value();
		float experience = recipe.getExperience();
		if (experience > 0.0F) {
			Component experienceString = Component.translatable("gui.jei.category.smelting.experience", new Object[]{experience});
			(builder.addText(experienceString, this.getWidth() - 20, 10).setPosition(0, 0, this.getWidth(), this.getHeight(), HorizontalAlignment.RIGHT, VerticalAlignment.TOP)).setTextAlignment(HorizontalAlignment.RIGHT).setColor(-8355712);
		}

	}

	protected void addCookTime(IRecipeExtrasBuilder builder, RecipeHolder<T> recipeHolder) {
		T recipe = recipeHolder.value();
		int cookTime = recipe.getCookingTime();
		if (cookTime <= 0) {
			cookTime = this.regularCookTime;
		}

		if (cookTime > 0) {
			int cookTimeSeconds = cookTime / 20;
			Component timeString = Component.translatable("gui.jei.category.smelting.time.seconds", new Object[]{cookTimeSeconds});
			(builder.addText(timeString, this.getWidth() - 20, 10).setPosition(0, 0, this.getWidth(), this.getHeight(), HorizontalAlignment.RIGHT, VerticalAlignment.BOTTOM)).setTextAlignment(HorizontalAlignment.RIGHT).setTextAlignment(VerticalAlignment.BOTTOM).setColor(-8355712);
		}

	}

	public boolean isHandled(RecipeHolder<T> recipeHolder) {
		T recipe = recipeHolder.value();
		return !recipe.isSpecial();
	}

	public ResourceLocation getRegistryName(RecipeHolder<T> recipe) {
		return recipe.id();
	}

	public Codec<RecipeHolder<T>> getCodec(ICodecHelper codecHelper, IRecipeManager recipeManager) {
		return codecHelper.getRecipeHolderCodec();
	}
}
