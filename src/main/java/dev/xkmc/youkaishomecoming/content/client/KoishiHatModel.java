package dev.xkmc.youkaishomecoming.content.client;// Made with Blockbench 4.9.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class KoishiHatModel {

	public static final ModelLayerLocation HAT = new ModelLayerLocation(YoukaisHomecoming.loc("koishi_hat"), "main");

	public static LayerDefinition createHat() {
		MeshDefinition meshdefinition = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
		PartDefinition partdefinition = meshdefinition.getRoot().getChild("head");

		PartDefinition hat = partdefinition.addOrReplaceChild("hat", CubeListBuilder.create()
						.texOffs(0, 14)
						.addBox(-10.0264F, -5.3562F, -9.925F, 20.0F, 0.1F, 20.0F)
						.texOffs(0, 0)
						.addBox(-5.0F, -9.0F, -5.0F, 10.0F, 4.0F, 10.0F),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

}