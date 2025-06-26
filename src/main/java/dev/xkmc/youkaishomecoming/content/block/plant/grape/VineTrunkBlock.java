package dev.xkmc.youkaishomecoming.content.block.plant.grape;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.PlantType;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.registry.ModBlocks;

import static dev.xkmc.youkaishomecoming.content.block.plant.rope.RopeLoggedCropBlock.getRopeBlock;
import static dev.xkmc.youkaishomecoming.content.block.plant.rope.RopeLoggedCropBlock.isRope;

@SuppressWarnings("deprecation")
public abstract class VineTrunkBlock extends BushBlock implements BonemealableBlock {

	public static final BooleanProperty MERGED = BooleanProperty.create("merged");

	public static final VoxelShape SHAPE = box(6, 0, 6, 10, 16, 10);

	protected ItemLike seed;

	public VineTrunkBlock(Properties prop, ItemLike seed) {
		super(prop.randomTicks());
		this.seed = seed;
		registerDefaultState(defaultBlockState().setValue(MERGED, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(MERGED);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
		return SHAPE;
	}

	@Override
	public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
		return seed.asItem().getDefaultInstance();
	}

	protected abstract CenterCropVineBlock getTop();

	@Override
	public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (!level.isAreaLoaded(pos, 1)) return;
		var speed = getGrowthSpeed(level, pos) * 0.04f;
		attemptGrowth(state, level, pos, random, false, true, speed);
	}

	protected boolean attemptGrowth(BlockState state, LevelReader level, BlockPos pos, @Nullable RandomSource random, boolean simulate, boolean natural, float speed) {
		if (level.getRawBrightness(pos, 0) < 9) return false;
		if (!state.getValue(MERGED)) {
			if (simulate) return true;
			assert random != null;
			Level setter = (Level) level;
			if (!natural) {
				setter.setBlock(pos, state.setValue(MERGED, true), 2);
				return true;
			} else if (ForgeHooks.onCropsGrowPre(setter, pos, state, random.nextFloat() < speed)) {
				setter.setBlock(pos, state.setValue(MERGED, true), 2);
				ForgeHooks.onCropsGrowPost(setter, pos, state);
				return true;
			}
			return false;
		}
		var up = pos.above();
		var cen = level.getBlockState(up);
		if (cen.is(getTop())) {
			return getTop().attemptGrowth(cen, level, up, random, simulate, natural, speed);
		}
		if (!isRope(cen)) return false;
		BlockState nor = level.getBlockState(up.north());
		BlockState sth = level.getBlockState(up.south());
		BlockState wst = level.getBlockState(up.west());
		BlockState est = level.getBlockState(up.east());
		boolean z = isRope(nor) || isRope(sth);
		boolean x = isRope(wst) || isRope(est);
		if (!z && !x) return false;
		if (simulate) return true;
		assert random != null;
		Level setter = (Level) level;
		if (z && x) {
			z = random.nextBoolean();
		}
		if (natural && !ForgeHooks.onCropsGrowPre(setter, pos, state, random.nextFloat() < speed))
			return false;
		BlockState topState = getTop().defaultBlockState();
		if (z) {
			topState = topState.setValue(CenterCropVineBlock.AXIS, Direction.Axis.Z);
			if (isRope(nor)) {
				topState = topState.setValue(CenterCropVineBlock.LEFT, true);
				setter.setBlock(up.north(), getTop().getSide().defaultBlockState()
						.setValue(BranchCropVineBlock.FACING, Direction.SOUTH), 2);
			}
			if (isRope(sth)) {
				topState = topState.setValue(CenterCropVineBlock.RIGHT, true);
				setter.setBlock(up.south(), getTop().getSide().defaultBlockState()
						.setValue(BranchCropVineBlock.FACING, Direction.NORTH), 2);
			}
		} else {
			topState = topState.setValue(CenterCropVineBlock.AXIS, Direction.Axis.X);
			if (isRope(wst)) {
				topState = topState.setValue(CenterCropVineBlock.LEFT, true);
				setter.setBlock(up.west(), getTop().getSide().defaultBlockState()
						.setValue(BranchCropVineBlock.FACING, Direction.EAST), 2);
			}
			if (isRope(est)) {
				topState = topState.setValue(CenterCropVineBlock.RIGHT, true);
				setter.setBlock(up.east(), getTop().getSide().defaultBlockState()
						.setValue(BranchCropVineBlock.FACING, Direction.WEST), 2);
			}
		}
		setter.setBlock(up, topState, 2);
		if (natural) {
			ForgeHooks.onCropsGrowPost(setter, up, topState);
		}
		return true;
	}

	@Override
	public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state, boolean client) {
		return attemptGrowth(state, level, pos, null, true, false, 1);
	}

	@Override
	public boolean isBonemealSuccess(Level p_220878_, RandomSource p_220879_, BlockPos p_220880_, BlockState p_220881_) {
		return true;
	}

	@Override
	public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
		attemptGrowth(state, level, pos, random, false, false, 1);
	}

	@Override
	public PlantType getPlantType(BlockGetter level, BlockPos pos) {
		return PlantType.PLAINS;
	}

	protected float getGrowthSpeed(BlockGetter level, BlockPos pos) {
		float f = 1;
		BlockPos blockpos = pos.below();

		for (int i = -1; i <= 1; ++i) {
			for (int j = -1; j <= 1; ++j) {
				float f1 = 0;
				BlockState blockstate = level.getBlockState(blockpos.offset(i, 0, j));
				if (blockstate.canSustainPlant(level, blockpos.offset(i, 0, j), net.minecraft.core.Direction.UP, this)) {
					f1 = 1;
					if (blockstate.is(ModBlocks.RICH_SOIL.get()) || blockstate.is(ModBlocks.RICH_SOIL_FARMLAND.get())) {
						f1 = 3;
					}
				}

				if (i != 0 || j != 0) {
					f1 /= 4;
				}

				f += f1;
			}
		}

		BlockPos nor = pos.north();
		BlockPos sth = pos.south();
		BlockPos wst = pos.west();
		BlockPos est = pos.east();
		boolean flag = level.getBlockState(wst).is(this) || level.getBlockState(est).is(this);
		boolean flag1 = level.getBlockState(nor).is(this) || level.getBlockState(sth).is(this);
		if (flag && flag1) {
			f /= 2.0F;
		} else {
			boolean flag2 = level.getBlockState(wst.north()).is(this) || level.getBlockState(est.north()).is(this) ||
					level.getBlockState(est.south()).is(this) || level.getBlockState(wst.south()).is(this);
			if (flag2) {
				f /= 2.0F;
			}
		}

		return f;
	}

	public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @javax.annotation.Nullable BlockEntity be, ItemStack stack) {
		doPlayerDestroy(level, player, pos, state, be, stack);
		destroyAndPlaceRope(level, pos);
	}

	public void doPlayerDestroy(Level level, Player player, BlockPos pos, BlockState state, @javax.annotation.Nullable BlockEntity be, ItemStack stack) {
		super.playerDestroy(level, player, pos, state, be, stack);
	}

	public BlockState updateShape(BlockState state, Direction facing, BlockState fstate, LevelAccessor level, BlockPos cpos, BlockPos fpos) {
		if (!state.canSurvive(level, cpos)) {
			level.scheduleTick(cpos, this, 1);
		}
		return state;
	}

	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		level.destroyBlock(pos, true);
		destroyAndPlaceRope(level, pos);

	}

	public static void destroyAndPlaceRope(Level level, BlockPos pos) {
		level.setBlockAndUpdate(pos, getRopeBlock());
	}

}
