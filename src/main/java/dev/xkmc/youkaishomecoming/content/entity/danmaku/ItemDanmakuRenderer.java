package dev.xkmc.youkaishomecoming.content.entity.danmaku;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.fastprojectileapi.entity.SimplifiedProjectile;
import dev.xkmc.fastprojectileapi.render.core.ProjectileRenderer;
import dev.xkmc.youkaishomecoming.content.item.danmaku.DanmakuItem;
import dev.xkmc.youkaishomecoming.init.data.YHModConfig;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Quaternionf;

@OnlyIn(Dist.CLIENT)
public class ItemDanmakuRenderer<T extends ItemDanmakuEntity> extends EntityRenderer<T> implements ProjectileRenderer<T> {

	public ItemDanmakuRenderer(EntityRendererProvider.Context pContext) {
		super(pContext);
	}

	protected int getBlockLightLevel(T e, BlockPos pPos) {
		return e.fullBright() ? 15 : super.getBlockLightLevel(e, pPos);
	}

	@Override
	public double fading(SimplifiedProjectile e) {
		if (entityRenderDispatcher.camera.getEntity() == e.getOwner()) {
			return YHModConfig.CLIENT.selfDanmakuFading.get();
		}
		double fading = YHModConfig.CLIENT.farDanmakuFading.get();
		if (fading == 0) return 0;
		double dist = entityRenderDispatcher.camera.getPosition().distanceTo(e.position());
		double start = YHModConfig.CLIENT.fadingStart.get();
		double end = YHModConfig.CLIENT.fadingEnd.get();
		if (dist < start) return 0;
		return Math.min((dist - start) / (end - start), 1) * fading;
	}

	public boolean shouldRender(T e, Frustum frustum, double camx, double camy, double camz) {
		Entity cam = this.entityRenderDispatcher.camera.getEntity();
		if (e.getOwner() != cam || e.tickCount >= 40) return true;
		double dh = e.getBbHeight() / 2;
		double dist = cam.getEyePosition().distanceToSqr(e.position().add(0, dh, 0));
		double dy = Math.abs(cam.getEyeY() - e.getY() - dh);
		return dist > 12 || dy > 0.1 + dh * 2 && dist > 2;
	}

	@Override
	public Quaternionf cameraOrientation() {
		return entityRenderDispatcher.cameraOrientation();
	}

	public void render(T e, float yaw, float pTick, PoseStack pose, MultiBufferSource buffer, int light) {
		render(e, pTick, pose);
	}

	@Override
	public Vec3 getRenderOffset(T e, float f) {
		return new Vec3(0, e.getBbHeight() / 2, 0);
	}

	@Override
	public void render(T e, float pTick, PoseStack pose) {
		if (!(e.getItem().getItem() instanceof DanmakuItem danmaku)) return;
		pose.pushPose();
		float scale = e.scale();
		pose.scale(scale, scale, scale);
		danmaku.getTypeForRender().create(this, e, pose, pTick);
		pose.popPose();
	}

	public ResourceLocation getTextureLocation(T pEntity) {
		return TextureAtlas.LOCATION_BLOCKS;
	}

}