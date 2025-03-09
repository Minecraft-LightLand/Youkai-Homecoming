package dev.xkmc.youkaishomecoming.content.pot.steamer;

import dev.xkmc.l2modularblock.BlockProxy;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import static dev.xkmc.youkaishomecoming.content.pot.steamer.SteamerStates.POT_RACKS;
import static dev.xkmc.youkaishomecoming.content.pot.steamer.SteamerStates.RACKS;

public record RackInfo(boolean pot, int racks) {

	public int height() {
		return racks + (pot ? 2 : 0);
	}

	public static RackInfo getRackInfo(BlockState state) {
		if (state.is(YHBlocks.STEAMER_POT.get())) {
			return new RackInfo(true, state.getValue(POT_RACKS));
		}
		if (state.is(YHBlocks.STEAMER_RACK.get())) {
			return new RackInfo(false, state.getValue(RACKS));
		}
		return new RackInfo(false, 0);
	}

	public boolean tryAddRack(Level level, BlockPos pos, BlockState state) {
		if (pot) {
			if (racks >= 2) {
				return stackTop(level, pos.above(), state.getValue(BlockProxy.HORIZONTAL_FACING));
			}
			if (!level.isClientSide()) {
				level.setBlockAndUpdate(pos, state.setValue(POT_RACKS, racks + 1));
			}
			return true;
		} else {
			if (racks >= 4) {
				return stackTop(level, pos.above(), state.getValue(BlockProxy.HORIZONTAL_FACING));
			}
			if (!level.isClientSide()) {
				level.setBlockAndUpdate(pos, state.setValue(RACKS, racks + 1));
			}
			return true;
		}
	}

	private boolean stackTop(Level level, BlockPos pos, Direction facing) {
		if (level.isOutsideBuildHeight(pos)) return false;
		BlockState state = level.getBlockState(pos);
		if (state.is(YHBlocks.STEAMER_RACK.get()))
			return getRackInfo(state).tryAddRack(level, pos, state);
		if (!state.canBeReplaced()) return false;
		if (!level.isClientSide()) {
			level.setBlockAndUpdate(pos, YHBlocks.STEAMER_RACK.get().defaultBlockState()
					.setValue(BlockProxy.HORIZONTAL_FACING, facing));
		}
		return true;
	}

	public boolean tryTakeRake(Level level, BlockPos pos, BlockState state) {
		if (racks == 0) return false;
		if (level.isClientSide) return true;
		if (level.getBlockEntity(pos) instanceof SteamerBlockEntity be) {
			if (be.racks.size() == racks) {
				be.removeRack(level, pos, height());
			}
		}
		if (pot) {
			level.setBlockAndUpdate(pos, state.setValue(POT_RACKS, racks - 1));
		} else if (racks > 1) {
			level.setBlockAndUpdate(pos, state.setValue(RACKS, racks - 1));
		} else {
			level.removeBlock(pos, false);
		}
		return true;
	}

}
