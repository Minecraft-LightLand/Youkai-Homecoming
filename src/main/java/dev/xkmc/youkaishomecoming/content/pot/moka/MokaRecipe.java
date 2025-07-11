package dev.xkmc.youkaishomecoming.content.pot.moka;

import dev.xkmc.youkaishomecoming.content.pot.base.BasePotRecipe;
import dev.xkmc.youkaishomecoming.init.registrate.CoffeeBlocks;
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
		return CoffeeBlocks.MOKA_RS.get();
	}

	@Override
	public RecipeType<?> getType() {
		return CoffeeBlocks.MOKA_RT.get();
	}

	@Override
	public ItemStack getToastSymbol() {
		return CoffeeBlocks.MOKA.asStack();
	}

}
