package dev.xkmc.youkaishomecoming.content.pot.ferment;

import dev.xkmc.l2library.base.tile.BaseTank;
import net.minecraft.world.SimpleContainer;

public final class FermentationDummyContainer extends SimpleContainer {

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

}
