package dev.xkmc.youkaishomecoming.content.block.donation;

import dev.xkmc.l2modularblock.BlockProxy;
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

import javax.annotation.Nullable;

public class DonationBoxMethod implements PlacementBlockMethod, SetPlacedByBlockMethod, ShapeUpdateBlockMethod {

	@Nullable
	public BlockState getStateForPlacement(@Nullable BlockState def, BlockPlaceContext ctx) {
		if (def == null) return null;
		Direction dir = def.getValue(BlockProxy.HORIZONTAL_FACING);
		BlockState next = ctx.getLevel().getBlockState(ctx.getClickedPos().relative(dir));
		if (!next.canBeReplaced()) return null;
		return def;
	}

	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity le, ItemStack stack) {
		Direction dir = state.getValue(BlockProxy.HORIZONTAL_FACING);
		level.setBlockAndUpdate(pos.relative(dir), state.setValue(BlockProxy.HORIZONTAL_FACING, dir.getOpposite()));
	}

	@Override
	public BlockState updateShape(Block self, BlockState current, BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
		if (facing == current.getValue(BlockProxy.HORIZONTAL_FACING)) {
			if (!facingState.is(self) || facingState.getValue(BlockProxy.HORIZONTAL_FACING) != facing.getOpposite())
				return Blocks.AIR.defaultBlockState();
		}
		return current;
	}

}