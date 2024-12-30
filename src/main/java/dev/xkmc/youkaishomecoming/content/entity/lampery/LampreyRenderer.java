package dev.xkmc.youkaishomecoming.content.entity.lampery;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class LampreyRenderer extends MobRenderer<LampreyEntity, LampreyModel<LampreyEntity>> {

	public static final ResourceLocation TEX = YoukaisHomecoming.loc("textures/entity/lamprey.png");

	public LampreyRenderer(EntityRendererProvider.Context context) {
		super(context, new LampreyModel<>(context.bakeLayer(LampreyModel.LAYER_LOCATION)), 0.2F);
	}

	public ResourceLocation getTextureLocation(LampreyEntity entity) {
		return TEX;
	}

	@Override
	protected void setupRotations(LampreyEntity entity, PoseStack poseStack, float bob, float yBodyRot, float partialTick, float scale) {
		super.setupRotations(entity, poseStack, bob, yBodyRot, partialTick, scale);
		float f = 4.3F * Mth.sin(0.6F * (entity.tickCount + partialTick));
		poseStack.mulPose(Axis.YP.rotationDegrees(f));
		if (!entity.isInWater()) {
			poseStack.translate(0.1F, 0.1F, -0.1F);
			poseStack.mulPose(Axis.ZP.rotationDegrees(90.0F));
		}

	}

}
