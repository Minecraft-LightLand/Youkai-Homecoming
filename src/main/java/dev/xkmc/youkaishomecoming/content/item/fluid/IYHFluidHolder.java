package dev.xkmc.youkaishomecoming.content.item.fluid;

import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface IYHFluidHolder {

	int getColor();

	Item getContainer();

	ItemStack asStack(int count);

	ItemEntry<?> item();

	default int count() {
		return 4;
	}

	default int amount() {
		return 250;
	}

	@Nullable
	default String bottleTextureFolder() {
		return null;
	}

	@Nullable
	default BottledDrinkSet bottleSet() {
		return null;
	}

	Item asItem();

	YHFluid source();
}
