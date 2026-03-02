package dev.xkmc.youkaishomecoming.content.pot.table.recipe;

import dev.xkmc.l2core.serial.recipe.BaseRecipe;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

@SerialClass
public abstract class BaseCuisineRecipe<T extends BaseCuisineRecipe<T>> extends CuisineRecipe<T> {

	@SerialField
	public ResourceLocation base;
	@SerialField
	public ItemStack result = ItemStack.EMPTY;

	public BaseCuisineRecipe(BaseRecipe.RecType<T, CuisineRecipe<?>, CuisineInv> fac) {
		super(fac);
	}

	@Override
	public ResourceLocation base() {
		return base;
	}

	@Override
	public ItemStack assemble(CuisineInv inv, HolderLookup.Provider access) {
		return result.copy();
	}

	@Override
	public ItemStack getResultItem(HolderLookup.Provider access) {
		return result;
	}

	@Override
	public ItemStack getResult() {
		return result;
	}

}
