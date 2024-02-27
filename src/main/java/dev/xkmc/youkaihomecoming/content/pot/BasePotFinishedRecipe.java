package dev.xkmc.youkaihomecoming.content.pot;

import com.google.gson.JsonObject;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.Nullable;

public record BasePotFinishedRecipe(RecipeSerializer<?> rec, FinishedRecipe result) implements FinishedRecipe {

	@Override
	public void serializeRecipeData(JsonObject json) {
		result.serializeRecipeData(json);
	}

	@Override
	public ResourceLocation getId() {
		return result.getId();
	}

	@Override
	public RecipeSerializer<?> getType() {
		return rec;
	}

	@Nullable
	@Override
	public JsonObject serializeAdvancement() {
		return result.serializeAdvancement();
	}

	@Nullable
	@Override
	public ResourceLocation getAdvancementId() {
		return result.getAdvancementId();
	}

}
