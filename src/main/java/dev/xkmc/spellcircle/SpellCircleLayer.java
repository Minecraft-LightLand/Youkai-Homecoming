package dev.xkmc.spellcircle;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class SpellCircleLayer<T extends Entity, M extends EntityModel<T>> extends RenderLayer<T, M> {

	private static final ResourceLocation WHITE = YoukaisHomecoming.loc("textures/entities/laser_inner.png");

	public SpellCircleLayer(RenderLayerParent<T, M> pRenderer) {
		super(pRenderer);
	}

	@Override
	public void render(PoseStack pose, MultiBufferSource buffer, int light, T e,
					   float swing, float swingAmp, float pTick, float age,
					   float yaw, float pitch) {
		SpellComponent component = SpellCircleConfig.getFromConfig(YoukaisHomecoming.loc("test_spell"));
		if (component == null) return;
		SpellComponent.RenderHandle handle = new SpellComponent.RenderHandle(pose,
				buffer.getBuffer(SpellRenderState.getSpell(WHITE)),
				e.tickCount + pTick, light);
		pose.pushPose();
		pose.translate(0, e.getBbHeight() / 2, e.getBbWidth());
		//pose.mulPose(Axis.YP.rotationDegrees(-e.getViewYRot(pTick)));
		//pose.mulPose(Axis.XP.rotationDegrees(e.getViewXRot(pTick)));
		float scale = 1;//e.getSize(partial);
		pose.scale(scale / 16f, scale / 16f, scale / 16f);
		component.render(handle);
		pose.popPose();
	}

}
