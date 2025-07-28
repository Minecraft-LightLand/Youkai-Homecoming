package dev.xkmc.youkaishomecoming.compat.jei;

import dev.xkmc.l2serial.util.Wrappers;
import dev.xkmc.youkaishomecoming.content.item.fluid.SlipBottleItem;
import dev.xkmc.youkaishomecoming.content.pot.basin.SimpleBasinRecipe;
import dev.xkmc.youkaishomecoming.content.pot.cooking.core.PotCookingRecipe;
import dev.xkmc.youkaishomecoming.content.pot.ferment.SimpleFermentationRecipe;
import dev.xkmc.youkaishomecoming.content.pot.kettle.KettleRecipe;
import dev.xkmc.youkaishomecoming.content.pot.rack.DryingRackRecipe;
import dev.xkmc.youkaishomecoming.content.pot.steamer.SteamingRecipe;
import dev.xkmc.youkaishomecoming.content.pot.table.recipe.CuisineRecipe;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.*;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import vectorwing.farmersdelight.common.registry.ModBlocks;

import java.util.Objects;

@JeiPlugin
public class YHJeiPlugin implements IModPlugin {

	public static final ResourceLocation ID = YoukaisHomecoming.loc("main");

	public static final RecipeType<KettleRecipe> KETTLE = RecipeType.create(YoukaisHomecoming.MODID, "kettle", KettleRecipe.class);
	public static final RecipeType<DryingRackRecipe> RACK = RecipeType.create(YoukaisHomecoming.MODID, "drying_rack", DryingRackRecipe.class);
	public static final RecipeType<SteamingRecipe> STEAM = RecipeType.create(YoukaisHomecoming.MODID, "steaming", SteamingRecipe.class);
	public static final RecipeType<SimpleFermentationRecipe> FERMENT = RecipeType.create(YoukaisHomecoming.MODID, "ferment", SimpleFermentationRecipe.class);
	public static final RecipeType<CuisineRecipe<?>> CUISINE = RecipeType.create(YoukaisHomecoming.MODID, "cuisine", Wrappers.cast(CuisineRecipe.class));
	public static final RecipeType<SimpleBasinRecipe> BASIN = RecipeType.create(YoukaisHomecoming.MODID, "basin", SimpleBasinRecipe.class);
	public static final RecipeType<PotCookingRecipe<?>> BOWL_COOKING = RecipeType.create(YoukaisHomecoming.MODID, "bowl_cooking", Wrappers.cast(PotCookingRecipe.class));
	public static final RecipeType<PotCookingRecipe<?>> POT_COOKING = RecipeType.create(YoukaisHomecoming.MODID, "pot_cooking", Wrappers.cast(PotCookingRecipe.class));
	public static final RecipeType<PotCookingRecipe<?>> STOCK_COOKING = RecipeType.create(YoukaisHomecoming.MODID, "stock_cooking", Wrappers.cast(PotCookingRecipe.class));

	@Override
	public ResourceLocation getPluginUid() {
		return ID;
	}

	@Override
	public void registerItemSubtypes(ISubtypeRegistration registration) {
		registration.registerSubtypeInterpreter(YHItems.SAKE_BOTTLE.get(), this::slipSubType);
	}

	private String slipSubType(ItemStack stack, UidContext uid) {
		return SlipBottleItem.getFluid(stack).getFluid().builtInRegistryHolder().unwrapKey().orElseThrow().location().toString();
	}

	public void registerCategories(IRecipeCategoryRegistration registry) {
		var helper = registry.getJeiHelpers().getGuiHelper();
		registry.addRecipeCategories(new KettleRecipeCategory().init(helper));
		registry.addRecipeCategories(new DryingRackCategory(helper));
		registry.addRecipeCategories(new SteamingCategory(helper));
		registry.addRecipeCategories(new FermentRecipeCategory().init(helper));
		registry.addRecipeCategories(new BasinRecipeCategory().init(helper));
		registry.addRecipeCategories(new CuisineRecipeCategory().init(helper));
		registry.addRecipeCategories(new PotCookingRecipeCategory("bowl_cooking", YHBlocks.IRON_BOWL.asStack()).init(helper));
		registry.addRecipeCategories(new PotCookingRecipeCategory("pot_cooking", YHBlocks.IRON_POT.asStack()).init(helper));
		registry.addRecipeCategories(new PotCookingRecipeCategory("stock_cooking", YHBlocks.STOCKPOT.asStack()).init(helper));
	}

	public void registerRecipes(IRecipeRegistration registration) {
		var level = Minecraft.getInstance().level;
		assert level != null;
		var m = level.getRecipeManager();
		registration.addRecipes(KETTLE, m.getAllRecipesFor(YHBlocks.KETTLE_RT.get()));
		registration.addRecipes(RACK, m.getAllRecipesFor(YHBlocks.RACK_RT.get()));
		registration.addRecipes(STEAM, m.getAllRecipesFor(YHBlocks.STEAM_RT.get()));
		registration.addRecipes(FERMENT, m.getAllRecipesFor(YHBlocks.FERMENT_RT.get())
				.stream().map(e -> e instanceof SimpleFermentationRecipe x ? x : null).filter(Objects::nonNull).toList());
		registration.addRecipes(BASIN, m.getAllRecipesFor(YHBlocks.BASIN_RT.get())
				.stream().map(e -> e instanceof SimpleBasinRecipe x ? x : null).filter(Objects::nonNull).toList());
		registration.addRecipes(CUISINE, m.getAllRecipesFor(YHBlocks.CUISINE_RT.get()));
		registration.addRecipes(BOWL_COOKING, m.getAllRecipesFor(YHBlocks.COOKING_RT.get())
				.stream().filter(e -> e.matchContainer(YHBlocks.IRON_BOWL.asItem())).toList());
		registration.addRecipes(POT_COOKING, m.getAllRecipesFor(YHBlocks.COOKING_RT.get())
				.stream().filter(e -> e.matchContainer(YHBlocks.IRON_POT.asItem())).toList());
		registration.addRecipes(STOCK_COOKING, m.getAllRecipesFor(YHBlocks.COOKING_RT.get())
				.stream().filter(e -> e.matchContainer(YHBlocks.STOCKPOT.asItem())).toList());
	}

	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		registration.addRecipeCatalyst(YHBlocks.KETTLE.asStack(), KETTLE);
		registration.addRecipeCatalyst(YHBlocks.RACK.asStack(), RACK);
		registration.addRecipeCatalyst(YHBlocks.FERMENT.asStack(), FERMENT);
		registration.addRecipeCatalyst(YHBlocks.BASIN.asStack(), BASIN);
		registration.addRecipeCatalyst(YHBlocks.STEAMER_LID.asStack(), STEAM);
		registration.addRecipeCatalyst(YHBlocks.STEAMER_RACK.asStack(), STEAM);
		registration.addRecipeCatalyst(YHBlocks.STEAMER_POT.asStack(), STEAM);
		registration.addRecipeCatalyst(YHBlocks.CUISINE_BOARD.asStack(), CUISINE);
		registration.addRecipeCatalyst(YHBlocks.IRON_BOWL.asStack(), BOWL_COOKING);
		registration.addRecipeCatalyst(YHBlocks.IRON_POT.asStack(), POT_COOKING);
		registration.addRecipeCatalyst(YHBlocks.STOCKPOT.asStack(), STOCK_COOKING);
		registration.addRecipeCatalyst(ModBlocks.STOVE.get().asItem().getDefaultInstance(), KETTLE, STEAM, BOWL_COOKING, POT_COOKING, STOCK_COOKING);
	}

}
