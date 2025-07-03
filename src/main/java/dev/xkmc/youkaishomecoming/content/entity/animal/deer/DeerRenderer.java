package dev.xkmc.youkaishomecoming.content.entity.animal.deer;

import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class DeerRenderer extends MobRenderer<DeerEntity, DeerModel> {

	public static final ResourceLocation TEX = YoukaisHomecoming.loc("textures/entity/deer.png");

	public DeerRenderer(EntityRendererProvider.Context ctx) {
		super(ctx, new DeerModel(ctx.bakeLayer(DeerModel.LAYER_LOCATION)), 0.5f);
	}

	@Override
	protected @Nullable RenderType getRenderType(DeerEntity entity, boolean visible, boolean translucent, boolean outline) {
		return super.getRenderType(entity, visible, true, outline);
	}

	@Override
	public ResourceLocation getTextureLocation(DeerEntity e) {
		return TEX;
	}

}
