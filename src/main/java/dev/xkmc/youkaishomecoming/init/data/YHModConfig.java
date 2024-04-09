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

		public final ForgeConfigSpec.IntValue frogEatCountForHat;
		public final ForgeConfigSpec.IntValue frogEatRaiderVillagerSightRange;
		public final ForgeConfigSpec.IntValue frogEatRaiderVillagerNoSightRange;
		public final ForgeConfigSpec.BooleanValue koishiAttackEnable;
		public final ForgeConfigSpec.IntValue koishiAttackCoolDown;
		public final ForgeConfigSpec.DoubleValue koishiAttackChance;
		public final ForgeConfigSpec.IntValue koishiAttackDamage;
		public final ForgeConfigSpec.IntValue koishiAttackBlockCount;

		public final ForgeConfigSpec.DoubleValue damakuMinPHPDamage;
		public final ForgeConfigSpec.DoubleValue damakuPlayerPHPDamage;

		Common(ForgeConfigSpec.Builder builder) {
			builder.push("youkaifying_effect");
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
			builder.pop();

			builder.push("food_effect");
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
			builder.pop();

			builder.push("suwako_hat");
			frogEatCountForHat = builder.comment("Number of raiders with different types frogs need to eat in front of villager to drop Suwako hat")
					.defineInRange("frogEatCountForHat", 3, 1, 10);
			frogEatRaiderVillagerSightRange = builder.comment("Range for villagers with direct sight when frog eat raiders")
					.defineInRange("frogEatRaiderVillagerSightRange", 20, 1, 64);
			frogEatRaiderVillagerNoSightRange = builder.comment("Range for villagers without direct sight when frog eat raiders")
					.defineInRange("frogEatRaiderVillagerNoSightRange", 10, 1, 64);
			builder.pop();

			builder.push("koishi_attack");
			koishiAttackEnable = builder.comment("Enable koishi attack when player has youkaifying or youkaified effect")
					.define("koishiAttackEnable", true);
			koishiAttackCoolDown = builder.comment("Time in ticks for minimum time between koishi attacks")
					.defineInRange("koishiAttackCoolDown", 6000, 1, 1000000);
			koishiAttackChance = builder.comment("Chance every tick to do koishi attack")
					.defineInRange("koishiAttackChance", 0.001, 0, 1);
			koishiAttackDamage = builder.comment("Koishi attack damage")
					.defineInRange("koishiAttackDamage", 100, 0, 100000000);
			koishiAttackBlockCount = builder.comment("Number of times player needs to consecutively block Koishi attack to get hat")
					.defineInRange("koishiAttackBlockCount", 3, 0, 100);
			builder.pop();

			builder.push("damaku_battle");
			damakuMinPHPDamage = builder.comment("Minimum damage youkai damaku will deal against non-player")
							.defineInRange("damakuMinPHPDamage",0.02,0,1);
			damakuPlayerPHPDamage = builder.comment("Minimum damage youkai damaku will deal against player")
					.defineInRange("damakuPlayerPHPDamage",0.1,0,1);
			builder.pop();
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
