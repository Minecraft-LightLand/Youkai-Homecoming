package dev.xkmc.youkaishomecoming.content.entity.reimu;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.fastprojectileapi.spellcircle.SpellCircleLayer;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class ReimuRenderer extends MobRenderer<MaidenEntity, ReimuModel<MaidenEntity>> {

	public static final ResourceLocation TEX = YoukaisHomecoming.loc("textures/entities/reimu.png");
	public static final ResourceLocation BLUE = YoukaisHomecoming.loc("textures/entities/reimu_blue.png");

	public ReimuRenderer(EntityRendererProvider.Context context) {
		super(context, new ReimuModel<>(context.bakeLayer(ReimuModel.LAYER_LOCATION)), 0.2F);
		addLayer(new SpellCircleLayer<>(this));
	}

	public ResourceLocation getTextureLocation(MaidenEntity entity) {
		if (entity.isChaotic())
			return entity.tickCount / 2 % 2 == 0 ? TEX : BLUE;
		if (entity.getFlag(4))
			return BLUE;
		return TEX;
	}

	@Override
	public void render(MaidenEntity rumia, float yaw, float pTick, PoseStack pose, MultiBufferSource buffer, int light) {
		super.render(rumia, yaw, pTick, pose, buffer, light);
	}

}
