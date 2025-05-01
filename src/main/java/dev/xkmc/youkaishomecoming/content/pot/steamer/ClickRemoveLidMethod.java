package dev.xkmc.youkaishomecoming.content.pot.steamer;

import dev.xkmc.l2modularblock.mult.UseItemOnBlockMethod;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class ClickRemoveLidMethod implements UseItemOnBlockMethod {

	@Override
	public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (state.is(YHBlocks.STEAMER_LID.get())) {
			if (!level.isClientSide()) {
				level.removeBlock(pos, false);
			}
		} else {
			if (RackInfo.ofY(hit) != RackInfo.getRackInfo(state).height() || !RackInfo.tryRemoveCap(level, pos, state))
				return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
		}
		if (!level.isClientSide()) {
			if (stack.isEmpty() && !player.isShiftKeyDown()) {
				player.setItemInHand(hand, YHBlocks.STEAMER_LID.asStack());
			} else {
				player.getInventory().placeItemBackInInventory(YHBlocks.STEAMER_LID.asStack());
			}
		}
		return ItemInteractionResult.SUCCESS;
	}

}
