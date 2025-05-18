package dev.xkmc.youkaishomecoming.content.pot.table.food;

import dev.xkmc.youkaishomecoming.content.pot.table.item.FoodTableItemBase;
import dev.xkmc.youkaishomecoming.content.pot.table.model.FixedModelHolder;

public record FoodTableItemHolder(
		FoodTableItemBase base,
		FixedModelHolder model
) {
}
