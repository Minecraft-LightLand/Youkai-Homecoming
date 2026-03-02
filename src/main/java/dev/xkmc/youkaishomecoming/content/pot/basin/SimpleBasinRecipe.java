package dev.xkmc.youkaishomecoming.content.pot.basin;

import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;

@SerialClass
public class SimpleBasinRecipe extends BasinRecipe<SimpleBasinRecipe> {

	@SerialField
	public Ingredient input = Ingredient.EMPTY;
	@SerialField
	public FluidStack output = FluidStack.EMPTY;

	public SimpleBasinRecipe() {
		super(YHBlocks.BASIN_RS.get());
	}

	public FluidStack assembleFluid(BasinInput basinInput, RegistryAccess registryAccess) {
		return output.copy();
	}


	@Override
	public boolean matches(BasinInput basin, Level level) {
		return input.test(basin.be.items.getItem(0));
	}

}
