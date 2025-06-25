package dev.xkmc.youkaishomecoming.content.block.plant.grape;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import org.jetbrains.annotations.Nullable;

public abstract class BranchCropVineBlock extends BaseCropVineBlock {

	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final BooleanProperty EXTENDED = BooleanProperty.create("extended");

	public BranchCropVineBlock(Properties prop) {
		super(prop.randomTicks());
		registerDefaultState(defaultBlockState().setValue(EXTENDED, false));
	}

	protected abstract CenterCropVineBlock getCenter();

	@Override
	protected VineTrunkBlock getTrunk() {
		return getCenter().getTrunk();
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING, EXTENDED);
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

	@Override
	public BlockState updateShape(BlockState state, Direction facing, BlockState fstate, LevelAccessor level, BlockPos cpos, BlockPos fpos) {
		if (facing.getOpposite() == state.getValue(FACING)) {
			state = state.setValue(EXTENDED, false);
			if (fstate.getBlock() instanceof BranchCropVineBlock) {
				if (fstate.getValue(FACING) == facing) {
					state = state.setValue(EXTENDED, true);
				}
			}
		}
		return super.updateShape(state, facing, fstate, level, cpos, fpos);
	}

}
