package dev.xkmc.youkaishomecoming.content.block.plant.grape;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class GrapeCropBlock extends DoubleRopeCropBlock {

	public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 9);

	public static final VoxelShape[] LOWER, UPPER, ROPE_LOWER, ROPE_UPPER;

	static {
		LOWER = new VoxelShape[10];
		UPPER = new VoxelShape[10];
		ROPE_LOWER = new VoxelShape[10];
		ROPE_UPPER = new VoxelShape[10];
		var rod = box(7, 0, 7, 9, 16, 9);

		LOWER[0] = box(0, 0, 2, 12, 6, 14);
		LOWER[1] = box(0, 0, 2, 12, 9, 14);
		LOWER[2] = box(0, 0, 2, 12, 14, 14);
		LOWER[3] = box(0, 0, 2, 12, 15, 14);
		for (int i = 0; i < 4; i++) {
			ROPE_LOWER[i] = Shapes.or(LOWER[i], rod);
			UPPER[i] = Shapes.empty();
			ROPE_UPPER[i] = rod;
		}
		var full = box(0, 0, 2, 12, 16, 14);
		for (int i = 4; i < 10; i++) {
			ROPE_LOWER[i] = LOWER[i] = full;
		}

		UPPER[4] = UPPER[5] = LOWER[1];
		ROPE_UPPER[4] = ROPE_UPPER[5] = ROPE_LOWER[1];
		var top = box(0, 0, 2, 12, 11, 14);
		var rope_top = Shapes.or(top, rod);
		for (int i = 6; i < 10; i++) {
			UPPER[i] = top;
			ROPE_UPPER[i] = rope_top;
		}
	}

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
	protected boolean mayGrowTo(BlockState state, LevelReader level, BlockPos pos, int age) {
		if (state.getValue(ROOT) && age >= 2) {
			if (!state.getValue(ROPELOGGED))
				return false;
		}
		return super.mayGrowTo(state, level, pos, age);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		int age = state.getValue(getAgeProperty());
		boolean rope = state.getValue(ROPELOGGED);
		boolean base = state.getValue(ROOT);
		return (rope ? base ? ROPE_LOWER : ROPE_UPPER : base ? LOWER : UPPER)[age];
	}

}
