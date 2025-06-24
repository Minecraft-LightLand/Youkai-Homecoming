package dev.xkmc.youkaishomecoming.content.block.plant.grape;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class GrapeCropBlock extends DoubleRopeCropBlock {

	private final ItemLike seed, fruit;

	public GrapeCropBlock(Properties properties, ItemLike seed, ItemLike fruit) {
		super(properties);
		this.seed = seed;
		this.fruit = fruit;
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
	public int getDoubleBlockStart() {
		return 4;
	}

	@Override
	public int getBaseAge() {
		return 5;
	}

	@Override
	public int getMaxAge() {
		return 9;
	}

	@Override
	protected int getBonemealAgeIncrease(Level level) {
		return 1;
	}

	@Override
	protected boolean mayGrowTo(BlockState state, ServerLevel level, BlockPos pos, int age) {
		if (age >= 2) {
			if (!state.getValue(ROPELOGGED))
				return false;
		}
		if (age >= getDoubleBlockStart()) {
			BlockState upper = level.getBlockState(pos.above());
			if (!upper.isAir() && !upper.is(this))
				return false;
		}
		return super.mayGrowTo(state, level, pos, age);
	}

	@Override
	protected void growTo(BlockState state, ServerLevel level, BlockPos pos, int age) {
		super.growTo(state, level, pos, age);
		if (age >= getDoubleBlockStart()) {
			level.setBlock(pos.above(), state.setValue(getAgeProperty(), age).setValue(ROOT, false), 2);
		}
	}

}
