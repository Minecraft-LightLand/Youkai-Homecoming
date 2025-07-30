package dev.xkmc.youkaishomecoming.content.pot.overlay;

import dev.xkmc.l2modularblock.type.SingletonBlockMethod;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

import java.util.List;

public interface IHintableBlock extends SingletonBlockMethod {

	static IHintableBlock wrap(Item... items) {
		return (level, pos) -> List.of(Ingredient.of(items));
	}

	List<Ingredient> getHints(Level level, BlockPos pos);

}
