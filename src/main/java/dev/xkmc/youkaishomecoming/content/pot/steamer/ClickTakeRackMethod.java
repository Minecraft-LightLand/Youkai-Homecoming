package dev.xkmc.youkaishomecoming.content.pot.steamer;

import dev.xkmc.l2modularblock.mult.UseItemOnBlockMethod;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class ClickTakeRackMethod implements UseItemOnBlockMethod {

	@Override
	public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (!player.isShiftKeyDown()) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
		RackInfo info = RackInfo.getRackInfo(state);
		if (info.racks() == 0) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
		if ((hit.getLocation().y() + 128) % 1 < (info.height() - 1) * 0.25 + 1e-3) {
			if (info.height() < 4 || hit.getDirection() != Direction.UP)
				return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
		}
		if (info.tryTakeRack(level, pos, state)) {
			if (!level.isClientSide()) {
				player.getInventory().placeItemBackInInventory(YHBlocks.STEAMER_RACK.asStack());
			}
			return ItemInteractionResult.SUCCESS;
		}
		return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
	}

}
