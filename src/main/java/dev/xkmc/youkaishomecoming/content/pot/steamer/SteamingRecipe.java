package dev.xkmc.youkaishomecoming.content.pot.steamer;

import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class SteamingRecipe extends AbstractCookingRecipe {

	public SteamingRecipe(ResourceLocation id, String group, CookingBookCategory category,
						  Ingredient ingredient, ItemStack stack, float exp, int time) {
		super(YHBlocks.STEAM_RT.get(), id, group, category, ingredient, stack, exp, time);
	}

	public ItemStack getToastSymbol() {
		return YHBlocks.STEAMER_RACK.asStack();
	}

	public RecipeSerializer<?> getSerializer() {
		return YHBlocks.STEAM_RS.get();
	}
}