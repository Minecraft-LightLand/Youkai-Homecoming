package dev.xkmc.danmaku.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.xkmc.danmaku.entity.SimplifiedProjectile;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public record DoubleLayerLaserType(ResourceLocation inner, ResourceLocation outer, int color)
		implements RenderableProjectileType<DoubleLayerLaserType, DoubleLayerLaserType.Ins> {

	@Override
	public void start(MultiBufferSource buffer, Iterable<Ins> list) {
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

	@Override
	public void create(ProjectileRenderer r, SimplifiedProjectile e, PoseStack pose, float pTick) {
		PoseStack.Pose mat = pose.last();
		Matrix4f m4 = new Matrix4f(mat.pose());
		Matrix3f m3 = new Matrix3f(mat.normal());
		ProjectileRenderHelper.add(this, new Ins(m3, m4));
	}

	public record Ins(Matrix3f m3, Matrix4f m4) {

		public void texInner(VertexConsumer vc, int color) {
			float v0 = -0.25f, v1 = 0.25f;
			renderPart(vc, color, 0, 1,
					v0, v0, v0, v1, v1, v0, v1, v1,
					0, 1, 0, 1);
		}

		public void texOuter(VertexConsumer vc, int color) {
			float v0 = -0.5f, v1 = 0.5f;
			renderPart(vc, color, 0, 1,
					v0, v0, v0, v1, v1, v0, v1, v1,
					0, 1, 0, 1);
		}

		private void renderPart(VertexConsumer vc, int color, int pMinY, int pMaxY, float pX0, float pZ0, float pX1, float pZ1, float pX2, float pZ2, float pX3, float pZ3, float pMinU, float pMaxU, float pMinV, float pMaxV) {
			renderQuad(vc, color, pMinY, pMaxY, pX0, pZ0, pX1, pZ1, pMinU, pMaxU, pMinV, pMaxV);
			renderQuad(vc, color, pMinY, pMaxY, pX3, pZ3, pX2, pZ2, pMinU, pMaxU, pMinV, pMaxV);
			renderQuad(vc, color, pMinY, pMaxY, pX1, pZ1, pX3, pZ3, pMinU, pMaxU, pMinV, pMaxV);
			renderQuad(vc, color, pMinY, pMaxY, pX2, pZ2, pX0, pZ0, pMinU, pMaxU, pMinV, pMaxV);
		}

		private void renderQuad(VertexConsumer pConsumer, int color, int pMinY, int pMaxY, float pMinX, float pMinZ, float pMaxX, float pMaxZ, float pMinU, float pMaxU, float pMinV, float pMaxV) {
			addVertex(pConsumer, color, pMaxY, pMinX, pMinZ, pMaxU, pMinV);
			addVertex(pConsumer, color, pMinY, pMinX, pMinZ, pMaxU, pMaxV);
			addVertex(pConsumer, color, pMinY, pMaxX, pMaxZ, pMinU, pMaxV);
			addVertex(pConsumer, color, pMaxY, pMaxX, pMaxZ, pMinU, pMinV);
		}

		private void addVertex(VertexConsumer vc, int color, int pY, float pX, float pZ, float pU, float pV) {
			vc.vertex(m4, pX, pY, pZ)
					.color(color)
					.uv(pU, pV)
					.overlayCoords(OverlayTexture.NO_OVERLAY)
					.uv2(15728880)
					.normal(m3, 0, 1, 0)
					.endVertex();
		}

	}
}
