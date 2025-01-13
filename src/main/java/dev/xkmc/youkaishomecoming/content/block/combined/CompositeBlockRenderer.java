package dev.xkmc.youkaishomecoming.content.block.combined;

import dev.xkmc.l2serial.util.Wrappers;
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

public class CompositeBlockRenderer {

	private static final RandomSource random = RandomSource.createNewThreadLocalInstance();

	public static void renderSection(BlockPos posSource, AddSectionGeometryEvent.SectionRenderingContext ctx) {
		BlockAndTintGetter region = ctx.getRegion();
		BlockPos posTarget = posSource.offset(15, 15, 15);
		var pose = ctx.getPoseStack();
		var shaper = Minecraft.getInstance().getModelManager().getBlockModelShaper();
		for (BlockPos pos : BlockPos.betweenClosed(posSource, posTarget)) {
			if (!(region.getBlockEntity(pos) instanceof CombinedBlockEntity be)) continue;
			var setA = be.getA();
			var setB = be.getB();
			if (setA == null || setB == null) continue;
			pose.pushPose();
			pose.translate(pos.getX() - posSource.getX(), pos.getY() - posSource.getY(), pos.getZ() - posSource.getZ());
			BlockState state = be.getBlockState();
			BakedModel model = shaper.getBlockModel(state);
			BakedModel modelA = shaper.getBlockModel(setA.base().value().defaultBlockState());
			BakedModel modelB = shaper.getBlockModel(setB.base().value().defaultBlockState());
			BakedModel ans = new CompositeModel(model, modelA, modelB);
			LightPipelineAwareModelBlockRenderer.render(
					ctx.getOrCreateChunkBuffer(RenderType.solid()),
					ctx.getQuadLighter(true),
					region, ans, state, pos, pose, false, random, 42L,
					OverlayTexture.NO_OVERLAY, ModelData.EMPTY, RenderType.solid()
			);
			pose.popPose();
		}
	}

}
