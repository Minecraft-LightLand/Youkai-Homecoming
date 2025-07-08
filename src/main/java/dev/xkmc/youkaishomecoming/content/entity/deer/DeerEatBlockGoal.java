package dev.xkmc.youkaishomecoming.content.entity.deer;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.EnumSet;
import java.util.function.Predicate;

public class DeerEatBlockGoal extends Goal {
	private static final Predicate<BlockState> IS_TALL_GRASS = BlockStatePredicate.forBlock(Blocks.GRASS);
	private final DeerEntity mob;
	private final Level level;
	protected int eatAnimationTick, finishTick;

	public DeerEatBlockGoal(DeerEntity mob) {
		this.mob = mob;
		level = mob.level();
		setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.JUMP));
	}

	public boolean canUse() {
		if (!mob.states.canEat()) return false;
		if (mob.getRandom().nextInt(mob.prop.eatWillingness()) != 0)
			return false;
		else return canEat();
	}

	protected boolean canEat() {
		BlockPos blockpos = BlockPos.containing(mob.position().add(mob.getForward()));
		if (IS_TALL_GRASS.test(level.getBlockState(blockpos))) {
			return true;
		} else {
			return level.getBlockState(blockpos.below()).is(Blocks.GRASS_BLOCK);
		}
	}

	public void start() {
		mob.states.startEating();
		eatAnimationTick = adjustedTickDelay(34);
		finishTick = adjustedTickDelay(10);
		level.broadcastEntityEvent(mob, (byte) 10);
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
			BlockPos pos = BlockPos.containing(mob.position().add(mob.getForward()));
			if (IS_TALL_GRASS.test(level.getBlockState(pos))) {
				if (ForgeEventFactory.getMobGriefingEvent(level, mob)) {
					level.destroyBlock(pos, false);
				}
				mob.ate();
			} else {
				BlockPos down = pos.below();
				if (level.getBlockState(down).is(Blocks.GRASS_BLOCK)) {
					if (ForgeEventFactory.getMobGriefingEvent(level, mob)) {
						level.levelEvent(2001, down, Block.getId(Blocks.GRASS_BLOCK.defaultBlockState()));
						level.setBlock(down, Blocks.DIRT.defaultBlockState(), 2);
					}
					mob.ate();
				}
			}

		}
	}
}
