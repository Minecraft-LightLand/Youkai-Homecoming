package dev.xkmc.youkaishomecoming.content.pot.cooking.core;

import dev.xkmc.l2library.serial.recipe.BaseRecipe;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.pot.table.recipe.CuisineInv;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

import java.util.List;

@SerialClass
public abstract class SoupBaseRecipe<T extends SoupBaseRecipe<T>> extends BaseRecipe<T, SoupBaseRecipe<?>, CookingInv> {

	public static final ResourceLocation DEF = YoukaisHomecoming.loc("water");

	@SerialClass.SerialField
	protected ResourceLocation id = DEF;

	public SoupBaseRecipe(ResourceLocation id, RecType<T, SoupBaseRecipe<?>, CookingInv> fac) {
		super(id, fac);
	}

	public abstract int getIngredientCount();

	public abstract void removeConsumed(List<ItemStack> list);

	@Override
	public boolean canCraftInDimensions(int i, int i1) {
		return false;
	}

	@Override
	public ItemStack assemble(CookingInv cookingInv, RegistryAccess registryAccess) {
		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack getResultItem(RegistryAccess registryAccess) {
		return ItemStack.EMPTY;
	}

	public ItemStack getResult() {
		return ItemStack.EMPTY;
	}

}
