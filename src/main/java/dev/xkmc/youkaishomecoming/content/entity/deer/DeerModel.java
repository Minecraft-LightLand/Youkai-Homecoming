package dev.xkmc.youkaishomecoming.content.entity.deer;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;

public class DeerModel extends HierarchicalModel<DeerEntity> {


	@Override
	public ModelPart root() {
		return null;
	}

	public void setupAnim(DeerEntity e, float limbSw, float limbSA, float tick, float yrot, float xrot) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.head.yRot = yrot * (float) (Math.PI / 180.0);
		this.head.xRot = xrot * (float) (Math.PI / 180.0);

	}

}
