package dev.xkmc.youkaishomecoming.content.pot.table.item;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VariantTableItem implements TableItem {

	private final VariantTableItemBase base;
	private final List<ItemStack> contents;

	public VariantTableItem(VariantTableItemBase base, List<ItemStack> contents) {
		this.base = base;
		this.contents = contents;
	}

	@Override
	public Optional<TableItem> find(Level level, ItemStack stack) {
		var ans = new ArrayList<>(contents);
		ans.add(stack.copyWithCount(1));
		if (!base.isValid(level, ans)) return Optional.empty();
		return Optional.of(new VariantTableItem(base, ans));
	}

}
