package dev.xkmc.youkaishomecoming.content.pot.basin;

import dev.xkmc.l2core.serial.recipe.BaseRecipe;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

@SerialClass
public abstract class BasinRecipe<T extends BasinRecipe<T>> extends BaseRecipe<T, BasinRecipe<?>, BasinInput> {

	public BasinRecipe(RecType<T, BasinRecipe<?>, BasinInput> fac) {
		super(fac);
	}

	@Override
	public ItemStack assemble(BasinInput basinInput, HolderLookup.Provider registryAccess) {
		return ItemStack.EMPTY;
	}

	public FluidStack assembleFluid(BasinInput basinInput, RegistryAccess registryAccess) {
		return FluidStack.EMPTY;
	}

	@Override
	public boolean canCraftInDimensions(int i, int i1) {
		return false;
	}

	@Override
	public ItemStack getResultItem(HolderLookup.Provider access) {
		return ItemStack.EMPTY;
	}

}
