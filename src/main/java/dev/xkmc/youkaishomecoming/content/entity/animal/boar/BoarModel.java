package dev.xkmc.youkaishomecoming.content.entity.animal.boar;

import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.client.model.AgeableHierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;

public class BoarModel extends AgeableHierarchicalModel<BoarEntity> {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(YoukaisHomecoming.loc("boar"), "main");

	public final ModelPart root, head;

	public BoarModel(ModelPart root) {
		super(0.5f, 24f);
		this.root = root;
		this.head =root.getChild("root").getChild("head");
	}

	@Override
	public ModelPart root() {
		return root;
	}

	public void setupAnim(BoarEntity e, float limbSw, float limbSA, float tick, float yrot, float xrot) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.head.yRot = yrot * (float) (Math.PI / 180.0);
		this.head.xRot = xrot * (float) (Math.PI / 180.0);
		if (e.prop.isPanic()) {
			this.animateWalk(BoarModelData.RUN, limbSw, limbSA, 2f, 2.5f);
		} else if (e.states.isMobile()) {
			this.animateWalk(BoarModelData.WALK, limbSw, limbSA, 3f, 2.5f);
		}
		this.animate(e.states.sleepStart, BoarModelData.SLEEP_START, tick);
		this.animate(e.states.sleepDur, BoarModelData.SLEEP, tick);
		this.animate(e.states.sleepEnd, BoarModelData.SLEEP_END, tick);
		this.animate(e.states.eat, BoarModelData.EAT, tick);
		this.animate(e.states.attack, BoarModelData.ATTACK, tick);
		this.animate(e.states.smell, BoarModelData.SMELL, tick);
	}

}
