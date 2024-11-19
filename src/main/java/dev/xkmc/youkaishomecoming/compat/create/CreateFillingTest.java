package dev.xkmc.youkaishomecoming.compat.create;

import com.simibubi.create.Create;
import com.simibubi.create.content.fluids.spout.FillingBySpout;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.ModList;

import java.util.Optional;
import java.util.function.Supplier;

public record CreateFillingTest(Supplier<ItemStack> result, int amount) {

	public static Optional<CreateFillingTest> test(Level level, FluidStack fluid, ItemStack bottle) {
		if (!ModList.get().isLoaded(Create.ID)) return Optional.empty();
		if (fluid.isEmpty() || bottle.isEmpty()) return Optional.empty();
		var cont = bottle.copyWithCount(1);
		if (!FillingBySpout.canItemBeFilled(level, cont)) return Optional.empty();
		int amount = FillingBySpout.getRequiredAmountForItem(level, cont, fluid);
		if (amount <= 0 || amount > fluid.getAmount()) return Optional.empty();
		return Optional.of(new CreateFillingTest(() -> FillingBySpout.fillItem(level, amount, bottle, fluid), amount));
	}

}
