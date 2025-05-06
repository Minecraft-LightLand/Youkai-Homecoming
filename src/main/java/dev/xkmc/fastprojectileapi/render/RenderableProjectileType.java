package dev.xkmc.fastprojectileapi.render;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.fastprojectileapi.entity.SimplifiedProjectile;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public interface RenderableProjectileType<T extends RenderableProjectileType<T, I>, I> extends Comparable<RenderableProjectileType<?, ?>> {

	void start(MultiBufferSource buffer, Iterable<I> list);

	void create(Consumer<I> holder, ProjectileRenderer r, SimplifiedProjectile e, PoseStack pose, float pTick);

	@Override
	default int compareTo(@NotNull RenderableProjectileType<?, ?> o) {
		if (this == o) return 0;
		int order = order() - o.order();
		if (order != 0) return order;
		return hashCode() - o.hashCode();
	}

	default int order() {
		return 0;
	}

}