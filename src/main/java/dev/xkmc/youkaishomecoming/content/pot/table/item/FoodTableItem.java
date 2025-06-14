package dev.xkmc.youkaishomecoming.content.pot.table.item;

import dev.xkmc.youkaishomecoming.content.pot.table.food.FoodModelHelper;
import dev.xkmc.youkaishomecoming.content.pot.table.food.FoodTableItemHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FoodTableItem implements TableItem {

	private final List<ItemStack> contents;
	private final ItemStack current;

	@Nullable
	private final FoodTableItemHolder holder;

	public FoodTableItem(ItemStack current) {
		this.contents = List.of();
		this.current = current;
		holder = FoodModelHelper.find(current);
	}

	private FoodTableItem(ItemStack current, FoodTableItemHolder holder, List<ItemStack> contents) {
		this.contents = contents;
		this.current = current;
		this.holder = holder;
	}

	@Override
	public int getCost(TableItem prev) {
		if (holder != null && prev instanceof TableItemManager)
			return holder.count();
		return 1;
	}

	@Override
	public Optional<TableItem> find(Level level, ItemStack stack) {
		return Optional.empty();//TODO
	}

	public Optional<ItemStack> doTransform() {
		return contents.isEmpty() ? Optional.of(current) : Optional.empty();
	}

	@Override
	public int step() {
		return contents.size() + 1;
	}

	@Override
	public Optional<ItemStack> complete(Level level) {
		if (contents.isEmpty()) {
			return Optional.of(current.copy());
		}
		return Optional.empty();//TODO
	}

	@Override
	public List<ResourceLocation> getModels() {
		if (holder == null) return List.of();
		if (contents.isEmpty() || holder.base().additional == null)
			return List.of(holder.model().modelLoc());
		List<ResourceLocation> ans = new ArrayList<>();
		ans.add(holder.model().modelLoc());
		holder.base().additional.buildContents(ans, contents);
		return ans;
	}

}
