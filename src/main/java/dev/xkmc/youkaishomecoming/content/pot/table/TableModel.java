package dev.xkmc.youkaishomecoming.content.pot.table;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public interface TableModel {

	Optional<TableModel> find(ItemStack stack);

	class BaseTableModel implements TableModel {

		private final List<IngredientTableModel> children = new ArrayList<>();

		public IngredientTableModel with(Ingredient item) {
			var ans = new IngredientTableModel(this, item);
			children.add(ans);
			return ans;
		}

		public IngredientTableModel with(Item item) {
			return with(Ingredient.of(item));
		}

		public IngredientTableModel with(TagKey<Item> item) {
			return with(Ingredient.of(item));
		}

		@Override
		public Optional<TableModel> find(ItemStack stack) {
			for (var e : children) {
				if (e.ingredient.test(stack)) {
					return Optional.of(e);
				}
			}
			return Optional.empty();
		}

	}

	class IngredientTableModel extends BaseTableModel {

		private final TableModel prev;
		private final Ingredient ingredient;
		@Nullable
		private VariantModelBase variant;

		public IngredientTableModel(TableModel prev, Ingredient ingredient) {
			this.prev = prev;
			this.ingredient = ingredient;
		}

		public VariantModelBase asBase(ResourceLocation id) {
			variant = VariantModelBase.create(id);
			return variant;
		}

		@Override
		public Optional<TableModel> find(ItemStack stack) {
			var ans = super.find(stack);
			if (ans.isPresent() || variant == null) return ans;
			return variant.find(stack);
		}

	}

	class VariantModelBase {

		private static final Map<ResourceLocation, VariantModelBase> MAP = new LinkedHashMap<>();

		public static synchronized VariantModelBase create(ResourceLocation id) {
			var ans = new VariantModelBase(id);
			MAP.put(id, ans);
			return ans;
		}

		private final ResourceLocation id;

		public VariantModelBase(ResourceLocation id) {
			this.id = id;
		}

		public Optional<TableModel> find(ItemStack stack) {
			var list = List.of(stack.copyWithCount(1));
			if (!isValid(list)) return Optional.empty();
			return Optional.of(new VariantModel(this, list));
		}

		public boolean isValid(List<ItemStack> stacks) {
			return false;//TODO
		}

	}

	class VariantModel implements TableModel {

		private final VariantModelBase base;
		private final List<ItemStack> contents;

		public VariantModel(VariantModelBase base, List<ItemStack> contents) {
			this.base = base;
			this.contents = contents;
		}

		@Override
		public Optional<TableModel> find(ItemStack stack) {
			var ans = new ArrayList<>(contents);
			ans.add(stack.copyWithCount(1));
			if (!base.isValid(ans)) return Optional.empty();
			return Optional.of(new VariantModel(base, ans));
		}

	}

}
