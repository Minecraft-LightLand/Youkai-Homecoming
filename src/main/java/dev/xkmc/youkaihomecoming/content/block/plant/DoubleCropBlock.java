package dev.xkmc.youkaihomecoming.content.block.plant;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Fluids;

import javax.annotation.Nullable;

public abstract class DoubleCropBlock extends CropBlock {

	public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

	public DoubleCropBlock(Properties pProperties) {
		super(pProperties);
	}

	public abstract int getDoubleBlockStart();

	@Override
	public boolean isRandomlyTicking(BlockState pState) {
		return pState.getValue(HALF) == DoubleBlockHalf.LOWER;
	}

	public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
		if (!pLevel.isAreaLoaded(pPos, 1))
			return; // Forge: prevent loading unloaded chunks when checking neighbor's light
		if (pLevel.getRawBrightness(pPos, 0) >= 9) {
			int i = this.getAge(pState);
			if (i < this.getMaxAge()) {
				float f = getGrowthSpeed(this, pLevel, pPos);
				if (net.minecraftforge.common.ForgeHooks.onCropsGrowPre(pLevel, pPos, pState, pRandom.nextInt((int) (25.0F / f) + 1) == 0)) {
					setGrowth(pLevel, pPos, i + 1, 2);
					net.minecraftforge.common.ForgeHooks.onCropsGrowPost(pLevel, pPos, pState);
				}
			}
		}

	}

	public void growCrops(Level pLevel, BlockPos pPos, BlockState pState) {
		if (pState.getValue(HALF) == DoubleBlockHalf.UPPER) {
			var lower = pLevel.getBlockState(pPos.below());
			if (lower.is(this) && lower.getValue(HALF) == DoubleBlockHalf.LOWER) {
				pPos = pPos.below();
				pState = lower;
			} else return;
		}
		int i = this.getAge(pState) + this.getBonemealAgeIncrease(pLevel);
		int j = this.getMaxAge();
		if (i > j) {
			i = j;
		}
		setGrowth(pLevel, pPos, i, 2);
	}

	public void setGrowth(Level level, BlockPos pos, int age, int flag) {
		if (age >= getDoubleBlockStart()) {
			boolean fail = level.isOutsideBuildHeight(pos.above());
			if (!fail) {
				var above = level.getBlockState(pos.above());
				fail = !above.is(this) && !above.canBeReplaced();
			}
			if (fail) {
				age = getDoubleBlockStart() - 1;
				if (age < 0) return;
			}
		}
		level.setBlock(pos, this.getStateForAge(age), flag);
		if (age >= getDoubleBlockStart()) {
			level.setBlock(pos.above(), this.getStateForAge(age).setValue(HALF, DoubleBlockHalf.UPPER), flag);
		} else {
			var above = level.getBlockState(pos.above());
			if (above.is(this) && above.getValue(HALF) == DoubleBlockHalf.UPPER) {
				level.removeBlock(pos.above(), false);
			}
		}
	}

	public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
		if (pState.getValue(HALF) != DoubleBlockHalf.UPPER) {
			return super.canSurvive(pState, pLevel, pPos);
		} else {
			BlockState blockstate = pLevel.getBlockState(pPos.below());
			if (pState.getBlock() != this)
				return super.canSurvive(pState, pLevel, pPos); //Forge: This function is called during world gen and placement, before this block is set, so if we are not 'here' then assume it's the pre-check.
			return blockstate.is(this) && blockstate.getValue(HALF) == DoubleBlockHalf.LOWER;
		}
	}

	public BlockState updateShape(BlockState state, Direction dir, BlockState sourceState, LevelAccessor level, BlockPos pos, BlockPos sourcePos) {
		DoubleBlockHalf half = state.getValue(HALF);
		if (half == DoubleBlockHalf.LOWER && dir == Direction.DOWN && !state.canSurvive(level, pos))
			return Blocks.AIR.defaultBlockState();
		if (dir.getAxis() == Direction.Axis.Y) {
			boolean illegal = !sourceState.is(this) || sourceState.getValue(HALF) == half;
			if (half == DoubleBlockHalf.UPPER && dir == Direction.DOWN && illegal) {
				return Blocks.AIR.defaultBlockState();
			}
			if (half == DoubleBlockHalf.LOWER && dir == Direction.UP && illegal) {
				if (state.getValue(getAgeProperty()) >= getDoubleBlockStart())
					return Blocks.AIR.defaultBlockState();
			}
		}
		return super.updateShape(state, dir, sourceState, level, pos, sourcePos);
	}

	public void playerWillDestroy(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
		if (!pLevel.isClientSide) {
			if (pPlayer.isCreative()) {
				preventCreativeDropFromBottomPart(pLevel, pPos, pState, pPlayer);
			} else {
				dropResources(pState, pLevel, pPos, null, pPlayer, pPlayer.getMainHandItem());
			}
		}

		super.playerWillDestroy(pLevel, pPos, pState, pPlayer);
	}

	public void playerDestroy(Level pLevel, Player pPlayer, BlockPos pPos, BlockState pState, @Nullable BlockEntity pTe, ItemStack pStack) {
		super.playerDestroy(pLevel, pPlayer, pPos, Blocks.AIR.defaultBlockState(), pTe, pStack);
	}

	protected static void preventCreativeDropFromBottomPart(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
		DoubleBlockHalf doubleblockhalf = pState.getValue(HALF);
		if (doubleblockhalf == DoubleBlockHalf.UPPER) {
			BlockPos blockpos = pPos.below();
			BlockState blockstate = pLevel.getBlockState(blockpos);
			if (blockstate.is(pState.getBlock()) && blockstate.getValue(HALF) == DoubleBlockHalf.LOWER) {
				BlockState blockstate1 = blockstate.getFluidState().is(Fluids.WATER) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
				pLevel.setBlock(blockpos, blockstate1, 35);
				pLevel.levelEvent(pPlayer, 2001, blockpos, Block.getId(blockstate));
			}
		}

	}

}
