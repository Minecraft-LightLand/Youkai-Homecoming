package dev.xkmc.youkaishomecoming.content.entity.animal.crab;

import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class CrabFlipGoal extends Goal {

	protected final CrabEntity mob;

	public CrabFlipGoal(CrabEntity e) {
		this.mob = e;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE, Flag.LOOK, Flag.JUMP));
	}

	@Override
	public boolean canUse() {
		return mob.states.isFlipped();
	}

	@Override
	public void start() {
		mob.getNavigation().stop();
	}

}
