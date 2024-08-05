package dev.xkmc.youkaishomecoming.content.entity.youkai;

import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;

public class MultiHurtByTargetGoal extends HurtByTargetGoal {

	private final YoukaiEntity youkai;

	public MultiHurtByTargetGoal(YoukaiEntity mob, Class<?>... ignore) {
		super(mob, ignore);
		this.youkai = mob;
	}

	@Override
	public void start() {
		youkai.targets.checkTarget();
		super.start();
	}
}
