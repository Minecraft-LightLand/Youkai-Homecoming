package dev.xkmc.youkaishomecoming.content.pot.ferment;

import dev.xkmc.l2core.base.tile.BaseTank;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public final class FermentationDummyContainer implements RecipeInput {

	private final FermentationItemContainer items;
	private final BaseTank fluids;

	public FermentationDummyContainer(FermentationItemContainer items,
									  BaseTank fluids) {
		this.items = items;
		this.fluids = fluids;
	}

	public FermentationItemContainer items() {
		return items;
	}

	public BaseTank fluids() {
		return fluids;
	}

	@Override
	public ItemStack getItem(int i) {
		return items().getItem(i);
	}

	@Override
	public int size() {
		return items().getContainerSize();
	}

}
