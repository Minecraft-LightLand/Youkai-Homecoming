package dev.xkmc.youkaishomecoming.content.entity.animal.boar;

import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class BoarSleepGoal extends Goal {

	private final BoarEntity mob;

	private int relaxTick = 0;

	public BoarSleepGoal(BoarEntity mob) {
		this.mob = mob;
		setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP));
	}

	public boolean canUse() {
		if (!mob.states.canSleep()) return false;
		if (mob.states.isSleeping()) return true;
		if (mob.getRandom().nextInt(mob.prop.sleepiness()) != 0)
			return false;
		else return mob.eat.canEat();
	}

	@Override
	public boolean canContinueToUse() {
		return mob.states.isSleeping();
	}

	public void start() {
		mob.getNavigation().stop();
		mob.states.startSleep();
		relaxTick = 0;
	}

	@Override
	public void tick() {
		relaxTick++;
		if (!mob.states.mayWakeUp()) return;
		if (relaxTick < adjustedTickDelay(100)) return;
		int chance = adjustedTickDelay(mob.prop.sleepTime()) - relaxTick;
		if (chance <= 1 || mob.getRandom().nextInt(chance) == 0) {
			mob.states.wakeUp();
		}
	}

}
