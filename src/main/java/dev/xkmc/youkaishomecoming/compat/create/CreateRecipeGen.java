package dev.xkmc.youkaishomecoming.compat.create;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.Create;
import com.simibubi.create.content.fluids.transfer.EmptyingRecipe;
import com.simibubi.create.content.fluids.transfer.FillingRecipe;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeSerializer;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import dev.xkmc.l2library.serial.recipe.ConditionalRecipeWrapper;
import dev.xkmc.youkaishomecoming.content.item.fluid.BottledDrinkSet;
import dev.xkmc.youkaishomecoming.content.item.fluid.IYHFluidHolder;
import dev.xkmc.youkaishomecoming.init.data.TagRef;
import dev.xkmc.youkaishomecoming.init.data.YHTagGen;
import dev.xkmc.youkaishomecoming.init.food.YHDrink;
import dev.xkmc.youkaishomecoming.init.food.YHFood;
import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;

public class CreateRecipeGen {

	public static void onRecipeGen(RegistrateRecipeProvider pvd) {
		for (var e : YHDrink.values()) {
			bottles(pvd, e);
		}
		bottles(pvd, YHItems.SOY_SAUCE_BOTTLE);
		bottles(pvd, YHItems.MAYONNAISE);
		bottles(pvd, YHItems.CREAM);

		mixing(YHItems.CREAM.getId())
				.withFluidIngredients(FluidIngredient.fromTag(Tags.Fluids.MILK, 1000))
				.require(YHTagGen.ICE)
				.output(YHItems.CREAM.source(), 250)
				.build(ConditionalRecipeWrapper.mod(pvd, Create.ID));

		mixing(YHItems.MAYONNAISE.getId())
				.require(YHTagGen.BUTTER)
				.require(TagRef.EGGS)
				.require(Items.SUGAR)
				.output(YHItems.MAYONNAISE.source(), 500)
				.build(ConditionalRecipeWrapper.mod(pvd, Create.ID));

		mixing(YHFood.BUTTER.item.getId())
				.withFluidIngredients(FluidIngredient.fromFluid(YHItems.CREAM.source(), 500))
				.requiresHeat(HeatCondition.HEATED)
				.output(YHFood.BUTTER.item)
				.build(ConditionalRecipeWrapper.mod(pvd, Create.ID));

	}

	private static void bottles(RegistrateRecipeProvider pvd, IYHFluidHolder sake) {
		filling(sake.item().getId())
				.withFluidIngredients(FluidIngredient.fromFluid(sake.source(), sake.amount()))
				.withItemIngredients(Ingredient.of(sake.getContainer()))
				.output(sake.item())
				.build(ConditionalRecipeWrapper.mod(pvd, Create.ID));

		emptying(sake.item().getId().withSuffix("_emptying"))
				.withItemIngredients(Ingredient.of(sake.item()))
				.output(sake.getContainer())
				.output(sake.source(), sake.amount())
				.build(ConditionalRecipeWrapper.mod(pvd, Create.ID));

		if (sake.bottleSet() instanceof BottledDrinkSet set) {
			filling(set.bottle.getId())
					.withFluidIngredients(FluidIngredient.fromFluid(sake.source(), 1000))
					.withItemIngredients(Ingredient.of(YHItems.SAKE_BOTTLE))
					.output(set.bottle)
					.build(ConditionalRecipeWrapper.mod(pvd, Create.ID));

			emptying(set.bottle.getId().withSuffix("_emptying"))
					.withItemIngredients(Ingredient.of(set.bottle))
					.output(YHItems.SAKE_BOTTLE)
					.output(sake.source(), 1000)
					.build(ConditionalRecipeWrapper.mod(pvd, Create.ID));
		}
	}

	private static ProcessingRecipeBuilder<FillingRecipe> filling(ResourceLocation id) {
		ProcessingRecipeSerializer<FillingRecipe> ser = AllRecipeTypes.FILLING.getSerializer();
		return new ProcessingRecipeBuilder<>(ser.getFactory(), id);
	}

	private static ProcessingRecipeBuilder<EmptyingRecipe> emptying(ResourceLocation id) {
		ProcessingRecipeSerializer<EmptyingRecipe> ser = AllRecipeTypes.EMPTYING.getSerializer();
		return new ProcessingRecipeBuilder<>(ser.getFactory(), id);
	}

	private static ProcessingRecipeBuilder<EmptyingRecipe> mixing(ResourceLocation id) {
		ProcessingRecipeSerializer<EmptyingRecipe> ser = AllRecipeTypes.MIXING.getSerializer();
		return new ProcessingRecipeBuilder<>(ser.getFactory(), id);
	}

}
