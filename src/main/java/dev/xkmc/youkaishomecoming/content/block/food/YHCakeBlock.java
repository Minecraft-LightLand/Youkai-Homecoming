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
import vectorwing.farmersdelight.common.tag.ForgeTags;
import vectorwing.farmersdelight.common.utility.ItemUtils;

import java.util.List;
import java.util.function.Supplier;

public class YHCakeBlock extends CakeBlock {
	private final Supplier<Item> food;

	public YHCakeBlock(Supplier<Item> food, Properties properties) {
		super(properties);
		this.food = food;
	}

	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
		ItemStack stack = player.getItemInHand(handIn);
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
				return InteractionResult.SUCCESS;
			}
		}
		if (stack.is(ForgeTags.TOOLS_KNIVES)) {
			if (!level.isClientSide()) {
				int bite = state.getValue(BITES);
				if (bite < MAX_BITES) level.setBlockAndUpdate(pos, state.setValue(BITES, bite + 1));
				else level.removeBlock(pos, false);
				ItemUtils.spawnItemEntity(level, food.get().getDefaultInstance(),
						pos.getX() + bite * 0.1, pos.getY() + 0.2, pos.getZ() + 0.5,
						-0.05, 0.0, 0.0);
				level.playSound(null, pos, SoundEvents.WOOL_BREAK, SoundSource.PLAYERS, 0.8F, 0.8F);
			}
			return InteractionResult.SUCCESS;
		}

		if (level.isClientSide) {
			ItemStack itemstack = player.getItemInHand(handIn);
			if (this.eatSlice(level, pos, state, player).consumesAction()) {
				return InteractionResult.SUCCESS;
			}

			if (itemstack.isEmpty()) {
				return InteractionResult.CONSUME;
			}
		}

		return this.eatSlice(level, pos, state, player);
	}

	public InteractionResult eatSlice(Level level, BlockPos pos, BlockState state, Player player) {
		if (!player.canEat(false)) {
			return InteractionResult.PASS;
		} else {
			player.awardStat(Stats.EAT_CAKE_SLICE);
			if (!level.isClientSide()) {
				player.eat(level, food.get().getDefaultInstance());
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
