package dev.xkmc.youkaishomecoming.content.pot.overlay;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

import java.util.List;

public interface IHintableBlock {

	static IHintableBlock wrap(Item... items) {
		return (level, pos) -> List.of(Ingredient.of(items));
	}

	List<Ingredient> getHints(Level level, BlockPos pos);

}
