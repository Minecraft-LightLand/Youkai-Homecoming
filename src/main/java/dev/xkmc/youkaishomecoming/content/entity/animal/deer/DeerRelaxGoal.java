package dev.xkmc.youkaishomecoming.content.entity.animal.deer;

import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class DeerRelaxGoal extends Goal {

	private final DeerEntity mob;

	private int relaxTick = 0;

	public DeerRelaxGoal(DeerEntity mob) {
		this.mob = mob;
		setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP));
	}

	public boolean canUse() {
		if (!mob.states.canRelax()) return false;
		if (mob.states.isRelaxed()) return true;
		if (mob.getRandom().nextInt(mob.prop.relaxWillingness()) != 0)
			return false;
		else return mob.eat.canEat();
	}

	@Override
	public boolean canContinueToUse() {
		return mob.states.isRelaxed();
	}

	public void start() {
		mob.getNavigation().stop();
		mob.states.startRelax();
		relaxTick = 0;
	}

	@Override
	public void tick() {
		relaxTick++;
		if (!mob.states.mayStopRelax()) return;
		if (relaxTick < adjustedTickDelay(100)) return;
		int chance = adjustedTickDelay(mob.prop.relaxTime()) - relaxTick;
		if (chance <= 1 || mob.getRandom().nextInt(chance) == 0) {
			mob.states.stopRelax();
		}
	}

}
