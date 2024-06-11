package dev.xkmc.youkaishomecoming.content.entity.rumia;

import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class RumiaModel<T extends RumiaEntity> extends HierarchicalModel<T> {

	public static final ModelLayerLocation HAIRBAND = new ModelLayerLocation(YoukaisHomecoming.loc("rumia_hairband"), "main");
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(YoukaisHomecoming.loc("rumia"), "main");

	public static LayerDefinition createHairbandLayer() {
		MeshDefinition meshdefinition = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
		PartDefinition head = meshdefinition.getRoot().getChild("head");
		head.addOrReplaceChild("tie0", CubeListBuilder.create()
						.texOffs(0, 0).addBox(0.0F, -3.0F, 0.0F, 3.0F, 3.0F, 1.0F),
				PartPose.offsetAndRotation(3.25F, -5.0F, -0.7F, -0.3897F, -0.05F, -0.121F));
		head.addOrReplaceChild("tie1", CubeListBuilder.create()
				.texOffs(0, 4).addBox(0.0F, -3.0F, 0.0F, 3.0F, 3.0F, 1.0F),
				PartPose.offsetAndRotation(3.25F, -5.0F, -0.7F, -0.0254F, 0.4401F, -0.3778F));
		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 16).addBox(-3.5F, -7.0F, -3.5F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-4.0F, -7.5F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(21, 16).addBox(-3.0F, -7.0F, -3.51F, 6.0F, 7.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.0F, 0.0F));

		PartDefinition cube_r1 = head.addOrReplaceChild("tie0", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -3.0F, 0.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.25F, -5.0F, -0.7F, -0.3897F, -0.05F, -0.121F));

		PartDefinition cube_r2 = head.addOrReplaceChild("tie1", CubeListBuilder.create().texOffs(0, 4).addBox(0.0F, -3.0F, 0.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.25F, -5.0F, -0.7F, -0.0254F, 0.4401F, -0.3778F));

		PartDefinition rightLeg = partdefinition.addOrReplaceChild("rightLeg", CubeListBuilder.create().texOffs(48, 0).addBox(-3.0F, 1.0F, -1.5F, 3.0F, 10.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(32, 0).addBox(-3.5F, 0.5F, -2.0F, 4.0F, 11.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 13.0F, 0.0F));

		PartDefinition leftLeg = partdefinition.addOrReplaceChild("leftLeg", CubeListBuilder.create().texOffs(44, 51).addBox(0.0F, 1.0F, -1.5F, 3.0F, 10.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 30).addBox(-0.5F, 0.5F, -2.0F, 4.0F, 11.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 13.0F, 0.0F));

		PartDefinition rightArm = partdefinition.addOrReplaceChild("rightArm", CubeListBuilder.create().texOffs(0, 45).addBox(-2.0F, -1.0F, -1.5F, 2.0F, 10.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(28, 41).addBox(-2.55F, -1.5F, -2.0F, 3.0F, 11.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, 5.0F, 0.0F));

		PartDefinition leftArm = partdefinition.addOrReplaceChild("leftArm", CubeListBuilder.create().texOffs(44, 38).addBox(0.0F, -1.0F, -1.5F, 2.0F, 10.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(12, 41).addBox(-0.55F, -1.5F, -2.0F, 3.0F, 11.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 5.0F, 0.0F));

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(42, 15).addBox(-3.0F, -20.0F, -1.5F, 6.0F, 10.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(24, 26).addBox(-3.5F, -20.5F, -2.0F, 7.0F, 11.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition cube_r3 = body.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(60, 64).addBox(-3.0F, -0.5F, -2.0F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0635F, -17.87F, -0.1532F, -0.3045F, 0.1234F, 0.0619F));

		PartDefinition cube_r4 = body.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(10, 65).addBox(0.0F, -0.5F, -2.0F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0635F, -17.87F, -0.1532F, -0.3045F, -0.1234F, -0.0619F));

		PartDefinition skirt = partdefinition.addOrReplaceChild("skirt", CubeListBuilder.create(), PartPose.offset(5.1659F, 20.3904F, 1.8277F));

		PartDefinition cube_r5 = skirt.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(6, 55).addBox(0.001F, -9.0F, 0.0F, 0.0F, 9.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.3318F, 0.0F, -3.3553F, 0.0F, 0.0F, 0.2443F));

		PartDefinition cube_r6 = skirt.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(18, 53).addBox(-0.001F, -9.0F, -3.0F, 0.0F, 9.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.2507F, -0.9361F, 2.4838F, 0.3289F, -0.0039F, -0.2503F));

		PartDefinition cube_r7 = skirt.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(48, 64).addBox(0.0F, -9.0F, -0.001F, 3.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.1659F, -0.3217F, 2.844F, 0.3314F, 0.12F, -0.2078F));

		PartDefinition cube_r8 = skirt.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(58, 28).addBox(-2.0F, -6.0F, 0.0F, 4.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.1659F, -3.1404F, 1.817F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r9 = skirt.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(64, 46).addBox(-3.0F, -9.0F, -0.001F, 3.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.1659F, -0.3217F, 2.844F, 0.3314F, -0.12F, 0.2078F));

		PartDefinition cube_r10 = skirt.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(12, 53).addBox(0.001F, -9.0F, -3.0F, 0.0F, 9.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.0812F, -0.9361F, 2.4838F, 0.3289F, 0.0039F, 0.2503F));

		PartDefinition cube_r11 = skirt.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(24, 53).addBox(0.001F, -9.0F, 0.0F, 0.0F, 9.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.0812F, -0.9361F, -6.1945F, -0.3289F, -0.0039F, 0.2503F));

		PartDefinition cube_r12 = skirt.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(54, 64).addBox(-3.0F, -9.0F, 0.001F, 3.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.1659F, -0.3217F, -6.5547F, -0.3314F, 0.12F, 0.2078F));

		PartDefinition cube_r13 = skirt.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(56, 34).addBox(-0.001F, -9.0F, 0.0F, 0.0F, 9.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -3.3553F, 0.0F, 0.0F, -0.2443F));

		PartDefinition cube_r14 = skirt.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(30, 53).addBox(-0.001F, -9.0F, 0.0F, 0.0F, 9.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.2507F, -0.9361F, -6.1945F, -0.3289F, 0.0039F, -0.2503F));

		PartDefinition cube_r15 = skirt.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(64, 55).addBox(0.0F, -9.0F, 0.001F, 3.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.1659F, -0.3217F, -6.5547F, -0.3314F, -0.12F, -0.2078F));

		PartDefinition cube_r16 = skirt.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(60, 0).addBox(-2.0F, -6.0F, 0.0F, 4.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.1659F, -3.1404F, -5.5277F, -0.3491F, 0.0F, 0.0F));

		PartDefinition skirt2 = partdefinition.addOrReplaceChild("skirt2", CubeListBuilder.create(), PartPose.offset(5.1659F, 19.6404F, 1.8277F));

		PartDefinition cube_r17 = skirt2.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(36, 53).addBox(0.001F, -9.0F, 0.0077F, 0.0F, 9.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.3318F, 0.0F, -3.3553F, 0.0F, 0.0F, 0.2443F));

		PartDefinition cube_r18 = skirt2.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(16, 27).addBox(-0.001F, -9.0F, -3.0F, 0.0F, 9.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.2507F, -0.9361F, 2.4838F, 0.3289F, -0.0039F, -0.2503F));

		PartDefinition cube_r19 = skirt2.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(60, 9).addBox(0.0F, -9.0F, -0.001F, 3.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.1659F, -0.3217F, 2.844F, 0.3314F, 0.12F, -0.2078F));

		PartDefinition cube_r20 = skirt2.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(56, 46).addBox(-2.0F, -6.0F, -0.0047F, 4.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.1659F, -3.1404F, 1.817F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r21 = skirt2.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(60, 18).addBox(-3.0F, -9.0F, -0.001F, 3.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.1659F, -0.3217F, 2.844F, 0.3314F, -0.12F, 0.2078F));

		PartDefinition cube_r22 = skirt2.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(33, 12).addBox(0.001F, -9.0F, -3.0F, 0.0F, 9.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.0812F, -0.9361F, 2.4838F, 0.3289F, 0.0039F, 0.2503F));

		PartDefinition cube_r23 = skirt2.addOrReplaceChild("cube_r23", CubeListBuilder.create().texOffs(46, 25).addBox(0.001F, -9.0F, 0.0F, 0.0F, 9.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.0812F, -0.9361F, -6.1945F, -0.3289F, -0.0039F, 0.2503F));

		PartDefinition cube_r24 = skirt2.addOrReplaceChild("cube_r24", CubeListBuilder.create().texOffs(62, 37).addBox(-3.0F, -9.0F, 0.001F, 3.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.1659F, -0.3217F, -6.5547F, -0.3314F, 0.12F, 0.2078F));

		PartDefinition cube_r25 = skirt2.addOrReplaceChild("cube_r25", CubeListBuilder.create().texOffs(0, 55).addBox(-0.001F, -9.0F, 0.0077F, 0.0F, 9.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -3.3553F, 0.0F, 0.0F, -0.2443F));

		PartDefinition cube_r26 = skirt2.addOrReplaceChild("cube_r26", CubeListBuilder.create().texOffs(52, 25).addBox(-0.001F, -9.0F, 0.0F, 0.0F, 9.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.2507F, -0.9361F, -6.1945F, -0.3289F, 0.0039F, -0.2503F));

		PartDefinition cube_r27 = skirt2.addOrReplaceChild("cube_r27", CubeListBuilder.create().texOffs(42, 64).addBox(0.0F, -9.0F, 0.001F, 3.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.1659F, -0.3217F, -6.5547F, -0.3314F, -0.12F, -0.2078F));

		PartDefinition cube_r28 = skirt2.addOrReplaceChild("cube_r28", CubeListBuilder.create().texOffs(56, 55).addBox(-2.0F, -6.0F, 0.0F, 4.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.1659F, -3.1404F, -5.5277F, -0.3491F, 0.0F, 0.0F));

		PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(-0.1966F, 6.763F, -2.0439F));

		PartDefinition cube_r29 = bone.addOrReplaceChild("cube_r29", CubeListBuilder.create().texOffs(0, 16).addBox(-1.0F, -1.25F, -0.5F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.3054F));

		PartDefinition cube_r30 = bone.addOrReplaceChild("cube_r30", CubeListBuilder.create().texOffs(24, 0).addBox(-1.0F, -1.25F, -0.5F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.3933F, 0.0F, 0.0F, -0.3927F, 0.0F, -0.3054F));

		PartDefinition cube_r31 = bone.addOrReplaceChild("cube_r31", CubeListBuilder.create().texOffs(24, 5).addBox(-1.0F, -1.7F, -1.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.1966F, -0.763F, 0.5439F, -0.3054F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	private final ModelPart root;
	private final ModelPart head;
	private final ModelPart rightLeg;
	private final ModelPart leftLeg;
	private final ModelPart rightArm;
	private final ModelPart leftArm;
	private final ModelPart tie0;
	private final ModelPart tie1;

	public RumiaModel(ModelPart root) {
		this.root = root;
		this.head = root.getChild("head");
		this.rightLeg = root.getChild("rightLeg");
		this.leftLeg = root.getChild("leftLeg");
		this.rightArm = root.getChild("rightArm");
		this.leftArm = root.getChild("leftArm");
		this.tie0 = head.getChild("tie0");
		this.tie1 = head.getChild("tie1");
	}

	@Override
	public void prepareMobModel(T e, float pLimbSwing, float pLimbSwingAmount, float pPartialTick) {
		boolean ex = e.isEx();
		tie0.skipDraw = ex;
		tie1.skipDraw = ex;
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

		float legSwing = !e.onGround() || e.isBlocked() ? 0 : Math.min(legMaxSwing, pLimbSwingAmount);
		leftLeg.xRot = Mth.cos(pLimbSwing * 0.6662F + (float) Math.PI) * 1.4F * legSwing / f;
		rightLeg.xRot = Mth.cos(pLimbSwing * 0.6662F) * 1.4F * legSwing / f;

	}


}