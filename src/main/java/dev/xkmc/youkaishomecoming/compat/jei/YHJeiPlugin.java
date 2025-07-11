package dev.xkmc.youkaishomecoming.compat.jei;

import dev.xkmc.youkaishomecoming.content.pot.moka.MokaMenu;
import dev.xkmc.youkaishomecoming.content.pot.moka.MokaRecipe;
import dev.xkmc.youkaishomecoming.content.pot.moka.MokaScreen;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.registrate.CoffeeBlocks;
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

	@Override
	public ResourceLocation getPluginUid() {
		return ID;
	}

	public void registerCategories(IRecipeCategoryRegistration registry) {
		var helper = registry.getJeiHelpers().getGuiHelper();
		registry.addRecipeCategories(new MokaRecipeCategory(helper));
	}

	public void registerRecipes(IRecipeRegistration registration) {
		var level = Minecraft.getInstance().level;
		assert level != null;
		var m = level.getRecipeManager();
		registration.addRecipes(MOKA, m.getAllRecipesFor(CoffeeBlocks.MOKA_RT.get()));
	}

	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		registration.addRecipeCatalyst(CoffeeBlocks.MOKA.asStack(), MOKA);
		registration.addRecipeCatalyst(ModBlocks.STOVE.get().asItem().getDefaultInstance(), MOKA);
	}

	public void registerGuiHandlers(IGuiHandlerRegistration registration) {
		registration.addRecipeClickArea(MokaScreen.class, 89, 25, 24, 17, MOKA);
	}

	public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
		registration.addRecipeTransferHandler(MokaMenu.class, CoffeeBlocks.MOKA_MT.get(), MOKA, 0, 4, 7, 36);
	}

}
