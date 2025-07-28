package dev.xkmc.youkaishomecoming.content.pot.cooking.soup;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import dev.xkmc.l2library.serial.recipe.BaseRecipeBuilder;
import dev.xkmc.youkaishomecoming.content.pot.cooking.core.CookingInv;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public class SoupBaseBuilder
		extends BaseRecipeBuilder<SoupBaseBuilder, SimpleSoupBaseRecipe, SoupBaseRecipe<?>, CookingInv> {

	public SoupBaseBuilder(ResourceLocation id) {
		super(YHBlocks.IMMEDIATE_SOUP.get());
		recipe.id = id;
	}

	public SoupBaseBuilder time(int time) {
		recipe.time = time;
		return this;
	}

	public SoupBaseBuilder col(int col) {
		recipe.color = col;
		return this;
	}

	public SoupBaseBuilder add(Ingredient item) {
		recipe.input.add(item);
		return this;
	}

	public SoupBaseBuilder add(ItemLike item) {
		recipe.input.add(Ingredient.of(item));
		return this;
	}

	public SoupBaseBuilder add(TagKey<Item> item) {
		recipe.input.add(Ingredient.of(item));
		return this;
	}

	public void save(RegistrateRecipeProvider pvd) {
		save(pvd, pvd.safeId(recipe.id));
	}

}
