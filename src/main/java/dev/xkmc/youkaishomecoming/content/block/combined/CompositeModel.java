package dev.xkmc.youkaishomecoming.content.block.combined;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public record CompositeModel(BakedModel base, BakedModel a, BakedModel b) implements BakedModel {

	@Override
	public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction direction, RandomSource random) {
		List<BakedQuad> ans = new ArrayList<>();
		for (var e : base.getQuads(state, direction, random)) {
			int tint = e.getTintIndex();
			var set = tint == 1 ? a : b;
			var quadA = set.getQuads(state, e.getDirection(), random).getFirst();
			var spr = quadA.getSprite();
			int[] vtx = new int[32];
			int[] base = e.getVertices();
			var espr = e.getSprite();
			var eu0 = espr.getU0();
			var eu1 = espr.getU1();
			var ev0 = espr.getV0();
			var ev1 = espr.getV1();
			for (int i = 0; i < 32; i++) {
				int index = i & 0x7;
				if (index == 4) {
					float f = Float.intBitsToFloat(base[i]);
					vtx[i] = Float.floatToRawIntBits(spr.getU((f - eu0) / (eu1 - eu0)));
				} else if (index == 5) {
					float f = Float.intBitsToFloat(base[i]);
					vtx[i] = Float.floatToRawIntBits(spr.getV((f - ev0) / (ev1 - ev0)));
				} else {
					vtx[i] = base[i];
				}

			}
			ans.add(new BakedQuad(vtx, e.getTintIndex(), e.getDirection(), spr, e.isShade(), e.hasAmbientOcclusion()));
		}
		return ans;
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
		return a.getParticleIcon();
	}

	@Override
	public ItemOverrides getOverrides() {
		return base.getOverrides();
	}

}
