package dev.xkmc.youkaishomecoming.compat.create;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.Create;
import com.simibubi.create.content.fluids.transfer.EmptyingRecipe;
import com.simibubi.create.content.fluids.transfer.FillingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeSerializer;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import dev.xkmc.l2core.serial.recipe.ConditionalRecipeWrapper;
import dev.xkmc.youkaishomecoming.content.item.fluid.IYHFluidHolder;
import dev.xkmc.youkaishomecoming.init.food.YHDrink;
import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;

public class CreateRecipeGen {

	public static void onRecipeGen(RegistrateRecipeProvider pvd) {
		for (var e : YHDrink.values()) {
			bottles(pvd, e);
		}
		bottles(pvd, YHItems.SOY_SAUCE_BOTTLE);
	}

	private static void bottles(RegistrateRecipeProvider pvd, IYHFluidHolder sake) {
		filling(sake.item().getId())
				.withFluidIngredients(FluidIngredient.fromFluid(sake.fluid().get(), sake.amount()))
				.withItemIngredients(Ingredient.of(sake.getContainer()))
				.output(sake.item())
				.build(ConditionalRecipeWrapper.mod(pvd, Create.ID));

		emptying(sake.item().getId().withSuffix("_emptying"))
				.withItemIngredients(Ingredient.of(sake.item()))
				.output(sake.getContainer())
				.output(sake.fluid().get(), sake.amount())
				.build(ConditionalRecipeWrapper.mod(pvd, Create.ID));
	}

	private static ProcessingRecipeBuilder<FillingRecipe> filling(ResourceLocation id) {
		ProcessingRecipeSerializer<FillingRecipe> ser = AllRecipeTypes.FILLING.getSerializer();
		return new ProcessingRecipeBuilder<>(ser.getFactory(), id);
	}

	private static ProcessingRecipeBuilder<EmptyingRecipe> emptying(ResourceLocation id) {
		ProcessingRecipeSerializer<EmptyingRecipe> ser = AllRecipeTypes.EMPTYING.getSerializer();
		return new ProcessingRecipeBuilder<>(ser.getFactory(), id);
	}

}
