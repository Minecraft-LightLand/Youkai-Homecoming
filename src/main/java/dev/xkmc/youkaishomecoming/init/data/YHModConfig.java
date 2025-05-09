package dev.xkmc.youkaishomecoming.init.data;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

public class YHModConfig {

	public static class Client {

		public final ForgeConfigSpec.BooleanValue laserRenderAdditive;
		public final ForgeConfigSpec.BooleanValue laserRenderInverted;
		public final ForgeConfigSpec.DoubleValue laserTransparency;
		public final ForgeConfigSpec.DoubleValue farDanmakuFading;
		public final ForgeConfigSpec.DoubleValue selfDanmakuFading;
		public final ForgeConfigSpec.DoubleValue fadingStart;
		public final ForgeConfigSpec.DoubleValue fadingEnd;

		Client(ForgeConfigSpec.Builder builder) {
			laserRenderAdditive = builder.define("laserRenderAdditive", true);
			laserRenderInverted = builder.define("laserRenderInverted", true);
			laserTransparency = builder.defineInRange("laserTransparency", 0.5, 0, 1);
			farDanmakuFading = builder.defineInRange("farDanmakuFading", 0.5d, 0, 1);
			selfDanmakuFading = builder.defineInRange("selfDanmakuFading", 0.5d, 0, 1);
			fadingStart = builder.defineInRange("fadingStart", 8d, 0, 128);
			fadingEnd = builder.defineInRange("fadingEnd", 64d, 0, 128);
		}

	}

	public static class Common {

		public final ForgeConfigSpec.IntValue youkaifyingTime;
		public final ForgeConfigSpec.DoubleValue youkaifyingChance;
		public final ForgeConfigSpec.IntValue youkaifyingConfusionTime;
		public final ForgeConfigSpec.IntValue youkaifyingThreshold;
		public final ForgeConfigSpec.IntValue youkaifiedDuration;
		public final ForgeConfigSpec.IntValue youkaifiedProlongation;

		public final ForgeConfigSpec.DoubleValue smoothingHealingFactor;
		public final ForgeConfigSpec.IntValue teaHealingPeriod;
		public final ForgeConfigSpec.IntValue udumbaraDuration;
		public final ForgeConfigSpec.IntValue udumbaraHealingPeriod;
		public final ForgeConfigSpec.IntValue udumbaraFullMoonReduction;
		public final ForgeConfigSpec.IntValue higiHealingPeriod;
		public final ForgeConfigSpec.DoubleValue fairyHealingFactor;

		public final ForgeConfigSpec.IntValue frogEatCountForHat;
		public final ForgeConfigSpec.IntValue frogEatRaiderVillagerSightRange;
		public final ForgeConfigSpec.IntValue frogEatRaiderVillagerNoSightRange;
		public final ForgeConfigSpec.BooleanValue koishiAttackEnable;
		public final ForgeConfigSpec.IntValue koishiAttackCoolDown;
		public final ForgeConfigSpec.DoubleValue koishiAttackChance;
		public final ForgeConfigSpec.IntValue koishiAttackDamage;
		public final ForgeConfigSpec.IntValue koishiAttackBlockCount;

		public final ForgeConfigSpec.DoubleValue danmakuMinPHPDamage;
		public final ForgeConfigSpec.DoubleValue danmakuPlayerPHPDamage;
		public final ForgeConfigSpec.DoubleValue danmakuHealOnHitTarget;
		public final ForgeConfigSpec.IntValue playerDanmakuCooldown;
		public final ForgeConfigSpec.IntValue playerLaserCooldown;
		public final ForgeConfigSpec.IntValue playerSpellCooldown;
		public final ForgeConfigSpec.IntValue playerLaserDuration;
		public final ForgeConfigSpec.BooleanValue invulFrameForDanmaku;

		public final ForgeConfigSpec.BooleanValue rumiaNaturalSpawn;
		public final ForgeConfigSpec.BooleanValue exRumiaConversion;
		public final ForgeConfigSpec.BooleanValue rumiaDamageCap;
		public final ForgeConfigSpec.BooleanValue rumiaNoTargetHealing;
		public final ForgeConfigSpec.BooleanValue rumiaHairbandDrop;

		public final ForgeConfigSpec.BooleanValue reimuSummonFlesh;
		public final ForgeConfigSpec.BooleanValue reimuSummonKill;
		public final ForgeConfigSpec.BooleanValue reimuSummonMoney;
		public final ForgeConfigSpec.IntValue reimuSummonCost;
		public final ForgeConfigSpec.BooleanValue reimuHairbandFlightEnable;
		public final ForgeConfigSpec.BooleanValue reimuExtraDamageCoolDown;
		public final ForgeConfigSpec.BooleanValue reimuDamageReduction;

		public final ForgeConfigSpec.BooleanValue cirnoSpawn;
		public final ForgeConfigSpec.DoubleValue cirnoFairyDrop;
		public final ForgeConfigSpec.BooleanValue cirnoSpawnCheckEffect;
		public final ForgeConfigSpec.BooleanValue fairyAttackYoukaified;
		public final ForgeConfigSpec.DoubleValue fairySummonReinforcement;

		public final ForgeConfigSpec.IntValue customSpellMaxDuration;
		public final ForgeConfigSpec.IntValue ringSpellDanmakuPerItemCost;
		public final ForgeConfigSpec.IntValue homingSpellDanmakuPerItemCost;

		Common(ForgeConfigSpec.Builder builder) {
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
				invulFrameForDanmaku = builder.comment("Enable danmaku damage invulnerability frame against non-player non-youkai mobs.")
						.comment("It's always enabled against player and youkais")
						.define("invulFrameForDanmaku", true);
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
				reimuExtraDamageCoolDown = builder.comment("Enable non-danmaku extra damage cooldown on Reimu")
						.define("reimuExtraDamageCoolDown", true);
				reimuDamageReduction = builder.comment("Enable non-danmaku damage reduction on Reimu")
						.define("reimuDamageReduction", true);
			}
			builder.pop();

			builder.push("cirno");
			{
				cirnoSpawn = builder.comment("Toggle for Cirno natural spawns")
						.define("cirnoSpawn", true);
				cirnoFairyDrop = builder.comment("Chance for fairy ice crystal to drop")
						.defineInRange("cirnoFairyDrop", 0.03, 0, 1);
				cirnoSpawnCheckEffect = builder.comment("Cirno spawns naturally only when there are youkaifying/ed player nearby")
						.define("cirnoSpawnCheckEffect", true);
				fairyAttackYoukaified = builder.comment("Fairies will actively attack players with youkaifying/ed effects")
						.define("fairyAttackYoukaified", true);
				fairySummonReinforcement = builder.comment("Chance for fairies to summon other fairies when killed by non-danmaku damage")
						.defineInRange("fairySummonReinforcement", 0.5, 0, 1);
			}
			builder.pop();

			builder.push("custom_spell");
			{
				customSpellMaxDuration = builder.comment("Max duration of custom spell allowed")
						.defineInRange("customSpellMaxDuration", 1, 60, 1000);
				ringSpellDanmakuPerItemCost = builder.comment("Ring Spell: Max number of bullet allowed per item cost")
						.defineInRange("ringSpellDanmakuPerItemCost", 32, 1, 1024);
				homingSpellDanmakuPerItemCost = builder.comment("Homing Spell: Max number of bullet allowed per item cost")
						.defineInRange("homingSpellDanmakuPerItemCost", 8, 1, 1024);
			}
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
