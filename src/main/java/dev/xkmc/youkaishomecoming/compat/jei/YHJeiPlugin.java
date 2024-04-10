package dev.xkmc.youkaishomecoming.compat.jei;

import dev.xkmc.youkaishomecoming.content.pot.kettle.KettleMenu;
import dev.xkmc.youkaishomecoming.content.pot.kettle.KettleRecipe;
import dev.xkmc.youkaishomecoming.content.pot.kettle.KettleScreen;
import dev.xkmc.youkaishomecoming.content.pot.moka.MokaMenu;
import dev.xkmc.youkaishomecoming.content.pot.moka.MokaRecipe;
import dev.xkmc.youkaishomecoming.content.pot.moka.MokaScreen;
import dev.xkmc.youkaishomecoming.content.pot.rack.DryingRackRecipe;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.*;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import vectorwing.farmersdelight.common.registry.ModBlocks;

@JeiPlugin
public class YHJeiPlugin implements IModPlugin {

	public static final ResourceLocation ID = YoukaisHomecoming.loc("main");

	public static final RecipeType<MokaRecipe> MOKA = RecipeType.create(YoukaisHomecoming.MODID, "moka", MokaRecipe.class);
	public static final RecipeType<KettleRecipe> KETTLE = RecipeType.create(YoukaisHomecoming.MODID, "kettle", KettleRecipe.class);
	public static final RecipeType<DryingRackRecipe> RACK = RecipeType.create(YoukaisHomecoming.MODID, "drying_rack", DryingRackRecipe.class);

	@Override
	public ResourceLocation getPluginUid() {
		return ID;
	}

	public void registerCategories(IRecipeCategoryRegistration registry) {
		var helper = registry.getJeiHelpers().getGuiHelper();
		registry.addRecipeCategories(new MokaRecipeCategory(helper));
		registry.addRecipeCategories(new KettleRecipeCategory(helper));
		registry.addRecipeCategories(new DryingRackCategory(helper));
	}

	public void registerRecipes(IRecipeRegistration registration) {
		var level = Minecraft.getInstance().level;
		assert level != null;
		var m = level.getRecipeManager();
		registration.addRecipes(MOKA, m.getAllRecipesFor(YHBlocks.MOKA_RT.get()));
		registration.addRecipes(KETTLE, m.getAllRecipesFor(YHBlocks.KETTLE_RT.get()));
		registration.addRecipes(RACK, m.getAllRecipesFor(YHBlocks.RACK_RT.get()));
	}

	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		registration.addRecipeCatalyst(YHBlocks.MOKA.asStack(), MOKA);
		registration.addRecipeCatalyst(YHBlocks.KETTLE.asStack(), KETTLE);
		registration.addRecipeCatalyst(YHBlocks.RACK.asStack(), RACK);
		registration.addRecipeCatalyst(ModBlocks.STOVE.get().asItem().getDefaultInstance(), MOKA, KETTLE);
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
