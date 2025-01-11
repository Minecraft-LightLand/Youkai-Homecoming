package dev.xkmc.youkaishomecoming.content.block.combined;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.EntryGroup;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
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

	private static void stairsBlockInternal(RegistrateBlockstateProvider pvd, CombinedStairsBlock block, String baseName, ResourceLocation side, ResourceLocation bottom, ResourceLocation top) {
		ModelFile stairs = pvd.models().stairs(baseName, side, bottom, top);
		ModelFile stairsInner = pvd.models().stairsInner(baseName + "_inner", side, bottom, top);
		ModelFile stairsOuter = pvd.models().stairsOuter(baseName + "_outer", side, bottom, top);
		stairsBlock(pvd, block, stairs, stairsInner, stairsOuter);
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


	public static void buildStates(DataGenContext<Block, CombinedStairsBlock> ctx, RegistrateBlockstateProvider pvd, IBlockSet a, IBlockSet b) {
		pvd.stairsBlock(null, a.side(), a.top(), a.top());
	}

	public static void buildLoot(RegistrateBlockLootTables pvd, CombinedStairsBlock block, IBlockSet a, IBlockSet b) {
		pvd.add(block, LootTable.lootTable().withPool(LootPool.lootPool().add(
				EntryGroup.list(LootItem.lootTableItem(a.stairs().value()), LootItem.lootTableItem(b.vertical().value()))
		)));
	}

}
