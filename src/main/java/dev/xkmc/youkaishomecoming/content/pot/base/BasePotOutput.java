package dev.xkmc.youkaishomecoming.content.pot.base;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.neoforged.neoforge.common.conditions.ICondition;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;

public record BasePotOutput<T extends BasePotRecipe>(
		BasePotSerializer<T> ser, RecipeOutput delegate
) implements RecipeOutput {

	@Override
	public Advancement.Builder advancement() {
		return delegate().advancement();
	}

	@Override
	public void accept(ResourceLocation id, Recipe<?> recipe, @Nullable AdvancementHolder adv, ICondition... cond) {
		if (recipe instanceof CookingPotRecipe c) {
			recipe = ser.wrap(c);
		}
		delegate().accept(id, recipe, adv, cond);
	}

}
