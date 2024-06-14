package dev.xkmc.youkaishomecoming.init.registrate;

import dev.xkmc.youkaishomecoming.content.entity.reimu.FeedReimuTrigger;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.critereon.PlayerTrigger;

public class YHCriteriaTriggers {

	public static final PlayerTrigger SUWAKO_WEAR = CriteriaTriggers.register(new PlayerTrigger(YoukaisHomecoming.loc("suwako_wear")));
	public static final PlayerTrigger KOISHI_RING = CriteriaTriggers.register(new PlayerTrigger(YoukaisHomecoming.loc("koishi_ring")));
	public static final PlayerTrigger TRADE = CriteriaTriggers.register(new PlayerTrigger(YoukaisHomecoming.loc("rumia_trade")));
	public static final FeedReimuTrigger FEED_REIMU = CriteriaTriggers.register(new FeedReimuTrigger());
	public static final PlayerTrigger REIMU_HAPPY = CriteriaTriggers.register(new PlayerTrigger(YoukaisHomecoming.loc("udumbara_feed")));


	private static PlayerTrigger reg(String id) {
		return CriteriaTriggers.register(new PlayerTrigger(YoukaisHomecoming.loc(id)));
	}

	public static void register() {

	}


}
