package dev.xkmc.youkaishomecoming.content.entity.rumia;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RumiaRenderer extends MobRenderer<RumiaEntity, RumiaModel<RumiaEntity>> {

	public static final ResourceLocation TEX = YoukaisHomecoming.loc("textures/entities/rumia.png");

	public RumiaRenderer(EntityRendererProvider.Context context) {
		super(context, new RumiaModel<>(context.bakeLayer(RumiaModel.LAYER_LOCATION)), 0.2F);
		addLayer(new BlackBallLayer<>(this, context.getModelSet()));
	}

	public ResourceLocation getTextureLocation(RumiaEntity entity) {
		return TEX;
	}

	protected void setupRotations(RumiaEntity rumia, PoseStack pose, float age, float yaw, float pTick) {
		if (rumia.isBlocked()) {
			pose.translate(0, 0.2, 0);
			pose.mulPose(Axis.XP.rotationDegrees(90));
			pose.translate(0, -0.85, 0);
		} else super.setupRotations(rumia, pose, age, yaw, pTick);
	}

	@Override
	public void render(RumiaEntity rumia, float yaw, float pTick, PoseStack pose, MultiBufferSource buffer, int light) {
		super.render(rumia, yaw, pTick, pose, buffer, rumia.isCharged() ? 0 : light);
	}
}
