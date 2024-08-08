package dev.xkmc.youkaishomecoming.init.data;

import dev.xkmc.l2core.util.ConfigInit;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.neoforged.neoforge.common.ModConfigSpec;

public class YHModConfig {

	public static class Common extends ConfigInit {

		public final ModConfigSpec.IntValue youkaifyingTime;
		public final ModConfigSpec.DoubleValue youkaifyingChance;
		public final ModConfigSpec.IntValue youkaifyingConfusionTime;
		public final ModConfigSpec.IntValue youkaifyingThreshold;
		public final ModConfigSpec.IntValue youkaifiedDuration;
		public final ModConfigSpec.IntValue youkaifiedProlongation;

		public final ModConfigSpec.DoubleValue smoothingHealingFactor;
		public final ModConfigSpec.IntValue teaHealingPeriod;
		public final ModConfigSpec.IntValue udumbaraDuration;
		public final ModConfigSpec.IntValue udumbaraHealingPeriod;
		public final ModConfigSpec.IntValue udumbaraFullMoonReduction;
		public final ModConfigSpec.IntValue higiHealingPeriod;
		public final ModConfigSpec.DoubleValue fairyHealingFactor;

		public final ModConfigSpec.IntValue frogEatCountForHat;
		public final ModConfigSpec.IntValue frogEatRaiderVillagerSightRange;
		public final ModConfigSpec.IntValue frogEatRaiderVillagerNoSightRange;
		public final ModConfigSpec.BooleanValue koishiAttackEnable;
		public final ModConfigSpec.IntValue koishiAttackCoolDown;
		public final ModConfigSpec.DoubleValue koishiAttackChance;
		public final ModConfigSpec.IntValue koishiAttackDamage;
		public final ModConfigSpec.IntValue koishiAttackBlockCount;

		public final ModConfigSpec.DoubleValue danmakuMinPHPDamage;
		public final ModConfigSpec.DoubleValue danmakuPlayerPHPDamage;
		public final ModConfigSpec.DoubleValue danmakuHealOnHitTarget;
		public final ModConfigSpec.IntValue playerDanmakuCooldown;
		public final ModConfigSpec.IntValue playerLaserCooldown;
		public final ModConfigSpec.IntValue playerSpellCooldown;
		public final ModConfigSpec.IntValue playerLaserDuration;

		public final ModConfigSpec.BooleanValue rumiaNaturalSpawn;
		public final ModConfigSpec.BooleanValue exRumiaConversion;
		public final ModConfigSpec.BooleanValue rumiaDamageCap;
		public final ModConfigSpec.BooleanValue rumiaNoTargetHealing;
		public final ModConfigSpec.BooleanValue rumiaHairbandDrop;

		public final ModConfigSpec.BooleanValue reimuSummonFlesh;
		public final ModConfigSpec.BooleanValue reimuSummonKill;
		public final ModConfigSpec.BooleanValue reimuSummonMoney;
		public final ModConfigSpec.IntValue reimuSummonCost;
		public final ModConfigSpec.BooleanValue reimuHairbandFlightEnable;

		public final ModConfigSpec.BooleanValue cirnoSpawn;
		public final ModConfigSpec.DoubleValue cirnoFairyDrop;

		Common(ModConfigSpec.Builder builder) {
			builder.push("youkaifying_effect");
			{
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
			}
			builder.pop();

			builder.push("food_effect");
			{
				smoothingHealingFactor = builder.comment("Smoothing Healing Factor")
						.defineInRange("smoothingHealingFactor", 1.5, 1, 100);
				teaHealingPeriod = builder.comment("Tea Healing Interval")
						.defineInRange("teaHealingPeriod", 60, 0, 10000);
				udumbaraHealingPeriod = builder.comment("Udumbara effect Healing Interval")
						.defineInRange("udumbaraHealingPeriod", 60, 0, 10000);
				udumbaraDuration = builder.comment("Udumbara flowering duration")
						.defineInRange("udumbaraDuration", 200, 0, 100000);
				udumbaraFullMoonReduction = builder.comment("Udumbara full moon damage reduction")
						.defineInRange("udumbaraFullMoonReduction", 4, 0, 100);
				higiHealingPeriod = builder.comment("Higi Healing Interval")
						.defineInRange("higiHealingPeriod", 60, 0, 10000);
				fairyHealingFactor = builder.comment("Fairy Healing Factor")
						.defineInRange("fairyHealingFactor", 2d, 1, 100);
			}
			builder.pop();

			builder.push("suwako_hat");
			{
				frogEatCountForHat = builder.comment("Number of raiders with different types frogs need to eat in front of villager to drop Suwako hat")
						.defineInRange("frogEatCountForHat", 3, 1, 10);
				frogEatRaiderVillagerSightRange = builder.comment("Range for villagers with direct sight when frog eat raiders")
						.defineInRange("frogEatRaiderVillagerSightRange", 20, 1, 64);
				frogEatRaiderVillagerNoSightRange = builder.comment("Range for villagers without direct sight when frog eat raiders")
						.defineInRange("frogEatRaiderVillagerNoSightRange", 10, 1, 64);
			}
			builder.pop();

			builder.push("koishi_attack");
			{
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
			}
			builder.pop();

			builder.push("danmaku_battle");
			{
				danmakuMinPHPDamage = builder.comment("Minimum damage youkai danmaku will deal against non-player")
						.defineInRange("danmakuMinPHPDamage", 0.02, 0, 1);
				danmakuPlayerPHPDamage = builder.comment("Minimum damage youkai danmaku will deal against player")
						.defineInRange("danmakuPlayerPHPDamage", 0.1, 0, 1);
				danmakuHealOnHitTarget = builder.comment("When danmaku hits target, heal youkai health by percentage of max health")
						.defineInRange("danmakuHealOnHitTarget", 0.2, 0, 1);
				playerDanmakuCooldown = builder.comment("Player item cooldown for using danmaku")
						.defineInRange("playerDanmakuCooldown", 20, 5, 1000);
				playerLaserCooldown = builder.comment("Player item cooldown for using laser")
						.defineInRange("playerLaserCooldown", 80, 5, 1000);
				playerSpellCooldown = builder.comment("Player item cooldown for using spellcard")
						.defineInRange("playerSpellCooldown", 40, 5, 1000);
				playerLaserDuration = builder.comment("Player laser duration")
						.defineInRange("playerLaserDuration", 100, 5, 1000);
			}
			builder.pop();

			builder.push("rumia");
			{
				rumiaNaturalSpawn = builder.comment("If Rumia would spawn naturally around her nest if the first one goes too far. Does not affect structure spawn")
						.define("rumiaNaturalSpawn", true);
				exRumiaConversion = builder.comment("Enable Ex Rumia conversion when Rumia takes too high damage in one hit")
						.define("exRumiaConversion", true);
				rumiaDamageCap = builder.comment("Allow Rumia to cap incoming damage at a factor of max health")
						.define("rumiaDamageCap", true);
				rumiaNoTargetHealing = builder.comment("Enable Rumia healing when having no target")
						.define("rumiaNoTargetHealing", true);
				rumiaHairbandDrop = builder.comment("Enable Ex Rumia hairband drop")
						.define("rumiaHairbandDrop", true);
			}
			builder.pop();

			builder.push("reimu");
			{
				reimuSummonFlesh = builder.comment("Summon Reimu when player eats flesh in front of villagers")
						.define("reimuSummonFlesh", true);
				reimuSummonKill = builder.comment("Summon Reimu when player with youkaified/fying effect kills villager in front of other villagers")
						.define("reimuSummonKill", true);
				reimuSummonMoney = builder.comment("Summon Reimu when player throws emerald or gold into donation box")
						.define("reimuSummonMoney", true);
				reimuSummonCost = builder.comment("Cost of emerald/gold to summon Reimu")
						.defineInRange("reimuSummonCost", 8, 1, 100000);
				reimuHairbandFlightEnable = builder.comment("Enable creative flight on Reimu hairband")
						.define("reimuHairbandFlightEnable", true);
			}
			builder.pop();

			builder.push("cirno");
			{
				cirnoSpawn = builder.comment("Toggle for Cirno natural spawns")
						.define("cirnoSpawn", true);
				cirnoFairyDrop = builder.comment("Chance for fairy ice crystal to drop")
						.defineInRange("cirnoFairyDrop", 0.03, 0, 1);
			}
			builder.pop();
		}

	}

	public static final Common COMMON = YoukaisHomecoming.REGISTRATE.registerClient(Common::new);

	/**
	 * Registers any relevant listeners for config
	 */
	public static void init() {
	}


}
