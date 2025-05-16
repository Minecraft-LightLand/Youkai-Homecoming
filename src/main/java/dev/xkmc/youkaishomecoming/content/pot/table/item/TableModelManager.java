package dev.xkmc.youkaishomecoming.content.pot.table.item;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.ArrayList;
import java.util.List;

public class TableModelManager {

	public static final BaseTableItem ROOT = new BaseTableItem();

	public static final IngredientTableItem RICE = ROOT.with(ModItems.COOKED_RICE.get());
	public static final IngredientTableItem SHOKAN = RICE.with(Items.DRIED_KELP);
	public static final IngredientTableItem RICE_2 = RICE.with(ModItems.COOKED_RICE.get());
	public static final IngredientTableItem CAL = RICE_2.with(Items.DRIED_KELP);

	public static final IngredientTableItem KELP = ROOT.with(Items.DRIED_KELP);
	public static final IngredientTableItem THIN = KELP.with(ModItems.COOKED_RICE.get());
	public static final IngredientTableItem TAI = THIN.with(ModItems.COOKED_RICE.get());

	public static final VariantTableItemBase BASE_SUSHI = RICE.asBase(YoukaisHomecoming.loc("sushi"));
	public static final VariantTableItemBase BASE_SHOKAN = SHOKAN.asBase(YoukaisHomecoming.loc("shokan"));
	public static final VariantTableItemBase BASE_THIN = THIN.asBase(YoukaisHomecoming.loc("thin"));
	public static final VariantTableItemBase BASE_TAI = TAI.asBase(YoukaisHomecoming.loc("tai"));
	public static final VariantTableItemBase BASE_CAL = CAL.asBase(YoukaisHomecoming.loc("california"));

	public static Either<TableItem, Pair<TableItem, List<ItemStack>>> find(Level level, List<ItemStack> list) {
		TableItem ans = ROOT;
		List<ItemStack> success = new ArrayList<>();
		for (var e : list) {
			var opt = ans.find(level, e);
			if (opt.isEmpty()) return Either.right(Pair.of(ans, success));
			success.add(e);
			ans = opt.get();
		}
		return Either.left(ans);
	}

	public static void init() {

	}

}
