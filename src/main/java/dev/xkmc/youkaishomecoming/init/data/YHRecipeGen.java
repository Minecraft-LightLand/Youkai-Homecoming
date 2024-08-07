package dev.xkmc.youkaishomecoming.init.data;

import com.simibubi.create.Create;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.fruitsdelight.init.FruitsDelight;
import dev.xkmc.fruitsdelight.init.food.FDFood;
import dev.xkmc.fruitsdelight.init.food.FruitType;
import dev.xkmc.l2core.serial.ingredients.PotionIngredient;
import dev.xkmc.l2library.compat.patchouli.ShapelessPatchouliBuilder;
import dev.xkmc.l2library.serial.recipe.ConditionalRecipeWrapper;
import dev.xkmc.youkaishomecoming.compat.create.CreateRecipeGen;
import dev.xkmc.youkaishomecoming.compat.food.FruitsDelightCompatFood;
import dev.xkmc.youkaishomecoming.content.pot.base.BasePotFinishedRecipe;
import dev.xkmc.youkaishomecoming.content.pot.ferment.SimpleFermentationBuilder;
import dev.xkmc.youkaishomecoming.init.food.*;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import dev.xkmc.youkaishomecoming.init.registrate.YHDanmaku;
import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.recipes.*;
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
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.fml.ModList;
import net.neoforged.neoforge.common.Tags;
import vectorwing.farmersdelight.client.recipebook.CookingPotRecipeBookTab;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.tag.CommonTags;
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
			pvd.stonecutting(DataIngredient.items(Items.IRON_INGOT), RecipeCategory.MISC, YHItems.CAN);
			pvd.smelting(DataIngredient.items(YHItems.CAN.get()), RecipeCategory.MISC, Items.IRON_INGOT::asItem, 0.1f, 200);
			for (var e : YHBlocks.WoodType.values()) {
				pvd.stonecutting(DataIngredient.items(e.plank), RecipeCategory.MISC, e.fence);

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
			YHBlocks.HAY.genRecipe(pvd);
			YHBlocks.STRAW.genRecipe(pvd);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, YHItems.STRAW_HAT)::unlockedBy, ModItems.CANVAS.get())
					.pattern(" A ").pattern("ASA")
					.define('A', ModItems.CANVAS.get())
					.define('S', Items.STRING)
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

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, YHBlocks.SIKKUI.BASE)::unlockedBy, ModItems.STRAW.get())
					.pattern("ABA").pattern("DCD").pattern("ABA")
					.define('A', Items.CLAY_BALL)
					.define('B', Items.BONE_MEAL)
					.define('D', Items.PAPER)
					.define('C', ModItems.STRAW.get())
					.save(pvd);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, YHBlocks.FRAMED_SIKKUI.BASE)::unlockedBy, YHBlocks.SIKKUI.BASE.asItem())
					.pattern(" A ").pattern("ABA").pattern(" A ")
					.define('A', Items.STICK)
					.define('B', YHBlocks.SIKKUI.BASE)
					.save(pvd);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, YHBlocks.CROSS_SIKKUI.BASE)::unlockedBy, YHBlocks.FRAMED_SIKKUI.BASE.asItem())
					.pattern(" A ").pattern("ABA").pattern(" A ")
					.define('A', Items.STICK)
					.define('B', YHBlocks.FRAMED_SIKKUI.BASE)
					.save(pvd);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, YHBlocks.GRID_SIKKUI.BASE)::unlockedBy, YHBlocks.CROSS_SIKKUI.BASE.asItem())
					.pattern(" A ").pattern("ABA").pattern(" A ")
					.define('A', Items.STICK)
					.define('B', YHBlocks.CROSS_SIKKUI.BASE)
					.save(pvd);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, YHBlocks.FINE_GRID_SIKKUI)::unlockedBy, YHBlocks.GRID_SIKKUI.BASE.asItem())
					.pattern(" A ").pattern("ABA").pattern(" A ")
					.define('A', Items.STICK)
					.define('B', YHBlocks.GRID_SIKKUI.BASE)
					.save(pvd);

			YHBlocks.SIKKUI.genRecipe(pvd);
			YHBlocks.FRAMED_SIKKUI.genRecipe(pvd);
			YHBlocks.CROSS_SIKKUI.genRecipe(pvd);
			YHBlocks.GRID_SIKKUI.genRecipe(pvd);

			pvd.stonecutting(DataIngredient.items(YHBlocks.FINE_GRID_SIKKUI.get()), RecipeCategory.MISC, YHBlocks.FINE_GRID_SIKKUI_TD, 6);
			pvd.stonecutting(DataIngredient.items(YHBlocks.FINE_GRID_SIKKUI.get()), RecipeCategory.MISC, YHBlocks.FINE_GRID_SHOJI, 3);

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
			pvd.smelting(DataIngredient.items(YHItems.STRIPPED_MANDRAKE_ROOT.get()), RecipeCategory.FOOD, YHFood.COOKED_MANDRAKE_ROOT.item, 0.1f, 200);
			pvd.smoking(DataIngredient.items(YHItems.STRIPPED_MANDRAKE_ROOT.get()), RecipeCategory.FOOD, YHFood.COOKED_MANDRAKE_ROOT.item, 0.1f, 200);
			cutting(pvd, YHCrops.MANDRAKE.seed, YHItems.STRIPPED_MANDRAKE_ROOT, 1);
			drying(pvd, DataIngredient.items(YHCrops.MANDRAKE.getFruits()), YHItems.DRIED_MANDRAKE_FLOWER);
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
					.build(pvd, YHFood.ROE.item.getRegisteredName());

			CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(YHItems.COFFEE_BEAN),
							Ingredient.of(ItemTags.SHOVELS), YHItems.COFFEE_POWDER, 1)
					.build(pvd, YHItems.COFFEE_POWDER.getRegisteredName());

			CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(YHTea.GREEN.leaves),
							Ingredient.of(ItemTags.SHOVELS), YHItems.MATCHA, 1)
					.build(pvd, YHItems.MATCHA.getRegisteredName());

			CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.ICE),
							Ingredient.of(ItemTags.PICKAXES), YHItems.ICE_CUBE, 8)
					.build(pvd, YHItems.ICE_CUBE.getRegisteredName());

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
					.define('3', CommonTags.FOODS_ONION)
					.define('4', CommonTags.FOODS_CABBAGE)
					.save(pvd);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, YHItems.RED_VELVET.block, 1)::unlockedBy, YHFood.FLESH.item.get())
					.pattern("ABA").pattern("CDC").pattern("EEE")
					.define('A', Items.SUGAR)
					.define('B', Items.MILK_BUCKET)
					.define('C', YHItems.BLOOD_BOTTLE.item)
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


			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, YHFood.FLESH_CHOCOLATE_MOUSSE.item, 4)::unlockedBy, YHFood.FLESH.item.get())
					.pattern(" B ").pattern("FDF").pattern("ECE")
					.define('B', CommonTags.FOODS_MILK)
					.define('C', YHItems.BLOOD_BOTTLE.item)
					.define('D', YHFood.FLESH.item)
					.define('E', Items.WHEAT)
					.define('F', Items.COCOA_BEANS)
					.save(pvd);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, YHFood.SCARLET_DEVIL_CAKE.item, 4)::unlockedBy, YHFood.FLESH.item.get())
					.pattern("FBF").pattern("ADA").pattern("ECE")
					.define('A', Items.HONEY_BOTTLE)
					.define('B', CommonTags.FOODS_MILK)
					.define('C', YHItems.BLOOD_BOTTLE.item)
					.define('D', YHFood.FLESH.item)
					.define('E', Items.WHEAT)
					.define('F', Items.PINK_PETALS)
					.save(pvd);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, YHFood.FOODS_DOUGHNUT.item, 4)::unlockedBy, ModItems.WHEAT_DOUGH.get())
					.pattern("CAC").pattern("ABA").pattern("CAC")
					.define('A', CommonTags.FOODS_DOUGH)
					.define('B', YHItems.CREAM)
					.define('C', Items.SUGAR)
					.save(pvd);

			unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, YHFood.HIGI_CHOCOLATE.item, 3)::unlockedBy, Items.COCOA_BEANS)
					.requires(YHItems.MATCHA).requires(Items.TWISTING_VINES).requires(Items.PINK_PETALS)
					.requires(Items.HONEY_BOTTLE).requires(Items.BLAZE_POWDER).requires(Items.BLUE_ORCHID)
					.requires(Items.COCOA_BEANS, 3)
					.save(pvd);

			unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, YHFood.HIGI_DOUGHNUT.item, 1)::unlockedBy, YHFood.HIGI_CHOCOLATE.item.get())
					.requires(YHFood.FOODS_DOUGHNUT.item).requires(YHFood.HIGI_CHOCOLATE.item)
					.save(pvd);

		}

		// food cooking
		{

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.BUTTER.item.get(), 1, 200, 0.1f)
					.addIngredient(CommonTags.FOODS_MILK)
					.build(pvd, YHFood.BUTTER.item.getRegisteredName());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.TOFU.item.get(), 1, 200, 0.1f)
					.addIngredient(YHCrops.SOYBEAN.getSeed())
					.addIngredient(YHCrops.SOYBEAN.getSeed())
					.build(pvd, YHFood.TOFU.item.getRegisteredName());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.ONIGILI.item.get(), 1, 200, 0.1f)
					.addIngredient(CommonTags.CROPS_RICE)
					.addIngredient(Tags.Items.FOODS_VEGETABLE)
					.addIngredient(Items.KELP)
					.build(pvd, YHFood.ONIGILI.item.getRegisteredName());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.SEKIBANKIYAKI.item.get(), 1, 200, 0.1f)
					.addIngredient(CommonTags.CROPS_RICE)
					.addIngredient(YHCrops.REDBEAN.getSeed())
					.addIngredient(YHFood.BUTTER.item)
					.build(pvd, YHFood.SEKIBANKIYAKI.item.getRegisteredName());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.MOCHI.item.get(), 2, 200, 0.1f)
					.addIngredient(CommonTags.CROPS_RICE)
					.addIngredient(YHCrops.REDBEAN.getSeed())
					.build(pvd, YHFood.MOCHI.item.getRegisteredName());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.TSUKIMI_DANGO.item.get(), 2, 200, 0.1f)
					.addIngredient(CommonTags.CROPS_RICE)
					.addIngredient(YHCrops.SOYBEAN.getSeed())
					.build(pvd, YHFood.TSUKIMI_DANGO.item.getRegisteredName());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.YASHOUMA_DANGO.item.get(), 1, 200, 0.1f)
					.addIngredient(CommonTags.CROPS_RICE)
					.addIngredient(Items.PINK_DYE)
					.addIngredient(Items.GREEN_DYE)
					.build(pvd, YHFood.YASHOUMA_DANGO.item.getRegisteredName());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.SAKURA_MOCHI.item.get(), 2, 200, 0.1f)
					.addIngredient(CommonTags.CROPS_RICE)
					.addIngredient(YHCrops.REDBEAN.getSeed())
					.addIngredient(Items.CHERRY_LEAVES)
					.build(pvd, YHFood.SAKURA_MOCHI.item.getRegisteredName());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.COFFEE_MOCHI.item.get(), 2, 200, 0.1f)
					.addIngredient(CommonTags.CROPS_RICE)
					.addIngredient(YHItems.COFFEE_POWDER)
					.build(pvd, YHFood.COFFEE_MOCHI.item.getRegisteredName());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.MATCHA_MOCHI.item.get(), 2, 200, 0.1f)
					.addIngredient(CommonTags.CROPS_RICE)
					.addIngredient(YHTagGen.MATCHA)
					.build(pvd, YHFood.MATCHA_MOCHI.item.getRegisteredName());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.SENBEI.item.get(), 3, 200, 0.1f)
					.addIngredient(CommonTags.CROPS_RICE)
					.addIngredient(YHFood.BUTTER.item)
					.addIngredient(Items.KELP)
					.build(pvd, YHFood.SENBEI.item.getRegisteredName());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.YAKUMO_INARI.item.get(), 3, 200, 0.1f)
					.addIngredient(CommonTags.CROPS_RICE)
					.addIngredient(Tags.Items.EGGS)
					.addIngredient(Items.CARROT)
					.addIngredient(YHFood.OILY_BEAN_CURD.item.get())
					.addIngredient(YHFood.OILY_BEAN_CURD.item.get())
					.addIngredient(YHFood.OILY_BEAN_CURD.item.get())
					.build(pvd, YHFood.YAKUMO_INARI.item.getRegisteredName());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.KOISHI_MOUSSE.item.get(), 1, 200, 0.1f)
					.addIngredient(Items.CORNFLOWER)
					.addIngredient(Items.ALLIUM)
					.addIngredient(CommonTags.FOODS_DOUGH)
					.addIngredient(Items.HONEY_BOTTLE)
					.addIngredient(YHItems.CREAM.get())
					.build(pvd, YHFood.KOISHI_MOUSSE.item.getRegisteredName());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.BUN.item.get(), 3, 200, 0.1f)
					.addIngredient(CommonTags.FOODS_DOUGH)
					.addIngredient(ModTags.CABBAGE_ROLL_INGREDIENTS)
					.addIngredient(CommonTags.FOODS_CABBAGE)
					.addIngredient(CommonTags.FOODS_ONION)
					.addIngredient(YHCrops.SOYBEAN.getSeed())
					.build(pvd, YHFood.BUN.item.getRegisteredName());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.OYAKI.item.get(), 1, 200, 0.1f)
					.addIngredient(CommonTags.FOODS_DOUGH)
					.addIngredient(Tags.Items.FOODS_VEGETABLE)
					.addIngredient(Items.BROWN_MUSHROOM)
					.build(pvd, YHFood.OYAKI.item.getRegisteredName());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.PORK_RICE_BALL.item.get(), 1, 200, 0.1f)
					.addIngredient(CommonTags.CROPS_RICE)
					.addIngredient(CommonTags.FOODS_RAW_PORK)
					.build(pvd, YHFood.PORK_RICE_BALL.item.getRegisteredName());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.TUTU_CONGEE.item.get(), 1, 200, 0.1f)
					.addIngredient(CommonTags.CROPS_RICE)
					.addIngredient(Items.BAMBOO)
					.build(pvd, YHFood.TUTU_CONGEE.item.getRegisteredName());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.STEAMED_EGG_IN_BAMBOO.item.get(), 1, 200, 0.1f)
					.addIngredient(Tags.Items.EGGS)
					.addIngredient(Items.BAMBOO)
					.build(pvd, YHFood.STEAMED_EGG_IN_BAMBOO.item.getRegisteredName());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.CANDY_APPLE.item.get(), 1, 200, 0.1f, Items.STICK)
					.addIngredient(Items.APPLE)
					.addIngredient(Items.SUGAR)
					.addIngredient(Items.SUGAR)
					.addIngredient(Items.SUGAR)
					.build(pvd, YHFood.CANDY_APPLE.item.getRegisteredName());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.MITARASHI_DANGO.item.get(), 1, 200, 0.1f, Items.STICK)
					.addIngredient(YHTagGen.DANGO)
					.addIngredient(YHTagGen.DANGO)
					.addIngredient(YHTagGen.DANGO)
					.addIngredient(YHCrops.SOYBEAN.getSeed())
					.addIngredient(YHItems.SOY_SAUCE_BOTTLE.item)
					.addIngredient(Items.SUGAR)
					.build(pvd, YHFood.MITARASHI_DANGO.item.getRegisteredName());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.FLESH_DUMPLINGS.item.get(), 2, 200, 0.1f)
					.addIngredient(CommonTags.FOODS_DOUGH)
					.addIngredient(YHTagGen.RAW_FLESH)
					.addIngredient(Tags.Items.FOODS_VEGETABLE)
					.build(pvd, YHFood.FLESH_DUMPLINGS.item.getRegisteredName());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.CANNED_FLESH.item.get(), 2, 200, 0.1f, YHItems.CAN)
					.addIngredient(YHTagGen.RAW_FLESH)
					.addIngredient(CommonTags.FOODS_ONION)
					.addIngredient(YHItems.SOY_SAUCE_BOTTLE.item)
					.build(pvd, YHFood.CANNED_FLESH.item.getRegisteredName());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.FAIRY_CANDY.item.get(), 3, 200, 0.1f)
					.addIngredient(YHItems.FAIRY_ICE_CRYSTAL)
					.addIngredient(Items.SUGAR)
					.addIngredient(Items.HONEY_BOTTLE)
					.build(pvd, YHFood.FAIRY_CANDY.item.getRegisteredName());

		}

		// food cooking bowl
		{
			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.APAKI.item.get(), 1, 200, 0.1f, Items.BOWL)
					.addIngredient(CommonTags.FOODS_RAW_PORK)
					.addIngredient(CommonTags.FOODS_RAW_PORK)
					.addIngredient(Items.PINK_PETALS)
					.build(pvd, YHFood.APAKI.item.getRegisteredName());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.AVGOLEMONO.item.get(), 1, 200, 0.1f, Items.BOWL)
					.addIngredient(Tags.Items.EGGS)
					.addIngredient(Items.GLOW_BERRIES)
					.addIngredient(Items.GLOW_BERRIES)
					.build(pvd, YHFood.AVGOLEMONO.item.getRegisteredName());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.BLAZING_RED_CURRY.item.get(), 1, 200, 0.1f, Items.BOWL)
					.addIngredient(CommonTags.CROPS_RICE)
					.addIngredient(Items.CRIMSON_FUNGUS)
					.addIngredient(Items.CRIMSON_FUNGUS)
					.addIngredient(Items.BLAZE_POWDER)
					.addIngredient(Tags.Items.CROPS_POTATO)
					.addIngredient(CommonTags.FOODS_RAW_CHICKEN)
					.build(pvd, YHFood.BLAZING_RED_CURRY.item.getRegisteredName());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.GRILLED_EEL_OVER_RICE.item.get(), 1, 200, 0.1f, Items.BOWL)
					.addIngredient(YHTagGen.RAW_EEL)
					.addIngredient(YHTagGen.RAW_EEL)
					.addIngredient(CommonTags.CROPS_RICE)
					.addIngredient(YHItems.SOY_SAUCE_BOTTLE.item)
					.build(pvd, YHFood.GRILLED_EEL_OVER_RICE.item.getRegisteredName());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.HIGAN_SOUP.item.get(), 1, 200, 0.1f, Items.BOWL)
					.addIngredient(Items.SOUL_SAND)
					.addIngredient(Items.SOUL_SAND)
					.addIngredient(Tags.Items.FOODS_VEGETABLE)
					.build(pvd, YHFood.HIGAN_SOUP.item.getRegisteredName());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.LONGEVITY_NOODLES.item.get(), 1, 200, 0.1f, Items.BOWL)
					.addIngredient(CommonTags.FOODS_PASTA)
					.addIngredient(Tags.Items.FOODS_VEGETABLE)
					.addIngredient(Items.BROWN_MUSHROOM)
					.addIngredient(CommonTags.FOODS_RAW_PORK)
					.build(pvd, YHFood.LONGEVITY_NOODLES.item.getRegisteredName());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.MISO_SOUP.item.get(), 1, 200, 0.1f, Items.BOWL)
					.addIngredient(YHFood.TOFU.item.get())
					.addIngredient(YHCrops.SOYBEAN.getSeed())
					.addIngredient(Items.DRIED_KELP)
					.addIngredient(Items.BROWN_MUSHROOM)
					.build(pvd, YHFood.MISO_SOUP.item.getRegisteredName());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.SEAFOOD_MISO_SOUP.item.get(), 1, 200, 0.1f, Items.BOWL)
					.addIngredient(YHFood.TOFU.item.get())
					.addIngredient(YHCrops.SOYBEAN.getSeed())
					.addIngredient(Items.DRIED_KELP)
					.addIngredient(Items.BROWN_MUSHROOM)
					.addIngredient(CommonTags.FOODS_RAW_SALMON)
					.addIngredient(CommonTags.FOODS_RAW_SALMON)
					.build(pvd, YHFood.SEAFOOD_MISO_SOUP.item.getRegisteredName());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.POOR_GOD_SOUP.item.get(), 1, 200, 0.1f, Items.BOWL)
					.addIngredient(Tags.Items.SEEDS)
					.addIngredient(Tags.Items.CROPS)
					.addIngredient(ItemTags.FLOWERS)
					.build(pvd, YHFood.POOR_GOD_SOUP.item.getRegisteredName());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.POWER_SOUP.item.get(), 1, 200, 0.1f, Items.BOWL)
					.addIngredient(CommonTags.FOODS_RAW_PORK)
					.addIngredient(CommonTags.FOODS_RAW_PORK)
					.addIngredient(Items.KELP)
					.addIngredient(Items.KELP)
					.addIngredient(CommonTags.FOODS_ONION)
					.build(pvd, YHFood.POWER_SOUP.item.getRegisteredName());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.SHIRAYUKI.item.get(), 1, 200, 0.1f, Items.BOWL)
					.addIngredient(Items.PUFFERFISH)
					.addIngredient(YHTagGen.RAW_EEL)
					.addIngredient(Items.KELP)
					.addIngredient(YHFood.TOFU.item.get())
					.addIngredient(Tags.Items.FOODS_VEGETABLE)
					.build(pvd, YHFood.SHIRAYUKI.item.getRegisteredName());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.SWEET_ORMOSIA_MOCHI_MIXED_BOILED.item.get(), 1, 200, 0.1f, Items.BOWL)
					.addIngredient(YHTagGen.DANGO)
					.addIngredient(Items.CARROT)
					.addIngredient(Tags.Items.FOODS_VEGETABLE)
					.build(pvd, YHFood.SWEET_ORMOSIA_MOCHI_MIXED_BOILED.item.getRegisteredName());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.FLESH_STEW.item.get(), 1, 200, 0.1f, Items.BOWL)
					.addIngredient(YHTagGen.RAW_FLESH)
					.addIngredient(YHTagGen.RAW_FLESH)
					.addIngredient(YHTagGen.RAW_EEL)
					.addIngredient(Tags.Items.FOODS_VEGETABLE)
					.addIngredient(Tags.Items.FOODS_VEGETABLE)
					.addIngredient(YHItems.SOY_SAUCE_BOTTLE.item)
					.build(pvd, YHFood.FLESH_STEW.item.getRegisteredName());

			CookingPotRecipeBuilder.cookingPotRecipe(YHItems.FLESH_FEAST.get(), 1, 200, 0.1f, Items.BOWL)
					.addIngredient(YHItems.RAW_FLESH_FEAST)
					.addIngredient(YHTagGen.RAW_FLESH)
					.addIngredient(YHItems.BLOOD_BOTTLE.item)
					.addIngredient(YHItems.BLOOD_BOTTLE.item)
					.addIngredient(YHItems.SOY_SAUCE_BOTTLE.item)
					.build(pvd, YHItems.FLESH_FEAST.getRegisteredName());

			CookingPotRecipeBuilder.cookingPotRecipe(YHItems.CREAM.get(), 1, 200, 0.1f, Items.BOWL)
					.addIngredient(CommonTags.FOODS_MILK)
					.addIngredient(CommonTags.FOODS_MILK)
					.addIngredient(CommonTags.FOODS_MILK)
					.build(pvd, YHItems.CREAM.getRegisteredName());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.TUSCAN_SALMON.item.get(), 1, 200, 0.1f, Items.BOWL)
					.addIngredient(CommonTags.FOODS_RAW_SALMON)
					.addIngredient(CommonTags.FOODS_TOMATO)
					.addIngredient(CommonTags.FOODS_CABBAGE)
					.addIngredient(YHItems.CREAM.get())
					.build(pvd, YHFood.TUSCAN_SALMON.item.getRegisteredName());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.MUSHROOM_SOUP.item.get(), 1, 200, 0.1f, Items.BOWL)
					.addIngredient(Items.BROWN_MUSHROOM)
					.addIngredient(Items.BROWN_MUSHROOM)
					.addIngredient(Items.BROWN_MUSHROOM)
					.addIngredient(CommonTags.FOODS_ONION)
					.addIngredient(YHItems.CREAM.get())
					.build(pvd, YHFood.MUSHROOM_SOUP.item.getRegisteredName());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.LIONS_HEAD.item.get(), 1, 200, 0.1f, Items.BOWL)
					.addIngredient(CommonTags.FOODS_RAW_PORK)
					.addIngredient(CommonTags.FOODS_RAW_PORK)
					.addIngredient(CommonTags.VEGETABLES_CARROT)
					.addIngredient(CommonTags.SALAD_INGREDIENTS_CABBAGE)
					.build(pvd, YHFood.LIONS_HEAD.item.getRegisteredName());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.MAPO_TOFU.item.get(), 1, 200, 0.1f, Items.BOWL)
					.addIngredient(YHFood.TOFU.item)
					.addIngredient(YHFood.TOFU.item)
					.addIngredient(YHFood.TOFU.item)
					.addIngredient(CommonTags.FOODS_RAW_PORK)
					.addIngredient(Items.BLAZE_POWDER)
					.build(pvd, YHFood.MAPO_TOFU.item.getRegisteredName());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.UDUMBARA_CAKE.item.get(), 1, 200, 0.1f, Items.BOWL)
					.addIngredient(CommonTags.FOODS_DOUGH_WHEAT)
					.addIngredient(YHCrops.UDUMBARA.getFruits())
					.build(pvd, YHFood.UDUMBARA_CAKE.item.getRegisteredName());


			CookingPotRecipeBuilder.cookingPotRecipe(YHItems.SURP_CHEST.get(), 1, 200, 0.1f, Items.CHEST)
					.addIngredient(Items.RED_MUSHROOM)
					.addIngredient(Items.RED_MUSHROOM)
					.addIngredient(Items.HONEY_BOTTLE)
					.addIngredient(YHItems.CREAM.get())
					.addIngredient(YHCrops.UDUMBARA.getFruits())
					.addIngredient(Items.PURPLE_BANNER)
					.build(pvd, YHItems.SURP_CHEST.getRegisteredName());
		}

		// food cooking saucer
		{
			CookingPotRecipeBuilder.cookingPotRecipe(YHDish.BAMBOO_MIZUYOKAN.block.get(), 1, 200, 0.1f, YHItems.SAUCER.get())
					.addIngredient(YHCrops.REDBEAN.getSeed())
					.addIngredient(PotionIngredient.of(Potions.WATER))
					.addIngredient(Items.BAMBOO)
					.addIngredient(Items.SUGAR)
					.build(pvd, YHDish.BAMBOO_MIZUYOKAN.block.getRegisteredName());

			CookingPotRecipeBuilder.cookingPotRecipe(YHDish.DRIED_FISH.block.get(), 1, 200, 0.1f, YHItems.SAUCER.get())
					.addIngredient(CommonTags.FOODS_SAFE_RAW_FISH)
					.addIngredient(CommonTags.FOODS_SAFE_RAW_FISH)
					.addIngredient(CommonTags.FOODS_SAFE_RAW_FISH)
					.build(pvd, YHDish.DRIED_FISH.block.getRegisteredName());

			CookingPotRecipeBuilder.cookingPotRecipe(YHDish.IMITATION_BEAR_PAW.block.get(), 1, 200, 0.1f, YHItems.SAUCER.get())
					.addIngredient(Items.PUFFERFISH)
					.addIngredient(Items.BAMBOO)
					.addIngredient(CommonTags.FOODS_RAW_PORK)
					.addIngredient(CommonTags.FOODS_ONION)
					.addIngredient(YHTagGen.RAW_EEL)
					.addIngredient(YHItems.SOY_SAUCE_BOTTLE.item)
					.build(pvd, YHDish.IMITATION_BEAR_PAW.block.getRegisteredName());

			CookingPotRecipeBuilder.cookingPotRecipe(YHDish.PASTITSIO.block.get(), 1, 200, 0.1f, YHItems.SAUCER.get())
					.addIngredient(CommonTags.FOODS_PASTA)
					.addIngredient(YHFood.BUTTER.item)
					.addIngredient(ModItems.TOMATO_SAUCE.get())
					.addIngredient(CommonTags.FOODS_RAW_BEEF)
					.addIngredient(CommonTags.FOODS_ONION)
					.build(pvd, YHDish.PASTITSIO.block.getRegisteredName());

			CookingPotRecipeBuilder.cookingPotRecipe(YHDish.SAUCE_GRILLED_FISH.block.get(), 1, 200, 0.1f, YHItems.SAUCER.get())
					.addIngredient(CommonTags.FOODS_SAFE_RAW_FISH)
					.addIngredient(YHItems.SOY_SAUCE_BOTTLE.item)
					.addIngredient(Tags.Items.FOODS_VEGETABLE)
					.addIngredient(Tags.Items.FOODS_VEGETABLE)
					.addIngredient(CommonTags.FOODS_ONION)
					.build(pvd, YHDish.SAUCE_GRILLED_FISH.block.getRegisteredName());

			CookingPotRecipeBuilder.cookingPotRecipe(YHDish.STINKY_TOFU.block.get(), 1, 200, 0.1f, YHItems.SAUCER.get())
					.addIngredient(YHFood.TOFU.item)
					.addIngredient(YHFood.TOFU.item)
					.addIngredient(Items.BROWN_MUSHROOM)
					.build(pvd, YHDish.STINKY_TOFU.block.getRegisteredName());

			CookingPotRecipeBuilder.cookingPotRecipe(YHDish.TOFU_BURGER.block.get(), 1, 200, 0.1f, YHItems.SAUCER.get())
					.addIngredient(YHFood.TOFU.item)
					.addIngredient(YHFood.BUTTER.item)
					.addIngredient(Items.SWEET_BERRIES)
					.build(pvd, YHDish.TOFU_BURGER.block.getRegisteredName());

			CookingPotRecipeBuilder.cookingPotRecipe(YHDish.BLOOD_CURD.block.get(), 1, 200, 0.1f, YHItems.SAUCER.get())
					.addIngredient(YHItems.BLOOD_BOTTLE.item)
					.addIngredient(YHItems.BLOOD_BOTTLE.item)
					.addIngredient(Tags.Items.FOODS_VEGETABLE)
					.build(pvd, YHDish.BLOOD_CURD.block.getRegisteredName());

			CookingPotRecipeBuilder.cookingPotRecipe(YHDish.SEVEN_COLORED_YOKAN.block.get(), 1, 200, 0.1f, YHItems.SAUCER.get())
					.addIngredient(YHCrops.REDBEAN.getSeed())
					.addIngredient(YHCrops.SOYBEAN.getSeed())
					.addIngredient(Items.CHERRY_LEAVES)
					.addIngredient(Items.CHORUS_FRUIT)
					.addIngredient(YHTagGen.MATCHA)
					.addIngredient(YHCrops.UDUMBARA.getFruits())
					.build(pvd, YHDish.SEVEN_COLORED_YOKAN.block.getRegisteredName());

		}

		var tea = tea(pvd);
		var coffee = coffee(pvd);

		// drinks
		{

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.BLACK_TEA.item.get(), 1, 200, 0.1f, Items.GLASS_BOTTLE)
					.setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
					.unlockedByAnyIngredient(YHTea.BLACK.leaves)
					.addIngredient(YHTagGen.TEA_BLACK)
					.addIngredient(YHTagGen.TEA_BLACK)
					.build(tea, YHFood.BLACK_TEA.item.getRegisteredName());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.GREEN_TEA.item.get(), 1, 200, 0.1f, Items.GLASS_BOTTLE)
					.setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
					.unlockedByAnyIngredient(YHTea.GREEN.leaves)
					.addIngredient(YHTagGen.TEA_GREEN)
					.addIngredient(YHTagGen.TEA_GREEN)
					.build(tea, YHFood.GREEN_TEA.item.getRegisteredName());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.OOLONG_TEA.item.get(), 1, 200, 0.1f, Items.GLASS_BOTTLE)
					.setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
					.unlockedByAnyIngredient(YHTea.OOLONG.leaves)
					.addIngredient(YHTagGen.TEA_OOLONG)
					.addIngredient(YHTagGen.TEA_OOLONG)
					.build(tea, YHFood.OOLONG_TEA.item.getRegisteredName());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.WHITE_TEA.item.get(), 1, 200, 0.1f, Items.GLASS_BOTTLE)
					.setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
					.unlockedByAnyIngredient(YHTea.WHITE.leaves)
					.addIngredient(YHTagGen.TEA_WHITE)
					.addIngredient(YHTagGen.TEA_WHITE)
					.build(tea, YHFood.WHITE_TEA.item.getRegisteredName());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.CORNFLOWER_TEA.item.get(), 1, 200, 0.1f, Items.GLASS_BOTTLE)
					.setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
					.unlockedByAnyIngredient(Items.CORNFLOWER)
					.addIngredient(Items.CORNFLOWER)
					.addIngredient(Items.CORNFLOWER)
					.build(tea, YHFood.CORNFLOWER_TEA.item.getRegisteredName());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.TEA_MOCHA.item.get(), 1, 200, 0.1f, Items.GLASS_BOTTLE)
					.setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
					.unlockedByAnyIngredient(YHTea.BLACK.leaves)
					.addIngredient(YHTagGen.TEA_BLACK)
					.addIngredient(Items.COCOA_BEANS)
					.addIngredient(CommonTags.MILK_BOTTLE)
					.build(tea, YHFood.TEA_MOCHA.item.getRegisteredName());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.SAIDI_TEA.item.get(), 1, 200, 0.1f, Items.GLASS_BOTTLE)
					.setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
					.unlockedByAnyIngredient(YHTea.BLACK.leaves)
					.addIngredient(YHTagGen.TEA_BLACK)
					.addIngredient(Items.SUGAR)
					.addIngredient(Items.SUGAR)
					.build(tea, YHFood.SAIDI_TEA.item.getRegisteredName());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.SAKURA_HONEY_TEA.item.get(), 1, 200, 0.1f, Items.GLASS_BOTTLE)
					.setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
					.unlockedByAnyIngredient(Items.CHERRY_LEAVES)
					.addIngredient(Items.CHERRY_LEAVES)
					.addIngredient(Items.HONEY_BOTTLE)
					.build(tea, YHFood.SAKURA_HONEY_TEA.item.getRegisteredName());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.GENMAI_TEA.item.get(), 1, 200, 0.1f, Items.GLASS_BOTTLE)
					.setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
					.unlockedByAnyIngredient(YHTea.GREEN.leaves)
					.addIngredient(YHTagGen.TEA_GREEN)
					.addIngredient(YHTagGen.TEA_GREEN)
					.addIngredient(CommonTags.CROPS_RICE)
					.build(tea, YHFood.GENMAI_TEA.item.getRegisteredName());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.SCARLET_TEA.item.get(), 1, 200, 0.1f, Items.GLASS_BOTTLE)
					.setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
					.unlockedByAnyIngredient(YHItems.BLOOD_BOTTLE.item)
					.addIngredient(YHTagGen.TEA_BLACK)
					.addIngredient(YHItems.BLOOD_BOTTLE.item)
					.build(tea, YHFood.SCARLET_TEA.item.getRegisteredName());

			CookingPotRecipeBuilder.cookingPotRecipe(YHFood.GREEN_WATER.item.get(), 1, 200, 0.1f, Items.GLASS_BOTTLE)
					.setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
					.unlockedByAnyIngredient(Items.GLASS_BOTTLE)
					.addIngredient(CommonTags.FOODS_CABBAGE)
					.addIngredient(CommonTags.FOODS_CABBAGE)
					.build(tea, YHFood.GREEN_WATER.item.getRegisteredName());

			CookingPotRecipeBuilder.cookingPotRecipe(YHCoffee.ESPRESSO.item.get(), 1, 200, 0.1f, Items.GLASS_BOTTLE)
					.setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
					.unlockedByAnyIngredient(YHItems.COFFEE_POWDER)
					.addIngredient(YHItems.COFFEE_POWDER)
					.addIngredient(PotionIngredient.of(Potions.WATER))
					.build(coffee, YHCoffee.ESPRESSO.item.getRegisteredName());

			coffee(coffee, YHCoffee.RISTRETTO, e -> e.addIngredient(YHItems.COFFEE_POWDER));
			coffee(coffee, YHCoffee.AMERICANO, e -> e.addIngredient(PotionIngredient.of(Potions.WATER)));
			coffee(coffee, YHCoffee.LATTE, e -> e.addIngredient(CommonTags.FOODS_MILK));
			coffee(coffee, YHCoffee.MOCHA, e -> e
					.addIngredient(CommonTags.FOODS_MILK)
					.addIngredient(Items.COCOA_BEANS));
			coffee(coffee, YHCoffee.CAPPUCCINO, e -> e
					.addIngredient(CommonTags.FOODS_MILK)
					.addIngredient(YHItems.CREAM));
			coffee(coffee, YHCoffee.MACCHIATO, e -> e.addIngredient(YHItems.CREAM));
			coffee(coffee, YHCoffee.CON_PANNA, e -> e
					.addIngredient(YHItems.COFFEE_POWDER)
					.addIngredient(YHItems.CREAM));
			coffee(coffee, YHCoffee.AFFOGATO, e -> e
					.addIngredient(YHItems.ICE_CUBE)
					.addIngredient(YHItems.CREAM));

			unlock(pvd, ShapelessPatchouliBuilder.shapeless(RecipeCategory.FOOD, YHCoffee.AFFOGATO.item.get(), 1)::unlockedBy,
					YHCoffee.ESPRESSO.item.get()).requires(YHCoffee.ESPRESSO.item)
					.requires(YHItems.ICE_CUBE).requires(YHItems.CREAM)
					.save(coffee, YHCoffee.AFFOGATO.item.getRegisteredName().withSuffix("_craft"));

		}

		// sake
		{

			unlock(pvd, new SimpleFermentationBuilder(Fluids.WATER, YHItems.SOY_SAUCE_BOTTLE.fluid.get(), 1800)::unlockedBy, YHCrops.SOYBEAN.getSeed())
					.addInput(YHCrops.SOYBEAN.getSeed()).addInput(YHCrops.SOYBEAN.getSeed())
					.addInput(YHCrops.SOYBEAN.getSeed()).addInput(YHCrops.SOYBEAN.getSeed())
					.save(pvd, YHItems.SOY_SAUCE_BOTTLE.item.getRegisteredName());

			unlock(pvd, new SimpleFermentationBuilder(Fluids.WATER, YHSake.MIO.fluid.get(), 2400)::unlockedBy, ModItems.RICE.get())
					.addInput(CommonTags.CROPS_RICE).addInput(CommonTags.CROPS_RICE).addInput(CommonTags.CROPS_RICE).addInput(CommonTags.CROPS_RICE)
					.save(pvd, YHSake.MIO.item.getRegisteredName());

			unlock(pvd, new SimpleFermentationBuilder(Fluids.WATER, YHSake.MEAD.fluid.get(), 2400)::unlockedBy, ModItems.RICE.get())
					.addInput(CommonTags.CROPS_RICE).addInput(CommonTags.CROPS_RICE).addInput(CommonTags.CROPS_RICE).addInput(CommonTags.CROPS_RICE)
					.addInput(Items.HONEY_BOTTLE)
					.save(pvd, YHSake.MEAD.item.getRegisteredName());

			unlock(pvd, new SimpleFermentationBuilder(Fluids.WATER, YHSake.DAIGINJO.fluid.get(), 2400)::unlockedBy, ModItems.RICE.get())
					.addInput(CommonTags.CROPS_RICE).addInput(CommonTags.CROPS_RICE).addInput(CommonTags.CROPS_RICE)
					.addInput(Items.NETHER_WART).addInput(Items.BLAZE_POWDER)
					.save(pvd, YHSake.DAIGINJO.item.getRegisteredName());

			unlock(pvd, new SimpleFermentationBuilder(Fluids.WATER, YHSake.DASSAI.fluid.get(), 2400)::unlockedBy, ModItems.RICE.get())
					.addInput(CommonTags.CROPS_RICE).addInput(CommonTags.CROPS_RICE).addInput(CommonTags.CROPS_RICE)
					.addInput(Items.NETHER_WART).addInput(Items.NAUTILUS_SHELL)
					.save(pvd, YHSake.DASSAI.item.getRegisteredName());

			unlock(pvd, new SimpleFermentationBuilder(Fluids.WATER, YHSake.TENGU_TANGO.fluid.get(), 2400)::unlockedBy, ModItems.RICE.get())
					.addInput(CommonTags.CROPS_RICE).addInput(CommonTags.CROPS_RICE).addInput(CommonTags.CROPS_RICE)
					.addInput(Items.NETHER_WART).addInput(Items.PHANTOM_MEMBRANE)
					.save(pvd, YHSake.TENGU_TANGO.item.getRegisteredName());

			unlock(pvd, new SimpleFermentationBuilder(Fluids.WATER, YHSake.SPARROW_SAKE.fluid.get(), 2400)::unlockedBy, ModItems.RICE.get())
					.addInput(CommonTags.CROPS_RICE).addInput(CommonTags.CROPS_RICE).addInput(CommonTags.CROPS_RICE)
					.addInput(Items.FEATHER).addInput(Items.RABBIT_FOOT)
					.save(pvd, YHSake.SPARROW_SAKE.item.getRegisteredName());

			unlock(pvd, new SimpleFermentationBuilder(Fluids.WATER, YHSake.FULL_MOONS_EVE.fluid.get(), 2400)::unlockedBy, ModItems.RICE.get())
					.addInput(CommonTags.CROPS_RICE).addInput(CommonTags.CROPS_RICE).addInput(CommonTags.CROPS_RICE)
					.addInput(Items.NETHER_WART).addInput(YHCrops.UDUMBARA.getFruits())
					.save(pvd, YHSake.FULL_MOONS_EVE.item.getRegisteredName());

			unlock(pvd, new SimpleFermentationBuilder(YHItems.BLOOD_BOTTLE.fluid.get(), YHSake.SCARLET_MIST.fluid.get(), 3600)::unlockedBy, ModItems.RICE.get())
					.addInput(Items.ROSE_BUSH).addInput(Items.ROSE_BUSH)
					.addInput(Items.POPPY)
					.addInput(YHDanmaku.Bullet.CIRCLE.get(DyeColor.RED))
					.addInput(YHDanmaku.Bullet.CIRCLE.get(DyeColor.RED))
					.save(pvd, YHSake.SCARLET_MIST.item.getRegisteredName());

			unlock(pvd, new SimpleFermentationBuilder(Fluids.WATER, YHSake.WIND_PRIESTESSES.fluid.get(), 3600)::unlockedBy, ModItems.RICE.get())
					.addInput(CommonTags.CROPS_RICE).addInput(CommonTags.CROPS_RICE).addInput(CommonTags.CROPS_RICE)
					.addInput(YHDanmaku.Bullet.CIRCLE.get(DyeColor.LIME))
					.addInput(Items.DANDELION).addInput(YHTagGen.TEA_GREEN).addInput(YHItems.MATCHA)
					.save(pvd, YHSake.WIND_PRIESTESSES.item.getRegisteredName());

		}

		if (ModList.get().isLoaded(FruitsDelight.MODID)) {
			Consumer<FinishedRecipe> modtea = e -> tea.accept(new ConditionalRecipeWrapper(e, new ModLoadedCondition(FruitsDelight.MODID)));
			CookingPotRecipeBuilder.cookingPotRecipe(FruitsDelightCompatFood.MOON_ROCKET.item.get(), 1, 200, 0.1f, Items.GLASS_BOTTLE)
					.setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
					.unlockedByAnyIngredient(FDFood.LEMON_SLICE.get())
					.addIngredient(FDFood.LEMON_SLICE.get())
					.addIngredient(Items.SUGAR)
					.build(modtea, FruitsDelightCompatFood.MOON_ROCKET.item.getRegisteredName());

			CookingPotRecipeBuilder.cookingPotRecipe(FruitsDelightCompatFood.LEMON_BLACK_TEA.item.get(), 1, 200, 0.1f, Items.GLASS_BOTTLE)
					.setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
					.unlockedByAnyIngredient(FDFood.LEMON_SLICE.get())
					.addIngredient(FDFood.LEMON_SLICE.get())
					.addIngredient(YHTagGen.TEA_BLACK)
					.addIngredient(Items.SUGAR)
					.build(modtea, FruitsDelightCompatFood.LEMON_BLACK_TEA.item.getRegisteredName());

			CookingPotRecipeBuilder.cookingPotRecipe(FruitsDelightCompatFood.PEACH_TAPIOCA.item.get(), 1, 200, 0.1f, Items.BOWL)
					.addIngredient(FruitType.PEACH.getFruitTag())
					.addIngredient(Items.LILY_PAD)
					.build(ConditionalRecipeWrapper.mod(pvd, FruitsDelight.MODID), FruitsDelightCompatFood.PEACH_TAPIOCA.item.getRegisteredName());


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
						Ingredient.of(CommonTags.TOOLS_KNIFE), out, count, 1)
				.build(pvd, in.getId().withSuffix("_cutting"));
	}

	private static void coffee(RecipeOutput cons, YHCoffee coffee, UnaryOperator<CookingPotRecipeBuilder> func) {
		func.apply(CookingPotRecipeBuilder.cookingPotRecipe(coffee.item.get(), 1, 200, 0.1f, Items.GLASS_BOTTLE)
						.setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
						.unlockedByAnyIngredient(YHItems.COFFEE_POWDER)
						.addIngredient(YHItems.COFFEE_POWDER)
						.addIngredient(PotionIngredient.of(Potions.WATER)))
				.build(cons, coffee.item.getRegisteredName());

		func.apply(CookingPotRecipeBuilder.cookingPotRecipe(coffee.item.get(), 1, 200, 0.1f, Items.GLASS_BOTTLE)
						.setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
						.unlockedByAnyIngredient(YHCoffee.ESPRESSO.item)
						.addIngredient(YHCoffee.ESPRESSO.item))
				.build(cons, coffee.item.getId().withSuffix("_remix").toString());
	}

	private static void cake(RegistrateRecipeProvider pvd, CakeEntry cake) {
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(cake.block.get()),
						Ingredient.of(CommonTags.TOOLS_KNIVES), cake.item.get(), cake.isCake ? 7 : 4)
				.build(pvd, cake.item.getRegisteredName());
		if (cake.isCake) {
			unlock(pvd, new ShapelessRecipeBuilder(RecipeCategory.FOOD, cake.block.get(), 1)::unlockedBy, cake.item.get())
					.requires(cake.item.get(), 7)
					.save(pvd, cake.block.getRegisteredName().withSuffix("_assemble"));
		} else {
			unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.FOOD, cake.block.get(), 1)::unlockedBy, cake.item.get())
					.pattern("AA").pattern("AA")
					.define('A', cake.item.get())
					.save(pvd, cake.block.getRegisteredName().withSuffix("_assemble"));
		}
	}

	private static void drying(RegistrateRecipeProvider pvd, DataIngredient in, Supplier<Item> out) {
		cooking(pvd, in, RecipeCategory.MISC, out, 0, 200, "drying", YHBlocks.RACK_RS.get());
	}

	public static <T extends ItemLike> void cooking(RegistrateRecipeProvider pvd, DataIngredient source, RecipeCategory category, Supplier<? extends T> result, float experience, int cookingTime, String typeName, RecipeSerializer<? extends AbstractCookingRecipe> serializer) {
		new SimpleCookingRecipeBuilder(category, CookingBookCategory.MISC, result.get(), source, experience, cookingTime, serializer)
				.unlockedBy("has_" + pvd.safeName(source), source.getCriterion(pvd))
				.save(pvd, pvd.safeId(result.get()) + "_from_" + pvd.safeName(source) + "_" + typeName);
	}

	private static RecipeOutput tea(RegistrateRecipeProvider pvd) {
		return (id, e, adv) -> pvd.accept(id, new BasePotFinishedRecipe(YHBlocks.KETTLE_RS.get(), e), adv);
	}

	private static RecipeOutput coffee(RegistrateRecipeProvider pvd) {
		return (id, e, adv) -> pvd.accept(id, new BasePotFinishedRecipe(YHBlocks.MOKA_RS.get(), e), adv);
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
