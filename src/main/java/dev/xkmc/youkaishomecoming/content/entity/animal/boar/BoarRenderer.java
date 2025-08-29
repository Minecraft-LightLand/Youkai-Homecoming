package dev.xkmc.youkaishomecoming.content.entity.animal.boar;

import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class BoarRenderer extends MobRenderer<BoarEntity, BoarModel> {

	public static final ResourceLocation NORMAL = YoukaisHomecoming.loc("textures/entities/boar/boar.png");

	public BoarRenderer(EntityRendererProvider.Context ctx) {
		super(ctx, new BoarModel(ctx.bakeLayer(BoarModel.LAYER_LOCATION)), 0.5f);
	}

	@Override
	public ResourceLocation getTextureLocation(BoarEntity e) {
		return NORMAL;
	}

}
