package dev.xkmc.fastprojectileapi.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.xkmc.fastprojectileapi.entity.SimplifiedProjectile;
import dev.xkmc.youkaishomecoming.init.data.YHModConfig;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.RenderLevelStageEvent;

import java.util.function.Consumer;

public record DoubleLayerLaserType(ResourceLocation inner, ResourceLocation outer, int color)
		implements RenderableProjectileType<DoubleLayerLaserType, DoubleLayerLaserType.Ins> {

	@Override
	public int order() {
		return 10;
	}

	@Override
	public void start(MultiBufferSource buffer, Iterable<Ins> list) {
		boolean additive = YHModConfig.CLIENT.laserRenderAdditive.get();
		boolean invert = YHModConfig.CLIENT.laserRenderInverted.get();
		double tran = YHModConfig.CLIENT.laserTransparency.get();
		int col = (color & 0xffffff) | (((int) (tran * 255.9)) << 24);
		int add = (int) ((color & 0xff) * tran) |
				(int) ((color >> 8 & 0xff) * tran) << 8 |
				(int) ((color >> 16 & 0xff) * tran) << 16 | 0xff000000;
		VertexConsumer vc;
		vc = buffer.getBuffer(DanmakuRenderStates.laser(inner, DisplayType.SOLID));
		for (var e : list) {
			e.texInner(vc, -1);
		}
		if (additive) {
			vc = buffer.getBuffer(DanmakuRenderStates.laser(outer, DisplayType.ADDITIVE));
			for (var e : list) {
				e.texOuter(false, vc, add);
			}
		}
		if (invert) {
			vc = buffer.getBuffer(DanmakuRenderStates.laser(outer, DisplayType.TRANSPARENT));
			for (var e : list) {
				e.texOuter(true, vc, col);
			}
		}
		if (!additive && !invert) {
			vc = buffer.getBuffer(DanmakuRenderStates.laser(outer, DisplayType.TRANSPARENT));
			for (var e : list) {
				e.texOuter(false, vc, col);
			}
		}
	}

	@Override
	public void create(Consumer<Ins> holder, ProjectileRenderer r, SimplifiedProjectile e, PoseStack pose, float pTick) {
		PoseStack.Pose mat = pose.last();
		holder.accept(new Ins(mat));
	}

	public record Ins(PoseStack.Pose pose) {

		public void texInner(VertexConsumer vc, int color) {
			float v0 = -0.167f, v1 = 0.167f;
			renderPart(false, vc, color, 0, 1,
					v0, v0, v0, v1, v1, v0, v1, v1,
					0, 1, 0, 1);
		}

		public void texOuter(boolean invert, VertexConsumer vc, int color) {
			float v0 = -0.5f, v1 = 0.5f;
			renderPart(invert, vc, color, 0, 1,
					v0, v0, v0, v1, v1, v0, v1, v1,
					0, 1, 0, 1);
		}

		private void renderPart(boolean invert, VertexConsumer vc, int color, int pMinY, int pMaxY, float pX0, float pZ0, float pX1, float pZ1, float pX2, float pZ2, float pX3, float pZ3, float pMinU, float pMaxU, float pMinV, float pMaxV) {
			renderQuad(invert, vc, color, pMinY, pMaxY, pX0, pZ0, pX1, pZ1, pMinU, pMaxU, pMinV, pMaxV);
			renderQuad(invert, vc, color, pMinY, pMaxY, pX3, pZ3, pX2, pZ2, pMinU, pMaxU, pMinV, pMaxV);
			renderQuad(invert, vc, color, pMinY, pMaxY, pX1, pZ1, pX3, pZ3, pMinU, pMaxU, pMinV, pMaxV);
			renderQuad(invert, vc, color, pMinY, pMaxY, pX2, pZ2, pX0, pZ0, pMinU, pMaxU, pMinV, pMaxV);
		}

		private void renderQuad(boolean invert, VertexConsumer pConsumer, int color, int pMinY, int pMaxY, float pMinX, float pMinZ, float pMaxX, float pMaxZ, float pMinU, float pMaxU, float pMinV, float pMaxV) {
			if (invert) {
				addVertex(pConsumer, color, pMaxY, pMaxX, pMaxZ, pMinU, pMinV);
				addVertex(pConsumer, color, pMinY, pMaxX, pMaxZ, pMinU, pMaxV);
				addVertex(pConsumer, color, pMinY, pMinX, pMinZ, pMaxU, pMaxV);
				addVertex(pConsumer, color, pMaxY, pMinX, pMinZ, pMaxU, pMinV);
			} else {
				addVertex(pConsumer, color, pMaxY, pMinX, pMinZ, pMaxU, pMinV);
				addVertex(pConsumer, color, pMinY, pMinX, pMinZ, pMaxU, pMaxV);
				addVertex(pConsumer, color, pMinY, pMaxX, pMaxZ, pMinU, pMaxV);
				addVertex(pConsumer, color, pMaxY, pMaxX, pMaxZ, pMinU, pMinV);

			}
		}

		private void addVertex(VertexConsumer vc, int color, int pY, float pX, float pZ, float pU, float pV) {
			vc.vertex(pose.pose(), pX, pY, pZ).uv(pU, pV).color(color).endVertex();
		}

	}
}
