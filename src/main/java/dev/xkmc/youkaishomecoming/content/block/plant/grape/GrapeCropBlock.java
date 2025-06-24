package dev.xkmc.youkaishomecoming.content.block.plant.grape;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class GrapeCropBlock extends DoubleRopeCropBlock {

	public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 9);

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
	protected IntegerProperty getAgeProperty() {
		return AGE;
	}

	@Override
	public int getDoubleBlockStart() {
		return 4;
	}

	@Override
	public int getBaseAge() {
		return 6;
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
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(AGE, ROPELOGGED, ROOT);
	}

	@Override
	protected boolean mayGrowTo(BlockState state, LevelReader level, BlockPos pos, int age) {
		if (state.getValue(ROOT) && age >= 2) {
			if (!state.getValue(ROPELOGGED))
				return false;
		}
		return super.mayGrowTo(state, level, pos, age);
	}

}
