package dev.xkmc.youkaishomecoming.content.entity.fairy;

import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class CirnoModel<T extends CirnoEntity> extends HierarchicalModel<T> {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(YoukaisHomecoming.loc("cirno"), "main");
	public static final ModelLayerLocation HAT_LOCATION = new ModelLayerLocation(YoukaisHomecoming.loc("cirno_hat"), "main");
	public static final ModelLayerLocation WINGS_LOCATION = new ModelLayerLocation(YoukaisHomecoming.loc("cirno_wings"), "main");

	private final ModelPart head;
	private final ModelPart root;
	private final ModelPart rightArm;
	private final ModelPart leftArm;
	private final ModelPart rightLeg;
	private final ModelPart leftLeg;

	public CirnoModel(ModelPart root) {
		this.head = root.getChild("head");
		this.root = root;
		this.rightArm = root.getChild("rightArm");
		this.leftArm = root.getChild("leftArm");
		this.rightLeg = root.getChild("rightLeg");
		this.leftLeg = root.getChild("leftLeg");
	}


	@Override
	public ModelPart root() {
		return root;
	}

	public void setupAnim(T e, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
		head.yRot = pNetHeadYaw * ((float) Math.PI / 180F);
		head.xRot = pHeadPitch * ((float) Math.PI / 180F);

		float f = 1;
		float legMaxSwing = 0.283f;

		if (e.isAggressive()) {
			leftArm.zRot = (float) -Math.PI / 2;
			rightArm.zRot = (float) Math.PI / 2;
			leftArm.xRot = 0;
			rightArm.xRot = 0;
		} else {
			leftArm.zRot = 0;
			rightArm.zRot = 0;
			rightArm.xRot = Mth.cos(pLimbSwing * 0.6662F + (float) Math.PI) * 2.0F * pLimbSwingAmount * 0.5F / f;
			leftArm.xRot = Mth.cos(pLimbSwing * 0.6662F) * 2.0F * pLimbSwingAmount * 0.5F / f;
		}

		float legSwing = !e.onGround() ? 0 : Math.min(legMaxSwing, pLimbSwingAmount);
		leftLeg.xRot = Mth.cos(pLimbSwing * 0.6662F + (float) Math.PI) * 1.4F * legSwing / f;
		rightLeg.xRot = Mth.cos(pLimbSwing * 0.6662F) * 1.4F * legSwing / f;

	}

	public static LayerDefinition createHatLayer() {
		MeshDefinition meshdefinition = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
		PartDefinition head = meshdefinition.getRoot().getChild("head");

		PartDefinition r_r = head.addOrReplaceChild("r_r", CubeListBuilder.create().texOffs(71, 71)
						.addBox(-5.0F, -2.0F, 0.0F, 6.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offset(-1.75F, -7.5F, 3.0F));

		PartDefinition cube_r1 = r_r.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(40, 12).addBox(-9.0F, -2.0F, 0.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(59, 38).addBox(-8.0F, -2.0F, 0.0F, 7.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0622F, -0.7679F, 0.0F, -0.0666F, 0.2079F, -0.836F));

		PartDefinition cube_r2 = r_r.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(70, 46).addBox(-6.0F, -3.0F, 0.0F, 6.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.6962F, 0.5981F, 0.0F, -0.0087F, 0.0151F, -0.5237F));

		PartDefinition cube_r3 = r_r.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(72, 8).addBox(-6.0F, -3.0F, 0.0F, 6.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.1962F, 1.0F, 0.0F, -0.0087F, -0.0151F, 0.5237F));

		PartDefinition l_r = head.addOrReplaceChild("l_r", CubeListBuilder.create().texOffs(58, 68)
				.addBox(-1.0F, -2.0F, 0.0F, 6.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offset(1.75F, -7.5F, 3.0F));

		PartDefinition cube_r4 = l_r.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 30).addBox(8.0F, -2.0F, 0.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(58, 26).addBox(1.0F, -2.0F, 0.0F, 7.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0622F, -0.7679F, 0.0F, -0.0666F, -0.2079F, 0.836F));

		PartDefinition cube_r5 = l_r.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 70).addBox(-1.0F, -3.0F, 0.0F, 6.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.8301F, 1.0981F, 0.0F, -0.0087F, -0.0151F, 0.5237F));

		PartDefinition cube_r6 = l_r.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(30, 70).addBox(0.0F, -3.0F, 0.0F, 6.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1962F, 1.0F, 0.0F, -0.0087F, 0.0151F, -0.5237F));

		head.addOrReplaceChild("tie", CubeListBuilder.create()
				.texOffs(78, 40).addBox(-1.5F, -8.5F, 2.5F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, -1f, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	public static LayerDefinition createWingsLayer() {

		MeshDefinition meshdefinition = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
		PartDefinition body = meshdefinition.getRoot().getChild("body");

		PartDefinition b1 = body.addOrReplaceChild("b1", CubeListBuilder.create(), PartPose.offsetAndRotation(-3.2811F, 0.2679F, 8.0F, 0.0411F, 0.3027F, 0.2681F));

		PartDefinition cube_r25 = b1.addOrReplaceChild("cube_r25", CubeListBuilder.create().texOffs(0, 78).addBox(-3.7284F, -1.0808F, -0.412F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.3089F, -2.4756F, -2.2986F, 0.0F, 0.3229F, 0.4363F));

		PartDefinition cube_r26 = b1.addOrReplaceChild("cube_r26", CubeListBuilder.create().texOffs(0, 74).addBox(-4.0311F, -1.0808F, -2.4925F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.3089F, -2.4756F, 2.2986F, 0.0F, -0.3229F, 0.4363F));

		PartDefinition cube_r27 = b1.addOrReplaceChild("cube_r27", CubeListBuilder.create().texOffs(74, 28).addBox(-3.9054F, -2.1168F, -1.4769F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.2804F, -0.3923F, 0.0F, 0.0F, 0.0F, 0.7592F));

		PartDefinition cube_r28 = b1.addOrReplaceChild("cube_r28", CubeListBuilder.create().texOffs(74, 66).addBox(-3.8541F, -0.0364F, -1.4769F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.3375F, -4.5589F, 0.0F, 0.0F, 0.0F, 0.1134F));

		PartDefinition cube_r29 = b1.addOrReplaceChild("cube_r29", CubeListBuilder.create().texOffs(30, 66).addBox(-5.8937F, -0.1063F, -1.4769F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.4226F, -0.9063F, 0.0F, 0.0F, 0.0F, 0.6545F));

		PartDefinition cube_r30 = b1.addOrReplaceChild("cube_r30", CubeListBuilder.create().texOffs(14, 68).addBox(-5.8587F, -2.0514F, -1.4769F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.4226F, 0.9063F, 0.0F, 0.0F, 0.0F, 0.2182F));

		PartDefinition cube_r31 = b1.addOrReplaceChild("cube_r31", CubeListBuilder.create().texOffs(6, 82).addBox(-9.5732F, -1.0808F, -1.4769F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.4363F));

		PartDefinition cube_r32 = b1.addOrReplaceChild("cube_r32", CubeListBuilder.create().texOffs(45, 59).addBox(-0.8732F, -1.0808F, -1.4769F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.4363F));

		PartDefinition cube_r33 = b1.addOrReplaceChild("cube_r33", CubeListBuilder.create().texOffs(30, 61).addBox(-5.773F, -1.0808F, -2.4381F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, 0.0F, 0.2182F, 0.4363F));

		PartDefinition cube_r34 = b1.addOrReplaceChild("cube_r34", CubeListBuilder.create().texOffs(58, 22).addBox(-5.9794F, -1.0808F, -0.493F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -1.0F, 0.0F, -0.2182F, 0.4363F));

		PartDefinition b2 = body.addOrReplaceChild("b2", CubeListBuilder.create(), PartPose.offsetAndRotation(-3.2811F, 2.5179F, 8.0F, -0.1733F, 0.3542F, -0.4674F));

		PartDefinition cube_r35 = b2.addOrReplaceChild("cube_r35", CubeListBuilder.create().texOffs(56, 77).addBox(-3.672F, -1.0F, -0.3774F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.3089F, -2.4756F, -2.2986F, 0.0F, 0.3229F, 0.4363F));

		PartDefinition cube_r36 = b2.addOrReplaceChild("cube_r36", CubeListBuilder.create().texOffs(73, 54).addBox(-3.9651F, -1.0F, -2.4988F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.3089F, -2.4756F, 2.2986F, 0.0F, -0.3229F, 0.4363F));

		PartDefinition cube_r37 = b2.addOrReplaceChild("cube_r37", CubeListBuilder.create().texOffs(12, 72).addBox(-3.8185F, -2.0607F, -1.4619F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.2804F, -0.3923F, 0.0F, 0.0F, 0.0F, 0.7592F));

		PartDefinition cube_r38 = b2.addOrReplaceChild("cube_r38", CubeListBuilder.create().texOffs(73, 58).addBox(-3.8185F, 0.0607F, -1.4619F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.3375F, -4.5589F, 0.0F, 0.0F, 0.0F, 0.1134F));

		PartDefinition cube_r39 = b2.addOrReplaceChild("cube_r39", CubeListBuilder.create().texOffs(60, 34).addBox(-5.8132F, -0.0414F, -1.4619F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.4226F, -0.9063F, 0.0F, 0.0F, 0.0F, 0.6545F));

		PartDefinition cube_r40 = b2.addOrReplaceChild("cube_r40", CubeListBuilder.create().texOffs(57, 60).addBox(-5.8132F, -1.9586F, -1.4619F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.4226F, 0.9063F, 0.0F, 0.0F, 0.0F, 0.2182F));

		PartDefinition cube_r41 = b2.addOrReplaceChild("cube_r41", CubeListBuilder.create().texOffs(26, 54).addBox(-9.5087F, -1.0F, -1.4619F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.4363F));

		PartDefinition cube_r42 = b2.addOrReplaceChild("cube_r42", CubeListBuilder.create().texOffs(21, 16).addBox(-0.8087F, -1.0F, -1.4619F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.4363F));

		PartDefinition cube_r43 = b2.addOrReplaceChild("cube_r43", CubeListBuilder.create().texOffs(16, 58).addBox(-5.7132F, -1.0F, -2.4096F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, 0.0F, 0.2182F, 0.4363F));

		PartDefinition cube_r44 = b2.addOrReplaceChild("cube_r44", CubeListBuilder.create().texOffs(58, 18).addBox(-5.9132F, -1.0F, -0.4924F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -1.0F, 0.0F, -0.2182F, 0.4363F));

		PartDefinition b3 = body.addOrReplaceChild("b3", CubeListBuilder.create(), PartPose.offsetAndRotation(-3.2811F, 4.7321F, 8.0F, -0.0411F, 0.3027F, -0.2681F));

		PartDefinition cube_r45 = b3.addOrReplaceChild("cube_r45", CubeListBuilder.create().texOffs(10, 76).addBox(-3.7284F, -0.9192F, -0.412F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.3089F, 2.4756F, -2.2986F, 0.0F, 0.3229F, -0.4363F));

		PartDefinition cube_r46 = b3.addOrReplaceChild("cube_r46", CubeListBuilder.create().texOffs(73, 50).addBox(-4.0311F, -0.9192F, -2.4925F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.3089F, 2.4756F, 2.2986F, 0.0F, -0.3229F, -0.4363F));

		PartDefinition cube_r47 = b3.addOrReplaceChild("cube_r47", CubeListBuilder.create().texOffs(74, 24).addBox(-3.9054F, 0.1168F, -1.4769F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.2804F, 0.3923F, 0.0F, 0.0F, 0.0F, -0.7592F));

		PartDefinition cube_r48 = b3.addOrReplaceChild("cube_r48", CubeListBuilder.create().texOffs(74, 62).addBox(-3.8541F, -1.9636F, -1.4769F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.3375F, 4.5589F, 0.0F, 0.0F, 0.0F, -0.1134F));

		PartDefinition cube_r49 = b3.addOrReplaceChild("cube_r49", CubeListBuilder.create().texOffs(60, 64).addBox(-5.8937F, -1.8937F, -1.4769F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.4226F, 0.9063F, 0.0F, 0.0F, 0.0F, -0.6545F));

		PartDefinition cube_r50 = b3.addOrReplaceChild("cube_r50", CubeListBuilder.create().texOffs(0, 66).addBox(-5.8587F, 0.0514F, -1.4769F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.4226F, -0.9063F, 0.0F, 0.0F, 0.0F, -0.2182F));

		PartDefinition cube_r51 = b3.addOrReplaceChild("cube_r51", CubeListBuilder.create().texOffs(0, 82).addBox(-9.5732F, -0.9192F, -1.4769F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.4363F));

		PartDefinition cube_r52 = b3.addOrReplaceChild("cube_r52", CubeListBuilder.create().texOffs(42, 52).addBox(-0.8732F, -0.9192F, -1.4769F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.4363F));

		PartDefinition cube_r53 = b3.addOrReplaceChild("cube_r53", CubeListBuilder.create().texOffs(60, 30).addBox(-5.773F, -0.9192F, -2.4381F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, 0.0F, 0.2182F, -0.4363F));

		PartDefinition cube_r54 = b3.addOrReplaceChild("cube_r54", CubeListBuilder.create().texOffs(58, 14).addBox(-5.9794F, -0.9192F, -0.493F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -1.0F, 0.0F, -0.2182F, -0.4363F));

		PartDefinition b4 = body.addOrReplaceChild("b4", CubeListBuilder.create(), PartPose.offsetAndRotation(3.2811F, 0.2679F, 8.0F, 0.0411F, -0.3027F, -0.2681F));

		PartDefinition cube_r55 = b4.addOrReplaceChild("cube_r55", CubeListBuilder.create().texOffs(68, 75).addBox(-0.2716F, -1.0808F, -0.412F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.3089F, -2.4756F, -2.2986F, 0.0F, -0.3229F, -0.4363F));

		PartDefinition cube_r56 = b4.addOrReplaceChild("cube_r56", CubeListBuilder.create().texOffs(58, 72).addBox(0.0311F, -1.0808F, -2.4925F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.3089F, -2.4756F, 2.2986F, 0.0F, 0.3229F, -0.4363F));

		PartDefinition cube_r57 = b4.addOrReplaceChild("cube_r57", CubeListBuilder.create().texOffs(22, 74).addBox(-0.0946F, -2.1168F, -1.4769F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.2804F, -0.3923F, 0.0F, 0.0F, 0.0F, -0.7592F));

		PartDefinition cube_r58 = b4.addOrReplaceChild("cube_r58", CubeListBuilder.create().texOffs(34, 74).addBox(-0.1459F, -0.0364F, -1.4769F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.3375F, -4.5589F, 0.0F, 0.0F, 0.0F, -0.1134F));

		PartDefinition cube_r59 = b4.addOrReplaceChild("cube_r59", CubeListBuilder.create().texOffs(64, 42).addBox(-0.1063F, -0.1063F, -1.4769F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.4226F, -0.9063F, 0.0F, 0.0F, 0.0F, -0.6545F));

		PartDefinition cube_r60 = b4.addOrReplaceChild("cube_r60", CubeListBuilder.create().texOffs(44, 64).addBox(-0.1413F, -2.0514F, -1.4769F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.4226F, 0.9063F, 0.0F, 0.0F, 0.0F, -0.2182F));

		PartDefinition cube_r61 = b4.addOrReplaceChild("cube_r61", CubeListBuilder.create().texOffs(62, 81).addBox(8.5732F, -1.0808F, -1.4769F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.4363F));

		PartDefinition cube_r62 = b4.addOrReplaceChild("cube_r62", CubeListBuilder.create().texOffs(49, 39).addBox(-0.1268F, -1.0808F, -1.4769F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.4363F));

		PartDefinition cube_r63 = b4.addOrReplaceChild("cube_r63", CubeListBuilder.create().texOffs(60, 4).addBox(-0.227F, -1.0808F, -2.4381F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, 0.0F, -0.2182F, -0.4363F));

		PartDefinition cube_r64 = b4.addOrReplaceChild("cube_r64", CubeListBuilder.create().texOffs(58, 10).addBox(-0.0206F, -1.0808F, -0.493F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -1.0F, 0.0F, 0.2182F, -0.4363F));

		PartDefinition b5 = body.addOrReplaceChild("b5", CubeListBuilder.create(), PartPose.offsetAndRotation(3.2811F, 2.5179F, 8.0F, -0.1733F, -0.3542F, 0.4674F));

		PartDefinition cube_r65 = b5.addOrReplaceChild("cube_r65", CubeListBuilder.create().texOffs(46, 75).addBox(-0.328F, -1.0F, -0.3774F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.3089F, -2.4756F, -2.2986F, 0.0F, -0.3229F, -0.4363F));

		PartDefinition cube_r66 = b5.addOrReplaceChild("cube_r66", CubeListBuilder.create().texOffs(72, 16).addBox(-0.0349F, -1.0F, -2.4988F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.3089F, -2.4756F, 2.2986F, 0.0F, 0.3229F, -0.4363F));

		PartDefinition cube_r67 = b5.addOrReplaceChild("cube_r67", CubeListBuilder.create().texOffs(12, 30).addBox(-0.1815F, -2.0607F, -1.4619F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.2804F, -0.3923F, 0.0F, 0.0F, 0.0F, -0.7592F));

		PartDefinition cube_r68 = b5.addOrReplaceChild("cube_r68", CubeListBuilder.create().texOffs(72, 20).addBox(-0.1815F, 0.0607F, -1.4619F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.3375F, -4.5589F, 0.0F, 0.0F, 0.0F, -0.1134F));

		PartDefinition cube_r69 = b5.addOrReplaceChild("cube_r69", CubeListBuilder.create().texOffs(59, 56).addBox(-0.1868F, -0.0414F, -1.4619F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.4226F, -0.9063F, 0.0F, 0.0F, 0.0F, -0.6545F));

		PartDefinition cube_r70 = b5.addOrReplaceChild("cube_r70", CubeListBuilder.create().texOffs(60, 0).addBox(-0.1868F, -1.9586F, -1.4619F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.4226F, 0.9063F, 0.0F, 0.0F, 0.0F, -0.2182F));

		PartDefinition cube_r71 = b5.addOrReplaceChild("cube_r71", CubeListBuilder.create().texOffs(22, 40).addBox(8.5087F, -1.0F, -1.4619F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.4363F));

		PartDefinition cube_r72 = b5.addOrReplaceChild("cube_r72", CubeListBuilder.create().texOffs(0, 16).addBox(-0.1913F, -1.0F, -1.4619F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.4363F));

		PartDefinition cube_r73 = b5.addOrReplaceChild("cube_r73", CubeListBuilder.create().texOffs(56, 48).addBox(-0.2868F, -1.0F, -2.4096F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, 0.0F, -0.2182F, -0.4363F));

		PartDefinition cube_r74 = b5.addOrReplaceChild("cube_r74", CubeListBuilder.create().texOffs(0, 58).addBox(-0.0868F, -1.0F, -0.4924F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -1.0F, 0.0F, 0.2182F, -0.4363F));

		PartDefinition b6 = body.addOrReplaceChild("b6", CubeListBuilder.create(), PartPose.offsetAndRotation(3.2811F, 4.7321F, 8.0F, -0.0411F, -0.3027F, 0.2681F));

		PartDefinition cube_r75 = b6.addOrReplaceChild("cube_r75", CubeListBuilder.create().texOffs(75, 36).addBox(-0.2716F, -0.9192F, -0.412F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.3089F, 2.4756F, -2.2986F, 0.0F, -0.3229F, 0.4363F));

		PartDefinition cube_r76 = b6.addOrReplaceChild("cube_r76", CubeListBuilder.create().texOffs(72, 12).addBox(0.0311F, -0.9192F, -2.4925F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.3089F, 2.4756F, 2.2986F, 0.0F, 0.3229F, 0.4363F));

		PartDefinition cube_r77 = b6.addOrReplaceChild("cube_r77", CubeListBuilder.create().texOffs(74, 2).addBox(-0.0946F, 0.1168F, -1.4769F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.2804F, 0.3923F, 0.0F, 0.0F, 0.0F, 0.7592F));

		PartDefinition cube_r78 = b6.addOrReplaceChild("cube_r78", CubeListBuilder.create().texOffs(74, 32).addBox(-0.1459F, -1.9636F, -1.4769F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.3375F, 4.5589F, 0.0F, 0.0F, 0.0F, 0.1134F));

		PartDefinition cube_r79 = b6.addOrReplaceChild("cube_r79", CubeListBuilder.create().texOffs(0, 62).addBox(-0.1063F, -1.8937F, -1.4769F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.4226F, 0.9063F, 0.0F, 0.0F, 0.0F, 0.6545F));

		PartDefinition cube_r80 = b6.addOrReplaceChild("cube_r80", CubeListBuilder.create().texOffs(16, 63).addBox(-0.1413F, 0.0514F, -1.4769F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.4226F, -0.9063F, 0.0F, 0.0F, 0.0F, 0.2182F));

		PartDefinition cube_r81 = b6.addOrReplaceChild("cube_r81", CubeListBuilder.create().texOffs(56, 81).addBox(8.5732F, -0.9192F, -1.4769F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.4363F));

		PartDefinition cube_r82 = b6.addOrReplaceChild("cube_r82", CubeListBuilder.create().texOffs(16, 34).addBox(-0.1268F, -0.9192F, -1.4769F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.4363F));

		PartDefinition cube_r83 = b6.addOrReplaceChild("cube_r83", CubeListBuilder.create().texOffs(59, 52).addBox(-0.227F, -0.9192F, -2.4381F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, 0.0F, -0.2182F, 0.4363F));

		PartDefinition cube_r84 = b6.addOrReplaceChild("cube_r84", CubeListBuilder.create().texOffs(10, 54).addBox(-0.0206F, -0.9192F, -0.493F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -1.0F, 0.0F, 0.2182F, 0.4363F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create()
				.texOffs(0, 16).addBox(-3.5F, -7.0F, -3.5F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F))
				.texOffs(46, 68).addBox(-3.0F, -7.0F, -3.51F, 6.0F, 7.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-4.0F, -7.5F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(78, 40).addBox(-1.5F, -8.5F, 2.5F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 0.0F));

		PartDefinition r_r = head.addOrReplaceChild("r_r", CubeListBuilder.create().texOffs(71, 71).addBox(-5.0F, -2.0F, 0.0F, 6.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.75F, -6.5F, 3.0F));

		PartDefinition cube_r1 = r_r.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(40, 12).addBox(-9.0F, -2.0F, 0.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(59, 38).addBox(-8.0F, -2.0F, 0.0F, 7.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0622F, -0.7679F, 0.0F, -0.0666F, 0.2079F, -0.836F));

		PartDefinition cube_r2 = r_r.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(70, 46).addBox(-6.0F, -3.0F, 0.0F, 6.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.6962F, 0.5981F, 0.0F, -0.0087F, 0.0151F, -0.5237F));

		PartDefinition cube_r3 = r_r.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(72, 8).addBox(-6.0F, -3.0F, 0.0F, 6.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.1962F, 1.0F, 0.0F, -0.0087F, -0.0151F, 0.5237F));

		PartDefinition l_r = head.addOrReplaceChild("l_r", CubeListBuilder.create().texOffs(58, 68).addBox(-1.0F, -2.0F, 0.0F, 6.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(1.75F, -6.5F, 3.0F));

		PartDefinition cube_r4 = l_r.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 30).addBox(8.0F, -2.0F, 0.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(58, 26).addBox(1.0F, -2.0F, 0.0F, 7.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0622F, -0.7679F, 0.0F, -0.0666F, -0.2079F, 0.836F));

		PartDefinition cube_r5 = l_r.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 70).addBox(-1.0F, -3.0F, 0.0F, 6.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.8301F, 1.0981F, 0.0F, -0.0087F, -0.0151F, 0.5237F));

		PartDefinition cube_r6 = l_r.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(30, 70).addBox(0.0F, -3.0F, 0.0F, 6.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1962F, 1.0F, 0.0F, -0.0087F, 0.0151F, -0.5237F));

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(32, 0).addBox(-3.0F, 0.0F, -1.5F, 6.0F, 9.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(24, 26).addBox(-3.5F, -0.5F, -2.0F, 7.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 0.0F));

		PartDefinition cube_r7 = body.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(78, 77).addBox(-2.9637F, 0.2736F, -0.3263F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0635F, 0.38F, -1.5032F, -0.1292F, 0.1108F, 0.0824F));

		PartDefinition cube_r8 = body.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(46, 79).addBox(0.0894F, 0.2857F, -0.3388F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0635F, 0.38F, -1.5032F, -0.1292F, -0.1108F, -0.0824F));

		PartDefinition skirt = body.addOrReplaceChild("skirt", CubeListBuilder.create(), PartPose.offset(3.94F, 13.6975F, -3.3671F));

		PartDefinition cube_r9 = skirt.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(72, 79).addBox(0.0F, -8.0F, 0.0F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2197F, 0.0182F, -0.1985F));

		PartDefinition cube_r10 = skirt.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -8.0F, -1.5F, 1.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(35, 52).addBox(-1.0F, -4.0F, -2.5F, 1.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0406F, 0.013F, 3.3765F, 0.0F, 0.0F, -0.2007F));

		PartDefinition cube_r11 = skirt.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(68, 79).addBox(0.0F, -8.0F, -1.0F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 6.7341F, 0.2197F, -0.0182F, -0.1985F));

		PartDefinition cube_r12 = skirt.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(24, 0).addBox(0.0F, -8.0F, -1.5F, 1.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(52, 39).addBox(0.0F, -4.0F, -2.5F, 1.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.9206F, 0.013F, 3.3765F, 0.0F, 0.0F, 0.2007F));

		PartDefinition cube_r13 = skirt.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(28, 78).addBox(-2.0F, -10.0F, -2.0F, 2.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.3984F, 1.2928F, 9.3794F, 0.3438F, -0.1123F, 0.1824F));

		PartDefinition cube_r14 = skirt.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(44, 12).addBox(-3.0F, -10.0F, -2.0F, 6.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.94F, 1.3705F, 9.3709F, 0.3318F, 0.0F, 0.0F));

		PartDefinition cube_r15 = skirt.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(22, 78).addBox(0.0F, -10.0F, -2.0F, 2.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.4816F, 1.2928F, 9.3794F, 0.3438F, 0.1123F, -0.1824F));

		PartDefinition cube_r16 = skirt.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(44, 21).addBox(-3.0F, -10.0F, 1.0F, 6.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.94F, 1.3705F, -2.6368F, -0.3318F, 0.0F, 0.0F));

		PartDefinition cube_r17 = skirt.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(12, 80).addBox(-1.0F, -8.0F, -1.0F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.88F, 0.0F, 6.7341F, 0.2197F, 0.0182F, 0.1985F));

		PartDefinition cube_r18 = skirt.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(16, 80).addBox(-1.0F, -8.0F, 0.0F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.88F, 0.0F, 0.0F, -0.2197F, -0.0182F, 0.1985F));

		PartDefinition cube_r19 = skirt.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(34, 78).addBox(0.0F, -10.0F, 1.0F, 2.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.4816F, 1.2928F, -2.6453F, -0.3438F, -0.1123F, -0.1824F));

		PartDefinition cube_r20 = skirt.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(40, 78).addBox(-2.0F, -10.0F, 1.0F, 2.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.3984F, 1.2928F, -2.6453F, -0.3438F, 0.1123F, 0.1824F));

		PartDefinition bone = body.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(21, 21).addBox(-0.4617F, -0.363F, 0.0F, 2.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5383F, 1.363F, -2.0F));

		PartDefinition cube_r21 = bone.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(0, 7).addBox(-3.0F, -0.5F, 0.0F, 3.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.0873F, -0.0873F, -1.0908F));

		PartDefinition cube_r22 = bone.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(0, 20).addBox(0.0F, -0.5F, 0.0F, 3.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0765F, 0.0F, 0.0F, -0.0873F, 0.0873F, 1.0908F));

		PartDefinition cube_r23 = bone.addOrReplaceChild("cube_r23", CubeListBuilder.create().texOffs(0, 21).addBox(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.7883F, -0.113F, 0.0F, 0.0F, 0.0F, -0.6109F));

		PartDefinition cube_r24 = bone.addOrReplaceChild("cube_r24", CubeListBuilder.create().texOffs(21, 20).addBox(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.7117F, -0.113F, 0.0F, 0.0F, 0.0F, 0.6109F));

		PartDefinition b1 = body.addOrReplaceChild("b1", CubeListBuilder.create(), PartPose.offsetAndRotation(-3.2811F, 0.2679F, 8.0F, 0.0411F, 0.3027F, 0.2681F));

		PartDefinition cube_r25 = b1.addOrReplaceChild("cube_r25", CubeListBuilder.create().texOffs(0, 78).addBox(-3.7284F, -1.0808F, -0.412F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.3089F, -2.4756F, -2.2986F, 0.0F, 0.3229F, 0.4363F));

		PartDefinition cube_r26 = b1.addOrReplaceChild("cube_r26", CubeListBuilder.create().texOffs(0, 74).addBox(-4.0311F, -1.0808F, -2.4925F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.3089F, -2.4756F, 2.2986F, 0.0F, -0.3229F, 0.4363F));

		PartDefinition cube_r27 = b1.addOrReplaceChild("cube_r27", CubeListBuilder.create().texOffs(74, 28).addBox(-3.9054F, -2.1168F, -1.4769F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.2804F, -0.3923F, 0.0F, 0.0F, 0.0F, 0.7592F));

		PartDefinition cube_r28 = b1.addOrReplaceChild("cube_r28", CubeListBuilder.create().texOffs(74, 66).addBox(-3.8541F, -0.0364F, -1.4769F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.3375F, -4.5589F, 0.0F, 0.0F, 0.0F, 0.1134F));

		PartDefinition cube_r29 = b1.addOrReplaceChild("cube_r29", CubeListBuilder.create().texOffs(30, 66).addBox(-5.8937F, -0.1063F, -1.4769F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.4226F, -0.9063F, 0.0F, 0.0F, 0.0F, 0.6545F));

		PartDefinition cube_r30 = b1.addOrReplaceChild("cube_r30", CubeListBuilder.create().texOffs(14, 68).addBox(-5.8587F, -2.0514F, -1.4769F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.4226F, 0.9063F, 0.0F, 0.0F, 0.0F, 0.2182F));

		PartDefinition cube_r31 = b1.addOrReplaceChild("cube_r31", CubeListBuilder.create().texOffs(6, 82).addBox(-9.5732F, -1.0808F, -1.4769F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.4363F));

		PartDefinition cube_r32 = b1.addOrReplaceChild("cube_r32", CubeListBuilder.create().texOffs(45, 59).addBox(-0.8732F, -1.0808F, -1.4769F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.4363F));

		PartDefinition cube_r33 = b1.addOrReplaceChild("cube_r33", CubeListBuilder.create().texOffs(30, 61).addBox(-5.773F, -1.0808F, -2.4381F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, 0.0F, 0.2182F, 0.4363F));

		PartDefinition cube_r34 = b1.addOrReplaceChild("cube_r34", CubeListBuilder.create().texOffs(58, 22).addBox(-5.9794F, -1.0808F, -0.493F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -1.0F, 0.0F, -0.2182F, 0.4363F));

		PartDefinition b2 = body.addOrReplaceChild("b2", CubeListBuilder.create(), PartPose.offsetAndRotation(-3.2811F, 2.5179F, 8.0F, -0.1733F, 0.3542F, -0.4674F));

		PartDefinition cube_r35 = b2.addOrReplaceChild("cube_r35", CubeListBuilder.create().texOffs(56, 77).addBox(-3.672F, -1.0F, -0.3774F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.3089F, -2.4756F, -2.2986F, 0.0F, 0.3229F, 0.4363F));

		PartDefinition cube_r36 = b2.addOrReplaceChild("cube_r36", CubeListBuilder.create().texOffs(73, 54).addBox(-3.9651F, -1.0F, -2.4988F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.3089F, -2.4756F, 2.2986F, 0.0F, -0.3229F, 0.4363F));

		PartDefinition cube_r37 = b2.addOrReplaceChild("cube_r37", CubeListBuilder.create().texOffs(12, 72).addBox(-3.8185F, -2.0607F, -1.4619F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.2804F, -0.3923F, 0.0F, 0.0F, 0.0F, 0.7592F));

		PartDefinition cube_r38 = b2.addOrReplaceChild("cube_r38", CubeListBuilder.create().texOffs(73, 58).addBox(-3.8185F, 0.0607F, -1.4619F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.3375F, -4.5589F, 0.0F, 0.0F, 0.0F, 0.1134F));

		PartDefinition cube_r39 = b2.addOrReplaceChild("cube_r39", CubeListBuilder.create().texOffs(60, 34).addBox(-5.8132F, -0.0414F, -1.4619F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.4226F, -0.9063F, 0.0F, 0.0F, 0.0F, 0.6545F));

		PartDefinition cube_r40 = b2.addOrReplaceChild("cube_r40", CubeListBuilder.create().texOffs(57, 60).addBox(-5.8132F, -1.9586F, -1.4619F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.4226F, 0.9063F, 0.0F, 0.0F, 0.0F, 0.2182F));

		PartDefinition cube_r41 = b2.addOrReplaceChild("cube_r41", CubeListBuilder.create().texOffs(26, 54).addBox(-9.5087F, -1.0F, -1.4619F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.4363F));

		PartDefinition cube_r42 = b2.addOrReplaceChild("cube_r42", CubeListBuilder.create().texOffs(21, 16).addBox(-0.8087F, -1.0F, -1.4619F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.4363F));

		PartDefinition cube_r43 = b2.addOrReplaceChild("cube_r43", CubeListBuilder.create().texOffs(16, 58).addBox(-5.7132F, -1.0F, -2.4096F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, 0.0F, 0.2182F, 0.4363F));

		PartDefinition cube_r44 = b2.addOrReplaceChild("cube_r44", CubeListBuilder.create().texOffs(58, 18).addBox(-5.9132F, -1.0F, -0.4924F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -1.0F, 0.0F, -0.2182F, 0.4363F));

		PartDefinition b3 = body.addOrReplaceChild("b3", CubeListBuilder.create(), PartPose.offsetAndRotation(-3.2811F, 4.7321F, 8.0F, -0.0411F, 0.3027F, -0.2681F));

		PartDefinition cube_r45 = b3.addOrReplaceChild("cube_r45", CubeListBuilder.create().texOffs(10, 76).addBox(-3.7284F, -0.9192F, -0.412F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.3089F, 2.4756F, -2.2986F, 0.0F, 0.3229F, -0.4363F));

		PartDefinition cube_r46 = b3.addOrReplaceChild("cube_r46", CubeListBuilder.create().texOffs(73, 50).addBox(-4.0311F, -0.9192F, -2.4925F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.3089F, 2.4756F, 2.2986F, 0.0F, -0.3229F, -0.4363F));

		PartDefinition cube_r47 = b3.addOrReplaceChild("cube_r47", CubeListBuilder.create().texOffs(74, 24).addBox(-3.9054F, 0.1168F, -1.4769F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.2804F, 0.3923F, 0.0F, 0.0F, 0.0F, -0.7592F));

		PartDefinition cube_r48 = b3.addOrReplaceChild("cube_r48", CubeListBuilder.create().texOffs(74, 62).addBox(-3.8541F, -1.9636F, -1.4769F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.3375F, 4.5589F, 0.0F, 0.0F, 0.0F, -0.1134F));

		PartDefinition cube_r49 = b3.addOrReplaceChild("cube_r49", CubeListBuilder.create().texOffs(60, 64).addBox(-5.8937F, -1.8937F, -1.4769F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.4226F, 0.9063F, 0.0F, 0.0F, 0.0F, -0.6545F));

		PartDefinition cube_r50 = b3.addOrReplaceChild("cube_r50", CubeListBuilder.create().texOffs(0, 66).addBox(-5.8587F, 0.0514F, -1.4769F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.4226F, -0.9063F, 0.0F, 0.0F, 0.0F, -0.2182F));

		PartDefinition cube_r51 = b3.addOrReplaceChild("cube_r51", CubeListBuilder.create().texOffs(0, 82).addBox(-9.5732F, -0.9192F, -1.4769F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.4363F));

		PartDefinition cube_r52 = b3.addOrReplaceChild("cube_r52", CubeListBuilder.create().texOffs(42, 52).addBox(-0.8732F, -0.9192F, -1.4769F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.4363F));

		PartDefinition cube_r53 = b3.addOrReplaceChild("cube_r53", CubeListBuilder.create().texOffs(60, 30).addBox(-5.773F, -0.9192F, -2.4381F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, 0.0F, 0.2182F, -0.4363F));

		PartDefinition cube_r54 = b3.addOrReplaceChild("cube_r54", CubeListBuilder.create().texOffs(58, 14).addBox(-5.9794F, -0.9192F, -0.493F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -1.0F, 0.0F, -0.2182F, -0.4363F));

		PartDefinition b4 = body.addOrReplaceChild("b4", CubeListBuilder.create(), PartPose.offsetAndRotation(3.2811F, 0.2679F, 8.0F, 0.0411F, -0.3027F, -0.2681F));

		PartDefinition cube_r55 = b4.addOrReplaceChild("cube_r55", CubeListBuilder.create().texOffs(68, 75).addBox(-0.2716F, -1.0808F, -0.412F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.3089F, -2.4756F, -2.2986F, 0.0F, -0.3229F, -0.4363F));

		PartDefinition cube_r56 = b4.addOrReplaceChild("cube_r56", CubeListBuilder.create().texOffs(58, 72).addBox(0.0311F, -1.0808F, -2.4925F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.3089F, -2.4756F, 2.2986F, 0.0F, 0.3229F, -0.4363F));

		PartDefinition cube_r57 = b4.addOrReplaceChild("cube_r57", CubeListBuilder.create().texOffs(22, 74).addBox(-0.0946F, -2.1168F, -1.4769F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.2804F, -0.3923F, 0.0F, 0.0F, 0.0F, -0.7592F));

		PartDefinition cube_r58 = b4.addOrReplaceChild("cube_r58", CubeListBuilder.create().texOffs(34, 74).addBox(-0.1459F, -0.0364F, -1.4769F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.3375F, -4.5589F, 0.0F, 0.0F, 0.0F, -0.1134F));

		PartDefinition cube_r59 = b4.addOrReplaceChild("cube_r59", CubeListBuilder.create().texOffs(64, 42).addBox(-0.1063F, -0.1063F, -1.4769F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.4226F, -0.9063F, 0.0F, 0.0F, 0.0F, -0.6545F));

		PartDefinition cube_r60 = b4.addOrReplaceChild("cube_r60", CubeListBuilder.create().texOffs(44, 64).addBox(-0.1413F, -2.0514F, -1.4769F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.4226F, 0.9063F, 0.0F, 0.0F, 0.0F, -0.2182F));

		PartDefinition cube_r61 = b4.addOrReplaceChild("cube_r61", CubeListBuilder.create().texOffs(62, 81).addBox(8.5732F, -1.0808F, -1.4769F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.4363F));

		PartDefinition cube_r62 = b4.addOrReplaceChild("cube_r62", CubeListBuilder.create().texOffs(49, 39).addBox(-0.1268F, -1.0808F, -1.4769F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.4363F));

		PartDefinition cube_r63 = b4.addOrReplaceChild("cube_r63", CubeListBuilder.create().texOffs(60, 4).addBox(-0.227F, -1.0808F, -2.4381F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, 0.0F, -0.2182F, -0.4363F));

		PartDefinition cube_r64 = b4.addOrReplaceChild("cube_r64", CubeListBuilder.create().texOffs(58, 10).addBox(-0.0206F, -1.0808F, -0.493F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -1.0F, 0.0F, 0.2182F, -0.4363F));

		PartDefinition b5 = body.addOrReplaceChild("b5", CubeListBuilder.create(), PartPose.offsetAndRotation(3.2811F, 2.5179F, 8.0F, -0.1733F, -0.3542F, 0.4674F));

		PartDefinition cube_r65 = b5.addOrReplaceChild("cube_r65", CubeListBuilder.create().texOffs(46, 75).addBox(-0.328F, -1.0F, -0.3774F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.3089F, -2.4756F, -2.2986F, 0.0F, -0.3229F, -0.4363F));

		PartDefinition cube_r66 = b5.addOrReplaceChild("cube_r66", CubeListBuilder.create().texOffs(72, 16).addBox(-0.0349F, -1.0F, -2.4988F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.3089F, -2.4756F, 2.2986F, 0.0F, 0.3229F, -0.4363F));

		PartDefinition cube_r67 = b5.addOrReplaceChild("cube_r67", CubeListBuilder.create().texOffs(12, 30).addBox(-0.1815F, -2.0607F, -1.4619F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.2804F, -0.3923F, 0.0F, 0.0F, 0.0F, -0.7592F));

		PartDefinition cube_r68 = b5.addOrReplaceChild("cube_r68", CubeListBuilder.create().texOffs(72, 20).addBox(-0.1815F, 0.0607F, -1.4619F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.3375F, -4.5589F, 0.0F, 0.0F, 0.0F, -0.1134F));

		PartDefinition cube_r69 = b5.addOrReplaceChild("cube_r69", CubeListBuilder.create().texOffs(59, 56).addBox(-0.1868F, -0.0414F, -1.4619F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.4226F, -0.9063F, 0.0F, 0.0F, 0.0F, -0.6545F));

		PartDefinition cube_r70 = b5.addOrReplaceChild("cube_r70", CubeListBuilder.create().texOffs(60, 0).addBox(-0.1868F, -1.9586F, -1.4619F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.4226F, 0.9063F, 0.0F, 0.0F, 0.0F, -0.2182F));

		PartDefinition cube_r71 = b5.addOrReplaceChild("cube_r71", CubeListBuilder.create().texOffs(22, 40).addBox(8.5087F, -1.0F, -1.4619F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.4363F));

		PartDefinition cube_r72 = b5.addOrReplaceChild("cube_r72", CubeListBuilder.create().texOffs(0, 16).addBox(-0.1913F, -1.0F, -1.4619F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.4363F));

		PartDefinition cube_r73 = b5.addOrReplaceChild("cube_r73", CubeListBuilder.create().texOffs(56, 48).addBox(-0.2868F, -1.0F, -2.4096F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, 0.0F, -0.2182F, -0.4363F));

		PartDefinition cube_r74 = b5.addOrReplaceChild("cube_r74", CubeListBuilder.create().texOffs(0, 58).addBox(-0.0868F, -1.0F, -0.4924F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -1.0F, 0.0F, 0.2182F, -0.4363F));

		PartDefinition b6 = body.addOrReplaceChild("b6", CubeListBuilder.create(), PartPose.offsetAndRotation(3.2811F, 4.7321F, 8.0F, -0.0411F, -0.3027F, 0.2681F));

		PartDefinition cube_r75 = b6.addOrReplaceChild("cube_r75", CubeListBuilder.create().texOffs(75, 36).addBox(-0.2716F, -0.9192F, -0.412F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.3089F, 2.4756F, -2.2986F, 0.0F, -0.3229F, 0.4363F));

		PartDefinition cube_r76 = b6.addOrReplaceChild("cube_r76", CubeListBuilder.create().texOffs(72, 12).addBox(0.0311F, -0.9192F, -2.4925F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.3089F, 2.4756F, 2.2986F, 0.0F, 0.3229F, 0.4363F));

		PartDefinition cube_r77 = b6.addOrReplaceChild("cube_r77", CubeListBuilder.create().texOffs(74, 2).addBox(-0.0946F, 0.1168F, -1.4769F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.2804F, 0.3923F, 0.0F, 0.0F, 0.0F, 0.7592F));

		PartDefinition cube_r78 = b6.addOrReplaceChild("cube_r78", CubeListBuilder.create().texOffs(74, 32).addBox(-0.1459F, -1.9636F, -1.4769F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.3375F, 4.5589F, 0.0F, 0.0F, 0.0F, 0.1134F));

		PartDefinition cube_r79 = b6.addOrReplaceChild("cube_r79", CubeListBuilder.create().texOffs(0, 62).addBox(-0.1063F, -1.8937F, -1.4769F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.4226F, 0.9063F, 0.0F, 0.0F, 0.0F, 0.6545F));

		PartDefinition cube_r80 = b6.addOrReplaceChild("cube_r80", CubeListBuilder.create().texOffs(16, 63).addBox(-0.1413F, 0.0514F, -1.4769F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.4226F, -0.9063F, 0.0F, 0.0F, 0.0F, 0.2182F));

		PartDefinition cube_r81 = b6.addOrReplaceChild("cube_r81", CubeListBuilder.create().texOffs(56, 81).addBox(8.5732F, -0.9192F, -1.4769F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.4363F));

		PartDefinition cube_r82 = b6.addOrReplaceChild("cube_r82", CubeListBuilder.create().texOffs(16, 34).addBox(-0.1268F, -0.9192F, -1.4769F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.4363F));

		PartDefinition cube_r83 = b6.addOrReplaceChild("cube_r83", CubeListBuilder.create().texOffs(59, 52).addBox(-0.227F, -0.9192F, -2.4381F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, 0.0F, -0.2182F, 0.4363F));

		PartDefinition cube_r84 = b6.addOrReplaceChild("cube_r84", CubeListBuilder.create().texOffs(10, 54).addBox(-0.0206F, -0.9192F, -0.493F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -1.0F, 0.0F, 0.2182F, 0.4363F));

		PartDefinition rightArm = partdefinition.addOrReplaceChild("rightArm", CubeListBuilder.create().texOffs(50, 0).addBox(-2.0F, -1.0F, -1.5F, 2.0F, 9.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(26, 40).addBox(-2.55F, -1.5F, -2.0F, 3.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, 7.0F, 0.0F));

		PartDefinition leftArm = partdefinition.addOrReplaceChild("leftArm", CubeListBuilder.create().texOffs(49, 49).addBox(0.0F, -1.0F, -1.5F, 2.0F, 9.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(12, 40).addBox(-0.55F, -1.5F, -2.0F, 3.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 7.0F, 0.0F));

		PartDefinition rightLeg = partdefinition.addOrReplaceChild("rightLeg", CubeListBuilder.create().texOffs(0, 44).addBox(-1.35F, 0.0F, -1.5F, 3.0F, 9.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 30).addBox(-1.85F, -0.5F, -2.0F, 4.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.65F, 15.0F, 0.0F));

		PartDefinition leftLeg = partdefinition.addOrReplaceChild("leftLeg", CubeListBuilder.create().texOffs(40, 40).addBox(-1.35F, 0.0F, -1.5F, 3.0F, 9.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(28, 12).addBox(-1.85F, -0.5F, -2.0F, 4.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(1.35F, 15.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

}