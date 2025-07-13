package dev.xkmc.youkaishomecoming.content.pot.cooking.core;

import dev.xkmc.l2library.serial.recipe.BaseRecipe;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.pot.base.TimedRecipe;
import dev.xkmc.youkaishomecoming.content.pot.table.recipe.CuisineInv;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

import java.util.List;

@SerialClass
public abstract class PotCookingRecipe<T extends PotCookingRecipe<T>> extends BaseRecipe<T, PotCookingRecipe<?>, CookingInv>
		implements TimedRecipe {

	@SerialClass.SerialField
	protected ItemStack result = ItemStack.EMPTY;
	@SerialClass.SerialField
	protected int time;

	public PotCookingRecipe(ResourceLocation id, RecType<T, PotCookingRecipe<?>, CookingInv> fac) {
		super(id, fac);
	}

	public abstract List<Ingredient> getInput();

	@Override
	public boolean canCraftInDimensions(int i, int i1) {
		return false;
	}

	@Override
	public ItemStack assemble(CookingInv cookingInv, RegistryAccess registryAccess) {
		return result.copy();
	}

	@Override
	public ItemStack getResultItem(RegistryAccess registryAccess) {
		return result;
	}

	public ItemStack getResult() {
		return result;
	}

	public abstract List<Ingredient> getHints(Level level, CookingInv cont);

	public int getProcessTime() {
		return time;
	}

}
