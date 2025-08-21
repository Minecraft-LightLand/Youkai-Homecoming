package dev.xkmc.youkaishomecoming.compat.terrablender;

import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import terrablender.api.Regions;
import terrablender.api.SurfaceRuleManager;

public class Terrablender {

	public static void registerBiomes() {
		Regions.register(new GensokyoRegion(YoukaisHomecoming.loc("overworld"), 2));
		SurfaceRuleManager.addToDefaultSurfaceRulesAtStage(SurfaceRuleManager.RuleCategory.OVERWORLD, SurfaceRuleManager.RuleStage.AFTER_BEDROCK,1,
				GensokyoSurfaceRules.buildRules());
	}
}
