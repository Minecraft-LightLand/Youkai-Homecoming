package dev.xkmc.youkaishomecoming.content.entity.rumia;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.xkmc.youkaishomecoming.content.entity.lampery.LampreyEntity;
import dev.xkmc.youkaishomecoming.content.entity.lampery.LampreyModel;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class RumiaRenderer extends MobRenderer<Rumia, RumiaModel<Rumia>> {

	public static final ResourceLocation TEX = new ResourceLocation(YoukaisHomecoming.MODID, "textures/entities/rumia.png");

	public RumiaRenderer(EntityRendererProvider.Context context) {
		super(context, new RumiaModel<>(context.bakeLayer(RumiaModel.LAYER_LOCATION)), 0.2F);
	}

	public ResourceLocation getTextureLocation(Rumia entity) {
		return TEX;
	}

	protected void setupRotations(Rumia pEntityLiving, PoseStack pPoseStack, float pAgeInTicks, float pRotationYaw, float pPartialTicks) {
		super.setupRotations(pEntityLiving, pPoseStack, pAgeInTicks, pRotationYaw, pPartialTicks);
	}

	@Override
	protected float getAttackAnim(Rumia pLivingBase, float pPartialTickTime) {
		return super.getAttackAnim(pLivingBase, pPartialTickTime);
	}

	@Override
	public void render(Rumia pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
		super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
	}
}
