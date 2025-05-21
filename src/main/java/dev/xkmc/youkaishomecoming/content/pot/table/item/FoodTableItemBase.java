package dev.xkmc.youkaishomecoming.content.pot.table.item;

import dev.xkmc.youkaishomecoming.content.pot.table.model.AdditionalModelHolder;
import org.jetbrains.annotations.Nullable;

public class FoodTableItemBase {

	@Nullable
	public final AdditionalModelHolder additional;

	public FoodTableItemBase(@Nullable AdditionalModelHolder additional) {
		this.additional = additional;
	}
}
