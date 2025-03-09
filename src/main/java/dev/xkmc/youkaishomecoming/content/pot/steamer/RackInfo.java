package dev.xkmc.youkaishomecoming.content.pot.steamer;

import dev.xkmc.l2modularblock.BlockProxy;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;

import static dev.xkmc.youkaishomecoming.content.pot.steamer.SteamerStates.*;

public record RackInfo(boolean pot, int racks) {

	public static int ofY(BlockHitResult vec) {
		double y = Mth.positiveModulo(vec.getLocation().y(), 1);
		if (y == 0) return vec.getDirection() == Direction.UP ? 3 : 0;
		return (int) ((y - 1e-3) * 4);
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

	public static boolean isCapped(Level level, BlockPos pos) {
		var state = level.getBlockState(pos);
		RackInfo info = getRackInfo(state);
		if (!info.pot() && info.racks() == 0)
			return false;
		if (info.height() == 4) {
			return level.getBlockState(pos.above()).is(YHBlocks.STEAMER_LID.get());
		}
		return state.getValue(SteamerStates.CAPPED);
	}

	public static boolean tryCap(Level level, BlockPos pos, BlockState state) {
		if (state.getValue(CAPPED)) return false;
		var info = getRackInfo(state);
		if (info.height() == 4) {
			return info.popLid(level, pos.above(), state.getValue(BlockProxy.HORIZONTAL_FACING));
		}
		if (!level.isClientSide()) {
			level.setBlockAndUpdate(pos, state.setValue(CAPPED, true));
		}
		return true;
	}

	public static boolean tryRemoveCap(Level level, BlockPos pos, BlockState state) {
		if (!state.getValue(CAPPED)) return false;
		var info = getRackInfo(state);
		if (info.height() == 4) {
			return info.popLid(level, pos.above(), state.getValue(BlockProxy.HORIZONTAL_FACING));
		}
		if (!level.isClientSide()) {
			level.setBlockAndUpdate(pos, state.setValue(CAPPED, false));
		}
		return true;
	}

	public int height() {
		return racks + (pot ? 2 : 0);
	}

	public boolean tryAddRack(Level level, BlockPos pos, BlockState state) {
		var dir = state.getValue(BlockProxy.HORIZONTAL_FACING);
		IntegerProperty rackProp = pot ? POT_RACKS : RACKS;
		int max = pot ? 2 : 4;
		if (racks == max) {
			return stackTop(level, pos.above(), dir);
		}
		if (racks == max - 1 && state.getValue(CAPPED)) {
			if (!popLid(level, pos.above(), dir))
				return false;
			if (!level.isClientSide()) {
				level.setBlockAndUpdate(pos, state.setValue(rackProp, racks + 1).setValue(CAPPED, false));
			}
			return true;
		}
		if (!level.isClientSide()) {
			level.setBlockAndUpdate(pos, state.setValue(rackProp, racks + 1));
		}
		return true;
	}

	private boolean popLid(Level level, BlockPos pos, Direction facing) {
		if (level.isOutsideBuildHeight(pos)) return false;
		BlockState state = level.getBlockState(pos);
		if (!state.canBeReplaced()) return false;
		if (!level.isClientSide()) {
			level.setBlockAndUpdate(pos, YHBlocks.STEAMER_LID.get().defaultBlockState()
					.setValue(BlockProxy.HORIZONTAL_FACING, facing));
		}
		return true;
	}

	private boolean stackTop(Level level, BlockPos pos, Direction facing) {
		if (level.isOutsideBuildHeight(pos)) return false;
		BlockState state = level.getBlockState(pos);
		if (state.is(YHBlocks.STEAMER_RACK.get()))
			return getRackInfo(state).tryAddRack(level, pos, state);
		else if (state.is(YHBlocks.STEAMER_LID.get())) {
			if (!level.isClientSide()) {
				level.setBlockAndUpdate(pos, YHBlocks.STEAMER_RACK.get().defaultBlockState()
						.setValue(BlockProxy.HORIZONTAL_FACING, facing)
						.setValue(CAPPED, true));
			}
			return true;
		}
		if (!state.canBeReplaced()) return false;
		if (!level.isClientSide()) {
			level.setBlockAndUpdate(pos, YHBlocks.STEAMER_RACK.get().defaultBlockState()
					.setValue(BlockProxy.HORIZONTAL_FACING, facing));
		}
		return true;
	}

	public boolean tryTakeRack(Level level, BlockPos pos, BlockState state) {
		if (racks == 0) return false;
		if (level.isClientSide) return true;
		if (level.getBlockEntity(pos) instanceof SteamerBlockEntity be) {
			if (be.racks.size() == racks) {
				be.removeRack(level, pos, height());
			}
		}
		var rackProp = pot ? POT_RACKS : RACKS;
		if (pot || racks > 1) {
			boolean capped = state.getValue(CAPPED);
			if (height() == 4) {
				var aboveState = level.getBlockState(pos.above());
				if (aboveState.is(YHBlocks.STEAMER_RACK.get()))
					return false;
				if (aboveState.is(YHBlocks.STEAMER_LID.get())) {
					level.removeBlock(pos.above(), false);
					capped = true;
				}
			}
			level.setBlockAndUpdate(pos, state.setValue(rackProp, racks - 1).setValue(CAPPED, capped));
		} else {
			if (state.getValue(CAPPED)) {
				level.setBlockAndUpdate(pos, YHBlocks.STEAMER_LID.get().defaultBlockState()
						.setValue(BlockProxy.HORIZONTAL_FACING, state.getValue(BlockProxy.HORIZONTAL_FACING)));
			} else {
				level.removeBlock(pos, false);
			}
		}
		return true;
	}

}
