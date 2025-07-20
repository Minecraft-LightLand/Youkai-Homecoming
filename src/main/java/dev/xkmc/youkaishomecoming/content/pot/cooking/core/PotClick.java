package dev.xkmc.youkaishomecoming.content.pot.cooking.core;

import com.tterrag.registrate.util.entry.BlockEntry;
import dev.xkmc.l2modularblock.BlockProxy;
import dev.xkmc.l2modularblock.mult.OnClickBlockMethod;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public record PotClick(
		BlockEntry<?> block
) implements OnClickBlockMethod {

	@Override
	public InteractionResult onClick(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		ItemStack stack = player.getItemInHand(hand);
		if (level.getBlockEntity(pos) instanceof CookingBlockEntity be) {
			if (player.isShiftKeyDown()) {
				be.dumpInventory();
				player.playSound(SoundEvents.ITEM_PICKUP, 1, 1);
				var bowl = block.getDefaultState().setValue(BlockProxy.HORIZONTAL_FACING,
						state.getValue(BlockProxy.HORIZONTAL_FACING));
				level.setBlockAndUpdate(pos, bowl);
				return InteractionResult.SUCCESS;
			}
			if (be.tryAddItem(stack, level.isClientSide())) {
				if (!level.isClientSide) be.setLastPlayer(player);
				if (!level.isClientSide && !player.getAbilities().instabuild) {
					if (stack.isDamageableItem()) {
						stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
					} else {
						ItemStack cont = stack.getCraftingRemainingItem();
						stack.shrink(1);
						if (!cont.isEmpty()) {
							player.getInventory().placeItemBackInInventory(cont.copyWithCount(1));
						}
					}
				}
				player.playSound(SoundEvents.WOOL_PLACE, 0.8F, 1.0F);
				return InteractionResult.SUCCESS;
			}
		}
		return InteractionResult.PASS;
	}


}
