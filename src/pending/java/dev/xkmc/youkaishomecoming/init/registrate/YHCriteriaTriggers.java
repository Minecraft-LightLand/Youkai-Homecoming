
	public static class YHCriteriaTriggers {

		public static final PlayerTrigger SUWAKO_WEAR = CriteriaTriggers.register(new PlayerTrigger(YoukaisHomecoming.loc("suwako_wear")));
		public static final PlayerTrigger KOISHI_RING = CriteriaTriggers.register(new PlayerTrigger(YoukaisHomecoming.loc("koishi_ring")));
		public static final PlayerTrigger TRADE = CriteriaTriggers.register(new PlayerTrigger(YoukaisHomecoming.loc("rumia_trade")));
		public static final FeedReimuTrigger FEED_REIMU = CriteriaTriggers.register(new FeedReimuTrigger());
		public static final PlayerTrigger REIMU_HAPPY = CriteriaTriggers.register(new PlayerTrigger(YoukaisHomecoming.loc("udumbara_feed")));
		public static final PlayerTrigger FLESH_WARN = CriteriaTriggers.register(new PlayerTrigger(YoukaisHomecoming.loc("flesh_warn")));
		public static final PlayerTrigger HURT_WARN = CriteriaTriggers.register(new PlayerTrigger(YoukaisHomecoming.loc("hurt_warn")));
		public static final PlayerTrigger KOISHI_FIRST = CriteriaTriggers.register(new PlayerTrigger(YoukaisHomecoming.loc("koishi_first")));


		private static PlayerTrigger reg(String id) {
			return CriteriaTriggers.register(new PlayerTrigger(YoukaisHomecoming.loc(id)));
		}

		public static void register() {

		}


	}