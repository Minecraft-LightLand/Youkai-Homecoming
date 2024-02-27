package dev.xkmc.youkaihomecoming.compat.jei;

import dev.xkmc.youkaihomecoming.content.pot.*;
import dev.xkmc.youkaihomecoming.init.YoukaiHomecoming;
import dev.xkmc.youkaihomecoming.init.registrate.YHBlocks;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.*;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import vectorwing.farmersdelight.common.registry.ModBlocks;

@JeiPlugin
public class YHJeiPlugin implements IModPlugin {

	public static final ResourceLocation ID = new ResourceLocation(YoukaiHomecoming.MODID, "main");

	public static final RecipeType<MokaRecipe> MOKA = RecipeType.create(YoukaiHomecoming.MODID, "moka", MokaRecipe.class);
	public static final RecipeType<KettleRecipe> KETTLE = RecipeType.create(YoukaiHomecoming.MODID, "kettle", KettleRecipe.class);

	@Override
	public ResourceLocation getPluginUid() {
		return ID;
	}

	public void registerCategories(IRecipeCategoryRegistration registry) {
		registry.addRecipeCategories(new MokaRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new KettleRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
	}

	public void registerRecipes(IRecipeRegistration registration) {
		var level = Minecraft.getInstance().level;
		assert level != null;
		var m = level.getRecipeManager();
		registration.addRecipes(MOKA, m.getAllRecipesFor(YHBlocks.MOKA_RT.get()));
		registration.addRecipes(KETTLE, m.getAllRecipesFor(YHBlocks.KETTLE_RT.get()));
	}

	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		registration.addRecipeCatalyst(YHBlocks.MOKA.asStack(), MOKA);
		registration.addRecipeCatalyst(YHBlocks.KETTLE.asStack(), KETTLE);
		registration.addRecipeCatalyst(ModBlocks.STOVE.get().asItem().getDefaultInstance(), MOKA, KETTLE);
	}

	public void registerGuiHandlers(IGuiHandlerRegistration registration) {
		registration.addRecipeClickArea(MokaScreen.class, 89, 25, 24, 17, MOKA);
		registration.addRecipeClickArea(KettleScreen.class, 89, 25, 24, 17, MOKA);
	}

	public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
		registration.addRecipeTransferHandler(MokaMenu.class, YHBlocks.MOKA_MT.get(), MOKA, 0, 6, 9, 36);
		registration.addRecipeTransferHandler(KettleMenu.class, YHBlocks.KETTLE_MT.get(), KETTLE, 0, 6, 9, 36);
	}


}
