package dev.xkmc.youkaishomecoming.init.registrate;

import com.tterrag.registrate.util.entry.EntityEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.l2library.util.data.LootTableTemplate;
import dev.xkmc.youkaishomecoming.content.entity.boss.YukariEntity;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.ItemDanmakuEntity;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.ItemDanmakuRenderer;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.ItemLaserEntity;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.ItemLaserRenderer;
import dev.xkmc.youkaishomecoming.content.entity.fairy.CirnoEntity;
import dev.xkmc.youkaishomecoming.content.entity.fairy.FairyEntity;
import dev.xkmc.youkaishomecoming.content.entity.lampery.LampreyEntity;
import dev.xkmc.youkaishomecoming.content.entity.lampery.LampreyRenderer;
import dev.xkmc.youkaishomecoming.content.entity.rumia.RumiaEntity;
import dev.xkmc.youkaishomecoming.content.entity.rumia.RumiaRenderer;
import dev.xkmc.youkaishomecoming.content.entity.boss.BossYoukaiEntity;
import dev.xkmc.youkaishomecoming.content.entity.youkai.GeneralYoukaiRenderer;
import dev.xkmc.youkaishomecoming.content.entity.boss.MaidenEntity;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.food.YHFood;
import net.minecraft.advancements.critereon.EntityFlagsPredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SmeltItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.registries.ForgeRegistries;

public class YHEntities {

	public static final EntityEntry<LampreyEntity> LAMPREY = YoukaisHomecoming.REGISTRATE
			.entity("lamprey", LampreyEntity::new, MobCategory.WATER_AMBIENT)
			.properties(e -> e.sized(0.5F, 0.4F).clientTrackingRange(4))
			.spawnPlacement(SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules)
			.attributes(LampreyEntity::createAttributes)
			.renderer(() -> LampreyRenderer::new)
			.spawnEgg(-3814463, -6646165).tab(YoukaisHomecoming.TAB.getKey()).build()
			.loot((pvd, type) -> pvd.add(type,
					LootTable.lootTable()
							.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
									.add(LootItem.lootTableItem(YHFood.RAW_LAMPREY.item.get())
											.apply(SmeltItemFunction.smelted()
													.when(LootItemEntityPropertyCondition.hasProperties(
															LootContext.EntityTarget.THIS,
															EntityPredicate.Builder.entity()
																	.flags(EntityFlagsPredicate.Builder.flags()
																			.setOnFire(true).build()))))))
							.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
									.add(LootItem.lootTableItem(Items.BONE_MEAL))
									.when(LootItemRandomChanceCondition.randomChance(0.05F)))
			)).register();

	public static final EntityEntry<RumiaEntity> RUMIA = YoukaisHomecoming.REGISTRATE
			.entity("rumia", RumiaEntity::new, MobCategory.MONSTER)
			.properties(e -> e.sized(0.4F, 1.7f).clientTrackingRange(10))
			.spawnPlacement(SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, RumiaEntity::checkRumiaSpawnRules)
			.attributes(RumiaEntity::createAttributes)
			.renderer(() -> RumiaRenderer::new)
			.spawnEgg(0x000000, 0x000000).tab(YHDanmaku.TAB.getKey()).build()
			.loot((pvd, type) -> pvd.add(type,
					LootTable.lootTable()
							.withPool(LootPool.lootPool().add(LootTableTemplate.getItem(YHDanmaku.Bullet.CIRCLE.get(DyeColor.RED).get(), 5, 10))
									.when(LootTableTemplate.byPlayer()))
							.withPool(LootPool.lootPool().add(LootTableTemplate.getItem(YHDanmaku.Bullet.CIRCLE.get(DyeColor.BLACK).get(), 3, 6))
									.when(LootTableTemplate.byPlayer()))
			)).register();

	public static final EntityEntry<BossYoukaiEntity> GENERAL_YOUKAI = YoukaisHomecoming.REGISTRATE
			.entity("youkai", BossYoukaiEntity::new, MobCategory.MONSTER)
			.properties(e -> e.sized(0.4F, 1.8f).clientTrackingRange(10))
			.attributes(BossYoukaiEntity::createAttributes)
			.renderer(() -> GeneralYoukaiRenderer::new)
			.spawnEgg(0x000000, 0x000000).tab(YHDanmaku.TAB.getKey()).build()
			.loot((pvd, type) -> pvd.add(type, LootTable.lootTable())).register();

	public static final EntityEntry<MaidenEntity> MAIDEN = YoukaisHomecoming.REGISTRATE
			.entity("shrine_maiden", MaidenEntity::new, MobCategory.MONSTER)
			.properties(e -> e.sized(0.4F, 1.8f).clientTrackingRange(10))
			.attributes(BossYoukaiEntity::createAttributes)
			.renderer(() -> GeneralYoukaiRenderer::new)
			.spawnEgg(0xC20C1C, 0xFFFFFF).tab(YHDanmaku.TAB.getKey()).build()
			.loot((pvd, type) -> pvd.add(type, LootTable.lootTable())).register();

	public static final EntityEntry<FairyEntity> FAIRY = YoukaisHomecoming.REGISTRATE
			.entity("fairy", FairyEntity::new, MobCategory.MONSTER)
			.properties(e -> e.sized(0.4F, 1.8f).clientTrackingRange(10))
			.attributes(FairyEntity::createAttributes)
			.renderer(() -> GeneralYoukaiRenderer::new)
			.spawnEgg(0x000000, 0x000000).tab(YHDanmaku.TAB.getKey()).build()
			.loot((pvd, type) -> pvd.add(type, LootTable.lootTable())).register();

	public static final EntityEntry<CirnoEntity> CIRNO = YoukaisHomecoming.REGISTRATE
			.entity("cirno", CirnoEntity::new, MobCategory.MONSTER)
			.properties(e -> e.sized(0.4F, 1.8f).clientTrackingRange(10))
			.attributes(CirnoEntity::createAttributes)
			.renderer(() -> GeneralYoukaiRenderer::new)
			.spawnEgg(0xA8C3D9, 0x7E8DC4).tab(YHDanmaku.TAB.getKey()).build()
			.loot((pvd, type) -> pvd.add(type, LootTable.lootTable())).register();

	public static final EntityEntry<YukariEntity> YUKARI = YoukaisHomecoming.REGISTRATE
			.entity("yukari", YukariEntity::new, MobCategory.MONSTER)
			.properties(e -> e.sized(0.4F, 1.8f).clientTrackingRange(10))
			.attributes(BossYoukaiEntity::createAttributes)
			.renderer(() -> GeneralYoukaiRenderer::new)
			.spawnEgg(0x4B1442, 0xFFFFFF).tab(YHDanmaku.TAB.getKey()).build()
			.loot((pvd, type) -> pvd.add(type, LootTable.lootTable())).register();

	public static final EntityEntry<ItemDanmakuEntity> ITEM_DANMAKU = YoukaisHomecoming.REGISTRATE
			.<ItemDanmakuEntity>entity("item_danmaku", ItemDanmakuEntity::new, MobCategory.MISC)
			.properties(e -> e.sized(0.4f, 0.4f).clientTrackingRange(4).updateInterval(1 << 16))
			.renderer(() -> ItemDanmakuRenderer::new)
			.register();

	public static final EntityEntry<ItemLaserEntity> ITEM_LASER = YoukaisHomecoming.REGISTRATE
			.<ItemLaserEntity>entity("item_laser", ItemLaserEntity::new, MobCategory.MISC)
			.properties(e -> e.sized(0.4f, 0.4f).clientTrackingRange(4).updateInterval(1 << 16))
			.renderer(() -> ItemLaserRenderer::new)
			.register();

	private static <A extends RecipeSerializer<?>> RegistryEntry<A> reg(String id, NonNullSupplier<A> sup) {
		return YoukaisHomecoming.REGISTRATE.simple(id, ForgeRegistries.Keys.RECIPE_SERIALIZERS, sup);
	}

	public static void register() {
	}

}
