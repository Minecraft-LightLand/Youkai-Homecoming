package dev.xkmc.youkaishomecoming.init.data;

import com.google.common.collect.Streams;
import com.tterrag.registrate.providers.RegistrateAdvancementProvider;
import dev.xkmc.l2library.serial.advancements.AdvancementGenerator;
import dev.xkmc.l2library.serial.advancements.CriterionBuilder;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.food.YHCoffee;
import dev.xkmc.youkaishomecoming.init.food.YHCrops;
import dev.xkmc.youkaishomecoming.init.food.YHDish;
import dev.xkmc.youkaishomecoming.init.food.YHFood;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
import net.minecraft.Util;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.*;
import net.minecraft.world.level.block.Blocks;

import java.util.Arrays;
import java.util.stream.Stream;

public class YHAdvGen {

	public static void genAdv(RegistrateAdvancementProvider pvd) {
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
		redbean.create("coffea", YHCrops.REDBEAN.getSeed(),
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
								.map(e -> ConsumeItemTrigger.TriggerInstance.usedItem(e.item.get()))
								.forEach(c::add)),
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
								).map(e -> ConsumeItemTrigger.TriggerInstance.usedItem(e.item.get()))
								.forEach(c::add)), "Tea Master", "Drink all kinds of tea in original flavor")
				.type(FrameType.GOAL, true, true, false);
		root.create("mousse", YHFood.KOISHI_MOUSSE.item.asStack(),
						CriterionBuilder.one(ConsumeItemTrigger.TriggerInstance.usedItem(
								ItemPredicate.Builder.item().of(YHFood.KOISHI_MOUSSE.item.get()).build())),
						"Well... Yes? What's Just Happened?", "Eat a Koishi Mousse")
				.type(FrameType.GOAL, true, true, true)
				.create("enthusiastic", YHDish.IMITATION_BEAR_PAW.block.asStack(),
						Util.make(CriterionBuilder.and(), c -> Streams.concat(
										Arrays.stream(YHDish.values()).map(e -> e.block.get()),
										Arrays.stream(YHCoffee.values()).map(e -> e.item.get()),
										Arrays.stream(YHFood.values()).map(e -> e.item.get()))
								.map(ConsumeItemTrigger.TriggerInstance::usedItem)
								.forEach(c::add)),
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
		youkai.create("powerful_being", YHItems.SUWAKO_HAT.asStack(),
						CriterionBuilder.one(EffectsChangedTrigger.TriggerInstance.hasEffects(
								MobEffectsPredicate.effects().and(YHEffects.YOUKAIFIED.get()))),
						"Powerful Being", "Get Youkaified effect")
				.type(FrameType.CHALLENGE, true, true, false);
		root.finish();
	}

}
