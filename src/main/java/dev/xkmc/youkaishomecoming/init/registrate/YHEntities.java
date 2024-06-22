package dev.xkmc.youkaishomecoming.init.registrate;

import com.tterrag.registrate.util.entry.EntityEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.youkaishomecoming.content.block.furniture.SeatEntity;
import dev.xkmc.youkaishomecoming.content.block.furniture.NothingRenderer;
import dev.xkmc.youkaishomecoming.content.entity.boss.BossYoukaiEntity;
import dev.xkmc.youkaishomecoming.content.entity.boss.SanaeEntity;
import dev.xkmc.youkaishomecoming.content.entity.boss.YukariEntity;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.ItemDanmakuEntity;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.ItemDanmakuRenderer;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.ItemLaserEntity;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.ItemLaserRenderer;
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
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.registries.ForgeRegistries;

public class YHEntities {

	public static final EntityEntry<LampreyEntity> LAMPREY;
	public static final EntityEntry<RumiaEntity> RUMIA;
	public static final EntityEntry<ReimuEntity> REIMU;
	public static final EntityEntry<CirnoEntity> CIRNO;

	public static final EntityEntry<BossYoukaiEntity> GENERAL_YOUKAI;
	public static final EntityEntry<FairyEntity> FAIRY;
	public static final EntityEntry<YukariEntity> YUKARI;
	public static final EntityEntry<SanaeEntity> SANAE;

	public static final EntityEntry<FrozenFrog> FROZEN_FROG;
	public static final EntityEntry<FairyIce> FAIRY_ICE;
	public static final EntityEntry<ItemDanmakuEntity> ITEM_DANMAKU;
	public static final EntityEntry<ItemLaserEntity> ITEM_LASER;
	public static final EntityEntry<SeatEntity> SEAT;

	static {

		{
			LAMPREY = YoukaisHomecoming.REGISTRATE
					.entity("lamprey", LampreyEntity::new, MobCategory.WATER_AMBIENT)
					.properties(e -> e.sized(0.5F, 0.4F).clientTrackingRange(4))
					.spawnPlacement(SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules)
					.attributes(LampreyEntity::createAttributes)
					.renderer(() -> LampreyRenderer::new)
					.spawnEgg(-3814463, -6646165).tab(YoukaisHomecoming.TAB.getKey()).build()
					.loot(EntityLootGen::lamprey).register();

			RUMIA = YoukaisHomecoming.REGISTRATE
					.entity("rumia", RumiaEntity::new, MobCategory.MONSTER)
					.properties(e -> e.sized(0.4F, 1.7f).clientTrackingRange(10))
					.spawnPlacement(SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, RumiaEntity::checkRumiaSpawnRules)
					.attributes(RumiaEntity::createAttributes)
					.renderer(() -> RumiaRenderer::new)
					.spawnEgg(0x000000, 0x000000).tab(YHDanmaku.TAB.getKey()).build()
					.loot(EntityLootGen::rumia).register();

			REIMU = YoukaisHomecoming.REGISTRATE
					.entity("shrine_maiden", ReimuEntity::new, MobCategory.MONSTER)
					.properties(e -> e.sized(0.4F, 1.8f).clientTrackingRange(10))
					.attributes(BossYoukaiEntity::createAttributes)
					.renderer(() -> ReimuRenderer::new)
					.spawnEgg(0xC20C1C, 0xFFFFFF).tab(YHDanmaku.TAB.getKey()).build()
					.loot(EntityLootGen::reimu).register();

			CIRNO = YoukaisHomecoming.REGISTRATE
					.entity("cirno", CirnoEntity::new, MobCategory.MONSTER)
					.properties(e -> e.sized(0.4F, 1.8f).clientTrackingRange(10))
					.spawnPlacement(SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, CirnoEntity::checkCirnoSpawnRules)
					.attributes(CirnoEntity::createAttributes)
					.renderer(() -> CirnoRenderer::new)
					.spawnEgg(0xA8C3D9, 0x7E8DC4).tab(YHDanmaku.TAB.getKey()).build()
					.loot(EntityLootGen::cirno).register();
		}

		{
			GENERAL_YOUKAI = YoukaisHomecoming.REGISTRATE
					.entity("youkai", BossYoukaiEntity::new, MobCategory.MONSTER)
					.properties(e -> e.sized(0.4F, 1.8f).clientTrackingRange(10))
					.attributes(BossYoukaiEntity::createAttributes)
					.renderer(() -> GeneralYoukaiRenderer::new)
					.spawnEgg(0x000000, 0x000000).tab(YHDanmaku.TAB.getKey()).build()
					.loot(EntityLootGen::noLoot).register();

			FAIRY = YoukaisHomecoming.REGISTRATE
					.entity("fairy", FairyEntity::new, MobCategory.MONSTER)
					.properties(e -> e.sized(0.4F, 1.8f).clientTrackingRange(10))
					.attributes(FairyEntity::createAttributes)
					.renderer(() -> GeneralYoukaiRenderer::new)
					.spawnEgg(0x000000, 0x000000).tab(YHDanmaku.TAB.getKey()).build()
					.loot(EntityLootGen::noLoot).register();

			YUKARI = YoukaisHomecoming.REGISTRATE
					.entity("yukari", YukariEntity::new, MobCategory.MONSTER)
					.properties(e -> e.sized(0.4F, 1.8f).clientTrackingRange(10))
					.attributes(BossYoukaiEntity::createAttributes)
					.renderer(() -> GeneralYoukaiRenderer::new)
					.spawnEgg(0x4B1442, 0xFFFFFF).tab(YHDanmaku.TAB.getKey()).build()
					.loot(EntityLootGen::noLoot).register();

			SANAE = YoukaisHomecoming.REGISTRATE
					.entity("kochiya_sanae", SanaeEntity::new, MobCategory.MONSTER)
					.properties(e -> e.sized(0.4F, 1.8f).clientTrackingRange(10))
					.attributes(BossYoukaiEntity::createAttributes)
					.renderer(() -> GeneralYoukaiRenderer::new)
					.spawnEgg(0x4eaff9, 0xFFFFFF).tab(YHDanmaku.TAB.getKey()).build()
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

			ITEM_DANMAKU = YoukaisHomecoming.REGISTRATE
					.<ItemDanmakuEntity>entity("item_danmaku", ItemDanmakuEntity::new, MobCategory.MISC)
					.properties(e -> e.sized(0.4f, 0.4f).clientTrackingRange(4).updateInterval(1 << 16))
					.renderer(() -> ItemDanmakuRenderer::new)
					.register();

			ITEM_LASER = YoukaisHomecoming.REGISTRATE
					.<ItemLaserEntity>entity("item_laser", ItemLaserEntity::new, MobCategory.MISC)
					.properties(e -> e.sized(0.4f, 0.4f).clientTrackingRange(4).updateInterval(1 << 16))
					.renderer(() -> ItemLaserRenderer::new)
					.register();

			SEAT = YoukaisHomecoming.REGISTRATE
					.<SeatEntity>entity("seat", SeatEntity::new, MobCategory.MISC)
					.properties(e -> e.sized(0.25f, 0.5f))
					.renderer(() -> NothingRenderer::new)
					.register();
		}

	}

	private static <A extends RecipeSerializer<?>> RegistryEntry<A> reg(String id, NonNullSupplier<A> sup) {
		return YoukaisHomecoming.REGISTRATE.simple(id, ForgeRegistries.Keys.RECIPE_SERIALIZERS, sup);
	}

	public static void register() {
	}

}
