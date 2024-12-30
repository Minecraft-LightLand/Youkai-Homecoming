package dev.xkmc.youkaishomecoming.content.block.food;

import dev.xkmc.youkaishomecoming.init.food.IYHDish;
import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
import net.minecraft.core.BlockPos;
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

public class FoodSaucerBlock extends BaseSaucerBlock {

	private final VoxelShape shape;
	private final IYHDish base;

	public FoodSaucerBlock(Properties pProperties, IYHDish base) {
		super(pProperties);
		this.base = base;
		shape = Block.box(2.0D, 0.0D, 2.0D, 14.0D, base.height(), 14.0D);
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
		if (player.canEat(false)) {
			if (!level.isClientSide()) {
				player.eat(level, asItem().getDefaultInstance());
				level.setBlockAndUpdate(pos, YHItems.SAUCER.getDefaultState()
						.setValue(EmptySaucerBlock.TYPE, base.base())
						.setValue(BlockStateProperties.HORIZONTAL_FACING,
								state.getValue(BlockStateProperties.HORIZONTAL_FACING)));
			}
			return InteractionResult.CONSUME;
		}
		return super.useWithoutItem(state, level, pos, player, hit);
	}

	@Override
	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		return shape;
	}

}
