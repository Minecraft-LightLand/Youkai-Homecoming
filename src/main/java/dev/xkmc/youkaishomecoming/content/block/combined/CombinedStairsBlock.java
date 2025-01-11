package dev.xkmc.youkaishomecoming.content.block.combined;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.EntryGroup;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelBuilder;
import net.neoforged.neoforge.client.model.generators.ModelFile;

public class CombinedStairsBlock extends Block {

	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
	public static final EnumProperty<Half> HALF = BlockStateProperties.HALF;
	public static final EnumProperty<StairsShape> SHAPE = BlockStateProperties.STAIRS_SHAPE;

	public CombinedStairsBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(
				this.stateDefinition
						.any()
						.setValue(FACING, Direction.NORTH)
						.setValue(HALF, Half.BOTTOM)
						.setValue(SHAPE, StairsShape.STRAIGHT)
		);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		Direction dir = ctx.getClickedFace();
		BlockPos pos = ctx.getClickedPos();
		boolean low = dir != Direction.DOWN && (dir == Direction.UP || !(ctx.getClickLocation().y - (double) pos.getY() > 0.5));
		return defaultBlockState()
				.setValue(FACING, ctx.getHorizontalDirection())
				.setValue(HALF, low ? Half.BOTTOM : Half.TOP);
	}

	@Override
	protected BlockState rotate(BlockState state, Rotation rot) {
		return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
	}

	@Override
	protected BlockState mirror(BlockState state, Mirror mirror) {
		Direction direction = state.getValue(FACING);
		StairsShape stairsshape = state.getValue(SHAPE);
		switch (mirror) {
			case LEFT_RIGHT:
				if (direction.getAxis() == Direction.Axis.Z) {
					switch (stairsshape) {
						case INNER_LEFT:
							return state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.INNER_RIGHT);
						case INNER_RIGHT:
							return state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.INNER_LEFT);
						case OUTER_LEFT:
							return state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.OUTER_RIGHT);
						case OUTER_RIGHT:
							return state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.OUTER_LEFT);
						default:
							return state.rotate(Rotation.CLOCKWISE_180);
					}
				}
				break;
			case FRONT_BACK:
				if (direction.getAxis() == Direction.Axis.X) {
					switch (stairsshape) {
						case INNER_LEFT:
							return state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.INNER_LEFT);
						case INNER_RIGHT:
							return state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.INNER_RIGHT);
						case OUTER_LEFT:
							return state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.OUTER_RIGHT);
						case OUTER_RIGHT:
							return state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.OUTER_LEFT);
						case STRAIGHT:
							return state.rotate(Rotation.CLOCKWISE_180);
					}
				}
		}

		return super.mirror(state, mirror);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, HALF, SHAPE);
	}

	@Override
	protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
		return false;
	}

	private static void cubeStair(ModelBuilder<?> builder) {
		builder.element()
				.from(0, 0, 0).to(16, 8, 16)
				.face(Direction.DOWN).texture("#t1").cullface(Direction.DOWN).end()
				.face(Direction.NORTH).texture("#s1").cullface(Direction.NORTH).end()
				.face(Direction.SOUTH).texture("#s1").cullface(Direction.SOUTH).end()
				.face(Direction.WEST).texture("#s1").cullface(Direction.WEST).end()
				.face(Direction.EAST).texture("#s1").cullface(Direction.EAST).end()
				.end()
				.element()
				.from(8, 8, 0).to(16, 16, 16)
				.face(Direction.UP).texture("#t1").cullface(Direction.UP).end()
				.face(Direction.NORTH).texture("#s1").cullface(Direction.NORTH).end()
				.face(Direction.SOUTH).texture("#s1").cullface(Direction.SOUTH).end()
				.face(Direction.EAST).texture("#s1").cullface(Direction.EAST).end()
				.end()
				.element()
				.from(0, 8, 0).to(8, 16, 16)
				.face(Direction.UP).texture("#t2").cullface(Direction.UP).end()
				.face(Direction.NORTH).texture("#s2").cullface(Direction.NORTH).end()
				.face(Direction.SOUTH).texture("#s2").cullface(Direction.SOUTH).end()
				.face(Direction.WEST).texture("#s2").cullface(Direction.WEST).end()
				.end()
				.texture("t1", "#top_a")
				.texture("s1", "#side_a")
				.texture("t2", "#top_b")
				.texture("s2", "#side_b");
	}

	private static void cubeOuter(ModelBuilder<?> builder) {
		builder.element()
				.from(0, 0, 0).to(16, 8, 16)
				.face(Direction.DOWN).texture("#t1").cullface(Direction.DOWN).end()
				.face(Direction.NORTH).texture("#s1").cullface(Direction.NORTH).end()
				.face(Direction.SOUTH).texture("#s1").cullface(Direction.SOUTH).end()
				.face(Direction.WEST).texture("#s1").cullface(Direction.WEST).end()
				.face(Direction.EAST).texture("#s1").cullface(Direction.EAST).end()
				.end()
				.element()
				.from(8, 8, 8).to(16, 16, 16)
				.face(Direction.UP).texture("#t1").cullface(Direction.UP).end()
				.face(Direction.SOUTH).texture("#s1").cullface(Direction.SOUTH).end()
				.face(Direction.EAST).texture("#s1").cullface(Direction.EAST).end()
				.end()
				.element()
				.from(0, 8, 0).to(8, 16, 16)
				.face(Direction.UP).texture("#t2").cullface(Direction.UP).end()
				.face(Direction.NORTH).texture("#s2").cullface(Direction.NORTH).end()
				.face(Direction.SOUTH).texture("#s2").cullface(Direction.SOUTH).end()
				.face(Direction.WEST).texture("#s2").cullface(Direction.WEST).end()
				.end()
				.element()
				.from(0, 8, 0).to(16, 16, 8)
				.face(Direction.UP).texture("#t2").cullface(Direction.UP).end()
				.face(Direction.NORTH).texture("#s2").cullface(Direction.NORTH).end()
				.face(Direction.EAST).texture("#s2").cullface(Direction.EAST).end()
				.face(Direction.WEST).texture("#s2").cullface(Direction.WEST).end()
				.end()
				.texture("t1", "#top_a")
				.texture("s1", "#side_a")
				.texture("t2", "#top_b")
				.texture("s2", "#side_b");
	}

	private static void cubeInner(ModelBuilder<?> builder) {
		builder.element()
				.from(0, 0, 0).to(16, 8, 16)
				.face(Direction.DOWN).texture("#t1").cullface(Direction.DOWN).end()
				.face(Direction.NORTH).texture("#s1").cullface(Direction.NORTH).end()
				.face(Direction.SOUTH).texture("#s1").cullface(Direction.SOUTH).end()
				.face(Direction.WEST).texture("#s1").cullface(Direction.WEST).end()
				.face(Direction.EAST).texture("#s1").cullface(Direction.EAST).end()
				.end()
				.element()
				.from(8, 8, 0).to(16, 16, 16)
				.face(Direction.UP).texture("#t1").cullface(Direction.UP).end()
				.face(Direction.NORTH).texture("#s1").cullface(Direction.NORTH).end()
				.face(Direction.SOUTH).texture("#s1").cullface(Direction.SOUTH).end()
				.face(Direction.EAST).texture("#s1").cullface(Direction.EAST).end()
				.end()
				.element()
				.from(0, 8, 8).to(16, 16, 16)
				.face(Direction.UP).texture("#t1").cullface(Direction.UP).end()
				.face(Direction.SOUTH).texture("#s1").cullface(Direction.SOUTH).end()
				.face(Direction.EAST).texture("#s1").cullface(Direction.EAST).end()
				.face(Direction.WEST).texture("#s1").cullface(Direction.WEST).end()
				.end()
				.element()
				.from(0, 8, 0).to(8, 16, 8)
				.face(Direction.UP).texture("#t2").cullface(Direction.UP).end()
				.face(Direction.NORTH).texture("#s2").cullface(Direction.NORTH).end()
				.face(Direction.WEST).texture("#s2").cullface(Direction.WEST).end()
				.end()
				.texture("t1", "#top_a")
				.texture("s1", "#side_a")
				.texture("t2", "#top_b")
				.texture("s2", "#side_b");
	}

	public static void stairsBlock(RegistrateBlockstateProvider pvd, CombinedStairsBlock block, ModelFile stairs, ModelFile stairsInner, ModelFile stairsOuter) {
		pvd.getVariantBuilder(block)
				.forAllStatesExcept(state -> {
					Direction facing = state.getValue(StairBlock.FACING);
					Half half = state.getValue(StairBlock.HALF);
					StairsShape shape = state.getValue(StairBlock.SHAPE);
					int yRot = (int) facing.getClockWise().toYRot(); // Stairs model is rotated 90 degrees clockwise for some reason
					if (shape == StairsShape.INNER_LEFT || shape == StairsShape.OUTER_LEFT) {
						yRot += 270; // Left facing stairs are rotated 90 degrees clockwise
					}
					if (shape != StairsShape.STRAIGHT && half == Half.TOP) {
						yRot += 90; // Top stairs are rotated 90 degrees clockwise
					}
					yRot %= 360;
					boolean uvlock = yRot != 0 || half == Half.TOP; // Don't set uvlock for states that have no rotation
					return ConfiguredModel.builder()
							.modelFile(shape == StairsShape.STRAIGHT ? stairs : shape == StairsShape.INNER_LEFT || shape == StairsShape.INNER_RIGHT ? stairsInner : stairsOuter)
							.rotationX(half == Half.BOTTOM ? 0 : 180)
							.rotationY(yRot)
							.uvLock(uvlock)
							.build();
				});
	}

	private static BlockModelBuilder stair = null, inner = null, outer = null;

	public static void buildStates(DataGenContext<Block, CombinedStairsBlock> ctx, RegistrateBlockstateProvider pvd, IBlockSet a, IBlockSet b) {
		if (stair == null) {
			stair = pvd.models().withExistingParent("combined_stairs", "block/block");
			cubeStair(stair);
			stair.texture("particle", "#top_a");
			inner = pvd.models().withExistingParent("combined_stairs_inner", "block/block");
			cubeInner(inner);
			inner.texture("particle", "#top_a");
			outer = pvd.models().withExistingParent("combined_stairs_outer", "block/block");
			cubeOuter(outer);
			outer.texture("particle", "#top_a");
		}
		ModelFile stairs = pvd.models().getBuilder(ctx.getName())
				.parent(stair)
				.texture("top_a", a.top())
				.texture("side_a", a.side())
				.texture("top_b", b.top())
				.texture("side_b", b.side());
		ModelFile stairsInner = pvd.models().getBuilder(ctx.getName() + "_inner")
				.parent(inner)
				.texture("top_a", a.top())
				.texture("side_a", a.side())
				.texture("top_b", b.top())
				.texture("side_b", b.side());

		ModelFile stairsOuter = pvd.models().getBuilder(ctx.getName() + "_outer")
				.parent(outer)
				.texture("top_a", a.top())
				.texture("side_a", a.side())
				.texture("top_b", b.top())
				.texture("side_b", b.side());
		stairsBlock(pvd, ctx.get(), stairs, stairsInner, stairsOuter);
	}

	public static void buildLoot(RegistrateBlockLootTables pvd, CombinedStairsBlock block, IBlockSet a, IBlockSet b) {
		pvd.add(block, LootTable.lootTable().withPool(LootPool.lootPool().add(
				EntryGroup.list(LootItem.lootTableItem(a.stairs().value()), LootItem.lootTableItem(b.vertical().value()))
		)));
	}

}
