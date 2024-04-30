package dev.xkmc.youkaishomecoming.content.pot.ferment;

import dev.xkmc.l2library.serial.recipe.BaseRecipeBuilder;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;

public class SimpleFermentationBuilder extends BaseRecipeBuilder<
		SimpleFermentationBuilder,
		SimpleFermentationRecipe,
		FermentationRecipe<?>,
		FermentationDummyContainer
		> {

	public SimpleFermentationBuilder(Fluid output, int time) {
		this(Fluids.EMPTY, output, time);
	}

	public SimpleFermentationBuilder(Fluid input, Fluid output, int time) {
		this(new FluidStack(input, 1000), new FluidStack(output, 1000), time);
	}

	public SimpleFermentationBuilder(FluidStack input, FluidStack output, int time) {
		super(YHBlocks.FERMENT_RS.get());
		recipe.inputFluid = input;
		recipe.outputFluid = output;
		recipe.time = time;
	}

	public SimpleFermentationBuilder addInput(ItemLike item) {
		return addInput(Ingredient.of(item));
	}

	public SimpleFermentationBuilder addInput(TagKey<Item> item) {
		return addInput(Ingredient.of(item));
	}

	public SimpleFermentationBuilder addInput(Ingredient ing) {
		recipe.ingredients.add(ing);
		return this;
	}

	public SimpleFermentationBuilder addOutput(ItemLike stack) {
		return addOutput(stack.asItem().getDefaultInstance());
	}

	public SimpleFermentationBuilder addOutput(ItemStack stack) {
		recipe.results.add(stack);
		return this;
	}

}
