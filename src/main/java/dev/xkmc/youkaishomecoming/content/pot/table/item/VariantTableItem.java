package dev.xkmc.youkaishomecoming.content.pot.table.item;

import dev.xkmc.youkaishomecoming.content.pot.table.recipe.CuisineInv;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.resources.ResourceLocation;
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
	public int step() {
		return base.step() + contents.size();
	}

	@Override
	public Optional<TableItem> find(Level level, ItemStack stack) {
		if (stack.isEmpty()) {
			var next = base.next();
			if (next == null) return Optional.empty();
			var cont = new CuisineInv(base.id(), contents, 0, true);
			return level.getRecipeManager().getRecipeFor(YHBlocks.CUISINE_RT.get(), cont, level)
					.map(r -> new FoodTableItem(r.assemble(cont, level.registryAccess())));
		}
		var ans = new ArrayList<>(contents);
		ans.add(stack.copyWithCount(1));
		if (!base.isValid(level, ans)) return Optional.empty();
		return Optional.of(new VariantTableItem(base, ans));
	}

	@Override
	public Optional<ItemStack> complete(Level level) {
		if (base.next() != null) return Optional.empty();
		var cont = new CuisineInv(base.id(), contents, 0, true);
		return level.getRecipeManager().getRecipeFor(YHBlocks.CUISINE_RT.get(), cont, level)
				.map(r -> r.assemble(cont, level.registryAccess()));
	}

	@Override
	public List<ResourceLocation> getModels() {
		List<ResourceLocation> ans = new ArrayList<>();
		ans.add(base.model.modelLoc());
		base.model.buildContents(ans, contents);
		return ans;
	}

}
