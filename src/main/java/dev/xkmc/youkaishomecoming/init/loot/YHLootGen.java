package dev.xkmc.youkaishomecoming.init.loot;

import com.tterrag.registrate.providers.loot.RegistrateLootTableProvider;
import dev.xkmc.l2core.serial.loot.LootTableTemplate;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.food.YHCrops;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

public class YHLootGen {

	public static final ResourceKey<LootTable> UDUMBARA_LOOT = key("udumbara_chest_loot");
	public static final ResourceKey<LootTable> CRAB_SAND_BASE = key("crab_digging/sand_base");
	public static final ResourceKey<LootTable> CRAB_SAND_RIVER = key("crab_digging/sand_river");
	public static final ResourceKey<LootTable> CRAB_SAND_BEACH = key("crab_digging/sand_beach");
	public static final ResourceKey<LootTable> CRAB_GRAVEL_BASE = key("crab_digging/gravel_base");
	public static final ResourceKey<LootTable> CRAB_GRAVEL_RIVER = key("crab_digging/gravel_river");
	public static final ResourceKey<LootTable> CRAB_GRAVEL_BEACH = key("crab_digging/gravel_beach");

	private static ResourceKey<LootTable> key(String id) {
		return ResourceKey.create(Registries.LOOT_TABLE, YoukaisHomecoming.loc(id));
	}

	public static void genLoot(RegistrateLootTableProvider pvd) {

		pvd.addLootAction(LootContextParamSets.EMPTY, cons -> cons.accept(UDUMBARA_LOOT, LootTable.lootTable()
				.withPool(LootPool.lootPool().add(LootTableTemplate.getItem(YHCrops.UDUMBARA.getSeed(), 2, 4)
						.when(LootTableTemplate.chance(0.3f))))));

		{

			var sandBase = LootTableTemplate.getPool(1, 0)
					.add(LootItem.lootTableItem(Items.STICK).setWeight(100))
					.add(LootItem.lootTableItem(Items.CLAY_BALL).setWeight(100))
					.add(LootItem.lootTableItem(Items.FLINT).setWeight(100))
					.add(LootItem.lootTableItem(Items.IRON_NUGGET).setWeight(100));

			var sandRiver = LootTableTemplate.getPool(1, 0)
					.add(LootItem.lootTableItem(Items.STICK).setWeight(100))
					.add(LootItem.lootTableItem(Items.CLAY_BALL).setWeight(100))
					.add(LootItem.lootTableItem(Items.FLINT).setWeight(100))
					.add(LootItem.lootTableItem(Items.IRON_NUGGET).setWeight(100))
					.add(LootItem.lootTableItem(Items.GOLD_NUGGET).setWeight(100))
					.add(LootItem.lootTableItem(Items.BONE).setWeight(100))
					.add(LootItem.lootTableItem(Items.BAMBOO).setWeight(150))
					.add(LootItem.lootTableItem(Items.EMERALD).setWeight(50));

			var sandBeach = LootTableTemplate.getPool(1, 0)
					.add(LootItem.lootTableItem(Items.STICK).setWeight(100))
					.add(LootItem.lootTableItem(Items.CLAY_BALL).setWeight(100))
					.add(LootItem.lootTableItem(Items.FLINT).setWeight(100))
					.add(LootItem.lootTableItem(Items.IRON_NUGGET).setWeight(100))
					.add(LootItem.lootTableItem(Items.GOLD_NUGGET).setWeight(100))
					.add(LootItem.lootTableItem(Items.TURTLE_SCUTE).setWeight(180))
					.add(LootItem.lootTableItem(Items.STRIPPED_OAK_LOG).setWeight(100))
					.add(LootItem.lootTableItem(Items.DIAMOND).setWeight(20));

			var gravelBase = LootTableTemplate.getPool(1, 0)
					.add(LootItem.lootTableItem(Items.FLINT).setWeight(200))
					.add(LootItem.lootTableItem(Items.RAW_COPPER).setWeight(100))
					.add(LootItem.lootTableItem(Items.IRON_NUGGET).setWeight(100));

			var gravelRiver = LootTableTemplate.getPool(1, 0)
					.add(LootItem.lootTableItem(Items.FLINT).setWeight(200))
					.add(LootItem.lootTableItem(Items.RAW_COPPER).setWeight(100))
					.add(LootItem.lootTableItem(Items.IRON_NUGGET).setWeight(100))
					.add(LootItem.lootTableItem(Items.GOLD_NUGGET).setWeight(100))
					.add(LootItem.lootTableItem(Items.RAW_IRON).setWeight(100));

			var gravelBeach = LootTableTemplate.getPool(1, 0)
					.add(LootItem.lootTableItem(Items.FLINT).setWeight(200))
					.add(LootItem.lootTableItem(Items.RAW_COPPER).setWeight(100))
					.add(LootItem.lootTableItem(Items.IRON_NUGGET).setWeight(100))
					.add(LootItem.lootTableItem(Items.GOLD_NUGGET).setWeight(100))
					.add(LootItem.lootTableItem(Items.RAW_IRON).setWeight(100))
					.add(LootItem.lootTableItem(Items.PRISMARINE_SHARD).setWeight(100))
					.add(LootItem.lootTableItem(Items.PRISMARINE_CRYSTALS).setWeight(100))
					.add(LootItem.lootTableItem(Items.NAUTILUS_SHELL).setWeight(200));

			pvd.addLootAction(LootContextParamSets.GIFT, cons ->
					cons.accept(CRAB_SAND_BASE, LootTable.lootTable().withPool(sandBase)));

			pvd.addLootAction(LootContextParamSets.GIFT, cons ->
					cons.accept(CRAB_SAND_RIVER, LootTable.lootTable().withPool(sandRiver)));

			pvd.addLootAction(LootContextParamSets.GIFT, cons ->
					cons.accept(CRAB_SAND_BEACH, LootTable.lootTable().withPool(sandBeach)));

			pvd.addLootAction(LootContextParamSets.GIFT, cons ->
					cons.accept(CRAB_GRAVEL_BASE, LootTable.lootTable().withPool(gravelBase)));

			pvd.addLootAction(LootContextParamSets.GIFT, cons ->
					cons.accept(CRAB_GRAVEL_RIVER, LootTable.lootTable().withPool(gravelRiver)));

			pvd.addLootAction(LootContextParamSets.GIFT, cons ->
					cons.accept(CRAB_GRAVEL_BEACH, LootTable.lootTable().withPool(gravelBeach)));
		}
	}

}
