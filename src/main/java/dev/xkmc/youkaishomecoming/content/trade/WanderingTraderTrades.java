package dev.xkmc.youkaishomecoming.content.trade;

import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.food.YHCrops;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.BasicItemListing;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = YoukaisHomecoming.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class WanderingTraderTrades {

	private static final int MAX_USES = 4;
	private static final int VILLAGER_XP = 1;
	private static final float PRICE_MULTIPLIER = 0.05F;

	@SubscribeEvent
	public static void onWandererTrades(WandererTradesEvent event) {
		var trades = event.getGenericTrades();
		for (var crop : YHCrops.values()) {
			trades.add(new BasicItemListing(emeraldCost(crop), new ItemStack(crop.getSeed()),
					MAX_USES, VILLAGER_XP, PRICE_MULTIPLIER));
		}
	}

	private static int emeraldCost(YHCrops crop) {
		return switch (crop) {
			case SOYBEAN, REDBEAN -> 1;
			case CUCUMBER, TEA -> 2;
			case RED_GRAPE, WHITE_GRAPE, BLACK_GRAPE -> 3;
			case UDUMBARA -> 16;
		};
	}

}
