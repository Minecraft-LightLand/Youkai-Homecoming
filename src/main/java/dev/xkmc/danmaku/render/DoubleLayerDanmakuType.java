package dev.xkmc.danmaku.render;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public record DoubleLayerDanmakuType(ResourceLocation colored, ResourceLocation overlay, int color)
		implements RenderableDanmakuType {

	@Override
	public void start(MultiBufferSource buffer, Iterable<RenderableDanmakuInstance> list) {
		VertexConsumer vc;
		vc = buffer.getBuffer(RenderType.entityCutoutNoCull(colored));
		for (var e : list) {
			e.tex(vc, color);
		}
		vc = buffer.getBuffer(RenderType.entityCutoutNoCull(overlay));
		for (var e : list) {
			e.tex(vc, -1);
		}
	}

}
