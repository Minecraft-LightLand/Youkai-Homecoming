package dev.xkmc.danmaku.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dev.xkmc.danmaku.entity.SimplifiedProjectile;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public record ButterflyProjectileType(ResourceLocation colored, ResourceLocation overlay, int period, int color)
		implements RenderableProjectileType<ButterflyProjectileType, ButterflyProjectileType.Ins> {

	@Override
	public void start(MultiBufferSource buffer, Iterable<Ins> list) {
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

	@Override
	public void create(ProjectileRenderer r, SimplifiedProjectile e, PoseStack pose, float pTick) {
		pose.mulPose(Axis.YP.rotationDegrees(-Mth.lerp(pTick, e.yRotO, e.getYRot())));
		pose.mulPose(Axis.XP.rotationDegrees(Mth.lerp(pTick, e.xRotO, e.getXRot())));
		float time = Math.abs((e.tickCount + pTick) / period % 1 * 4 - 2) - 1;
		float angle = 60f;
		{
			pose.pushPose();
			pose.mulPose(Axis.ZP.rotationDegrees(time * angle));
			PoseStack.Pose mat = pose.last();
			Matrix4f m4 = new Matrix4f(mat.pose());
			Matrix3f m3 = new Matrix3f(mat.normal());
			ProjectileRenderHelper.add(this, new Ins(m3, m4, false));
			pose.popPose();
		}
		{
			pose.pushPose();
			pose.mulPose(Axis.ZP.rotationDegrees(time * -angle));
			PoseStack.Pose mat = pose.last();
			Matrix4f m4 = new Matrix4f(mat.pose());
			Matrix3f m3 = new Matrix3f(mat.normal());
			ProjectileRenderHelper.add(this, new Ins(m3, m4, true));
			pose.popPose();
		}
	}

	public record Ins(Matrix3f m3, Matrix4f m4, boolean right) {

		public void tex(VertexConsumer vc, int color) {
			float x0 = 0;
			float x1 = .5f;
			if (right) {
				x0 += 0.5f;
				x1 += 0.5f;
			}
			vertex(vc, m4, m3, x0, 0, x0, 1, color);
			vertex(vc, m4, m3, x1, 0, x1, 1, color);
			vertex(vc, m4, m3, x1, 1, x1, 0, color);
			vertex(vc, m4, m3, x0, 1, x0, 0, color);
		}

		private static void vertex(VertexConsumer vc, Matrix4f m4, Matrix3f m3, float x, float y, float u, float v, int color) {
			vc.vertex(m4, x - 0.5F, 0.0F, y - 0.5F).color(color)
					.uv(u, v).overlayCoords(OverlayTexture.NO_OVERLAY)
					.uv2(LightTexture.FULL_BRIGHT).normal(m3, 0.0F, 1.0F, 0.0F).endVertex();
		}

	}
}
