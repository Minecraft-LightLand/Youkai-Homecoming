package dev.xkmc.youkaishomecoming.content.pot.kettle;

import dev.xkmc.youkaishomecoming.content.pot.base.BasePotRecipe;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.client.recipebook.CookingPotRecipeBookTab;

public class KettleRecipe extends BasePotRecipe {

	public KettleRecipe(String group, @Nullable CookingPotRecipeBookTab tab, NonNullList<Ingredient> inputItems, ItemStack output, ItemStack container, float experience, int cookTime) {
		super(group, tab, inputItems, output, container, experience, cookTime);
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return YHBlocks.KETTLE_RS.get();
	}

	@Override
	public RecipeType<?> getType() {
		return YHBlocks.KETTLE_RT.get();
	}

	@Override
	public ItemStack getToastSymbol() {
		return YHBlocks.KETTLE.asStack();
	}

}
