package dev.xkmc.youkaishomecoming.init.data;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

public class YHModConfig {

	public static class Client {

		Client(ForgeConfigSpec.Builder builder) {
		}

	}

	public static class Common {

		public final ForgeConfigSpec.IntValue youkaifyingTime;
		public final ForgeConfigSpec.DoubleValue youkaifyingChance;
		public final ForgeConfigSpec.IntValue youkaifyingConfusionTime;
		public final ForgeConfigSpec.IntValue youkaifyingThreshold;
		public final ForgeConfigSpec.IntValue youkaifiedDuration;
		public final ForgeConfigSpec.IntValue youkaifiedProlongation;
		public final ForgeConfigSpec.DoubleValue youkaifiedBoost;

		public final ForgeConfigSpec.DoubleValue smoothingHealingFactor;
		public final ForgeConfigSpec.IntValue teaHealingPeriod;
		public final ForgeConfigSpec.IntValue udumbaraDuration;
		public final ForgeConfigSpec.IntValue udumbaraHealingPeriod;
		public final ForgeConfigSpec.IntValue udumbaraFullMoonReduction;

		Common(ForgeConfigSpec.Builder builder) {
			youkaifyingChance = builder.comment("Chance for flesh food to add Youkaifying effect for the first time")
					.defineInRange("youkaifyingChance", 0.2, 0, 1);
			youkaifyingConfusionTime = builder.comment("Confusion time when flesh food to add Youkaifying effect for the first time")
					.defineInRange("youkaifyingConfusionTime", 200, 0, 1000000);
			youkaifyingTime = builder.comment("Time for flesh food to add Youkaifying effect")
					.defineInRange("youkaifyingTime", 1200, 0, 1000000);
			youkaifyingThreshold = builder.comment("Threshold for Youkaifying effect to turn into Youkaified effect")
					.defineInRange("youkaifyingThreshold", 6000, 0, 1000000);
			youkaifiedDuration = builder.comment("Youkaified duration once reached")
					.defineInRange("youkaifiedDuration", 24000, 0, 1000000);
			youkaifiedProlongation = builder.comment("Time for flesh food to add Youkaified effect")
					.defineInRange("youkaifiedProlongation", 6000, 0, 1000000);
			youkaifiedBoost = builder.comment("Time for flesh food to add Youkaified effect")
					.defineInRange("youkaifiedBoost", 2d, 0, 10d);

			smoothingHealingFactor = builder.comment("Smoothing Healing Factor")
					.defineInRange("smoothingHealingFactor", 1.5, 0, 100);
			teaHealingPeriod = builder.comment("Tea Healing Interval")
					.defineInRange("teaHealingPeriod", 60, 0, 10000);
			udumbaraHealingPeriod = builder.comment("Udumbara effect Healing Interval")
					.defineInRange("udumbaraHealingPeriod", 60, 0, 10000);
			udumbaraDuration = builder.comment("Udumbara flowering duration")
					.defineInRange("udumbaraDuration", 200, 0, 100000);
			udumbaraFullMoonReduction = builder.comment("Udumbara full moon damage reduction")
					.defineInRange("udumbaraFullMoonReduction", 4, 0, 100);
		}

	}

	public static final ForgeConfigSpec CLIENT_SPEC;
	public static final Client CLIENT;

	public static final ForgeConfigSpec COMMON_SPEC;
	public static final Common COMMON;

	static {
		final Pair<Client, ForgeConfigSpec> client = new ForgeConfigSpec.Builder().configure(Client::new);
		CLIENT_SPEC = client.getRight();
		CLIENT = client.getLeft();

		final Pair<Common, ForgeConfigSpec> common = new ForgeConfigSpec.Builder().configure(Common::new);
		COMMON_SPEC = common.getRight();
		COMMON = common.getLeft();
	}

	/**
	 * Registers any relevant listeners for config
	 */
	public static void init() {
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CLIENT_SPEC);
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, COMMON_SPEC);
	}


}
