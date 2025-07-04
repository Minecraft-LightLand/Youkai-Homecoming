package dev.xkmc.youkaishomecoming.content.entity.animal.crab;

import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;

public class CrabModel extends HierarchicalModel<CrabEntity> {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(YoukaisHomecoming.loc("crab"), "main");

	public final ModelPart root;

	public CrabModel(ModelPart root) {
		this.root = root.getChild("AllBody");
	}

	@Override
	public ModelPart root() {
		return root;
	}

	public void setupAnim(CrabEntity e, float limbSw, float limbSA, float tick, float yrot, float xrot) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		if (e.onGround()) {
			this.animateWalk(CrabModelData.WALK, limbSw, limbSA, 2f, 2.5f);
		} else if (e.isInWaterOrBubble()) {
			this.animateWalk(CrabModelData.SWIM, limbSw, limbSA, 2f, 2.5f);
		}
	}

}
