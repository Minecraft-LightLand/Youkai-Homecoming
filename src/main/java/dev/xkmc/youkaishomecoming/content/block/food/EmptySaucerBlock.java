package dev.xkmc.youkaishomecoming.content.block.food;

import dev.xkmc.youkaishomecoming.init.food.Saucer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class EmptySaucerBlock extends BaseSaucerBlock {

	public static final EnumProperty<Saucer> TYPE = EnumProperty.create("type", Saucer.class);
	protected static final VoxelShape[] SHAPE_BY_TYPE = new VoxelShape[]{
			Block.box(2.0D, 0.0D, 2.0D, 14.0D, 6.0D, 14.0D),
			Block.box(2.0D, 0.0D, 2.0D, 14.0D, 3.0D, 14.0D),
			Block.box(2.0D, 0.0D, 2.0D, 14.0D, 3.0D, 14.0D),
			Block.box(2.0D, 0.0D, 2.0D, 14.0D, 1.0D, 14.0D)};

	public EmptySaucerBlock(Properties pProperties) {
		super(pProperties);
	}

	@Override
	public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(TYPE);
	}

	@Override
	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		return SHAPE_BY_TYPE[pState.getValue(TYPE).ordinal()];
	}
}
