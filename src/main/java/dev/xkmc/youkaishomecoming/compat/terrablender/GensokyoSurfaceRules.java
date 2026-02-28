package dev.xkmc.youkaishomecoming.compat.terrablender;

import dev.xkmc.youkaishomecoming.content.world.NoiseQuadCondition;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.data.YHBiomes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

public class GensokyoSurfaceRules {

	public static final ResourceKey<NormalNoise.NoiseParameters> PATH_NOISE = createKey("path");

	public static SurfaceRules.RuleSource buildRules() {
		var lo = new NoiseQuadCondition(PATH_NOISE, 1.5, 0.1);
		var state = SurfaceRules.state(Blocks.DIRT_PATH.defaultBlockState());
		return SurfaceRules.ifTrue(SurfaceRules.abovePreliminarySurface(),
				SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR,
						SurfaceRules.ifTrue(SurfaceRules.isBiome(YHBiomes.ANIMAL_PATH),
								SurfaceRules.ifTrue(SurfaceRules.waterBlockCheck(0, 0),
										SurfaceRules.ifTrue(lo, state))))
		);
	}

	private static ResourceKey<NormalNoise.NoiseParameters> createKey(String id) {
		return ResourceKey.create(Registries.NOISE, YoukaisHomecoming.loc(id));
	}

}
