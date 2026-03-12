package dev.xkmc.youkaishomecoming.content.item.fluid;

import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

public interface IYHFluidHolder extends ItemLike, IYHFluidBottled {

	ItemEntry<?> item();

	Item asItem();

	YHFluid source();

}
