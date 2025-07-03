package dev.xkmc.youkaishomecoming.init.loot;

import com.tterrag.registrate.providers.loot.RegistrateLootTableProvider;
import dev.xkmc.l2library.util.data.LootTableTemplate;
import dev.xkmc.youkaishomecoming.content.pot.table.food.YHSushi;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.food.YHCrops;
import dev.xkmc.youkaishomecoming.init.food.YHFood;
import dev.xkmc.youkaishomecoming.init.food.YHTea;
import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import vectorwing.farmersdelight.common.registry.ModItems;

public class YHLootGen {

	public static final ResourceLocation UDUMBARA_LOOT = YoukaisHomecoming.loc("udumbara_chest_loot");

	public static void genLoot(RegistrateLootTableProvider pvd) {

		pvd.addLootAction(LootContextParamSets.EMPTY, cons -> cons.accept(UDUMBARA_LOOT, LootTable.lootTable()
				.withPool(LootPool.lootPool().add(LootTableTemplate.getItem(YHCrops.UDUMBARA.getSeed(), 2, 4)
						.when(LootTableTemplate.chance(0.3f))))));


	}

}
