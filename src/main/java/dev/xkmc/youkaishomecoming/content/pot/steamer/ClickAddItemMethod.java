package dev.xkmc.youkaishomecoming.content.pot.steamer;

import dev.xkmc.l2modularblock.mult.OnClickBlockMethod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class ClickAddItemMethod implements OnClickBlockMethod {

	@Override
	public InteractionResult onClick(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (player.isShiftKeyDown()) return InteractionResult.PASS;
		ItemStack stack = player.getItemInHand(hand);
		if (stack.isEmpty()) return InteractionResult.PASS;
		RackInfo info = RackInfo.getRackInfo(state);
		if (info.racks() == 0) return InteractionResult.PASS;
		if (!(level.getBlockEntity(pos) instanceof SteamerBlockEntity be)) return InteractionResult.PASS;
		if (!RackData.isValid(level, stack)) return InteractionResult.PASS;
		int y = RackInfo.ofY(hit);
		if (info.pot()) y -= 2;
		if (y < 0 || y >= be.racks.size()) return InteractionResult.PASS;
		var rack = be.racks.get(y);
		if (hit.getDirection() == Direction.UP && !RackInfo.isCapped(level, pos)) {
			if (rack.tryAddItemAt(be, level, stack, hit.getLocation())) {
				return InteractionResult.SUCCESS;
			}
		}
		if (rack.tryAddItem(be, level, stack)) {
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}
}
