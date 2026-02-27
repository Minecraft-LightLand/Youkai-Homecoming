package dev.xkmc.youkaishomecoming.content.entity.deer.goal;

import dev.xkmc.youkaishomecoming.content.entity.deer.DeerEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class DeerRelaxGoal extends Goal {

	private final DeerEntity mob;

	private int relaxTick = 0;

	public DeerRelaxGoal(DeerEntity mob) {
		this.mob = mob;
		setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP, Flag.LOOK));
	}

	public boolean canUse() {
		if (!mob.states.canRelax()) return false;
		if (mob.states.isRelaxed()) return true;
		if (mob.getRandom().nextInt(mob.prop.relaxWillingness()) != 0)
			return false;
		else return mob.eat.canEat();
	}

	public boolean isInterruptable() {
		return false;
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
		boolean wakeUp = mob.states().wantsToMove();
		if (relaxTick >= adjustedTickDelay(100)) {
			int chance = adjustedTickDelay(mob.prop.relaxTime()) - relaxTick;
			wakeUp |= chance <= 1 || mob.getRandom().nextInt(chance) == 0;
		}
		if (wakeUp) {
			mob.states.stopRelax();
		}
	}

}
