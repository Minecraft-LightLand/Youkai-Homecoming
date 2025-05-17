package dev.xkmc.youkaishomecoming.content.pot.table.item;

import cpw.mods.util.Lazy;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class IngredientTableItem extends BaseTableItem {

	private final TableItem prev;
	private final Lazy<Ingredient> ingredient;
	private final TableModelBuilder model;
	private final int step;
	@Nullable
	private VariantTableItemBase variant;

	public IngredientTableItem(BaseTableItem prev, Supplier<Ingredient> ingredient, TableModelBuilder model) {
		this.prev = prev;
		this.ingredient = Lazy.of(ingredient);
		this.model = model;
		this.step = prev.step() + 1;
	}

	public Ingredient ingredient() {
		return ingredient.get();
	}

	@Override
	public int step() {
		return step;
	}

	public VariantTableItemBase asBase() {
		variant = VariantTableItemBase.create(this, model.id());
		return variant;
	}

	@Override
	public Optional<TableItem> find(Level level, ItemStack stack) {
		var ans = super.find(level, stack);
		if (ans.isPresent()) return ans;
		if (variant == null) return Optional.empty();
		return variant.find(level, stack);
	}

	@Override
	public List<ResourceLocation> getModels() {
		return List.of(model.id());
	}
}
