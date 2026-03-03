package dev.xkmc.youkaishomecoming.init.registrate;

import dev.xkmc.l2core.init.reg.simple.SR;
import dev.xkmc.l2core.init.reg.simple.Val;
import dev.xkmc.youkaishomecoming.content.pot.steamer.SteamTrigger;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.core.registries.Registries;

public class YHCriteriaTriggers {

	private static final SR<CriterionTrigger<?>> REG = SR.of(YoukaisHomecoming.REG, Registries.TRIGGER_TYPE);

	public static final Val<PlayerTrigger> CUCUMBER = REG.reg("cucumber", PlayerTrigger::new);
	public static final Val<PlayerTrigger> GRAPE_CUT = REG.reg("grape_cut", PlayerTrigger::new);
	public static final Val<PlayerTrigger> GRAPE_HARVEST = REG.reg("grape_harvest", PlayerTrigger::new);
	public static final Val<PlayerTrigger> BASIN = REG.reg("basin", PlayerTrigger::new);
	public static final Val<PlayerTrigger> COOKING = REG.reg("cooking", PlayerTrigger::new);
	public static final Val<PlayerTrigger> POT_GRAB = REG.reg("pot_grab", PlayerTrigger::new);
	public static final Val<SteamTrigger> STEAM = REG.reg("steam", SteamTrigger::new);
	public static final Val<PlayerTrigger> TABLE = REG.reg("table", PlayerTrigger::new);
	public static final Val<PlayerTrigger> CRAB_GRAB = REG.reg("crab_grab", PlayerTrigger::new);
	public static final Val<PlayerTrigger> UDUMBARA_LEAVES = REG.reg("udumbara_leaves", PlayerTrigger::new);

	public static void register() {

	}


}
