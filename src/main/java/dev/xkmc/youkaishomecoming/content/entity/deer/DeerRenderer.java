package dev.xkmc.youkaishomecoming.content.entity.deer;

import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class DeerRenderer extends MobRenderer<DeerEntity, DeerModel> {

	public static final ResourceLocation NORMAL = YoukaisHomecoming.loc("textures/entities/deer/deer.png");
	public static final ResourceLocation FALLOW = YoukaisHomecoming.loc("textures/entities/deer/fallow_deer.png");
	public static final ResourceLocation SAKURA = YoukaisHomecoming.loc("textures/entities/deer/sakura_deer.png");
	public static final ResourceLocation WHITELIPPED = YoukaisHomecoming.loc("textures/entities/deer/whitelipped_deer.png");

	public DeerRenderer(EntityRendererProvider.Context ctx) {
		super(ctx, new DeerModel(ctx.bakeLayer(DeerModel.LAYER_LOCATION)), 0.5f);
	}

	@Override
	protected @Nullable RenderType getRenderType(DeerEntity entity, boolean visible, boolean translucent, boolean outline) {
		return super.getRenderType(entity, visible, translucent, outline);
	}

	@Override
	public ResourceLocation getTextureLocation(DeerEntity e) {
		return switch (e.prop.getVariant()) {
			case FALLOW -> FALLOW;
			case SAKURA -> SAKURA;
			case WHITELIPPED -> WHITELIPPED;
			default -> NORMAL;
		};
	}

}
