package dev.xkmc.youkaishomecoming.content.item.fluid;

import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

public interface IYHFluidHolder extends ItemLike {

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
	default BottleTexture bottleSet() {
		return null;
	}

	Item asItem();

	YHFluid source();
}
