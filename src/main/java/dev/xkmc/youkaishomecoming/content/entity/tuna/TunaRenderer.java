package dev.xkmc.youkaishomecoming.content.entity.tuna;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class TunaRenderer extends MobRenderer<TunaEntity, TunaModel<TunaEntity>> {

	public static final ResourceLocation TEX = YoukaisHomecoming.loc("textures/entities/tuna.png");

	public TunaRenderer(EntityRendererProvider.Context context) {
		super(context, new TunaModel<>(context.bakeLayer(TunaModel.LAYER_LOCATION)), 0.2F);
	}

	public ResourceLocation getTextureLocation(TunaEntity entity) {
		return TEX;
	}

	protected void setupRotations(TunaEntity e, PoseStack pos, float age, float yaw, float pTick) {
		if (e.isAggressive()) age *= 1.5f;
		super.setupRotations(e, pos, age, yaw, pTick);
		float f = 4.3F * Mth.sin(0.6F * age);
		pos.mulPose(Axis.YP.rotationDegrees(f));
		if (!e.isInWater()) {
			pos.translate(0.1F, 0.1F, -0.1F);
			pos.mulPose(Axis.ZP.rotationDegrees(90.0F));
		}

	}

}
