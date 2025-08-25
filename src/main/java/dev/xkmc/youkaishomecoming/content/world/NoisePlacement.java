
package dev.xkmc.youkaishomecoming.content.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.youkaishomecoming.init.registrate.YHWorldGen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class NoisePlacement extends PlacementModifier {

	public static final Codec<NoisePlacement> CODEC = RecordCodecBuilder.create((i) -> i.group(
			ResourceLocation.CODEC.fieldOf("noise").forGetter(e -> e.noise.location()),
			Codec.DOUBLE.fieldOf("reference").forGetter((e) -> e.reference),
			Codec.DOUBLE.fieldOf("low").forGetter((e) -> e.low),
			Codec.DOUBLE.fieldOf("high").forGetter((e) -> e.high),
			Codec.DOUBLE.fieldOf("min").forGetter((e) -> e.min),
			Codec.DOUBLE.fieldOf("max").forGetter((e) -> e.max)
	).apply(i, NoisePlacement::new));

	private final ResourceKey<NormalNoise.NoiseParameters> noise;
	private final double reference;
	private final double low, high, min, max;

	private NoisePlacement(ResourceLocation noise, double level, double low, double high, double min, double max) {
		this.noise = ResourceKey.create(Registries.NOISE, noise);
		this.reference = level;
		this.low = low;
		this.high = high;
		this.min = min;
		this.max = max;
	}

	public static NoisePlacement of(ResourceLocation noise, double level, double low, double high, double min, double max) {
		return new NoisePlacement(noise, level, low, high, min, max);
	}

	private double getNoise(PlacementContext ctx, BlockPos ipos) {
		var chunkSource = ctx.getLevel().getChunkSource();
		if (chunkSource instanceof ServerChunkCache cache) {
			var state = cache.randomState();
			return state.getOrCreateNoise(noise).getValue(ipos.getX(), 0, ipos.getZ());
		}
		return 0;
	}

	public Stream<BlockPos> getPositions(PlacementContext ctx, RandomSource rand, BlockPos pos) {
		List<BlockPos> ans = new ArrayList<>();
		for (int i = 0; i < max; i++) {
			var ipos = pos.offset(rand.nextInt(16), 0, rand.nextInt(16));
			double noise = Math.abs(getNoise(ctx, ipos) - reference);
			if (noise < low) continue;
			noise = (Mth.clamp(noise, low, high) - low) / (high - low);
			double density = (min + (max - min) * noise) / max;
			if (rand.nextFloat() < density)
				ans.add(ipos);
		}
		return ans.stream();
	}

	public PlacementModifierType<?> type() {
		return YHWorldGen.NOISE.get();
	}
}