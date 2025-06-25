package dev.xkmc.youkaishomecoming.content.block.plant.grape;

import dev.xkmc.l2harvester.api.HarvestResult;
import dev.xkmc.l2harvester.api.HarvestableBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.registry.ModSounds;

import java.util.List;

public abstract class VineFruitBlock extends Block implements HarvestableBlock {

	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

	public VineFruitBlock(Properties prop) {
		super(prop);
	}

	protected abstract ItemLike getFruit(BlockState state);

	protected abstract BranchCropVineBlock getHanger();

	protected abstract IntegerProperty getAgeProperty();

	protected abstract int getMaxAge();

	protected abstract int getBaseAge();

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(getAgeProperty(), FACING);
	}

	@Override
	public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
		return getFruit(state).asItem().getDefaultInstance();
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (state.getValue(getAgeProperty()) == getMaxAge()) {
			if (!level.isClientSide()) {
				pickup(state, level, pos);
				var up = pos.above();
				var branch = level.getBlockState(up);
				if (branch.is(getHanger()) && branch.getValue(getHanger().getAgeProperty()) == getHanger().getMaxAge()) {
					getHanger().pickup(branch, level, up);
				}
			}
			level.playSound(player, pos, ModSounds.ITEM_TOMATO_PICK_FROM_BUSH.get(), SoundSource.BLOCKS, 1.0F, 0.8F + level.random.nextFloat() * 0.4F);
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

	protected void pickup(BlockState state, Level level, BlockPos pos) {
		int quantity = 1 + level.random.nextInt(2);
		popResource(level, pos, new ItemStack(getFruit(state), quantity));
		level.setBlock(pos, state.setValue(getAgeProperty(), getBaseAge()), 2);
	}


	public BlockState updateShape(BlockState state, Direction dir, BlockState fstate, LevelAccessor level, BlockPos pos, BlockPos fpos) {
		return !state.canSurvive(level, pos) ? Blocks.AIR.defaultBlockState() :
				super.updateShape(state, dir, fstate, level, pos, fpos);
	}

	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		BlockPos up = pos.above();
		if (state.getBlock() == this) //Forge: This function is called during world gen and placement, before this block is set, so if we are not 'here' then assume it's the pre-check.
			return level.getBlockState(up).is(getHanger());
		return true;
	}

	public boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
		return state.getFluidState().isEmpty();
	}

	public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type) {
		return type == PathComputationType.AIR && !this.hasCollision ? true : super.isPathfindable(state, level, pos, type);
	}

	@Override
	public @Nullable HarvestResult getHarvestResult(Level level, BlockState state, BlockPos pos) {
		if (state.getValue(getAgeProperty()) < getMaxAge()) return null;
		int quantity = 1 + level.random.nextInt(2);
		return new HarvestResult(state.setValue(getAgeProperty(), getBaseAge()),
				List.of(new ItemStack(getFruit(state), quantity)));
	}


}
