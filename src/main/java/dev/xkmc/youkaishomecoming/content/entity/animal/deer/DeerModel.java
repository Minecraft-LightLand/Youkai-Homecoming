package dev.xkmc.youkaishomecoming.content.entity.animal.deer;

import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class DeerModel extends HierarchicalModel<DeerEntity> {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(YoukaisHomecoming.loc("deer"), "main");

	public final ModelPart root, head;

	public DeerModel(ModelPart root) {
		this.root = root.getChild("root");
		var neck = this.root.getChild("neck");
		this.head = neck.getChild("head");
	}

	@Override
	public ModelPart root() {
		return root;
	}

	public void setupAnim(DeerEntity e, float limbSw, float limbSA, float tick, float yrot, float xrot) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.head.yRot = yrot * (float) (Math.PI / 180.0);
		this.head.xRot = xrot * (float) (Math.PI / 180.0);
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 9.0F, -8.0F));

		PartDefinition neck = root.addOrReplaceChild("neck", CubeListBuilder.create().texOffs(56, 27).addBox(-3.0F, -7.0F, -3.0F, 6.0F, 11.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.0F, 2.0F));

		PartDefinition head = neck.addOrReplaceChild("head", CubeListBuilder.create().texOffs(56, 13).addBox(-3.5F, -7.0F, -4.0F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -7.0F, -1.0F));

		PartDefinition horn = head.addOrReplaceChild("horn", CubeListBuilder.create(), PartPose.offset(0.0F, 9.0F, 11.0F));

		PartDefinition left_horn = horn.addOrReplaceChild("left_horn", CubeListBuilder.create().texOffs(72, 51).addBox(-1.0F, -2.25F, -1.25F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 56).addBox(-2.0F, -8.245F, -2.25F, 8.0F, 6.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, -16.175F, -9.75F, -0.1745F, 0.0F, 0.4363F));

		PartDefinition right_horn = horn.addOrReplaceChild("right_horn", CubeListBuilder.create().texOffs(44, 75).addBox(-1.0F, -2.25F, -1.25F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(56, 0).addBox(-6.0F, -8.245F, -2.25F, 8.0F, 6.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, -16.175F, -9.75F, -0.1745F, 0.0F, -0.4363F));

		PartDefinition ears = head.addOrReplaceChild("ears", CubeListBuilder.create(), PartPose.offset(7.0F, 16.0F, 12.0F));

		PartDefinition left_ear = ears.addOrReplaceChild("left_ear", CubeListBuilder.create().texOffs(72, 43).addBox(0.0F, -1.5F, -0.5F, 4.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.775F, -21.4F, -9.675F, 0.0F, -0.1309F, -0.1745F));

		PartDefinition right_ear = ears.addOrReplaceChild("right_ear", CubeListBuilder.create().texOffs(72, 47).addBox(-4.0F, -1.5F, -0.5F, 4.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.225F, -21.4F, -9.675F, 0.0F, 0.1309F, 0.1745F));

		PartDefinition mouth = head.addOrReplaceChild("mouth", CubeListBuilder.create(), PartPose.offset(1.0F, 18.0F, 11.7F));

		PartDefinition up_mouth = mouth.addOrReplaceChild("up_mouth", CubeListBuilder.create().texOffs(1, 70).addBox(-2.0F, -2.75F, -2.7F, 4.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, -20.25F, -16.0F));

		PartDefinition nose = up_mouth.addOrReplaceChild("nose", CubeListBuilder.create().texOffs(16, 78).addBox(-2.0F, -2.75F, -1.0F, 4.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -2.7F));

		PartDefinition down_mouth = mouth.addOrReplaceChild("down_mouth", CubeListBuilder.create().texOffs(16, 72).addBox(-2.0F, 0.25F, -3.7F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, -20.25F, -16.0F));

		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -3.0F, -8.0F, 10.0F, 10.0F, 18.0F, new CubeDeformation(0.0F))
				.texOffs(0, 28).addBox(-5.0F, -3.0F, -8.0F, 10.0F, 10.0F, 18.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, -4.0F, 9.0F));

		PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(32, 72).addBox(-2.0F, -5.0F, -1.0F, 4.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.025F, 10.1F));

		PartDefinition leg0 = root.addOrReplaceChild("leg0", CubeListBuilder.create().texOffs(30, 56).addBox(-1.375F, 0.0F, -1.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.625F, 3.0F, 17.0F));

		PartDefinition leg1 = root.addOrReplaceChild("leg1", CubeListBuilder.create().texOffs(56, 43).addBox(-3.0F, 0.0F, -1.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 3.0F, 17.0F));

		PartDefinition leg2 = root.addOrReplaceChild("leg2", CubeListBuilder.create().texOffs(46, 59).addBox(-1.0F, 0.0F, -1.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 3.0F, 2.0F));

		PartDefinition leg3 = root.addOrReplaceChild("leg3", CubeListBuilder.create().texOffs(62, 59).addBox(-3.0F, 0.0F, -1.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 3.0F, 2.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

}
