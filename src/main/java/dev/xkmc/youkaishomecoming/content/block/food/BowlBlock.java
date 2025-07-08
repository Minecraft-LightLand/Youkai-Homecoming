package dev.xkmc.youkaishomecoming.content.block.food;

import dev.xkmc.l2modularblock.mult.OnClickBlockMethod;
import dev.xkmc.l2modularblock.one.ShapeBlockMethod;
import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import static net.minecraft.world.level.block.Block.box;

public record BowlBlock(VoxelShape shape) implements ShapeBlockMethod, OnClickBlockMethod {

	public static final VoxelShape IRON_SHAPE = box(4, 0, 4, 12, 4, 12);
	public static final VoxelShape WOOD_SHAPE = box(5, 0, 5, 11, 3, 11);

	@Override
	public @Nullable VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
		return shape;
	}

	@Override
	public InteractionResult onClick(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (state.is(YHItems.WOOD_BOWL.get())) {
			if (!level.isClientSide()) {
				level.removeBlock(pos, false);
				player.getInventory().placeItemBackInInventory(Items.BOWL.getDefaultInstance());
			}
			return InteractionResult.SUCCESS;
		}
		if (state.is(YHItems.IRON_BOWL.get())) {
			if (!level.isClientSide()) {
				level.removeBlock(pos, false);
				player.getInventory().placeItemBackInInventory(YHItems.IRON_BOWL.asStack());
			}
			return InteractionResult.SUCCESS;
		}
		var stack = state.getBlock().asItem().getDefaultInstance();
		var food = stack.getFoodProperties(player);
		if (food != null && player.canEat(false)) {
			if (!level.isClientSide()) {
				player.eat(level, stack.copy());
				var cont = stack.getCraftingRemainingItem();
				if (cont.getItem() instanceof BlockItem block) {
					level.setBlockAndUpdate(pos, block.getBlock().defaultBlockState()
							.setValue(BlockStateProperties.HORIZONTAL_FACING,
									state.getValue(BlockStateProperties.HORIZONTAL_FACING)));
				} else {
					level.setBlockAndUpdate(pos, YHItems.WOOD_BOWL.getDefaultState()
							.setValue(BlockStateProperties.HORIZONTAL_FACING,
									state.getValue(BlockStateProperties.HORIZONTAL_FACING)));
				}
			}
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

}
