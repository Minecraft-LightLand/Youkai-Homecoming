package dev.xkmc.youkaishomecoming.content.pot.steamer;

import dev.xkmc.l2modularblock.mult.UseItemOnBlockMethod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class ClickTakeItemMethod implements UseItemOnBlockMethod {

	@Override
	public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		RackInfo info = RackInfo.getRackInfo(state);
		if (info.racks() == 0) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
		if (!(level.getBlockEntity(pos) instanceof SteamerBlockEntity be))
			return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
		int y = RackInfo.ofY(hit);
		if (info.pot()) y -= 2;
		if (y < 0 || y >= be.racks.size()) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
		var rack = be.racks.get(y);
		if (hit.getDirection() == Direction.UP && !RackInfo.isCapped(level, pos)) {
			if (rack.tryTakeItemAt(be, level, hit.getLocation(), player, hand)) {
				return ItemInteractionResult.SUCCESS;
			}
			if (!player.isShiftKeyDown()) {
				return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
			}
		}
		if (rack.tryTakeItem(be, level, player, hand)) {
			return ItemInteractionResult.SUCCESS;
		}
		return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
	}
}
