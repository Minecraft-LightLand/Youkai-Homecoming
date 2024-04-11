package dev.xkmc.danmaku.render;

import com.mojang.blaze3d.vertex.VertexConsumer;

public interface RenderableDanmakuInstance {

	RenderableDanmakuType key();

	void tex(VertexConsumer vc, int color);

}
