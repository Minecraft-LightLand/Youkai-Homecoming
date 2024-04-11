package dev.xkmc.youkaishomecoming.content.entity.danmaku;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dev.xkmc.youkaishomecoming.content.item.danmaku.DanmakuItem;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

@OnlyIn(Dist.CLIENT)
public class ItemDanmakuRenderer<T extends ItemDanmakuEntity> extends EntityRenderer<T> {

	private static final float MIN_CAMERA_DISTANCE_SQUARED = 12.25F;

	public ItemDanmakuRenderer(EntityRendererProvider.Context pContext) {
		super(pContext);
	}

	protected int getBlockLightLevel(T e, BlockPos pPos) {
		return e.fullBright() ? 15 : super.getBlockLightLevel(e, pPos);
	}

	public void render(T e, float yaw, float pTick, PoseStack pose, MultiBufferSource buffer, int light) {
		if (!(e.getItem().getItem() instanceof DanmakuItem danmaku)) return;
		if (e.tickCount >= 2 || !(this.entityRenderDispatcher.camera.getEntity().distanceToSqr(e) < MIN_CAMERA_DISTANCE_SQUARED)) {
			pose.pushPose();
			float scale = e.scale();
			pose.translate(0, e.getBbHeight() / 2, 0);
			pose.scale(scale, scale, scale);
			pose.mulPose(this.entityRenderDispatcher.cameraOrientation());
			pose.mulPose(Axis.YP.rotationDegrees(180.0F));
			tex(YoukaisHomecoming.loc("textures/item/danmaku/" + danmaku.type.name + ".png"),
					pose, buffer, light, 0xff000000 | danmaku.color.getFireworkColor());
			tex(YoukaisHomecoming.loc("textures/item/danmaku/" + danmaku.type.name + "_overlay.png"),
					pose, buffer, light, -1);
			pose.popPose();
			super.render(e, yaw, pTick, pose, buffer, light);
		}
	}

	private void tex(ResourceLocation rl, PoseStack pose, MultiBufferSource buffer, int light, int color) {
		VertexConsumer vc = buffer.getBuffer(RenderType.entityCutoutNoCull(rl));
		PoseStack.Pose mat = pose.last();
		Matrix4f m4 = mat.pose();
		Matrix3f m3 = mat.normal();
		vertex(vc, m4, m3, light, 0, 0, 0, 1, color);
		vertex(vc, m4, m3, light, 1, 0, 1, 1, color);
		vertex(vc, m4, m3, light, 1, 1, 1, 0, color);
		vertex(vc, m4, m3, light, 0, 1, 0, 0, color);
	}

	private static void vertex(VertexConsumer vc, Matrix4f m4, Matrix3f m3, int light, float x, int y, int u, int v, int color) {
		vc.vertex(m4, x - 0.5F, y - 0.5F, 0.0F).color(color)
				.uv((float) u, (float) v).overlayCoords(OverlayTexture.NO_OVERLAY)
				.uv2(light).normal(m3, 0.0F, 1.0F, 0.0F).endVertex();
	}


	public ResourceLocation getTextureLocation(T pEntity) {
		return TextureAtlas.LOCATION_BLOCKS;
	}

}