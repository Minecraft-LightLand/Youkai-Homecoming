package dev.xkmc.youkaishomecoming.content.block.food;

import dev.xkmc.youkaishomecoming.init.food.Saucer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class EmptySaucerBlock extends BaseSaucerBlock {

	public static final EnumProperty<Saucer> TYPE = EnumProperty.create("type", Saucer.class);
	protected static final VoxelShape[] SHAPE_X = new VoxelShape[]{
			Block.box(4, 0, 3, 12, 1, 13),
			Block.box(2, 0, 2, 14, 6, 14),
			Block.box(2, 0, 2, 14, 3, 14),
			Block.box(2, 0, 2, 14, 3, 14),
			Block.box(2, 0, 2, 14, 1, 14)};
	protected static final VoxelShape[] SHAPE_Z = new VoxelShape[]{
			Block.box(3, 0, 4, 13, 1, 12),
			Block.box(2, 0, 2, 14, 6, 14),
			Block.box(2, 0, 2, 14, 3, 14),
			Block.box(2, 0, 2, 14, 3, 14),
			Block.box(2, 0, 2, 14, 1, 14)};

	public EmptySaucerBlock(Properties pProperties) {
		super(pProperties);
	}

	@Override
	public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(TYPE);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		boolean x = state.getValue(BlockStateProperties.HORIZONTAL_FACING).getAxis() == Direction.Axis.X;
		return (x ? SHAPE_X : SHAPE_Z)[state.getValue(TYPE).ordinal()];
	}
}
