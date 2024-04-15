package dev.xkmc.youkaishomecoming.init.data;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.l2library.compat.patchouli.ShapelessPatchouliBuilder;
import dev.xkmc.l2library.serial.ingredients.PotionIngredient;
import dev.xkmc.youkaishomecoming.content.pot.base.BasePotFinishedRecipe;
import dev.xkmc.youkaishomecoming.init.food.*;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import dev.xkmc.youkaishomecoming.init.registrate.YHDanmaku;
import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;
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
			foodCut(pvd, YHFood.RAW_LAMPREY, YHFood.ROASTED_LAMPREY, YHFood.RAW_LAMPREY_FILLET, YHFood.ROASTED_LAMPREY_FILLET);
			food(pvd, YHFood.FLESH, YHFood.COOKED_FLESH);
			food(pvd, YHFood.TOFU, YHFood.OILY_BEAN_CURD);
			pvd.stonecutting(DataIngredient.items(Items.CLAY_BALL), RecipeCategory.MISC, YHItems.CLAY_SAUCER);
			pvd.stonecutting(DataIngredient.items(Items.BAMBOO_BLOCK), RecipeCategory.MISC, YHBlocks.RACK);
			pvd.smelting(DataIngredient.items(YHItems.CLAY_SAUCER.get()), RecipeCategory.MISC, YHItems.SAUCER, 0.1f, 200);
			for (var e : YHBlocks.WoodType.values()) {
				pvd.stonecutting(DataIngredient.items(e.item), RecipeCategory.MISC, e.fence);
			}
			YHBlocks.HAY.genRecipe(pvd);
			YHBlocks.STRAW.genRecipe(pvd);

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

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, YHItems.STRAW_HAT)::unlockedBy, ModItems.CANVAS.get())
					.pattern(" A ").pattern("ASA")
					.define('A', ModItems.CANVAS.get())
					.define('S', Items.STRING)
					.save(pvd);
		}

		// plants
		{
			cutting(pvd, YHCrops.SOYBEAN.fruits, YHCrops.SOYBEAN.seed, 1);
			cutting(pvd, YHCrops.COFFEA.fruits, YHCrops.COFFEA.seed, 1);
			pvd.smelting(DataIngredient.items(YHCrops.COFFEA.getSeed()), RecipeCategory.MISC, YHItems.COFFEE_BEAN, 0.1f, 200);
			pvd.smoking(DataIngredient.items(YHCrops.COFFEA.getSeed()), RecipeCategory.MISC, YHItems.COFFEE_BEAN, 0.1f, 200);
			pvd.smelting(DataIngredient.items(YHCrops.MANDRAKE.getSeed()), RecipeCategory.FOOD, YHFood.COOKED_MANDRAKE_ROOT.item, 0.1f, 200);
			pvd.smoking(DataIngredient.items(YHCrops.MANDRAKE.getSeed()), RecipeCategory.FOOD, YHFood.COOKED_MANDRAKE_ROOT.item, 0.1f, 200);
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

			drying(pvd, DataIngredient.items(YHTea.GREEN.leaves.get()), YHTea.WHITE.leaves);
		}

		// food craft
		{
			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, YHFood.MILK_POPSICLE.item, 1)::unlockedBy, Items.ICE)
					.pattern(" MM").pattern("SIM").pattern("TS ")
					.define('M', ForgeTags.MILK_BOTTLE)
					.define('S', Items.SUGAR)
					.define('I', Items.ICE)
					.define('T', Items.STICK)
					.save(pvd);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, YHFood.BIG_POPSICLE.item, 1)::unlockedBy, Items.ICE)
					.pattern(" II").pattern("SII").pattern("TS ")
					.define('S', Items.SUGAR)
					.define('I', Items.ICE)
					.define('T', Items.STICK)
					.save(pvd);

			unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, YHFood.ASSORTED_DANGO.item, 1)::unlockedBy, YHFood.MOCHI.item.get())
					.requires(YHFood.MOCHI.item).requires(YHFood.MATCHA_MOCHI.item).requires(YHFood.SAKURA_MOCHI.item).requires(Items.STICK)
					.save(pvd);

			unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, YHFood.KINAKO_DANGO.item, 1)::unlockedBy, YHFood.MOCHI.item.get())
					.requires(YHTagGen.DANGO).requires(YHTagGen.DANGO).requires(YHTagGen.DANGO)
					.requires(YHCrops.SOYBEAN.getSeed()).requires(Items.STICK)
					.save(pvd);

			unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, YHFood.SHAVED_ICE_OVER_RICE.item, 1)::unlockedBy, Items.ICE)
					.requires(ForgeTags.GRAIN_RICE).requires(Items.ICE).requires(YHCrops.REDBEAN.getSeed())
					.requires(ModItems.COD_ROLL.get())
					.save(pvd);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, YHFood.FLESH_ROLL.item, 2)::unlockedBy, YHFood.FLESH.item.get())
					.pattern("FF").pattern("KR")
					.define('F', YHTagGen.RAW_FLESH)
					.define('K', Items.DRIED_KELP)
					.define('R', ModItems.COOKED_RICE.get())
					.save(pvd);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, YHItems.RAW_FLESH_FEAST, 1)::unlockedBy, YHFood.FLESH.item.get())
					.pattern("FFF").pattern("1F2").pattern("3S4")
					.define('F', YHTagGen.RAW_FLESH)
					.define('S', Items.SKELETON_SKULL)
					.define('1', Items.CARROT)
					.define('2', Items.BROWN_MUSHROOM)
					.define('3', ForgeTags.VEGETABLES_ONION)
					.define('4', ForgeTags.SALAD_INGREDIENTS_CABBAGE)
					.save(pvd);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, YHItems.RED_VELVET.block, 1)::unlockedBy, YHFood.FLESH.item.get())
					.pattern("ABA").pattern("CDC").pattern("EEE")
					.define('A', Items.SUGAR)
					.define('B', Items.MILK_BUCKET)
					.define('C', YHItems.BLOOD_BOTTLE)
					.define('D', YHFood.FLESH.item)
					.define('E', Items.WHEAT)
					.save(pvd);

			unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, YHFood.TOBIKO_GUNKAN.item, 2)::unlockedBy, YHFood.ROE.item.get())
					.requires(YHFood.ROE.item).requires(ModItems.COOKED_RICE.get()).requires(Items.DRIED_KELP)
					.save(pvd);

			cake(pvd, YHItems.RED_VELVET);
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

			CookingPotRecipeBuilder.cookingPotRecipe(YHItems.SOY_SAUCE_BOTTLE.get(), 1, 200, 0.1f)
					.addIngredient(new PotionIngredient(Potions.WATER))
					.addIngredient(YHCrops.SOYBEAN.getSeed())
					.build(pvd, YHItems.SOY_SAUCE_BOTTLE.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.BUTTER.item.get(), 1, 200, 0.1f)
					.addIngredient(ForgeTags.MILK_BOTTLE)
					.build(pvd, YHFood.BUTTER.item.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.TOFU.item.get(), 1, 200, 0.1f)
					.addIngredient(YHCrops.SOYBEAN.getSeed())
					.addIngredient(YHCrops.SOYBEAN.getSeed())
					.build(pvd, YHFood.TOFU.item.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.ONIGILI.item.get(), 1, 200, 0.1f)
					.addIngredient(ForgeTags.GRAIN_RICE)
					.addIngredient(ForgeTags.VEGETABLES)
					.addIngredient(Items.KELP)
					.build(pvd, YHFood.ONIGILI.item.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.SEKIBANKIYAKI.item.get(), 1, 200, 0.1f)
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
					.addIngredient(Items.KELP)
					.build(pvd, YHFood.SENBEI.item.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.YAKUMO_INARI.item.get(), 3, 200, 0.1f)
					.addIngredient(ForgeTags.GRAIN_RICE)
					.addIngredient(ForgeTags.EGGS)
					.addIngredient(Items.CARROT)
					.addIngredient(YHFood.OILY_BEAN_CURD.item.get())
					.addIngredient(YHFood.OILY_BEAN_CURD.item.get())
					.addIngredient(YHFood.OILY_BEAN_CURD.item.get())
					.build(pvd, YHFood.YAKUMO_INARI.item.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.KOISHI_MOUSSE.item.get(), 1, 200, 0.1f)
					.addIngredient(Items.CORNFLOWER)
					.addIngredient(Items.ALLIUM)
					.addIngredient(ForgeTags.DOUGH)
					.addIngredient(Items.HONEY_BOTTLE)
					.addIngredient(YHItems.CREAM.get())
					.build(pvd, YHFood.KOISHI_MOUSSE.item.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.BUN.item.get(), 3, 200, 0.1f)
					.addIngredient(ForgeTags.DOUGH)
					.addIngredient(ModTags.CABBAGE_ROLL_INGREDIENTS)
					.addIngredient(ForgeTags.SALAD_INGREDIENTS_CABBAGE)
					.addIngredient(ForgeTags.VEGETABLES_ONION)
					.addIngredient(YHCrops.SOYBEAN.getSeed())
					.build(pvd, YHFood.BUN.item.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.OYAKI.item.get(), 1, 200, 0.1f)
					.addIngredient(ForgeTags.DOUGH)
					.addIngredient(ForgeTags.VEGETABLES)
					.addIngredient(Items.BROWN_MUSHROOM)
					.build(pvd, YHFood.OYAKI.item.getId());

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
					.addIngredient(YHItems.SOY_SAUCE_BOTTLE)
					.addIngredient(Items.SUGAR)
					.build(pvd, YHFood.MITARASHI_DANGO.item.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.FLESH_DUMPLINGS.item.get(), 2, 200, 0.1f)
					.addIngredient(ForgeTags.DOUGH)
					.addIngredient(YHTagGen.RAW_FLESH)
					.addIngredient(ForgeTags.VEGETABLES)
					.build(pvd, YHFood.FLESH_DUMPLINGS.item.getId());

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
					.addIngredient(YHItems.SOY_SAUCE_BOTTLE)
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

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.POOR_GOD_SOUP.item.get(), 1, 200, 0.1f, Items.BOWL)
					.addIngredient(ForgeTags.SEEDS)
					.addIngredient(ForgeTags.CROPS)
					.addIngredient(ItemTags.FLOWERS)
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

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.FLESH_STEW.item.get(), 1, 200, 0.1f, Items.BOWL)
					.addIngredient(YHTagGen.RAW_FLESH)
					.addIngredient(YHTagGen.RAW_FLESH)
					.addIngredient(YHTagGen.RAW_EEL)
					.addIngredient(ForgeTags.VEGETABLES)
					.addIngredient(ForgeTags.VEGETABLES)
					.addIngredient(YHItems.SOY_SAUCE_BOTTLE)
					.build(pvd, YHFood.FLESH_STEW.item.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHItems.FLESH_FEAST.get(), 1, 200, 0.1f, Items.BOWL)
					.addIngredient(YHItems.RAW_FLESH_FEAST)
					.addIngredient(YHTagGen.RAW_FLESH)
					.addIngredient(YHItems.BLOOD_BOTTLE)
					.addIngredient(YHItems.BLOOD_BOTTLE)
					.addIngredient(YHItems.SOY_SAUCE_BOTTLE)
					.build(pvd, YHItems.FLESH_FEAST.getId());

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
					.build(pvd, YHDish.DRIED_FISH.block.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHDish.IMITATION_BEAR_PAW.block.get(), 1, 200, 0.1f, YHItems.SAUCER.get())
					.addIngredient(Items.PUFFERFISH)
					.addIngredient(Items.BAMBOO)
					.addIngredient(ForgeTags.RAW_PORK)
					.addIngredient(ForgeTags.VEGETABLES_ONION)
					.addIngredient(YHTagGen.RAW_EEL)
					.addIngredient(YHItems.SOY_SAUCE_BOTTLE)
					.build(pvd, YHDish.IMITATION_BEAR_PAW.block.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHDish.PASTITSIO.block.get(), 1, 200, 0.1f, YHItems.SAUCER.get())
					.addIngredient(ForgeTags.PASTA)
					.addIngredient(YHFood.BUTTER.item)
					.addIngredient(ModItems.TOMATO_SAUCE.get())
					.addIngredient(ForgeTags.RAW_BEEF)
					.addIngredient(ForgeTags.VEGETABLES_ONION)
					.build(pvd, YHDish.PASTITSIO.block.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHDish.SAUCE_GRILLED_FISH.block.get(), 1, 200, 0.1f, YHItems.SAUCER.get())
					.addIngredient(ForgeTags.RAW_FISHES)
					.addIngredient(YHItems.SOY_SAUCE_BOTTLE)
					.addIngredient(ForgeTags.VEGETABLES)
					.addIngredient(ForgeTags.VEGETABLES)
					.addIngredient(ForgeTags.VEGETABLES_ONION)
					.build(pvd, YHDish.SAUCE_GRILLED_FISH.block.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHDish.STINKY_TOFU.block.get(), 1, 200, 0.1f, YHItems.SAUCER.get())
					.addIngredient(YHFood.TOFU.item)
					.addIngredient(YHFood.TOFU.item)
					.addIngredient(Items.BROWN_MUSHROOM)
					.build(pvd, YHDish.STINKY_TOFU.block.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHDish.TOFU_BURGER.block.get(), 1, 200, 0.1f, YHItems.SAUCER.get())
					.addIngredient(YHFood.TOFU.item)
					.addIngredient(YHFood.BUTTER.item)
					.addIngredient(Items.SWEET_BERRIES)
					.build(pvd, YHDish.TOFU_BURGER.block.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHDish.BLOOD_CURD.block.get(), 1, 200, 0.1f, YHItems.SAUCER.get())
					.addIngredient(YHItems.BLOOD_BOTTLE)
					.addIngredient(YHItems.BLOOD_BOTTLE)
					.addIngredient(ForgeTags.VEGETABLES)
					.build(pvd, YHDish.BLOOD_CURD.block.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHDish.SEVEN_COLORED_YOKAN.block.get(), 1, 200, 0.1f, YHItems.SAUCER.get())
					.addIngredient(YHCrops.REDBEAN.getSeed())
					.addIngredient(YHCrops.SOYBEAN.getSeed())
					.addIngredient(Items.CHERRY_LEAVES)
					.addIngredient(YHTagGen.MATCHA)
					.addIngredient(YHCrops.UDUMBARA.getFruits())
					.build(pvd, YHDish.SEVEN_COLORED_YOKAN.block.getId());

		}

		// drinks
		{

			var tea = tea(pvd);

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.BLACK_TEA.item.get(), 1, 200, 0.1f, Items.GLASS_BOTTLE)
					.setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
					.unlockedByAnyIngredient(YHTea.BLACK.leaves)
					.addIngredient(YHTagGen.TEA_BLACK)
					.addIngredient(YHTagGen.TEA_BLACK)
					.build(tea, YHFood.BLACK_TEA.item.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.GREEN_TEA.item.get(), 1, 200, 0.1f, Items.GLASS_BOTTLE)
					.setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
					.unlockedByAnyIngredient(YHTea.GREEN.leaves)
					.addIngredient(YHTagGen.TEA_GREEN)
					.addIngredient(YHTagGen.TEA_GREEN)
					.build(tea, YHFood.GREEN_TEA.item.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.OOLONG_TEA.item.get(), 1, 200, 0.1f, Items.GLASS_BOTTLE)
					.setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
					.unlockedByAnyIngredient(YHTea.OOLONG.leaves)
					.addIngredient(YHTagGen.TEA_OOLONG)
					.addIngredient(YHTagGen.TEA_OOLONG)
					.build(tea, YHFood.OOLONG_TEA.item.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.WHITE_TEA.item.get(), 1, 200, 0.1f, Items.GLASS_BOTTLE)
					.setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
					.unlockedByAnyIngredient(YHTea.WHITE.leaves)
					.addIngredient(YHTagGen.TEA_WHITE)
					.addIngredient(YHTagGen.TEA_WHITE)
					.build(tea, YHFood.WHITE_TEA.item.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.CORNFLOWER_TEA.item.get(), 1, 200, 0.1f, Items.GLASS_BOTTLE)
					.setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
					.unlockedByAnyIngredient(Items.CORNFLOWER)
					.addIngredient(Items.CORNFLOWER)
					.addIngredient(Items.CORNFLOWER)
					.build(tea, YHFood.CORNFLOWER_TEA.item.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.TEA_MOCHA.item.get(), 1, 200, 0.1f, Items.GLASS_BOTTLE)
					.setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
					.unlockedByAnyIngredient(YHTea.BLACK.leaves)
					.addIngredient(YHTagGen.TEA_BLACK)
					.addIngredient(Items.COCOA_BEANS)
					.addIngredient(ForgeTags.MILK_BOTTLE)
					.build(tea, YHFood.TEA_MOCHA.item.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.SAIDI_TEA.item.get(), 1, 200, 0.1f, Items.GLASS_BOTTLE)
					.setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
					.unlockedByAnyIngredient(YHTea.BLACK.leaves)
					.addIngredient(YHTagGen.TEA_BLACK)
					.addIngredient(Items.SUGAR)
					.addIngredient(Items.SUGAR)
					.build(tea, YHFood.SAIDI_TEA.item.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.SAKURA_HONEY_TEA.item.get(), 1, 200, 0.1f, Items.GLASS_BOTTLE)
					.setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
					.unlockedByAnyIngredient(Items.CHERRY_LEAVES)
					.addIngredient(Items.CHERRY_LEAVES)
					.addIngredient(Items.HONEY_BOTTLE)
					.build(tea, YHFood.SAKURA_HONEY_TEA.item.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.GENMAI_TEA.item.get(), 1, 200, 0.1f, Items.GLASS_BOTTLE)
					.setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
					.unlockedByAnyIngredient(YHTea.GREEN.leaves)
					.addIngredient(YHTagGen.TEA_GREEN)
					.addIngredient(YHTagGen.TEA_GREEN)
					.addIngredient(ForgeTags.GRAIN_RICE)
					.build(tea, YHFood.GENMAI_TEA.item.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.SCARLET_MIST.item.get(), 1, 200, 0.1f, Items.GLASS_BOTTLE)
					.setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
					.unlockedByAnyIngredient(YHItems.BLOOD_BOTTLE)
					.addIngredient(YHTagGen.TEA_BLACK)
					.addIngredient(YHItems.BLOOD_BOTTLE)
					.build(tea, YHFood.SCARLET_MIST.item.getId());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.WIND_PRIESTESSES.item.get(), 1, 200, 0.1f, Items.GLASS_BOTTLE)
					.setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
					.unlockedByAnyIngredient(YHItems.MATCHA)
					.addIngredient(YHTagGen.TEA_GREEN)
					.addIngredient(YHItems.MATCHA)
					.build(tea, YHFood.WIND_PRIESTESSES.item.getId());

			var coffee = coffee(pvd);

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
					.addIngredient(Items.ICE)
					.addIngredient(YHItems.CREAM));

			unlock(pvd, ShapelessPatchouliBuilder.shapeless(RecipeCategory.FOOD, YHCoffee.AFFOGATO.item.get(), 1)::unlockedBy,
					YHCoffee.ESPRESSO.item.get()).requires(YHCoffee.ESPRESSO.item)
					.requires(Items.ICE).requires(YHItems.CREAM)
					.save(coffee, YHCoffee.AFFOGATO.item.getId().withSuffix("_craft"));

		}

		// danmaku
		{
			for (var t : YHDanmaku.Bullet.values()) {
				for (var e : DyeColor.values()) {
					Item dye = ForgeRegistries.ITEMS.getValue(new ResourceLocation(e.getName() + "_dye"));
					assert dye != null;
					Item danmaku = t.get(e).get();
					unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, danmaku, 8)::unlockedBy, danmaku)
							.pattern("AAA").pattern("ABA").pattern("AAA")
							.define('A', t.tag)
							.define('B', dye)
							.save(pvd);
				}
			}
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
		func.apply(CookingPotRecipeBuilder.cookingPotRecipe(coffee.item.get(), 1, 200, 0.1f, Items.GLASS_BOTTLE)
						.setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
						.unlockedByAnyIngredient(YHItems.COFFEE_POWDER)
						.addIngredient(YHItems.COFFEE_POWDER)
						.addIngredient(new PotionIngredient(Potions.WATER)))
				.build(cons, coffee.item.getId());

		func.apply(CookingPotRecipeBuilder.cookingPotRecipe(coffee.item.get(), 1, 200, 0.1f, Items.GLASS_BOTTLE)
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
		cooking(pvd, in, RecipeCategory.MISC, out, 0, 200, "drying", YHBlocks.RACK_RS.get());
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
								YHFood raw_cut, YHFood cooked_cut) {
		food(pvd, raw, cooked);
		food(pvd, raw_cut, cooked_cut);
		cutting(pvd, raw.item, raw_cut.item, 2);
	}

	public static <T> T unlock(RegistrateRecipeProvider pvd, BiFunction<String, InventoryChangeTrigger.TriggerInstance, T> func, Item item) {
		return func.apply("has_" + pvd.safeName(item), DataIngredient.items(item).getCritereon(pvd));
	}

}
