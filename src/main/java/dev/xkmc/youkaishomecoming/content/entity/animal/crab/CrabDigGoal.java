package dev.xkmc.youkaishomecoming.content.entity.animal.crab;

import dev.xkmc.youkaishomecoming.init.data.YHTagGen;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;

import java.util.EnumSet;

public class CrabDigGoal extends Goal {
	private final CrabEntity mob;
	private final Level level;
	protected int eatAnimationTick, finishTick;

	public CrabDigGoal(CrabEntity mob) {
		this.mob = mob;
		level = mob.level();
		setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
	}

	public boolean canUse() {
		if (!mob.states.canDig()) return false;
		if (mob.getRandom().nextInt(mob.prop.digWillingness()) != 0)
			return false;
		else return canEat();
	}

	protected boolean canEat() {
		BlockPos blockpos = BlockPos.containing(mob.position());
		return level.getBlockState(blockpos.below()).is(YHTagGen.CRAB_DIGABLE);
	}

	public void start() {
		mob.states.startDigging();
		eatAnimationTick = adjustedTickDelay(64);
		finishTick = adjustedTickDelay(10);
		mob.getNavigation().stop();
	}

	public void stop() {
		eatAnimationTick = 0;
		finishTick = 0;
	}

	public boolean canContinueToUse() {
		return eatAnimationTick > 0;
	}

	public void tick() {
		eatAnimationTick = Math.max(0, eatAnimationTick - 1);
		if (eatAnimationTick == finishTick) {
			BlockPos pos = BlockPos.containing(mob.position());
			BlockPos down = pos.below();
			if (level.getBlockState(down).is(YHTagGen.CRAB_DIGABLE)) {
				mob.dig();
			}
		}
	}
}
