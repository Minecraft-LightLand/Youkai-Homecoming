package dev.xkmc.youkaishomecoming.content.pot.overlay;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

import java.util.List;

public interface IHintableBlockEntity {

	static IHintableBlockEntity wrap(Item... items) {
		return level -> List.of(Ingredient.of(items));
	}

	List<Ingredient> getHints(Level level);

}
