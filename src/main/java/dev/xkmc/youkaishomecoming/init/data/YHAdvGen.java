package dev.xkmc.youkaishomecoming.init.data;

import com.google.common.collect.Streams;
import com.mojang.datafixers.util.Pair;
import com.tterrag.registrate.providers.RegistrateAdvancementProvider;
import dev.xkmc.l2core.serial.advancements.AdvancementGenerator;
import dev.xkmc.l2core.serial.advancements.CriterionBuilder;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.food.*;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
import net.minecraft.Util;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

public class YHAdvGen {

	public static void genAdv(RegistrateAdvancementProvider pvd) {

		var gen = new AdvancementGenerator(pvd, YoukaisHomecoming.MODID);
		var b = gen.new TabBuilder("main");
		var root = b.root("welcome_to_youkais_homecoming", YHItems.OOLONG_TEA_BAG.asStack(),
				CriterionBuilder.one(PlayerTrigger.TriggerInstance.tick()),
				"Youkai's Homecoming", "Welcome To Youkai's Homecoming");
		root.create("sweet", YHFood.MOCHI.item.asStack(),
						CriterionBuilder.one(ConsumeItemTrigger.TriggerInstance.usedItem(
								ItemPredicate.Builder.item().of(YHTagGen.DANGO))),
						"Sweet?", "Eat a Mochi")
				.create("hmm", YHFood.SWEET_ORMOSIA_MOCHI_MIXED_BOILED.item.asStack(),
						CriterionBuilder.item(YHFood.SWEET_ORMOSIA_MOCHI_MIXED_BOILED.item.get()),
						"Hmm... Is it right?", "Get a Sweet Ormosia Mochi Mixed Boiled");
		var redbean = root.create("soybean", YHCrops.SOYBEAN.getSeed(),
						CriterionBuilder.one(ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(
								LocationPredicate.Builder.location().setBlock(
										BlockPredicate.Builder.block().of(Blocks.FARMLAND)),
								ItemPredicate.Builder.item().of(YHCrops.SOYBEAN.getSeed()))),
						"The Essential Harvest", "Plant Soybean")
				.create("redbean", YHCrops.REDBEAN.getSeed(),
						CriterionBuilder.one(ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(
								LocationPredicate.Builder.location().setBlock(
										BlockPredicate.Builder.block().of(YHTagGen.FARMLAND_REDBEAN)),
								ItemPredicate.Builder.item().of(YHCrops.REDBEAN.getSeed()))),
						"Leanness Resistant Red Bean", "Plant Red Bean on Coarse Dirt, Mud, or Clay");
		redbean.create("coffea", YHCrops.COFFEA.getSeed(),
						CriterionBuilder.one(ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(
								LocationPredicate.Builder.location().setBlock(
										BlockPredicate.Builder.block().of(YHTagGen.FARMLAND_COFFEA)),
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
								.forEach(p -> c.add(BuiltInRegistries.ITEM.getKey(p.getFirst().asItem()).toString(), p.getSecond()))),
						"Q Grader", "Drink Espresso")
				.type(AdvancementType.CHALLENGE, true, true, false);
		redbean.create("tea", YHCrops.TEA.getSeed(),
						CriterionBuilder.one(ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(
								LocationPredicate.Builder.location().setBlock(
										BlockPredicate.Builder.block().of(Blocks.FARMLAND)),
								ItemPredicate.Builder.item().of(YHCrops.TEA.getSeed()))),
						"Refreshing Hobby", "Plant Tea")
				.create("tea_master", YHFood.OOLONG_TEA.item.asStack(),
						Util.make(CriterionBuilder.and(), c -> Stream.of(
										YHFood.WHITE_TEA, YHFood.OOLONG_TEA, YHFood.GREEN_TEA, YHFood.BLACK_TEA
								).map(e -> e.item.get()).map(e -> Pair.of(e, ConsumeItemTrigger.TriggerInstance.usedItem(e)))
								.forEach(p -> c.add(BuiltInRegistries.ITEM.getKey(p.getFirst().asItem()).toString(), p.getSecond()))),
						"Tea Master", "Drink all kinds of tea in original flavor")
				.type(AdvancementType.GOAL, true, true, false);
		redbean.create("udumbara", YHCrops.UDUMBARA.getSeed(),
						CriterionBuilder.one(ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(
								LocationPredicate.Builder.location().setBlock(
										BlockPredicate.Builder.block().of(Blocks.FARMLAND)),
								ItemPredicate.Builder.item().of(YHCrops.UDUMBARA.getSeed()))),
						"Moon Crop", "Plants an Udumbara. It grows under moonlight and shrinks under sunlight.")
				.create("udumbara_flower", YHCrops.UDUMBARA.getFruits(),
						CriterionBuilder.item(YHCrops.UDUMBARA.getFruits()),
						"Fragile Flower", "Get an Udubara flower. It will only appear for 10 seconds during full moon.")
				.type(AdvancementType.CHALLENGE);
		root.create("alcoholic", YHBlocks.FERMENT.asStack(),
				CriterionBuilder.one(EffectsChangedTrigger.TriggerInstance.hasEffects(
						MobEffectsPredicate.Builder.effects().and(YHEffects.DRUNK))),
				"Alcoholic", "Brew and drink an alcoholic drink and obtain Drunk effect");
		root.create("passed_out", YHSake.DAIGINJO.item.asStack(),
				CriterionBuilder.one(EffectsChangedTrigger.TriggerInstance.hasEffects(
						MobEffectsPredicate.Builder.effects().and(YHEffects.DRUNK,
								new MobEffectsPredicate.MobEffectInstancePredicate(
										MinMaxBounds.Ints.atLeast(4), MinMaxBounds.Ints.ANY,
										Optional.empty(), Optional.empty())))),
				"Passed Out", "Drink until you have maximum Drunk effect");
		root.create("enthusiastic", YHDish.IMITATION_BEAR_PAW.block.asStack(),
						Util.make(CriterionBuilder.and(), c -> Streams.concat(
										Arrays.stream(YHDish.values()).map(e -> e.block.get()),
										Arrays.stream(YHSake.values()).map(e -> e.item.get()),
										Arrays.stream(YHCoffee.values()).map(e -> e.item.get()),
										Arrays.stream(YHFood.values()).map(e -> e.item.get()))
								.map(e -> Pair.of(e, ConsumeItemTrigger.TriggerInstance.usedItem(e)))
								.forEach(p -> c.add(BuiltInRegistries.ITEM.getKey(p.getFirst().asItem()).toString(), p.getSecond()))),
						"Gensokyo Food Enthusiastic", "Eat all Youkai's Homecoming food")
				.type(AdvancementType.CHALLENGE, true, true, false);
		root.finish();
	}

}
