package dev.xkmc.youkaishomecoming.content.pot.cooking.core;

import dev.xkmc.l2library.base.tile.BaseContainer;
import net.minecraft.world.item.ItemStack;

public class CookingItemContainer extends BaseContainer<CookingItemContainer> {

	private final CookingBlockEntity be;

	public CookingItemContainer(CookingBlockEntity be, int size) {
		super(size);
		this.be = be;
		add(be);
	}

	@Override
	public boolean canAddItem(ItemStack stack) {
		return be.tryAddItem(stack, true) && super.canAddItem(stack);
	}

	@Override
	public boolean canPlaceItem(int slot, ItemStack stack) {
		return (stack.isEmpty() || be.tryAddItem(stack, true)) && super.canPlaceItem(slot, stack);
	}

}
