package dev.xkmc.youkaishomecoming.content.entity.danmaku;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ItemDanmakuRenderer<T extends ItemDanmakuEntity> extends EntityRenderer<T> {

	private static final float MIN_CAMERA_DISTANCE_SQUARED = 12.25F;

	private final ItemRenderer itemRenderer;

	public ItemDanmakuRenderer(EntityRendererProvider.Context pContext) {
		super(pContext);
		this.itemRenderer = pContext.getItemRenderer();
	}

	protected int getBlockLightLevel(T e, BlockPos pPos) {
		return e.fullBright() ? 15 : super.getBlockLightLevel(e, pPos);
	}

	public void render(T e, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
		if (e.tickCount >= 2 || !(this.entityRenderDispatcher.camera.getEntity().distanceToSqr(e) < MIN_CAMERA_DISTANCE_SQUARED)) {
			pPoseStack.pushPose();
			float scale = e.scale();
			pPoseStack.scale(scale, scale, scale);
			pPoseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
			pPoseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
			this.itemRenderer.renderStatic(e.getItem(), ItemDisplayContext.GROUND, pPackedLight, OverlayTexture.NO_OVERLAY, pPoseStack, pBuffer, e.level(), e.getId());
			pPoseStack.popPose();
			super.render(e, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
		}
	}

	public ResourceLocation getTextureLocation(T pEntity) {
		return TextureAtlas.LOCATION_BLOCKS;
	}

}