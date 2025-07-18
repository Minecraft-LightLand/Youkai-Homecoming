package dev.xkmc.youkaishomecoming.compat.jei;

import dev.xkmc.l2library.serial.recipe.BaseRecipeCategory;
import dev.xkmc.l2serial.util.Wrappers;
import dev.xkmc.youkaishomecoming.content.block.food.PotFoodBlock;
import dev.xkmc.youkaishomecoming.content.pot.cooking.core.PotCookingRecipe;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.data.YHLangData;
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class PotCookingRecipeCategory extends BaseRecipeCategory<PotCookingRecipe<?>, PotCookingRecipeCategory> {

	private final ItemStack iconStack;

	public PotCookingRecipeCategory(String id, ItemStack icon) {
		super(YoukaisHomecoming.loc(id), Wrappers.cast(PotCookingRecipe.class));
		this.iconStack = icon;
	}

	public PotCookingRecipeCategory init(IGuiHelper guiHelper) {
		this.background = guiHelper.createBlankDrawable(18 * 5 + 62, 36);
		this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, iconStack);
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
		int n = compile(recipe.getInput()).size();
		int width = n <= 5 ? n : (n + 1) / 2;
		builder.addAnimatedRecipeArrow(cookTime).setPosition(98 - (5 - width) * 9, 6);
		builder.addAnimatedRecipeFlame(300).setPosition(102 - (5 - width) * 9, 22);
	}

	public void setRecipe(IRecipeLayoutBuilder builder, PotCookingRecipe<?> recipe, IFocusGroup focuses) {
		int x = 0;
		int y = 0;
		var list = compile(recipe.getInput());
		int n = list.size();
		int width = n <= 5 ? n : (n + 1) / 2;
		int xff = 1 + (5 - width) * 9;
		int yff = n <= 5 ? 10 : 1;
		for (var ing : list) {
			builder.addSlot(RecipeIngredientRole.INPUT, x * 18 + xff, y * 18 + yff)
					.setStandardSlotBackground()
					.addItemStacks(List.of(ing));
			x++;
			if (x >= width) {
				x = 0;
				y++;
				xff = (width - n + 5) * 9 + 1;
			}
		}

		var results = new ArrayList<ItemStack>();
		results.add(recipe.getResult());
		if (recipe.getResult().getItem() instanceof BlockItem item && item.getBlock() instanceof PotFoodBlock block) {
			results.add(block.asBowls());
		}
		builder.addSlot(RecipeIngredientRole.OUTPUT, 131 - (5 - width) * 9, 10)
				.setOutputSlotBackground()
				.addItemStacks(results);
	}


	private List<ItemStack[]> compile(List<Ingredient> list) {
		Int2ObjectLinkedOpenHashMap<ItemStack[]> set = new Int2ObjectLinkedOpenHashMap<>();
		for (var e : list) {
			if (e.isEmpty()) {
				set.put(1, new ItemStack[0]);
				continue;
			}
			var stacks = e.getItems();
			int result = 1;
			for (var stack : stacks) {
				int hash;
				if (stack.isEmpty()) {
					hash = 0;
				} else {
					hash = BuiltInRegistries.ITEM.getId(stack.getItem());
					var tag = stack.getTag();
					if (tag != null) {
						hash += tag.hashCode() * 15;
					}
				}
				result = 31 * result + hash;
			}
			var old = set.get(result);
			if (old != null) {
				for (var x : old) {
					x.grow(1);
				}
			} else {
				var copy = stacks.clone();
				for (int i = 0; i < copy.length; i++)
					copy[i] = copy[i].copy();
				set.put(result, copy);
			}
		}
		return new ArrayList<>(set.values());
	}

}
