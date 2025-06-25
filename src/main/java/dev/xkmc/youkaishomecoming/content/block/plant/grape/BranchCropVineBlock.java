package dev.xkmc.youkaishomecoming.content.block.plant.grape;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import org.jetbrains.annotations.Nullable;

public abstract class BranchCropVineBlock extends BaseCropVineBlock {

	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

	public BranchCropVineBlock(Properties prop) {
		super(prop.randomTicks());
	}

	protected abstract CenterCropVineBlock getCenter();

	@Override
	protected VineTrunkBlock getTrunk() {
		return getCenter().getTrunk();
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING);
	}

	@Nullable
	protected BlockPos getTrunk(BlockState state, BlockGetter level, BlockPos pos) {
		var dir = state.getValue(FACING);
		var next = pos.relative(dir);
		var center = level.getBlockState(next);
		if (!center.is(getCenter())) return null;
		var axis = center.getValue(CenterCropVineBlock.AXIS);
		if (dir.getAxis() != axis) return null;
		return getCenter().getTrunk(center, level, next);
	}

}
