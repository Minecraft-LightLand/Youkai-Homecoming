package dev.xkmc.youkaishomecoming.content.entity.danmaku;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.fastprojectileapi.render.core.ProjectileRenderer;
import dev.xkmc.youkaishomecoming.content.item.danmaku.DanmakuItem;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Quaternionf;

@OnlyIn(Dist.CLIENT)
public class ItemDanmakuRenderer<T extends ItemDanmakuEntity> extends EntityRenderer<T> implements ProjectileRenderer {

	public ItemDanmakuRenderer(EntityRendererProvider.Context pContext) {
		super(pContext);
	}

	protected int getBlockLightLevel(T e, BlockPos pPos) {
		return e.fullBright() ? 15 : super.getBlockLightLevel(e, pPos);
	}

	@Override
	public boolean shouldRender(T pLivingEntity, Frustum pCamera, double pCamX, double pCamY, double pCamZ) {
		return true;
	}

	@Override
	public Quaternionf cameraOrientation() {
		return entityRenderDispatcher.cameraOrientation();
	}

	public void render(T e, float yaw, float pTick, PoseStack pose, MultiBufferSource buffer, int light) {
		if (!(e.getItem().getItem() instanceof DanmakuItem danmaku)) return;
		Entity cam = this.entityRenderDispatcher.camera.getEntity();
		double dh = e.getBbHeight() / 2;
		double dist = cam.getEyePosition().distanceToSqr(e.position().add(0, dh, 0));
		double dy = Math.abs(cam.getEyeY() - e.getY() - dh);
		if (e.getOwner() != cam || dist > 12 || dy > 0.1 + dh * 2 && dist > 2 || e.tickCount >= 40) {
			pose.pushPose();
			float scale = e.scale();
			pose.translate(0, e.getBbHeight() / 2, 0);
			pose.scale(scale, scale, scale);
			danmaku.getTypeForRender().create(this, e, pose, pTick);
			pose.popPose();
		}
	}

	public ResourceLocation getTextureLocation(T pEntity) {
		return TextureAtlas.LOCATION_BLOCKS;
	}

}