package dev.xkmc.youkaishomecoming.content.item.fluid;

import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.crafting.ICustomIngredient;
import net.neoforged.neoforge.common.crafting.IngredientType;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.stream.Stream;

public record SlipBottleIngredient(Fluid fluid) implements ICustomIngredient {

	public static Ingredient of(Fluid fluid) {
		return (new SlipBottleIngredient(fluid)).toVanilla();
	}

	@Override
	public Stream<ItemStack> getItems() {
		if (fluid instanceof YHFluid type) {
			var item = type.type.asStack(1);
			if (type.type.bottleSet() != null) {
				var filled = new SlipFluidWrapper(YHItems.SAKE_BOTTLE.asStack());
				filled.setFluid(new FluidStack(fluid, 1000));
				var flask = filled.getContainer();
				if (type.type.bottleSet() instanceof BottledDrinkSet set) {
					return Stream.of(set.bottle.asStack(), flask, item);
				} else return Stream.of(flask, item);
			} else return Stream.of(item);
		}
		return Stream.of();
	}

	@Override
	public boolean isSimple() {
		return false;
	}

	@Override
	public IngredientType<?> getType() {
		return YoukaisHomecoming.ING_BOTTLE.get();
	}

	@Override
	public boolean test(ItemStack stack) {
		if (!(fluid instanceof YHFluid type)) return false;
		if (stack.isEmpty()) return false;
		if (stack.is(type.type.asItem())) return true;
		if (!SlipBottleItem.isSlipContainer(stack)) return false;
		return SlipBottleItem.getFluid(stack).getFluid() == fluid;
	}

}
