package dev.xkmc.youkaishomecoming.content.entity.animal.crab;

import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class CrabRenderer extends MobRenderer<CrabEntity, CrabModel> {

	public static final ResourceLocation NORMAL = YoukaisHomecoming.loc("textures/entities/crab/crab.png");
	public static final ResourceLocation MUD = YoukaisHomecoming.loc("textures/entities/crab/mud_crab.png");

	public CrabRenderer(EntityRendererProvider.Context ctx) {
		super(ctx, new CrabModel(ctx.bakeLayer(CrabModel.LAYER_LOCATION)), 0.5f);
	}

	@Override
	public ResourceLocation getTextureLocation(CrabEntity e) {
		return switch (e.prop.getVariant()) {
			case MUD -> MUD;
			default -> NORMAL;
		};
	}

}
