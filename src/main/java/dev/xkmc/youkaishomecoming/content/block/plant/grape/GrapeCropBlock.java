package dev.xkmc.youkaishomecoming.content.block.plant.grape;

import dev.xkmc.youkaishomecoming.init.food.YHCrops;
import dev.xkmc.youkaishomecoming.init.registrate.YHCriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ToolActions;
import vectorwing.farmersdelight.common.registry.ModBlocks;

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

	private final YHCrops crop;

	public GrapeCropBlock(Properties properties, YHCrops crop) {
		super(properties);
		this.crop = crop;
	}

	@Override
	protected ItemLike getBaseSeedId() {
		return crop.getSeed();
	}

	@Override
	protected ItemLike getFruit() {
		return crop.getFruits();
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

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		ItemStack stack = player.getItemInHand(hand);
		if (stack.canPerformAction(ToolActions.SHEARS_HARVEST)) {
			if (state.getValue(getAgeProperty()) >= getBaseAge()) {
				if (!level.isClientSide()) {
					if (!state.getValue(ROOT)) {
						pos = pos.below();
					}
					var up = level.getBlockState(pos.above());
					var empty = up.getValue(ROPELOGGED) ? getRopeBlock() : Blocks.AIR.defaultBlockState();
					level.setBlock(pos, crop.set.trunk.getDefaultState(), 2);
					level.setBlock(pos.above(), empty, 35);
					var down = level.getBlockState(pos.below());
					if (down.is(Blocks.FARMLAND)) {
						level.setBlock(pos.below(), Blocks.DIRT.defaultBlockState(), 2);
					} else if (down.is(ModBlocks.RICH_SOIL_FARMLAND.get())) {
						level.setBlock(pos.below(), ModBlocks.RICH_SOIL.get().defaultBlockState(), 2);
					}
					if (!player.getAbilities().instabuild) {
						stack.hurtAndBreak(1, player, e -> e.broadcastBreakEvent(hand));
					}
					if (player instanceof ServerPlayer sp) {
						YHCriteriaTriggers.GRAPE_CUT.trigger(sp);
					}
				}
				return InteractionResult.SUCCESS;
			}
		}
		return super.use(state, level, pos, player, hand, hit);
	}
}
