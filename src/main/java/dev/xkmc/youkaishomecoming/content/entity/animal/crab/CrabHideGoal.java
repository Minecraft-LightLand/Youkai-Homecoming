package dev.xkmc.youkaishomecoming.content.entity.animal.crab;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

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
		if (mob.states.state().isInHidingAnim() && mob.level() instanceof ServerLevel sl) {
			BlockPos pos = BlockPos.containing(mob.position());
			var down = sl.getBlockState(pos.below());
			var vec = mob.position().add(
					Vec3.directionFromRotation(new Vec2(0, mob.getYRot()))
							.scale(mob.getBbWidth() / 2));
			sl.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, down),
					vec.x, vec.y + 0.1f, vec.z, 10, 0, 0, 0, 0.05);
			if (relaxTick % adjustedTickDelay(10) == 0) {
				sl.playSound(mob, mob.blockPosition(), SoundEvents.BRUSH_SAND,
						SoundSource.AMBIENT, 1, 1);
			}
		}
	}

}
