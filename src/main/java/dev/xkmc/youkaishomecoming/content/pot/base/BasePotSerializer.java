package dev.xkmc.youkaishomecoming.content.pot.base;

import com.mojang.serialization.MapCodec;
import dev.xkmc.l2serial.util.Wrappers;
import dev.xkmc.youkaishomecoming.mixin.CookingPotRecipeAccessor;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.client.recipebook.CookingPotRecipeBookTab;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.common.registry.ModRecipeSerializers;

public record BasePotSerializer<T extends BasePotRecipe>(Factory<T> factory) implements RecipeSerializer<T> {

	public interface Factory<R extends BasePotRecipe> {

		R create(String group, @Nullable CookingPotRecipeBookTab tab,
				 NonNullList<Ingredient> inputItems, ItemStack output, ItemStack container,
				 float experience, int cookTime);

	}

	public T wrap(CookingPotRecipe recipe) {
		return factory.create(recipe.getGroup(), recipe.getRecipeBookTab(),
				recipe.getIngredients(), ((CookingPotRecipeAccessor) recipe).getOutput(), recipe.getOutputContainer(),
				recipe.getExperience(), recipe.getCookTime());
	}

	@Override
	public MapCodec<T> codec() {
		return getOriginal().codec().xmap(this::wrap, e -> e);
	}

	@Override
	public StreamCodec<RegistryFriendlyByteBuf, T> streamCodec() {
		return getOriginal().streamCodec().map(this::wrap, e -> e);
	}

	private static RecipeSerializer<CookingPotRecipe> getOriginal() {
		return Wrappers.cast(ModRecipeSerializers.COOKING.get());
	}

}
