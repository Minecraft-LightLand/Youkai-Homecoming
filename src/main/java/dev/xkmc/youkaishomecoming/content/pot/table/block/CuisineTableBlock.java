package dev.xkmc.youkaishomecoming.content.pot.table.block;

import dev.xkmc.l2modularblock.mult.OnClickBlockMethod;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class CuisineTableBlock implements OnClickBlockMethod {


	@Override
	public InteractionResult onClick(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		ItemStack stack = player.getItemInHand(hand);
		if (level.getBlockEntity(pos) instanceof CuisineTableBlockEntity be) {
			if (be.addItem(stack)) {
				return InteractionResult.SUCCESS;
			} else if (stack.isEmpty() && be.addToPlayer(player)) {
				return InteractionResult.SUCCESS;
			}
		}
		return InteractionResult.PASS;
	}

}
