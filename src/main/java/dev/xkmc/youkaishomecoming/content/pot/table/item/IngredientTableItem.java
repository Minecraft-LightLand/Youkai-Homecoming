package dev.xkmc.youkaishomecoming.content.pot.table.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class IngredientTableItem extends BaseTableItem {

	private final TableItem prev;
	private final Ingredient ingredient;
	private final int step;
	@Nullable
	private VariantTableItemBase variant;

	public IngredientTableItem(BaseTableItem prev, Ingredient ingredient) {
		this.prev = prev;
		this.ingredient = ingredient;
		this.step = prev.step() + 1;
	}

	public Ingredient ingredient() {
		return ingredient;
	}

	@Override
	public int step() {
		return step;
	}

	public VariantTableItemBase asBase(ResourceLocation id) {
		variant = VariantTableItemBase.create(this, id);
		return variant;
	}

	@Override
	public Optional<TableItem> find(Level level, ItemStack stack) {
		var ans = super.find(level, stack);
		if (ans.isPresent() || variant == null) return ans;
		return variant.find(level, stack);
	}

}
