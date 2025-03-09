package dev.xkmc.youkaishomecoming.content.pot.steamer;

import dev.xkmc.l2modularblock.mult.OnClickBlockMethod;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class ClickAddRackMethod implements OnClickBlockMethod {

	@Override
	public InteractionResult onClick(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (player.isShiftKeyDown()) return InteractionResult.PASS;
		ItemStack stack = player.getItemInHand(hand);
		if (stack.is(YHBlocks.STEAMER_RACK.asItem())) {
			var info = RackInfo.getRackInfo(state);
			if (info.tryAddRack(level, pos, state)) {
				if (!level.isClientSide() && !player.getAbilities().instabuild) {
					stack.shrink(1);
				}
				return InteractionResult.CONSUME;
			}
		}
		return InteractionResult.PASS;
	}

}
