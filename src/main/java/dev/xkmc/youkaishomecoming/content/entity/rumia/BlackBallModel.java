package dev.xkmc.youkaishomecoming.content.entity.rumia;// Made with Blockbench 4.9.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class BlackBallModel<T extends RumiaEntity> extends HierarchicalModel<T> {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(YoukaisHomecoming.loc("black_ball"), "main");

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create()
						.texOffs(0, 68).addBox(-15.0F, -32.0F, -15.0F, 30.0F, 30.0F, 30.0F, new CubeDeformation(0.0F)),
				//.texOffs(0, 0).addBox(-17.0F, -34.0F, -17.0F, 34.0F, 34.0F, 34.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 24.0F, 0.0F));
		return LayerDefinition.create(meshdefinition, 256, 256);
	}

	private final ModelPart root;

	public BlackBallModel(ModelPart root) {
		this.root = root;
	}

	@Override
	public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack pose, VertexConsumer buffer, int light, int overlay,
							   float r, float g, float b, float a) {
		root.render(pose, buffer, light, overlay, r, g, b, a);
	}

	@Override
	public ModelPart root() {
		return root;
	}

}