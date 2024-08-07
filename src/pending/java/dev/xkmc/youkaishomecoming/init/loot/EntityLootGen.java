package dev.xkmc.youkaishomecoming.init.loot;

import com.tterrag.registrate.providers.loot.RegistrateEntityLootTables;
import dev.xkmc.l2library.util.data.LootTableTemplate;
import dev.xkmc.youkaishomecoming.content.entity.fairy.CirnoEntity;
import dev.xkmc.youkaishomecoming.content.entity.lampery.LampreyEntity;
import dev.xkmc.youkaishomecoming.content.entity.reimu.ReimuEntity;
import dev.xkmc.youkaishomecoming.content.entity.rumia.RumiaEntity;
import dev.xkmc.youkaishomecoming.init.data.YHDamageTypes;
import dev.xkmc.youkaishomecoming.init.food.YHFood;
import dev.xkmc.youkaishomecoming.init.registrate.YHDanmaku;
import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
import net.minecraft.advancements.critereon.DamageSourcePredicate;
import net.minecraft.advancements.critereon.EntityFlagsPredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.TagPredicate;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
import net.minecraft.world.level.storage.loot.functions.SmeltItemFunction;
import net.minecraft.world.level.storage.loot.predicates.DamageSourceCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public class EntityLootGen {

	public static <T extends LivingEntity> void noLoot(RegistrateEntityLootTables pvd, EntityType<T> type) {
		pvd.add(type, LootTable.lootTable());
	}

	public static void rumia(RegistrateEntityLootTables pvd, EntityType<RumiaEntity> type) {
		pvd.add(type, LootTable.lootTable()
				.withPool(LootPool.lootPool().add(LootTableTemplate.getItem(YHDanmaku.Bullet.CIRCLE.get(DyeColor.RED).get(), 5, 10))
						.apply(lootCount(1f))
						.when(LootTableTemplate.byPlayer()))
				.withPool(LootPool.lootPool().add(LootTableTemplate.getItem(YHDanmaku.Bullet.CIRCLE.get(DyeColor.BLACK).get(), 3, 6))
						.apply(lootCount(1f))
						.when(LootTableTemplate.byPlayer()))
		);
	}

	public static void reimu(RegistrateEntityLootTables pvd, EntityType<ReimuEntity> type) {
		pvd.add(type, LootTable.lootTable()
				.withPool(LootPool.lootPool().add(LootTableTemplate.getItem(YHItems.REIMU_SPELL.get(), 1, 1))
						.when(LootTableTemplate.byPlayer()))
		);
	}

	public static void cirno(RegistrateEntityLootTables pvd, EntityType<CirnoEntity> type) {
		pvd.add(type, LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.add(LootTableTemplate.getItem(YHItems.FAIRY_ICE_CRYSTAL.get(), 2, 3))
						.apply(lootCount(0.5f))
						.when(LootTableTemplate.byPlayer()))
				.withPool(LootPool.lootPool()
						.add(LootTableTemplate.getItem(YHDanmaku.Bullet.MENTOS.get(DyeColor.LIGHT_BLUE).get(), 1, 1)
								.setWeight(6))
						.add(LootTableTemplate.getItem(YHItems.FROZEN_FROG_COLD.get(), 1, 1))
						.add(LootTableTemplate.getItem(YHItems.FROZEN_FROG_WARM.get(), 1, 1))
						.add(LootTableTemplate.getItem(YHItems.FROZEN_FROG_TEMPERATE.get(), 1, 1))
						.when(lootChance(0.3f))
						.when(danmakuKill())
						.when(LootTableTemplate.byPlayer()))
		);
	}

	private static LootItemCondition.Builder danmakuKill() {
		return DamageSourceCondition.hasDamageSource(DamageSourcePredicate.Builder.damageType()
				.tag(TagPredicate.is(YHDamageTypes.DANMAKU_TYPE)));
	}


}
