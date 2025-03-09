package dev.xkmc.youkaishomecoming.content.pot.steamer;

import dev.xkmc.l2modularblock.mult.OnClickBlockMethod;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class ClickTakeItemMethod implements OnClickBlockMethod {

	@Override
	public InteractionResult onClick(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		RackInfo info = RackInfo.getRackInfo(state);
		if (info.racks() == 0) return InteractionResult.PASS;
		if (!(level.getBlockEntity(pos) instanceof SteamerBlockEntity be)) return InteractionResult.PASS;
		ItemStack stack = player.getItemInHand(hand);
		if (!stack.isEmpty() && !player.isShiftKeyDown()) return InteractionResult.PASS;
		int y = (int) ((hit.getLocation().y() + 128 - 1e-3) % 1 * 4);
		if (info.pot()) y -= 2;
		if (y < 0 || y >= be.racks.size()) return InteractionResult.PASS;
		var rack = be.racks.get(y);
		if (hit.getDirection() == Direction.UP) {
			if (rack.tryTakeItemAt(be, level, hit.getLocation(), player, hand)) {
				return InteractionResult.SUCCESS;
			}
			if (!player.isShiftKeyDown()) {
				return InteractionResult.PASS;
			}
		}
		if (rack.tryTakeItem(be, level, player, hand)) {
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}
}
