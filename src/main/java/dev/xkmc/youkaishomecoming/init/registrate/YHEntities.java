package dev.xkmc.youkaishomecoming.init.registrate;

import com.tterrag.registrate.util.entry.EntityEntry;
import dev.xkmc.youkaishomecoming.content.block.furniture.ChairEntity;
import dev.xkmc.youkaishomecoming.content.block.furniture.NothingRenderer;
import dev.xkmc.youkaishomecoming.content.entity.crab.CrabEntity;
import dev.xkmc.youkaishomecoming.content.entity.crab.CrabRenderer;
import dev.xkmc.youkaishomecoming.content.entity.deer.DeerEntity;
import dev.xkmc.youkaishomecoming.content.entity.deer.DeerRenderer;
import dev.xkmc.youkaishomecoming.content.entity.lampery.LampreyEntity;
import dev.xkmc.youkaishomecoming.content.entity.lampery.LampreyRenderer;
import dev.xkmc.youkaishomecoming.content.entity.tuna.TunaEntity;
import dev.xkmc.youkaishomecoming.content.entity.tuna.TunaRenderer;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.loot.EntityLootGen;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.level.levelgen.Heightmap;

public class YHEntities {

	public static final EntityEntry<LampreyEntity> LAMPREY;
	public static final EntityEntry<TunaEntity> TUNA;
	public static final EntityEntry<DeerEntity> DEER;
	public static final EntityEntry<CrabEntity> CRAB;
	public static final EntityEntry<ChairEntity> CHAIR;

	static {

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
				.spawnEgg(0x424F75, 0xE08E46).build()//TODO
				.loot(EntityLootGen::deer).register();

		CRAB = YoukaisHomecoming.REGISTRATE
				.entity("crab", CrabEntity::new, MobCategory.WATER_AMBIENT)
				.properties(e -> e.sized(0.6f, 0.3f).clientTrackingRange(10))
				.spawnPlacement(SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules)
				.attributes(CrabEntity::createAttributes)
				.renderer(() -> CrabRenderer::new)
				.spawnEgg(0x424F75, 0xE08E46).build()//TODO
				.loot(EntityLootGen::crab).register();

		CHAIR = YoukaisHomecoming.REGISTRATE
				.<ChairEntity>entity("dining_chair", ChairEntity::new, MobCategory.MISC)
				.properties(e -> e.sized(0.25f, 0.5f))
				.renderer(() -> NothingRenderer::new)
				.register();

	}

	public static void register() {
	}

}
