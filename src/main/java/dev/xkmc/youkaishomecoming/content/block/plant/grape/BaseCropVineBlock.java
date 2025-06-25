package dev.xkmc.youkaishomecoming.content.block.plant.grape;

import dev.xkmc.l2harvester.api.HarvestResult;
import dev.xkmc.l2harvester.api.HarvestableBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.registry.ModSounds;

import java.util.List;

import static dev.xkmc.youkaishomecoming.content.block.plant.rope.RopeLoggedCropBlock.getRopeBlock;

@SuppressWarnings("deprecation")
public abstract class BaseCropVineBlock extends BushBlock implements HarvestableBlock {

	public static final BooleanProperty TOP = BooleanProperty.create("top");

	public BaseCropVineBlock(BlockBehaviour.Properties prop) {
		super(prop.randomTicks());
		this.registerDefaultState(this.stateDefinition.any()
				.setValue(this.getAgeProperty(), 0)
				.setValue(TOP, false)
		);
	}

	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(getAgeProperty(), TOP);
	}

	protected abstract IntegerProperty getAgeProperty();

	public abstract int getMaxAge();

	protected abstract int getBaseAge();

	protected abstract VineTrunkBlock getTrunk();

	protected abstract ItemLike getFruit();

	@Nullable
	protected abstract BlockPos getTrunk(BlockState state, BlockGetter level, BlockPos pos);

	public abstract ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state);

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (state.getValue(getAgeProperty()) == getMaxAge()) {
			if (!level.isClientSide()) {
				int quantity = 1 + level.random.nextInt(2);
				popResource(level, pos, new ItemStack(getFruit(), quantity));
				level.setBlock(pos, state.setValue(getAgeProperty(), getBaseAge()), 2);
			}
			level.playSound(player, pos, ModSounds.ITEM_TOMATO_PICK_FROM_BUSH.get(), SoundSource.BLOCKS, 1.0F, 0.8F + level.random.nextFloat() * 0.4F);
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		return getTrunk(state, level, pos) != null;
	}

	protected float getGrowthSpeed(BlockState state, BlockGetter level, BlockPos pos) {
		var trunk = getTrunk(state, level, pos);
		if (trunk == null)
			return 0;
		return getTrunk().getGrowthSpeed(level, pos);
	}

	protected boolean mayGrow(BlockState state, LevelReader level, BlockPos pos) {
		return level.getRawBrightness(pos, 0) >= 9;
	}

	protected boolean attemptGrowth(BlockState state, LevelReader level, BlockPos pos, @Nullable RandomSource random, boolean simulate, boolean natural, float speed) {
		int age = state.getValue(getAgeProperty());
		if (age < getMaxAge()) {
			if (simulate) return true;
			assert random != null;
			Level setter = (Level) level;
			if (!natural) {
				setter.setBlock(pos, state.setValue(getAgeProperty(), age + 1), 2);
				return true;
			} else if (ForgeHooks.onCropsGrowPre(setter, pos, state, random.nextFloat() < speed)) {
				setter.setBlock(pos, state.setValue(getAgeProperty(), age + 1), 2);
				ForgeHooks.onCropsGrowPost(setter, pos, state);
				return true;
			}
		}
		return false;
	}

	public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
		if (!level.isAreaLoaded(pos, 1)) return;
		float f = getGrowthSpeed(state, level, pos) * 0.04f;
		if (!mayGrow(state, level, pos)) return;
		attemptGrowth(state, level, pos, rand, false, true, f);
	}

	public void entityInside(BlockState state, Level level, BlockPos pos, Entity e) {
		if (e instanceof Ravager && ForgeEventFactory.getMobGriefingEvent(level, e)) {
			level.destroyBlock(pos, true, e);
		}
		super.entityInside(state, level, pos, e);
	}

	@Override
	public @Nullable HarvestResult getHarvestResult(Level level, BlockState state, BlockPos pos) {
		if (state.getValue(getAgeProperty()) < getMaxAge()) return null;
		int quantity = 1 + level.random.nextInt(2);
		return new HarvestResult(state.setValue(getAgeProperty(), getBaseAge()),
				List.of(new ItemStack(getFruit(), quantity)));
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
