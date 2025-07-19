package dev.xkmc.youkaishomecoming.init.registrate;

import dev.xkmc.youkaishomecoming.content.pot.steamer.SteamTrigger;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.critereon.PlayerTrigger;

public class YHCriteriaTriggers {

	public static final PlayerTrigger CUCUMBER = reg("cucumber");
	public static final PlayerTrigger GRAPE_CUT = reg("grape_cut");
	public static final PlayerTrigger GRAPE_HARVEST = reg("grape_harvest");
	public static final PlayerTrigger BASIN = reg("basin");
	public static final PlayerTrigger COOKING = reg("cooking");
	public static final SteamTrigger STEAM = CriteriaTriggers.register(new SteamTrigger());
	public static final PlayerTrigger TABLE = reg("table");
	public static final PlayerTrigger CRAB_GRAB = reg("crab_grab");
	public static final PlayerTrigger UDUMBARA_LEAVES = reg("udumbara_leaves");


	private static PlayerTrigger reg(String id) {
		return CriteriaTriggers.register(new PlayerTrigger(YoukaisHomecoming.loc(id)));
	}

	public static void register() {

	}


}
