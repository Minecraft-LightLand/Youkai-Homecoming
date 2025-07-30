package dev.xkmc.youkaishomecoming.content.pot.table.item;

import dev.xkmc.youkaishomecoming.content.pot.table.model.FixedModelHolder;
import dev.xkmc.youkaishomecoming.content.pot.table.model.VariantModelHolder;
import dev.xkmc.youkaishomecoming.content.pot.table.recipe.CuisineInv;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.Lazy;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class IngredientTableItem extends BaseTableItem {

	public static final Map<ResourceLocation, IngredientTableItem> FIXED = new ConcurrentHashMap<>();

	private final TableItem prev;
	private final Lazy<Ingredient> ingredient;
	private final FixedModelHolder model;
	private final int step;
	@Nullable
	private VariantTableItemBase variant;
	@Nullable
	private ResourceLocation id;

	public IngredientTableItem(BaseTableItem prev, Supplier<Ingredient> ingredient, FixedModelHolder model) {
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

	public synchronized VariantTableItemBase asBase(ResourceLocation id) {
		if (variant != null)
			throw new IllegalStateException("Already has a base");
		if (!(model instanceof VariantModelHolder holder))
			throw new IllegalStateException("Variant item requires a variant model");
		variant = VariantTableItemBase.create(this, id, holder);
		return variant;
	}

	public ResourceLocation getId() {
		if (id == null) {
			throw new IllegalStateException("Not an exported model");
		}
		return id;
	}

	public ResourceLocation register() {
		var strs = model.modelLoc().getPath().split("/");
		id = model.modelLoc().withPath(strs[strs.length - 1]);
		FIXED.put(id, this);
		return id;
	}

	public void collectIngredients(List<Ingredient> list) {
		prev.collectIngredients(list);
		list.add(ingredient.get());
	}

	@Override
	public Optional<TableItem> find(Level level, ItemStack stack) {
		var ans = super.find(level, stack);
		if (ans.isPresent()) return ans;
		if (variant == null) return Optional.empty();
		return variant.find(level, stack);
	}

	@Override
	public Optional<ItemStack> complete(Level level) {
		if (id == null) return Optional.empty();
		var cont = new CuisineInv(id, List.of(), 0, true);
		return level.getRecipeManager().getRecipeFor(YHBlocks.CUISINE_RT.get(), cont, level)
				.map(r -> r.assemble(cont, level.registryAccess()));
	}


	@Override
	public List<Ingredient> getHints(Level level) {
		List<Ingredient> ans = super.getHints(level);
		if (variant != null) {
			ans.addAll(variant.getHints(level));
		}
		return ans;
	}

	@Override
	public List<ResourceLocation> getModels() {
		return List.of(model.modelLoc());
	}
}
