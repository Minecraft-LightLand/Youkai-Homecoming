package dev.xkmc.youkaishomecoming.compat.jei;

import dev.xkmc.l2serial.util.Wrappers;
import dev.xkmc.youkaishomecoming.content.pot.ferment.SimpleFermentationRecipe;
import dev.xkmc.youkaishomecoming.content.pot.kettle.KettleMenu;
import dev.xkmc.youkaishomecoming.content.pot.kettle.KettleRecipe;
import dev.xkmc.youkaishomecoming.content.pot.kettle.KettleScreen;
import dev.xkmc.youkaishomecoming.content.pot.moka.MokaMenu;
import dev.xkmc.youkaishomecoming.content.pot.moka.MokaRecipe;
import dev.xkmc.youkaishomecoming.content.pot.moka.MokaScreen;
import dev.xkmc.youkaishomecoming.content.pot.rack.DryingRackRecipe;
import dev.xkmc.youkaishomecoming.content.pot.steamer.SteamingRecipe;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.*;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import vectorwing.farmersdelight.common.registry.ModBlocks;

import java.util.Objects;

@JeiPlugin
public class YHJeiPlugin implements IModPlugin {

	public static final ResourceLocation ID = YoukaisHomecoming.loc("main");

	public static final RecipeType<MokaRecipe> MOKA = RecipeType.create(YoukaisHomecoming.MODID, "moka", MokaRecipe.class);
	public static final RecipeType<KettleRecipe> KETTLE = RecipeType.create(YoukaisHomecoming.MODID, "kettle", KettleRecipe.class);
	public static final RecipeType<RecipeHolder<DryingRackRecipe>> RACK = RecipeType.create(YoukaisHomecoming.MODID, "drying_rack", Wrappers.cast(RecipeHolder.class));
	public static final RecipeType<RecipeHolder<SteamingRecipe>> STEAM = RecipeType.create(YoukaisHomecoming.MODID, "steaming", Wrappers.cast(RecipeHolder.class));
	public static final RecipeType<SimpleFermentationRecipe> FERMENT = RecipeType.create(YoukaisHomecoming.MODID, "ferment", SimpleFermentationRecipe.class);

	@Override
	public ResourceLocation getPluginUid() {
		return ID;
	}

	public void registerCategories(IRecipeCategoryRegistration registry) {
		var helper = registry.getJeiHelpers().getGuiHelper();
		registry.addRecipeCategories(new MokaRecipeCategory(helper));
		registry.addRecipeCategories(new KettleRecipeCategory(helper));
		registry.addRecipeCategories(new DryingRackCategory(helper));
		registry.addRecipeCategories(new SteamingCategory(helper));
		registry.addRecipeCategories(new FermentRecipeCategory().init(helper));
	}

	public void registerRecipes(IRecipeRegistration registration) {
		var level = Minecraft.getInstance().level;
		assert level != null;
		var m = level.getRecipeManager();
		registration.addRecipes(MOKA, m.getAllRecipesFor(YHBlocks.MOKA_RT.get()).stream().map(RecipeHolder::value).toList());
		registration.addRecipes(KETTLE, m.getAllRecipesFor(YHBlocks.KETTLE_RT.get()).stream().map(RecipeHolder::value).toList());
		registration.addRecipes(RACK, m.getAllRecipesFor(YHBlocks.RACK_RT.get()));
		registration.addRecipes(STEAM, m.getAllRecipesFor(YHBlocks.STEAM_RT.get()));
		registration.addRecipes(FERMENT, m.getAllRecipesFor(YHBlocks.FERMENT_RT.get())
				.stream().map(e -> e.value() instanceof SimpleFermentationRecipe x ? x : null).filter(Objects::nonNull).toList());
	}

	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		registration.addRecipeCatalyst(YHBlocks.MOKA.asStack(), MOKA);
		registration.addRecipeCatalyst(YHBlocks.KETTLE.asStack(), KETTLE);
		registration.addRecipeCatalyst(YHBlocks.RACK.asStack(), RACK);
		registration.addRecipeCatalyst(YHBlocks.FERMENT.asStack(), FERMENT);
		registration.addRecipeCatalyst(YHBlocks.STEAMER_LID.asStack(), STEAM);
		registration.addRecipeCatalyst(YHBlocks.STEAMER_RACK.asStack(), STEAM);
		registration.addRecipeCatalyst(YHBlocks.STEAMER_POT.asStack(), STEAM);
		registration.addRecipeCatalyst(ModBlocks.STOVE.get().asItem().getDefaultInstance(), MOKA, KETTLE, STEAM);
	}

	public void registerGuiHandlers(IGuiHandlerRegistration registration) {
		registration.addRecipeClickArea(MokaScreen.class, 89, 25, 24, 17, MOKA);
		registration.addRecipeClickArea(KettleScreen.class, 89, 25, 24, 17, KETTLE);
	}

	public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
		registration.addRecipeTransferHandler(MokaMenu.class, YHBlocks.MOKA_MT.get(), MOKA, 0, 4, 7, 36);
		registration.addRecipeTransferHandler(KettleMenu.class, YHBlocks.KETTLE_MT.get(), KETTLE, 0, 4, 7, 36);
	}


}
