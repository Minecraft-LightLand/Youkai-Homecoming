package dev.xkmc.youkaihomecoming.content.pot.base;

import com.google.gson.JsonObject;
import dev.xkmc.l2serial.util.Wrappers;
import dev.xkmc.youkaihomecoming.mixin.CookingPotRecipeAccessor;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.client.recipebook.CookingPotRecipeBookTab;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.common.registry.ModRecipeSerializers;

public record BasePotSerializer<T extends BasePotRecipe>(Factory<T> factory) implements RecipeSerializer<T> {

	public interface Factory<R extends BasePotRecipe> {

		R create(ResourceLocation id, String group, @Nullable CookingPotRecipeBookTab tab,
				 NonNullList<Ingredient> inputItems, ItemStack output, ItemStack container,
				 float experience, int cookTime);

	}

	private T wrap(CookingPotRecipe recipe) {
		return factory.create(recipe.getId(), recipe.getGroup(), recipe.getRecipeBookTab(),
				recipe.getIngredients(), ((CookingPotRecipeAccessor) recipe).getOutput(), recipe.getOutputContainer(),
				recipe.getExperience(), recipe.getCookTime());
	}

	@Override
	public T fromJson(ResourceLocation id, JsonObject json) {
		return wrap(getOriginal().fromJson(id, json));
	}

	@Override
	public @Nullable T fromNetwork(ResourceLocation id, FriendlyByteBuf packet) {
		var old = getOriginal().fromNetwork(id, packet);
		if (old == null) return null;
		return wrap(old);
	}

	@Override
	public void toNetwork(FriendlyByteBuf packet, T recipe) {
		getOriginal().toNetwork(packet, recipe);
	}

	private static RecipeSerializer<CookingPotRecipe> getOriginal() {
		return Wrappers.cast(ModRecipeSerializers.COOKING.get());
	}

}
