package dev.xkmc.youkaishomecoming.content.pot.table.item;

import dev.xkmc.youkaishomecoming.content.pot.table.food.FoodModelHelper;
import dev.xkmc.youkaishomecoming.content.pot.table.food.FoodTableItemHolder;
import dev.xkmc.youkaishomecoming.content.pot.table.recipe.CuisineInv;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
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
	private final FoodTableItemBase base;
	private final ResourceLocation baseId;

	@Nullable
	private final FoodTableItemHolder holder;

	public FoodTableItem(FoodTableItemBase base, ItemStack current) {
		this.base = base;
		this.contents = List.of();
		this.current = current.copyWithCount(1);
		holder = FoodModelHelper.find(this.current);
		baseId = this.current.getItemHolder().unwrapKey().orElseThrow().location();
	}

	private FoodTableItem(FoodTableItemBase base, ItemStack current, FoodTableItemHolder holder, List<ItemStack> contents) {
		this.base = base;
		this.contents = contents;
		this.current = current;
		this.holder = holder;
		baseId = this.current.getItemHolder().unwrapKey().orElseThrow().location();
	}

	@Override
	public int getCost(TableItem prev) {
		if (holder != null && prev instanceof TableItemManager)
			return holder.count();
		return 1;
	}

	@Override
	public Optional<TableItem> find(Level level, ItemStack stack) {
		if (holder == null) return Optional.empty();
		var ans = new ArrayList<>(contents);
		ans.add(stack.copyWithCount(1));
		if (!base.isValid(level, baseId, ans)) return Optional.empty();
		return Optional.of(new FoodTableItem(base, current, holder, ans));
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
		var cont = new CuisineInv(baseId, contents, 0, true);
		return level.getRecipeManager().getRecipeFor(YHBlocks.CUISINE_RT.get(), cont, level)
				.map(r -> r.assemble(cont, level.registryAccess()));
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
