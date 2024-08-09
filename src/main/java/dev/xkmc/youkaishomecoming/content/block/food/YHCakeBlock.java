package dev.xkmc.youkaishomecoming.content.block.food;

import dev.xkmc.youkaishomecoming.content.item.food.YHFoodItem;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import vectorwing.farmersdelight.common.Configuration;

import java.util.List;
import java.util.function.Supplier;

public class YHCakeBlock extends CakeBlock {
	private final Supplier<Item> food;

	public YHCakeBlock(Supplier<Item> food, Properties properties) {
		super(properties);
		this.food = food;
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
		if (level.isClientSide) {
			if (this.eatSlice(level, pos, state, player).consumesAction()) {
				return InteractionResult.SUCCESS;
			}
		}
		return this.eatSlice(level, pos, state, player).result();
	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		Item item = stack.getItem();
		if (stack.is(ItemTags.CANDLES) && state.getValue(BITES) == 0) {
			Block block = Block.byItem(item);
			if (block instanceof CandleBlock && YHCandleCakeBlock.hasEntry(block, this)) {
				if (!player.isCreative()) {
					stack.shrink(1);
				}
				level.playSound(null, pos, SoundEvents.CAKE_ADD_CANDLE, SoundSource.BLOCKS, 1.0F, 1.0F);
				level.setBlockAndUpdate(pos, YHCandleCakeBlock.byCandle(block, this));
				level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
				player.awardStat(Stats.ITEM_USED.get(item));
				return ItemInteractionResult.SUCCESS;
			}
		}
		return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
	}

	public ItemInteractionResult eatSlice(Level level, BlockPos pos, BlockState state, Player player) {
		if (!player.canEat(false)) {
			return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
		} else {
			player.awardStat(Stats.EAT_CAKE_SLICE);
			if (!level.isClientSide()) {
				player.eat(level, food.get().getDefaultInstance());
				if (food.get() instanceof IEatEffectFood flesh) {
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
			return ItemInteractionResult.SUCCESS;
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext ctx, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(stack, ctx, list, flag);
		if (Configuration.FOOD_EFFECT_TOOLTIP.get()) {
			var prop = food.get().getFoodProperties(stack, null);
			if (prop != null) YHFoodItem.getFoodEffects(prop, list);
		}
	}

}
