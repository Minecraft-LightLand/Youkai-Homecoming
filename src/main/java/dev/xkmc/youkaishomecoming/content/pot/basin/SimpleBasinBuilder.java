package dev.xkmc.youkaishomecoming.content.pot.basin;

import dev.xkmc.l2library.serial.recipe.BaseRecipeBuilder;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class SimpleBasinBuilder extends BaseRecipeBuilder<SimpleBasinBuilder, SimpleBasinRecipe, BasinRecipe<?>, BasinInput> {

	public SimpleBasinBuilder(Fluid output, int amount) {
		this(new FluidStack(output, amount));
	}

	public SimpleBasinBuilder(FluidStack output) {
		super(YHBlocks.BASIN_RS.get());
		recipe.output = output;
	}

	public SimpleBasinBuilder setInput(ItemLike item) {
		return setInput(Ingredient.of(item));
	}

	public SimpleBasinBuilder setInput(ItemLike item, int count) {
		for (int i = 0; i < count; i++)
			setInput(item);
		return this;
	}

	public SimpleBasinBuilder setInput(TagKey<Item> item) {
		return setInput(Ingredient.of(item));
	}

	public SimpleBasinBuilder setInput(Ingredient ing) {
		recipe.input = ing;
		return this;
	}

}
