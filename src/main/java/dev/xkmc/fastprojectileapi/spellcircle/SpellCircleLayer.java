package dev.xkmc.fastprojectileapi.spellcircle;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class SpellCircleLayer<T extends Entity, M extends EntityModel<T>> extends RenderLayer<T, M> {

	private static final ResourceLocation SPELL = YoukaisHomecoming.loc("textures/entities/spell_circle.png");

	public SpellCircleLayer(RenderLayerParent<T, M> pRenderer) {
		super(pRenderer);
	}

	@Override
	public void render(PoseStack pose, MultiBufferSource buffer, int light, T entity,
					   float swing, float swingAmp, float pTick, float age,
					   float yaw, float pitch) {
		if (!(entity instanceof SpellCircleHolder e)) return;
		ResourceLocation rl = e.getSpellCircle();
		if (rl == null) return;
		SpellComponent component = SpellCircleConfig.getFromConfig(rl);
		if (component == null) return;
		SpellComponent.RenderHandle handle = new SpellComponent.RenderHandle(pose,
				buffer.getBuffer(SpellRenderState.getSpell(SPELL)),
				entity.tickCount + pTick, light);
		pose.pushPose();
		pose.translate(0, entity.getBbHeight() / 2, entity.getBbWidth());
		float scale = e.getCircleSize(pTick);
		pose.scale(scale / 16f, scale / 16f, scale / 16f);
		component.render(handle);
		pose.popPose();
	}

}
