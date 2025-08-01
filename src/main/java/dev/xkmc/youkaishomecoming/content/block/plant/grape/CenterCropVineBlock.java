package dev.xkmc.youkaishomecoming.content.block.plant.grape;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraftforge.common.ForgeHooks;
import org.jetbrains.annotations.Nullable;

import static dev.xkmc.youkaishomecoming.content.block.plant.rope.RopeLoggedCropBlock.isRope;

public abstract class CenterCropVineBlock extends BaseCropVineBlock {

	public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;
	public static final BooleanProperty LEFT = BooleanProperty.create("left");
	public static final BooleanProperty RIGHT = BooleanProperty.create("right");

	public CenterCropVineBlock(Properties prop) {
		super(prop.randomTicks());
		registerDefaultState(defaultBlockState()
				.setValue(AXIS, Direction.Axis.Z)
				.setValue(LEFT, false)
				.setValue(RIGHT, false)
		);
	}

	protected abstract BranchCropVineBlock getSide();

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(AXIS, LEFT, RIGHT);
	}

	@Override
	protected boolean attemptGrowth(BlockState state, LevelReader level, BlockPos pos, @Nullable RandomSource random, boolean simulate, boolean natural, float speed) {
		if (attemptGrowthImpl(state, level, pos, random, simulate, natural, speed, true))
			return true;
		int age = state.getValue(getAgeProperty());
		if (age < getMaxAge() && simulate) return true;
		if (age >= getBaseAge()) {
			if (!state.getValue(TOP)) {
				var up = pos.above();
				var top = level.getBlockState(up);
				if (isRope(top)) {
					top = defaultBlockState()
							.setValue(AXIS, state.getValue(AXIS))
							.setValue(TOP, true);
					if (attemptClimb(top, level, up, random, simulate, natural, speed))
						return true;
				} else if (top.is(this)) {
					int prevAge = top.getValue(getAgeProperty());
					boolean pass = prevAge < age || age == getMaxAge();
					if (pass && prevAge >= getBaseAge() && !simulate && random != null) {
						if (age < getMaxAge() ||
								attemptGrowthImpl(state, level, pos, random, true, natural, speed, false) ||
								super.attemptGrowth(state, level, pos, random, true, natural, speed))
							pass = random.nextBoolean();
					}
					if (pass) {
						if (attemptGrowth(top, level, up, random, simulate, natural, speed))
							return true;
					}
				}
			}
			if (attemptGrowthImpl(state, level, pos, random, simulate, natural, speed, false))
				return true;
		}
		return super.attemptGrowth(state, level, pos, random, simulate, natural, speed);
	}

	protected boolean attemptGrowthImpl(BlockState state, LevelReader level, BlockPos pos, @Nullable RandomSource random, boolean simulate, boolean natural, float speed, boolean first) {
		var axis = state.getValue(AXIS);
		var firstDir = Direction.get(Direction.AxisDirection.NEGATIVE, axis);
		var secondDir = Direction.get(Direction.AxisDirection.POSITIVE, axis);
		int age = state.getValue(getAgeProperty());
		if (!simulate && random != null && random.nextBoolean()) {
			firstDir = firstDir.getOpposite();
			secondDir = secondDir.getOpposite();
		}
		if (attemptGrowthBranch(firstDir, age, level, pos.relative(firstDir), pos, state, random, simulate, natural, speed, first))
			return true;
		if (attemptGrowthBranch(secondDir, age, level, pos.relative(secondDir), pos, state, random, simulate, natural, speed, first))
			return true;
		return false;
	}

	protected boolean attemptClimb(BlockState state, LevelReader level, BlockPos pos, @Nullable RandomSource random, boolean simulate, boolean natural, float speed) {
		if (simulate) return true;
		assert random != null;
		Level setter = (Level) level;
		if (natural && !ForgeHooks.onCropsGrowPre(setter, pos, state, random.nextFloat() < speed))
			return false;
		setter.setBlock(pos, state, 2);
		if (natural) {
			ForgeHooks.onCropsGrowPost(setter, pos, state);
		}
		return true;
	}

	protected boolean attemptGrowthBranch(
			Direction dir, int age, LevelReader level, BlockPos pos,
			BlockPos source, BlockState center,
			@Nullable RandomSource random, boolean simulate, boolean natural, float speed, boolean first) {
		var down = level.getBlockState(pos.below());
		if (down.is(getSide()) && down.getValue(getSide().getAgeProperty()) < getSide().getBaseAge())
			return false;
		var state = level.getBlockState(pos);
		boolean updateSelf = false;
		if (!state.is(getSide())) {
			if (!isRope(state)) return false;
			var next = level.getBlockState(pos.relative(dir));
			boolean ext = next.getBlock() instanceof BranchCropVineBlock;
			var low = level.getBlockState(pos.below());
			boolean top = low.is(getSide()) && low.getValue(BranchCropVineBlock.FACING).getAxis() == dir.getAxis();
			state = getSide().defaultBlockState()
					.setValue(BranchCropVineBlock.FACING, dir.getOpposite())
					.setValue(BranchCropVineBlock.EXTENDED, ext)
					.setValue(TOP, top);
			updateSelf = true;
		} else {
			int prevAge = state.is(getSide()) ? state.getValue(getSide().getAgeProperty()) : -1;
			if (prevAge >= age) return false;
			if (first && prevAge >= getBaseAge()) return false;
		}
		if (simulate) return true;
		assert random != null;
		Level setter = (Level) level;
		if (natural && !ForgeHooks.onCropsGrowPre(setter, pos, state, random.nextFloat() < speed))
			return false;
		if (updateSelf) {
			setter.setBlock(pos, state, 2);
			var prop = dir.getAxisDirection() == Direction.AxisDirection.NEGATIVE ? LEFT : RIGHT;
			setter.setBlock(source, center.setValue(prop, true), 2);
		} else {
			getSide().doGrowth(state, setter, pos, random);
		}
		if (natural) {
			ForgeHooks.onCropsGrowPost(setter, pos, state);
		}
		return true;
	}

	@Nullable
	protected BlockPos getTrunk(BlockState state, BlockGetter level, BlockPos pos) {
		pos = pos.below();
		var low = level.getBlockState(pos);
		if (low.is(getTrunk()))
			return pos;
		if (low.is(this)) {
			pos = pos.below();
			low = level.getBlockState(pos);
			if (low.is(getTrunk()))
				return pos;
		}
		return null;
	}

	@Override
	public BlockState updateShape(BlockState state, Direction facing, BlockState fstate, LevelAccessor level, BlockPos cpos, BlockPos fpos) {
		if (facing.getAxis() == state.getValue(AXIS)) {
			if (facing.getAxisDirection() == Direction.AxisDirection.NEGATIVE) {
				if (!fstate.is(getSide()) || fstate.getValue(BranchCropVineBlock.FACING) != facing.getOpposite()) {
					state = state.setValue(LEFT, false);
				}
			} else {
				if (!fstate.is(getSide()) || fstate.getValue(BranchCropVineBlock.FACING) != facing.getOpposite()) {
					state = state.setValue(RIGHT, false);
				}
			}
		}
		return super.updateShape(state, facing, fstate, level, cpos, fpos);
	}
}
