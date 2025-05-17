package dev.xkmc.youkaishomecoming.content.pot.table.item;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.ArrayList;
import java.util.List;

public class TableModelManager {

	private static final List<TableModelBuilder> BUILDERS = new ArrayList<>();

	public static ResourceLocation fixed(ResourceLocation id) {
		return id;//TODO
	}

	public static ResourceLocation fixed(String id) {
		return fixed(YoukaisHomecoming.loc(id));
	}

	public static ResourceLocation variant(ResourceLocation id) {
		return id;//TODO
	}

	public static ResourceLocation variant(String id) {
		return variant(YoukaisHomecoming.loc(id));
	}


	public static final BaseTableItem ROOT = new BaseTableItem();

	public static final IngredientTableItem RICE = ROOT.with(fixed("rice"), ModItems.COOKED_RICE::get);
	public static final IngredientTableItem SUSHI = RICE.addNext(variant("base_sushi"));
	public static final IngredientTableItem SHOKAN = SUSHI.with(variant("base_shokan"), Items.DRIED_KELP);
	public static final IngredientTableItem RICE_2 = RICE.with(fixed("rice_2"), ModItems.COOKED_RICE::get);
	public static final IngredientTableItem CAL = RICE_2.with(variant("base_california_roll"), Items.DRIED_KELP);

	public static final IngredientTableItem KELP = ROOT.with(fixed("kelp"), Items.DRIED_KELP);
	public static final IngredientTableItem THIN = KELP.with(variant("base_thin"), ModItems.COOKED_RICE::get);
	public static final IngredientTableItem TAI = THIN.with(variant("base_tai"), ModItems.COOKED_RICE::get);

	public static final VariantTableItemBase BASE_SUSHI = RICE.asBase();
	public static final VariantTableItemBase BASE_SHOKAN = SHOKAN.asBase();
	public static final VariantTableItemBase BASE_THIN = THIN.asBase();
	public static final VariantTableItemBase BASE_TAI = TAI.asBase();
	public static final VariantTableItemBase BASE_CAL = CAL.asBase();

	public static final FilledTableItemBase COMPLETE_THIN = BASE_THIN.addNextStep();
	public static final FilledTableItemBase COMPLETE_TAI = BASE_TAI.addNextStep();
	public static final FilledTableItemBase COMPLETE_CAL = BASE_CAL.addNextStep();

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
