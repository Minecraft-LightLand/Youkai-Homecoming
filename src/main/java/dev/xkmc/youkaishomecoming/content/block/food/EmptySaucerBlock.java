package dev.xkmc.youkaishomecoming.content.block.food;

import dev.xkmc.youkaishomecoming.init.food.Saucer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class EmptySaucerBlock extends BaseSaucerBlock {

	public static final EnumProperty<Saucer> TYPE = EnumProperty.create("type", Saucer.class);
	protected static final VoxelShape[] SHAPE_X, SHAPE_Z;

	static {
		int n = Saucer.values().length;
		SHAPE_X = new VoxelShape[n];
		SHAPE_Z = new VoxelShape[n];
		for (int i = 0; i < n; i++) {
			var saucer = Saucer.values()[i];
			SHAPE_X[i] = Block.box(saucer.x, 0, saucer.z, 16 - saucer.x, saucer.height, 16 - saucer.z);
			SHAPE_Z[i] = Block.box(saucer.z, 0, saucer.x, 16 - saucer.z, saucer.height, 16 - saucer.x);
		}
	}

	public EmptySaucerBlock(Properties pProperties) {
		super(pProperties);
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
		if (!level.isClientSide()) {
			var old = state.getValue(TYPE);
			var next = Saucer.values()[(old.ordinal() + 1) % Saucer.values().length];
			level.setBlockAndUpdate(pos, state.setValue(TYPE, next));
		}
		return InteractionResult.SUCCESS;
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
