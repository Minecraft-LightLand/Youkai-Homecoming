package dev.xkmc.youkaishomecoming.content.pot.cooking.core;

import com.tterrag.registrate.util.entry.BlockEntry;
import dev.xkmc.l2modularblock.core.BlockTemplates;
import dev.xkmc.l2modularblock.mult.UseItemOnBlockMethod;
import dev.xkmc.youkaishomecoming.content.item.fluid.SlipBottleItem;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public record PotClick(
		BlockEntry<?> block
) implements UseItemOnBlockMethod {

	@Override
	public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (level.getBlockEntity(pos) instanceof CookingBlockEntity be) {
			if (player.isShiftKeyDown()) {
				be.dumpInventory();
				player.playSound(SoundEvents.ITEM_PICKUP, 1, 1);
				var bowl = block.getDefaultState().setValue(BlockTemplates.HORIZONTAL_FACING,
						state.getValue(BlockTemplates.HORIZONTAL_FACING));
				level.setBlockAndUpdate(pos, bowl);
				return ItemInteractionResult.SUCCESS;
			}
			if (be.tryAddItem(stack, level.isClientSide())) {
				if (!level.isClientSide) be.setLastPlayer(player);
				if (!level.isClientSide && !player.getAbilities().instabuild) {
					if (stack.isDamageableItem()) {
						stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
					} else if (SlipBottleItem.isSlipContainer(stack)) {
						var toConsume = stack.split(1);
						player.setItemInHand(hand, SlipBottleItem.drain(toConsume));
						player.getInventory().placeItemBackInInventory(stack);
					} else {
						ItemStack cont = stack.getCraftingRemainingItem();
						stack.shrink(1);
						if (!cont.isEmpty()) {
							player.getInventory().placeItemBackInInventory(cont.copyWithCount(1));
						}
					}
				}
				player.playSound(SoundEvents.WOOL_PLACE, 0.8F, 1.0F);
				return ItemInteractionResult.SUCCESS;
			}
		}
		return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
	}


}
