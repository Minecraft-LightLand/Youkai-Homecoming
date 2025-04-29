package dev.xkmc.youkaishomecoming.content.pot.tank;

import dev.xkmc.l2modularblock.mult.CreateBlockStateBlockMethod;
import dev.xkmc.l2modularblock.mult.PlacementBlockMethod;
import dev.xkmc.l2modularblock.mult.SetPlacedByBlockMethod;
import dev.xkmc.l2modularblock.mult.ShapeUpdateBlockMethod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.material.Fluids;

import javax.annotation.Nullable;

public class CopperTankBlockVertical implements CreateBlockStateBlockMethod, PlacementBlockMethod, SetPlacedByBlockMethod, ShapeUpdateBlockMethod {

	@Override
	public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.DOUBLE_BLOCK_HALF);
		builder.add(BlockStateProperties.OPEN);
	}

	@Nullable
	public BlockState getStateForPlacement(@Nullable BlockState def, BlockPlaceContext ctx) {
		if (def == null) return null;
		if (ctx.getLevel().getBlockState(ctx.getClickedPos().above()).canBeReplaced(ctx))
			return def.setValue(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER);
		if (ctx.getLevel().getBlockState(ctx.getClickedPos().below()).canBeReplaced(ctx))
			return def.setValue(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER)
					.setValue(BlockStateProperties.OPEN, ctx.getLevel().getFluidState(ctx.getClickedPos().above()).isSourceOfType(Fluids.WATER));
		return null;
	}

	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity le, ItemStack stack) {
		var half = state.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF);
		if (half == DoubleBlockHalf.LOWER) {
			level.setBlockAndUpdate(pos.above(), state.setValue(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER)
					.setValue(BlockStateProperties.OPEN, level.getFluidState(pos.above()).isSourceOfType(Fluids.WATER)));
		} else {
			level.setBlockAndUpdate(pos.below(), state.setValue(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER));
		}
	}

	@Override
	public BlockState updateShape(Block self, BlockState current, BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
		var half = current.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF);
		var dir = half == DoubleBlockHalf.LOWER ? Direction.UP : Direction.DOWN;
		if (facing == dir) {
			if (!facingState.is(self) || facingState.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == half)
				return Blocks.AIR.defaultBlockState();
		}
		if (half == DoubleBlockHalf.UPPER && facing == Direction.UP) {
			return current.setValue(BlockStateProperties.OPEN, level.getFluidState(facingPos).isSourceOfType(Fluids.WATER));
		}
		return current;
	}

}