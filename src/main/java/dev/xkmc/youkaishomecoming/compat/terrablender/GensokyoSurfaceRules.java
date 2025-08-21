package dev.xkmc.youkaishomecoming.compat.terrablender;

import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

public class GensokyoSurfaceRules {

	public static final ResourceKey<NormalNoise.NoiseParameters> PATH_NOISE = createKey("path");

	public static SurfaceRules.RuleSource buildRules() {
		var lo = SurfaceRules.noiseCondition(PATH_NOISE, -0.05, 0.05);
		var state = SurfaceRules.state(Blocks.DIRT_PATH.defaultBlockState());
		return //SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.PLAINS),
				SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR,
						SurfaceRules.ifTrue(lo, state));
	}

	private static ResourceKey<NormalNoise.NoiseParameters> createKey(String id) {
		return ResourceKey.create(Registries.NOISE, YoukaisHomecoming.loc(id));
	}

}
