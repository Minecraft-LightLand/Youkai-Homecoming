package dev.xkmc.youkaihomecoming.content.pot.base;

import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.RecipeMatcher;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.client.recipebook.CookingPotRecipeBookTab;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;

import java.util.ArrayList;
import java.util.List;

public abstract class BasePotRecipe extends CookingPotRecipe {

	public BasePotRecipe(ResourceLocation id, String group,
						 @Nullable CookingPotRecipeBookTab tab,
						 NonNullList<Ingredient> inputItems,
						 ItemStack output, ItemStack container,
						 float experience, int cookTime) {
		super(id, group, tab, inputItems, output, container, experience, cookTime);
	}

	public boolean matches(RecipeWrapper inv, Level level) {
		List<ItemStack> inputs = new ArrayList<>();
		int i = 0;

		for(int j = 0; j < 6; ++j) {
			ItemStack itemstack = inv.getItem(j);
			if (!itemstack.isEmpty()) {
				++i;
				inputs.add(itemstack);
			}
		}

		return i == this.getIngredients().size() &&
				RecipeMatcher.findMatches(inputs, this.getIngredients()) != null;
	}

	public abstract RecipeSerializer<?> getSerializer();

	public abstract RecipeType<?> getType();

	public abstract ItemStack getToastSymbol();

}
