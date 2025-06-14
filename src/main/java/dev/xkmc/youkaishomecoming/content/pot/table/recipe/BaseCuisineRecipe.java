package dev.xkmc.youkaishomecoming.content.pot.table.recipe;

import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

@SerialClass
public abstract class BaseCuisineRecipe<T extends BaseCuisineRecipe<T>> extends CuisineRecipe<T> {

	@SerialClass.SerialField
	public ResourceLocation base;
	@SerialClass.SerialField
	public ItemStack result = ItemStack.EMPTY;

	public BaseCuisineRecipe(ResourceLocation id, RecType<T, CuisineRecipe<?>, CuisineInv> fac) {
		super(id, fac);
	}

	@Override
	public ResourceLocation base() {
		return base;
	}

	@Override
	public ItemStack assemble(CuisineInv inv, RegistryAccess access) {
		return result.copy();
	}

	@Override
	public ItemStack getResultItem(RegistryAccess access) {
		return result;
	}

	@Override
	public ItemStack getResult() {
		return result;
	}

}
