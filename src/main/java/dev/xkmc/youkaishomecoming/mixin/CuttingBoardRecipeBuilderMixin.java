package dev.xkmc.youkaishomecoming.mixin;

import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vectorwing.farmersdelight.common.crafting.ingredient.ChanceResult;
import vectorwing.farmersdelight.data.builder.CuttingBoardRecipeBuilder;

@Mixin(CuttingBoardRecipeBuilder.class)
public abstract class CuttingBoardRecipeBuilderMixin {

	@Shadow
	@Final
	private Ingredient ingredient;

	@Shadow
	public abstract void save(RecipeOutput output, ResourceLocation id);

	@Shadow
	@Final
	private NonNullList<ChanceResult> results;

	@Inject(at = @At("HEAD"), method = "build(Lnet/minecraft/data/recipes/RecipeOutput;)V", cancellable = true)
	public void youkaishomcoming$respectModid(RecipeOutput consumerIn, CallbackInfo ci) {
		ResourceLocation location = BuiltInRegistries.ITEM.getKey(ingredient.getItems()[0].getItem());
		ResourceLocation res = BuiltInRegistries.ITEM.getKey(results.getLast().stack().getItem());
		save(consumerIn, res.withPath(location.getPath()));
		ci.cancel();
	}

}
