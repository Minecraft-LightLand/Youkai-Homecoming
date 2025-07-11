package dev.xkmc.youkaishomecoming.init.data;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.l2library.compat.patchouli.ShapelessPatchouliBuilder;
import dev.xkmc.l2library.serial.ingredients.PotionIngredient;
import dev.xkmc.youkaishomecoming.content.pot.base.BasePotFinishedRecipe;
import dev.xkmc.youkaishomecoming.init.food.CoffeeCrops;
import dev.xkmc.youkaishomecoming.init.food.CoffeeDrinks;
import dev.xkmc.youkaishomecoming.init.registrate.CoffeeBlocks;
import dev.xkmc.youkaishomecoming.init.registrate.CoffeeItems;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import vectorwing.farmersdelight.client.recipebook.CookingPotRecipeBookTab;
import vectorwing.farmersdelight.data.builder.CookingPotRecipeBuilder;
import vectorwing.farmersdelight.data.builder.CuttingBoardRecipeBuilder;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class YHRecipeGen {

	public static void genRecipes(RegistrateRecipeProvider pvd) {

		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.ICE),
						Ingredient.of(TagRef.TOOLS_PICKAXES), CoffeeItems.ICE_CUBE, 8)
				.build(pvd, CoffeeItems.ICE_CUBE.getId());

		CookingPotRecipeBuilder.cookingPotRecipe(CoffeeItems.CREAM.get(), 1, 200, 0.1f, Items.BOWL)
				.setRecipeBookTab(CookingPotRecipeBookTab.MISC)
				.addIngredient(TagRef.MILK_BOTTLE)
				.addIngredient(TagRef.MILK_BOTTLE)
				.addIngredient(TagRef.MILK_BOTTLE)
				.build(pvd, CoffeeItems.CREAM.getId());

		unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CoffeeBlocks.MOKA)::unlockedBy, CoffeeItems.COFFEE_POWDER.get())
				.pattern("ABA").pattern("IWI").pattern("ADA")
				.define('A', Items.IRON_NUGGET)
				.define('I', Items.IRON_INGOT)
				.define('B', CoffeeItems.COFFEE_POWDER)
				.define('D', Items.DEEPSLATE)
				.define('W', new PotionIngredient(Potions.WATER))
				.save(pvd);

		unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CoffeeBlocks.MOKA_KIT)::unlockedBy, Items.IRON_INGOT)
				.pattern("ABA").pattern("I I").pattern("EDE")
				.define('A', Items.IRON_NUGGET)
				.define('I', Items.IRON_INGOT)
				.define('B', Items.BLACK_DYE)
				.define('D', Items.DEEPSLATE)
				.define('E', Items.TERRACOTTA)
				.save(pvd);

		cutting(pvd, CoffeeCrops.COFFEA.fruits, CoffeeCrops.COFFEA.seed, 2);
		pvd.singleItem(DataIngredient.items(CoffeeCrops.COFFEA.fruits.get()), RecipeCategory.MISC, CoffeeCrops.COFFEA.seed, 1, 1);
		pvd.smelting(DataIngredient.items(CoffeeCrops.COFFEA.getSeed()), RecipeCategory.MISC, CoffeeItems.COFFEE_BEAN, 0.1f, 200);
		pvd.smoking(DataIngredient.items(CoffeeCrops.COFFEA.getSeed()), RecipeCategory.MISC, CoffeeItems.COFFEE_BEAN, 0.1f, 200);
		pvd.storage(CoffeeItems.COFFEE_BEAN, RecipeCategory.MISC, CoffeeItems.COFFEE_BEAN_BAG);


		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(CoffeeItems.COFFEE_BEAN),
						Ingredient.of(TagRef.TOOLS_SHOVELS), CoffeeItems.COFFEE_POWDER, 1)
				.build(pvd, CoffeeItems.COFFEE_POWDER.getId());

		var coffee = coffee(pvd);

		CookingPotRecipeBuilder.cookingPotRecipe(CoffeeDrinks.ESPRESSO.item.get(), 1, 200, 0.1f, Items.GLASS_BOTTLE)
				.setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
				.unlockedByAnyIngredient(CoffeeItems.COFFEE_POWDER)
				.addIngredient(CoffeeItems.COFFEE_POWDER)
				.addIngredient(new PotionIngredient(Potions.WATER))
				.build(coffee, CoffeeDrinks.ESPRESSO.item.getId());

		coffee(coffee, CoffeeDrinks.RISTRETTO, e -> e.addIngredient(CoffeeItems.COFFEE_POWDER));
		coffee(coffee, CoffeeDrinks.AMERICANO, e -> e.addIngredient(new PotionIngredient(Potions.WATER)));
		coffee(coffee, CoffeeDrinks.LATTE, e -> e.addIngredient(TagRef.MILK_BOTTLE));
		coffee(coffee, CoffeeDrinks.MOCHA, e -> e
				.addIngredient(TagRef.MILK_BOTTLE)
				.addIngredient(Items.COCOA_BEANS));
		coffee(coffee, CoffeeDrinks.CAPPUCCINO, e -> e
				.addIngredient(TagRef.MILK_BOTTLE)
				.addIngredient(CoffeeItems.CREAM));
		coffee(coffee, CoffeeDrinks.MACCHIATO, e -> e.addIngredient(CoffeeItems.CREAM));
		coffee(coffee, CoffeeDrinks.CON_PANNA, e -> e
				.addIngredient(CoffeeItems.COFFEE_POWDER)
				.addIngredient(CoffeeItems.CREAM));
		coffee(coffee, CoffeeDrinks.AFFOGATO, e -> e
				.addIngredient(CoffeeTagGen.ICE)
				.addIngredient(CoffeeItems.CREAM));

		unlock(pvd, ShapelessPatchouliBuilder.shapeless(RecipeCategory.FOOD, CoffeeDrinks.AFFOGATO.item.get(), 1)::unlockedBy,
				CoffeeDrinks.ESPRESSO.item.get()).requires(CoffeeDrinks.ESPRESSO.item)
				.requires(CoffeeTagGen.ICE).requires(CoffeeItems.CREAM)
				.save(coffee, CoffeeDrinks.AFFOGATO.item.getId().withSuffix("_craft"));

	}

	private static void cutting(RegistrateRecipeProvider pvd, ItemEntry<?> in, ItemEntry<?> out, int count) {
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(in),
						Ingredient.of(TagRef.TOOLS_KNIVES), out, count, 1)
				.build(pvd, in.getId().withSuffix("_cutting"));
	}

	private static void coffee(Consumer<FinishedRecipe> cons, CoffeeDrinks coffee, UnaryOperator<CookingPotRecipeBuilder> func) {
		func.apply(CookingPotRecipeBuilder.cookingPotRecipe(coffee.item.get(), 1, 60, 0.1f, Items.GLASS_BOTTLE)
						.setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
						.unlockedByAnyIngredient(CoffeeDrinks.ESPRESSO.item)
						.addIngredient(CoffeeDrinks.ESPRESSO.item))
				.build(cons, coffee.item.getId().withSuffix("_remix"));
	}

	public static <T extends ItemLike> void cooking(RegistrateRecipeProvider pvd, DataIngredient source, RecipeCategory category, Supplier<? extends T> result, float experience, int cookingTime, String typeName, RecipeSerializer<? extends AbstractCookingRecipe> serializer) {
		new SimpleCookingRecipeBuilder(category, CookingBookCategory.MISC, result.get(), source, experience, cookingTime, serializer)
				.unlockedBy("has_" + pvd.safeName(source), source.getCritereon(pvd))
				.save(pvd, pvd.safeId(result.get()) + "_from_" + pvd.safeName(source) + "_" + typeName);
	}

	private static Consumer<FinishedRecipe> coffee(RegistrateRecipeProvider pvd) {
		return e -> pvd.accept(new BasePotFinishedRecipe(CoffeeBlocks.MOKA_RS.get(), e));
	}

	public static <T> T unlock(RegistrateRecipeProvider pvd, BiFunction<String, InventoryChangeTrigger.TriggerInstance, T> func, Item item) {
		return func.apply("has_" + pvd.safeName(item), DataIngredient.items(item).getCritereon(pvd));
	}

}
