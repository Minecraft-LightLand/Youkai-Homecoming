package dev.xkmc.youkaishomecoming.content.pot.table.item;

import dev.xkmc.youkaishomecoming.content.pot.table.model.AdditionalModelHolder;
import dev.xkmc.youkaishomecoming.content.pot.table.model.VariantModelHolder;
import dev.xkmc.youkaishomecoming.content.pot.table.model.VariantModelPart;
import dev.xkmc.youkaishomecoming.content.pot.table.recipe.CuisineInv;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class VariantTableItemBase {

	public static final Map<ResourceLocation, VariantTableItemBase> MAP = new LinkedHashMap<>();

	public static synchronized VariantTableItemBase create(IngredientTableItem base, ResourceLocation id, VariantModelHolder model) {
		var ans = new VariantTableItemBase(base, id, model);
		MAP.put(id, ans);
		return ans;
	}

	private final IngredientTableItem base;
	private final ResourceLocation id;
	protected final VariantModelHolder model;
	@Nullable
	private FoodTableItemBase click;

	private VariantTableItemBase(IngredientTableItem base, ResourceLocation id, VariantModelHolder model) {
		this.base = base;
		this.id = id;
		this.model = model;
	}

	public int step() {
		return base.step();
	}

	public ResourceLocation id() {
		return id;
	}

	public FoodTableItemBase addNextStep(@Nullable AdditionalModelHolder model) {
		click = new FoodTableItemBase(model);
		return click;
	}

	public VariantModelPart addPart(String name, int max) {
		return model.addPart(name, max);
	}

	@Nullable
	public FoodTableItemBase next() {
		return click;
	}

	public Optional<TableItem> find(Level level, ItemStack stack) {
		if (stack.isEmpty()) return Optional.empty();
		var list = List.of(stack.copyWithCount(1));
		if (!isValid(level, list)) return Optional.empty();
		return Optional.of(new VariantTableItem(this, list));
	}

	public boolean isValid(Level level, List<ItemStack> stacks) {
		var cont = new CuisineInv(id, stacks, 0, false);
		return level.getRecipeManager().getRecipeFor(YHBlocks.CUISINE_RT.get(), cont, level).isPresent();
	}

	public void collectIngredients(List<Ingredient> list, List<Ingredient> recipe) {
		base.collectIngredients(list);
		if (click != null) {
			recipe.add(Ingredient.EMPTY);
		}
	}

	public List<Ingredient> getHints(Level level) {
		List<Ingredient> ans = new ArrayList<>();
		var cont = new CuisineInv(id, List.of(), 0, false);
		var list = level.getRecipeManager().getRecipesFor(YHBlocks.CUISINE_RT.get(), cont, level);
		for (var r : list) {
			ans.addAll(r.getHints(level, cont));
		}
		return ans;
	}

}
