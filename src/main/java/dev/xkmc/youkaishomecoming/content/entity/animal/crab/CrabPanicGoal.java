package dev.xkmc.youkaishomecoming.content.entity.animal.crab;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class CrabPanicGoal extends Goal {
	protected final CrabEntity mob;
	protected final double speedModifier;
	protected double posX;
	protected double posY;
	protected double posZ;
	protected boolean isRunning;

	public CrabPanicGoal(CrabEntity e, double speed) {
		this.mob = e;
		this.speedModifier = speed;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE));
	}

	public boolean canUse() {
		if (!this.shouldPanic()) {
			return false;
		} else {
			if (!mob.isInWater()) {
				BlockPos blockpos = this.lookForWater(this.mob.level(), this.mob, 5);
				if (blockpos != null) {
					this.posX = blockpos.getX();
					this.posY = blockpos.getY();
					this.posZ = blockpos.getZ();
					return true;
				}
			}

			return this.findRandomPosition();
		}
	}

	protected boolean shouldPanic() {
		if (mob.states().isFlipped()) return false;
		return this.mob.getLastHurtByMob() != null || this.mob.isFreezing() || this.mob.isOnFire();
	}

	protected boolean findRandomPosition() {
		Vec3 vec3 = DefaultRandomPos.getPos(this.mob, 5, 4);
		if (vec3 == null) {
			return false;
		} else {
			this.posX = vec3.x;
			this.posY = vec3.y;
			this.posZ = vec3.z;
			return true;
		}
	}

	public boolean isRunning() {
		return this.isRunning;
	}

	public void start() {
		this.mob.getNavigation().moveTo(this.posX, this.posY, this.posZ, this.speedModifier);
		this.isRunning = true;
	}

	public void stop() {
		this.isRunning = false;
	}

	public boolean canContinueToUse() {
		if (mob.states().isFlipped()) return false;
		return !this.mob.getNavigation().isDone();
	}

	@Nullable
	protected BlockPos lookForWater(BlockGetter level, Entity e, int range) {
		BlockPos blockpos = e.blockPosition();
		return !level.getBlockState(blockpos).getCollisionShape(level, blockpos).isEmpty() ? null :
				BlockPos.findClosestMatch(e.blockPosition(), range, 1,
						(pos) -> level.getFluidState(pos).is(FluidTags.WATER)).orElse(null);
	}
}