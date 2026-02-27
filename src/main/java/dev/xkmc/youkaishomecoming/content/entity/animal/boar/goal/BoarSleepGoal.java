package dev.xkmc.youkaishomecoming.content.entity.animal.boar.goal;

import dev.xkmc.youkaishomecoming.content.entity.animal.boar.BoarEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class BoarSleepGoal extends Goal {

	private final BoarEntity mob;

	private int relaxTick = 0;

	public BoarSleepGoal(BoarEntity mob) {
		this.mob = mob;
		setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
	}

	public boolean canUse() {
		if (!mob.states.canSleep()) return false;
		if (mob.states.isSleeping()) return true;
		return mob.getRandom().nextInt(mob.prop.sleepiness()) == 0;
	}

	public boolean isInterruptable() {
		return false;
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
		boolean wakeUp = mob.states().wantsToMove();
		if (relaxTick >= adjustedTickDelay(100)) {
			int chance = adjustedTickDelay(mob.prop.sleepTime()) - relaxTick;
			wakeUp |= chance <= 1 || mob.getRandom().nextInt(chance) == 0;
		}
		if (wakeUp) {
			mob.states.wakeUp();
		}
	}

}
