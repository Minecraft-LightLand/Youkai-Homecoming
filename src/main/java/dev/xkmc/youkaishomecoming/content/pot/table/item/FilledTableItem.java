package dev.xkmc.youkaishomecoming.content.pot.table.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;

public class FilledTableItem implements TableItem {

	private final FilledTableItemBase base;
	private final List<ItemStack> contents;
	private final ItemStack current;
	private final int step;

	public FilledTableItem(FilledTableItemBase base, List<ItemStack> contents, ItemStack current, int step) {
		this.base = base;
		this.contents = contents;
		this.current = current;
		this.step = step;
	}

	@Override
	public Optional<TableItem> find(Level level, ItemStack stack) {
		return Optional.empty();//TODO
	}

	@Override
	public int step() {
		return step;
	}

	@Override
	public Optional<ItemStack> complete(Level level) {
		return Optional.of(current.copy());
	}

	@Override
	public List<ResourceLocation> getModels() {
		return List.of();//TODO
	}

}
