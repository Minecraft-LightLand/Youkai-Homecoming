package dev.xkmc.youkaishomecoming.content.pot.table.food;

import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.youkaishomecoming.init.food.EffectEntry;
import dev.xkmc.youkaishomecoming.init.food.FoodType;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

import java.util.List;
import java.util.Locale;
import java.util.function.Function;

public enum YHRolls implements ItemLike {
	SALMON_HOSOMAKI(FoodType.MEAT, 10, 0.9f, FoodModelHelper::hosomaki, List.of()),
	SALMON_FUTOMAKI(FoodType.MEAT, 16, 0.9f, FoodModelHelper::futomaki, List.of()),
	;

	public final FoodTableItemHolder model;
	public final ItemEntry<Item> item;

	YHRolls(FoodType type, int nutrition, float sat,
			Function<String, FoodTableItemHolder> modelFactory,
			List<EffectEntry> effs, TagKey<Item>... tags) {
		String name = name().toLowerCase(Locale.ROOT);
		this.model = modelFactory.apply(name);
		item = type.build(name, nutrition, sat, tags, effs)
				.model((ctx, pvd) ->
						FoodModelHelper.buildModel(model.model(), ctx, pvd))
				.register();
		FoodModelHelper.map(item.getId(), model);
	}

	@Override
	public Item asItem() {
		return item.get();
	}

	public static void init() {

	}

}
