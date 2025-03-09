package dev.xkmc.youkaishomecoming.content.pot.steamer;

import dev.xkmc.l2modularblock.mult.OnClickBlockMethod;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class ClickTakeRackMethod implements OnClickBlockMethod {

	@Override
	public InteractionResult onClick(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (!player.isShiftKeyDown()) return InteractionResult.PASS;
		RackInfo info = RackInfo.getRackInfo(state);
		if (info.racks() == 0) return InteractionResult.PASS;
		if ((hit.getLocation().y() + 128) % 1 < (info.height() - 1) * 0.25 + 1e-3) {
			if (info.height() < 4 || hit.getDirection() != Direction.UP)
				return InteractionResult.PASS;
		}
		if (info.tryTakeRake(level, pos, state)) {
			if (!level.isClientSide()) {
				player.getInventory().placeItemBackInInventory(YHBlocks.STEAMER_RACK.asStack());
			}
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

}
