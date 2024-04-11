package dev.xkmc.youkaishomecoming.content.entity.danmaku;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.xkmc.danmaku.render.DanmakuRenderHelper;
import dev.xkmc.danmaku.render.SimpleDanmakuInstance;
import dev.xkmc.youkaishomecoming.content.item.danmaku.DanmakuItem;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
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
			PoseStack.Pose mat = pose.last();
			Matrix4f m4 = new Matrix4f(mat.pose());
			Matrix3f m3 = new Matrix3f(mat.normal());
			DanmakuRenderHelper.add(new SimpleDanmakuInstance(danmaku.getTypeForRender(), m3, m4));
			pose.popPose();
			super.render(e, yaw, pTick, pose, buffer, light);
		}
	}

	public ResourceLocation getTextureLocation(T pEntity) {
		return TextureAtlas.LOCATION_BLOCKS;
	}

}