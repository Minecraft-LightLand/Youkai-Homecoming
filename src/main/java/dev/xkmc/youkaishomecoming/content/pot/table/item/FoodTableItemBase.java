package dev.xkmc.youkaishomecoming.content.pot.table.item;

import dev.xkmc.youkaishomecoming.content.pot.table.model.AdditionalModelHolder;
import dev.xkmc.youkaishomecoming.content.pot.table.model.VariantModelPart;
import dev.xkmc.youkaishomecoming.content.pot.table.recipe.CuisineInv;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FoodTableItemBase {

	@Nullable
	public final AdditionalModelHolder additional;

	public FoodTableItemBase(@Nullable AdditionalModelHolder additional) {
		this.additional = additional;
	}

	public VariantModelPart addPart(String name, int max) {
		assert additional != null;
		return additional.addPart(name, max);
	}

	public boolean isValid(Level level, ResourceLocation id, ArrayList<ItemStack> ans) {
		var cont = new CuisineInv(id, ans, 0, false);
		return level.getRecipeManager().getRecipeFor(YHBlocks.CUISINE_RT.get(), cont, level).isPresent();
	}

	public List<Ingredient> getHints(Level level, ResourceLocation id, List<ItemStack> contents) {
		List<Ingredient> ans = new ArrayList<>();
		var cont = new CuisineInv(id, contents, 0, false);
		var list = level.getRecipeManager().getRecipesFor(YHBlocks.CUISINE_RT.get(), cont, level);
		for (var r : list) {
			ans.addAll(r.getHints(level, cont));
		}
		return ans;
	}

}
