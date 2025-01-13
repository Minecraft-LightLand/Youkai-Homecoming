package dev.xkmc.youkaishomecoming.content.block.combined;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.event.AddSectionGeometryEvent;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.lighting.LightPipelineAwareModelBlockRenderer;

import java.util.HashMap;
import java.util.Map;

public class CompositeBlockRenderer {

	private static final RandomSource random = RandomSource.createNewThreadLocalInstance();

	public static void renderSection(BlockPos posSource, AddSectionGeometryEvent.SectionRenderingContext ctx) {
		BlockAndTintGetter region = ctx.getRegion();
		BlockPos posTarget = posSource.offset(15, 15, 15);
		var pose = ctx.getPoseStack();
		var shaper = Minecraft.getInstance().getModelManager().getBlockModelShaper();
		Map<ModelKey, BakedModel> cache = new HashMap<>();
		for (BlockPos pos : BlockPos.betweenClosed(posSource, posTarget)) {
			if (!(region.getBlockEntity(pos) instanceof CombinedBlockEntity be)) continue;
			var setA = be.getA();
			var setB = be.getB();
			if (setA == null || setB == null) continue;
			pose.pushPose();
			pose.translate(pos.getX() - posSource.getX(), pos.getY() - posSource.getY(), pos.getZ() - posSource.getZ());
			BlockState state = be.getBlockState();
			ModelKey key = new ModelKey(state, setA, setB);
			BakedModel ans = cache.get(key);
			if (ans == null) {
				BakedModel model = shaper.getBlockModel(state);
				BakedModel modelA = shaper.getBlockModel(setA.base().value().defaultBlockState());
				BakedModel modelB = shaper.getBlockModel(setB.base().value().defaultBlockState());
				ans = CompositeModel.build(model, modelA, modelB, state, random);
				cache.put(key, ans);
			}
			LightPipelineAwareModelBlockRenderer.render(
					ctx.getOrCreateChunkBuffer(RenderType.solid()),
					ctx.getQuadLighter(true),
					region, ans, state, pos, pose, true, random, 42L,
					OverlayTexture.NO_OVERLAY, ModelData.EMPTY, RenderType.solid()
			);
			pose.popPose();
		}
	}

	private record ModelKey(BlockState state, IBlockSet a, IBlockSet b) {

	}

}
