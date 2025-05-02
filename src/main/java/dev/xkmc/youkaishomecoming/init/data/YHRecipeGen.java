package dev.xkmc.youkaishomecoming.init.data;

import com.simibubi.create.Create;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.fruitsdelight.init.FruitsDelight;
import dev.xkmc.fruitsdelight.init.food.FDFood;
import dev.xkmc.fruitsdelight.init.food.FruitType;
import dev.xkmc.l2core.serial.ingredients.PotionIngredient;
import dev.xkmc.l2core.serial.recipe.ConditionalRecipeWrapper;
import dev.xkmc.youkaishomecoming.compat.create.CreateRecipeGen;
import dev.xkmc.youkaishomecoming.compat.food.FruitsDelightCompatFood;
import dev.xkmc.youkaishomecoming.content.pot.base.BasePotOutput;
import dev.xkmc.youkaishomecoming.content.pot.ferment.SimpleFermentationBuilder;
import dev.xkmc.youkaishomecoming.content.pot.rack.DryingRackRecipe;
import dev.xkmc.youkaishomecoming.content.pot.steamer.SteamingRecipe;
import dev.xkmc.youkaishomecoming.init.food.*;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import vectorwing.farmersdelight.client.recipebook.CookingPotRecipeBookTab;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.tag.CommonTags;
import vectorwing.farmersdelight.common.tag.ModTags;
import vectorwing.farmersdelight.data.builder.CookingPotRecipeBuilder;
import vectorwing.farmersdelight.data.builder.CuttingBoardRecipeBuilder;

import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class YHRecipeGen {

	public static void genRecipes(RegistrateRecipeProvider pvd) {
		{
			foodCut(pvd, YHFood.RAW_LAMPREY, YHFood.ROASTED_LAMPREY, YHFood.RAW_LAMPREY_FILLET, YHFood.ROASTED_LAMPREY_FILLET);

			food(pvd, YHFood.TOFU, YHFood.OILY_BEAN_CURD);
			pvd.stonecutting(DataIngredient.items(Items.CLAY_BALL), RecipeCategory.MISC, YHItems.CLAY_SAUCER);
			pvd.stonecutting(DataIngredient.items(Items.BAMBOO_BLOCK), RecipeCategory.MISC, YHBlocks.RACK);
			pvd.stonecutting(DataIngredient.items(Items.BAMBOO_BLOCK), RecipeCategory.MISC, YHBlocks.STEAMER_RACK);
			pvd.stonecutting(DataIngredient.tag(ItemTags.PLANKS), RecipeCategory.MISC, YHBlocks.STEAMER_LID);
			pvd.smelting(DataIngredient.items(YHItems.CLAY_SAUCER.get()), RecipeCategory.MISC, YHItems.SAUCER, 0.1f, 200);
			pvd.stonecutting(DataIngredient.items(Items.GLASS), RecipeCategory.MISC, YHItems.SAKE_BOTTLE);


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
					.define('W', PotionIngredient.of(Potions.WATER))
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
			cutting(pvd, YHCrops.SOYBEAN.fruits, YHCrops.SOYBEAN.seed, 1);
			cutting(pvd, YHCrops.COFFEA.fruits, YHCrops.COFFEA.seed, 1);
			pvd.smelting(DataIngredient.items(YHCrops.COFFEA.getSeed()), RecipeCategory.MISC, YHItems.COFFEE_BEAN, 0.1f, 200);
			pvd.smoking(DataIngredient.items(YHCrops.COFFEA.getSeed()), RecipeCategory.MISC, YHItems.COFFEE_BEAN, 0.1f, 200);
			pvd.storage(YHCrops.SOYBEAN::getSeed, RecipeCategory.MISC, YHItems.SOYBEAN_BAG);
			pvd.storage(YHCrops.REDBEAN::getSeed, RecipeCategory.MISC, YHItems.REDBEAN_BAG);
			pvd.storage(YHItems.COFFEE_BEAN, RecipeCategory.MISC, YHItems.COFFEE_BEAN_BAG);
			pvd.storage(YHCrops.TEA::getFruits, RecipeCategory.MISC, YHItems.TEA_BAG);
			pvd.storage(YHTea.BLACK.leaves, RecipeCategory.MISC, YHItems.BLACK_TEA_BAG);
			pvd.storage(YHTea.GREEN.leaves, RecipeCategory.MISC, YHItems.GREEN_TEA_BAG);
			pvd.storage(YHTea.OOLONG.leaves, RecipeCategory.MISC, YHItems.OOLONG_TEA_BAG);
			pvd.storage(YHTea.WHITE.leaves, RecipeCategory.MISC, YHItems.WHITE_TEA_BAG);

			drying(pvd, DataIngredient.items(Items.WHEAT), ModItems.STRAW);
			drying(pvd, DataIngredient.items(YHCrops.TEA.getFruits()), YHTea.GREEN.leaves);
			pvd.smoking(DataIngredient.items(YHTea.GREEN.leaves.get()), RecipeCategory.MISC, YHTea.BLACK.leaves, 0.1f, 200);
			pvd.campfire(DataIngredient.items(YHTea.GREEN.leaves.get()), RecipeCategory.MISC, YHTea.OOLONG.leaves, 0.1f, 200);

			CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.SALMON_BUCKET),
							Ingredient.of(CommonTags.TOOLS_KNIFE), Items.WATER_BUCKET, 1)
					.addResult(ModItems.SALMON_SLICE.get(), 2)
					.addResult(Items.BONE_MEAL)
					.addResultWithChance(YHFood.ROE.item.get(), 0.5f, 1)
					.build(pvd);

			CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(YHItems.COFFEE_BEAN),
							Ingredient.of(ItemTags.SHOVELS), YHItems.COFFEE_POWDER, 1)
					.build(pvd);

			CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(YHTea.GREEN.leaves),
							Ingredient.of(ItemTags.SHOVELS), YHItems.MATCHA, 1)
					.build(pvd);

			CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.ICE),
							Ingredient.of(ItemTags.PICKAXES), YHItems.ICE_CUBE, 8)
					.build(pvd);

			drying(pvd, DataIngredient.items(YHTea.GREEN.leaves.get()), YHTea.WHITE.leaves);
		}

		// food craft
		{
			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, YHFood.MILK_POPSICLE.item, 1)::unlockedBy, YHItems.ICE_CUBE.get())
					.pattern(" MM").pattern("SIM").pattern("TS ")
					.define('M', CommonTags.FOODS_MILK)
					.define('S', Items.SUGAR)
					.define('I', YHItems.ICE_CUBE)
					.define('T', Items.STICK)
					.save(pvd);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, YHFood.BIG_POPSICLE.item, 1)::unlockedBy, YHItems.ICE_CUBE.get())
					.pattern(" II").pattern("SII").pattern("TS ")
					.define('S', Items.SUGAR)
					.define('I', YHItems.ICE_CUBE)
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
					.requires(CommonTags.CROPS_RICE).requires(YHItems.ICE_CUBE).requires(YHCrops.REDBEAN.getSeed())
					.requires(ModItems.COD_ROLL.get())
					.save(pvd);


			unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, YHFood.TOBIKO_GUNKAN.item, 2)::unlockedBy, YHFood.ROE.item.get())
					.requires(YHFood.ROE.item).requires(ModItems.COOKED_RICE.get()).requires(Items.DRIED_KELP)
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
		}

		// food cooking
		{

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.BUTTER.item.get(), 1, 200, 0.1f)
					.setRecipeBookTab(CookingPotRecipeBookTab.MISC)
					.addIngredient(CommonTags.FOODS_MILK)
					.build(pvd);

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.TOFU.item.get(), 1, 200, 0.1f)
					.setRecipeBookTab(CookingPotRecipeBookTab.MISC)
					.addIngredient(YHCrops.SOYBEAN.getSeed())
					.addIngredient(YHCrops.SOYBEAN.getSeed())
					.build(pvd);

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.ONIGILI.item.get(), 1, 200, 0.1f)
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(CommonTags.CROPS_RICE)
					.addIngredient(Tags.Items.FOODS_VEGETABLE)
					.addIngredient(Items.KELP)
					.build(pvd);

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.SEKIBANKIYAKI.item.get(), 1, 200, 0.1f)
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(CommonTags.CROPS_RICE)
					.addIngredient(YHCrops.REDBEAN.getSeed())
					.addIngredient(YHFood.BUTTER.item)
					.build(pvd);

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.MOCHI.item.get(), 2, 200, 0.1f)
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(CommonTags.CROPS_RICE)
					.addIngredient(YHCrops.REDBEAN.getSeed())
					.build(pvd);

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.TSUKIMI_DANGO.item.get(), 2, 200, 0.1f)
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(CommonTags.CROPS_RICE)
					.addIngredient(YHCrops.SOYBEAN.getSeed())
					.build(pvd);

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.YASHOUMA_DANGO.item.get(), 1, 200, 0.1f)
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(CommonTags.CROPS_RICE)
					.addIngredient(Items.PINK_DYE)
					.addIngredient(Items.GREEN_DYE)
					.build(pvd);

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.SAKURA_MOCHI.item.get(), 2, 200, 0.1f)
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(CommonTags.CROPS_RICE)
					.addIngredient(YHCrops.REDBEAN.getSeed())
					.addIngredient(Items.CHERRY_LEAVES)
					.build(pvd);

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.COFFEE_MOCHI.item.get(), 2, 200, 0.1f)
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(CommonTags.CROPS_RICE)
					.addIngredient(YHItems.COFFEE_POWDER)
					.build(pvd);

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.MATCHA_MOCHI.item.get(), 2, 200, 0.1f)
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(CommonTags.CROPS_RICE)
					.addIngredient(YHTagGen.MATCHA)
					.build(pvd);

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.SENBEI.item.get(), 3, 200, 0.1f)
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(CommonTags.CROPS_RICE)
					.addIngredient(YHFood.BUTTER.item)
					.addIngredient(Items.KELP)
					.build(pvd);

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.YAKUMO_INARI.item.get(), 3, 200, 0.1f)
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(CommonTags.CROPS_RICE)
					.addIngredient(Tags.Items.EGGS)
					.addIngredient(Items.CARROT)
					.addIngredient(YHFood.OILY_BEAN_CURD.item.get())
					.addIngredient(YHFood.OILY_BEAN_CURD.item.get())
					.addIngredient(YHFood.OILY_BEAN_CURD.item.get())
					.build(pvd);

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.BUN.item.get(), 3, 200, 0.1f)
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(CommonTags.FOODS_DOUGH)
					.addIngredient(ModTags.CABBAGE_ROLL_INGREDIENTS)
					.addIngredient(CommonTags.FOODS_CABBAGE)
					.addIngredient(CommonTags.FOODS_ONION)
					.addIngredient(YHCrops.SOYBEAN.getSeed())
					.build(pvd);

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.OYAKI.item.get(), 1, 200, 0.1f)
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(CommonTags.FOODS_DOUGH)
					.addIngredient(Tags.Items.FOODS_VEGETABLE)
					.addIngredient(Items.BROWN_MUSHROOM)
					.build(pvd);

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.PORK_RICE_BALL.item.get(), 1, 200, 0.1f)
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(CommonTags.CROPS_RICE)
					.addIngredient(CommonTags.FOODS_RAW_PORK)
					.build(pvd);

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.TUTU_CONGEE.item.get(), 1, 200, 0.1f)
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(CommonTags.CROPS_RICE)
					.addIngredient(Items.BAMBOO)
					.build(pvd);

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.STEAMED_EGG_IN_BAMBOO.item.get(), 1, 200, 0.1f)
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(Tags.Items.EGGS)
					.addIngredient(Items.BAMBOO)
					.build(pvd);

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.CANDY_APPLE.item.get(), 1, 200, 0.1f, Items.STICK)
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(Items.APPLE)
					.addIngredient(Items.SUGAR)
					.addIngredient(Items.SUGAR)
					.addIngredient(Items.SUGAR)
					.build(pvd);

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.MITARASHI_DANGO.item.get(), 1, 200, 0.1f, Items.STICK)
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(YHTagGen.DANGO)
					.addIngredient(YHTagGen.DANGO)
					.addIngredient(YHTagGen.DANGO)
					.addIngredient(YHCrops.SOYBEAN.getSeed())
					.addIngredient(YHItems.SOY_SAUCE_BOTTLE.item)
					.addIngredient(Items.SUGAR)
					.build(pvd);

			CookingPotRecipeBuilder.cookingPotRecipe(YHItems.SURP_CHEST.get(), 1, 200, 0.1f, Items.CHEST)
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(Items.RED_MUSHROOM)
					.addIngredient(Items.RED_MUSHROOM)
					.addIngredient(Items.HONEY_BOTTLE)
					.addIngredient(YHItems.CREAM.get())
					.addIngredient(YHCrops.UDUMBARA.getFruits())
					.addIngredient(Items.PURPLE_BANNER)
					.build(pvd);


		}

		// food cooking bowl
		{
			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.APAKI.item.get(), 1, 200, 0.1f, Items.BOWL)
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(CommonTags.FOODS_RAW_PORK)
					.addIngredient(CommonTags.FOODS_RAW_PORK)
					.addIngredient(Items.PINK_PETALS)
					.build(pvd);

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.AVGOLEMONO.item.get(), 1, 200, 0.1f, Items.BOWL)
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(Tags.Items.EGGS)
					.addIngredient(Items.GLOW_BERRIES)
					.addIngredient(Items.GLOW_BERRIES)
					.build(pvd);

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.BLAZING_RED_CURRY.item.get(), 1, 200, 0.1f, Items.BOWL)
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(CommonTags.CROPS_RICE)
					.addIngredient(Items.CRIMSON_FUNGUS)
					.addIngredient(Items.CRIMSON_FUNGUS)
					.addIngredient(Items.BLAZE_POWDER)
					.addIngredient(Tags.Items.CROPS_POTATO)
					.addIngredient(CommonTags.FOODS_RAW_CHICKEN)
					.build(pvd);

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.GRILLED_EEL_OVER_RICE.item.get(), 1, 200, 0.1f, Items.BOWL)
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(YHTagGen.RAW_EEL)
					.addIngredient(YHTagGen.RAW_EEL)
					.addIngredient(CommonTags.CROPS_RICE)
					.addIngredient(YHItems.SOY_SAUCE_BOTTLE.item)
					.build(pvd);

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.HIGAN_SOUP.item.get(), 1, 200, 0.1f, Items.BOWL)
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(Items.SOUL_SAND)
					.addIngredient(Items.SOUL_SAND)
					.addIngredient(Tags.Items.FOODS_VEGETABLE)
					.build(pvd);

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.LONGEVITY_NOODLES.item.get(), 1, 200, 0.1f, Items.BOWL)
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(CommonTags.FOODS_PASTA)
					.addIngredient(Tags.Items.FOODS_VEGETABLE)
					.addIngredient(Items.BROWN_MUSHROOM)
					.addIngredient(CommonTags.FOODS_RAW_PORK)
					.build(pvd);

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.MISO_SOUP.item.get(), 1, 200, 0.1f, Items.BOWL)
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(YHFood.TOFU.item.get())
					.addIngredient(YHCrops.SOYBEAN.getSeed())
					.addIngredient(Items.DRIED_KELP)
					.addIngredient(Items.BROWN_MUSHROOM)
					.build(pvd);

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.SEAFOOD_MISO_SOUP.item.get(), 1, 200, 0.1f, Items.BOWL)
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(YHFood.TOFU.item.get())
					.addIngredient(YHCrops.SOYBEAN.getSeed())
					.addIngredient(Items.DRIED_KELP)
					.addIngredient(Items.BROWN_MUSHROOM)
					.addIngredient(CommonTags.FOODS_RAW_SALMON)
					.addIngredient(CommonTags.FOODS_RAW_SALMON)
					.build(pvd);

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.POOR_GOD_SOUP.item.get(), 1, 200, 0.1f, Items.BOWL)
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(Tags.Items.SEEDS)
					.addIngredient(Tags.Items.CROPS)
					.addIngredient(ItemTags.FLOWERS)
					.addIngredient(Items.BONE_MEAL)
					.build(pvd);

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.POWER_SOUP.item.get(), 1, 200, 0.1f, Items.BOWL)
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(CommonTags.FOODS_RAW_PORK)
					.addIngredient(CommonTags.FOODS_RAW_PORK)
					.addIngredient(Items.KELP)
					.addIngredient(Items.KELP)
					.addIngredient(CommonTags.FOODS_ONION)
					.build(pvd);

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.SHIRAYUKI.item.get(), 1, 200, 0.1f, Items.BOWL)
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(Items.PUFFERFISH)
					.addIngredient(YHTagGen.RAW_EEL)
					.addIngredient(Items.KELP)
					.addIngredient(YHFood.TOFU.item.get())
					.addIngredient(Tags.Items.FOODS_VEGETABLE)
					.build(pvd);

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.SWEET_ORMOSIA_MOCHI_MIXED_BOILED.item.get(), 1, 200, 0.1f, Items.BOWL)
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(YHTagGen.DANGO)
					.addIngredient(Items.CARROT)
					.addIngredient(Tags.Items.FOODS_VEGETABLE)
					.build(pvd);

			CookingPotRecipeBuilder.cookingPotRecipe(YHItems.CREAM.get(), 1, 200, 0.1f, Items.BOWL)
					.setRecipeBookTab(CookingPotRecipeBookTab.MISC)
					.addIngredient(CommonTags.FOODS_MILK)
					.addIngredient(CommonTags.FOODS_MILK)
					.addIngredient(CommonTags.FOODS_MILK)
					.build(pvd);

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.TUSCAN_SALMON.item.get(), 1, 200, 0.1f, Items.BOWL)
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(CommonTags.FOODS_RAW_SALMON)
					.addIngredient(CommonTags.FOODS_TOMATO)
					.addIngredient(CommonTags.FOODS_CABBAGE)
					.addIngredient(YHItems.CREAM.get())
					.build(pvd);

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.MUSHROOM_SOUP.item.get(), 1, 200, 0.1f, Items.BOWL)
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(Items.BROWN_MUSHROOM)
					.addIngredient(Items.BROWN_MUSHROOM)
					.addIngredient(Items.BROWN_MUSHROOM)
					.addIngredient(CommonTags.FOODS_ONION)
					.addIngredient(YHItems.CREAM.get())
					.build(pvd);

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.LIONS_HEAD.item.get(), 1, 200, 0.1f, Items.BOWL)
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(CommonTags.FOODS_RAW_PORK)
					.addIngredient(CommonTags.FOODS_RAW_PORK)
					.addIngredient(Tags.Items.CROPS_CARROT)
					.addIngredient(CommonTags.FOODS_CABBAGE)
					.build(pvd);

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.MAPO_TOFU.item.get(), 1, 200, 0.1f, Items.BOWL)
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(YHFood.TOFU.item)
					.addIngredient(YHFood.TOFU.item)
					.addIngredient(YHFood.TOFU.item)
					.addIngredient(CommonTags.FOODS_RAW_PORK)
					.addIngredient(Items.BLAZE_POWDER)
					.build(pvd);

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.UDUMBARA_CAKE.item.get(), 1, 200, 0.1f, Items.BOWL)
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(CommonTags.FOODS_DOUGH)
					.addIngredient(YHCrops.UDUMBARA.getFruits())
					.build(pvd);
		}

		// food cooking saucer
		{
			CookingPotRecipeBuilder.cookingPotRecipe(YHDish.BAMBOO_MIZUYOKAN.block.get(), 1, 200, 0.1f, YHItems.SAUCER.get())
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(YHCrops.REDBEAN.getSeed())
					.addIngredient(PotionIngredient.of(Potions.WATER))
					.addIngredient(Items.BAMBOO)
					.addIngredient(Items.SUGAR)
					.build(pvd);

			CookingPotRecipeBuilder.cookingPotRecipe(YHDish.DRIED_FISH.block.get(), 1, 200, 0.1f, YHItems.SAUCER.get())
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(CommonTags.FOODS_SAFE_RAW_FISH)
					.addIngredient(CommonTags.FOODS_SAFE_RAW_FISH)
					.addIngredient(CommonTags.FOODS_SAFE_RAW_FISH)
					.addIngredient(CommonTags.FOODS_CABBAGE)
					.build(pvd);

			CookingPotRecipeBuilder.cookingPotRecipe(YHDish.IMITATION_BEAR_PAW.block.get(), 1, 200, 0.1f, YHItems.SAUCER.get())
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(Items.PUFFERFISH)
					.addIngredient(Items.BAMBOO)
					.addIngredient(CommonTags.FOODS_RAW_PORK)
					.addIngredient(CommonTags.FOODS_ONION)
					.addIngredient(YHTagGen.RAW_EEL)
					.addIngredient(YHItems.SOY_SAUCE_BOTTLE.item)
					.build(pvd);

			CookingPotRecipeBuilder.cookingPotRecipe(YHDish.PASTITSIO.block.get(), 1, 200, 0.1f, YHItems.SAUCER.get())
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(CommonTags.FOODS_PASTA)
					.addIngredient(YHFood.BUTTER.item)
					.addIngredient(ModItems.TOMATO_SAUCE.get())
					.addIngredient(CommonTags.FOODS_RAW_BEEF)
					.addIngredient(CommonTags.FOODS_ONION)
					.build(pvd);

			CookingPotRecipeBuilder.cookingPotRecipe(YHDish.SAUCE_GRILLED_FISH.block.get(), 1, 200, 0.1f, YHItems.SAUCER.get())
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(CommonTags.FOODS_SAFE_RAW_FISH)
					.addIngredient(YHItems.SOY_SAUCE_BOTTLE.item)
					.addIngredient(Tags.Items.FOODS_VEGETABLE)
					.addIngredient(Tags.Items.FOODS_VEGETABLE)
					.addIngredient(CommonTags.FOODS_ONION)
					.build(pvd);

			CookingPotRecipeBuilder.cookingPotRecipe(YHDish.STINKY_TOFU.block.get(), 1, 200, 0.1f, YHItems.SAUCER.get())
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(YHFood.TOFU.item)
					.addIngredient(YHFood.TOFU.item)
					.addIngredient(Items.BROWN_MUSHROOM)
					.addIngredient(CommonTags.FOODS_CABBAGE)
					.build(pvd);

			CookingPotRecipeBuilder.cookingPotRecipe(YHDish.TOFU_BURGER.block.get(), 1, 200, 0.1f, YHItems.SAUCER.get())
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(YHFood.TOFU.item)
					.addIngredient(YHFood.BUTTER.item)
					.addIngredient(Items.SWEET_BERRIES)
					.addIngredient(CommonTags.FOODS_CABBAGE)
					.build(pvd);


			CookingPotRecipeBuilder.cookingPotRecipe(YHDish.SEVEN_COLORED_YOKAN.block.get(), 1, 200, 0.1f, YHItems.SAUCER.get())
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(YHCrops.REDBEAN.getSeed())
					.addIngredient(YHCrops.SOYBEAN.getSeed())
					.addIngredient(Items.CHERRY_LEAVES)
					.addIngredient(Items.CHORUS_FRUIT)
					.addIngredient(YHTagGen.MATCHA)
					.addIngredient(YHCrops.UDUMBARA.getFruits())
					.build(pvd);

			unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, YHDish.IMITATION_BEAR_PAW.raw.get())::unlockedBy, YHItems.SAUCER.asItem())
					.requires(Items.PUFFERFISH)
					.requires(Items.BAMBOO)
					.requires(CommonTags.FOODS_RAW_PORK)
					.requires(CommonTags.FOODS_ONION)
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
					.requires(CommonTags.FOODS_CABBAGE)
					.requires(YHItems.ICE_CUBE)
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
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(CommonTags.FOODS_RAW_PORK)
					.addIngredient(CommonTags.FOODS_RAW_PORK)
					.addIngredient(ModItems.TOMATO_SAUCE.get())
					.addIngredient(YHItems.SOY_SAUCE_BOTTLE.item)
					.addIngredient(CommonTags.FOODS_CABBAGE)
					.build(pvd);

			CookingPotRecipeBuilder.cookingPotRecipe(YHDish.TOMATO_SAUCE_COD.block.get(), 1, 200, 0.1f, YHItems.SAUCER.get())
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(CommonTags.FOODS_RAW_COD)
					.addIngredient(CommonTags.FOODS_RAW_COD)
					.addIngredient(ModItems.TOMATO_SAUCE.get())
					.addIngredient(YHItems.SOY_SAUCE_BOTTLE.item)
					.build(pvd);

		}

		// steam
		{
			steaming(pvd, DataIngredient.items(Items.POTATO), () -> Items.BAKED_POTATO);
			steaming(pvd, DataIngredient.items(ModItems.CHICKEN_CUTS.get()), ModItems.COOKED_CHICKEN_CUTS);
			steaming(pvd, DataIngredient.items(ModItems.SALMON_SLICE.get()), ModItems.COOKED_SALMON_SLICE);
			steaming(pvd, DataIngredient.items(ModItems.COD_SLICE.get()), ModItems.COOKED_COD_SLICE);


			unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, YHFood.BUN.raw.get(), 4)::unlockedBy, Items.WHEAT)
					.requires(CommonTags.FOODS_DOUGH)
					.requires(CommonTags.FOODS_DOUGH)
					.requires(ModTags.CABBAGE_ROLL_INGREDIENTS)
					.requires(CommonTags.FOODS_CABBAGE)
					.requires(CommonTags.FOODS_ONION)
					.requires(YHCrops.SOYBEAN.getSeed())
					.save(pvd);

			steaming(pvd, DataIngredient.items(YHFood.BUN.raw.get()), YHFood.BUN.item);
			steaming(pvd, DataIngredient.tag(CommonTags.FOODS_DOUGH), YHFood.MANTOU.item);

			unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, YHFood.OYAKI.raw.get(), 4)::unlockedBy, Items.WHEAT)
					.requires(CommonTags.FOODS_DOUGH)
					.requires(CommonTags.FOODS_DOUGH)
					.requires(CommonTags.FOODS_LEAFY_GREEN)
					.requires(Items.BROWN_MUSHROOM)
					.save(pvd);

			steaming(pvd, DataIngredient.items(YHFood.OYAKI.raw.get()), YHFood.OYAKI.item);
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
					.build(tea);

			CookingPotRecipeBuilder.cookingPotRecipe(YHDrink.GREEN_TEA.item.get(), 1, 200, 0.1f, Items.GLASS_BOTTLE)
					.setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
					.unlockedByAnyIngredient(YHTea.GREEN.leaves)
					.addIngredient(YHTagGen.TEA_GREEN)
					.addIngredient(YHTagGen.TEA_GREEN)
					.build(tea);

			CookingPotRecipeBuilder.cookingPotRecipe(YHDrink.OOLONG_TEA.item.get(), 1, 200, 0.1f, Items.GLASS_BOTTLE)
					.setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
					.unlockedByAnyIngredient(YHTea.OOLONG.leaves)
					.addIngredient(YHTagGen.TEA_OOLONG)
					.addIngredient(YHTagGen.TEA_OOLONG)
					.build(tea);

			CookingPotRecipeBuilder.cookingPotRecipe(YHDrink.WHITE_TEA.item.get(), 1, 200, 0.1f, Items.GLASS_BOTTLE)
					.setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
					.unlockedByAnyIngredient(YHTea.WHITE.leaves)
					.addIngredient(YHTagGen.TEA_WHITE)
					.addIngredient(YHTagGen.TEA_WHITE)
					.build(tea);

			CookingPotRecipeBuilder.cookingPotRecipe(YHDrink.CORNFLOWER_TEA.item.get(), 1, 200, 0.1f, Items.GLASS_BOTTLE)
					.setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
					.unlockedByAnyIngredient(Items.CORNFLOWER)
					.addIngredient(Items.CORNFLOWER)
					.addIngredient(Items.CORNFLOWER)
					.build(tea);

			CookingPotRecipeBuilder.cookingPotRecipe(YHDrink.TEA_MOCHA.item.get(), 1, 200, 0.1f, Items.GLASS_BOTTLE)
					.setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
					.unlockedByAnyIngredient(YHTea.BLACK.leaves)
					.addIngredient(YHTagGen.TEA_BLACK)
					.addIngredient(Items.COCOA_BEANS)
					.addIngredient(CommonTags.FOODS_MILK)
					.build(tea);

			CookingPotRecipeBuilder.cookingPotRecipe(YHDrink.SAIDI_TEA.item.get(), 1, 200, 0.1f, Items.GLASS_BOTTLE)
					.setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
					.unlockedByAnyIngredient(YHTea.BLACK.leaves)
					.addIngredient(YHTagGen.TEA_BLACK)
					.addIngredient(Items.SUGAR)
					.addIngredient(Items.SUGAR)
					.build(tea);

			CookingPotRecipeBuilder.cookingPotRecipe(YHDrink.SAKURA_HONEY_TEA.item.get(), 1, 200, 0.1f, Items.GLASS_BOTTLE)
					.setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
					.unlockedByAnyIngredient(Items.CHERRY_LEAVES)
					.addIngredient(Items.CHERRY_LEAVES)
					.addIngredient(Items.HONEY_BOTTLE)
					.build(tea);

			CookingPotRecipeBuilder.cookingPotRecipe(YHDrink.GENMAI_TEA.item.get(), 1, 200, 0.1f, Items.GLASS_BOTTLE)
					.setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
					.unlockedByAnyIngredient(YHTea.GREEN.leaves)
					.addIngredient(YHTagGen.TEA_GREEN)
					.addIngredient(YHTagGen.TEA_GREEN)
					.addIngredient(CommonTags.CROPS_RICE)
					.build(tea);


			CookingPotRecipeBuilder.cookingPotRecipe(YHDrink.GREEN_WATER.item.get(), 1, 200, 0.1f, Items.GLASS_BOTTLE)
					.setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
					.unlockedByAnyIngredient(Items.GLASS_BOTTLE)
					.addIngredient(CommonTags.FOODS_CABBAGE)
					.addIngredient(CommonTags.FOODS_CABBAGE)
					.build(tea);

			CookingPotRecipeBuilder.cookingPotRecipe(YHCoffee.ESPRESSO.item.get(), 1, 200, 0.1f, Items.GLASS_BOTTLE)
					.setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
					.unlockedByAnyIngredient(YHItems.COFFEE_POWDER)
					.addIngredient(YHItems.COFFEE_POWDER)
					.addIngredient(PotionIngredient.of(Potions.WATER))
					.build(coffee);

			coffee(coffee, YHCoffee.RISTRETTO, 1, e -> e.addIngredient(YHItems.COFFEE_POWDER));
			coffee(coffee, YHCoffee.AMERICANO, 2, e -> e.addIngredient(PotionIngredient.of(Potions.WATER)));
			coffee(coffee, YHCoffee.LATTE, 2, e -> e.addIngredient(CommonTags.FOODS_MILK));
			coffee(coffee, YHCoffee.MOCHA, 2, e -> e
					.addIngredient(CommonTags.FOODS_MILK)
					.addIngredient(Items.COCOA_BEANS));
			coffee(coffee, YHCoffee.CAPPUCCINO, 2, e -> e
					.addIngredient(CommonTags.FOODS_MILK)
					.addIngredient(YHItems.CREAM));
			coffee(coffee, YHCoffee.MACCHIATO, 2, e -> e.addIngredient(YHItems.CREAM));
			coffee(coffee, YHCoffee.CON_PANNA, 1, e -> e
					.addIngredient(YHItems.COFFEE_POWDER)
					.addIngredient(YHItems.CREAM));
			coffee(coffee, YHCoffee.AFFOGATO, 2, e -> e
					.addIngredient(YHItems.ICE_CUBE)
					.addIngredient(YHItems.CREAM));

			unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, YHCoffee.AFFOGATO.item.get(), 1)::unlockedBy,
					YHCoffee.ESPRESSO.item.get()).requires(YHCoffee.ESPRESSO.item)
					.requires(YHItems.ICE_CUBE).requires(YHItems.CREAM)
					.save(coffee, YHCoffee.AFFOGATO.item.getId().withSuffix("_craft"));

		}

		// sake
		{

			unlock(pvd, new SimpleFermentationBuilder(FluidTags.WATER, YHItems.SOY_SAUCE_BOTTLE, 1800)::unlockedBy, YHCrops.SOYBEAN.getSeed())
					.addInput(YHCrops.SOYBEAN.getSeed()).addInput(YHCrops.SOYBEAN.getSeed())
					.addInput(YHCrops.SOYBEAN.getSeed()).addInput(YHCrops.SOYBEAN.getSeed())
					.save(pvd);

			unlock(pvd, new SimpleFermentationBuilder(FluidTags.WATER, YHDrink.MIO, 2400)::unlockedBy, ModItems.RICE.get())
					.addInput(CommonTags.CROPS_RICE).addInput(CommonTags.CROPS_RICE).addInput(CommonTags.CROPS_RICE).addInput(CommonTags.CROPS_RICE)
					.save(pvd);

			unlock(pvd, new SimpleFermentationBuilder(FluidTags.WATER, YHDrink.MEAD, 2400)::unlockedBy, ModItems.RICE.get())
					.addInput(CommonTags.CROPS_RICE).addInput(CommonTags.CROPS_RICE).addInput(CommonTags.CROPS_RICE).addInput(CommonTags.CROPS_RICE)
					.addInput(Items.HONEY_BOTTLE)
					.save(pvd);

			unlock(pvd, new SimpleFermentationBuilder(FluidTags.WATER, YHDrink.KIKU, 2400)::unlockedBy, ModItems.RICE.get())
					.addInput(CommonTags.CROPS_RICE).addInput(CommonTags.CROPS_RICE).addInput(CommonTags.CROPS_RICE)
					.addInput(Items.BROWN_MUSHROOM)
					.save(pvd);

			unlock(pvd, new SimpleFermentationBuilder(FluidTags.WATER, YHDrink.HAKUTSURU, 2400)::unlockedBy, ModItems.RICE.get())
					.addInput(CommonTags.CROPS_RICE).addInput(CommonTags.CROPS_RICE).addInput(CommonTags.CROPS_RICE)
					.addInput(Items.BROWN_MUSHROOM).addInput(Tags.Items.EGGS)
					.save(pvd);

			unlock(pvd, new SimpleFermentationBuilder(FluidTags.WATER, YHDrink.KAPPA_VILLAGE, 2400)::unlockedBy, ModItems.RICE.get())
					.addInput(CommonTags.CROPS_RICE).addInput(CommonTags.CROPS_RICE).addInput(CommonTags.CROPS_RICE)
					.addInput(Items.BROWN_MUSHROOM).addInput(Items.SEAGRASS)
					.save(pvd);


			unlock(pvd, new SimpleFermentationBuilder(FluidTags.WATER, YHDrink.SUIGEI, 2400)::unlockedBy, ModItems.RICE.get())
					.addInput(CommonTags.CROPS_RICE).addInput(CommonTags.CROPS_RICE).addInput(CommonTags.CROPS_RICE)
					.addInput(Items.SEA_PICKLE).addInput(Items.KELP).addInput(Items.PUFFERFISH)
					.save(pvd);

			unlock(pvd, new SimpleFermentationBuilder(FluidTags.WATER, YHDrink.DAIGINJO, 2400)::unlockedBy, ModItems.RICE.get())
					.addInput(CommonTags.CROPS_RICE).addInput(CommonTags.CROPS_RICE).addInput(CommonTags.CROPS_RICE)
					.addInput(Items.NETHER_WART).addInput(Items.BLAZE_POWDER)
					.save(pvd);

			unlock(pvd, new SimpleFermentationBuilder(FluidTags.WATER, YHDrink.DASSAI, 2400)::unlockedBy, ModItems.RICE.get())
					.addInput(CommonTags.CROPS_RICE).addInput(CommonTags.CROPS_RICE).addInput(CommonTags.CROPS_RICE)
					.addInput(Items.NETHER_WART).addInput(Items.NAUTILUS_SHELL)
					.save(pvd);

			unlock(pvd, new SimpleFermentationBuilder(FluidTags.WATER, YHDrink.TENGU_TANGO, 2400)::unlockedBy, ModItems.RICE.get())
					.addInput(CommonTags.CROPS_RICE).addInput(CommonTags.CROPS_RICE).addInput(CommonTags.CROPS_RICE)
					.addInput(Items.NETHER_WART).addInput(Items.PHANTOM_MEMBRANE)
					.save(pvd);

			unlock(pvd, new SimpleFermentationBuilder(FluidTags.WATER, YHDrink.SPARROW_SAKE, 2400)::unlockedBy, ModItems.RICE.get())
					.addInput(CommonTags.CROPS_RICE).addInput(CommonTags.CROPS_RICE).addInput(CommonTags.CROPS_RICE)
					.addInput(Items.FEATHER).addInput(Items.RABBIT_FOOT)
					.save(pvd);

			unlock(pvd, new SimpleFermentationBuilder(FluidTags.WATER, YHDrink.FULL_MOONS_EVE, 2400)::unlockedBy, ModItems.RICE.get())
					.addInput(CommonTags.CROPS_RICE).addInput(CommonTags.CROPS_RICE).addInput(CommonTags.CROPS_RICE)
					.addInput(Items.NETHER_WART).addInput(YHCrops.UDUMBARA.getFruits())
					.save(pvd);


		}

		if (ModList.get().isLoaded(FruitsDelight.MODID)) {
			RecipeOutput modtea = new ConditionalRecipeWrapper(tea, new ModLoadedCondition(FruitsDelight.MODID));
			CookingPotRecipeBuilder.cookingPotRecipe(FruitsDelightCompatFood.MOON_ROCKET.item.get(), 1, 200, 0.1f, Items.GLASS_BOTTLE)
					.setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
					.unlockedByAnyIngredient(FDFood.LEMON_SLICE.get())
					.addIngredient(FDFood.LEMON_SLICE.get())
					.addIngredient(Items.SUGAR)
					.build(modtea);

			CookingPotRecipeBuilder.cookingPotRecipe(FruitsDelightCompatFood.LEMON_BLACK_TEA.item.get(), 1, 200, 0.1f, Items.GLASS_BOTTLE)
					.setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
					.unlockedByAnyIngredient(FDFood.LEMON_SLICE.get())
					.addIngredient(FDFood.LEMON_SLICE.get())
					.addIngredient(YHTagGen.TEA_BLACK)
					.addIngredient(Items.SUGAR)
					.build(modtea);

			CookingPotRecipeBuilder.cookingPotRecipe(FruitsDelightCompatFood.PEACH_TAPIOCA.item.get(), 1, 200, 0.1f, Items.BOWL)
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(FruitType.PEACH.getFruitTag())
					.addIngredient(Items.LILY_PAD)
					.build(ConditionalRecipeWrapper.mod(pvd, FruitsDelight.MODID));


			unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, FruitsDelightCompatFood.PEACH_YATSUHASHI.item, 2)::unlockedBy, FruitType.PEACH.getFruit())
					.requires(FruitType.PEACH.getFruitTag())
					.requires(ModItems.COOKED_RICE.get())
					.save(ConditionalRecipeWrapper.mod(pvd, FruitsDelight.MODID));

		}

		if (ModList.get().isLoaded(Create.ID)) CreateRecipeGen.onRecipeGen(pvd);

	}

	private static void food(RegistrateRecipeProvider pvd, YHFood raw, YHFood cooked) {
		pvd.food(DataIngredient.items(raw.item.get()), RecipeCategory.FOOD, cooked.item, 0.1f);
	}

	private static void cutting(RegistrateRecipeProvider pvd, ItemEntry<?> in, ItemEntry<?> out, int count) {
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(in),
						Ingredient.of(CommonTags.TOOLS_KNIFE), out, count, 1)
				.build(pvd, in.getId().withSuffix("_cutting"));
	}

	private static void coffee(RecipeOutput cons, YHCoffee coffee, int count, UnaryOperator<CookingPotRecipeBuilder> func) {
		func.apply(CookingPotRecipeBuilder.cookingPotRecipe(coffee.item.get(), count, 200, 0.1f, Items.GLASS_BOTTLE)
						.setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
						.unlockedByAnyIngredient(YHItems.COFFEE_POWDER)
						.addIngredient(YHItems.COFFEE_POWDER)
						.addIngredient(PotionIngredient.of(Potions.WATER)))
				.build(cons);

		func.apply(CookingPotRecipeBuilder.cookingPotRecipe(coffee.item.get(), count, 200, 0.1f, Items.GLASS_BOTTLE)
						.setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
						.unlockedByAnyIngredient(YHCoffee.ESPRESSO.item)
						.addIngredient(YHCoffee.ESPRESSO.item))
				.build(cons, coffee.item.getId().withSuffix("_remix").toString());
	}

	private static void cake(RegistrateRecipeProvider pvd, CakeEntry cake) {
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(cake.block.get()),
						Ingredient.of(CommonTags.TOOLS_KNIFE), cake.item.get(), cake.isCake ? 7 : 4)
				.build(pvd);
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
		cooking(pvd, in, RecipeCategory.MISC, out, 0, 200,
				"drying", YHBlocks.RACK_RS.get(), DryingRackRecipe::new);
	}

	private static void steaming(RegistrateRecipeProvider pvd, DataIngredient in, Supplier<Item> out) {
		cooking(pvd, in, RecipeCategory.MISC, out, 0, 200, "steaming", YHBlocks.STEAM_RS.get(), SteamingRecipe::new);
	}

	public static <T extends ItemLike, R extends AbstractCookingRecipe> void cooking(
			RegistrateRecipeProvider pvd, DataIngredient source, RecipeCategory category,
			Supplier<? extends T> result, float experience, int cookingTime, String typeName,
			RecipeSerializer<R> ser, AbstractCookingRecipe.Factory<R> fac) {
		new SimpleCookingRecipeBuilder(category, CookingBookCategory.MISC, result.get(), source.toVanilla(), experience, cookingTime, fac)
				.unlockedBy("has_" + pvd.safeName(source), source.getCriterion(pvd))
				.save(pvd, pvd.safeId(result.get()).withPath(e -> typeName + "/" + e + "_from_" + pvd.safeName(source)));
	}

	private static RecipeOutput tea(RegistrateRecipeProvider pvd) {
		return new BasePotOutput<>(YHBlocks.KETTLE_RS.get(), pvd, "kettle");
	}

	private static RecipeOutput coffee(RegistrateRecipeProvider pvd) {
		return new BasePotOutput<>(YHBlocks.MOKA_RS.get(), pvd, "moka");
	}

	private static void foodCut(RegistrateRecipeProvider pvd,
								YHFood raw, YHFood cooked,
								YHFood raw_cut, YHFood cooked_cut) {
		food(pvd, raw, cooked);
		food(pvd, raw_cut, cooked_cut);
		cutting(pvd, raw.item, raw_cut.item, 2);
	}

	public static <T> T unlock(RegistrateRecipeProvider pvd, BiFunction<String, Criterion<InventoryChangeTrigger.TriggerInstance>, T> func, Item item) {
		return func.apply("has_" + pvd.safeName(item), DataIngredient.items(item).getCriterion(pvd));
	}

}
