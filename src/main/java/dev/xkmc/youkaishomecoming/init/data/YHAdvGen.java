package dev.xkmc.youkaishomecoming.init.data;

import com.google.common.collect.Streams;
import com.mojang.datafixers.util.Pair;
import com.tterrag.registrate.providers.RegistrateAdvancementProvider;
import dev.xkmc.l2library.serial.advancements.AdvancementGenerator;
import dev.xkmc.l2library.serial.advancements.CriterionBuilder;
import dev.xkmc.l2library.serial.advancements.RewardBuilder;
import dev.xkmc.l2library.util.data.LootTableTemplate;
import dev.xkmc.youkaishomecoming.content.entity.reimu.FeedReimuTrigger;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.food.*;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import dev.xkmc.youkaishomecoming.init.registrate.YHCriteriaTriggers;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
import net.minecraft.Util;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraftforge.registries.ForgeRegistries;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.Arrays;
import java.util.stream.Stream;

public class YHAdvGen {

	public static final ResourceLocation FLESH_WARN = YoukaisHomecoming.loc("flesh_warn");
	public static final ResourceLocation HURT_WARN = YoukaisHomecoming.loc("hurt_warn");

	public static void genAdv(RegistrateAdvancementProvider pvd) {
		pvd.accept(Advancement.Builder.advancement().addCriterion("flesh_warn",
				new PlayerTrigger.TriggerInstance(YHCriteriaTriggers.FLESH_WARN.getId(), ContextAwarePredicate.ANY)
		).build(FLESH_WARN));
		pvd.accept(Advancement.Builder.advancement().addCriterion("hurt_warn",
				new PlayerTrigger.TriggerInstance(YHCriteriaTriggers.HURT_WARN.getId(), ContextAwarePredicate.ANY)
		).build(HURT_WARN));

		var gen = new AdvancementGenerator(pvd, YoukaisHomecoming.MODID);
		var b = gen.new TabBuilder("main");
		var root = b.root("welcome_to_youkais_homecoming", YHItems.SUWAKO_HAT.asStack(),
				CriterionBuilder.one(PlayerTrigger.TriggerInstance.tick()),
				"Youkai's Homecoming", "Welcome To Youkai's Homecoming");
		root.create("sweet", YHFood.MOCHI.item.asStack(),
						CriterionBuilder.one(ConsumeItemTrigger.TriggerInstance.usedItem(
								ItemPredicate.Builder.item().of(YHTagGen.DANGO).build())),
						"Sweet?", "Eat a Mochi")
				.create("hmm", YHFood.SWEET_ORMOSIA_MOCHI_MIXED_BOILED.item.asStack(),
						CriterionBuilder.item(YHFood.SWEET_ORMOSIA_MOCHI_MIXED_BOILED.item.get()),
						"Hmm... Is it right?", "Get a Sweet Ormosia Mochi Mixed Boiled");
		var redbean = root.create("soybean", YHCrops.SOYBEAN.getSeed(),
						CriterionBuilder.one(ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(
								LocationPredicate.Builder.location().setBlock(
										BlockPredicate.Builder.block().of(Blocks.FARMLAND).build()),
								ItemPredicate.Builder.item().of(YHCrops.SOYBEAN.getSeed()))),
						"The Essential Harvest", "Plant Soybean")
				.create("redbean", YHCrops.REDBEAN.getSeed(),
						CriterionBuilder.one(ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(
								LocationPredicate.Builder.location().setBlock(
										BlockPredicate.Builder.block().of(YHTagGen.FARMLAND_REDBEAN).build()),
								ItemPredicate.Builder.item().of(YHCrops.REDBEAN.getSeed()))),
						"Leanness Resistant Red Bean", "Plant Red Bean on Coarse Dirt, Mud, or Clay");
		redbean.create("coffea", YHCrops.COFFEA.getSeed(),
						CriterionBuilder.one(ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(
								LocationPredicate.Builder.location().setBlock(
										BlockPredicate.Builder.block().of(YHTagGen.FARMLAND_COFFEA).build()),
								ItemPredicate.Builder.item().of(YHCrops.COFFEA.getSeed()))),
						"Needs Nutrition Coffea", "Plant Coffea on Podzol, Mud, or Soul Soil")
				.create("coffee_era", YHItems.COFFEE_POWDER.asStack(),
						CriterionBuilder.item(YHItems.COFFEE_POWDER.get()),
						"Coffee Era", "Get Coffee Powder")
				.create("fragrant", YHCoffee.ESPRESSO.item.asStack(),
						CriterionBuilder.one(ConsumeItemTrigger.TriggerInstance.usedItem(YHCoffee.ESPRESSO.item.get())),
						"Fragrant!", "Drink Espresso")
				.create("q_grader", YHCoffee.ESPRESSO.item.asStack(),
						Util.make(CriterionBuilder.and(), c -> Arrays.stream(YHCoffee.values())
								.map(e -> e.item.get()).map(e -> Pair.of(e, ConsumeItemTrigger.TriggerInstance.usedItem(e)))
								.forEach(p -> c.add(ForgeRegistries.ITEMS.getKey(p.getFirst().asItem()).toString(), p.getSecond()))),
						"Q Grader", "Drink Espresso")
				.type(FrameType.CHALLENGE, true, true, false);
		redbean.create("tea", YHCrops.TEA.getSeed(),
						CriterionBuilder.one(ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(
								LocationPredicate.Builder.location().setBlock(
										BlockPredicate.Builder.block().of(Blocks.FARMLAND).build()),
								ItemPredicate.Builder.item().of(YHCrops.TEA.getSeed()))),
						"Refreshing Hobby", "Plant Tea")
				.create("tea_master", YHFood.OOLONG_TEA.item.asStack(),
						Util.make(CriterionBuilder.and(), c -> Stream.of(
										YHFood.WHITE_TEA, YHFood.OOLONG_TEA, YHFood.GREEN_TEA, YHFood.BLACK_TEA
								).map(e -> e.item.get()).map(e -> Pair.of(e, ConsumeItemTrigger.TriggerInstance.usedItem(e)))
								.forEach(p -> c.add(ForgeRegistries.ITEMS.getKey(p.getFirst().asItem()).toString(), p.getSecond()))),
						"Tea Master", "Drink all kinds of tea in original flavor")
				.type(FrameType.GOAL, true, true, false);
		redbean.create("udumbara", YHCrops.UDUMBARA.getSeed(),
						CriterionBuilder.one(ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(
								LocationPredicate.Builder.location().setBlock(
										BlockPredicate.Builder.block().of(Blocks.FARMLAND).build()),
								ItemPredicate.Builder.item().of(YHCrops.UDUMBARA.getSeed()))),
						"Moon Crop", "Plants an Udumbara. It grows under moonlight and shrinks under sunlight.")
				.create("udumbara_flower", YHCrops.UDUMBARA.getFruits(),
						CriterionBuilder.item(YHCrops.UDUMBARA.getFruits()),
						"Fragile Flower", "Get an Udubara flower. It will only appear for 10 seconds during full moon.")
				.type(FrameType.CHALLENGE);
		root.create("alcoholic", YHBlocks.FERMENT.asStack(),
				CriterionBuilder.one(EffectsChangedTrigger.TriggerInstance.hasEffects(
						MobEffectsPredicate.effects().and(YHEffects.DRUNK.get()))),
				"Alcoholic", "Brew and drink an alcoholic drink and obtain Drunk effect");
		root.create("passed_out", YHSake.DAIGINJO.item.asStack(),
				CriterionBuilder.one(EffectsChangedTrigger.TriggerInstance.hasEffects(
						MobEffectsPredicate.effects().and(YHEffects.DRUNK.get(),
								new MobEffectsPredicate.MobEffectInstancePredicate(
										MinMaxBounds.Ints.atLeast(4), MinMaxBounds.Ints.ANY,
										null, null)))),
				"Passed Out", "Drink until you have maximum Drunk effect");
		root.create("mousse", YHFood.KOISHI_MOUSSE.item.asStack(),
						CriterionBuilder.one(ConsumeItemTrigger.TriggerInstance.usedItem(
								ItemPredicate.Builder.item().of(YHFood.KOISHI_MOUSSE.item.get()).build())),
						"Well... Yes? What's Just Happened?", "Eat a Koishi Mousse")
				.type(FrameType.GOAL, true, true, true)
				.create("enthusiastic", YHDish.IMITATION_BEAR_PAW.block.asStack(),
						Util.make(CriterionBuilder.and(), c -> Streams.concat(
										Arrays.stream(YHDish.values()).map(e -> e.block.get()),
										Arrays.stream(YHSake.values()).map(e -> e.item.get()),
										Arrays.stream(YHCoffee.values()).map(e -> e.item.get()),
										Arrays.stream(YHFood.values()).map(e -> e.item.get()))
								.map(e -> Pair.of(e, ConsumeItemTrigger.TriggerInstance.usedItem(e)))
								.forEach(p -> c.add(ForgeRegistries.ITEMS.getKey(p.getFirst().asItem()).toString(), p.getSecond()))),
						"Gensokyo Food Enthusiastic", "Eat all Youkai's Homecoming food")
				.type(FrameType.CHALLENGE, true, true, false);


		var youkai = root.create("flesh", YHFood.FLESH.item.asStack(),
						CriterionBuilder.item(YHFood.FLESH.item.get()),
						"Where is it from?", "Get weird meat")
				.type(FrameType.GOAL, true, true, false)
				.create("first_time", YHFood.FLESH_ROLL.item.asStack(),
						CriterionBuilder.one(EffectsChangedTrigger.TriggerInstance.hasEffects(
								MobEffectsPredicate.effects().and(YHEffects.YOUKAIFYING.get()))),
						"The First Time", "Get Youkaifying effect")
				.type(FrameType.CHALLENGE, true, true, true);
		youkai.create("bloody", YHItems.BLOOD_BOTTLE.asStack(),
						CriterionBuilder.item(YHItems.BLOOD_BOTTLE.get()),
						"Bloody!", "Get a Blood Bottle")
				.create("monstrosity", YHItems.FLESH_FEAST.asStack(),
						CriterionBuilder.one(ConsumeItemTrigger.TriggerInstance.usedItem(YHFood.BOWL_OF_FLESH_FEAST.item.get())),
						"Monstrosity", "Eat a bowl of flesh feast")
				.type(FrameType.GOAL, true, true, false);
		var youkaified = youkai.create("powerful_being", YHItems.SUWAKO_HAT.asStack(),
						CriterionBuilder.one(EffectsChangedTrigger.TriggerInstance.hasEffects(
								MobEffectsPredicate.effects().and(YHEffects.YOUKAIFIED.get()))),
						"Powerful Being", "Get Youkaified effect")
				.type(FrameType.CHALLENGE, true, true, false);

		youkai.create("mary_call", ModItems.IRON_KNIFE.get(),
						CriterionBuilder.one(new PlayerTrigger.TriggerInstance(YHCriteriaTriggers.KOISHI_RING.getId(), ContextAwarePredicate.ANY)),
						"It’s Mary-san", "Receives a phone call from Koishi in the nether in Youkaifying effect")
				.create("koishi_hat", YHItems.KOISHI_HAT.get(),
						CriterionBuilder.item(YHItems.KOISHI_HAT.get()),
						"Koishi's Hat", "Obtain Koishi's Hat after playing with Koishi")
				.type(FrameType.CHALLENGE, true, true, false);
		youkai.create("suwako_wear", YHItems.STRAW_HAT.get(),
						CriterionBuilder.one(new PlayerTrigger.TriggerInstance(YHCriteriaTriggers.SUWAKO_WEAR.getId(), ContextAwarePredicate.ANY)),
						"Godhood Ascension", "In youkaified or youkaifying effect, give the straw hat to a frog")
				.create("suwako_hat", YHItems.SUWAKO_HAT.get(),
						CriterionBuilder.item(YHItems.SUWAKO_HAT.get()),
						"Faith Collection", "Obtain Suwako's Hat after collecting enough faith")
				.type(FrameType.CHALLENGE, true, true, false);

		var danmaku = youkaified.create("trade_danmaku", YHFood.FLESH_CHOCOLATE_MOUSSE.item.get(),
				CriterionBuilder.one(new PlayerTrigger.TriggerInstance(YHCriteriaTriggers.TRADE.getId(), ContextAwarePredicate.ANY)),
				"Cute Merchant", "In youkaified effect, trade with Rumia to get Danmaku");

		danmaku.create("lost_memories", YHItems.RUMIA_HAIRBAND.get(),
						CriterionBuilder.item(YHItems.RUMIA_HAIRBAND.get()),
						"Lost Memories", "When Rumia takes more than 40 damage, she will convert to Ex. Rumia. Defeat Ex. Rumia with danmaku and obtain Rumia's Hairband.")
				.type(FrameType.CHALLENGE);

		danmaku.create("spellcard_power", YHItems.REIMU_SPELL.get(),
						CriterionBuilder.item(YHItems.REIMU_SPELL.get()),
						"Spellcard Power", "When you eat flesh in front of villagers, Reimu will try to exterminate you. Defat Reimu and obtain her spellcard")
				.type(FrameType.CHALLENGE);

		danmaku.create("feed_reimu", YHItems.REIMU_HAIRBAND.get(),
						Util.make(CriterionBuilder.and(), c -> Streams.concat(
										Arrays.stream(YHDish.values()).filter(e -> !e.isFlesh()).map(e -> e.block.get()),
										Arrays.stream(YHSake.values()).filter(e -> !e.isFlesh()).map(e -> e.item.get()),
										Arrays.stream(YHCoffee.values()).map(e -> e.item.get()),
										Arrays.stream(YHFood.values()).filter(YHFood::isReimuFood).map(e -> e.item.get()))
								.map(e -> Pair.of(e, FeedReimuTrigger.usedItem(e)))
								.forEach(p -> c.add(ForgeRegistries.ITEMS.getKey(p.getFirst().asItem()).toString(), p.getSecond()))),
						"Satisfied Reimu", "Feed Reimu all appealing food from Youkai's Homecoming to make her happy and give you her hairband")
				.add(new RewardBuilder(YoukaisHomecoming.REGISTRATE, 0, YoukaisHomecoming.loc("reimu_reward"), () ->
						LootTable.lootTable().withPool(LootPool.lootPool().add(
								LootTableTemplate.getItem(YHItems.REIMU_HAIRBAND.get(), 1, 1)))));

		root.finish();
	}

}
