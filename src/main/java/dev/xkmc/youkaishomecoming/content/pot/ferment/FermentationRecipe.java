package dev.xkmc.youkaishomecoming.content.pot.ferment;

import dev.xkmc.l2core.serial.recipe.BaseRecipe;
import dev.xkmc.youkaishomecoming.content.pot.base.TimedRecipe;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;

public abstract class FermentationRecipe<T extends FermentationRecipe<T>> extends BaseRecipe<T, FermentationRecipe<?>, FermentationDummyContainer>
		implements TimedRecipe {

	public FermentationRecipe(RecType<T, FermentationRecipe<?>, FermentationDummyContainer> fac) {
		super(fac);
	}

	@Override
	public boolean canCraftInDimensions(int i, int i1) {
		return false;
	}

	@Override
	public ItemStack getResultItem(HolderLookup.Provider provider) {
		return ItemStack.EMPTY;
	}

	public boolean isSimple() {
		return false;
	}

}
