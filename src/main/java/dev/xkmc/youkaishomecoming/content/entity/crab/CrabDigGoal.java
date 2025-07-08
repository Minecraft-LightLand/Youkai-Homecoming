package dev.xkmc.youkaishomecoming.content.entity.crab;

import dev.xkmc.youkaishomecoming.init.data.YHTagGen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

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
		BlockPos pos = BlockPos.containing(mob.position());
		var down = level.getBlockState(pos.below());
		if (down.is(YHTagGen.CRAB_DIGABLE)) {
			if (eatAnimationTick > finishTick) {
				if (mob.level() instanceof ServerLevel sl) {
					var vec = mob.position().add(
							Vec3.directionFromRotation(new Vec2(0, mob.getYRot()))
									.scale(mob.getBbWidth() / 2));
					sl.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, down),
							vec.x, vec.y + 0.1f, vec.z, 10, 0, 0, 0, 0.05);
					if (eatAnimationTick % finishTick == 0) {
						sl.playSound(mob, mob.blockPosition(), SoundEvents.BRUSH_SAND,
								SoundSource.AMBIENT, 1, 1);
					}
				}
			} else if (eatAnimationTick == finishTick) {
				mob.dig(down);
			}
		}


	}

}
