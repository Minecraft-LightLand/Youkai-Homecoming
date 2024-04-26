package dev.xkmc.danmaku.render;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.danmaku.entity.SimplifiedProjectile;
import net.minecraft.client.renderer.MultiBufferSource;

public interface RenderableDanmakuType<T extends RenderableDanmakuType<T, I>, I> {

	void start(MultiBufferSource buffer, Iterable<I> list);

	void create(DanmakuRenderer r, SimplifiedProjectile e, PoseStack pose, float pTick);

}
