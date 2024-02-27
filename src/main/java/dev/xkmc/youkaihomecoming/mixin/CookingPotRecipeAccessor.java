package dev.xkmc.youkaihomecoming.mixin;

import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;

@Mixin(CookingPotRecipe.class)
public interface CookingPotRecipeAccessor {

	@Accessor
	ItemStack getOutput();

}
