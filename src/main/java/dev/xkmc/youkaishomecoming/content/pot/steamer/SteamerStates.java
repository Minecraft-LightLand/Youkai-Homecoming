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
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class SteamerStates {

	public static final VoxelShape[] SHAPES;
	public static final BlockMethod POT = new Pot();
	public static final BlockMethod RACK = new Rack();
	public static final BlockMethod TE = new BlockEntityBlockMethodImpl<>(YHBlocks.STEAMER_BE, SteamerBlockEntity.class);
	public static final BlockMethod ADD_RACK = new ClickAddRackMethod();
	public static final BlockMethod TAKE_RACK = new ClickTakeRackMethod();
	public static final BlockMethod ADD_ITEM = new ClickAddItemMethod();
	public static final BlockMethod TAKE_ITEM = new ClickTakeItemMethod();

	static {
		SHAPES = new VoxelShape[4];
		for (int i = 0; i < 4; i++)
			SHAPES[i] = Block.box(2, 0, 2, 14, 4 * (i + 1), 14);
	}

	public static final IntegerProperty RACKS = IntegerProperty.create("racks", 1, 4);

	public static final IntegerProperty POT_RACKS = IntegerProperty.create("pot_racks", 0, 2);

	public static DelegateBlock createPotBlock() {
		return DelegateBlock.newBaseBlock(
				BlockBehaviour.Properties.of().strength(2f).sound(SoundType.METAL).mapColor(MapColor.METAL),
				POT, BlockProxy.HORIZONTAL, ADD_RACK, ADD_ITEM, TAKE_ITEM, TAKE_RACK, TE
		);
	}

	public static DelegateBlock createRackBlock() {
		return DelegateBlock.newBaseBlock(
				BlockBehaviour.Properties.of().strength(0.5f).sound(SoundType.BAMBOO).mapColor(MapColor.PLANT),
				RACK, BlockProxy.HORIZONTAL, ADD_RACK, ADD_ITEM, TAKE_ITEM, TAKE_RACK, TE
		);
	}

	public record Pot() implements CreateBlockStateBlockMethod, DefaultStateBlockMethod, ShapeBlockMethod {

		@Override
		public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
			builder.add(POT_RACKS);
		}

		@Override
		public BlockState getDefaultState(BlockState state) {
			return state.setValue(POT_RACKS, 0);
		}

		@Nullable
		@Override
		public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
			return SHAPES[state.getValue(POT_RACKS) + 1];
		}

	}

	public record Rack() implements CreateBlockStateBlockMethod, DefaultStateBlockMethod, ShapeBlockMethod, ShapeUpdateBlockMethod {

		@Override
		public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
			builder.add(RACKS);
		}

		@Override
		public BlockState getDefaultState(BlockState state) {
			return state.setValue(RACKS, 1);
		}

		@Nullable
		@Override
		public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
			return SHAPES[state.getValue(RACKS) - 1];
		}

		@Override
		public BlockState updateShape(
				Block block, BlockState current, BlockState state, Direction dir, BlockState neiState,
				LevelAccessor level, BlockPos pos, BlockPos neiPos) {
			if (dir == Direction.DOWN && !neiState.isSolid()) {
				return Blocks.AIR.defaultBlockState();
			}
			return current;
		}
	}


}
