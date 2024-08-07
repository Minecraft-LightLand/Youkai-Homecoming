package dev.xkmc.youkaishomecoming.init.loot;

import com.tterrag.registrate.providers.loot.RegistrateEntityLootTables;
import dev.xkmc.l2core.serial.loot.LootHelper;
import dev.xkmc.youkaishomecoming.content.entity.lampery.LampreyEntity;
import dev.xkmc.youkaishomecoming.init.food.YHFood;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;

public class EntityLootGen {

	public static <T extends LivingEntity> void noLoot(RegistrateEntityLootTables pvd, EntityType<T> type) {
		pvd.add(type, LootTable.lootTable());
	}

	public static void lamprey(RegistrateEntityLootTables pvd, EntityType<LampreyEntity> type) {
		var helper = new LootHelper(pvd);
		pvd.add(type, LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.add(LootItem.lootTableItem(YHFood.RAW_LAMPREY.item.get()))
						.apply(helper.smelt()))
				.withPool(LootPool.lootPool()
						.add(LootItem.lootTableItem(Items.BONE_MEAL))
						.when(LootItemRandomChanceCondition.randomChance(0.05F)))
		);
	}

}
