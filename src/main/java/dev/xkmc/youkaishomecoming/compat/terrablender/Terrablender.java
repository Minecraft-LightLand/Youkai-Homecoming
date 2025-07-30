package dev.xkmc.youkaishomecoming.compat.terrablender;

import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import terrablender.api.Regions;

public class Terrablender {

	public static void registerBiomes() {
		Regions.register(new GensokyoRegion(YoukaisHomecoming.loc("overworld"), 2));
	}
}
