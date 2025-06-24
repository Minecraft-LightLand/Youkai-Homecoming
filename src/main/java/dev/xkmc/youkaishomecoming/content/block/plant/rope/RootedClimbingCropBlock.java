package dev.xkmc.youkaishomecoming.content.block.plant.rope;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.tag.ModTags;

public class RootedClimbingCropBlock extends RopeClimbingCropBlock {

	public static final BooleanProperty BASE = BooleanProperty.create("rooted");

	private final ItemLike seed, fruit;

	public RootedClimbingCropBlock(Properties properties, ItemLike seed, ItemLike fruit) {
		super(properties);
		this.seed = seed;
		this.fruit = fruit;
		registerDefaultState(stateDefinition.any()
				.setValue(getAgeProperty(), 0)
				.setValue(ROPELOGGED, false)
				.setValue(BASE, true)
		);
	}

	@Override
	protected ItemLike getBaseSeedId() {
		return seed;
	}

	@Override
	protected ItemLike getFruit() {
		return fruit;
	}

	@Override
	public int getBaseAge() {
		return 4;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(BASE);
	}

	@Override
	protected BlockState climbingVine() {
		return defaultBlockState().setValue(BASE, false);
	}

	@Override
	protected boolean mayClimb(ServerLevel level, BlockPos pos, RandomSource random, BlockPos cpos) {
		if (level.getBlockState(pos).getValue(getAgeProperty()) <= 3)
			return false;
		if (random.nextFloat() > 0.3F) return false;
		BlockState cstate = level.getBlockState(cpos);
		boolean canClimb = Configuration.ENABLE_TOMATO_VINE_CLIMBING_TAGGED_ROPES.get() ? cstate.is(ModTags.ROPES) : cstate.is(ModBlocks.ROPE.get());
		if (!canClimb) return false;
		BlockState self = level.getBlockState(pos);
		if (!self.is(this) || !self.getValue(ROPELOGGED)) return false;
		if (self.is(this) && self.getValue(BASE)) return true;
		BlockState rstate = level.getBlockState(pos.below());
		return rstate.is(this) && rstate.getValue(BASE);
	}

	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		if (state.getValue(BASE)) {
			return super.canSurvive(state, level, pos);
		} else {
			BlockPos down = pos.below();
			BlockState downState = level.getBlockState(down);
			return downState.is(this) && hasGoodCropConditions(level, pos);
		}
	}

}
