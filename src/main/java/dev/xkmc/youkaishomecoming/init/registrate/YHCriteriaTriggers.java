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
	public static final PlayerTrigger FLESH_WARN = CriteriaTriggers.register(new PlayerTrigger(YoukaisHomecoming.loc("flesh_warn")));
	public static final PlayerTrigger HURT_WARN = CriteriaTriggers.register(new PlayerTrigger(YoukaisHomecoming.loc("hurt_warn")));
	public static final PlayerTrigger KOISHI_FIRST = CriteriaTriggers.register(new PlayerTrigger(YoukaisHomecoming.loc("koishi_first")));
	public static final PlayerTrigger CUCUMBER = CriteriaTriggers.register(new PlayerTrigger(YoukaisHomecoming.loc("cucumber")));
	public static final PlayerTrigger GRAPE_CUT = CriteriaTriggers.register(new PlayerTrigger(YoukaisHomecoming.loc("grape_cut")));
	public static final PlayerTrigger GRAPE_HARVEST = CriteriaTriggers.register(new PlayerTrigger(YoukaisHomecoming.loc("grape_harvest")));
	public static final PlayerTrigger BASIN = CriteriaTriggers.register(new PlayerTrigger(YoukaisHomecoming.loc("basin")));
	public static final PlayerTrigger COOKING = CriteriaTriggers.register(new PlayerTrigger(YoukaisHomecoming.loc("cooking")));
	public static final PlayerTrigger TABLE = CriteriaTriggers.register(new PlayerTrigger(YoukaisHomecoming.loc("table")));
	public static final PlayerTrigger CRAB_GRAB = CriteriaTriggers.register(new PlayerTrigger(YoukaisHomecoming.loc("crab_grab")));


	private static PlayerTrigger reg(String id) {
		return CriteriaTriggers.register(new PlayerTrigger(YoukaisHomecoming.loc(id)));
	}

	public static void register() {

	}


}
