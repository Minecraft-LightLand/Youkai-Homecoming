package dev.xkmc.youkaishomecoming.content.block.plant;

import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import dev.xkmc.l2core.serial.loot.LootHelper;
import dev.xkmc.youkaishomecoming.init.food.YHCrops;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;

public class MandrakeGen {


	public static void buildPlantLoot(RegistrateBlockLootTables pvd, YHCropBlock block, YHCrops crop) {
		var helper = new LootHelper(pvd);
		pvd.add(block, pvd.applyExplosionDecay(block,
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.add(LootItem.lootTableItem(crop.getSeed())))
						.withPool(LootPool.lootPool()
								.when(helper.intState(block, CropBlock.AGE, 7))
								.add(LootItem.lootTableItem(crop.getFruits())))
						.withPool(LootPool.lootPool()
								.when(helper.intState(block, CropBlock.AGE, 7))
								.add(LootItem.lootTableItem(crop.getSeed()).apply(helper.fortuneBin())))));
	}

	public static void buildWildLoot(RegistrateBlockLootTables pvd, BushBlock block, YHCrops crop) {
		var helper = new LootHelper(pvd);
		pvd.add(block, LootTable.lootTable().withPool(LootPool.lootPool()
				.add(LootItem.lootTableItem(block.asItem())
						.when(helper.silk()).otherwise(pvd.applyExplosionDecay(block,
								LootItem.lootTableItem(crop.getSeed()).apply(helper.fortuneBin())))
				)));
	}

}
