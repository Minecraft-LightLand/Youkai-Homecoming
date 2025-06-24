package dev.xkmc.youkaishomecoming.content.block.plant.rope;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

public abstract class RopeClimbingCropBlock extends RopeLoggedCropBlock {

	public RopeClimbingCropBlock(Properties properties) {
		super(properties);
	}

	public BlockState getStateForAge(int age) {
		return climbingVine().setValue(getAgeProperty(), age);
	}

	public void postGrowth(ServerLevel level, BlockPos pos, RandomSource random) {
		BlockPos cpos = pos.relative(Direction.UP);
		if (!mayClimb(level, pos, random, cpos)) return;
		level.setBlockAndUpdate(cpos, climbingVine().setValue(ROPELOGGED, true));
	}

	protected boolean mayClimb(ServerLevel level, BlockPos pos, RandomSource random, BlockPos cpos) {
		if (random.nextFloat() > 0.3F) return false;
		BlockState cstate = level.getBlockState(cpos);
		if (!isRope(cstate)) return false;
		int h;
		for (h = 1; level.getBlockState(pos.below(h)).is(this); ++h) ;
		return h < 3;
	}

	protected BlockState climbingVine() {
		return defaultBlockState();
	}

}
