package dev.xkmc.youkaishomecoming.content.pot.table.item;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BaseTableItem implements TableItem {

	private final List<IngredientTableItem> children = new ArrayList<>();

	public IngredientTableItem with(Ingredient item) {
		var ans = new IngredientTableItem(this, item);
		children.add(ans);
		return ans;
	}

	public IngredientTableItem with(Item item) {
		return with(Ingredient.of(item));
	}

	public IngredientTableItem with(TagKey<Item> item) {
		return with(Ingredient.of(item));
	}

	@Override
	public Optional<TableItem> find(Level level, ItemStack stack) {
		for (var e : children) {
			if (e.ingredient().test(stack)) {
				return Optional.of(e);
			}
		}
		return Optional.empty();
	}

	public int step() {
		return 0;
	}

}
