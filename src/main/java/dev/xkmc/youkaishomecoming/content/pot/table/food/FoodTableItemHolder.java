package dev.xkmc.youkaishomecoming.content.pot.table.food;

import dev.xkmc.youkaishomecoming.content.pot.table.item.FoodTableItemBase;
import dev.xkmc.youkaishomecoming.content.pot.table.model.FixedModelHolder;

public record FoodTableItemHolder(
		int count,
		FoodTableItemBase base,
		FixedModelHolder model
) {
}
