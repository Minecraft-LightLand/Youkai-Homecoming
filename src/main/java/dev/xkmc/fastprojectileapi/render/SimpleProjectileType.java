package dev.xkmc.fastprojectileapi.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.xkmc.fastprojectileapi.entity.SimplifiedProjectile;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

import java.util.function.Consumer;

public record SimpleProjectileType(ResourceLocation tex, DisplayType display)
		implements RenderableDanmakuType<SimpleProjectileType, SimpleProjectileType.Ins> {

	@Override
	public void start(MultiBufferSource buffer, Iterable<Ins> list) {
		VertexConsumer vc;
		vc = buffer.getBuffer(DanmakuRenderStates.danmaku(tex, display));
		for (var e : list) {
			e.tex(vc, -1);
		}
	}

	@Override
	public void create(Consumer<Ins> holder, ProjectileRenderer r, SimplifiedProjectile e, PoseStack pose, float pTick) {
		var sim4 = new Matrix4f(pose.last().pose());
		sim4.set3x3(new Matrix4f().scale((float) Math.pow(sim4.determinant3x3(), 1 / 3d)));
		holder.accept(new Ins(sim4));
	}

	public record Ins(Matrix4f m4) {

		public void tex(VertexConsumer vc, int color) {
			vertex(vc, m4, 1, 1, 1, 0, color);
			vertex(vc, m4, 1, 0, 1, 1, color);
			vertex(vc, m4, 0, 0, 0, 1, color);
			vertex(vc, m4, 0, 1, 0, 0, color);
		}

		private static void vertex(VertexConsumer vc, Matrix4f m4, float x, int y, int u, int v, int color) {
			vc.vertex(m4, x - 0.5F, y - 0.5F, 0.0F).uv(u, v).color(color).endVertex();
		}

	}
}
