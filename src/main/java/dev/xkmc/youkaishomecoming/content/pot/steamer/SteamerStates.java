package dev.xkmc.youkaishomecoming.content.pot.steamer;

import dev.xkmc.l2modularblock.BlockProxy;
import dev.xkmc.l2modularblock.DelegateBlock;
import dev.xkmc.l2modularblock.impl.BlockEntityBlockMethodImpl;
import dev.xkmc.l2modularblock.mult.CreateBlockStateBlockMethod;
import dev.xkmc.l2modularblock.mult.DefaultStateBlockMethod;
import dev.xkmc.l2modularblock.mult.ShapeUpdateBlockMethod;
import dev.xkmc.l2modularblock.one.ShapeBlockMethod;
import dev.xkmc.l2modularblock.type.BlockMethod;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class SteamerStates {

	public static final VoxelShape[] SHAPES_NO_LID, SHAPES_WITH_LID;
	public static final BlockMethod POT = new Pot();
	public static final BlockMethod RACK = new Rack();
	public static final BlockMethod LID = new Lid();
	public static final BlockMethod TE = new BlockEntityBlockMethodImpl<>(YHBlocks.STEAMER_BE, SteamerBlockEntity.class);
	public static final BlockMethod ADD_RACK = new ClickAddRackMethod();
	public static final BlockMethod TAKE_RACK = new ClickTakeRackMethod();
	public static final BlockMethod ADD_ITEM = new ClickAddItemMethod();
	public static final BlockMethod TAKE_ITEM = new ClickTakeItemMethod();
	public static final BlockMethod ADD_WATER = new ClickAddWaterMethod();
	public static final BlockMethod ADD_LID = new ClickAddLidMethod();
	public static final BlockMethod TAKE_LID = new ClickRemoveLidMethod();

	static {
		SHAPES_NO_LID = new VoxelShape[4];
		SHAPES_WITH_LID = new VoxelShape[4];
		for (int i = 0; i < 4; i++) {
			SHAPES_NO_LID[i] = Block.box(2, 0, 2, 14, 4 * i + 4, 14);
			SHAPES_WITH_LID[i] = Block.box(2, 0, 2, 14, 4 * i + 1, 14);
		}
	}

	public static final IntegerProperty RACKS = IntegerProperty.create("racks", 1, 4);
	public static final IntegerProperty POT_RACKS = IntegerProperty.create("pot_racks", 0, 2);
	public static final BooleanProperty WATER = BooleanProperty.create("water");
	public static final BooleanProperty CAPPED = BooleanProperty.create("capped");

	public static DelegateBlock createPotBlock() {
		return DelegateBlock.newBaseBlock(
				BlockBehaviour.Properties.of().strength(2f).forceSolidOn()
						.sound(SoundType.METAL).mapColor(MapColor.METAL),
				POT, BlockProxy.HORIZONTAL,
				ADD_LID, TAKE_LID, ADD_WATER, ADD_RACK, ADD_ITEM, TAKE_ITEM, TAKE_RACK, TE
		);
	}

	public static DelegateBlock createRackBlock() {
		return DelegateBlock.newBaseBlock(
				BlockBehaviour.Properties.of().strength(0.5f).forceSolidOn()
						.sound(SoundType.BAMBOO).mapColor(MapColor.PLANT),
				RACK, BlockProxy.HORIZONTAL,
				ADD_LID, TAKE_LID, ADD_RACK, ADD_ITEM, TAKE_ITEM, TAKE_RACK, TE
		);
	}

	public static DelegateBlock createLidBlock() {
		return DelegateBlock.newBaseBlock(
				BlockBehaviour.Properties.of().strength(0.5f).forceSolidOn()
						.sound(SoundType.WOOD).mapColor(MapColor.WOOD),
				LID, BlockProxy.HORIZONTAL, TAKE_LID
		);
	}

	public record Pot() implements CreateBlockStateBlockMethod, DefaultStateBlockMethod, ShapeBlockMethod {

		@Override
		public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
			builder.add(POT_RACKS, CAPPED, WATER);
		}

		@Override
		public BlockState getDefaultState(BlockState state) {
			return state.setValue(POT_RACKS, 0).setValue(CAPPED, false).setValue(WATER, false);
		}

		@Nullable
		@Override
		public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
			if (state.getValue(CAPPED)) {
				return SHAPES_WITH_LID[Math.min(3, state.getValue(POT_RACKS) + 2)];
			}
			return SHAPES_NO_LID[state.getValue(POT_RACKS) + 1];
		}

	}

	public record Rack() implements CreateBlockStateBlockMethod, DefaultStateBlockMethod,
			ShapeBlockMethod, ShapeUpdateBlockMethod {

		@Override
		public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
			builder.add(RACKS, CAPPED);
		}

		@Override
		public BlockState getDefaultState(BlockState state) {
			return state.setValue(RACKS, 1).setValue(CAPPED, false);
		}

		@Nullable
		@Override
		public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
			if (state.getValue(CAPPED)) {
				return SHAPES_WITH_LID[Math.min(3, state.getValue(RACKS))];
			}
			return SHAPES_NO_LID[state.getValue(RACKS) - 1];
		}

		@Override
		public BlockState updateShape(
				Block block, BlockState current, BlockState state, Direction dir, BlockState neiState,
				LevelAccessor level, BlockPos pos, BlockPos neiPos) {
			if (dir == Direction.DOWN) {
				if (!neiState.isCollisionShapeFullBlock(level, neiPos) &&
						!Block.canSupportCenter(level, neiPos, Direction.UP))
					return Blocks.AIR.defaultBlockState();
			}
			return current;
		}
	}

	public record Lid() implements ShapeBlockMethod {

		@Nullable
		@Override
		public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
			return SHAPES_WITH_LID[0];
		}

	}

}
