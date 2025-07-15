package dev.xkmc.youkaishomecoming.content.entity.animal.crab;

import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class CrabHideGoal extends Goal {

	private final CrabEntity mob;

	private int relaxTick = 0;

	public CrabHideGoal(CrabEntity mob) {
		this.mob = mob;
		setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP));
	}

	public boolean canUse() {
		if (!mob.states.canHide()) return false;
		if (mob.states.isHiding()) return true;
		if (mob.getRandom().nextInt(mob.prop.hideWillingness()) != 0)
			return false;
		else return mob.dig.canDig();
	}

	@Override
	public boolean canContinueToUse() {
		return mob.states.isHiding();
	}

	public void start() {
		mob.getNavigation().stop();
		mob.states.startHiding();
		relaxTick = 0;
	}

	@Override
	public void tick() {
		relaxTick++;
		if (!mob.states.mayStopHiding()) return;
		if (relaxTick < adjustedTickDelay(100)) return;
		int chance = adjustedTickDelay(mob.prop.hideTime()) - relaxTick;
		if (chance <= 1 || mob.getRandom().nextInt(chance) == 0) {
			mob.states.stopHiding();
		}
	}

}
