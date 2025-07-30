package dev.xkmc.youkaishomecoming.content.pot.table.recipe;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

@SerialClass
public class FixedCuisineRecipe extends BaseCuisineRecipe<FixedCuisineRecipe> {

	public FixedCuisineRecipe(ResourceLocation id) {
		super(id, YHBlocks.CUISINE_FIXED.get());
	}

	@Override
	public List<Ingredient> getCustomIngredients() {
		return List.of();
	}

	@Override
	public boolean matches(CuisineInv inv, Level level) {
		return inv.base().equals(base) && inv.getContainerSize() == 0;
	}

	@Override
	public List<Ingredient> getHints(Level level, CuisineInv inv) {
		return List.of();
	}

}
