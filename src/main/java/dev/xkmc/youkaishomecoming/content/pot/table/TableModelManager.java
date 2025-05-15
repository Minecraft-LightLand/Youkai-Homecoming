package dev.xkmc.youkaishomecoming.content.pot.table;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import dev.xkmc.youkaishomecoming.content.pot.table.TableModel.BaseTableModel;
import dev.xkmc.youkaishomecoming.content.pot.table.TableModel.IngredientTableModel;
import dev.xkmc.youkaishomecoming.content.pot.table.TableModel.VariantModelBase;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.ArrayList;
import java.util.List;

public class TableModelManager {

	public static final BaseTableModel ROOT = new BaseTableModel();

	public static final IngredientTableModel RICE = ROOT.with(ModItems.COOKED_RICE.get());
	public static final IngredientTableModel SHOKAN = RICE.with(Items.DRIED_KELP);
	public static final IngredientTableModel RICE_2 = RICE.with(ModItems.COOKED_RICE.get());
	public static final IngredientTableModel CAL = RICE_2.with(Items.DRIED_KELP);

	public static final IngredientTableModel KELP = ROOT.with(Items.DRIED_KELP);
	public static final IngredientTableModel THIN = KELP.with(ModItems.COOKED_RICE.get());
	public static final IngredientTableModel TAI = THIN.with(ModItems.COOKED_RICE.get());

	public static final VariantModelBase BASE_SUSHI = RICE.asBase(YoukaisHomecoming.loc("sushi"));
	public static final VariantModelBase BASE_SHOKAN = SHOKAN.asBase(YoukaisHomecoming.loc("shokan"));
	public static final VariantModelBase BASE_THIN = THIN.asBase(YoukaisHomecoming.loc("thin"));
	public static final VariantModelBase BASE_TAI = TAI.asBase(YoukaisHomecoming.loc("tai"));
	public static final VariantModelBase BASE_CAL = CAL.asBase(YoukaisHomecoming.loc("california"));

	public static Either<TableModel, Pair<TableModel, List<ItemStack>>> find(List<ItemStack> list) {
		TableModel ans = ROOT;
		List<ItemStack> success = new ArrayList<>();
		for (var e : list) {
			var opt = ans.find(e);
			if (opt.isEmpty()) return Either.right(Pair.of(ans, success));
			success.add(e);
			ans = opt.get();
		}
		return Either.left(ans);
	}

	public static void init() {

	}

}
