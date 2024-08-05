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

import java.util.ArrayList;
import java.util.List;

public abstract class BasePotRecipe extends CookingPotRecipe {

	public BasePotRecipe(String group, @Nullable CookingPotRecipeBookTab tab, NonNullList<Ingredient> inputItems, ItemStack output, ItemStack container, float experience, int cookTime) {
		super(group, tab, inputItems, output, container, experience, cookTime);
	}

	public boolean matches(RecipeWrapper inv, Level level) {
		List<ItemStack> inputs = new ArrayList<>();
		int i = 0;

		for (int j = 0; j < 4; ++j) {
			ItemStack itemstack = inv.getItem(j);
			if (!itemstack.isEmpty()) {
				++i;
				inputs.add(itemstack);
			}
		}

		return i == getIngredients().size() &&
				RecipeMatcher.findMatches(inputs, getIngredients()) != null;
	}

	public abstract RecipeSerializer<?> getSerializer();

	public abstract RecipeType<?> getType();

	public abstract ItemStack getToastSymbol();

}
