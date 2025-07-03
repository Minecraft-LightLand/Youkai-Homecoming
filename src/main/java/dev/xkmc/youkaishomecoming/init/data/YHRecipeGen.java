package dev.xkmc.youkaishomecoming.init.data;

import com.simibubi.create.Create;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.fruitsdelight.init.FruitsDelight;
import dev.xkmc.fruitsdelight.init.food.FDFood;
import dev.xkmc.fruitsdelight.init.food.FruitType;
import dev.xkmc.l2library.compat.patchouli.ShapelessPatchouliBuilder;
import dev.xkmc.l2library.serial.ingredients.PotionIngredient;
import dev.xkmc.l2library.serial.recipe.ConditionalRecipeWrapper;
import dev.xkmc.youkaishomecoming.compat.create.CreateRecipeGen;
import dev.xkmc.youkaishomecoming.compat.food.FruitsDelightCompatFood;
import dev.xkmc.youkaishomecoming.content.pot.base.BasePotFinishedRecipe;
import dev.xkmc.youkaishomecoming.content.pot.basin.SimpleBasinBuilder;
import dev.xkmc.youkaishomecoming.content.pot.ferment.SimpleFermentationBuilder;
import dev.xkmc.youkaishomecoming.content.pot.table.food.YHRolls;
import dev.xkmc.youkaishomecoming.content.pot.table.food.YHSushi;
import dev.xkmc.youkaishomecoming.content.pot.table.item.TableItemManager;
import dev.xkmc.youkaishomecoming.content.pot.table.recipe.MixedRecipeBuilder;
import dev.xkmc.youkaishomecoming.content.pot.table.recipe.OrderedRecipeBuilder;
import dev.xkmc.youkaishomecoming.init.food.*;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.fml.ModList;
import vectorwing.farmersdelight.client.recipebook.CookingPotRecipeBookTab;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.tag.ForgeTags;
import vectorwing.farmersdelight.common.tag.ModTags;
import vectorwing.farmersdelight.data.builder.CookingPotRecipeBuilder;
import vectorwing.farmersdelight.data.builder.CuttingBoardRecipeBuilder;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class YHRecipeGen {

	public static void genRecipes(RegistrateRecipeProvider pvd) {
		{
			foodCut(pvd, YHFood.RAW_LAMPREY, YHFood.ROASTED_LAMPREY, YHFood.RAW_LAMPREY_FILLET, YHFood.ROASTED_LAMPREY_FILLET, 2);
			foodCut(pvd, YHFood.RAW_TUNA, YHFood.SEARED_TUNA, YHFood.RAW_TUNA_SLICE, YHFood.SEARED_TUNA_SLICE, 3);
			food(pvd, YHFood.TOFU, YHFood.OILY_BEAN_CURD);
			pvd.stonecutting(DataIngredient.items(Items.CLAY_BALL), RecipeCategory.MISC, YHItems.CLAY_SAUCER);
			pvd.stonecutting(DataIngredient.items(Items.BAMBOO_BLOCK), RecipeCategory.MISC, YHBlocks.RACK);
			pvd.stonecutting(DataIngredient.items(Items.BAMBOO_BLOCK), RecipeCategory.MISC, YHBlocks.STEAMER_RACK);
			pvd.stonecutting(DataIngredient.items(Items.BAMBOO_BLOCK), RecipeCategory.MISC, YHBlocks.CUISINE_BOARD);
			pvd.stonecutting(DataIngredient.items(Items.COPPER_BLOCK), RecipeCategory.MISC, YHBlocks.COPPER_TANK);
			pvd.stonecutting(DataIngredient.items(Items.COPPER_INGOT), RecipeCategory.MISC, YHBlocks.COPPER_FAUCET);
			pvd.stonecutting(DataIngredient.tag(ItemTags.PLANKS), RecipeCategory.MISC, YHBlocks.STEAMER_LID);
			pvd.smelting(DataIngredient.items(YHItems.CLAY_SAUCER.get()), RecipeCategory.MISC, YHItems.SAUCER, 0.1f, 200);
			pvd.stonecutting(DataIngredient.items(Items.GLASS), RecipeCategory.MISC, YHItems.SAKE_BOTTLE);
			for (var e : YHBlocks.WoodType.values()) {

				unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, e.seat, 4)::unlockedBy, ModItems.CANVAS.get())
						.pattern("PCP").pattern("PSP")
						.define('C', ModItems.CANVAS.get())
						.define('P', e.plank)
						.define('S', Items.STICK)
						.save(pvd);

				unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, e.table, 4)::unlockedBy, Items.STICK)
						.pattern("WWW").pattern(" S ").pattern(" P ")
						.define('W', e.strippedWood)
						.define('S', Items.STICK)
						.define('P', e.slab)
						.save(pvd);

			}

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, YHBlocks.STEAMER_POT)::unlockedBy, Items.IRON_INGOT)
					.pattern("B B").pattern("I I").pattern("CIC")
					.define('B', Items.BAMBOO)
					.define('C', Items.CLAY_BALL)
					.define('I', Items.IRON_INGOT)
					.save(pvd);

		}

		// furniture
		{

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, YHBlocks.MOKA)::unlockedBy, YHItems.COFFEE_POWDER.get())
					.pattern("ABA").pattern("IWI").pattern("ADA")
					.define('A', Items.IRON_NUGGET)
					.define('I', Items.IRON_INGOT)
					.define('B', YHItems.COFFEE_POWDER)
					.define('D', Items.DEEPSLATE)
					.define('W', new PotionIngredient(Potions.WATER))
					.save(pvd);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, YHBlocks.KETTLE)::unlockedBy, YHCrops.TEA.getFruits())
					.pattern("ABA").pattern("IWI").pattern("AIA")
					.define('A', Items.IRON_NUGGET)
					.define('I', Items.IRON_INGOT)
					.define('B', YHCrops.TEA.getFruits())
					.define('W', Items.WATER_BUCKET)
					.save(pvd);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, YHBlocks.MOKA_KIT)::unlockedBy, Items.IRON_INGOT)
					.pattern("ABA").pattern("I I").pattern("EDE")
					.define('A', Items.IRON_NUGGET)
					.define('I', Items.IRON_INGOT)
					.define('B', Items.BLACK_DYE)
					.define('D', Items.DEEPSLATE)
					.define('E', Items.TERRACOTTA)
					.save(pvd);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, YHBlocks.FERMENT)::unlockedBy, Items.BUCKET)
					.pattern("ABA").pattern("ACA").pattern("AAA")
					.define('A', Items.MUD_BRICKS)
					.define('B', ItemTags.WOODEN_TRAPDOORS)
					.define('C', Items.BUCKET)
					.save(pvd);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, YHBlocks.BASIN)::unlockedBy, Items.IRON_NUGGET)
					.pattern("C C").pattern("BAB")
					.define('A', ItemTags.WOODEN_SLABS)
					.define('B', ItemTags.WOODEN_STAIRS)
					.define('C', Items.IRON_NUGGET)
					.save(pvd);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, YHBlocks.MOON_LANTERN)::unlockedBy, YHCrops.UDUMBARA.getFruits())
					.pattern("RAR").pattern("GFG").pattern("RAR")
					.define('A', Items.MANGROVE_PLANKS)
					.define('G', Items.AMETHYST_SHARD)
					.define('R', Items.END_ROD)
					.define('F', YHCrops.UDUMBARA.getFruits())
					.save(pvd);

		}

		// plants
		{
			cutting(pvd, YHCrops.SOYBEAN.fruits, YHCrops.SOYBEAN.seed, 2);
			cutting(pvd, YHCrops.COFFEA.fruits, YHCrops.COFFEA.seed, 2);
			cutting(pvd, YHCrops.CUCUMBER.fruits, YHFood.CUCUMBER_SLICE.item, 2);
			pvd.singleItem(DataIngredient.items(YHCrops.SOYBEAN.fruits.get()), RecipeCategory.MISC, YHCrops.SOYBEAN.seed, 1, 1);
			pvd.singleItem(DataIngredient.items(YHCrops.COFFEA.fruits.get()), RecipeCategory.MISC, YHCrops.COFFEA.seed, 1, 1);
			pvd.singleItem(DataIngredient.items(YHCrops.CUCUMBER.fruits.get()), RecipeCategory.MISC, YHCrops.CUCUMBER.seed, 1, 1);
			pvd.singleItem(DataIngredient.items(YHCrops.RED_GRAPE.fruits.get()), RecipeCategory.MISC, YHCrops.RED_GRAPE.seed, 1, 1);
			pvd.singleItem(DataIngredient.items(YHCrops.BLACK_GRAPE.fruits.get()), RecipeCategory.MISC, YHCrops.BLACK_GRAPE.seed, 1, 1);
			pvd.singleItem(DataIngredient.items(YHCrops.WHITE_GRAPE.fruits.get()), RecipeCategory.MISC, YHCrops.WHITE_GRAPE.seed, 1, 1);

			pvd.smelting(DataIngredient.items(YHCrops.COFFEA.getSeed()), RecipeCategory.MISC, YHItems.COFFEE_BEAN, 0.1f, 200);
			pvd.smoking(DataIngredient.items(YHCrops.COFFEA.getSeed()), RecipeCategory.MISC, YHItems.COFFEE_BEAN, 0.1f, 200);
			drying(pvd, DataIngredient.tag(YHTagGen.GRAPE), YHFood.RAISIN.item);
			pvd.storage(YHCrops.SOYBEAN::getFruits, RecipeCategory.MISC, YHItems.PODS_CRATE);
			pvd.storage(YHCrops.SOYBEAN::getSeed, RecipeCategory.MISC, YHItems.SOYBEAN_BAG);
			pvd.storage(YHCrops.REDBEAN::getSeed, RecipeCategory.MISC, YHItems.REDBEAN_BAG);
			pvd.storage(YHCrops.CUCUMBER::getFruits, RecipeCategory.MISC, YHItems.CUCUMBER_BAG);
			pvd.storage(YHCrops.RED_GRAPE::getFruits, RecipeCategory.MISC, YHItems.RED_GRAPE_CRATE);
			pvd.storage(YHCrops.BLACK_GRAPE::getFruits, RecipeCategory.MISC, YHItems.BLACK_GRAPE_CRATE);
			pvd.storage(YHCrops.WHITE_GRAPE::getFruits, RecipeCategory.MISC, YHItems.WHITE_GRAPE_CRATE);
			pvd.storage(YHItems.COFFEE_BEAN, RecipeCategory.MISC, YHItems.COFFEE_BEAN_BAG);
			pvd.storage(YHCrops.TEA::getFruits, RecipeCategory.MISC, YHItems.TEA_BAG);
			pvd.storage(YHTea.BLACK.leaves, RecipeCategory.MISC, YHItems.BLACK_TEA_BAG);
			pvd.storage(YHTea.GREEN.leaves, RecipeCategory.MISC, YHItems.GREEN_TEA_BAG);
			pvd.storage(YHTea.OOLONG.leaves, RecipeCategory.MISC, YHItems.OOLONG_TEA_BAG);
			pvd.storage(YHTea.WHITE.leaves, RecipeCategory.MISC, YHItems.WHITE_TEA_BAG);

			drying(pvd, DataIngredient.items(Items.WHEAT), ModItems.STRAW);
			drying(pvd, DataIngredient.items(Items.KELP), () -> Items.DRIED_KELP);
			drying(pvd, DataIngredient.tag(ItemTags.SAPLINGS), () -> Items.DEAD_BUSH);
			drying(pvd, DataIngredient.items(Items.ROTTEN_FLESH), () -> Items.LEATHER, 18000);
			drying(pvd, DataIngredient.items(YHCrops.TEA.getFruits()), YHTea.GREEN.leaves);
			pvd.smoking(DataIngredient.items(YHTea.GREEN.leaves.get()), RecipeCategory.MISC, YHTea.BLACK.leaves, 0.1f, 200);
			pvd.campfire(DataIngredient.items(YHTea.GREEN.leaves.get()), RecipeCategory.MISC, YHTea.OOLONG.leaves, 0.1f, 200);

			CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.SALMON_BUCKET),
							Ingredient.of(ForgeTags.TOOLS_KNIVES), Items.WATER_BUCKET, 1)
					.addResult(ModItems.SALMON_SLICE.get(), 2)
					.addResult(Items.BONE_MEAL)
					.addResultWithChance(YHFood.ROE.item.get(), 0.5f, 1)
					.build(pvd, YHFood.ROE.item.getId());

			CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(YHItems.COFFEE_BEAN),
							Ingredient.of(ForgeTags.TOOLS_SHOVELS), YHItems.COFFEE_POWDER, 1)
					.build(pvd, YHItems.COFFEE_POWDER.getId());

			CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(YHTea.GREEN.leaves),
							Ingredient.of(ForgeTags.TOOLS_SHOVELS), YHItems.MATCHA, 1)
					.build(pvd, YHItems.MATCHA.getId());

			CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.ICE),
							Ingredient.of(ForgeTags.TOOLS_PICKAXES), YHItems.ICE_CUBE, 8)
					.build(pvd, YHItems.ICE_CUBE.getId());

			drying(pvd, DataIngredient.items(YHTea.GREEN.leaves.get()), YHTea.WHITE.leaves);
		}

		// food craft
		{
			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, YHFood.MILK_POPSICLE.item, 1)::unlockedBy, YHItems.ICE_CUBE.get())
					.pattern(" MM").pattern("SIM").pattern("TS ")
					.define('M', ForgeTags.MILK_BOTTLE)
					.define('S', Items.SUGAR)
					.define('I', YHTagGen.ICE)
					.define('T', Items.STICK)
					.save(pvd);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, YHFood.BIG_POPSICLE.item, 1)::unlockedBy, YHItems.ICE_CUBE.get())
					.pattern(" II").pattern("SII").pattern("TS ")
					.define('S', Items.SUGAR)
					.define('I', YHTagGen.ICE)
					.define('T', Items.STICK)
					.save(pvd);

			unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, YHFood.ASSORTED_DANGO.item, 1)::unlockedBy, YHFood.MOCHI.item.get())
					.requires(YHFood.MOCHI.item).requires(YHFood.MATCHA_MOCHI.item).requires(YHFood.SAKURA_MOCHI.item).requires(Items.STICK)
					.save(pvd);

			unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, YHFood.KINAKO_DANGO.item, 1)::unlockedBy, YHFood.MOCHI.item.get())
					.requires(YHTagGen.DANGO).requires(YHTagGen.DANGO).requires(YHTagGen.DANGO)
					.requires(YHCrops.SOYBEAN.getSeed()).requires(Items.STICK)
					.save(pvd);

			unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, YHFood.SHAVED_ICE_OVER_RICE.item, 1)::unlockedBy, YHItems.ICE_CUBE.get())
					.requires(ForgeTags.GRAIN_RICE).requires(YHTagGen.ICE).requires(YHCrops.REDBEAN.getSeed())
					.requires(ModItems.COD_ROLL.get())
					.save(pvd);

			cake(pvd, YHItems.TARTE_LUNE);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, YHItems.TARTE_LUNE.block, 1)::unlockedBy, ModItems.PIE_CRUST.get())
					.pattern("FBF").pattern("DCD").pattern("AEA")
					.define('A', Items.SUGAR)
					.define('B', Items.ALLIUM)
					.define('D', Items.CORNFLOWER)
					.define('F', Items.WHEAT)
					.define('C', YHItems.CREAM)
					.define('E', ModItems.PIE_CRUST.get())
					.save(pvd);


			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, YHFood.DOUGHNUT.item, 4)::unlockedBy, ModItems.WHEAT_DOUGH.get())
					.pattern("CAC").pattern("ABA").pattern("CAC")
					.define('A', ForgeTags.DOUGH_WHEAT)
					.define('B', YHItems.CREAM)
					.define('C', Items.SUGAR)
					.save(pvd);

			unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, YHFood.HIGI_CHOCOLATE.item, 3)::unlockedBy, Items.COCOA_BEANS)
					.requires(YHItems.MATCHA).requires(Items.TWISTING_VINES).requires(Items.PINK_PETALS)
					.requires(Items.HONEY_BOTTLE).requires(Items.BLAZE_POWDER).requires(Items.BLUE_ORCHID)
					.requires(Items.COCOA_BEANS, 3)
					.save(pvd);

			unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, YHFood.HIGI_DOUGHNUT.item, 1)::unlockedBy, YHFood.HIGI_CHOCOLATE.item.get())
					.requires(YHFood.DOUGHNUT.item).requires(YHFood.HIGI_CHOCOLATE.item)
					.save(pvd);

		}

		// food cooking
		{

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.BUTTER.item.get(), 1, 200, 0.1f)
					.addIngredient(YHItems.CREAM)
					.addIngredient(YHItems.CREAM)
					.build(pvd, YHFood.BUTTER.item.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHItems.MAYONNAISE.item.get(), 3, 200, 0.1f, Items.GLASS_BOTTLE)
					.addIngredient(YHFood.BUTTER.item)
					.addIngredient(YHFood.BUTTER.item)
					.addIngredient(ForgeTags.EGGS)
					.addIngredient(ForgeTags.EGGS)
					.addIngredient(Items.SUGAR)
					.build(pvd, YHItems.MAYONNAISE.item.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.IMITATION_CRAB.item.get(), 4, 200, 0.1f)
					.addIngredient(ForgeTags.RAW_FISHES_COD)
					.addIngredient(ForgeTags.RAW_FISHES_COD)
					.addIngredient(Items.WHEAT)
					.addIngredient(Items.WHEAT)
					.addIngredient(Items.SUGAR)
					.build(pvd, YHFood.IMITATION_CRAB.item.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.TAMAGOYAKI.item.get(), 1, 200, 0.1f)
					.addIngredient(ForgeTags.EGGS)
					.addIngredient(ForgeTags.EGGS)
					.addIngredient(ForgeTags.EGGS)
					.addIngredient(ForgeTags.MILK_BOTTLE)
					.addIngredient(Items.SUGAR)
					.build(pvd, YHFood.TAMAGOYAKI.item.getId());

			cutting(pvd, YHFood.TAMAGOYAKI.item, YHFood.TAMAGOYAKI_SLICE.item, 3);

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.KABAYAKI.item.get(), 1, 200, 0.1f)
					.addIngredient(YHFood.RAW_LAMPREY_FILLET.item)
					.addIngredient(YHItems.SOY_SAUCE_BOTTLE.item)
					.addIngredient(Items.SUGAR)
					.build(pvd, YHFood.KABAYAKI.item.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.LAMPREY_SKEWER.item.get(), 1, 200, 0.1f, Items.STICK)
					.addIngredient(YHFood.RAW_LAMPREY_FILLET.item)
					.addIngredient(YHFood.RAW_LAMPREY_FILLET.item)
					.addIngredient(YHFood.RAW_LAMPREY_FILLET.item)
					.addIngredient(YHItems.SOY_SAUCE_BOTTLE.item)
					.addIngredient(Items.SUGAR)
					.build(pvd, YHFood.LAMPREY_SKEWER.item.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.TOFU.item.get(), 1, 200, 0.1f)
					.addIngredient(YHCrops.SOYBEAN.getSeed())
					.addIngredient(YHCrops.SOYBEAN.getSeed())
					.build(pvd, YHFood.TOFU.item.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.ONIGILI.item.get(), 2, 200, 0.1f)
					.addIngredient(ForgeTags.GRAIN_RICE)
					.addIngredient(ForgeTags.VEGETABLES)
					.addIngredient(Items.DRIED_KELP)
					.build(pvd, YHFood.ONIGILI.item.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.SEKIBANKIYAKI.item.get(), 2, 200, 0.1f)
					.addIngredient(ForgeTags.GRAIN_RICE)
					.addIngredient(YHCrops.REDBEAN.getSeed())
					.addIngredient(YHFood.BUTTER.item)
					.build(pvd, YHFood.SEKIBANKIYAKI.item.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.MOCHI.item.get(), 2, 200, 0.1f)
					.addIngredient(ForgeTags.GRAIN_RICE)
					.addIngredient(YHCrops.REDBEAN.getSeed())
					.build(pvd, YHFood.MOCHI.item.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.TSUKIMI_DANGO.item.get(), 2, 200, 0.1f)
					.addIngredient(ForgeTags.GRAIN_RICE)
					.addIngredient(YHCrops.SOYBEAN.getSeed())
					.build(pvd, YHFood.TSUKIMI_DANGO.item.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.YASHOUMA_DANGO.item.get(), 1, 200, 0.1f)
					.addIngredient(ForgeTags.GRAIN_RICE)
					.addIngredient(Items.PINK_DYE)
					.addIngredient(Items.GREEN_DYE)
					.build(pvd, YHFood.YASHOUMA_DANGO.item.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.SAKURA_MOCHI.item.get(), 2, 200, 0.1f)
					.addIngredient(ForgeTags.GRAIN_RICE)
					.addIngredient(YHCrops.REDBEAN.getSeed())
					.addIngredient(Items.CHERRY_LEAVES)
					.build(pvd, YHFood.SAKURA_MOCHI.item.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.COFFEE_MOCHI.item.get(), 2, 200, 0.1f)
					.addIngredient(ForgeTags.GRAIN_RICE)
					.addIngredient(YHItems.COFFEE_POWDER)
					.build(pvd, YHFood.COFFEE_MOCHI.item.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.MATCHA_MOCHI.item.get(), 2, 200, 0.1f)
					.addIngredient(ForgeTags.GRAIN_RICE)
					.addIngredient(YHTagGen.MATCHA)
					.build(pvd, YHFood.MATCHA_MOCHI.item.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.SENBEI.item.get(), 3, 200, 0.1f)
					.addIngredient(ForgeTags.GRAIN_RICE)
					.addIngredient(YHFood.BUTTER.item)
					.addIngredient(Items.DRIED_KELP)
					.build(pvd, YHFood.SENBEI.item.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.YAKUMO_INARI.item.get(), 3, 200, 0.1f)
					.addIngredient(ForgeTags.GRAIN_RICE)
					.addIngredient(ForgeTags.EGGS)
					.addIngredient(Items.CARROT)
					.addIngredient(YHFood.OILY_BEAN_CURD.item.get())
					.addIngredient(YHFood.OILY_BEAN_CURD.item.get())
					.addIngredient(YHFood.OILY_BEAN_CURD.item.get())
					.build(pvd, YHFood.YAKUMO_INARI.item.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.PORK_RICE_BALL.item.get(), 1, 200, 0.1f)
					.addIngredient(ForgeTags.GRAIN_RICE)
					.addIngredient(ForgeTags.RAW_PORK)
					.build(pvd, YHFood.PORK_RICE_BALL.item.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.TUTU_CONGEE.item.get(), 1, 200, 0.1f)
					.addIngredient(ForgeTags.GRAIN_RICE)
					.addIngredient(Items.BAMBOO)
					.build(pvd, YHFood.TUTU_CONGEE.item.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.STEAMED_EGG_IN_BAMBOO.item.get(), 1, 200, 0.1f)
					.addIngredient(ForgeTags.EGGS)
					.addIngredient(Items.BAMBOO)
					.build(pvd, YHFood.STEAMED_EGG_IN_BAMBOO.item.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.CANDY_APPLE.item.get(), 1, 200, 0.1f, Items.STICK)
					.addIngredient(Items.APPLE)
					.addIngredient(Items.SUGAR)
					.addIngredient(Items.SUGAR)
					.addIngredient(Items.SUGAR)
					.build(pvd, YHFood.CANDY_APPLE.item.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.MITARASHI_DANGO.item.get(), 1, 200, 0.1f, Items.STICK)
					.addIngredient(YHTagGen.DANGO)
					.addIngredient(YHTagGen.DANGO)
					.addIngredient(YHTagGen.DANGO)
					.addIngredient(YHCrops.SOYBEAN.getSeed())
					.addIngredient(YHItems.SOY_SAUCE_BOTTLE.item)
					.addIngredient(Items.SUGAR)
					.build(pvd, YHFood.MITARASHI_DANGO.item.getId());


		}

		// food cooking bowl
		{
			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.APAKI.item.get(), 1, 200, 0.1f, Items.BOWL)
					.addIngredient(ForgeTags.RAW_PORK)
					.addIngredient(ForgeTags.RAW_PORK)
					.addIngredient(Items.PINK_PETALS)
					.build(pvd, YHFood.APAKI.item.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.AVGOLEMONO.item.get(), 1, 200, 0.1f, Items.BOWL)
					.addIngredient(ForgeTags.EGGS)
					.addIngredient(Items.GLOW_BERRIES)
					.addIngredient(Items.GLOW_BERRIES)
					.build(pvd, YHFood.AVGOLEMONO.item.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.BLAZING_RED_CURRY.item.get(), 1, 200, 0.1f, Items.BOWL)
					.addIngredient(ForgeTags.GRAIN_RICE)
					.addIngredient(Items.CRIMSON_FUNGUS)
					.addIngredient(Items.CRIMSON_FUNGUS)
					.addIngredient(Items.BLAZE_POWDER)
					.addIngredient(ForgeTags.VEGETABLES_POTATO)
					.addIngredient(ForgeTags.RAW_CHICKEN)
					.build(pvd, YHFood.BLAZING_RED_CURRY.item.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.GRILLED_EEL_OVER_RICE.item.get(), 1, 200, 0.1f, Items.BOWL)
					.addIngredient(YHTagGen.RAW_EEL)
					.addIngredient(YHTagGen.RAW_EEL)
					.addIngredient(ForgeTags.GRAIN_RICE)
					.addIngredient(YHItems.SOY_SAUCE_BOTTLE.item)
					.build(pvd, YHFood.GRILLED_EEL_OVER_RICE.item.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.HIGAN_SOUP.item.get(), 1, 200, 0.1f, Items.BOWL)
					.addIngredient(Items.SOUL_SAND)
					.addIngredient(Items.SOUL_SAND)
					.addIngredient(ForgeTags.CROPS)
					.build(pvd, YHFood.HIGAN_SOUP.item.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.LONGEVITY_NOODLES.item.get(), 1, 200, 0.1f, Items.BOWL)
					.addIngredient(ForgeTags.PASTA)
					.addIngredient(ForgeTags.VEGETABLES)
					.addIngredient(Items.BROWN_MUSHROOM)
					.addIngredient(ForgeTags.RAW_PORK)
					.build(pvd, YHFood.LONGEVITY_NOODLES.item.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.MISO_SOUP.item.get(), 1, 200, 0.1f, Items.BOWL)
					.addIngredient(YHFood.TOFU.item.get())
					.addIngredient(YHCrops.SOYBEAN.getSeed())
					.addIngredient(Items.DRIED_KELP)
					.addIngredient(Items.BROWN_MUSHROOM)
					.build(pvd, YHFood.MISO_SOUP.item.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.SEAFOOD_MISO_SOUP.item.get(), 1, 200, 0.1f, Items.BOWL)
					.addIngredient(YHFood.TOFU.item.get())
					.addIngredient(YHCrops.SOYBEAN.getSeed())
					.addIngredient(Items.DRIED_KELP)
					.addIngredient(Items.BROWN_MUSHROOM)
					.addIngredient(ForgeTags.RAW_FISHES_SALMON)
					.addIngredient(ForgeTags.RAW_FISHES_SALMON)
					.build(pvd, YHFood.SEAFOOD_MISO_SOUP.item.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.POOR_GOD_SOUP.item.get(), 1, 200, 0.1f, Items.BOWL)
					.addIngredient(ForgeTags.SEEDS)
					.addIngredient(ForgeTags.CROPS)
					.addIngredient(ItemTags.FLOWERS)
					.addIngredient(Items.BONE_MEAL)
					.build(pvd, YHFood.POOR_GOD_SOUP.item.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.POWER_SOUP.item.get(), 1, 200, 0.1f, Items.BOWL)
					.addIngredient(ForgeTags.RAW_PORK)
					.addIngredient(ForgeTags.RAW_PORK)
					.addIngredient(Items.KELP)
					.addIngredient(Items.KELP)
					.addIngredient(ForgeTags.VEGETABLES_ONION)
					.build(pvd, YHFood.POWER_SOUP.item.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.SHIRAYUKI.item.get(), 1, 200, 0.1f, Items.BOWL)
					.addIngredient(Items.PUFFERFISH)
					.addIngredient(YHTagGen.RAW_EEL)
					.addIngredient(Items.KELP)
					.addIngredient(YHFood.TOFU.item.get())
					.addIngredient(ForgeTags.VEGETABLES)
					.build(pvd, YHFood.SHIRAYUKI.item.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.SWEET_ORMOSIA_MOCHI_MIXED_BOILED.item.get(), 1, 200, 0.1f, Items.BOWL)
					.addIngredient(YHTagGen.DANGO)
					.addIngredient(Items.CARROT)
					.addIngredient(ForgeTags.VEGETABLES)
					.build(pvd, YHFood.SWEET_ORMOSIA_MOCHI_MIXED_BOILED.item.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHItems.CREAM.get(), 1, 200, 0.1f, Items.BOWL)
					.addIngredient(ForgeTags.MILK_BOTTLE)
					.addIngredient(ForgeTags.MILK_BOTTLE)
					.addIngredient(ForgeTags.MILK_BOTTLE)
					.build(pvd, YHItems.CREAM.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.TUSCAN_SALMON.item.get(), 1, 200, 0.1f, Items.BOWL)
					.addIngredient(ForgeTags.RAW_FISHES_SALMON)
					.addIngredient(ForgeTags.VEGETABLES_TOMATO)
					.addIngredient(ForgeTags.SALAD_INGREDIENTS_CABBAGE)
					.addIngredient(YHItems.CREAM.get())
					.build(pvd, YHFood.TUSCAN_SALMON.item.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.MUSHROOM_SOUP.item.get(), 1, 200, 0.1f, Items.BOWL)
					.addIngredient(Items.BROWN_MUSHROOM)
					.addIngredient(Items.BROWN_MUSHROOM)
					.addIngredient(Items.BROWN_MUSHROOM)
					.addIngredient(ForgeTags.VEGETABLES_ONION)
					.addIngredient(YHItems.CREAM.get())
					.build(pvd, YHFood.MUSHROOM_SOUP.item.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.HONEY_GLAZED_CUCUMBER.item.get(), 1, 200, 0.1f, Items.BOWL)
					.addIngredient(YHTagGen.CUCUMBER)
					.addIngredient(YHTagGen.CUCUMBER)
					.addIngredient(YHTagGen.CUCUMBER)
					.addIngredient(ForgeTags.RAW_PORK)//change to deer in the future
					.addIngredient(Items.HONEY_BOTTLE)
					.build(pvd, YHFood.HONEY_GLAZED_CUCUMBER.item.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.LIONS_HEAD.item.get(), 1, 200, 0.1f, Items.BOWL)
					.addIngredient(ForgeTags.RAW_PORK)
					.addIngredient(ForgeTags.RAW_PORK)
					.addIngredient(ForgeTags.VEGETABLES_CARROT)
					.addIngredient(ForgeTags.SALAD_INGREDIENTS_CABBAGE)
					.build(pvd, YHFood.LIONS_HEAD.item.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.MAPO_TOFU.item.get(), 1, 200, 0.1f, Items.BOWL)
					.addIngredient(YHFood.TOFU.item)
					.addIngredient(YHFood.TOFU.item)
					.addIngredient(YHFood.TOFU.item)
					.addIngredient(ForgeTags.RAW_PORK)
					.addIngredient(Items.BLAZE_POWDER)
					.build(pvd, YHFood.MAPO_TOFU.item.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.UDUMBARA_CAKE.item.get(), 1, 200, 0.1f, Items.BOWL)
					.addIngredient(ForgeTags.DOUGH_WHEAT)
					.addIngredient(YHCrops.UDUMBARA.getFruits())
					.build(pvd, YHFood.UDUMBARA_CAKE.item.getId());


			CookingPotRecipeBuilder.cookingPotRecipe(YHItems.SURP_CHEST.get(), 1, 200, 0.1f, Items.CHEST)
					.addIngredient(Items.RED_MUSHROOM)
					.addIngredient(Items.RED_MUSHROOM)
					.addIngredient(Items.HONEY_BOTTLE)
					.addIngredient(YHItems.CREAM.get())
					.addIngredient(YHCrops.UDUMBARA.getFruits())
					.addIngredient(Items.PURPLE_BANNER)
					.build(pvd, YHItems.SURP_CHEST.getId());
		}

		// food cooking saucer
		{
			CookingPotRecipeBuilder.cookingPotRecipe(YHDish.BAMBOO_MIZUYOKAN.block.get(), 1, 200, 0.1f, YHItems.SAUCER.get())
					.addIngredient(YHCrops.REDBEAN.getSeed())
					.addIngredient(new PotionIngredient(Potions.WATER))
					.addIngredient(Items.BAMBOO)
					.addIngredient(Items.SUGAR)
					.build(pvd, YHDish.BAMBOO_MIZUYOKAN.block.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHDish.DRIED_FISH.block.get(), 1, 200, 0.1f, YHItems.SAUCER.get())
					.addIngredient(ForgeTags.RAW_FISHES)
					.addIngredient(ForgeTags.RAW_FISHES)
					.addIngredient(ForgeTags.RAW_FISHES)
					.addIngredient(ForgeTags.SALAD_INGREDIENTS_CABBAGE)
					.build(pvd, YHDish.DRIED_FISH.block.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHDish.PASTITSIO.block.get(), 1, 200, 0.1f, YHItems.SAUCER.get())
					.addIngredient(ForgeTags.PASTA)
					.addIngredient(YHFood.BUTTER.item)
					.addIngredient(ModItems.TOMATO_SAUCE.get())
					.addIngredient(ForgeTags.RAW_BEEF)
					.addIngredient(ForgeTags.VEGETABLES_ONION)
					.build(pvd, YHDish.PASTITSIO.block.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHDish.SAUCE_GRILLED_FISH.block.get(), 1, 200, 0.1f, YHItems.SAUCER.get())
					.addIngredient(ForgeTags.RAW_FISHES)
					.addIngredient(YHItems.SOY_SAUCE_BOTTLE.item)
					.addIngredient(ForgeTags.VEGETABLES)
					.addIngredient(ForgeTags.VEGETABLES)
					.addIngredient(ForgeTags.VEGETABLES_ONION)
					.build(pvd, YHDish.SAUCE_GRILLED_FISH.block.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHDish.STINKY_TOFU.block.get(), 1, 200, 0.1f, YHItems.SAUCER.get())
					.addIngredient(YHFood.TOFU.item)
					.addIngredient(YHFood.TOFU.item)
					.addIngredient(Items.BROWN_MUSHROOM)
					.addIngredient(ForgeTags.SALAD_INGREDIENTS_CABBAGE)
					.build(pvd, YHDish.STINKY_TOFU.block.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHDish.TOFU_BURGER.block.get(), 1, 200, 0.1f, YHItems.SAUCER.get())
					.addIngredient(YHFood.TOFU.item)
					.addIngredient(YHFood.BUTTER.item)
					.addIngredient(Items.SWEET_BERRIES)
					.addIngredient(ForgeTags.SALAD_INGREDIENTS_CABBAGE)
					.build(pvd, YHDish.TOFU_BURGER.block.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHDish.SEVEN_COLORED_YOKAN.block.get(), 1, 200, 0.1f, YHItems.SAUCER.get())
					.addIngredient(YHCrops.REDBEAN.getSeed())
					.addIngredient(YHCrops.SOYBEAN.getSeed())
					.addIngredient(Items.CHERRY_LEAVES)
					.addIngredient(Items.CHORUS_FRUIT)
					.addIngredient(YHTagGen.MATCHA)
					.addIngredient(YHCrops.UDUMBARA.getFruits())
					.build(pvd, YHDish.SEVEN_COLORED_YOKAN.block.getId());

			unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, YHDish.IMITATION_BEAR_PAW.raw.get())::unlockedBy, YHItems.SAUCER.asItem())
					.requires(Items.BAMBOO)
					.requires(ForgeTags.RAW_PORK)
					.requires(ForgeTags.RAW_BEEF)
					.requires(ForgeTags.VEGETABLES_ONION)
					.requires(YHTagGen.RAW_EEL)
					.requires(YHItems.SOY_SAUCE_BOTTLE.item)
					.requires(YHItems.SAUCER.get())
					.save(pvd, YHDish.IMITATION_BEAR_PAW.raw.getId());

			steaming(pvd, DataIngredient.items(YHDish.IMITATION_BEAR_PAW.raw.asItem()), YHDish.IMITATION_BEAR_PAW.block::asItem);

			unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, YHDish.COLD_TOFU.block.get())::unlockedBy, YHItems.SAUCER.asItem())
					.requires(YHFood.TOFU.item)
					.requires(YHFood.TOFU.item)
					.requires(YHItems.SOY_SAUCE_BOTTLE.item)
					.requires(YHCrops.SOYBEAN.getSeed())
					.requires(ForgeTags.SALAD_INGREDIENTS_CABBAGE)
					.requires(YHTagGen.ICE)
					.requires(YHItems.SAUCER.get())
					.save(pvd, YHDish.COLD_TOFU.block.getId());

			unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, YHDish.SCHOLAR_GINKGO.raw.get())::unlockedBy, YHItems.SAUCER.asItem())
					.requires(Items.BIRCH_SAPLING)
					.requires(Items.HONEY_BOTTLE)
					.requires(YHCrops.SOYBEAN.getSeed())
					.requires(YHItems.SAUCER.get())
					.save(pvd, YHDish.SCHOLAR_GINKGO.raw.getId());

			steaming(pvd, DataIngredient.items(YHDish.SCHOLAR_GINKGO.raw.asItem()), YHDish.SCHOLAR_GINKGO.block::asItem);

			CookingPotRecipeBuilder.cookingPotRecipe(YHDish.CUMBERLAND_LOIN.block.get(), 1, 200, 0.1f, YHItems.SAUCER.get())
					.addIngredient(ForgeTags.RAW_PORK)
					.addIngredient(ForgeTags.RAW_PORK)
					.addIngredient(ModItems.TOMATO_SAUCE.get())
					.addIngredient(YHItems.SOY_SAUCE_BOTTLE.item)
					.addIngredient(ForgeTags.SALAD_INGREDIENTS_CABBAGE)
					.build(pvd, YHDish.CUMBERLAND_LOIN.block.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHDish.TOMATO_SAUCE_COD.block.get(), 1, 200, 0.1f, YHItems.SAUCER.get())
					.addIngredient(ForgeTags.RAW_FISHES_COD)
					.addIngredient(ForgeTags.RAW_FISHES_COD)
					.addIngredient(ModItems.TOMATO_SAUCE.get())
					.addIngredient(YHItems.SOY_SAUCE_BOTTLE.item)
					.build(pvd, YHDish.TOMATO_SAUCE_COD.block.getId());


		}

		var tea = tea(pvd);
		var coffee = coffee(pvd);

		// drinks
		{

			CookingPotRecipeBuilder.cookingPotRecipe(YHDrink.BLACK_TEA.item.get(), 1, 200, 0.1f, Items.GLASS_BOTTLE)
					.setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
					.unlockedByAnyIngredient(YHTea.BLACK.leaves)
					.addIngredient(YHTagGen.TEA_BLACK)
					.addIngredient(YHTagGen.TEA_BLACK)
					.build(tea, YHDrink.BLACK_TEA.item.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHDrink.GREEN_TEA.item.get(), 1, 200, 0.1f, Items.GLASS_BOTTLE)
					.setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
					.unlockedByAnyIngredient(YHTea.GREEN.leaves)
					.addIngredient(YHTagGen.TEA_GREEN)
					.addIngredient(YHTagGen.TEA_GREEN)
					.build(tea, YHDrink.GREEN_TEA.item.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHDrink.OOLONG_TEA.item.get(), 1, 200, 0.1f, Items.GLASS_BOTTLE)
					.setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
					.unlockedByAnyIngredient(YHTea.OOLONG.leaves)
					.addIngredient(YHTagGen.TEA_OOLONG)
					.addIngredient(YHTagGen.TEA_OOLONG)
					.build(tea, YHDrink.OOLONG_TEA.item.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHDrink.WHITE_TEA.item.get(), 1, 200, 0.1f, Items.GLASS_BOTTLE)
					.setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
					.unlockedByAnyIngredient(YHTea.WHITE.leaves)
					.addIngredient(YHTagGen.TEA_WHITE)
					.addIngredient(YHTagGen.TEA_WHITE)
					.build(tea, YHDrink.WHITE_TEA.item.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHDrink.CORNFLOWER_TEA.item.get(), 1, 200, 0.1f, Items.GLASS_BOTTLE)
					.setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
					.unlockedByAnyIngredient(Items.CORNFLOWER)
					.addIngredient(Items.CORNFLOWER)
					.addIngredient(Items.CORNFLOWER)
					.build(tea, YHDrink.CORNFLOWER_TEA.item.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHDrink.TEA_MOCHA.item.get(), 1, 200, 0.1f, Items.GLASS_BOTTLE)
					.setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
					.unlockedByAnyIngredient(YHTea.BLACK.leaves)
					.addIngredient(YHTagGen.TEA_BLACK)
					.addIngredient(Items.COCOA_BEANS)
					.addIngredient(ForgeTags.MILK_BOTTLE)
					.build(tea, YHDrink.TEA_MOCHA.item.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHDrink.SAIDI_TEA.item.get(), 1, 200, 0.1f, Items.GLASS_BOTTLE)
					.setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
					.unlockedByAnyIngredient(YHTea.BLACK.leaves)
					.addIngredient(YHTagGen.TEA_BLACK)
					.addIngredient(Items.SUGAR)
					.addIngredient(Items.SUGAR)
					.build(tea, YHDrink.SAIDI_TEA.item.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHDrink.SAKURA_HONEY_TEA.item.get(), 1, 200, 0.1f, Items.GLASS_BOTTLE)
					.setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
					.unlockedByAnyIngredient(Items.CHERRY_LEAVES)
					.addIngredient(Items.CHERRY_LEAVES)
					.addIngredient(Items.HONEY_BOTTLE)
					.build(tea, YHDrink.SAKURA_HONEY_TEA.item.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHDrink.GENMAI_TEA.item.get(), 1, 200, 0.1f, Items.GLASS_BOTTLE)
					.setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
					.unlockedByAnyIngredient(YHTea.GREEN.leaves)
					.addIngredient(YHTagGen.TEA_GREEN)
					.addIngredient(YHTagGen.TEA_GREEN)
					.addIngredient(ForgeTags.GRAIN_RICE)
					.build(tea, YHDrink.GENMAI_TEA.item.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHDrink.GREEN_WATER.item.get(), 1, 200, 0.1f, Items.GLASS_BOTTLE)
					.setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
					.unlockedByAnyIngredient(Items.GLASS_BOTTLE)
					.addIngredient(ForgeTags.SALAD_INGREDIENTS_CABBAGE)
					.addIngredient(ForgeTags.SALAD_INGREDIENTS_CABBAGE)
					.build(tea, YHDrink.GREEN_WATER.item.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHCoffee.ESPRESSO.item.get(), 1, 200, 0.1f, Items.GLASS_BOTTLE)
					.setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
					.unlockedByAnyIngredient(YHItems.COFFEE_POWDER)
					.addIngredient(YHItems.COFFEE_POWDER)
					.addIngredient(new PotionIngredient(Potions.WATER))
					.build(coffee, YHCoffee.ESPRESSO.item.getId());

			coffee(coffee, YHCoffee.RISTRETTO, e -> e.addIngredient(YHItems.COFFEE_POWDER));
			coffee(coffee, YHCoffee.AMERICANO, e -> e.addIngredient(new PotionIngredient(Potions.WATER)));
			coffee(coffee, YHCoffee.LATTE, e -> e.addIngredient(ForgeTags.MILK_BOTTLE));
			coffee(coffee, YHCoffee.MOCHA, e -> e
					.addIngredient(ForgeTags.MILK_BOTTLE)
					.addIngredient(Items.COCOA_BEANS));
			coffee(coffee, YHCoffee.CAPPUCCINO, e -> e
					.addIngredient(ForgeTags.MILK_BOTTLE)
					.addIngredient(YHItems.CREAM));
			coffee(coffee, YHCoffee.MACCHIATO, e -> e.addIngredient(YHItems.CREAM));
			coffee(coffee, YHCoffee.CON_PANNA, e -> e
					.addIngredient(YHItems.COFFEE_POWDER)
					.addIngredient(YHItems.CREAM));
			coffee(coffee, YHCoffee.AFFOGATO, e -> e
					.addIngredient(YHTagGen.ICE)
					.addIngredient(YHItems.CREAM));

			unlock(pvd, ShapelessPatchouliBuilder.shapeless(RecipeCategory.FOOD, YHCoffee.AFFOGATO.item.get(), 1)::unlockedBy,
					YHCoffee.ESPRESSO.item.get()).requires(YHCoffee.ESPRESSO.item)
					.requires(YHTagGen.ICE).requires(YHItems.CREAM)
					.save(coffee, YHCoffee.AFFOGATO.item.getId().withSuffix("_craft"));

		}

		// wine
		{

			unlock(pvd, new SimpleBasinBuilder(YHDrink.BLACK_GRAPE_JUICE.fluid.getSource(), 50)::unlockedBy,
					YHCrops.BLACK_GRAPE.getFruits())
					.setInput(YHCrops.BLACK_GRAPE.getFruits())
					.save(pvd, YHDrink.BLACK_GRAPE_JUICE.item.getId());

			unlock(pvd, new SimpleBasinBuilder(YHDrink.RED_GRAPE_JUICE.fluid.getSource(), 50)::unlockedBy,
					YHCrops.RED_GRAPE.getFruits())
					.setInput(YHCrops.RED_GRAPE.getFruits())
					.save(pvd, YHDrink.RED_GRAPE_JUICE.item.getId());

			unlock(pvd, new SimpleBasinBuilder(YHDrink.WHITE_GRAPE_JUICE.fluid.getSource(), 50)::unlockedBy,
					YHCrops.WHITE_GRAPE.getFruits())
					.setInput(YHCrops.WHITE_GRAPE.getFruits())
					.save(pvd, YHDrink.WHITE_GRAPE_JUICE.item.getId());

			unlock(pvd, new SimpleFermentationBuilder(
							YHDrink.WHITE_GRAPE_JUICE.fluid.getSource(),
							YHDrink.WHITE_WINE.fluid.getSource(), 1800)::unlockedBy,
					YHCrops.WHITE_GRAPE.getFruits())
					.save(pvd, YHDrink.WHITE_WINE.item.getId());

			unlock(pvd, new SimpleFermentationBuilder(
							YHDrink.RED_GRAPE_JUICE.fluid.getSource(),
							YHDrink.RED_WINE.fluid.getSource(), 1800)::unlockedBy,
					YHCrops.RED_GRAPE.getFruits())
					.save(pvd, YHDrink.RED_WINE.item.getId());


			unlock(pvd, new SimpleFermentationBuilder(
							YHDrink.RED_GRAPE_JUICE.fluid.getSource(),
							YHDrink.VAN_ALLEN.fluid.getSource(), 2400)::unlockedBy,
					YHCrops.RED_GRAPE.getFruits())
					.addInput(Items.SWEET_BERRIES)
					.save(pvd, YHDrink.VAN_ALLEN.item.getId());

			unlock(pvd, new SimpleFermentationBuilder(
							YHDrink.BLACK_GRAPE_JUICE.fluid.getSource(),
							YHDrink.BURGUNDY.fluid.getSource(), 2400)::unlockedBy,
					YHCrops.BLACK_GRAPE.getFruits())
					.addInput(Items.SWEET_BERRIES)
					.save(pvd, YHDrink.BURGUNDY.item.getId());

			unlock(pvd, new SimpleFermentationBuilder(
							YHDrink.WHITE_GRAPE_JUICE.fluid.getSource(),
							YHDrink.CHAMPAGNE.fluid.getSource(), 2400)::unlockedBy,
					YHCrops.WHITE_GRAPE.getFruits())
					.addInput(Items.SWEET_BERRIES)
					.addInput(Items.SUGAR)
					.save(pvd, YHDrink.CHAMPAGNE.item.getId());
		}

		// sake
		{

			unlock(pvd, new SimpleFermentationBuilder(Fluids.WATER, YHItems.SOY_SAUCE_BOTTLE.fluid.getSource(), 1800)::unlockedBy, YHCrops.SOYBEAN.getSeed())
					.addInput(YHCrops.SOYBEAN.getSeed(), 4)
					.save(pvd, YHItems.SOY_SAUCE_BOTTLE.item.getId());

			unlock(pvd, new SimpleFermentationBuilder(Fluids.EMPTY, 1800)::unlockedBy, YHCrops.SOYBEAN.getSeed())
					.addInput(YHCrops.SOYBEAN.getSeed(), 6)
					.addInput(Items.BROWN_MUSHROOM)
					.addOutput(YHFood.NATTOU.item, 6)
					.save(pvd, YHFood.NATTOU.item.getId());

			unlock(pvd, new SimpleFermentationBuilder(Fluids.WATER, YHDrink.MIO.fluid.getSource(), 2400)::unlockedBy, ModItems.RICE.get())
					.addInput(ForgeTags.GRAIN_RICE, 4)
					.save(pvd, YHDrink.MIO.item.getId());

			unlock(pvd, new SimpleFermentationBuilder(Fluids.WATER, YHDrink.MEAD.fluid.getSource(), 2400)::unlockedBy, ModItems.RICE.get())
					.addInput(ForgeTags.GRAIN_RICE, 4)
					.addInput(Items.HONEY_BOTTLE)
					.save(pvd, YHDrink.MEAD.item.getId());

			unlock(pvd, new SimpleFermentationBuilder(Fluids.WATER, YHDrink.KIKU.fluid.getSource(), 2400)::unlockedBy, ModItems.RICE.get())
					.addInput(ForgeTags.GRAIN_RICE, 3)
					.addInput(Items.BROWN_MUSHROOM)
					.save(pvd, YHDrink.KIKU.item.getId());

			unlock(pvd, new SimpleFermentationBuilder(Fluids.WATER, YHDrink.HAKUTSURU.fluid.getSource(), 2400)::unlockedBy, ModItems.RICE.get())
					.addInput(ForgeTags.GRAIN_RICE, 3)
					.addInput(Items.BROWN_MUSHROOM).addInput(ForgeTags.EGGS)
					.save(pvd, YHDrink.HAKUTSURU.item.getId());

			unlock(pvd, new SimpleFermentationBuilder(Fluids.WATER, YHDrink.KAPPA_VILLAGE.fluid.getSource(), 2400)::unlockedBy, ModItems.RICE.get())
					.addInput(ForgeTags.GRAIN_RICE, 3)
					.addInput(Items.BROWN_MUSHROOM).addInput(Items.SEAGRASS)
					.save(pvd, YHDrink.KAPPA_VILLAGE.item.getId());

			unlock(pvd, new SimpleFermentationBuilder(Fluids.WATER, YHDrink.SUIGEI.fluid.getSource(), 2400)::unlockedBy, ModItems.RICE.get())
					.addInput(ForgeTags.GRAIN_RICE, 3)
					.addInput(Items.SEA_PICKLE).addInput(Items.KELP).addInput(Items.PUFFERFISH)
					.save(pvd, YHDrink.SUIGEI.item.getId());

			unlock(pvd, new SimpleFermentationBuilder(Fluids.WATER, YHDrink.DAIGINJO.fluid.getSource(), 2400)::unlockedBy, ModItems.RICE.get())
					.addInput(ForgeTags.GRAIN_RICE, 3)
					.addInput(Items.NETHER_WART).addInput(Items.BLAZE_POWDER)
					.save(pvd, YHDrink.DAIGINJO.item.getId());

			unlock(pvd, new SimpleFermentationBuilder(Fluids.WATER, YHDrink.DASSAI.fluid.getSource(), 2400)::unlockedBy, ModItems.RICE.get())
					.addInput(ForgeTags.GRAIN_RICE, 3)
					.addInput(Items.NETHER_WART).addInput(Items.NAUTILUS_SHELL)
					.save(pvd, YHDrink.DASSAI.item.getId());

			unlock(pvd, new SimpleFermentationBuilder(Fluids.WATER, YHDrink.TENGU_TANGO.fluid.getSource(), 2400)::unlockedBy, ModItems.RICE.get())
					.addInput(ForgeTags.GRAIN_RICE, 3)
					.addInput(Items.NETHER_WART).addInput(Items.PHANTOM_MEMBRANE)
					.save(pvd, YHDrink.TENGU_TANGO.item.getId());

			unlock(pvd, new SimpleFermentationBuilder(Fluids.WATER, YHDrink.SPARROW_SAKE.fluid.getSource(), 2400)::unlockedBy, ModItems.RICE.get())
					.addInput(ForgeTags.GRAIN_RICE, 3)
					.addInput(Items.FEATHER).addInput(Items.RABBIT_FOOT)
					.save(pvd, YHDrink.SPARROW_SAKE.item.getId());

			unlock(pvd, new SimpleFermentationBuilder(Fluids.WATER, YHDrink.FULL_MOONS_EVE.fluid.getSource(), 2400)::unlockedBy, ModItems.RICE.get())
					.addInput(ForgeTags.GRAIN_RICE, 3)
					.addInput(Items.NETHER_WART).addInput(YHCrops.UDUMBARA.getFruits())
					.save(pvd, YHDrink.FULL_MOONS_EVE.item.getId());

		}

		// steam
		{
			steaming(pvd, DataIngredient.items(Items.POTATO), () -> Items.BAKED_POTATO);
			steaming(pvd, DataIngredient.items(ModItems.CHICKEN_CUTS.get()), ModItems.COOKED_CHICKEN_CUTS);
			steaming(pvd, DataIngredient.items(ModItems.SALMON_SLICE.get()), ModItems.COOKED_SALMON_SLICE);
			steaming(pvd, DataIngredient.items(ModItems.COD_SLICE.get()), ModItems.COOKED_COD_SLICE);


			unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, YHFood.BUN.raw.get(), 4)::unlockedBy, Items.WHEAT)
					.requires(ForgeTags.DOUGH)
					.requires(ForgeTags.DOUGH)
					.requires(ModTags.CABBAGE_ROLL_INGREDIENTS)
					.requires(ForgeTags.SALAD_INGREDIENTS_CABBAGE)
					.requires(ForgeTags.VEGETABLES_ONION)
					.requires(YHCrops.SOYBEAN.getSeed())
					.save(pvd);

			steaming(pvd, DataIngredient.items(YHFood.BUN.raw.get()), YHFood.BUN.item);
			steaming(pvd, DataIngredient.tag(ForgeTags.DOUGH_WHEAT), YHFood.MANTOU.item);

			unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, YHFood.OYAKI.raw.get(), 4)::unlockedBy, Items.WHEAT)
					.requires(ForgeTags.DOUGH)
					.requires(ForgeTags.DOUGH)
					.requires(ForgeTags.VEGETABLES)
					.requires(Items.BROWN_MUSHROOM)
					.save(pvd);

			steaming(pvd, DataIngredient.items(YHFood.OYAKI.raw.get()), YHFood.OYAKI.item);
		}

		// cuisine
		{

			for (var e : YHRolls.values()) {
				var slice = e.sliceStack();
				if (slice.isEmpty()) continue;
				cutting(pvd, e.item, e.slice, slice.getCount());
			}


			{

				unlock(pvd, new OrderedRecipeBuilder(TableItemManager.BASE_SUSHI)::unlockedBy, ModItems.SALMON_SLICE.get())
						.result(ModItems.SALMON_ROLL.get(), 2)
						.add(ForgeTags.RAW_FISHES_SALMON)
						.save(pvd);

				unlock(pvd, new OrderedRecipeBuilder(TableItemManager.BASE_SUSHI)::unlockedBy, ModItems.COD_SLICE.get())
						.result(ModItems.COD_ROLL.get(), 2)
						.add(ForgeTags.RAW_FISHES_COD)
						.save(pvd);

				unlock(pvd, new OrderedRecipeBuilder(TableItemManager.BASE_SUSHI)::unlockedBy, YHFood.RAW_TUNA_SLICE.item.get())
						.result(YHSushi.TUNA_NIGIRI.item.get(), 2)
						.add(YHTagGen.RAW_TUNA)
						.save(pvd);

				unlock(pvd, new OrderedRecipeBuilder(TableItemManager.BASE_SUSHI)::unlockedBy, YHFood.OTORO.item.get())
						.result(YHSushi.OTORO_NIGIRI.item.get(), 2)
						.add(YHFood.OTORO.item)
						.save(pvd);

				unlock(pvd, new OrderedRecipeBuilder(TableItemManager.BASE_SUSHI)::unlockedBy, YHFood.ROASTED_LAMPREY_FILLET.item.get())
						.result(YHSushi.LORELEI_NIGIRI.item.get(), 2)
						.add(YHFood.KABAYAKI.item.get())
						.add(Items.DRIED_KELP)
						.save(pvd);

				unlock(pvd, new OrderedRecipeBuilder(TableItemManager.BASE_SUSHI)::unlockedBy, YHFood.TAMAGOYAKI.item.get())
						.result(YHSushi.EGG_NIGIRI.item.get(), 2)
						.add(YHFood.TAMAGOYAKI.item.get())
						.add(Items.DRIED_KELP)
						.save(pvd);

			}

			{

				unlock(pvd, new OrderedRecipeBuilder(TableItemManager.BASE_GUNKAN)::unlockedBy, YHFood.ROE.item.get())
						.result(YHSushi.TOBIKO_GUNKAN, 2)
						.add(YHFood.ROE.item.get())
						.save(pvd);

				unlock(pvd, new OrderedRecipeBuilder(TableItemManager.BASE_GUNKAN)::unlockedBy, YHFood.NATTOU.item.get())
						.result(YHSushi.NATTOU_GUNKAN, 2)
						.add(YHFood.NATTOU.item.get())
						.save(pvd);

				unlock(pvd, new OrderedRecipeBuilder(TableItemManager.BASE_GUNKAN)::unlockedBy, Items.SEAGRASS)
						.result(YHSushi.SEAGRASS_GUNKAN, 2)
						.add(Items.SEAGRASS)
						.save(pvd);
			}

			{
				unlock(pvd, new OrderedRecipeBuilder(TableItemManager.BASE_HOSOMAKI)::unlockedBy, ModItems.COOKED_RICE.get())
						.result(ModItems.KELP_ROLL.get())
						.add(Items.CARROT)
						.save(pvd);

				unlock(pvd, new OrderedRecipeBuilder(TableItemManager.BASE_HOSOMAKI)::unlockedBy, YHFood.RAW_TUNA_SLICE.item.get())
						.result(YHRolls.TEKKA_MAKI)
						.add(YHItems.SOY_SAUCE_BOTTLE.item.get())
						.add(YHTagGen.RAW_TUNA)
						.save(pvd);

				unlock(pvd, new OrderedRecipeBuilder(TableItemManager.BASE_HOSOMAKI)::unlockedBy, ModItems.COOKED_RICE.get())
						.result(YHRolls.SHINNKO_MAKI)
						.add(YHItems.SOY_SAUCE_BOTTLE.item.get())
						.add(Items.BEETROOT)
						.save(pvd);

				unlock(pvd, new OrderedRecipeBuilder(TableItemManager.BASE_HOSOMAKI)::unlockedBy, ModItems.COOKED_RICE.get())
						.result(YHRolls.KAPPA_MAKI)
						.add(YHItems.SOY_SAUCE_BOTTLE.item.get())
						.add(YHTagGen.CUCUMBER_SLICE)
						.save(pvd);
			}

			{
				unlock(pvd, new MixedRecipeBuilder(TableItemManager.BASE_FUTOMAKI)::unlockedBy, YHFood.TAMAGOYAKI_SLICE.item.get())
						.result(YHRolls.EGG_FUTOMAKI)
						.addOrdered(YHItems.SOY_SAUCE_BOTTLE.item.get())
						.addUnordered(YHFood.TAMAGOYAKI_SLICE.item.get())
						.addUnordered(YHFood.TAMAGOYAKI_SLICE.item.get())
						.addUnordered(YHFood.TAMAGOYAKI_SLICE.item.get())
						.save(pvd);

				unlock(pvd, new MixedRecipeBuilder(TableItemManager.BASE_FUTOMAKI)::unlockedBy, ModItems.SALMON_SLICE.get())
						.result(YHRolls.SALMON_FUTOMAKI)
						.addOrdered(YHItems.SOY_SAUCE_BOTTLE.item.get())
						.addUnordered(YHTagGen.CUCUMBER_SLICE)
						.addUnordered(Ingredient.of(Items.CARROT, Items.BEETROOT))
						.addUnordered(YHFood.IMITATION_CRAB.item.asItem())
						.addUnordered(ForgeTags.RAW_FISHES_SALMON)
						.save(pvd);

				unlock(pvd, new MixedRecipeBuilder(TableItemManager.BASE_FUTOMAKI)::unlockedBy, ModItems.SALMON_SLICE.get())
						.result(YHRolls.RAINBOW_FUTOMAKI)
						.addOrdered(YHItems.SOY_SAUCE_BOTTLE.item.get())
						.addUnordered(YHTagGen.CUCUMBER_SLICE)
						.addUnordered(Ingredient.of(Items.CARROT, Items.BEETROOT))
						.addUnordered(YHFood.TAMAGOYAKI_SLICE.item.get())
						.addUnordered(YHFood.IMITATION_CRAB.item.asItem())
						.addUnordered(ForgeTags.RAW_FISHES_SALMON)
						.save(pvd);
			}

			{

				unlock(pvd, new MixedRecipeBuilder(TableItemManager.BASE_CAL)::unlockedBy, YHFood.IMITATION_CRAB.item.get())
						.result(YHRolls.CALIFORNIA_ROLL)
						.addOrdered(YHItems.MAYONNAISE.item)
						.addUnordered(YHTagGen.CUCUMBER_SLICE)
						.addUnordered(YHFood.TAMAGOYAKI_SLICE.item)
						.addUnordered(YHFood.IMITATION_CRAB.item)
						.save(pvd);

				unlock(pvd, new OrderedRecipeBuilder(YHRolls.CALIFORNIA_ROLL.item.get())::unlockedBy, YHFood.ROE.item.get())
						.result(YHRolls.ROE_CALIFORNIA_ROLL)
						.add(YHFood.ROE.item)
						.save(pvd);

				unlock(pvd, new MixedRecipeBuilder(YHRolls.CALIFORNIA_ROLL.item.get())::unlockedBy, ModItems.SALMON_SLICE.get())
						.result(YHRolls.SALMON_LOVER_ROLL)
						.addOrdered(YHFood.ROE.item)
						.addUnordered(ForgeTags.RAW_FISHES_SALMON)
						.addUnordered(ForgeTags.RAW_FISHES_SALMON)
						.addUnordered(ForgeTags.RAW_FISHES_SALMON)
						.save(pvd);

				unlock(pvd, new MixedRecipeBuilder(YHRolls.CALIFORNIA_ROLL.item.get())::unlockedBy, YHFood.RAW_TUNA_SLICE.item.get())
						.result(YHRolls.VOLCANO_ROLL)
						.addOrdered(YHItems.SOY_SAUCE_BOTTLE.item.get())
						.addUnordered(YHTagGen.RAW_TUNA)
						.addUnordered(YHFood.OTORO.item)
						.addUnordered(YHTagGen.RAW_TUNA)
						.save(pvd);

				unlock(pvd, new MixedRecipeBuilder(YHRolls.CALIFORNIA_ROLL.item.get())::unlockedBy, YHFood.RAW_TUNA_SLICE.item.get())
						.result(YHRolls.RAINBOW_ROLL)
						.addOrdered(YHFood.ROE.item)
						.addUnordered(ForgeTags.RAW_FISHES_SALMON)
						.addUnordered(ForgeTags.RAW_FISHES_COD)
						.addUnordered(YHTagGen.RAW_TUNA)
						.save(pvd);

			}
		}

		if (ModList.get().isLoaded(FruitsDelight.MODID)) {
			Consumer<FinishedRecipe> modtea =
					e -> pvd.accept(new ConditionalRecipeWrapper(
							new BasePotFinishedRecipe(YHBlocks.KETTLE_RS.get(), e),
							new ModLoadedCondition(FruitsDelight.MODID)));

			CookingPotRecipeBuilder.cookingPotRecipe(FruitsDelightCompatFood.MOON_ROCKET.item.get(), 1, 200, 0.1f, Items.GLASS_BOTTLE)
					.setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
					.unlockedByAnyIngredient(FDFood.LEMON_SLICE.get())
					.addIngredient(FDFood.LEMON_SLICE.get())
					.addIngredient(Items.SUGAR)
					.build(modtea, FruitsDelightCompatFood.MOON_ROCKET.item.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(FruitsDelightCompatFood.LEMON_BLACK_TEA.item.get(), 1, 200, 0.1f, Items.GLASS_BOTTLE)
					.setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
					.unlockedByAnyIngredient(FDFood.LEMON_SLICE.get())
					.addIngredient(FDFood.LEMON_SLICE.get())
					.addIngredient(YHTagGen.TEA_BLACK)
					.addIngredient(Items.SUGAR)
					.build(modtea, FruitsDelightCompatFood.LEMON_BLACK_TEA.item.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(FruitsDelightCompatFood.PEACH_TAPIOCA.item.get(), 1, 200, 0.1f, Items.BOWL)
					.addIngredient(FruitType.PEACH.getFruitTag())
					.addIngredient(Items.LILY_PAD)
					.build(ConditionalRecipeWrapper.mod(pvd, FruitsDelight.MODID), FruitsDelightCompatFood.PEACH_TAPIOCA.item.getId());


			unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, FruitsDelightCompatFood.PEACH_YATSUHASHI.item, 2)::unlockedBy, FruitType.PEACH.getFruit())
					.requires(FruitType.PEACH.getFruitTag())
					.requires(ModItems.COOKED_RICE.get())
					.save(ConditionalRecipeWrapper.mod(pvd, FruitsDelight.MODID));

		}

		if (ModList.get().isLoaded(Create.ID)) {
			CreateRecipeGen.onRecipeGen(pvd);
		}

	}

	private static void food(RegistrateRecipeProvider pvd, YHFood raw, YHFood cooked) {
		pvd.food(DataIngredient.items(raw.item.get()), RecipeCategory.FOOD, cooked.item, 0.1f);
	}

	private static void cutting(RegistrateRecipeProvider pvd, ItemEntry<?> in, ItemEntry<?> out, int count) {
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(in),
						Ingredient.of(ForgeTags.TOOLS_KNIVES), out, count, 1)
				.build(pvd, in.getId().withSuffix("_cutting"));
	}

	private static void coffee(Consumer<FinishedRecipe> cons, YHCoffee coffee, UnaryOperator<CookingPotRecipeBuilder> func) {
		func.apply(CookingPotRecipeBuilder.cookingPotRecipe(coffee.item.get(), 1, 60, 0.1f, Items.GLASS_BOTTLE)
						.setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
						.unlockedByAnyIngredient(YHCoffee.ESPRESSO.item)
						.addIngredient(YHCoffee.ESPRESSO.item))
				.build(cons, coffee.item.getId().withSuffix("_remix"));
	}

	private static void cake(RegistrateRecipeProvider pvd, CakeEntry cake) {
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(cake.block.get()),
						Ingredient.of(ForgeTags.TOOLS_KNIVES), cake.item.get(), cake.isCake ? 7 : 4)
				.build(pvd, cake.item.getId());
		if (cake.isCake) {
			unlock(pvd, new ShapelessRecipeBuilder(RecipeCategory.FOOD, cake.block.get(), 1)::unlockedBy, cake.item.get())
					.requires(cake.item.get(), 7)
					.save(pvd, cake.block.getId().withSuffix("_assemble"));
		} else {
			unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.FOOD, cake.block.get(), 1)::unlockedBy, cake.item.get())
					.pattern("AA").pattern("AA")
					.define('A', cake.item.get())
					.save(pvd, cake.block.getId().withSuffix("_assemble"));
		}
	}

	private static void drying(RegistrateRecipeProvider pvd, DataIngredient in, Supplier<Item> out) {
		drying(pvd, in, out, 200);
	}

	private static void drying(RegistrateRecipeProvider pvd, DataIngredient in, Supplier<Item> out, int time) {
		cooking(pvd, in, RecipeCategory.MISC, out, 0, time, "drying", YHBlocks.RACK_RS.get());
	}

	private static void steaming(RegistrateRecipeProvider pvd, DataIngredient in, Supplier<Item> out) {
		cooking(pvd, in, RecipeCategory.MISC, out, 0, 200, "steaming", YHBlocks.STEAM_RS.get());
	}

	public static <T extends ItemLike> void cooking(RegistrateRecipeProvider pvd, DataIngredient source, RecipeCategory category, Supplier<? extends T> result, float experience, int cookingTime, String typeName, RecipeSerializer<? extends AbstractCookingRecipe> serializer) {
		new SimpleCookingRecipeBuilder(category, CookingBookCategory.MISC, result.get(), source, experience, cookingTime, serializer)
				.unlockedBy("has_" + pvd.safeName(source), source.getCritereon(pvd))
				.save(pvd, pvd.safeId(result.get()) + "_from_" + pvd.safeName(source) + "_" + typeName);
	}

	private static Consumer<FinishedRecipe> tea(RegistrateRecipeProvider pvd) {
		return e -> pvd.accept(new BasePotFinishedRecipe(YHBlocks.KETTLE_RS.get(), e));
	}

	private static Consumer<FinishedRecipe> coffee(RegistrateRecipeProvider pvd) {
		return e -> pvd.accept(new BasePotFinishedRecipe(YHBlocks.MOKA_RS.get(), e));
	}

	private static void foodCut(RegistrateRecipeProvider pvd,
								YHFood raw, YHFood cooked,
								YHFood raw_cut, YHFood cooked_cut, int count) {
		food(pvd, raw, cooked);
		food(pvd, raw_cut, cooked_cut);
		cutting(pvd, raw.item, raw_cut.item, count);
	}

	public static <T> T unlock(RegistrateRecipeProvider pvd, BiFunction<String, InventoryChangeTrigger.TriggerInstance, T> func, Item item) {
		return func.apply("has_" + pvd.safeName(item), DataIngredient.items(item).getCritereon(pvd));
	}

}
