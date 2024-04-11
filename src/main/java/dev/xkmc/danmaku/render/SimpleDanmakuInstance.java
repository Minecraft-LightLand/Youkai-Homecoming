package dev.xkmc.danmaku.render;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public record SimpleDanmakuInstance(RenderableDanmakuType key,
									Matrix3f m3, Matrix4f m4
) implements RenderableDanmakuInstance {

	public void tex(VertexConsumer vc, int color) {
		vertex(vc, m4, m3, 0, 0, 0, 1, color);
		vertex(vc, m4, m3, 1, 0, 1, 1, color);
		vertex(vc, m4, m3, 1, 1, 1, 0, color);
		vertex(vc, m4, m3, 0, 1, 0, 0, color);
	}

	private static void vertex(VertexConsumer vc, Matrix4f m4, Matrix3f m3, float x, int y, int u, int v, int color) {
		vc.vertex(m4, x - 0.5F, y - 0.5F, 0.0F).color(color)
				.uv((float) u, (float) v).overlayCoords(OverlayTexture.NO_OVERLAY)
				.uv2(LightTexture.FULL_BRIGHT).normal(m3, 0.0F, 1.0F, 0.0F).endVertex();
	}

}
