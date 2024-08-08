package dev.xkmc.youkaishomecoming.init.data;

import dev.xkmc.l2core.util.ConfigInit;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.neoforged.neoforge.common.ModConfigSpec;

public class YHModConfig {

	public static class Server extends ConfigInit {

		public final ModConfigSpec.DoubleValue smoothingHealingFactor;
		public final ModConfigSpec.IntValue teaHealingPeriod;
		public final ModConfigSpec.IntValue udumbaraDuration;
		public final ModConfigSpec.IntValue udumbaraHealingPeriod;
		public final ModConfigSpec.IntValue udumbaraFullMoonReduction;
		public final ModConfigSpec.IntValue higiHealingPeriod;
		public final ModConfigSpec.DoubleValue fairyHealingFactor;

		Server(Builder builder) {
			markPlain();
			smoothingHealingFactor = builder.text("Smoothing Healing Factor")
					.defineInRange("smoothingHealingFactor", 1.5, 1, 100);
			teaHealingPeriod = builder.text("Tea Healing Interval")
					.defineInRange("teaHealingPeriod", 60, 0, 10000);
			udumbaraHealingPeriod = builder.text("Udumbara effect Healing Interval")
					.defineInRange("udumbaraHealingPeriod", 60, 0, 10000);
			udumbaraDuration = builder.text("Udumbara flowering duration")
					.defineInRange("udumbaraDuration", 200, 0, 100000);
			udumbaraFullMoonReduction = builder.text("Udumbara full moon damage reduction")
					.defineInRange("udumbaraFullMoonReduction", 4, 0, 100);
			higiHealingPeriod = builder.text("Higi Healing Interval")
					.defineInRange("higiHealingPeriod", 60, 0, 10000);
			fairyHealingFactor = builder.text("Fairy Healing Factor")
					.defineInRange("fairyHealingFactor", 2d, 1, 100);

		}

	}

	public static final Server SERVER = YoukaisHomecoming.REGISTRATE.registerSynced(Server::new);

	/**
	 * Registers any relevant listeners for config
	 */
	public static void init() {
	}


}
