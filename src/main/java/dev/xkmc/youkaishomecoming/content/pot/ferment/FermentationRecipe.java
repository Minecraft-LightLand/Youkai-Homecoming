package dev.xkmc.youkaishomecoming.content.pot.ferment;

import dev.xkmc.l2library.serial.recipe.BaseRecipe;
import dev.xkmc.youkaishomecoming.content.pot.base.TimedRecipe;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public abstract class FermentationRecipe<T extends FermentationRecipe<T>> extends BaseRecipe<T, FermentationRecipe<?>, FermentationDummyContainer>
		implements TimedRecipe {

	public FermentationRecipe(ResourceLocation id, RecType<T, FermentationRecipe<?>, FermentationDummyContainer> fac) {
		super(id, fac);
	}

	@Override
	public boolean canCraftInDimensions(int i, int i1) {
		return false;
	}

	@Override
	public ItemStack getResultItem(RegistryAccess registryAccess) {
		return ItemStack.EMPTY;
	}

}
