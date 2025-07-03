package dev.xkmc.youkaishomecoming.content.block.plant.rope;

import dev.xkmc.youkaishomecoming.init.registrate.YHCriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CucumberCropBlock extends RootedClimbingCropBlock {

	private static final VoxelShape[] LOWER, UPPER, ROPE_LOWER, ROPE_UPPER;

	static {
		LOWER = new VoxelShape[8];
		UPPER = new VoxelShape[8];
		ROPE_LOWER = new VoxelShape[8];
		ROPE_UPPER = new VoxelShape[8];

		LOWER[0] = box(0, 0, 2, 12, 7, 14);
		LOWER[1] = box(0, 0, 2, 12, 10, 14);
		LOWER[2] = box(0, 0, 2, 12, 13, 14);

		UPPER[0] = box(0, 0, 2, 12, 9, 14);
		UPPER[1] = box(0, 0, 2, 12, 10, 14);
		UPPER[2] = box(0, 0, 2, 12, 15, 14);

		var rod = box(7, 0, 7, 9, 16, 9);
		var full = box(0, 0, 2, 12, 16, 14);
		for (int i = 0; i < 3; i++) {
			ROPE_LOWER[i] = Shapes.or(LOWER[i], rod);
			ROPE_UPPER[i] = Shapes.or(UPPER[i], rod);
		}
		for (int i = 3; i < 8; i++) {
			LOWER[i] = UPPER[i] = ROPE_LOWER[i] = ROPE_UPPER[i] = full;
		}
	}

	public CucumberCropBlock(Properties properties, ItemLike seed, ItemLike fruit) {
		super(properties, seed, fruit);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		int age = state.getValue(getAgeProperty());
		boolean rope = state.getValue(ROPELOGGED);
		boolean base = state.getValue(BASE);
		return (rope ? base ? ROPE_LOWER : ROPE_UPPER : base ? LOWER : UPPER)[age];
	}

	@Override
	protected void pickup(BlockState state, Level level, BlockPos pos, Player player) {
		super.pickup(state, level, pos, player);
		if (player instanceof ServerPlayer sp &&
				level.getBlockState(pos.below()).is(this) &&
				level.getBlockState(pos.below().below()).is(this)) {
			YHCriteriaTriggers.CUCUMBER.trigger(sp);
		}
	}
}
