package dev.xkmc.youkaishomecoming.content.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class LampreyRenderer extends MobRenderer<LampreyEntity, LampreyModel<LampreyEntity>> {

	public static final ResourceLocation TEX = new ResourceLocation(YoukaisHomecoming.MODID, "textures/entities/lamprey.png");

	public LampreyRenderer(EntityRendererProvider.Context context) {
		super(context, new LampreyModel<>(context.bakeLayer(LampreyModel.LAYER_LOCATION)), 0.2F);
	}

	public ResourceLocation getTextureLocation(LampreyEntity entity) {
		return TEX;
	}

	protected void setupRotations(LampreyEntity pEntityLiving, PoseStack pPoseStack, float pAgeInTicks, float pRotationYaw, float pPartialTicks) {
		super.setupRotations(pEntityLiving, pPoseStack, pAgeInTicks, pRotationYaw, pPartialTicks);
		float f = 4.3F * Mth.sin(0.6F * pAgeInTicks);
		pPoseStack.mulPose(Axis.YP.rotationDegrees(f));
		if (!pEntityLiving.isInWater()) {
			pPoseStack.translate(0.1F, 0.1F, -0.1F);
			pPoseStack.mulPose(Axis.ZP.rotationDegrees(90.0F));
		}

	}

}
