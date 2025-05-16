package dev.xkmc.youkaishomecoming.content.pot.table.recipe;

import dev.xkmc.l2library.serial.recipe.BaseRecipe;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

@SerialClass
public abstract class CuisineTableRecipe<T extends CuisineTableRecipe<T>> extends BaseRecipe<T, CuisineTableRecipe<?>, CuisineInv> {

	public CuisineTableRecipe(ResourceLocation id, RecType<T, CuisineTableRecipe<?>, CuisineInv> fac) {
		super(id, fac);
	}

	public abstract ResourceLocation base();

	public abstract List<Ingredient> getCustomIngredients();

	@Override
	public boolean canCraftInDimensions(int i, int i1) {
		return false;
	}

}
