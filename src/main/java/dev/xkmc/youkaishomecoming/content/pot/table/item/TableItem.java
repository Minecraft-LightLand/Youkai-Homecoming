package dev.xkmc.youkaishomecoming.content.pot.table.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;

public interface TableItem {

	Optional<TableItem> find(Level level, ItemStack stack);

	List<Ingredient> getHints(Level level);

	int step();

	Optional<ItemStack> complete(Level level);

	default Optional<ItemStack> doTransform() {
		return Optional.empty();
	}

	List<ResourceLocation> getModels();

	default void collectIngredients(List<Ingredient> list) {

	}

	default int getCost(TableItem prev) {
		return 1;
	}

}
