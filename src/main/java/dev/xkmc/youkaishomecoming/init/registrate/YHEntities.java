package dev.xkmc.youkaishomecoming.init.registrate;

import com.tterrag.registrate.util.entry.EntityEntry;
import dev.xkmc.youkaishomecoming.content.block.furniture.ChairEntity;
import dev.xkmc.youkaishomecoming.content.block.furniture.NothingRenderer;
import dev.xkmc.youkaishomecoming.content.entity.lampery.LampreyEntity;
import dev.xkmc.youkaishomecoming.content.entity.lampery.LampreyRenderer;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.loot.EntityLootGen;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

public class YHEntities {

	public static final EntityEntry<LampreyEntity> LAMPREY;
	public static final EntityEntry<ChairEntity> CHAIR;

	static {

		YoukaisHomecoming.REGISTRATE.defaultCreativeTab(YoukaisHomecoming.TAB.key());

		LAMPREY = YoukaisHomecoming.REGISTRATE
				.entity("lamprey", LampreyEntity::new, MobCategory.WATER_AMBIENT)
				.properties(e -> e.sized(0.5F, 0.4F).clientTrackingRange(4))
				.spawnPlacement(SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
						(t, l, reason, pos, rand) ->
								pos.getY() >= 50 && pos.getY() <= 64 &&
										WaterAnimal.checkSurfaceWaterAnimalSpawnRules(t, l, reason, pos, rand),
						RegisterSpawnPlacementsEvent.Operation.REPLACE)
				.attributes(LampreyEntity::createAttributes)
				.renderer(() -> LampreyRenderer::new)
				.spawnEgg(-3814463, -6646165).tab(YoukaisHomecoming.TAB.key()).build()
				.loot(EntityLootGen::lamprey).register();

		CHAIR = YoukaisHomecoming.REGISTRATE
				.<ChairEntity>entity("dining_chair", ChairEntity::new, MobCategory.MISC)
				.properties(e -> e.sized(0.25f, 0.5f))
				.renderer(() -> NothingRenderer::new)
				.register();


	}

	public static void register() {
	}

}
