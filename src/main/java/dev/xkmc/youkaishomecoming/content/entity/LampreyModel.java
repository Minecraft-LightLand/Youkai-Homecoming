
package dev.xkmc.youkaishomecoming.content.entity;

import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class LampreyModel<T extends Entity> extends HierarchicalModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(YoukaisHomecoming.MODID, "lamprey"), "main");

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		partdefinition.addOrReplaceChild("front", CubeListBuilder.create().texOffs(1, 19).addBox(-1.0F, -1.75F, -9.025F, 2.0F, 3.0F, 9.0F, new CubeDeformation(0.0F)).texOffs(15, 0).addBox(-1.0F, -0.75F, -11.525F, 2.0F, 2.0F, 2.5F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 22.75F, 1.025F));
		partdefinition.addOrReplaceChild("back", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -1.375F, 0.0F, 2.0F, 3.0F, 10.0F, new CubeDeformation(0.0F)).texOffs(0, -1).addBox(0.0F, -2.125F, 0.0F, 0.0F, 4.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 22.375F, 1.0F));
		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	public final ModelPart root;
	public final ModelPart back;

	public LampreyModel(ModelPart root) {
		this.root = root;
		this.back = root.getChild("back");
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

}
