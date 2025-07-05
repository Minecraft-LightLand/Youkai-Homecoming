package dev.xkmc.youkaishomecoming.content.entity.animal.crab;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.HumanoidArm;

public class CrabModel extends HierarchicalModel<CrabEntity> implements ArmedModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(YoukaisHomecoming.loc("crab"), "main");

	public final ModelPart root, body, up, leftClaw, rightClaw;

	public CrabModel(ModelPart root) {
		this.root = root;
		body = root.getChild("AllBody");
		up = body.getChild("UpBody");
		leftClaw = up.getChild("LeftClaw");
		rightClaw = up.getChild("RightClaw");
	}

	@Override
	public ModelPart root() {
		return root;
	}

	public void setupAnim(CrabEntity e, float limbSw, float limbSA, float tick, float yrot, float xrot) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		if (e.states().state() == CrabState.FLIP) {
			this.animate(e.states.flip, CrabModelData.AFRAID, tick);
			return;
		}
		if (e.onGround()) {
			this.animateWalk(CrabModelData.WALK, limbSw, limbSA, 2f, 2.5f);
		} else if (e.isInWaterOrBubble()) {
			this.animateWalk(CrabModelData.SWIM, limbSw, limbSA, 2f, 2.5f);
		}
		this.animate(e.states.dig, CrabModelData.DIG, tick);
		this.animate(e.states.swing, CrabModelData.SWING, tick);
	}

	@Override
	public void translateToHand(HumanoidArm arm, PoseStack pose) {
		root.translateAndRotate(pose);
		body.translateAndRotate(pose);
		up.translateAndRotate(pose);
		(arm == HumanoidArm.LEFT ? leftClaw : rightClaw).translateAndRotate(pose);
		pose.translate((arm == HumanoidArm.LEFT ? -1 : 1f) * 0.1f, -0.6f, 0.1f);
	}

}
