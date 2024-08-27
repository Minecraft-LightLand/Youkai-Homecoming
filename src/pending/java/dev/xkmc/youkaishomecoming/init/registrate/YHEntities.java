package dev.xkmc.youkaishomecoming.init.registrate;

import com.tterrag.registrate.util.entry.EntityEntry;
import dev.xkmc.danmakuapi.init.DanmakuAPI;
import dev.xkmc.youkaishomecoming.content.block.furniture.ChairEntity;
import dev.xkmc.youkaishomecoming.content.block.furniture.NothingRenderer;
import dev.xkmc.youkaishomecoming.content.entity.boss.BossYoukaiEntity;
import dev.xkmc.youkaishomecoming.content.entity.boss.KoishiEntity;
import dev.xkmc.youkaishomecoming.content.entity.boss.SanaeEntity;
import dev.xkmc.youkaishomecoming.content.entity.boss.YukariEntity;
import dev.xkmc.youkaishomecoming.content.entity.fairy.CirnoEntity;
import dev.xkmc.youkaishomecoming.content.entity.fairy.CirnoRenderer;
import dev.xkmc.youkaishomecoming.content.entity.fairy.FairyEntity;
import dev.xkmc.youkaishomecoming.content.entity.lampery.LampreyEntity;
import dev.xkmc.youkaishomecoming.content.entity.lampery.LampreyRenderer;
import dev.xkmc.youkaishomecoming.content.entity.misc.FairyIce;
import dev.xkmc.youkaishomecoming.content.entity.misc.FrozenFrog;
import dev.xkmc.youkaishomecoming.content.entity.reimu.ReimuEntity;
import dev.xkmc.youkaishomecoming.content.entity.reimu.ReimuRenderer;
import dev.xkmc.youkaishomecoming.content.entity.rumia.RumiaEntity;
import dev.xkmc.youkaishomecoming.content.entity.rumia.RumiaRenderer;
import dev.xkmc.youkaishomecoming.content.entity.youkai.GeneralYoukaiRenderer;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.loot.EntityLootGen;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

public class YHEntities {

	public static final EntityEntry<RumiaEntity> RUMIA;
	public static final EntityEntry<ReimuEntity> REIMU;
	public static final EntityEntry<CirnoEntity> CIRNO;

	public static final EntityEntry<BossYoukaiEntity> GENERAL_YOUKAI;
	public static final EntityEntry<FairyEntity> FAIRY;
	public static final EntityEntry<YukariEntity> YUKARI;
	public static final EntityEntry<SanaeEntity> SANAE;
	public static final EntityEntry<KoishiEntity> KOISHI;

	public static final EntityEntry<FrozenFrog> FROZEN_FROG;
	public static final EntityEntry<FairyIce> FAIRY_ICE

	static {

		{
			RUMIA = YoukaisHomecoming.REGISTRATE
					.entity("rumia", RumiaEntity::new, MobCategory.MONSTER)
					.properties(e -> e.sized(0.4F, 1.7f).clientTrackingRange(10))
					.spawnPlacement(SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
							RumiaEntity::checkRumiaSpawnRules, RegisterSpawnPlacementsEvent.Operation.OR)
					.attributes(RumiaEntity::createAttributes)
					.renderer(() -> RumiaRenderer::new)
					.spawnEgg(0x000000, 0x000000).tab(DanmakuAPI.TAB.key()).build()
					.loot(EntityLootGen::rumia).register();

			REIMU = YoukaisHomecoming.REGISTRATE
					.entity("shrine_maiden", ReimuEntity::new, MobCategory.MONSTER)
					.properties(e -> e.sized(0.4F, 1.8f).clientTrackingRange(10))
					.attributes(BossYoukaiEntity::createAttributes)
					.renderer(() -> ReimuRenderer::new)
					.spawnEgg(0xa93937, 0xfaf5f2).tab(DanmakuAPI.TAB.key()).build()
					.loot(EntityLootGen::reimu).register();

			CIRNO = YoukaisHomecoming.REGISTRATE
					.entity("cirno", CirnoEntity::new, MobCategory.MONSTER)
					.properties(e -> e.sized(0.4F, 1.8f).clientTrackingRange(10))
					.spawnPlacement(SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
							CirnoEntity::checkCirnoSpawnRules, RegisterSpawnPlacementsEvent.Operation.OR)
					.attributes(CirnoEntity::createAttributes)
					.renderer(() -> CirnoRenderer::new)
					.spawnEgg(0x5676af, 0xb6ecf1).tab(DanmakuAPI.TAB.key()).build()
					.loot(EntityLootGen::cirno).register();
		}

		{

			FAIRY = YoukaisHomecoming.REGISTRATE
					.entity("fairy", FairyEntity::new, MobCategory.MONSTER)
					.properties(e -> e.sized(0.4F, 1.8f).clientTrackingRange(10))
					.attributes(FairyEntity::createAttributes)
					.renderer(() -> GeneralYoukaiRenderer::new)
					.spawnEgg(0x000000, 0x000000).tab(DanmakuAPI.TAB.key()).build()
					.loot(EntityLootGen::noLoot).register();

			YUKARI = YoukaisHomecoming.REGISTRATE
					.entity("yukari", YukariEntity::new, MobCategory.MONSTER)
					.properties(e -> e.sized(0.4F, 1.8f).clientTrackingRange(10))
					.attributes(BossYoukaiEntity::createAttributes)
					.renderer(() -> GeneralYoukaiRenderer::new)
					.spawnEgg(0x4B1442, 0xFFFFFF).tab(DanmakuAPI.TAB.key()).build()
					.loot(EntityLootGen::noLoot).register();

			SANAE = YoukaisHomecoming.REGISTRATE
					.entity("kochiya_sanae", SanaeEntity::new, MobCategory.MONSTER)
					.properties(e -> e.sized(0.4F, 1.8f).clientTrackingRange(10))
					.attributes(BossYoukaiEntity::createAttributes)
					.renderer(() -> GeneralYoukaiRenderer::new)
					.spawnEgg(0x4eaff9, 0xFFFFFF).tab(DanmakuAPI.TAB.key()).build()
					.loot(EntityLootGen::noLoot).register();

			KOISHI = YoukaisHomecoming.REGISTRATE
					.entity("komeiji_koishi", KoishiEntity::new, MobCategory.MONSTER)
					.properties(e -> e.sized(0.4F, 1.8f).clientTrackingRange(10))
					.attributes(BossYoukaiEntity::createAttributes)
					.renderer(() -> GeneralYoukaiRenderer::new)
					.spawnEgg(0x88BA7F, 0x645856).tab(DanmakuAPI.TAB.key()).build()
					.loot(EntityLootGen::noLoot).register();
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

		}

	}

	public static void register() {
	}

}
