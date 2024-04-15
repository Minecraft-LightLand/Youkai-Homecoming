package dev.xkmc.danmaku.render;

import net.minecraft.client.renderer.MultiBufferSource;

public interface RenderableDanmakuType<
		T extends RenderableDanmakuType<T, I>,
		I extends RenderableDanmakuInstance<T>> {

	void start(MultiBufferSource buffer, Iterable<I> list);

}
