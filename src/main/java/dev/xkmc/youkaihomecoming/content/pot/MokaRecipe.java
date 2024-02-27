package dev.xkmc.youkaihomecoming.content.pot;

import dev.xkmc.youkaihomecoming.init.registrate.YHBlocks;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.client.recipebook.CookingPotRecipeBookTab;

public class MokaRecipe extends BasePotRecipe {

	public MokaRecipe(ResourceLocation id, String group, @Nullable CookingPotRecipeBookTab tab, NonNullList<Ingredient> inputItems, ItemStack output, ItemStack container, float experience, int cookTime) {
		super(id, group, tab, inputItems, output, container, experience, cookTime);
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return YHBlocks.MOKA_RS.get();
	}

	@Override
	public RecipeType<?> getType() {
		return YHBlocks.MOKA_RT.get();
	}

	@Override
	public ItemStack getToastSymbol() {
		return YHBlocks.MOKA.asStack();
	}

}
