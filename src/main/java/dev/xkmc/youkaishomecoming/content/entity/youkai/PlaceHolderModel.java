package dev.xkmc.youkaishomecoming.content.entity.youkai;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;

public class PlaceHolderModel<T extends GeneralYoukaiEntity> extends HierarchicalModel<T> {

	private final ModelPart root;
	private final ModelPart head;
	private final ModelPart rightLeg;
	private final ModelPart leftLeg;
	private final ModelPart rightArm;
	private final ModelPart leftArm;

	public PlaceHolderModel(ModelPart root) {
		this.root = root;
		this.head = root.getChild("head");
		this.rightLeg = root.getChild("rightLeg");
		this.leftLeg = root.getChild("leftLeg");
		this.rightArm = root.getChild("rightArm");
		this.leftArm = root.getChild("leftArm");
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