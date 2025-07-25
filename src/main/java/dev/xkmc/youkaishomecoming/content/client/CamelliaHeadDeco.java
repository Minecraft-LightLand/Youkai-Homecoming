package dev.xkmc.youkaishomecoming.content.client;// Made with Blockbench 4.12.5
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class CamelliaHeadDeco {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(YoukaisHomecoming.loc("camellia"), "main");


	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
		PartDefinition head = meshdefinition.getRoot().getChild("head");

		PartDefinition camellia = head.addOrReplaceChild("camellia", CubeListBuilder.create().texOffs(0, 0).addBox(4.45F, -7.25F, -2.36F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.15F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition camellia_r1 = camellia.addOrReplaceChild("camellia_r1", CubeListBuilder.create().texOffs(0, 9).addBox(-0.3F, -1.7F, -0.3F, 1.0F, 2.0F, 2.0F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(4.45F, -6.75F, -1.86F, -0.6032F, 0.4127F, -0.2774F));

		PartDefinition camellia_r2 = camellia.addOrReplaceChild("camellia_r2", CubeListBuilder.create().texOffs(0, 5).addBox(-0.3F, -1.7F, -0.3F, 1.0F, 2.0F, 2.0F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(4.45F, -6.75F, -1.86F, -1.7628F, 0.1621F, -0.4394F));

		PartDefinition camellia_r3 = camellia.addOrReplaceChild("camellia_r3", CubeListBuilder.create().texOffs(6, 9).addBox(-0.3F, -1.7F, -0.3F, 1.0F, 2.0F, 2.0F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(4.45F, -6.75F, -1.86F, 0.7713F, 0.4052F, -0.0295F));

		PartDefinition camellia_r4 = camellia.addOrReplaceChild("camellia_r4", CubeListBuilder.create().texOffs(4, 13).addBox(-0.3F, -1.7F, -0.3F, 1.0F, 2.0F, 2.0F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(4.45F, -6.75F, -1.86F, 2.0342F, 0.0704F, -0.0142F));

		PartDefinition camellia_r5 = camellia.addOrReplaceChild("camellia_r5", CubeListBuilder.create().texOffs(10, 13).addBox(-0.3F, -1.7F, -0.3F, 1.0F, 2.0F, 2.0F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(4.45F, -6.75F, -1.86F, -3.0064F, 0.0739F, -0.3701F));

		PartDefinition leaf = head.addOrReplaceChild("leaf", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition leaf_r1 = leaf.addOrReplaceChild("leaf_r1", CubeListBuilder.create().texOffs(0, 22).addBox(0.0F, -2.0F, -2.0F, 1.0F, 2.0F, 3.0F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(3.74F, -5.5F, -1.86F, 0.609F, -0.0436F, -0.0437F));

		PartDefinition leaf_r2 = leaf.addOrReplaceChild("leaf_r2", CubeListBuilder.create().texOffs(0, 22).addBox(-0.7F, -1.0F, -1.5F, 1.0F, 2.0F, 3.0F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(4.7944F, -5.5124F, -1.8248F, 1.2858F, 0.0294F, -0.3466F));

		PartDefinition band = head.addOrReplaceChild("band", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition band_r1 = band.addOrReplaceChild("band_r1", CubeListBuilder.create().texOffs(12, 4).addBox(-0.5F, -0.5F, -1.5F, 1.0F, 1.0F, 3.0F, new CubeDeformation(-0.2F))
				.texOffs(11, 4).addBox(-0.5F, -0.5F, 0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.2F)), PartPose.offsetAndRotation(4.5F, -6.3F, -0.1F, 0.22F, 0.1278F, 0.0285F));

		PartDefinition band_r2 = band.addOrReplaceChild("band_r2", CubeListBuilder.create().texOffs(13, 14).addBox(-0.5F, 0.3F, -1.8F, 1.0F, 1.0F, 3.0F, new CubeDeformation(-0.2F)), PartPose.offsetAndRotation(4.495F, -6.2824F, -0.1349F, 0.0018F, 0.1278F, 0.0285F));

		PartDefinition band_r3 = band.addOrReplaceChild("band_r3", CubeListBuilder.create().texOffs(4, 0).addBox(-0.5F, -0.3F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.22F)), PartPose.offsetAndRotation(4.7358F, -3.482F, -2.1353F, -0.2451F, 0.0865F, -0.2511F));

		PartDefinition band_r4 = band.addOrReplaceChild("band_r4", CubeListBuilder.create().texOffs(4, 0).addBox(-0.5F, -0.3F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.21F)), PartPose.offsetAndRotation(4.7358F, -3.482F, -2.1353F, -0.0706F, 0.0865F, -0.2511F));

		PartDefinition band_r5 = band.addOrReplaceChild("band_r5", CubeListBuilder.create().texOffs(0, 13).addBox(-0.5F, -2.0F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(-0.2F)), PartPose.offsetAndRotation(4.55F, -4.25F, -2.01F, -0.1579F, 0.0865F, -0.2511F));

		PartDefinition band_r6 = band.addOrReplaceChild("band_r6", CubeListBuilder.create().texOffs(8, 0).addBox(-0.5F, -0.3F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.21F)), PartPose.offsetAndRotation(4.6311F, -3.475F, -0.9287F, 0.1103F, -0.3218F, -0.182F));

		PartDefinition band_r7 = band.addOrReplaceChild("band_r7", CubeListBuilder.create().texOffs(8, 0).addBox(-0.5F, -0.3F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.22F)), PartPose.offsetAndRotation(4.6311F, -3.475F, -0.9287F, 0.2848F, -0.3218F, -0.182F));

		PartDefinition band_r8 = band.addOrReplaceChild("band_r8", CubeListBuilder.create().texOffs(6, 17).addBox(-0.5F, -2.0F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(-0.2F)), PartPose.offsetAndRotation(4.55F, -4.25F, -1.11F, 0.2412F, -0.3218F, -0.182F));

		PartDefinition band_r9 = band.addOrReplaceChild("band_r9", CubeListBuilder.create().texOffs(12, 8).addBox(-1.0F, -3.0F, -1.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(-0.2F)), PartPose.offsetAndRotation(4.75F, -6.5F, -1.86F, -0.8578F, 0.1298F, -0.228F));

		PartDefinition band_r10 = band.addOrReplaceChild("band_r10", CubeListBuilder.create().texOffs(18, 8).addBox(0.0F, -3.0F, -1.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(-0.2F)), PartPose.offsetAndRotation(3.75F, -6.25F, -1.61F, 0.296F, -0.0507F, -0.2348F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

}