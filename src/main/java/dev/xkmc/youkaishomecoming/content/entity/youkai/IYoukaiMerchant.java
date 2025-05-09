package dev.xkmc.youkaishomecoming.content.entity.youkai;

import dev.xkmc.youkaishomecoming.content.item.danmaku.DanmakuItem;
import dev.xkmc.youkaishomecoming.init.registrate.YHCriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.item.trading.MerchantOffer;

public interface IYoukaiMerchant extends Merchant {

	default void init(Player player) {
		setTradingPlayer(player);
	}

	@Override
	default void notifyTradeUpdated(ItemStack pStack) {

	}

	@Override
	default int getVillagerXp() {
		return 0;
	}

	@Override
	default void overrideXp(int pXp) {

	}

	@Override
	default boolean showProgressBar() {
		return false;
	}

	@Override
	default SoundEvent getNotifyTradeSound() {
		return SoundEvents.CAT_AMBIENT;
	}

	@Override
	default boolean isClientSide() {
		return false;
	}

	@Override
	default void notifyTrade(MerchantOffer offer) {
		if (getTradingPlayer() instanceof ServerPlayer sp &&
				offer.getResult().getItem() instanceof DanmakuItem)
			YHCriteriaTriggers.TRADE.trigger(sp);
	}

}
