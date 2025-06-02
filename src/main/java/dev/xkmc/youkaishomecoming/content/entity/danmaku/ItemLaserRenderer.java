package dev.xkmc.youkaishomecoming.content.entity.danmaku;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.xkmc.fastprojectileapi.entity.SimplifiedProjectile;
import dev.xkmc.fastprojectileapi.render.core.ProjectileRenderer;
import dev.xkmc.youkaishomecoming.content.capability.GrazeHelper;
import dev.xkmc.youkaishomecoming.content.item.danmaku.LaserItem;
import dev.xkmc.youkaishomecoming.init.data.YHModConfig;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Quaternionf;

@OnlyIn(Dist.CLIENT)
public class ItemLaserRenderer<T extends ItemLaserEntity> extends EntityRenderer<T> implements ProjectileRenderer<T> {

	public ItemLaserRenderer(EntityRendererProvider.Context pContext) {
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
	public double fading(SimplifiedProjectile e) {
		if (entityRenderDispatcher.camera.getEntity() == e.getOwner()) {
			return YHModConfig.CLIENT.selfDanmakuFading.get();
		}
		return GrazeHelper.globalInvulTime > 0 ? YHModConfig.CLIENT.selfDanmakuFading.get() : 1;
	}

	@Override
	public Quaternionf cameraOrientation() {
		return entityRenderDispatcher.cameraOrientation();
	}

	@Override
	public Vec3 getRenderOffset(T e, float f) {
		return new Vec3(0, e.getBbHeight() / 2, 0);
	}

	public void render(T e, float yaw, float pTick, PoseStack pose, MultiBufferSource buffer, int light) {
		render(e, pTick, pose);
	}

	@Override
	public void render(T e, float pTick, PoseStack pose) {
		if (!(e.getItem().getItem() instanceof LaserItem danmaku)) return;
		if (e.tickCount < 2) return;
		pose.pushPose();
		float scale = e.scale() * e.percentOpen(pTick);
		pose.mulPose(Axis.YP.rotationDegrees(-e.getViewYRot(pTick)));
		pose.mulPose(Axis.XP.rotationDegrees(e.getViewXRot(pTick) + 90));
		pose.scale(e.getBbWidth() * scale, e.effectiveLength(pTick), e.getBbWidth() * scale);
		danmaku.getTypeForRender().create(this, e, pose, pTick);
		pose.popPose();
	}

	public ResourceLocation getTextureLocation(T pEntity) {
		return TextureAtlas.LOCATION_BLOCKS;
	}

}