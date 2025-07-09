package dev.xkmc.youkaishomecoming.content.pot.cooking.core;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import dev.xkmc.l2library.serial.recipe.BaseRecipe;
import dev.xkmc.l2library.serial.recipe.BaseRecipeBuilder;
import dev.xkmc.l2serial.util.Wrappers;
import dev.xkmc.youkaishomecoming.content.pot.table.food.FoodModelHelper;
import dev.xkmc.youkaishomecoming.content.pot.table.food.YHRolls;
import dev.xkmc.youkaishomecoming.content.pot.table.item.VariantTableItemBase;
import dev.xkmc.youkaishomecoming.content.pot.table.recipe.BaseCuisineRecipe;
import dev.xkmc.youkaishomecoming.content.pot.table.recipe.CuisineInv;
import dev.xkmc.youkaishomecoming.content.pot.table.recipe.CuisineRecipe;
import dev.xkmc.youkaishomecoming.content.pot.table.recipe.UnorderedRecipeBuilder;
import dev.xkmc.youkaishomecoming.init.food.YHBowl;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public abstract class CookingRecipeBuilder<T extends PotCookingRecipe<T>, B extends CookingRecipeBuilder<T, B>>
		extends BaseRecipeBuilder<B, T, PotCookingRecipe<?>, CookingInv> {

	public CookingRecipeBuilder(BaseRecipe.RecType<T, PotCookingRecipe<?>, CookingInv> type, YHBowl result, int time) {
		super(type);
		recipe.time = time;
		recipe.result = result.asItem().getDefaultInstance();
	}

	public void save(RegistrateRecipeProvider pvd) {
		var item = recipe.result.getItem();
		var path = item.builtInRegistryHolder().unwrapKey().orElseThrow().location();
		save(pvd, pvd.safeId(path));
	}

}
