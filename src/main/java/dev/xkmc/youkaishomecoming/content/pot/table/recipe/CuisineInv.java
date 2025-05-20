package dev.xkmc.youkaishomecoming.content.pot.table.recipe;

import dev.xkmc.l2library.serial.recipe.BaseRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public record CuisineInv(ResourceLocation base, List<ItemStack> list, int start, boolean isComplete)
		implements BaseRecipe.RecInv<CuisineRecipe<?>> {

	@Override
	public int getContainerSize() {
		return list.size() - start;
	}

	@Override
	public boolean isEmpty() {
		for (int i = start; i < list.size(); i++) {
			if (!list.get(i).isEmpty()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public ItemStack getItem(int i) {
		return list.get(i + start);
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
