package dev.xkmc.youkaishomecoming.content.spell.shooter;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.fastprojectileapi.spellcircle.SpellCircleLayer;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class ShooterRenderer<T extends ShooterEntity> extends EntityRenderer<T> {

	public static final ResourceLocation TEX = YoukaisHomecoming.loc("textures/entities/rumia.png");

	public ShooterRenderer(EntityRendererProvider.Context ctx) {
		super(ctx);
	}

	@Override
	public void render(T e, float yaw, float pTick, PoseStack pose, MultiBufferSource buffer, int light) {
		SpellCircleLayer.renderImpl(pose, buffer, light, e, pTick);
		super.render(e, yaw, pTick, pose, buffer, light);
	}

	@Override
	public ResourceLocation getTextureLocation(T pEntity) {
		return TEX;
	}

}
