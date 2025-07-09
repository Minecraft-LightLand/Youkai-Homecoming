package dev.xkmc.youkaishomecoming.content.block.food;

import dev.xkmc.youkaishomecoming.init.food.YHDish;
import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class FoodSaucerBlock extends BaseSaucerBlock implements ISteamerContentBlock {

	public final YHDish dish;
	private final VoxelShape shape_x, shape_z;

	public FoodSaucerBlock(Properties pProperties, YHDish dish) {
		super(pProperties);
		this.dish = dish;
		var saucer = dish.saucer;
		shape_x = Block.box(saucer.x, 0, saucer.z, 16 - saucer.x, dish.height, 16 - saucer.z);
		shape_z = Block.box(saucer.z, 0, saucer.x, 16 - saucer.z, dish.height, 16 - saucer.x);
	}

	@Override
	public float clearance() {
		var saucer = dish.saucer;
		return Math.min(saucer.x, saucer.z);
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (asItem().getDefaultInstance().getFoodProperties(player) != null && player.canEat(false)) {
			if (!level.isClientSide()) {
				player.eat(level, asItem().getDefaultInstance());
				level.setBlockAndUpdate(pos, YHItems.SAUCER.getDefaultState()
						.setValue(EmptySaucerBlock.TYPE, dish.saucer)
						.setValue(BlockStateProperties.HORIZONTAL_FACING,
								state.getValue(BlockStateProperties.HORIZONTAL_FACING)));
			}
			return InteractionResult.SUCCESS;
		}
		return super.use(state, level, pos, player, hand, hit);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		boolean x = state.getValue(BlockStateProperties.HORIZONTAL_FACING).getAxis() == Direction.Axis.X;
		return x ? shape_x : shape_z;
	}

}
