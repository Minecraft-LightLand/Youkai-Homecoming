package dev.xkmc.youkaishomecoming.content.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.xkmc.youkaishomecoming.content.capability.FrogGodCapability;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.client.model.FrogModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.frog.Frog;

import java.util.List;

public class FrogHatLayer<T extends Frog> extends RenderLayer<T, FrogModel<T>> {

	private static final ResourceLocation TEX = YoukaisHomecoming.loc("textures/models/straw_hat.png");
	private static final List<String> PART = List.of("body", "head", "hat");

	private final ModelPart model;

	public FrogHatLayer(RenderLayerParent<T, FrogModel<T>> pRenderer, EntityModelSet pModelSet) {
		super(pRenderer);
		this.model = pModelSet.bakeLayer(FrogStrawHatModel.LAYER_LOCATION);
	}

	@Override
	public void render(PoseStack pose, MultiBufferSource buffer, int light, T e, float swing, float swingAmp, float pTick, float age, float headYaw, float headPitch) {
		if (!FrogGodCapability.hasHat(e)) return;
		VertexConsumer vertexconsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(TEX));
		pose.pushPose();
		var part = model.getChild("root");
		var ref = getParentModel().root();
		for (var str : PART) {
			part.copyFrom(ref);
			part.translateAndRotate(pose);
			part = part.getChild(str);
			if (ref.hasChild(str))
				ref = ref.getChild(str);
		}
		part.render(pose, vertexconsumer, light,
				LivingEntityRenderer.getOverlayCoords(e, 0.0F),
				1, 1, 1, 1);
		pose.popPose();
	}

}
