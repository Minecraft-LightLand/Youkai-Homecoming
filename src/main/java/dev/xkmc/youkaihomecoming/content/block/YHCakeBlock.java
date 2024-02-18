
package dev.xkmc.youkaihomecoming.content.block;

import dev.xkmc.youkaihomecoming.content.item.FleshFoodItem;
import dev.xkmc.youkaihomecoming.content.item.YHFoodItem;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.Configuration;

import java.util.List;
import java.util.function.Supplier;

public class YHCakeBlock extends CakeBlock {
	private final Supplier<Item> food;

	public YHCakeBlock(Supplier<Item> food, Properties properties) {
		super(properties);
		this.food = food;
	}

	public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
		ItemStack stack = player.getItemInHand(handIn);
		Item item = stack.getItem();
		if (stack.is(ItemTags.CANDLES) && state.getValue(BITES) == 0) {
			Block block = Block.byItem(item);
			if (block instanceof CandleBlock && YHCandleCakeBlock.hasEntry(block, this)) {
				if (!player.isCreative()) {
					stack.shrink(1);
				}
				worldIn.playSound(null, pos, SoundEvents.CAKE_ADD_CANDLE, SoundSource.BLOCKS, 1.0F, 1.0F);
				worldIn.setBlockAndUpdate(pos, YHCandleCakeBlock.byCandle(block, this));
				worldIn.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
				player.awardStat(Stats.ITEM_USED.get(item));
				return InteractionResult.SUCCESS;
			}
		}

		if (worldIn.isClientSide) {
			ItemStack itemstack = player.getItemInHand(handIn);
			if (this.eatSlice(worldIn, pos, state, player).consumesAction()) {
				return InteractionResult.SUCCESS;
			}

			if (itemstack.isEmpty()) {
				return InteractionResult.CONSUME;
			}
		}

		return this.eatSlice(worldIn, pos, state, player);
	}

	public InteractionResult eatSlice(Level level, BlockPos pos, BlockState state, Player player) {
		if (!player.canEat(false)) {
			return InteractionResult.PASS;
		} else {
			player.awardStat(Stats.EAT_CAKE_SLICE);
			if (!level.isClientSide()) {
				player.eat(level, food.get().getDefaultInstance());
				if (food.get() instanceof FleshFoodItem flesh) {
					flesh.consume(player);
				}
			}

			int i = state.getValue(BITES);
			level.gameEvent(player, GameEvent.EAT, pos);
			if (i < 6) {
				level.setBlock(pos, state.setValue(BITES, i + 1), 3);
			} else {
				level.removeBlock(pos, false);
				level.gameEvent(player, GameEvent.BLOCK_DESTROY, pos);
			}
			return InteractionResult.SUCCESS;
		}
	}

	@Override
	public void appendHoverText(ItemStack pStack, @Nullable BlockGetter pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
		super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
		if (Configuration.FOOD_EFFECT_TOOLTIP.get())
			YHFoodItem.getFoodEffects(food.get().getFoodProperties(), pTooltip);
	}

}
