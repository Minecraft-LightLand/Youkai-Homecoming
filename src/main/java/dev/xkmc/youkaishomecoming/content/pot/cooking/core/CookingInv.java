package dev.xkmc.youkaishomecoming.content.pot.cooking.core;

import dev.xkmc.l2library.serial.recipe.BaseRecipe;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public record CookingInv(Item container, List<ItemStack> list, boolean isComplete)
		implements BaseRecipe.RecInv<PotCookingRecipe<?>> {

	@Override
	public int getContainerSize() {
		return list.size();
	}

	@Override
	public boolean isEmpty() {
		for (ItemStack stack : list) {
			if (!stack.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public ItemStack getItem(int i) {
		return list.get(i);
	}

	@Override
	public ItemStack removeItem(int index, int count) {
		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack removeItemNoUpdate(int index) {
		return ItemStack.EMPTY;
	}

	@Override
	public void setItem(int index, ItemStack stack) {

	}

	@Override
	public void setChanged() {

	}

	@Override
	public boolean stillValid(Player player) {
		return false;
	}

	@Override
	public void clearContent() {

	}

}
