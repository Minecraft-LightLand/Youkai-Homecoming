package dev.xkmc.fastprojectileapi.render.type;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.xkmc.fastprojectileapi.entity.SimplifiedProjectile;
import dev.xkmc.fastprojectileapi.render.core.BulkDataWriter;
import dev.xkmc.fastprojectileapi.render.core.DanmakuRenderStates;
import dev.xkmc.fastprojectileapi.render.core.DisplayType;
import dev.xkmc.fastprojectileapi.render.core.ProjectileRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;

import java.util.List;
import java.util.function.Consumer;

public record ButterflyProjectileType(ResourceLocation overlay, DisplayType display, int period)
		implements RenderableDanmakuType<ButterflyProjectileType, ButterflyProjectileType.Ins> {

	@Override
	public void start(MultiBufferSource buffer, List<Ins> list) {
		BulkDataWriter vc;
		vc = new BulkDataWriter(buffer.getBuffer(DanmakuRenderStates.danmaku(overlay, display())), list.size());
		for (var e : list) {
			e.tex(vc, -1);
		}
		vc.flush();
	}

	@Override
	public void create(Consumer<Ins> holder, ProjectileRenderer r, SimplifiedProjectile e, PoseStack pose, float pTick) {
		pose.mulPose(Axis.YP.rotationDegrees(-Mth.lerp(pTick, e.yRotO, e.getYRot())));
		pose.mulPose(Axis.XP.rotationDegrees(Mth.lerp(pTick, e.xRotO, e.getXRot())));
		float time = Math.abs((e.tickCount + pTick) / period % 1 * 4 - 2) - 1;
		float angle = 60f;
		{
			pose.pushPose();
			pose.mulPose(Axis.ZP.rotationDegrees(time * angle));
			PoseStack.Pose mat = pose.last();
			Matrix4f m4 = new Matrix4f(mat.pose());
			holder.accept(new Ins(m4, false));
			pose.popPose();
		}
		{
			pose.pushPose();
			pose.mulPose(Axis.ZP.rotationDegrees(time * -angle));
			PoseStack.Pose mat = pose.last();
			Matrix4f m4 = new Matrix4f(mat.pose());
			holder.accept(new Ins(m4, true));
			pose.popPose();
		}
	}

	public record Ins(Matrix4f m4, boolean right) {

		public void tex(BulkDataWriter vc, int color) {
			float x0 = 0;
			float x1 = .5f;
			if (right) {
				x0 += 0.5f;
				x1 += 0.5f;
			}
			vertex(vc, m4, x1, 1, x1, 0, color);
			vertex(vc, m4, x1, 0, x1, 1, color);
			vertex(vc, m4, x0, 0, x0, 1, color);
			vertex(vc, m4, x0, 1, x0, 0, color);
		}

		private static void vertex(BulkDataWriter vc, Matrix4f m4, float x, float y, float u, float v, int color) {
			vc.addVertex(m4, x - 0.5F, 0.0F, y - 0.5F, u, v, color);
		}

	}
}
