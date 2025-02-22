package dev.xkmc.youkaishomecoming.content.block.combined;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record CompositeModel(BakedModel base, Map<ModelKey, ModelQuads> cache) implements BakedModel {

	private record ModelQuads(
			List<BakedQuad> unCulled,
			Map<Direction, List<BakedQuad>> culled,
			TextureAtlasSprite particle) {

	}

	private record ModelKey(BlockState state, IBlockSet a, IBlockSet b) {

	}

	private static final ModelProperty<ModelKey> PART = new ModelProperty<>();

	private static ModelQuads build(BakedModel base, BakedModel a, BakedModel b, @Nullable BlockState state, RandomSource random) {
		var unCulled = buildFace(base, a, b, null, state, random);
		Map<Direction, List<BakedQuad>> map = new HashMap<>();
		for (var e : Direction.values()) {
			map.put(e, buildFace(base, a, b, e, state, random));
		}
		return new ModelQuads(unCulled, map, a.getParticleIcon());
	}

	private static List<BakedQuad> buildFace(BakedModel base, BakedModel a, BakedModel b, @Nullable Direction direction, @Nullable BlockState state, RandomSource random) {
		List<BakedQuad> ans = new ArrayList<>();
		for (var e : base.getQuads(state, direction, random)) {
			int tint = e.getTintIndex();
			var set = tint == 1 ? a : b;
			var quadA = set.getQuads(state, e.getDirection(), random).getFirst();
			var spr = quadA.getSprite();
			int[] vtx = new int[32];
			int[] oldVtx = e.getVertices();
			var espr = e.getSprite();
			var eu0 = espr.getU0();
			var eu1 = espr.getU1();
			var ev0 = espr.getV0();
			var ev1 = espr.getV1();
			for (int i = 0; i < 32; i++) {
				int index = i & 0x7;
				if (index == 4) {
					float f = Float.intBitsToFloat(oldVtx[i]);
					vtx[i] = Float.floatToRawIntBits(spr.getU((f - eu0) / (eu1 - eu0)));
				} else if (index == 5) {
					float f = Float.intBitsToFloat(oldVtx[i]);
					vtx[i] = Float.floatToRawIntBits(spr.getV((f - ev0) / (ev1 - ev0)));
				} else {
					vtx[i] = oldVtx[i];
				}

			}
			ans.add(new BakedQuad(vtx, e.getTintIndex(), e.getDirection(), spr, e.isShade(), e.hasAmbientOcclusion()));
		}
		return ans;
	}

	@Override
	public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand, ModelData data, @Nullable RenderType renderType) {
		var key = data.get(PART);
		if (key == null) return List.of();
		var cached = cache.get(key);
		if (cached == null) {
			var shaper = Minecraft.getInstance().getModelManager().getBlockModelShaper();
			BakedModel modelA = shaper.getBlockModel(key.a.base().value().defaultBlockState());
			BakedModel modelB = shaper.getBlockModel(key.b.base().value().defaultBlockState());
			cached = build(base, modelA, modelB, state, rand);
			cache.put(key, cached);
		}
		return side == null ? cached.unCulled() : cached.culled().get(side);
	}

	@Override
	public TextureAtlasSprite getParticleIcon(ModelData data) {
		var key = data.get(PART);
		if (key == null) return base.getParticleIcon();
		var cached = cache.get(key);
		return cached == null ? base.getParticleIcon() : cached.particle();
	}

	@Override
	public ModelData getModelData(BlockAndTintGetter level, BlockPos pos, BlockState state, ModelData modelData) {
		if (!(level.getBlockEntity(pos) instanceof CombinedBlockEntity be)) return modelData;
		IBlockSet a = be.getA(), b = be.getB();
		if (a == null || b == null) return modelData;
		ModelKey key = new ModelKey(state, a, b);
		return modelData.derive().with(PART, key).build();
	}

	@Override
	public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction direction, RandomSource random) {
		return List.of();
	}

	@Override
	public boolean useAmbientOcclusion() {
		return base.useAmbientOcclusion();
	}

	@Override
	public boolean isGui3d() {
		return base.isGui3d();
	}

	@Override
	public boolean usesBlockLight() {
		return base.usesBlockLight();
	}

	@Override
	public boolean isCustomRenderer() {
		return false;
	}

	@Override
	public TextureAtlasSprite getParticleIcon() {
		return base.getParticleIcon();
	}

	@Override
	public ItemOverrides getOverrides() {
		return base.getOverrides();
	}

}
