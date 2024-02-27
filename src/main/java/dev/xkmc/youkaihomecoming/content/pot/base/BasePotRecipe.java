package dev.xkmc.youkaihomecoming.content.pot.base;

import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.client.recipebook.CookingPotRecipeBookTab;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;

public abstract class BasePotRecipe extends CookingPotRecipe {

	public BasePotRecipe(ResourceLocation id, String group,
						 @Nullable CookingPotRecipeBookTab tab,
						 NonNullList<Ingredient> inputItems,
						 ItemStack output, ItemStack container,
						 float experience, int cookTime) {
		super(id, group, tab, inputItems, output, container, experience, cookTime);
	}

	public abstract RecipeSerializer<?> getSerializer();

	public abstract RecipeType<?> getType();

	public abstract ItemStack getToastSymbol();

}
