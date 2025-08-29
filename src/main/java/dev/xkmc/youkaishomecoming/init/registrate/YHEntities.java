package dev.xkmc.youkaishomecoming.init.registrate;

import com.tterrag.registrate.util.entry.EntityEntry;
import dev.xkmc.youkaishomecoming.content.block.furniture.ChairEntity;
import dev.xkmc.youkaishomecoming.content.block.furniture.NothingRenderer;
import dev.xkmc.youkaishomecoming.content.entity.animal.boar.BoarEntity;
import dev.xkmc.youkaishomecoming.content.entity.animal.boar.BoarRenderer;
import dev.xkmc.youkaishomecoming.content.entity.animal.crab.CrabEntity;
import dev.xkmc.youkaishomecoming.content.entity.animal.crab.CrabRenderer;
import dev.xkmc.youkaishomecoming.content.entity.animal.deer.DeerEntity;
import dev.xkmc.youkaishomecoming.content.entity.animal.deer.DeerRenderer;
import dev.xkmc.youkaishomecoming.content.entity.animal.lampery.LampreyEntity;
import dev.xkmc.youkaishomecoming.content.entity.animal.lampery.LampreyRenderer;
import dev.xkmc.youkaishomecoming.content.entity.animal.tuna.TunaEntity;
import dev.xkmc.youkaishomecoming.content.entity.animal.tuna.TunaRenderer;
import dev.xkmc.youkaishomecoming.content.entity.boss.*;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.ItemDanmakuEntity;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.ItemDanmakuRenderer;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.ItemLaserEntity;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.ItemLaserRenderer;
import dev.xkmc.youkaishomecoming.content.entity.fairy.*;
import dev.xkmc.youkaishomecoming.content.entity.misc.FairyIce;
import dev.xkmc.youkaishomecoming.content.entity.misc.FrozenFrog;
import dev.xkmc.youkaishomecoming.content.entity.reimu.ReimuEntity;
import dev.xkmc.youkaishomecoming.content.entity.reimu.ReimuRenderer;
import dev.xkmc.youkaishomecoming.content.entity.rumia.RumiaEntity;
import dev.xkmc.youkaishomecoming.content.entity.rumia.RumiaRenderer;
import dev.xkmc.youkaishomecoming.content.entity.youkai.GeneralYoukaiRenderer;
import dev.xkmc.youkaishomecoming.content.spell.shooter.ShooterEntity;
import dev.xkmc.youkaishomecoming.content.spell.shooter.ShooterRenderer;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.data.YHTagGen;
import dev.xkmc.youkaishomecoming.init.loot.EntityLootGen;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.level.levelgen.Heightmap;

public class YHEntities {

	public static final EntityEntry<LampreyEntity> LAMPREY;
	public static final EntityEntry<TunaEntity> TUNA;
	public static final EntityEntry<DeerEntity> DEER;
	public static final EntityEntry<BoarEntity> BOAR;
	public static final EntityEntry<CrabEntity> CRAB;
	public static final EntityEntry<RumiaEntity> RUMIA;
	public static final EntityEntry<ReimuEntity> REIMU;
	public static final EntityEntry<CirnoEntity> CIRNO;

	public static final EntityEntry<BossYoukaiEntity> GENERAL_YOUKAI;
	public static final EntityEntry<FairyEntity> FAIRY;
	public static final EntityEntry<YukariEntity> YUKARI;
	public static final EntityEntry<SanaeEntity> SANAE;
	public static final EntityEntry<MarisaEntity> MARISA;
	public static final EntityEntry<KoishiEntity> KOISHI;
	public static final EntityEntry<RemiliaEntity> REMILIA;
	public static final EntityEntry<MystiaEntity> MYSTIA;
	public static final EntityEntry<LunaEntity> LUNA;
	public static final EntityEntry<SunnyEntity> SUNNY;
	public static final EntityEntry<StarEntity> STAR;
	public static final EntityEntry<LarvaEntity> LARVA;
	public static final EntityEntry<ClownEntity> CLOWN;

	public static final EntityEntry<FrozenFrog> FROZEN_FROG;
	public static final EntityEntry<FairyIce> FAIRY_ICE;
	public static final EntityEntry<ItemDanmakuEntity> ITEM_DANMAKU;
	public static final EntityEntry<ItemLaserEntity> ITEM_LASER;
	public static final EntityEntry<ChairEntity> CHAIR;
	public static final EntityEntry<ShooterEntity> SHOOTER;

	static {

		{
			YoukaisHomecoming.REGISTRATE.defaultCreativeTab(YoukaisHomecoming.TAB.getKey());
			LAMPREY = YoukaisHomecoming.REGISTRATE
					.entity("lamprey", LampreyEntity::new, MobCategory.WATER_AMBIENT)
					.properties(e -> e.sized(0.5F, 0.4F).clientTrackingRange(4))
					.spawnPlacement(SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules)
					.attributes(LampreyEntity::createAttributes)
					.renderer(() -> LampreyRenderer::new)
					.spawnEgg(-3814463, -6646165).build()
					.loot(EntityLootGen::lamprey).register();

			TUNA = YoukaisHomecoming.REGISTRATE
					.entity("tuna", TunaEntity::new, MobCategory.WATER_AMBIENT)
					.properties(e -> e.sized(3F, 1.2F).clientTrackingRange(4))
					.spawnPlacement(SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules)
					.attributes(TunaEntity::createAttributes)
					.renderer(() -> TunaRenderer::new)
					.spawnEgg(0x424F75, 0xE08E46).build()
					.loot(EntityLootGen::tuna).register();

			DEER = YoukaisHomecoming.REGISTRATE
					.entity("deer", DeerEntity::new, MobCategory.CREATURE)
					.properties(e -> e.sized(1f, 2f).clientTrackingRange(10))
					.spawnPlacement(SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules)
					.attributes(DeerEntity::createAttributes)
					.renderer(() -> DeerRenderer::new)
					.spawnEgg(0xc77e55, 0xe8ddd0).build()
					.loot(EntityLootGen::deer).register();

			BOAR = YoukaisHomecoming.REGISTRATE
					.entity("boar", BoarEntity::new, MobCategory.CREATURE)
					.properties(e -> e.sized(1f, 1f).clientTrackingRange(10))
					.spawnPlacement(SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules)
					.attributes(BoarEntity::createAttributes)
					.renderer(() -> BoarRenderer::new)
					.spawnEgg(0x60554A, 0x3E342F).build()
					.loot(EntityLootGen::boar).register();

			CRAB = YoukaisHomecoming.REGISTRATE
					.entity("crab", CrabEntity::new, MobCategory.WATER_AMBIENT)
					.properties(e -> e.sized(0.6f, 0.3f).clientTrackingRange(10))
					.spawnPlacement(SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules)
					.attributes(CrabEntity::createAttributes)
					.renderer(() -> CrabRenderer::new)
					.spawnEgg(0x727e8b, 0xdbc297).build()
					.loot(EntityLootGen::crab).register();

			YoukaisHomecoming.REGISTRATE.defaultCreativeTab(YHDanmaku.TAB.getKey());
			RUMIA = YoukaisHomecoming.REGISTRATE
					.entity("rumia", RumiaEntity::new, MobCategory.MONSTER)
					.properties(e -> e.sized(0.4F, 1.7f).clientTrackingRange(10))
					.spawnPlacement(SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, RumiaEntity::checkRumiaSpawnRules)
					.attributes(RumiaEntity::createAttributes)
					.renderer(() -> RumiaRenderer::new)
					.spawnEgg(0x000000, 0x000000).build()
					.tag(YHTagGen.BOSS)
					.loot(EntityLootGen::rumia).register();

			REIMU = YoukaisHomecoming.REGISTRATE
					.entity("shrine_maiden", ReimuEntity::new, MobCategory.MONSTER)
					.properties(e -> e.sized(0.4F, 1.8f).clientTrackingRange(10))
					.attributes(BossYoukaiEntity::createAttributes)
					.renderer(() -> ReimuRenderer::new)
					.spawnEgg(0xa93937, 0xfaf5f2).build()
					.tag(YHTagGen.BOSS)
					.loot(EntityLootGen::reimu).register();

			CIRNO = YoukaisHomecoming.REGISTRATE
					.entity("cirno", CirnoEntity::new, MobCategory.MONSTER)
					.properties(e -> e.sized(0.4F, 1.8f).clientTrackingRange(10))
					.spawnPlacement(SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, CirnoEntity::checkCirnoSpawnRules)
					.attributes(CirnoEntity::createAttributes)
					.renderer(() -> CirnoRenderer::new)
					.spawnEgg(0x5676af, 0xb6ecf1).build()
					.loot(EntityLootGen::cirno).register();
		}

		{
			GENERAL_YOUKAI = YoukaisHomecoming.REGISTRATE
					.entity("youkai", BossYoukaiEntity::new, MobCategory.MONSTER)
					.properties(e -> e.sized(0.4F, 1.8f).clientTrackingRange(10))
					.attributes(BossYoukaiEntity::createAttributes)
					.renderer(() -> GeneralYoukaiRenderer::new)
					.spawnEgg(0x000000, 0x000000).build()
					.tag(YHTagGen.BOSS)
					.loot(EntityLootGen::noLoot).register();

			FAIRY = YoukaisHomecoming.REGISTRATE
					.entity("fairy", FairyEntity::new, MobCategory.MONSTER)
					.properties(e -> e.sized(0.4F, 1.8f).clientTrackingRange(10))
					.attributes(FairyEntity::createAttributes)
					.renderer(() -> GeneralYoukaiRenderer::new)
					.spawnEgg(0x000000, 0x000000).build()
					.loot(EntityLootGen::noLoot).register();

			YUKARI = YoukaisHomecoming.REGISTRATE
					.entity("yukari", YukariEntity::new, MobCategory.MONSTER)
					.properties(e -> e.sized(0.4F, 1.8f).clientTrackingRange(10))
					.attributes(BossYoukaiEntity::createAttributes)
					.renderer(() -> GeneralYoukaiRenderer::new)
					.spawnEgg(0x4B1442, 0xFFFFFF).build()
					.tag(YHTagGen.BOSS)
					.loot(EntityLootGen::yukari).register();

			SANAE = YoukaisHomecoming.REGISTRATE
					.entity("kochiya_sanae", SanaeEntity::new, MobCategory.MONSTER)
					.properties(e -> e.sized(0.4F, 1.8f).clientTrackingRange(10))
					.attributes(BossYoukaiEntity::createAttributes)
					.renderer(() -> GeneralYoukaiRenderer::new)
					.spawnEgg(0x4eaff9, 0xFFFFFF).build()
					.tag(YHTagGen.BOSS)
					.loot(EntityLootGen::sanae).register();

			MARISA = YoukaisHomecoming.REGISTRATE
					.entity("kirisame_marisa", MarisaEntity::new, MobCategory.MONSTER)
					.properties(e -> e.sized(0.4F, 1.8f).clientTrackingRange(10))
					.attributes(BossYoukaiEntity::createAttributes)
					.renderer(() -> GeneralYoukaiRenderer::new)
					.spawnEgg(0x52403C, 0xFAF2EF).build()
					.tag(YHTagGen.BOSS)
					.loot(EntityLootGen::marisa).register();

			KOISHI = YoukaisHomecoming.REGISTRATE
					.entity("komeiji_koishi", KoishiEntity::new, MobCategory.MONSTER)
					.properties(e -> e.sized(0.4F, 1.8f).clientTrackingRange(10))
					.attributes(BossYoukaiEntity::createAttributes)
					.renderer(() -> GeneralYoukaiRenderer::new)
					.spawnEgg(0x88BA7F, 0x645856).build()
					.tag(YHTagGen.BOSS)
					.loot(EntityLootGen::koishi).register();

			REMILIA = YoukaisHomecoming.REGISTRATE
					.entity("remilia_scarlet", RemiliaEntity::new, MobCategory.MONSTER)
					.properties(e -> e.sized(0.4F, 1.8f).clientTrackingRange(10))
					.attributes(BossYoukaiEntity::createAttributes)
					.renderer(() -> GeneralYoukaiRenderer::new)
					.spawnEgg(0xF3C1CC, 0x86B9F3).build()
					.tag(YHTagGen.BOSS)
					.loot(EntityLootGen::remilia).register();

			MYSTIA = YoukaisHomecoming.REGISTRATE
					.entity("mystia_lorelei", MystiaEntity::new, MobCategory.MONSTER)
					.properties(e -> e.sized(0.4F, 1.8f).clientTrackingRange(10))
					.attributes(BossYoukaiEntity::createAttributes)
					.renderer(() -> GeneralYoukaiRenderer::new)
					.spawnEgg(0x9B6D79, 0xF4BDAE).build()
					.tag(YHTagGen.BOSS)
					.loot(EntityLootGen::mystia).register();

			SUNNY = YoukaisHomecoming.REGISTRATE
					.entity("sunny_milk", SunnyEntity::new, MobCategory.MONSTER)
					.properties(e -> e.sized(0.4F, 1.8f).clientTrackingRange(10))
					.attributes(FairyEntity::createAttributes)
					.renderer(() -> GeneralYoukaiRenderer::new)
					.spawnEgg(0xB14435, 0xFCF5D8).build()
					.loot(EntityLootGen::fairy).register();

			LUNA = YoukaisHomecoming.REGISTRATE
					.entity("luna_child", LunaEntity::new, MobCategory.MONSTER)
					.properties(e -> e.sized(0.4F, 1.8f).clientTrackingRange(10))
					.attributes(FairyEntity::createAttributes)
					.renderer(() -> GeneralYoukaiRenderer::new)
					.spawnEgg(0xFFF9DA, 0xA26B4F).build()
					.loot(EntityLootGen::fairy).register();

			STAR = YoukaisHomecoming.REGISTRATE
					.entity("star_sapphire", StarEntity::new, MobCategory.MONSTER)
					.properties(e -> e.sized(0.4F, 1.8f).clientTrackingRange(10))
					.attributes(FairyEntity::createAttributes)
					.renderer(() -> GeneralYoukaiRenderer::new)
					.spawnEgg(0x353D95, 0x482E25).build()
					.loot(EntityLootGen::fairy).register();

			LARVA = YoukaisHomecoming.REGISTRATE
					.entity("eternity_larva", LarvaEntity::new, MobCategory.MONSTER)
					.properties(e -> e.sized(0.4F, 1.8f).clientTrackingRange(10))
					.attributes(LarvaEntity::createAttributes)
					.renderer(() -> GeneralYoukaiRenderer::new)
					.spawnEgg(0x92B445, 0x93C9E9).build()
					.loot(EntityLootGen::fairy).register();

			CLOWN = YoukaisHomecoming.REGISTRATE
					.entity("clownpiece", ClownEntity::new, MobCategory.MONSTER)
					.properties(e -> e.sized(0.4F, 1.8f).clientTrackingRange(10))
					.attributes(BossYoukaiEntity::createAttributes)
					.renderer(() -> GeneralYoukaiRenderer::new)
					.spawnEgg(0x008CCA, 0xCB0000).build()
					.tag(YHTagGen.BOSS)
					.loot(EntityLootGen::clownpiece).register();


		}

		{
			FROZEN_FROG = YoukaisHomecoming.REGISTRATE
					.<FrozenFrog>entity("frozen_frog", FrozenFrog::new, MobCategory.MISC)
					.properties(p -> p.sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10))
					.renderer(() -> ThrownItemRenderer::new)
					.register();

			FAIRY_ICE = YoukaisHomecoming.REGISTRATE
					.<FairyIce>entity("fairy_ice_crystal", FairyIce::new, MobCategory.MISC)
					.properties(p -> p.sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10))
					.renderer(() -> ThrownItemRenderer::new)
					.register();

			ITEM_DANMAKU = YoukaisHomecoming.REGISTRATE
					.<ItemDanmakuEntity>entity("item_danmaku", ItemDanmakuEntity::new, MobCategory.MISC)
					.properties(e -> e.sized(0.4f, 0.4f).clientTrackingRange(4).updateInterval(1 << 16))
					.renderer(() -> ItemDanmakuRenderer::new)
					.register();

			SHOOTER = YoukaisHomecoming.REGISTRATE
					.entity("shooter", ShooterEntity::new, MobCategory.MISC)
					.properties(e -> e.sized(0.4F, 0.4f).clientTrackingRange(10))
					.attributes(LivingEntity::createLivingAttributes)
					.renderer(() -> ShooterRenderer::new)
					.register();

			ITEM_LASER = YoukaisHomecoming.REGISTRATE
					.<ItemLaserEntity>entity("item_laser", ItemLaserEntity::new, MobCategory.MISC)
					.properties(e -> e.sized(0.4f, 0.4f).clientTrackingRange(4).updateInterval(1 << 16))
					.renderer(() -> ItemLaserRenderer::new)
					.register();

			CHAIR = YoukaisHomecoming.REGISTRATE
					.<ChairEntity>entity("dining_chair", ChairEntity::new, MobCategory.MISC)
					.properties(e -> e.sized(0.25f, 0.5f))
					.renderer(() -> NothingRenderer::new)
					.register();
		}

	}

	public static void register() {
	}

}
