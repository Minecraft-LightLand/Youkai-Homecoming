package dev.xkmc.youkaishomecoming.content.entity.reimu;// Made with Blockbench 4.10.3
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class ReimuModel<T extends MaidenEntity> extends HierarchicalModel<T> {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(YoukaisHomecoming.loc("hakurei_reimu"), "main");
	public static final ModelLayerLocation HAT_LOCATION = new ModelLayerLocation(YoukaisHomecoming.loc("reimu_hat"), "main");

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 18).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-4.5F, -8.5F, -4.5F, 9.0F, 9.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition r_r = head.addOrReplaceChild("r_r", CubeListBuilder.create(), PartPose.offset(-9.1648F, -4.4215F, -4.6F));

		PartDefinition cube_r1 = r_r.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(38, 2).addBox(-1.0F, -3.0F, -2.0F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(38, 3).addBox(-1.0F, -1.0F, -2.5F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(38, 4).addBox(-1.0F, 1.0F, -2.5F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(23, 46).addBox(-1.0F, 3.0F, -2.5F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(47, 22).addBox(-1.0F, 5.0F, -2.5F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(26, 1).addBox(3.0F, -3.0F, -2.0F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(26, 5).addBox(3.0F, -1.0F, -2.5F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(26, 6).addBox(3.0F, 1.0F, -2.5F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(32, 5).addBox(3.0F, 3.0F, -2.5F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(32, 6).addBox(3.0F, 5.0F, -2.5F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.5964F, -4.5245F, 12.6826F, 0.3736F, -0.1393F, 0.2727F));

		PartDefinition cube_r2 = r_r.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(76, 12).addBox(-4.1513F, -1.7307F, 5.03F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(2, 69).addBox(-4.1513F, 0.2693F, 4.03F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(69, 52).addBox(-4.1513F, 2.2693F, 5.03F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(70, 0).addBox(-4.1513F, 4.2693F, 4.03F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(74, 70).addBox(-0.1513F, -1.7307F, 5.03F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(62, 68).addBox(-0.1513F, 0.2693F, 4.03F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(64, 68).addBox(-0.1513F, 2.2693F, 5.03F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(0, 69).addBox(-0.1513F, 4.2693F, 4.03F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.1532F, -2.282F, 6.6398F, 0.3736F, -0.1393F, 0.2727F));

		PartDefinition cube_r3 = r_r.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(79, 40).addBox(-4.1513F, 6.2693F, 4.03F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(79, 29).addBox(-0.1513F, 6.2693F, 4.03F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.1273F, -2.6692F, 7.5614F, 0.3736F, -0.1393F, 0.2727F));

		PartDefinition cube_r4 = r_r.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(79, 42).addBox(-1.0F, 4.0F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.391F, -2.8023F, 13.4286F, 0.0F, -0.0436F, 0.1309F));

		PartDefinition cube_r5 = r_r.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(51, 48).addBox(-1.0F, 5.0F, -2.0F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.3476F, -2.807F, 14.4286F, 0.0F, -0.0436F, 0.1309F));

		PartDefinition cube_r6 = r_r.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(71, 42).addBox(-1.0F, 3.0F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.1732F, -0.8137F, 12.4295F, 0.0F, -0.0436F, 0.1309F));

		PartDefinition cube_r7 = r_r.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(51, 49).addBox(0.0F, 3.0F, -2.0F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.8349F, 1.0294F, 14.385F, 0.0F, -0.0436F, 0.1309F));

		PartDefinition cube_r8 = r_r.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(71, 54).addBox(-1.0F, 3.0F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.8255F, 1.1588F, 13.4286F, 0.0F, -0.0436F, 0.1309F));

		PartDefinition cube_r9 = r_r.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(16, 52).addBox(-1.0F, 5.0F, -1.0F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(83, 27).addBox(0.0F, 0.0F, -2.0F, 3.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.8255F, 1.1588F, 13.9286F, 0.0F, -0.0436F, 0.1309F));

		PartDefinition cube_r10 = r_r.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(16, 53).addBox(-1.0F, 5.0F, -1.0F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.7874F, 1.6814F, 14.1031F, 0.0F, -0.0436F, 0.1309F));

		PartDefinition cube_r11 = r_r.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(71, 62).addBox(-1.0F, 3.0F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.7874F, 1.6814F, 13.6031F, 0.0F, -0.0436F, 0.1309F));

		PartDefinition cube_r12 = r_r.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(72, 26).addBox(-1.0F, 3.0F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.1351F, -0.2911F, 12.604F, 0.0F, -0.0436F, 0.1309F));

		PartDefinition cube_r13 = r_r.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(56, 1).addBox(-1.0F, 5.0F, -2.0F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.3095F, -2.2844F, 14.6031F, 0.0F, -0.0436F, 0.1309F));

		PartDefinition cube_r14 = r_r.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(79, 43).addBox(-1.0F, 4.0F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.3529F, -2.2797F, 13.603F, 0.0F, -0.0436F, 0.1309F));

		PartDefinition cube_r15 = r_r.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(78, 10).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.0045F, -5.6155F, 9.0403F, 0.3736F, -0.1393F, 0.2727F));

		PartDefinition cube_r16 = r_r.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(36, 28).addBox(-8.9449F, -0.1269F, 0.6F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.9643F, -5.4984F, 7.0594F, -0.2849F, 0.274F, -0.8249F));

		PartDefinition cube_r17 = r_r.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(40, 49).addBox(-8.9449F, -0.1269F, -0.9F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.6079F, -4.0353F, 7.462F, -0.2849F, 0.274F, -0.8249F));

		PartDefinition cube_r18 = r_r.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(16, 54).addBox(0.0F, 3.0F, -2.0F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.7967F, 1.552F, 14.5595F, 0.0F, -0.0436F, 0.1309F));

		PartDefinition cube_r19 = r_r.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(1, 50).addBox(-8.9449F, -0.1269F, 0.1F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(1, 51).addBox(-8.9449F, 1.8731F, -0.4F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(14, 52).addBox(-8.9449F, -0.1269F, 0.6F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.0401F, -6.9134F, 8.5256F, -0.2849F, 0.274F, -0.8249F));

		PartDefinition cube_r20 = r_r.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(43, 20).addBox(1.0F, 2.0F, -1.0F, 1.0F, 0.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(84, 62).addBox(1.0F, 0.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(80, 6).addBox(2.0F, 1.0F, -1.0F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.6014F, -1.9074F, 8.7F, 0.0F, 0.0F, -0.3054F));

		PartDefinition cube_r21 = r_r.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(51, 3).addBox(-1.0F, 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.6425F, -3.6293F, 8.1F, 0.0F, 0.0F, 0.1309F));

		PartDefinition cube_r22 = r_r.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(51, 4).addBox(-1.0F, 0.0F, -1.0F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.3816F, -1.6474F, 9.101F, 0.0F, 0.0F, 0.1309F));

		PartDefinition cube_r23 = r_r.addOrReplaceChild("cube_r23", CubeListBuilder.create().texOffs(34, 50).addBox(-1.2F, 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.1017F, -5.5851F, 8.101F, 0.0F, 0.0F, 0.1309F));

		PartDefinition cube_r24 = r_r.addOrReplaceChild("cube_r24", CubeListBuilder.create().texOffs(40, 50).addBox(-1.0F, 0.0F, -1.0F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(67, 0).addBox(-1.0F, 0.0F, -0.5F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.1646F, -7.5951F, 8.6F, 0.0F, 0.0F, 0.1309F));

		PartDefinition cube_r25 = r_r.addOrReplaceChild("cube_r25", CubeListBuilder.create().texOffs(56, 36).addBox(-1.0F, 0.0F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.6427F, -3.6303F, 8.1F, 0.0F, 0.0F, 0.1309F));

		PartDefinition cube_r26 = r_r.addOrReplaceChild("cube_r26", CubeListBuilder.create().texOffs(57, 16).addBox(-1.0F, 0.0F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.9036F, -5.6122F, 9.1F, 0.0F, 0.0F, 0.1309F));

		PartDefinition cube_r27 = r_r.addOrReplaceChild("cube_r27", CubeListBuilder.create().texOffs(71, 62).addBox(-5.0F, 0.0F, -1.0F, 5.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.1219F, -6.9424F, 8.6F, 0.0F, 0.0F, 0.1309F));

		PartDefinition cube_r28 = r_r.addOrReplaceChild("cube_r28", CubeListBuilder.create().texOffs(70, 0).addBox(-8.0F, 0.0F, -1.0F, 8.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.1219F, -6.9424F, 8.6F, -0.2802F, 0.2789F, -0.8075F));

		PartDefinition l_r = head.addOrReplaceChild("l_r", CubeListBuilder.create(), PartPose.offset(9.1648F, -4.4215F, -4.6F));

		PartDefinition cube_r29 = l_r.addOrReplaceChild("cube_r29", CubeListBuilder.create().texOffs(59, 17).addBox(0.0F, 5.0F, -1.0F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.7874F, 1.6814F, 14.1031F, 0.0F, 0.0436F, -0.1309F));

		PartDefinition cube_r30 = l_r.addOrReplaceChild("cube_r30", CubeListBuilder.create().texOffs(73, 40).addBox(0.0F, 3.0F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.7874F, 1.6814F, 13.6031F, 0.0F, 0.0436F, -0.1309F));

		PartDefinition cube_r31 = l_r.addOrReplaceChild("cube_r31", CubeListBuilder.create().texOffs(59, 16).addBox(-1.0F, 3.0F, -2.0F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.7967F, 1.552F, 14.5595F, 0.0F, 0.0436F, -0.1309F));

		PartDefinition cube_r32 = l_r.addOrReplaceChild("cube_r32", CubeListBuilder.create().texOffs(72, 28).addBox(0.0F, 3.0F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.1351F, -0.2911F, 12.604F, 0.0F, 0.0436F, -0.1309F));

		PartDefinition cube_r33 = l_r.addOrReplaceChild("cube_r33", CubeListBuilder.create().texOffs(56, 2).addBox(0.0F, 5.0F, -2.0F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.3095F, -2.2844F, 14.6031F, 0.0F, 0.0436F, -0.1309F));

		PartDefinition cube_r34 = l_r.addOrReplaceChild("cube_r34", CubeListBuilder.create().texOffs(80, 6).addBox(0.0F, 4.0F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.3529F, -2.2797F, 13.603F, 0.0F, 0.0436F, -0.1309F));

		PartDefinition cube_r35 = l_r.addOrReplaceChild("cube_r35", CubeListBuilder.create().texOffs(51, 47).addBox(0.0F, 5.0F, -2.0F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.3476F, -2.807F, 14.4286F, 0.0F, 0.0436F, -0.1309F));

		PartDefinition cube_r36 = l_r.addOrReplaceChild("cube_r36", CubeListBuilder.create().texOffs(79, 28).addBox(-0.8487F, 6.2693F, 4.03F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(79, 41).addBox(3.1513F, 6.2693F, 4.03F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.1273F, -2.6692F, 7.5614F, 0.3736F, 0.1393F, -0.2727F));

		PartDefinition cube_r37 = l_r.addOrReplaceChild("cube_r37", CubeListBuilder.create().texOffs(26, 0).addBox(-4.0F, 5.0F, -2.5F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(23, 19).addBox(-4.0F, 3.0F, -2.5F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(23, 18).addBox(-4.0F, 1.0F, -2.5F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(5, 1).addBox(-4.0F, -1.0F, -2.5F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(5, 0).addBox(-4.0F, -3.0F, -2.0F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(47, 23).addBox(0.0F, -3.0F, -2.0F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(47, 28).addBox(0.0F, -1.0F, -2.5F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(51, 18).addBox(0.0F, 1.0F, -2.5F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(51, 19).addBox(0.0F, 3.0F, -2.5F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(17, 66).addBox(0.0F, 5.0F, -2.5F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.5964F, -4.5245F, 12.6826F, 0.3736F, 0.1393F, -0.2727F));

		PartDefinition cube_r38 = l_r.addOrReplaceChild("cube_r38", CubeListBuilder.create().texOffs(60, 68).addBox(-0.8487F, 4.2693F, 4.03F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(58, 68).addBox(-0.8487F, 2.2693F, 5.03F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(52, 68).addBox(-0.8487F, 0.2693F, 4.03F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(74, 14).addBox(-0.8487F, -1.7307F, 5.03F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(76, 14).addBox(3.1513F, -1.7307F, 5.03F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(70, 28).addBox(3.1513F, 2.2693F, 5.03F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(70, 26).addBox(3.1513F, 0.2693F, 4.03F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(71, 40).addBox(3.1513F, 4.2693F, 4.03F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.1532F, -2.282F, 6.6398F, 0.3736F, 0.1393F, -0.2727F));

		PartDefinition cube_r39 = l_r.addOrReplaceChild("cube_r39", CubeListBuilder.create().texOffs(51, 66).addBox(0.0F, 5.0F, -1.0F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(83, 27).addBox(-3.0F, 0.0F, -2.0F, 3.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.8255F, 1.1588F, 13.9286F, 0.0F, 0.0436F, -0.1309F));

		PartDefinition cube_r40 = l_r.addOrReplaceChild("cube_r40", CubeListBuilder.create().texOffs(80, 7).addBox(0.0F, 4.0F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.391F, -2.8023F, 13.4286F, 0.0F, 0.0436F, -0.1309F));

		PartDefinition cube_r41 = l_r.addOrReplaceChild("cube_r41", CubeListBuilder.create().texOffs(73, 42).addBox(0.0F, 3.0F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.1732F, -0.8137F, 12.4295F, 0.0F, 0.0436F, -0.1309F));

		PartDefinition cube_r42 = l_r.addOrReplaceChild("cube_r42", CubeListBuilder.create().texOffs(74, 12).addBox(0.0F, 3.0F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.8255F, 1.1588F, 13.4286F, 0.0F, 0.0436F, -0.1309F));

		PartDefinition cube_r43 = l_r.addOrReplaceChild("cube_r43", CubeListBuilder.create().texOffs(17, 65).addBox(-1.0F, 3.0F, -2.0F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.8349F, 1.0294F, 14.385F, 0.0F, 0.0436F, -0.1309F));

		PartDefinition cube_r44 = l_r.addOrReplaceChild("cube_r44", CubeListBuilder.create().texOffs(78, 10).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0045F, -5.6155F, 9.0403F, 0.3736F, 0.1393F, -0.2727F));

		PartDefinition cube_r45 = l_r.addOrReplaceChild("cube_r45", CubeListBuilder.create().texOffs(39, 28).addBox(7.9449F, -0.1269F, 0.6F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.9643F, -5.4984F, 7.0594F, -0.2849F, -0.274F, 0.8249F));

		PartDefinition cube_r46 = l_r.addOrReplaceChild("cube_r46", CubeListBuilder.create().texOffs(43, 28).addBox(7.9449F, -0.1269F, 0.1F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.6079F, -4.0353F, 7.462F, -0.2849F, -0.274F, 0.8249F));

		PartDefinition cube_r47 = l_r.addOrReplaceChild("cube_r47", CubeListBuilder.create().texOffs(33, 47).addBox(7.9449F, -0.1269F, -0.9F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(32, 65).addBox(7.9449F, -0.1269F, -0.4F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.0401F, -6.9134F, 8.5256F, -0.2849F, -0.274F, 0.8249F));

		PartDefinition cube_r48 = l_r.addOrReplaceChild("cube_r48", CubeListBuilder.create().texOffs(55, 34).addBox(7.9449F, -0.1269F, -0.4F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.7286F, -5.4991F, 8.9065F, -0.2849F, -0.274F, 0.8249F));

		PartDefinition cube_r49 = l_r.addOrReplaceChild("cube_r49", CubeListBuilder.create().texOffs(80, 6).addBox(-5.0F, 1.0F, -1.0F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(43, 18).addBox(-2.0F, 2.0F, -1.0F, 1.0F, 0.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(15, 50).addBox(-3.0F, 0.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.6014F, -1.9074F, 8.7F, 0.0F, 0.0F, 0.3054F));

		PartDefinition cube_r50 = l_r.addOrReplaceChild("cube_r50", CubeListBuilder.create().texOffs(39, 47).addBox(0.0F, 0.0F, -1.0F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.6425F, -3.6293F, 9.1F, 0.0F, 0.0F, -0.1309F));

		PartDefinition cube_r51 = l_r.addOrReplaceChild("cube_r51", CubeListBuilder.create().texOffs(33, 48).addBox(0.0F, 0.0F, 0.0F, 2.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.1646F, -1.5951F, 8.6F, 0.0F, 0.0F, -0.1309F));

		PartDefinition cube_r52 = l_r.addOrReplaceChild("cube_r52", CubeListBuilder.create().texOffs(39, 48).addBox(0.0F, 0.0F, -1.0F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.9035F, -5.6112F, 9.1F, 0.0F, 0.0F, -0.1309F));

		PartDefinition cube_r53 = l_r.addOrReplaceChild("cube_r53", CubeListBuilder.create().texOffs(33, 49).addBox(0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(48, 65).addBox(0.0F, 0.0F, 0.5F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.1646F, -7.5951F, 8.6F, 0.0F, 0.0F, -0.1309F));

		PartDefinition cube_r54 = l_r.addOrReplaceChild("cube_r54", CubeListBuilder.create().texOffs(56, 50).addBox(0.0F, 0.0F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.6427F, -3.6303F, 9.099F, 0.0F, 0.0F, -0.1309F));

		PartDefinition cube_r55 = l_r.addOrReplaceChild("cube_r55", CubeListBuilder.create().texOffs(57, 52).addBox(0.0F, 0.0F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.9036F, -5.6122F, 8.1F, 0.0F, 0.0F, -0.1309F));

		PartDefinition cube_r56 = l_r.addOrReplaceChild("cube_r56", CubeListBuilder.create().texOffs(71, 62).mirror().addBox(0.0F, 0.0F, -1.0F, 5.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-7.1219F, -6.9424F, 8.6F, 0.0F, 0.0F, -0.1309F));

		PartDefinition cube_r57 = l_r.addOrReplaceChild("cube_r57", CubeListBuilder.create().texOffs(70, 0).mirror().addBox(0.0F, 0.0F, -1.0F, 8.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-7.1219F, -6.9424F, 8.6F, -0.2802F, -0.2789F, 0.8075F));

		PartDefinition bone4 = head.addOrReplaceChild("bone4", CubeListBuilder.create().texOffs(8, 68).addBox(-5.0379F, -17.506F, -2.2F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 82).addBox(-4.6027F, -17.0845F, -1.0F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(12, 83).addBox(-4.6027F, -15.0845F, 1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(85, 56).addBox(-2.0F, -7.0F, 1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(84, 77).addBox(-7.2F, -7.0F, 1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(74, 81).addBox(-5.6027F, -8.0845F, 1.0001F, 4.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(27, 0).addBox(-5.6027F, -4.0845F, 1.0001F, 4.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(42, 22).addBox(-7.2F, -4.0F, 1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(27, 5).addBox(-2.0F, -4.0F, 1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(3.6027F, 7.5845F, 3.5F));

		PartDefinition cube_r58 = bone4.addOrReplaceChild("cube_r58", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, -0.9998F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.6027F, -13.0845F, 2.0F, 0.0F, 0.0F, 0.3927F));

		PartDefinition cube_r59 = bone4.addOrReplaceChild("cube_r59", CubeListBuilder.create().texOffs(30, 83).addBox(-1.0F, 0.0F, -0.9999F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.6027F, -13.0845F, 2.0F, 0.0F, 0.0F, -0.3927F));

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 34).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(27, 29).addBox(-4.5F, -0.25F, -2.5F, 9.0F, 13.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r60 = body.addOrReplaceChild("cube_r60", CubeListBuilder.create().texOffs(79, 21).addBox(-1.1F, -1.5F, -2.0F, 4.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0365F, 3.03F, -0.8032F, -0.2172F, -0.1234F, -0.0619F));

		PartDefinition cube_r61 = body.addOrReplaceChild("cube_r61", CubeListBuilder.create().texOffs(81, 38).addBox(-2.9F, -1.5F, -2.0F, 4.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0365F, 3.03F, -0.8032F, -0.2172F, 0.1234F, 0.0619F));

		PartDefinition bone3 = body.addOrReplaceChild("bone3", CubeListBuilder.create(), PartPose.offset(-0.1966F, 3.063F, -3.0439F));

		PartDefinition cube_r62 = bone3.addOrReplaceChild("cube_r62", CubeListBuilder.create().texOffs(12, 72).addBox(-1.0F, -1.25F, -0.5F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.3054F));

		PartDefinition cube_r63 = bone3.addOrReplaceChild("cube_r63", CubeListBuilder.create().texOffs(86, 82).addBox(-1.0F, -1.25F, -0.5F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.3933F, 0.0F, 0.0F, -0.3927F, 0.0F, -0.3054F));

		PartDefinition cube_r64 = bone3.addOrReplaceChild("cube_r64", CubeListBuilder.create().texOffs(51, 0).addBox(-1.0F, -1.7F, -1.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.1966F, -0.763F, 0.5439F, -0.3054F, 0.0F, 0.0F));

		PartDefinition skirt = body.addOrReplaceChild("skirt", CubeListBuilder.create().texOffs(82, 66).addBox(10.1644F, -10.5423F, -0.0697F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(34, 47).addBox(2.6644F, -10.5423F, -0.0697F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(32, 26).addBox(2.9042F, -10.5423F, 3.0803F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(24, 24).addBox(2.9042F, -10.5423F, -0.1697F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.9052F, 18.6225F, -1.949F));

		PartDefinition cube_r65 = skirt.addOrReplaceChild("cube_r65", CubeListBuilder.create().texOffs(116, 63).addBox(-2.0F, 0.0F, -4.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(116, 60).addBox(-2.0F, 0.0F, -2.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(118, 27).addBox(-2.0F, -10.0F, -4.0F, 1.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(13.8104F, -1.0F, 3.898F, 0.0F, 0.0F, -0.2182F));

		PartDefinition cube_r66 = skirt.addOrReplaceChild("cube_r66", CubeListBuilder.create().texOffs(116, 57).addBox(-0.9763F, -0.2164F, -4.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(110, 24).addBox(-0.9763F, -10.2164F, -4.0F, 1.0F, 10.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(12.8578F, -1.9613F, 7.8049F, 0.3414F, 0.0741F, -0.2054F));

		PartDefinition cube_r67 = skirt.addOrReplaceChild("cube_r67", CubeListBuilder.create().texOffs(122, 49).addBox(-0.0237F, -0.2164F, -4.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(110, 11).addBox(-0.0237F, -10.2164F, -4.0F, 1.0F, 10.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.9526F, -1.9613F, 7.8049F, 0.3414F, -0.0741F, 0.2054F));

		PartDefinition cube_r68 = skirt.addOrReplaceChild("cube_r68", CubeListBuilder.create().texOffs(122, 41).addBox(1.0F, 0.0F, -2.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(114, 37).addBox(0.0F, 0.0F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(98, 11).addBox(0.0F, -10.0F, -2.0F, 2.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.9052F, -1.5545F, 7.9529F, 0.3414F, 0.0741F, -0.2054F));

		PartDefinition cube_r69 = skirt.addOrReplaceChild("cube_r69", CubeListBuilder.create().texOffs(114, 41).addBox(-1.0F, 0.0F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(122, 47).addBox(-3.0F, 0.0F, -2.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(104, 11).addBox(-2.0F, -10.0F, -2.0F, 2.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.9052F, -1.5545F, 7.9529F, 0.3414F, -0.0741F, 0.2054F));

		PartDefinition cube_r70 = skirt.addOrReplaceChild("cube_r70", CubeListBuilder.create().texOffs(122, 43).addBox(0.0F, 0.0F, -2.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(122, 45).addBox(-2.0F, 0.0F, -2.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(118, 41).addBox(2.0F, 0.0F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(114, 39).addBox(-3.0F, 0.0F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(96, 22).addBox(-3.0F, -10.0F, -2.0F, 6.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.9052F, -1.5545F, 7.9529F, 0.3492F, 0.0F, 0.0F));

		PartDefinition cube_r71 = skirt.addOrReplaceChild("cube_r71", CubeListBuilder.create().texOffs(122, 52).addBox(1.0F, 0.0F, 2.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(122, 55).addBox(1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(118, 13).addBox(1.0F, -10.0F, 0.0F, 1.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 0.0F, 0.0F, 0.0F, 0.2182F));

		PartDefinition cube_r72 = skirt.addOrReplaceChild("cube_r72", CubeListBuilder.create().texOffs(122, 63).addBox(-0.9763F, -0.2164F, 2.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(102, 33).addBox(-0.9763F, -10.2164F, 1.0F, 1.0F, 10.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(12.8578F, -1.9613F, -3.9068F, -0.3414F, -0.0741F, -0.2054F));

		PartDefinition cube_r73 = skirt.addOrReplaceChild("cube_r73", CubeListBuilder.create().texOffs(122, 58).addBox(-0.0237F, -0.2164F, 2.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(116, 0).addBox(-0.0237F, -10.2164F, 1.0F, 1.0F, 10.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.9526F, -1.9613F, -3.9068F, -0.3414F, 0.0741F, 0.2054F));

		PartDefinition cube_r74 = skirt.addOrReplaceChild("cube_r74", CubeListBuilder.create().texOffs(103, 48).addBox(0.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(107, 50).addBox(1.0F, 0.0F, 1.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(104, 0).addBox(0.0F, -10.0F, 1.0F, 2.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.9052F, -1.5545F, -4.0549F, -0.3414F, -0.0741F, -0.2054F));

		PartDefinition cube_r75 = skirt.addOrReplaceChild("cube_r75", CubeListBuilder.create().texOffs(110, 44).addBox(-1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(122, 61).addBox(-3.0F, 0.0F, 1.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(110, 0).addBox(-2.0F, -10.0F, 1.0F, 2.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.9052F, -1.5545F, -4.0549F, -0.3414F, 0.0741F, 0.2054F));

		PartDefinition cube_r76 = skirt.addOrReplaceChild("cube_r76", CubeListBuilder.create().texOffs(107, 48).addBox(0.0F, 0.0F, 1.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(107, 46).addBox(-2.0F, 0.0F, 1.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(114, 43).addBox(-3.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(103, 46).addBox(2.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(90, 0).addBox(-3.0F, -10.0F, 1.0F, 6.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.9052F, -1.5545F, -4.0549F, -0.3492F, 0.0F, 0.0F));

		PartDefinition r_arm = partdefinition.addOrReplaceChild("rightArm", CubeListBuilder.create().texOffs(57, 50).addBox(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(39, 47).addBox(-2.5F, -2.25F, -2.5F, 4.0F, 13.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 2.0F, 0.0F));

		PartDefinition bone2 = r_arm.addOrReplaceChild("bone2", CubeListBuilder.create(), PartPose.offset(1.5F, 9.0882F, 4.552F));

		PartDefinition cube_r77 = bone2.addOrReplaceChild("cube_r77", CubeListBuilder.create().texOffs(62, 14).addBox(1.0F, -6.0F, -2.001F, 0.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(66, 24).addBox(1.0F, -6.0F, -2.001F, 0.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 88).addBox(0.0F, -6.0F, -0.001F, 1.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3451F, 0.0535F, -0.1477F));

		PartDefinition cube_r78 = bone2.addOrReplaceChild("cube_r78", CubeListBuilder.create().texOffs(70, 1).addBox(-0.001F, -6.0F, 0.0F, 0.0F, 6.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0597F, 0.5493F, -6.8578F, -0.004F, -0.0163F, -0.1475F));

		PartDefinition cube_r79 = bone2.addOrReplaceChild("cube_r79", CubeListBuilder.create().texOffs(82, 86).addBox(0.0464F, -5.9764F, -1.0301F, 0.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(88, 9).addBox(-0.9536F, -5.9764F, -1.0301F, 1.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0096F, 0.5372F, -6.8254F, -0.1268F, -0.0102F, -0.1533F));

		PartDefinition cube_r80 = bone2.addOrReplaceChild("cube_r80", CubeListBuilder.create().texOffs(12, 72).addBox(-1.0F, -6.0F, -5.001F, 0.0F, 6.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0001F, 0.6844F, -1.8793F, -0.004F, -0.0099F, 0.1476F));

		PartDefinition cube_r81 = bone2.addOrReplaceChild("cube_r81", CubeListBuilder.create().texOffs(24, 33).addBox(-0.0464F, -5.9764F, -1.0301F, 0.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(86, 87).addBox(-0.0464F, -5.9764F, -1.0301F, 1.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.8869F, 0.5127F, -6.8596F, -0.1268F, 0.0102F, 0.1533F));

		PartDefinition cube_r82 = bone2.addOrReplaceChild("cube_r82", CubeListBuilder.create().texOffs(0, 18).addBox(0.0F, -6.0F, -0.0002F, 4.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.9385F, 0.5512F, -7.8942F, -0.1269F, -0.0086F, 0.0061F));

		PartDefinition cube_r83 = bone2.addOrReplaceChild("cube_r83", CubeListBuilder.create().texOffs(67, 30).addBox(-1.0F, -6.0F, -2.001F, 0.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(2, 88).addBox(-1.0F, -6.0F, -0.001F, 1.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, 0.0F, 0.0F, 0.3451F, -0.0535F, 0.1477F));

		PartDefinition cube_r84 = bone2.addOrReplaceChild("cube_r84", CubeListBuilder.create().texOffs(83, 50).addBox(-2.0F, -6.0F, 0.0F, 4.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 0.0003F, -0.0009F, 0.3491F, 0.0F, 0.0F));

		PartDefinition l_arm = partdefinition.addOrReplaceChild("leftArm", CubeListBuilder.create().texOffs(57, 50).mirror().addBox(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(39, 47).mirror().addBox(-1.5F, -2.25F, -2.5F, 4.0F, 13.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(5.0F, 2.0F, 0.0F));

		PartDefinition bone = l_arm.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(-1.5F, 9.0882F, 4.552F));

		PartDefinition cube_r85 = bone.addOrReplaceChild("cube_r85", CubeListBuilder.create().texOffs(70, 1).addBox(0.001F, -6.0F, 0.0F, 0.0F, 6.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0597F, 0.5493F, -6.8578F, -0.004F, 0.0163F, 0.1475F));

		PartDefinition cube_r86 = bone.addOrReplaceChild("cube_r86", CubeListBuilder.create().texOffs(82, 86).addBox(-0.0464F, -5.9764F, -1.0301F, 0.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(88, 9).addBox(-0.0464F, -5.9764F, -1.0301F, 1.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0096F, 0.5372F, -6.8254F, -0.1268F, 0.0102F, 0.1533F));

		PartDefinition cube_r87 = bone.addOrReplaceChild("cube_r87", CubeListBuilder.create().texOffs(12, 72).addBox(1.0F, -6.0F, -5.001F, 0.0F, 6.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0001F, 0.6844F, -1.8793F, -0.004F, 0.0099F, -0.1476F));

		PartDefinition cube_r88 = bone.addOrReplaceChild("cube_r88", CubeListBuilder.create().texOffs(24, 33).addBox(0.0464F, -5.9764F, -1.0301F, 0.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(86, 87).addBox(-0.9536F, -5.9764F, -1.0301F, 1.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.8869F, 0.5127F, -6.8596F, -0.1268F, -0.0102F, -0.1533F));

		PartDefinition cube_r89 = bone.addOrReplaceChild("cube_r89", CubeListBuilder.create().texOffs(0, 18).mirror().addBox(-4.0F, -6.0F, -0.0002F, 4.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(3.9385F, 0.5512F, -7.3942F, -0.1269F, 0.0086F, -0.0061F));

		PartDefinition cube_r90 = bone.addOrReplaceChild("cube_r90", CubeListBuilder.create().texOffs(66, 24).mirror().addBox(-1.0F, -6.0F, -2.001F, 0.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 88).addBox(-1.0F, -6.0F, -0.001F, 1.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3451F, -0.0535F, 0.1477F));

		PartDefinition cube_r91 = bone.addOrReplaceChild("cube_r91", CubeListBuilder.create().texOffs(67, 30).addBox(1.0F, -6.0F, -2.001F, 0.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(2, 88).addBox(0.0F, -6.0F, -0.001F, 1.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, 0.0F, 0.0F, 0.3451F, 0.0535F, -0.1477F));

		PartDefinition cube_r92 = bone.addOrReplaceChild("cube_r92", CubeListBuilder.create().texOffs(83, 50).addBox(-2.0F, -6.0F, 0.0F, 4.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 0.0003F, -0.0009F, 0.3491F, 0.0F, 0.0F));

		PartDefinition r_leg = partdefinition.addOrReplaceChild("rightLeg", CubeListBuilder.create().texOffs(55, 34).addBox(-2.1F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(19, 47).addBox(-2.6F, -0.5F, -2.5F, 5.0F, 13.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.9F, 12.0F, 0.0F));

		PartDefinition l_leg = partdefinition.addOrReplaceChild("leftLeg", CubeListBuilder.create().texOffs(50, 18).addBox(-1.9F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(36, 0).addBox(-2.4F, -0.5F, -2.5F, 5.0F, 13.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(1.9F, 12.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	public static LayerDefinition createHatLayer() {
		MeshDefinition meshdefinition = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
		PartDefinition head = meshdefinition.getRoot().getChild("head");

		PartDefinition r_r = head.addOrReplaceChild("r_r", CubeListBuilder.create(), PartPose.offset(-9.1648F, -4.4215F, -4.6F));
		{
			PartDefinition cube_r1 = r_r.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(38, 2).addBox(-1.0F, -3.0F, -2.0F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
					.texOffs(38, 3).addBox(-1.0F, -1.0F, -2.5F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
					.texOffs(38, 4).addBox(-1.0F, 1.0F, -2.5F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
					.texOffs(23, 46).addBox(-1.0F, 3.0F, -2.5F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
					.texOffs(47, 22).addBox(-1.0F, 5.0F, -2.5F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
					.texOffs(26, 1).addBox(3.0F, -3.0F, -2.0F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
					.texOffs(26, 5).addBox(3.0F, -1.0F, -2.5F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
					.texOffs(26, 6).addBox(3.0F, 1.0F, -2.5F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
					.texOffs(32, 5).addBox(3.0F, 3.0F, -2.5F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
					.texOffs(32, 6).addBox(3.0F, 5.0F, -2.5F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.5964F, -4.5245F, 12.6826F, 0.3736F, -0.1393F, 0.2727F));

			PartDefinition cube_r2 = r_r.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(76, 12).addBox(-4.1513F, -1.7307F, 5.03F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
					.texOffs(2, 69).addBox(-4.1513F, 0.2693F, 4.03F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
					.texOffs(69, 52).addBox(-4.1513F, 2.2693F, 5.03F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
					.texOffs(70, 0).addBox(-4.1513F, 4.2693F, 4.03F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
					.texOffs(74, 70).addBox(-0.1513F, -1.7307F, 5.03F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
					.texOffs(62, 68).addBox(-0.1513F, 0.2693F, 4.03F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
					.texOffs(64, 68).addBox(-0.1513F, 2.2693F, 5.03F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
					.texOffs(0, 69).addBox(-0.1513F, 4.2693F, 4.03F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.1532F, -2.282F, 6.6398F, 0.3736F, -0.1393F, 0.2727F));

			PartDefinition cube_r3 = r_r.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(79, 40).addBox(-4.1513F, 6.2693F, 4.03F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
					.texOffs(79, 29).addBox(-0.1513F, 6.2693F, 4.03F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.1273F, -2.6692F, 7.5614F, 0.3736F, -0.1393F, 0.2727F));

			PartDefinition cube_r4 = r_r.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(79, 42).addBox(-1.0F, 4.0F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.391F, -2.8023F, 13.4286F, 0.0F, -0.0436F, 0.1309F));

			PartDefinition cube_r5 = r_r.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(51, 48).addBox(-1.0F, 5.0F, -2.0F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.3476F, -2.807F, 14.4286F, 0.0F, -0.0436F, 0.1309F));

			PartDefinition cube_r6 = r_r.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(71, 42).addBox(-1.0F, 3.0F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.1732F, -0.8137F, 12.4295F, 0.0F, -0.0436F, 0.1309F));

			PartDefinition cube_r7 = r_r.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(51, 49).addBox(0.0F, 3.0F, -2.0F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.8349F, 1.0294F, 14.385F, 0.0F, -0.0436F, 0.1309F));

			PartDefinition cube_r8 = r_r.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(71, 54).addBox(-1.0F, 3.0F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.8255F, 1.1588F, 13.4286F, 0.0F, -0.0436F, 0.1309F));

			PartDefinition cube_r9 = r_r.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(16, 52).addBox(-1.0F, 5.0F, -1.0F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
					.texOffs(83, 27).addBox(0.0F, 0.0F, -2.0F, 3.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.8255F, 1.1588F, 13.9286F, 0.0F, -0.0436F, 0.1309F));

			PartDefinition cube_r10 = r_r.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(16, 53).addBox(-1.0F, 5.0F, -1.0F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.7874F, 1.6814F, 14.1031F, 0.0F, -0.0436F, 0.1309F));

			PartDefinition cube_r11 = r_r.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(71, 62).addBox(-1.0F, 3.0F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.7874F, 1.6814F, 13.6031F, 0.0F, -0.0436F, 0.1309F));

			PartDefinition cube_r12 = r_r.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(72, 26).addBox(-1.0F, 3.0F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.1351F, -0.2911F, 12.604F, 0.0F, -0.0436F, 0.1309F));

			PartDefinition cube_r13 = r_r.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(56, 1).addBox(-1.0F, 5.0F, -2.0F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.3095F, -2.2844F, 14.6031F, 0.0F, -0.0436F, 0.1309F));

			PartDefinition cube_r14 = r_r.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(79, 43).addBox(-1.0F, 4.0F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.3529F, -2.2797F, 13.603F, 0.0F, -0.0436F, 0.1309F));

			PartDefinition cube_r15 = r_r.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(78, 10).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.0045F, -5.6155F, 9.0403F, 0.3736F, -0.1393F, 0.2727F));

			PartDefinition cube_r16 = r_r.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(36, 28).addBox(-8.9449F, -0.1269F, 0.6F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.9643F, -5.4984F, 7.0594F, -0.2849F, 0.274F, -0.8249F));

			PartDefinition cube_r17 = r_r.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(40, 49).addBox(-8.9449F, -0.1269F, -0.9F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.6079F, -4.0353F, 7.462F, -0.2849F, 0.274F, -0.8249F));

			PartDefinition cube_r18 = r_r.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(16, 54).addBox(0.0F, 3.0F, -2.0F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.7967F, 1.552F, 14.5595F, 0.0F, -0.0436F, 0.1309F));

			PartDefinition cube_r19 = r_r.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(1, 50).addBox(-8.9449F, -0.1269F, 0.1F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
					.texOffs(1, 51).addBox(-8.9449F, 1.8731F, -0.4F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
					.texOffs(14, 52).addBox(-8.9449F, -0.1269F, 0.6F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.0401F, -6.9134F, 8.5256F, -0.2849F, 0.274F, -0.8249F));

			PartDefinition cube_r20 = r_r.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(43, 20).addBox(1.0F, 2.0F, -1.0F, 1.0F, 0.0F, 2.0F, new CubeDeformation(0.0F))
					.texOffs(84, 62).addBox(1.0F, 0.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
					.texOffs(80, 6).addBox(2.0F, 1.0F, -1.0F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.6014F, -1.9074F, 8.7F, 0.0F, 0.0F, -0.3054F));

			PartDefinition cube_r21 = r_r.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(51, 3).addBox(-1.0F, 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.6425F, -3.6293F, 8.1F, 0.0F, 0.0F, 0.1309F));

			PartDefinition cube_r22 = r_r.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(51, 4).addBox(-1.0F, 0.0F, -1.0F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.3816F, -1.6474F, 9.101F, 0.0F, 0.0F, 0.1309F));

			PartDefinition cube_r23 = r_r.addOrReplaceChild("cube_r23", CubeListBuilder.create().texOffs(34, 50).addBox(-1.2F, 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.1017F, -5.5851F, 8.101F, 0.0F, 0.0F, 0.1309F));

			PartDefinition cube_r24 = r_r.addOrReplaceChild("cube_r24", CubeListBuilder.create().texOffs(40, 50).addBox(-1.0F, 0.0F, -1.0F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
					.texOffs(67, 0).addBox(-1.0F, 0.0F, -0.5F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.1646F, -7.5951F, 8.6F, 0.0F, 0.0F, 0.1309F));

			PartDefinition cube_r25 = r_r.addOrReplaceChild("cube_r25", CubeListBuilder.create().texOffs(56, 36).addBox(-1.0F, 0.0F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.6427F, -3.6303F, 8.1F, 0.0F, 0.0F, 0.1309F));

			PartDefinition cube_r26 = r_r.addOrReplaceChild("cube_r26", CubeListBuilder.create().texOffs(57, 16).addBox(-1.0F, 0.0F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.9036F, -5.6122F, 9.1F, 0.0F, 0.0F, 0.1309F));

			PartDefinition cube_r27 = r_r.addOrReplaceChild("cube_r27", CubeListBuilder.create().texOffs(71, 62).addBox(-5.0F, 0.0F, -1.0F, 5.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.1219F, -6.9424F, 8.6F, 0.0F, 0.0F, 0.1309F));

			PartDefinition cube_r28 = r_r.addOrReplaceChild("cube_r28", CubeListBuilder.create().texOffs(70, 0).addBox(-8.0F, 0.0F, -1.0F, 8.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.1219F, -6.9424F, 8.6F, -0.2802F, 0.2789F, -0.8075F));
		}
		PartDefinition l_r = head.addOrReplaceChild("l_r", CubeListBuilder.create(), PartPose.offset(9.1648F, -4.4215F, -4.6F));
		{
			PartDefinition cube_r29 = l_r.addOrReplaceChild("cube_r29", CubeListBuilder.create().texOffs(59, 17).addBox(0.0F, 5.0F, -1.0F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.7874F, 1.6814F, 14.1031F, 0.0F, 0.0436F, -0.1309F));

			PartDefinition cube_r30 = l_r.addOrReplaceChild("cube_r30", CubeListBuilder.create().texOffs(73, 40).addBox(0.0F, 3.0F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.7874F, 1.6814F, 13.6031F, 0.0F, 0.0436F, -0.1309F));

			PartDefinition cube_r31 = l_r.addOrReplaceChild("cube_r31", CubeListBuilder.create().texOffs(59, 16).addBox(-1.0F, 3.0F, -2.0F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.7967F, 1.552F, 14.5595F, 0.0F, 0.0436F, -0.1309F));

			PartDefinition cube_r32 = l_r.addOrReplaceChild("cube_r32", CubeListBuilder.create().texOffs(72, 28).addBox(0.0F, 3.0F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.1351F, -0.2911F, 12.604F, 0.0F, 0.0436F, -0.1309F));

			PartDefinition cube_r33 = l_r.addOrReplaceChild("cube_r33", CubeListBuilder.create().texOffs(56, 2).addBox(0.0F, 5.0F, -2.0F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.3095F, -2.2844F, 14.6031F, 0.0F, 0.0436F, -0.1309F));

			PartDefinition cube_r34 = l_r.addOrReplaceChild("cube_r34", CubeListBuilder.create().texOffs(80, 6).addBox(0.0F, 4.0F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.3529F, -2.2797F, 13.603F, 0.0F, 0.0436F, -0.1309F));

			PartDefinition cube_r35 = l_r.addOrReplaceChild("cube_r35", CubeListBuilder.create().texOffs(51, 47).addBox(0.0F, 5.0F, -2.0F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.3476F, -2.807F, 14.4286F, 0.0F, 0.0436F, -0.1309F));

			PartDefinition cube_r36 = l_r.addOrReplaceChild("cube_r36", CubeListBuilder.create().texOffs(79, 28).addBox(-0.8487F, 6.2693F, 4.03F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
					.texOffs(79, 41).addBox(3.1513F, 6.2693F, 4.03F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.1273F, -2.6692F, 7.5614F, 0.3736F, 0.1393F, -0.2727F));

			PartDefinition cube_r37 = l_r.addOrReplaceChild("cube_r37", CubeListBuilder.create().texOffs(26, 0).addBox(-4.0F, 5.0F, -2.5F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
					.texOffs(23, 19).addBox(-4.0F, 3.0F, -2.5F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
					.texOffs(23, 18).addBox(-4.0F, 1.0F, -2.5F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
					.texOffs(5, 1).addBox(-4.0F, -1.0F, -2.5F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
					.texOffs(5, 0).addBox(-4.0F, -3.0F, -2.0F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
					.texOffs(47, 23).addBox(0.0F, -3.0F, -2.0F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
					.texOffs(47, 28).addBox(0.0F, -1.0F, -2.5F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
					.texOffs(51, 18).addBox(0.0F, 1.0F, -2.5F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
					.texOffs(51, 19).addBox(0.0F, 3.0F, -2.5F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
					.texOffs(17, 66).addBox(0.0F, 5.0F, -2.5F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.5964F, -4.5245F, 12.6826F, 0.3736F, 0.1393F, -0.2727F));

			PartDefinition cube_r38 = l_r.addOrReplaceChild("cube_r38", CubeListBuilder.create().texOffs(60, 68).addBox(-0.8487F, 4.2693F, 4.03F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
					.texOffs(58, 68).addBox(-0.8487F, 2.2693F, 5.03F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
					.texOffs(52, 68).addBox(-0.8487F, 0.2693F, 4.03F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
					.texOffs(74, 14).addBox(-0.8487F, -1.7307F, 5.03F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
					.texOffs(76, 14).addBox(3.1513F, -1.7307F, 5.03F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
					.texOffs(70, 28).addBox(3.1513F, 2.2693F, 5.03F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
					.texOffs(70, 26).addBox(3.1513F, 0.2693F, 4.03F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
					.texOffs(71, 40).addBox(3.1513F, 4.2693F, 4.03F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.1532F, -2.282F, 6.6398F, 0.3736F, 0.1393F, -0.2727F));

			PartDefinition cube_r39 = l_r.addOrReplaceChild("cube_r39", CubeListBuilder.create().texOffs(51, 66).addBox(0.0F, 5.0F, -1.0F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
					.texOffs(83, 27).addBox(-3.0F, 0.0F, -2.0F, 3.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.8255F, 1.1588F, 13.9286F, 0.0F, 0.0436F, -0.1309F));

			PartDefinition cube_r40 = l_r.addOrReplaceChild("cube_r40", CubeListBuilder.create().texOffs(80, 7).addBox(0.0F, 4.0F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.391F, -2.8023F, 13.4286F, 0.0F, 0.0436F, -0.1309F));

			PartDefinition cube_r41 = l_r.addOrReplaceChild("cube_r41", CubeListBuilder.create().texOffs(73, 42).addBox(0.0F, 3.0F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.1732F, -0.8137F, 12.4295F, 0.0F, 0.0436F, -0.1309F));

			PartDefinition cube_r42 = l_r.addOrReplaceChild("cube_r42", CubeListBuilder.create().texOffs(74, 12).addBox(0.0F, 3.0F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.8255F, 1.1588F, 13.4286F, 0.0F, 0.0436F, -0.1309F));

			PartDefinition cube_r43 = l_r.addOrReplaceChild("cube_r43", CubeListBuilder.create().texOffs(17, 65).addBox(-1.0F, 3.0F, -2.0F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.8349F, 1.0294F, 14.385F, 0.0F, 0.0436F, -0.1309F));

			PartDefinition cube_r44 = l_r.addOrReplaceChild("cube_r44", CubeListBuilder.create().texOffs(78, 10).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0045F, -5.6155F, 9.0403F, 0.3736F, 0.1393F, -0.2727F));

			PartDefinition cube_r45 = l_r.addOrReplaceChild("cube_r45", CubeListBuilder.create().texOffs(39, 28).addBox(7.9449F, -0.1269F, 0.6F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.9643F, -5.4984F, 7.0594F, -0.2849F, -0.274F, 0.8249F));

			PartDefinition cube_r46 = l_r.addOrReplaceChild("cube_r46", CubeListBuilder.create().texOffs(43, 28).addBox(7.9449F, -0.1269F, 0.1F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.6079F, -4.0353F, 7.462F, -0.2849F, -0.274F, 0.8249F));

			PartDefinition cube_r47 = l_r.addOrReplaceChild("cube_r47", CubeListBuilder.create().texOffs(33, 47).addBox(7.9449F, -0.1269F, -0.9F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
					.texOffs(32, 65).addBox(7.9449F, -0.1269F, -0.4F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.0401F, -6.9134F, 8.5256F, -0.2849F, -0.274F, 0.8249F));

			PartDefinition cube_r48 = l_r.addOrReplaceChild("cube_r48", CubeListBuilder.create().texOffs(55, 34).addBox(7.9449F, -0.1269F, -0.4F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.7286F, -5.4991F, 8.9065F, -0.2849F, -0.274F, 0.8249F));

			PartDefinition cube_r49 = l_r.addOrReplaceChild("cube_r49", CubeListBuilder.create().texOffs(80, 6).addBox(-5.0F, 1.0F, -1.0F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
					.texOffs(43, 18).addBox(-2.0F, 2.0F, -1.0F, 1.0F, 0.0F, 2.0F, new CubeDeformation(0.0F))
					.texOffs(15, 50).addBox(-3.0F, 0.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.6014F, -1.9074F, 8.7F, 0.0F, 0.0F, 0.3054F));

			PartDefinition cube_r50 = l_r.addOrReplaceChild("cube_r50", CubeListBuilder.create().texOffs(39, 47).addBox(0.0F, 0.0F, -1.0F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.6425F, -3.6293F, 9.1F, 0.0F, 0.0F, -0.1309F));

			PartDefinition cube_r51 = l_r.addOrReplaceChild("cube_r51", CubeListBuilder.create().texOffs(33, 48).addBox(0.0F, 0.0F, 0.0F, 2.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.1646F, -1.5951F, 8.6F, 0.0F, 0.0F, -0.1309F));

			PartDefinition cube_r52 = l_r.addOrReplaceChild("cube_r52", CubeListBuilder.create().texOffs(39, 48).addBox(0.0F, 0.0F, -1.0F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.9035F, -5.6112F, 9.1F, 0.0F, 0.0F, -0.1309F));

			PartDefinition cube_r53 = l_r.addOrReplaceChild("cube_r53", CubeListBuilder.create().texOffs(33, 49).addBox(0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
					.texOffs(48, 65).addBox(0.0F, 0.0F, 0.5F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.1646F, -7.5951F, 8.6F, 0.0F, 0.0F, -0.1309F));

			PartDefinition cube_r54 = l_r.addOrReplaceChild("cube_r54", CubeListBuilder.create().texOffs(56, 50).addBox(0.0F, 0.0F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.6427F, -3.6303F, 9.099F, 0.0F, 0.0F, -0.1309F));

			PartDefinition cube_r55 = l_r.addOrReplaceChild("cube_r55", CubeListBuilder.create().texOffs(57, 52).addBox(0.0F, 0.0F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.9036F, -5.6122F, 8.1F, 0.0F, 0.0F, -0.1309F));

			PartDefinition cube_r56 = l_r.addOrReplaceChild("cube_r56", CubeListBuilder.create().texOffs(71, 62).mirror().addBox(0.0F, 0.0F, -1.0F, 5.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-7.1219F, -6.9424F, 8.6F, 0.0F, 0.0F, -0.1309F));

			PartDefinition cube_r57 = l_r.addOrReplaceChild("cube_r57", CubeListBuilder.create().texOffs(70, 0).mirror().addBox(0.0F, 0.0F, -1.0F, 8.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-7.1219F, -6.9424F, 8.6F, -0.2802F, -0.2789F, 0.8075F));
		}
		head.addOrReplaceChild("bone4", CubeListBuilder.create().texOffs(8, 68).addBox(-5.0379F, -17.506F, -2.2F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(3.6027F, 7.5845F, 3.5F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}


	private final ModelPart root;
	private final ModelPart head;
	private final ModelPart rightArm;
	private final ModelPart leftArm;
	private final ModelPart rightLeg;
	private final ModelPart leftLeg;

	public ReimuModel(ModelPart root) {
		this.root = root;
		this.head = root.getChild("head");
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

}