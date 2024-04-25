package dev.xkmc.danmaku.render;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public record DoubleLayerLaserType(ResourceLocation inner, ResourceLocation outer, int color)
		implements RenderableDanmakuType<DoubleLayerLaserType, DoubleLayerLaserInstance> {

	@Override
	public void start(MultiBufferSource buffer, Iterable<DoubleLayerLaserInstance> list) {
		VertexConsumer vc;
		vc = buffer.getBuffer(RenderType.beaconBeam(inner, false));
		for (var e : list) {
			e.texInner(vc, -1);
		}
		vc = buffer.getBuffer(RenderType.beaconBeam(outer, true));
		for (var e : list) {
			e.texOuter(vc, color);
		}
	}

}
