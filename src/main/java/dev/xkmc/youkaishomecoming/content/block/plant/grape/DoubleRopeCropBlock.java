package dev.xkmc.youkaishomecoming.content.block.plant.grape;

import dev.xkmc.youkaishomecoming.content.block.plant.rope.RopeLoggedCropBlock;
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
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluids;

import javax.annotation.Nullable;

public abstract class DoubleRopeCropBlock extends RopeLoggedCropBlock {

	public static final BooleanProperty ROOT = BooleanProperty.create("rooted");

	public DoubleRopeCropBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected boolean mayGrow(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		return state.getValue(ROOT) &&
				super.mayGrow(state, level, pos, random) &&
				mayGrowTo(state, level, pos, state.getValue(getAgeProperty()) + 1);
	}

	@Override
	protected boolean mayGrowTo(BlockState state, LevelReader level, BlockPos pos, int age) {
		if (age >= getDoubleBlockStart()) {
			BlockState upper = level.getBlockState(pos.above());
			if (!upper.isAir() && !upper.is(this) && !isRope(upper))
				return false;
		}
		return super.mayGrowTo(state, level, pos, age);
	}

	@Override
	protected void growTo(BlockState state, ServerLevel level, BlockPos pos, int age) {
		super.growTo(state, level, pos, age);
		if (age >= getDoubleBlockStart()) {
			var upper = level.getBlockState(pos.above());
			boolean ropped = isRope(upper) || upper.is(this) && upper.getValue(ROPELOGGED);
			var next = state
					.setValue(getAgeProperty(), age)
					.setValue(ROOT, false)
					.setValue(ROPELOGGED, ropped);
			level.setBlock(pos.above(), next, 2);
		}
	}

	@Override
	public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state, boolean client) {
		if (!mayGrowTo(state, level, pos, state.getValue(getAgeProperty()) + 1))
			return false;
		return super.isValidBonemealTarget(level, pos, state, client);
	}

	@Override
	public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
		if (!state.getValue(ROOT)) {
			var lower = level.getBlockState(pos.below());
			if (!lower.is(this)) return;
			performBonemeal(level, random, pos.below(), lower);
			return;
		}
		super.performBonemeal(level, random, pos, state);
	}

	@Override
	protected void pickup(BlockState state, Level level, BlockPos pos) {
		if (!state.getValue(ROOT)) {
			var lower = level.getBlockState(pos.below());
			if (!lower.is(this)) return;
			pickup(lower, level, pos.below());
			level.setBlock(pos, state.setValue(getAgeProperty(), getBaseAge()), 2);
			return;
		}
		super.pickup(state, level, pos);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(ROOT);
	}

	public abstract int getDoubleBlockStart();

	public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
		if (pState.getValue(ROOT)) {
			return super.canSurvive(pState, pLevel, pPos);
		} else {
			BlockState blockstate = pLevel.getBlockState(pPos.below());
			//Forge: This function is called during world gen and placement, before this block is set, so if we are not 'here' then assume it's the pre-check.
			if (pState.getBlock() != this)
				return super.canSurvive(pState, pLevel, pPos);
			return blockstate.is(this) && blockstate.getValue(ROOT);
		}
	}

	public BlockState updateShape(BlockState state, Direction dir, BlockState sourceState, LevelAccessor level, BlockPos pos, BlockPos sourcePos) {
		boolean root = state.getValue(ROOT);
		if (root && dir == Direction.DOWN && !state.canSurvive(level, pos))
			return Blocks.AIR.defaultBlockState();
		if (dir.getAxis() == Direction.Axis.Y) {
			boolean illegal = !sourceState.is(this) || sourceState.getValue(ROOT) == root;
			if (!root && dir == Direction.DOWN && illegal) {
				return Blocks.AIR.defaultBlockState();
			}
			if (root && dir == Direction.UP && illegal) {
				if (state.getValue(getAgeProperty()) >= getDoubleBlockStart())
					return Blocks.AIR.defaultBlockState();
			}
		}
		return super.updateShape(state, dir, sourceState, level, pos, sourcePos);
	}

	public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
		if (!level.isClientSide) {
			if (player.isCreative()) {
				preventCreativeDropFromBottomPart(level, pos, state, player);
			} else {
				dropResources(state, level, pos, null, player, player.getMainHandItem());
			}
		}
		super.playerWillDestroy(level, pos, state, player);
	}

	public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity be, ItemStack stack) {
		super.playerDestroy(level, player, pos, Blocks.AIR.defaultBlockState(), be, stack);
	}

	public static void preventCreativeDropFromBottomPart(Level level, BlockPos pos, BlockState state, Player player) {
		var base = state.getValue(ROOT);
		if (!base) {
			BlockPos low = pos.below();
			BlockState lowState = level.getBlockState(low);
			if (lowState.is(state.getBlock()) && lowState.getValue(ROOT)) {
				BlockState next = lowState.getFluidState().is(Fluids.WATER) ?
						Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
				level.setBlock(low, next, 35);
				level.levelEvent(player, 2001, low, Block.getId(lowState));
			}
		}
	}

}
