package dev.xkmc.youkaishomecoming.content.pot.table.food;

import dev.xkmc.youkaishomecoming.content.pot.table.item.IngredientTableItem;
import dev.xkmc.youkaishomecoming.content.pot.table.item.TableItemManager;
import dev.xkmc.youkaishomecoming.content.pot.table.model.FixedModelHolder;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.data.TagRef;
import dev.xkmc.youkaishomecoming.init.food.YHCrops;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;

public class TableBambooBowls {

	private static FixedModelHolder bamboo(String id, String parent) {
		return new FixedModelHolder(TableItemManager.MANAGER,
				YoukaisHomecoming.loc("bamboo/" + id),
				YoukaisHomecoming.loc("table/bamboo/" + parent),
				YoukaisHomecoming.loc("block/table/bamboo/"))
				.putDefault("in", "out");
	}

	private static FixedModelHolder complete(String id) {
		return new FixedModelHolder(TableItemManager.MANAGER,
				YoukaisHomecoming.loc("bamboo/" + id),
				YoukaisHomecoming.loc("table/bamboo/complete"),
				YoukaisHomecoming.loc("block/table/bamboo/"))
				.put("bamboo", YoukaisHomecoming.loc("block/bowl/bamboo/raw_bamboo"));
	}


	private static FixedModelHolder bamboo(String id) {
		return bamboo(id, id);
	}

	private static FixedModelHolder fill(String id, int layer) {
		return bamboo(id, "filled_" + layer).put("base", id);
	}

	public static final IngredientTableItem BAMBOO = TableItemManager.TABLE.with(bamboo("base"), Items.BAMBOO);
	public static final IngredientTableItem RICE = BAMBOO.with(fill("rice", 1), TagRef.GRAIN_RICE);
	public static final IngredientTableItem SOYBEAN = BAMBOO.with(fill("soybean", 1), YHCrops.SOYBEAN::getSeed);
	public static final IngredientTableItem CARROT = BAMBOO.with(fill("carrot", 1), Items.CARROT);

	public static final ResourceLocation TUTU_CONGEE = RICE.addNext(complete("tutu_congee")).register();

	public static final ResourceLocation RICE_POWDER_PORK = SOYBEAN
			.with(fill("soy_pork", 2), TagRef.RAW_PORK)
			.with(fill("rice_powder_pork_3", 3), TagRef.GRAIN_RICE)
			.addNext(complete("rice_powder_pork")).register();

	public static final ResourceLocation KAGUYA_HIME = CARROT
			.with(fill("carrot_mushroom", 2), Items.BROWN_MUSHROOM)
			.with(fill("kaguya_hime_3", 3), TagRef.VEGETABLES_ONION)
			.with(fill("kaguya_hime_4", 3), TagRef.GRAIN_RICE)
			.addNext(complete("kaguya_hime")).register();

	public static void init() {
	}

}
