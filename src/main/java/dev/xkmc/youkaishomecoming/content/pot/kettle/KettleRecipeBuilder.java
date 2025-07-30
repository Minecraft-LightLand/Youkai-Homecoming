package dev.xkmc.youkaishomecoming.content.pot.kettle;

import dev.xkmc.l2library.serial.recipe.BaseRecipeBuilder;
import dev.xkmc.youkaishomecoming.content.item.fluid.IYHFluidHolder;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.tags.TagKey;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.function.Consumer;

public class KettleRecipeBuilder extends BaseRecipeBuilder<
		KettleRecipeBuilder,
		KettleRecipe,
		KettleRecipe,
		SimpleContainer
		> {

	public KettleRecipeBuilder(IYHFluidHolder output, int time) {
		this(new FluidStack(output.source(), 1000), time);
	}

	public KettleRecipeBuilder(Fluid output, int time) {
		this(new FluidStack(output, 1000), time);
	}

	public KettleRecipeBuilder(FluidStack output, int time) {
		super(YHBlocks.KETTLE_RS.get());
		recipe.result = output;
		recipe.time = time;
	}

	public KettleRecipeBuilder addIngredient(ItemLike item) {
		return addIngredient(Ingredient.of(item));
	}

	public KettleRecipeBuilder addIngredient(ItemLike item, int count) {
		for (int i = 0; i < count; i++)
			addIngredient(item);
		return this;
	}

	public KettleRecipeBuilder addIngredient(TagKey<Item> item) {
		return addIngredient(Ingredient.of(item));
	}

	public KettleRecipeBuilder addIngredient(TagKey<Item> item, int count) {
		for (int i = 0; i < count; i++)
			addIngredient(item);
		return this;
	}

	public KettleRecipeBuilder addIngredient(Ingredient ing) {
		recipe.input.add(ing);
		return this;
	}

	public void save(Consumer<FinishedRecipe> pvd) {
		super.save(pvd, recipe.result.getFluid().builtInRegistryHolder().unwrapKey().orElseThrow().location());
	}

}
