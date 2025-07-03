package dev.xkmc.youkaishomecoming.init.loot;

import com.tterrag.registrate.providers.loot.RegistrateEntityLootTables;
import dev.xkmc.youkaishomecoming.content.entity.lampery.LampreyEntity;
import dev.xkmc.youkaishomecoming.content.entity.tuna.TunaEntity;
import dev.xkmc.youkaishomecoming.init.food.YHFood;
import net.minecraft.advancements.critereon.EntityEquipmentPredicate;
import net.minecraft.advancements.critereon.EntityFlagsPredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SmeltItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithLootingCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import vectorwing.farmersdelight.common.tag.ModTags;

public class EntityLootGen {

	public static <T extends LivingEntity> void noLoot(RegistrateEntityLootTables pvd, EntityType<T> type) {
		pvd.add(type, LootTable.lootTable());
	}

	public static void lamprey(RegistrateEntityLootTables pvd, EntityType<LampreyEntity> type) {
		pvd.add(type, LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.add(LootItem.lootTableItem(YHFood.RAW_LAMPREY.item.get()))
						.apply(LootingEnchantFunction.lootingMultiplier(ConstantValue.exactly(0.5f)))
						.apply(onFire()))
				.withPool(LootPool.lootPool()
						.add(LootItem.lootTableItem(Items.BONE_MEAL))
						.when(LootItemRandomChanceCondition.randomChance(0.05F)))
		);
	}

	public static void tuna(RegistrateEntityLootTables pvd, EntityType<TunaEntity> type) {
		pvd.add(type, LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.add(LootItem.lootTableItem(YHFood.RAW_TUNA.item.get()))
						.apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 6)))
						.apply(LootingEnchantFunction.lootingMultiplier(ConstantValue.exactly(0.5f)))
						.apply(onFire()))
				.withPool(LootPool.lootPool()
						.add(LootItem.lootTableItem(Items.BONE_MEAL))
						.when(LootItemRandomChanceCondition.randomChance(0.05F)))
				.withPool(LootPool.lootPool()
						.add(LootItem.lootTableItem(YHFood.OTORO.item.get()))
						.apply(LootingEnchantFunction.lootingMultiplier(ConstantValue.exactly(0.5f)))
						.when(LootItemEntityPropertyCondition.hasProperties(
								LootContext.EntityTarget.KILLER,
								EntityPredicate.Builder.entity().equipment(
										EntityEquipmentPredicate.Builder.equipment().mainhand(
														ItemPredicate.Builder.item().of(ModTags.KNIVES).build())
												.build()).build()))
				));
	}

	private static LootItemFunction.Builder onFire() {
		return SmeltItemFunction.smelted()
				.when(LootItemEntityPropertyCondition.hasProperties(
						LootContext.EntityTarget.THIS,
						EntityPredicate.Builder.entity()
								.flags(EntityFlagsPredicate.Builder.flags()
										.setOnFire(true).build())));
	}

	public static LootItemFunction.Builder lootCount(float factor) {
		return LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(factor * 0.5f, factor));
	}

	private static LootItemCondition.Builder lootChance(float base) {
		return LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(base, base * 0.2f);
	}

}
