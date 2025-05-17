package dev.xkmc.youkaishomecoming.content.pot.table.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;

public interface TableItem {

	Optional<TableItem> find(Level level, ItemStack stack);

	int step();

	Optional<ItemStack> complete(Level level);

	List<ResourceLocation> getModels();

}
