package dev.xkmc.youkaishomecoming.content.pot.base;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.RecipeMatcher;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.client.recipebook.CookingPotRecipeBookTab;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;

public abstract class BasePotRecipe extends CookingPotRecipe {

	public BasePotRecipe(String group, @Nullable CookingPotRecipeBookTab tab, NonNullList<Ingredient> inputItems, ItemStack output, ItemStack container, float experience, int cookTime) {
		super(group, tab, inputItems, output, container, experience, cookTime);
	}

	private boolean findMatch(RecipeWrapper inv, Ingredient e, int[] consume) {
		for (int i = 0; i < 4; i++) {
			var stack = inv.getItem(i);
			if (stack.isEmpty())
				continue;
			if (consume[i] > 0)
				continue;
			if (e.test(inv.getItem(i))) {
				consume[i]++;
				return true;
			}
		}
		for (int i = 0; i < 4; i++) {
			var stack = inv.getItem(i);
			if (stack.isEmpty())
				continue;
			if (consume[i] >= stack.getCount())
				continue;
			if (e.test(inv.getItem(i))) {
				consume[i]++;
				return true;
			}
		}
		return false;
	}

	public boolean matches(RecipeWrapper inv, Level level) {
		int[] consume = new int[4];
		int match = 0;
		for (var e : getIngredients()) {
			if (findMatch(inv, e, consume)) {
				match++;
			}
		}
		for (int i = 0; i < 4; i++) {
			if (consume[i] == 0 && !inv.getItem(i).isEmpty())
				return false;
		}
		return match == getIngredients().size();
	}

	public int[] getConsumption(RecipeWrapper inv) {
		int[] consume = new int[4];
		for (var e : getIngredients()) {
			findMatch(inv, e, consume);
		}
		return consume;
	}

	public abstract RecipeSerializer<?> getSerializer();

	public abstract RecipeType<?> getType();

	public abstract ItemStack getToastSymbol();

}
