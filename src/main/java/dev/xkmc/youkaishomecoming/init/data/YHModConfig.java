package dev.xkmc.youkaishomecoming.init.data;

import dev.xkmc.l2core.util.ConfigInit;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.neoforged.neoforge.common.ModConfigSpec;

public class YHModConfig {

	public static class Client extends ConfigInit {

		Client(Builder builder) {
		}

	}

	public static class Common extends ConfigInit {

		public final ModConfigSpec.DoubleValue breathingHealingFactor;
		public final ModConfigSpec.IntValue teaHealingPeriod;
		public final ModConfigSpec.IntValue udumbaraDuration;
		public final ModConfigSpec.IntValue udumbaraHealingPeriod;
		public final ModConfigSpec.IntValue udumbaraFullMoonReduction;
		public final ModConfigSpec.IntValue higiHealingPeriod;

		Common(Builder builder) {

			builder.push("food_effect", "Food Effects");
			{
				breathingHealingFactor = builder.text("Breathing Healing Factor")
						.defineInRange("breathingHealingFactor", 1.5, 1, 100);
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
			}
			builder.pop();
		}

	}

	public static final Client CLIENT = YoukaisHomecoming.REGISTRATE.registerClient(Client::new);

	public static final Common COMMON = YoukaisHomecoming.REGISTRATE.registerSynced(Common::new);

	public static void init() {
	}


}
