package dev.xkmc.youkaishomecoming.init.loot;

import com.tterrag.registrate.providers.loot.RegistrateEntityLootTables;
import dev.xkmc.l2library.util.data.LootTableTemplate;
import dev.xkmc.youkaishomecoming.content.entity.animal.crab.CrabEntity;
import dev.xkmc.youkaishomecoming.content.entity.animal.deer.DeerEntity;
import dev.xkmc.youkaishomecoming.content.entity.animal.lampery.LampreyEntity;
import dev.xkmc.youkaishomecoming.content.entity.animal.tuna.TunaEntity;
import dev.xkmc.youkaishomecoming.content.entity.boss.*;
import dev.xkmc.youkaishomecoming.content.entity.fairy.CirnoEntity;
import dev.xkmc.youkaishomecoming.content.entity.fairy.ClownEntity;
import dev.xkmc.youkaishomecoming.content.entity.fairy.FairyEntity;
import dev.xkmc.youkaishomecoming.content.entity.reimu.ReimuEntity;
import dev.xkmc.youkaishomecoming.content.entity.rumia.RumiaEntity;
import dev.xkmc.youkaishomecoming.init.data.YHDamageTypes;
import dev.xkmc.youkaishomecoming.init.food.YHFood;
import dev.xkmc.youkaishomecoming.init.registrate.YHDanmaku;
import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
import net.minecraft.advancements.critereon.*;
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
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SmeltItemFunction;
import net.minecraft.world.level.storage.loot.predicates.*;
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

	public static void deer(RegistrateEntityLootTables pvd, EntityType<DeerEntity> type) {
		pvd.add(type, LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.add(LootItem.lootTableItem(YHFood.RAW_VENISON.item.get()))
						.apply(LootingEnchantFunction.lootingMultiplier(ConstantValue.exactly(0.5f)))
						.apply(onFire())));
	}

	public static void crab(RegistrateEntityLootTables pvd, EntityType<CrabEntity> type) {
		pvd.add(type, LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.add(LootItem.lootTableItem(YHFood.CRAB.item.get())))
				.withPool(LootPool.lootPool()
						.add(LootItem.lootTableItem(YHFood.CRAB_ROE.item.get()))
						.when(LootItemRandomChanceCondition.randomChance(0.5f))
						.when(LootItemEntityPropertyCondition.hasProperties(
								LootContext.EntityTarget.KILLER,
								EntityPredicate.Builder.entity().equipment(
										EntityEquipmentPredicate.Builder.equipment().mainhand(
														ItemPredicate.Builder.item().of(ModTags.KNIVES).build())
												.build()).build()))
				));
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
				.withPool(LootPool.lootPool().add(LootTableTemplate.getItem(YHDanmaku.REIMU_SPELL.get(), 1, 1))
						.when(LootTableTemplate.byPlayer()).when(danmakuKill()))
		);
	}

	public static void sanae(RegistrateEntityLootTables pvd, EntityType<SanaeEntity> type) {
		pvd.add(type, LootTable.lootTable()
				.withPool(LootPool.lootPool().add(LootTableTemplate.getItem(YHDanmaku.SANAE_SPELL.get(), 1, 1))
						.when(LootTableTemplate.byPlayer()).when(danmakuKill()))
		);
	}

	public static void marisa(RegistrateEntityLootTables pvd, EntityType<MarisaEntity> type) {
		pvd.add(type, LootTable.lootTable()
				.withPool(LootPool.lootPool().add(LootTableTemplate.getItem(YHDanmaku.MARISA_SPELL.get(), 1, 1))
						.when(LootTableTemplate.byPlayer()).when(danmakuKill()))
		);
	}

	public static void mystia(RegistrateEntityLootTables pvd, EntityType<MystiaEntity> type) {
		pvd.add(type, LootTable.lootTable()
				.withPool(LootPool.lootPool().add(LootTableTemplate.getItem(YHDanmaku.MYSTIA_SPELL.get(), 1, 1))
						.when(LootTableTemplate.byPlayer()).when(danmakuKill()))
		);
	}


	public static void koishi(RegistrateEntityLootTables pvd, EntityType<KoishiEntity> type) {
		pvd.add(type, LootTable.lootTable()
				.withPool(LootPool.lootPool().add(LootTableTemplate.getItem(YHDanmaku.KOISHI_SPELL.get(), 1, 1))
						.when(LootTableTemplate.byPlayer()).when(danmakuKill()))
		);
	}

	public static void remilia(RegistrateEntityLootTables pvd, EntityType<RemiliaEntity> type) {
		pvd.add(type, LootTable.lootTable()
				.withPool(LootPool.lootPool().add(LootTableTemplate.getItem(YHDanmaku.REMILIA_SPELL.get(), 1, 1))
						.when(LootTableTemplate.byPlayer()).when(danmakuKill()))
		);
	}


	public static void clownpiece(RegistrateEntityLootTables pvd, EntityType<ClownEntity> type) {
		pvd.add(type, LootTable.lootTable()
				.withPool(LootPool.lootPool().add(LootTableTemplate.getItem(YHDanmaku.CLOWNPIECE_SPELL.get(), 1, 1))
						.when(LootTableTemplate.byPlayer()).when(danmakuKill()))
		);
	}

	public static void yukari(RegistrateEntityLootTables pvd, EntityType<YukariEntity> type) {
		pvd.add(type, LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.when(LootTableTemplate.byPlayer()).when(danmakuKill())
						.add(LootTableTemplate.getItem(YHDanmaku.YUKARI_SPELL_LASER.get(), 1, 1))
						.add(LootTableTemplate.getItem(YHDanmaku.YUKARI_SPELL_BUTTERFLY.get(), 1, 1)))
		);
	}

	public static void fairy(RegistrateEntityLootTables pvd, EntityType<? extends FairyEntity> type) {
		pvd.add(type, LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.add(LootTableTemplate.getItem(YHFood.FAIRY_CANDY.item.get(), 1, 2))
						.apply(lootCount(0.5f))
						.when(LootTableTemplate.byPlayer()))
		);
	}

	public static void cirno(RegistrateEntityLootTables pvd, EntityType<CirnoEntity> type) {
		pvd.add(type, LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.add(LootTableTemplate.getItem(YHItems.FAIRY_ICE_CRYSTAL.get(), 1, 2))
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

	private static LootItemFunction.Builder onFire() {
		return SmeltItemFunction.smelted()
				.when(LootItemEntityPropertyCondition.hasProperties(
						LootContext.EntityTarget.THIS,
						EntityPredicate.Builder.entity()
								.flags(EntityFlagsPredicate.Builder.flags()
										.setOnFire(true).build())));
	}

	private static LootItemCondition.Builder danmakuKill() {
		return DamageSourceCondition.hasDamageSource(DamageSourcePredicate.Builder.damageType()
				.tag(TagPredicate.is(YHDamageTypes.DANMAKU_TYPE)));
	}

	public static LootItemFunction.Builder lootCount(float factor) {
		return LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(factor * 0.5f, factor));
	}

	private static LootItemCondition.Builder lootChance(float base) {
		return LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(base, base * 0.2f);
	}

}
