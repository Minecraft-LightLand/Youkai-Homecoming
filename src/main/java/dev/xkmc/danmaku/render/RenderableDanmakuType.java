package dev.xkmc.danmaku.render;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;

public interface RenderableDanmakuType {

	void start(MultiBufferSource buffer, Iterable<RenderableDanmakuInstance> list);

}
