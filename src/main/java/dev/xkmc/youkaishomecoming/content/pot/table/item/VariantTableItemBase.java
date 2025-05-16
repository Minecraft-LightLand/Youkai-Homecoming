package dev.xkmc.youkaishomecoming.content.pot.table.item;

import dev.xkmc.youkaishomecoming.content.pot.table.recipe.CuisineInv;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class VariantTableItemBase {

	private static final Map<ResourceLocation, VariantTableItemBase> MAP = new LinkedHashMap<>();

	public static synchronized VariantTableItemBase create(BaseTableItem base, ResourceLocation id) {
		var ans = new VariantTableItemBase(base, id);
		MAP.put(id, ans);
		return ans;
	}

	private final BaseTableItem base;
	private final ResourceLocation id;

	public VariantTableItemBase(BaseTableItem base, ResourceLocation id) {
		this.base = base;
		this.id = id;
	}

	public Optional<TableItem> find(Level level, ItemStack stack) {
		var list = List.of(stack.copyWithCount(1));
		if (!isValid(level, list)) return Optional.empty();
		return Optional.of(new VariantTableItem(this, list));
	}

	public boolean isValid(Level level, List<ItemStack> stacks) {
		var cont = new CuisineInv(id, stacks, base.step(), false);
		return level.getRecipeManager().getRecipeFor(YHBlocks.TABLE_RT.get(), cont, level).isPresent();
	}

}
