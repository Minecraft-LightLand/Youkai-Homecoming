package dev.xkmc.youkaishomecoming.content.pot.kettle;

import dev.xkmc.l2core.base.tile.BaseContainer;
import net.minecraft.world.item.crafting.RecipeInput;

public class KettleContainer extends BaseContainer<KettleContainer> implements RecipeInput {

	public KettleContainer(int size) {
		super(size);
	}

	@Override
	public int size() {
		return getContainerSize();
	}

}
