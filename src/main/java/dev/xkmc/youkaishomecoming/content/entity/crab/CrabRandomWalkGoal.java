package dev.xkmc.youkaishomecoming.content.entity.crab;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class CrabRandomWalkGoal extends RandomSwimmingGoal {

	public CrabRandomWalkGoal(PathfinderMob mob, double speed, int interval) {
		super(mob, speed, interval);
	}

	@Override
	protected @Nullable Vec3 getPosition() {
		return getRandomSwimmablePos(mob, 10, 3);
	}

	@Nullable
	public Vec3 getRandomSwimmablePos(PathfinderMob mob, int hor, int ver) {
		Vec3 vec3 = DefaultRandomPos.getPos(mob, hor, ver);
		if (!mob.isInWater()) return vec3;
		int i = 0;
		while (true) {
			if (vec3 == null) return null;
			if (i++ >= 10) return vec3;
			var pos = BlockPos.containing(vec3);
			var state = mob.level().getBlockState(pos);
			var low = mob.level().getBlockState(pos.below());
			if (state.isPathfindable(mob.level(), pos, PathComputationType.WATER) &&
					low.isSolidRender(mob.level(), pos.below()))
				return vec3;
			vec3 = DefaultRandomPos.getPos(mob, hor, ver);
		}
	}

}
