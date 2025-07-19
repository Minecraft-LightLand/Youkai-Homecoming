package dev.xkmc.youkaishomecoming.content.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.xkmc.youkaishomecoming.compat.curios.CuriosManager;
import dev.xkmc.youkaishomecoming.content.entity.fairy.CirnoModel;
import dev.xkmc.youkaishomecoming.content.entity.fairy.CirnoRenderer;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class CamelliaHeadLayer<T extends LivingEntity, M extends HumanoidModel<T>> extends RenderLayer<T, M> {

	public static final ResourceLocation TEX = YoukaisHomecoming.loc("textures/entities/camellia.png");

	private final HumanoidModel<T> model;

	public CamelliaHeadLayer(RenderLayerParent<T, M> pRenderer, EntityModelSet models) {
		super(pRenderer);
		ModelPart root = models.bakeLayer(CamelliaHeadDeco.LAYER_LOCATION);
		root.getAllParts().forEach(e -> e.skipDraw = true);
		var head = root.getChild("head");
		head.getAllParts().forEach(e -> e.skipDraw = false);
		head.skipDraw = true;
		model = new HumanoidModel<>(root);
	}

	@Override
	public void render(PoseStack pose, MultiBufferSource buffer, int light, T e, float swing, float swingAmp, float pTick, float age, float yaw, float pitch) {
		if (!CuriosManager.hasHead(e, YHItems.CAMELLIA.get(), true)) return;

		pose.pushPose();
		pose.scale(2, 2, 2);
		pose.translate(0, -0.75, 0);

		Minecraft minecraft = Minecraft.getInstance();
		boolean flag = minecraft.shouldEntityAppearGlowing(e) && e.isInvisible();
		if (!e.isInvisible() || flag) {
			VertexConsumer vertexconsumer;
			if (flag) {
				vertexconsumer = buffer.getBuffer(RenderType.outline(this.getTextureLocation(e)));
			} else {
				vertexconsumer = buffer.getBuffer(RenderType.entityTranslucent(this.getTextureLocation(e)));
			}
			this.model.prepareMobModel(e, swing, swingAmp, pTick);
			this.model.setupAnim(e, swing, swingAmp, age, yaw, pitch);
			this.model.renderToBuffer(pose, vertexconsumer, light,
					LivingEntityRenderer.getOverlayCoords(e, 0.0F),
					1.0F, 1.0F, 1.0F, 1.0F);
		}

		pose.popPose();
	}

	@Override
	protected ResourceLocation getTextureLocation(T pEntity) {
		return TEX;
	}

}
