package dev.xkmc.youkaishomecoming.content.entity.animal.tuna;// Made with Blockbench 4.12.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class TunaModel<T extends Entity> extends HierarchicalModel<T> {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(YoukaisHomecoming.loc("tuna"), "main");

	public final ModelPart root;
	public final ModelPart back;

	public TunaModel(ModelPart root) {
		this.root = root;
		this.back = root.getChild("body_back");

		//this.body = root.getChild("body");
		//this.fin_left = this.body.getChild("fin_left");
		//this.fin_right = this.body.getChild("fin_right");
		//this.head = this.body.getChild("head");
		//this.mouth = this.head.getChild("mouth");
		//this.caudal_fin = this.body_back.getChild("caudal_fin");
	}

	@Override
	public ModelPart root() {
		return root;
	}

	public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
		float f = 1.0F;
		if (!pEntity.isInWater()) {
			f = 1.5F;
		}
		this.back.yRot = -f * 0.45F * Mth.sin(0.6F * pAgeInTicks);
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(37, 79).addBox(-7.0F, -9.5F, -30.0F, 14.0F, 19.0F, 30.0F, new CubeDeformation(0.0F))
				.texOffs(29, 80).addBox(0.0F, -19.5F, -24.0F, 0.0F, 10.0F, 19.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 14.5F, 6.0F));

		PartDefinition fin_down_right_r1 = body.addOrReplaceChild("fin_down_right_r1", CubeListBuilder.create().texOffs(108, 91).addBox(0.0F, 0.0F, -5.0F, 0.0F, 8.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 9.5F, -18.0F, 0.0F, 0.0F, 0.4363F));

		PartDefinition fin_down_left_r1 = body.addOrReplaceChild("fin_down_left_r1", CubeListBuilder.create().texOffs(108, 91).mirror().addBox(0.0F, 0.0F, -5.0F, 0.0F, 8.0F, 10.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(2.0F, 9.5F, -18.0F, 0.0F, 0.0F, -0.4363F));

		PartDefinition fin_left = body.addOrReplaceChild("fin_left", CubeListBuilder.create().texOffs(28, 15).addBox(0.01F, -2.0F, 0.0F, 0.0F, 7.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(7.0F, 0.5F, -24.0F));

		PartDefinition fin_right = body.addOrReplaceChild("fin_right", CubeListBuilder.create().texOffs(28, 8).addBox(0.0F, -2.0F, 0.0F, 0.0F, 7.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(-7.01F, 0.5F, -24.0F));

		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(28, 0).addBox(-5.0F, -7.0F, -8.0F, 10.0F, 14.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.5F, -30.0F));

		PartDefinition mouth = head.addOrReplaceChild("mouth", CubeListBuilder.create(), PartPose.offset(0.0F, 7.0F, -8.0F));

		PartDefinition mouth_r1 = mouth.addOrReplaceChild("mouth_r1", CubeListBuilder.create().texOffs(0, 54).addBox(-5.0F, -9.0F, -2.0F, 10.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2182F, 0.0F, 0.0F));

		PartDefinition body_back = partdefinition.addOrReplaceChild("body_back", CubeListBuilder.create().texOffs(84, -19).addBox(0.0F, -27.0F, -2.0F, 0.0F, 20.0F, 19.0F, new CubeDeformation(0.0F))
				.texOffs(68, 26).addBox(-5.0F, -7.0F, 0.0F, 10.0F, 14.0F, 17.0F, new CubeDeformation(0.0F))
				.texOffs(23, 60).addBox(0.0F, 7.0F, -2.0F, 0.0F, 20.0F, 19.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 15.0F, 6.0F));

		PartDefinition caudal_fin = body_back.addOrReplaceChild("caudal_fin", CubeListBuilder.create().texOffs(0, -11).addBox(0.0F, -21.0F, -0.5F, 0.0F, 42.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 17.5F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}
}