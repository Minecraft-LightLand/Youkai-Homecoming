package dev.xkmc.youkaishomecoming.content.pot.table.item;

import dev.xkmc.youkaishomecoming.content.pot.table.model.FixedModelHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class BaseTableItem implements TableItem {

	private final List<IngredientTableItem> children = new ArrayList<>();

	public synchronized IngredientTableItem with(FixedModelHolder id, Supplier<Ingredient> item) {
		var ans = new IngredientTableItem(this, item, id);
		children.add(ans);
		return ans;
	}

	public IngredientTableItem with(FixedModelHolder id, ItemLike item) {
		return with(id, () -> Ingredient.of(item.asItem()));
	}

	public IngredientTableItem with(FixedModelHolder id, TagKey<Item> item) {
		return with(id, () -> Ingredient.of(item));
	}

	public IngredientTableItem addNext(FixedModelHolder id) {
		return with(id, () -> Ingredient.EMPTY);
	}

	@Override
	public Optional<TableItem> find(Level level, ItemStack stack) {
		for (var e : children) {
			if (stack.isEmpty()) {
				if (e.ingredient().isEmpty()) {
					return Optional.of(e);
				}
			} else if (e.ingredient().test(stack)) {
				return Optional.of(e);
			}
		}
		return Optional.empty();
	}

	public int step() {
		return 0;
	}

	@Override
	public Optional<ItemStack> complete(Level level) {
		return Optional.empty();
	}

	@Override
	public List<ResourceLocation> getModels() {
		return List.of();
	}

}
