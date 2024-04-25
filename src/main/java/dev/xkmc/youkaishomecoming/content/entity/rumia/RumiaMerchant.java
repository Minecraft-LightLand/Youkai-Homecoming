package dev.xkmc.youkaishomecoming.content.entity.rumia;

import dev.xkmc.youkaishomecoming.init.food.YHFood;
import dev.xkmc.youkaishomecoming.init.registrate.YHDanmaku;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import org.jetbrains.annotations.Nullable;

public class RumiaMerchant implements Merchant {

	public static void openMenu(RumiaEntity rumia, Player player) {
		new RumiaMerchant(getOfferList(), player).openTradingScreen(player, rumia.getName(), 0);
	}

	private static MerchantOffers getOfferList() {
		MerchantOffers ans = new MerchantOffers();
		ans.add(offer(4, YHDanmaku.Bullet.CIRCLE.get(DyeColor.RED).asStack(8)));
		ans.add(offer(4, YHDanmaku.Bullet.BALL.get(DyeColor.RED).asStack(8)));
		ans.add(offer(8, YHDanmaku.Bullet.MENTOS.get(DyeColor.RED).asStack(8)));
		ans.add(offer(16, YHDanmaku.Bullet.BUBBLE.get(DyeColor.RED).asStack(8)));
		ans.add(offer(32, YHDanmaku.Laser.LASER.get(DyeColor.RED).asStack(8)));
		return ans;
	}

	private static MerchantOffer offer(int a, ItemStack b) {
		return new MerchantOffer(YHFood.FLESH_CHOCOLATE_MOUSSE.item.asStack(a), b, 64, 0, 0);
	}

	private MerchantOffers offers;
	private Player player;

	private RumiaMerchant(MerchantOffers offers, Player player) {
		this.offers = offers;
		this.player = player;
	}

	@Override
	public void setTradingPlayer(@Nullable Player player) {
		this.player = player;
	}

	@Nullable
	@Override
	public Player getTradingPlayer() {
		return player;
	}

	@Override
	public MerchantOffers getOffers() {
		return offers;
	}

	@Override
	public void overrideOffers(MerchantOffers offers) {
		this.offers = offers;
	}

	@Override
	public void notifyTrade(MerchantOffer pOffer) {

	}

	@Override
	public void notifyTradeUpdated(ItemStack pStack) {

	}

	@Override
	public int getVillagerXp() {
		return 0;
	}

	@Override
	public void overrideXp(int pXp) {

	}

	@Override
	public boolean showProgressBar() {
		return false;
	}

	@Override
	public SoundEvent getNotifyTradeSound() {
		return SoundEvents.CAT_AMBIENT;
	}

	@Override
	public boolean isClientSide() {
		return false;
	}

}
