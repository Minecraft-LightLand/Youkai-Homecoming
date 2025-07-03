package dev.xkmc.youkaishomecoming.content.pot.basin;

import dev.xkmc.l2library.serial.recipe.BaseRecipe;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

@SerialClass
public abstract class BasinRecipe<T extends BasinRecipe<T>> extends BaseRecipe<T, BasinRecipe<?>, BasinInput> {

	public BasinRecipe(ResourceLocation id, RecType<T, BasinRecipe<?>, BasinInput> fac) {
		super(id, fac);
	}

	@Override
	public ItemStack assemble(BasinInput basinInput, RegistryAccess registryAccess) {
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
	public ItemStack getResultItem(RegistryAccess access) {
		return ItemStack.EMPTY;
	}

}
