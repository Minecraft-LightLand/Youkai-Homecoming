package dev.xkmc.youkaishomecoming.compat.jei;

import dev.xkmc.l2library.serial.recipe.BaseRecipeCategory;
import dev.xkmc.l2serial.util.Wrappers;
import dev.xkmc.youkaishomecoming.content.pot.table.item.VariantTableItemBase;
import dev.xkmc.youkaishomecoming.content.pot.table.recipe.CuisineRecipe;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.data.YHLangData;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class CuisineRecipeCategory extends BaseRecipeCategory<CuisineRecipe<?>, CuisineRecipeCategory> {
	private IGuiHelper guiHelper;
	private IDrawableStatic hand;

	public CuisineRecipeCategory() {
		super(YoukaisHomecoming.loc("cuisine"), Wrappers.cast(CuisineRecipe.class));
	}

	public CuisineRecipeCategory init(IGuiHelper guiHelper) {
		this.guiHelper = guiHelper;
		this.background = guiHelper.createBlankDrawable(18 * 5 + 62, 36);
		this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, YHBlocks.CUISINE_BOARD.asStack());
		hand = guiHelper.drawableBuilder(YoukaisHomecoming.loc("textures/gui/hand.png"), 0, 0, 16, 16)
				.setTextureSize(32, 16).build();
		return this;
	}

	public Component getTitle() {
		return YHLangData.JEI_CUISINE.get();
	}

	public void draw(CuisineRecipe<?> recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
		int max = doRecipe(recipe, (ing, x, y) -> {
			if (ing.isEmpty()) {
				hand.draw(guiGraphics, x, y);
			}
		});
		IDrawableStatic recipeArrow = this.guiHelper.getRecipeArrow();
		recipeArrow.draw(guiGraphics, (max + 5) * 9 + 7, (36 - recipeArrow.getHeight()) / 2);
	}

	public void setRecipe(IRecipeLayoutBuilder builder, CuisineRecipe<?> recipe, IFocusGroup focuses) {
		int max = doRecipe(recipe, (ing, x, y) -> {
			if (!ing.isEmpty()) {
				builder.addInputSlot(x, y).setStandardSlotBackground().addIngredients(ing);
			}
		});
		builder.addSlot(RecipeIngredientRole.OUTPUT, 86 + max * 9, 10)
				.setOutputSlotBackground()
				.addItemStack(recipe.getResult());
	}

	private int doRecipe(CuisineRecipe<?> recipe, IngredientHandler handler) {
		List<Ingredient> listBase = new ArrayList<>();
		List<Ingredient> listRecipe = new ArrayList<>(recipe.getCustomIngredients());
		var base = VariantTableItemBase.MAP.get(recipe.base());
		if (base != null) {
			base.collectIngredients(listBase, listRecipe);
		} else {
			var item = ForgeRegistries.ITEMS.getValue(recipe.base());
			listBase.add(Ingredient.of(item));
		}
		balance(listBase, listRecipe);

		int offset = (5 - listBase.size()) * 9;
		int index = 0;
		int y = 1;
		for (var ing : listBase) {
			int x = index * 18 + 1 + offset;
			handler.draw(ing, x, y);
			index++;
		}
		index = 0;
		offset = (5 - listRecipe.size()) * 9;
		y = listBase.isEmpty() ? 10 : 19;
		for (var ing : listRecipe) {
			int x = index * 18 + 1 + offset;
			handler.draw(ing, x, y);
			index++;
		}
		return Math.max(listBase.size(), listRecipe.size());
	}

	private void balance(List<Ingredient> a, List<Ingredient> b) {
		if (a.size() + b.size() <= 5) {
			for (var e : a) b.add(0, e);
			a.clear();
		}
		while (b.size() > 5 && a.size() < 5) {
			a.add(b.get(0));
			b.remove(0);
		}
	}

	private interface IngredientHandler {

		void draw(Ingredient ing, int x, int y);

	}

}
