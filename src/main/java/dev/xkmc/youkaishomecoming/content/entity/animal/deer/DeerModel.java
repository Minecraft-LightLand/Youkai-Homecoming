package dev.xkmc.youkaishomecoming.content.entity.animal.deer;

import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.client.model.AgeableHierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;

public class DeerModel extends AgeableHierarchicalModel<DeerEntity> {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(YoukaisHomecoming.loc("deer"), "main");

	public final ModelPart root, head, horn;

	public DeerModel(ModelPart root) {
		super(0.5f, 24f);
		this.root = root.getChild("root");
		var neck = this.root.getChild("neck");
		this.head = neck.getChild("head");
		this.horn = head.getChild("horn");
	}

	@Override
	public ModelPart root() {
		return root;
	}

	public void setupAnim(DeerEntity e, float limbSw, float limbSA, float tick, float yrot, float xrot) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		horn.visible = e.prop.isHorned();
		this.head.yRot = yrot * (float) (Math.PI / 180.0);
		this.head.xRot = xrot * (float) (Math.PI / 180.0);
		if (e.prop.isPanic()) {
			this.animateWalk(DeerModelData.RUN, limbSw, limbSA, 2f, 2.5f);
		} else {
			this.animateWalk(DeerModelData.WALK, limbSw, limbSA, 2f, 2.5f);
		}
		this.animate(e.states.relaxStart, DeerModelData.RELAX_START, tick);
		this.animate(e.states.relaxDur, DeerModelData.RELAX, tick);
		this.animate(e.states.relaxEnd, DeerModelData.RELAX_END, tick);
		this.animate(e.states.eat, DeerModelData.EAT, tick);
		this.animate(e.states.attack, DeerModelData.ATTACK, tick);
		this.animate(e.states.smell, DeerModelData.SMELL, tick);
	}

}
