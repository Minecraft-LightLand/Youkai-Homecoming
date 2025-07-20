package dev.xkmc.youkaishomecoming.content.entity.animal.crab;

import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class CrabFlipGoal extends Goal {

	protected final CrabEntity mob;

	private int time = 0;

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
		time = 0;
	}

	@Override
	public void tick() {
		time++;
		if (time < adjustedTickDelay(40)) {
			return;
		}
		if (mob.getRandom().nextInt(mob.prop.flipPower()) == 0) {
			mob.states().flipBack();
		}
	}
}
