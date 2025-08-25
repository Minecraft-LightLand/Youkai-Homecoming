package dev.xkmc.youkaishomecoming.content.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

public record NoiseQuadCondition(
		ResourceKey<NormalNoise.NoiseParameters> noise,
		double radius, double threshold
) implements SurfaceRules.ConditionSource {

	public static final KeyDispatchDataCodec<NoiseQuadCondition> CODEC = KeyDispatchDataCodec.of(RecordCodecBuilder.mapCodec((i) -> i.group(
			ResourceKey.codec(Registries.NOISE).fieldOf("noise").forGetter(e -> e.noise),
			Codec.DOUBLE.fieldOf("radius").forGetter(e -> e.radius),
			Codec.DOUBLE.fieldOf("threshold").forGetter(e -> e.threshold)
	).apply(i, NoiseQuadCondition::new)));

	public KeyDispatchDataCodec<? extends SurfaceRules.ConditionSource> codec() {
		return CODEC;
	}

	public SurfaceRules.Condition apply(final SurfaceRules.Context ctx) {
		final NormalNoise normalnoise = ctx.randomState.getOrCreateNoise(this.noise);
		return new NoiseThresholdCondition(ctx, normalnoise);
	}

	private class NoiseThresholdCondition extends SurfaceRules.LazyXZCondition {
		private final NormalNoise normalnoise;

		NoiseThresholdCondition(SurfaceRules.Context ctx, NormalNoise normalnoise) {
			super(ctx);
			this.normalnoise = normalnoise;
		}

		protected boolean compute() {
			double d0 = normalnoise.getValue(context.blockX, 0.0D, context.blockZ);
			if (Math.abs(d0) > threshold) return false;

			double d1 = normalnoise.getValue(context.blockX - radius, 0.0D, context.blockZ - radius);
			double d2 = normalnoise.getValue(context.blockX - radius, 0.0D, context.blockZ + radius);
			double d3 = normalnoise.getValue(context.blockX + radius, 0.0D, context.blockZ - radius);
			double d4 = normalnoise.getValue(context.blockX + radius, 0.0D, context.blockZ + radius);

			return !(d1 < 0 && d2 < 0 && d3 < 0 && d4 < 0 || d1 > 0 && d2 > 0 && d3 > 0 && d4 > 0);
		}
	}
}