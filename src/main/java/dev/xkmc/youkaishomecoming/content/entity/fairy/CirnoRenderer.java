package dev.xkmc.youkaishomecoming.content.entity.fairy;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.fastprojectileapi.spellcircle.SpellCircleLayer;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class CirnoRenderer extends MobRenderer<CirnoEntity, CirnoModel<CirnoEntity>> {

	public static final ResourceLocation TEX = YoukaisHomecoming.loc("textures/entities/cirno.png");

	public CirnoRenderer(EntityRendererProvider.Context context) {
		super(context, new CirnoModel<>(context.bakeLayer(CirnoModel.LAYER_LOCATION)), 0.2F);
		addLayer(new SpellCircleLayer<>(this));
	}

	public ResourceLocation getTextureLocation(CirnoEntity entity) {
		return TEX;
	}

	@Override
	public void render(CirnoEntity rumia, float yaw, float pTick, PoseStack pose, MultiBufferSource buffer, int light) {
		super.render(rumia, yaw, pTick, pose, buffer, light);
	}

}
