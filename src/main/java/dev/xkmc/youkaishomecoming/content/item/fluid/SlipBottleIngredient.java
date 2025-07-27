package dev.xkmc.youkaishomecoming.content.item.fluid;

import dev.xkmc.l2library.serial.ingredients.BaseIngredient;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.stream.Stream;

@SerialClass
public class SlipBottleIngredient extends BaseIngredient<SlipBottleIngredient> {

	public static final BaseIngredient.Serializer<SlipBottleIngredient> INSTANCE = new BaseIngredient.Serializer<>(SlipBottleIngredient.class, YoukaisHomecoming.loc("fluid"));

	private static Stream<? extends Value> mapStream(Fluid fluid) {
		if (fluid instanceof YHFluid type) {
			var item = type.type.asStack(1);
			if (type.type.bottleSet() != null) {
				var filled = new SlipFluidWrapper(YHItems.SAKE_BOTTLE.asStack());
				filled.setFluid(new FluidStack(fluid, 1000));
				var flask = filled.getContainer();
				if (type.type.bottleSet() instanceof BottledDrinkSet set) {
					return Stream.of(new ItemValue(set.bottle.asStack()), new ItemValue(flask), new ItemValue(item));
				} else return Stream.of(new ItemValue(flask), new ItemValue(item));
			} else return Stream.of(new ItemValue(item));
		}
		return Stream.of();
	}

	@SerialClass.SerialField
	public Fluid fluid;

	public SlipBottleIngredient() {

	}

	public SlipBottleIngredient(Fluid fluid) {
		super(mapStream(fluid));
		this.fluid = fluid;
	}

	@Override
	protected SlipBottleIngredient validate() {
		return new SlipBottleIngredient(fluid);
	}

	@Override
	public boolean test(ItemStack stack) {
		if (!(fluid instanceof YHFluid type)) return false;
		if (stack.isEmpty()) return false;
		if (stack.is(type.type.asItem())) return true;
		if (!SlipBottleItem.isSlipContainer(stack)) return false;
		return SlipBottleItem.getFluid(stack).getFluid() == fluid;
	}

	@Override
	public Serializer<SlipBottleIngredient> getSerializer() {
		return INSTANCE;
	}

}
