package dev.xkmc.youkaishomecoming.init.data;

import com.google.common.collect.Streams;
import com.mojang.datafixers.util.Pair;
import com.tterrag.registrate.providers.RegistrateAdvancementProvider;
import dev.xkmc.l2library.serial.advancements.AdvancementGenerator;
import dev.xkmc.l2library.serial.advancements.CriterionBuilder;
import dev.xkmc.youkaishomecoming.content.pot.table.food.YHRolls;
import dev.xkmc.youkaishomecoming.content.pot.table.food.YHSushi;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.food.*;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import dev.xkmc.youkaishomecoming.init.registrate.YHCriteriaTriggers;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
import net.minecraft.Util;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.*;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;
import java.util.stream.Stream;

public class YHAdvGen {

	public static void genAdv(RegistrateAdvancementProvider pvd) {
		var gen = new AdvancementGenerator(pvd, YoukaisHomecoming.MODID);
		var b = gen.new TabBuilder("main");
		var root = b.root("welcome_to_gensokyo", YHItems.BLACK_TEA_BAG.asStack(),
				CriterionBuilder.one(PlayerTrigger.TriggerInstance.tick()),
				"Gensokyo Delight", "Welcome To Gensokyo");
		root.create("sweet", YHFood.MOCHI.item.asStack(),
						CriterionBuilder.one(ConsumeItemTrigger.TriggerInstance.usedItem(
								ItemPredicate.Builder.item().of(YHTagGen.DANGO).build())),
						"Sweet?", "Eat a Mochi")
				.create("hmm", YHFood.SWEET_ORMOSIA_MOCHI_MIXED_BOILED.item.asStack(),
						CriterionBuilder.item(YHFood.SWEET_ORMOSIA_MOCHI_MIXED_BOILED.item.get()),
						"Hmm... Is it right?", "Get a Sweet Ormosia Mochi Mixed Boiled");
		var soybean = root.create("soybean", YHCrops.SOYBEAN.getSeed(),
				CriterionBuilder.one(ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(
						LocationPredicate.Builder.location().setBlock(
								BlockPredicate.Builder.block().of(YHTagGen.FARMLAND_SOYBEAN).build()),
						ItemPredicate.Builder.item().of(YHCrops.SOYBEAN.getSeed()))),
				"The Essential Harvest", "Plant Soybean");
		soybean.create("cucumber", YHCrops.CUCUMBER.getSeed(),
						CriterionBuilder.one(ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(
								LocationPredicate.Builder.location().setBlock(
										BlockPredicate.Builder.block().of(YHTagGen.FARMLAND_SOYBEAN).build()),
								ItemPredicate.Builder.item().of(YHCrops.CUCUMBER.getSeed()))),
						"Rope Climber", "Plant Cucumber")
				.create("cucumber_top", YHCrops.CUCUMBER.getFruits(),
						CriterionBuilder.one(new PlayerTrigger.TriggerInstance(YHCriteriaTriggers.CUCUMBER.getId(), ContextAwarePredicate.ANY)),
						"Pinnacle Kappa", "Let cucumber climb ropes and harvest the top cucumber of a 3-block tall cucumber crop");
		var grape = soybean.create("grape", YHCrops.RED_GRAPE.getSeed(),
				CriterionBuilder.one(ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(
						LocationPredicate.Builder.location().setBlock(
								BlockPredicate.Builder.block().of(YHTagGen.FARMLAND_SOYBEAN).build()),
						ItemPredicate.Builder.item().of(YHTagGen.GRAPE_SEED))),
				"Sweet Vines", "Plant Grape");
		grape.create("grape_cut", Items.SHEARS,
						CriterionBuilder.one(new PlayerTrigger.TriggerInstance(YHCriteriaTriggers.GRAPE_CUT.getId(), ContextAwarePredicate.ANY)),
						"Niwaki", "Cut the leaves off a mature grape crop so that it can grow larger. Make sure it has 3 ropes in a row to climb onto.")
				.create("grape_harvest", Items.SHEARS,
						CriterionBuilder.one(new PlayerTrigger.TriggerInstance(YHCriteriaTriggers.GRAPE_HARVEST.getId(), ContextAwarePredicate.ANY)),
						"The Best Bunch", "Harvest grape hanging under a grape branch");
		grape.create("squeeze", YHBlocks.BASIN.asItem(),
				CriterionBuilder.one(new PlayerTrigger.TriggerInstance(YHCriteriaTriggers.BASIN.getId(), ContextAwarePredicate.ANY)),
				"Squeeze!", "Jump in the basin to squeeze juice out of grapes");

		var redbean = soybean.create("redbean", YHCrops.REDBEAN.getSeed(),
				CriterionBuilder.one(ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(
						LocationPredicate.Builder.location().setBlock(
								BlockPredicate.Builder.block().of(YHTagGen.FARMLAND_REDBEAN).build()),
						ItemPredicate.Builder.item().of(YHCrops.REDBEAN.getSeed()))),
				"Leanness Resistant Red Bean", "Plant Red Bean on Coarse Dirt, Mud, or Clay");
		redbean.create("tea", YHCrops.TEA.getSeed(),
						CriterionBuilder.one(ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(
								LocationPredicate.Builder.location().setBlock(
										BlockPredicate.Builder.block().of(Blocks.FARMLAND).build()),
								ItemPredicate.Builder.item().of(YHCrops.TEA.getSeed()))),
						"Refreshing Hobby", "Plant Tea")
				.create("tea_master", YHDrink.OOLONG_TEA.item.asStack(),
						Util.make(CriterionBuilder.and(), c -> Stream.of(
										YHDrink.WHITE_TEA, YHDrink.OOLONG_TEA, YHDrink.GREEN_TEA, YHDrink.BLACK_TEA
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
						"Alcoholic", "Brew and drink an alcoholic drink and obtain Drunk effect")
				.create("passed_out", YHDrink.DAIGINJO.item.asStack(),
						CriterionBuilder.one(EffectsChangedTrigger.TriggerInstance.hasEffects(
								MobEffectsPredicate.effects().and(YHEffects.DRUNK.get(),
										new MobEffectsPredicate.MobEffectInstancePredicate(
												MinMaxBounds.Ints.atLeast(4), MinMaxBounds.Ints.ANY,
												null, null)))),
						"Passed Out", "Drink until you have maximum Drunk effect");
		root.create("crab_grab", YHItems.CRAB_BUCKET.asItem(),
				CriterionBuilder.one(new PlayerTrigger.TriggerInstance(YHCriteriaTriggers.CRAB_GRAB.getId(), ContextAwarePredicate.ANY)),
				"Crab Grab", "Have a crab grab your water bucket when attempting to bucket a crab");
		root.create("small_pot", YHItems.IRON_BOWL.asStack(),
				CriterionBuilder.one(new PlayerTrigger.TriggerInstance(YHCriteriaTriggers.COOKING.getId(), ContextAwarePredicate.ANY)),
				"Hotpot", "Cooking with iron bowl");//TODO more pots
		var table = root.create("cuisine_board", YHBlocks.CUISINE_BOARD.asStack(),
				CriterionBuilder.one(new PlayerTrigger.TriggerInstance(YHCriteriaTriggers.TABLE.getId(), ContextAwarePredicate.ANY)),
				"Sushi Apprentice", "Make something with cuisine board");
		table.create("kaguya_hime", YHBowl.KAGUYA_HIME.item.asStack(),
				CriterionBuilder.item(YHBowl.KAGUYA_HIME.item.asItem()),
				"Tale of the Bamboo Princess", "Craft and Steam Kaguya Hime");
		table.create("sushi_master", YHSushi.OTORO_NIGIRI.asItem(),
						CriterionBuilder.and()
								.add(InventoryChangeTrigger.TriggerInstance.hasItems(YHSushi.OTORO_NIGIRI.asItem()))
								.add(InventoryChangeTrigger.TriggerInstance.hasItems(YHSushi.TOBIKO_GUNKAN.asItem()))
								.add(InventoryChangeTrigger.TriggerInstance.hasItems(YHSushi.LORELEI_NIGIRI.asItem()))
								.add(InventoryChangeTrigger.TriggerInstance.hasItems(YHRolls.RAINBOW_FUTOMAKI.slice.asItem())),
						"Sushi Master", "Make Otoro Nigiri, Lorelei Nigiri, Tobiko Gunkan, and Rainbow Futomaki Slice")
				.create("foreign_sushi_master", YHRolls.RAINBOW_ROLL.slice.asItem(),
						CriterionBuilder.and()
								.add(InventoryChangeTrigger.TriggerInstance.hasItems(YHRolls.RAINBOW_ROLL.slice.asItem()))
								.add(InventoryChangeTrigger.TriggerInstance.hasItems(YHRolls.VOLCANO_ROLL.slice.asItem())),
						"Sushi Aboard", "Make Rainbow Roll Slice and Volcano Roll Slice");
		root.create("enthusiastic", YHDish.IMITATION_BEAR_PAW.block.asStack(),
						Util.make(CriterionBuilder.and(), c -> Streams.concat(
										Arrays.stream(YHDish.values()).map(e -> e.block.get()),
										Arrays.stream(YHDrink.values()).map(e -> e.item.get()),
										Arrays.stream(YHBowl.values()).map(e -> e.item.get()),
										Arrays.stream(YHFood.values()).map(e -> e.item.get()),
										Arrays.stream(YHSushi.values()).map(e -> e.item.get()),
										Arrays.stream(YHRolls.values()).map(e -> e.slice.get()))
								.map(e -> Pair.of(e, ConsumeItemTrigger.TriggerInstance.usedItem(e)))
								.forEach(p -> c.add(ForgeRegistries.ITEMS.getKey(p.getFirst().asItem()).toString(), p.getSecond()))),
						"Gensokyo Food Enthusiastic", "Eat all Youkai's Homecoming food")
				.type(FrameType.CHALLENGE, true, true, false);

		root.finish();
	}

}
