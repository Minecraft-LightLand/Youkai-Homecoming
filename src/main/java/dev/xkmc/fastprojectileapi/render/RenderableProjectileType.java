package dev.xkmc.fastprojectileapi.render;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.fastprojectileapi.entity.SimplifiedProjectile;
import net.minecraft.client.renderer.MultiBufferSource;

public interface RenderableProjectileType<T extends RenderableProjectileType<T, I>, I> {

	void start(MultiBufferSource buffer, Iterable<I> list);

	void create(ProjectileRenderer r, SimplifiedProjectile e, PoseStack pose, float pTick);

}
