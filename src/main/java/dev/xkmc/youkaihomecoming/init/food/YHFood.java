package dev.xkmc.youkaihomecoming.init.food;

import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;

import java.util.List;
import java.util.Locale;

public enum YHFood {
	RAW_LAMPREY(FoodType.MEAT, 2, 0.3f,
			new EffectEntry(() -> MobEffects.NIGHT_VISION, 2400, 0, 0.5f),
			ItemTags.FISHES
	),
	ROASTED_LAMPREY(FoodType.MEAT, 10, 0.8f,
			new EffectEntry(() -> MobEffects.NIGHT_VISION, 2400, 0, 1)
	),
	RAW_LAMPREY_FILLET(FoodType.MEAT_SLICE, 1, 0.3f,
			new EffectEntry(() -> MobEffects.NIGHT_VISION, 1800, 0, 0.5f)
	),
	ROASTED_LAMPREY_FILLET(FoodType.MEAT_SLICE, 6, 0.8f,
			new EffectEntry(() -> MobEffects.NIGHT_VISION, 1800, 0, 1)
	),
	FLESH(FoodType.FLESH, 2, 0.3f),
	COOKED_FLESH(FoodType.FLESH, 5, 0.6f);


	public final ItemEntry<Item> item;

	@SafeVarargs
	YHFood(FoodType type, int nutrition, float sat, List<EffectEntry> effs, TagKey<Item>... tags) {
		String name = name().toLowerCase(Locale.ROOT);
		item = type.build(name, nutrition, sat, tags, effs);
	}

	@SafeVarargs
	YHFood(FoodType type, int nutrition, float sat, TagKey<Item>... tags) {
		this(type, nutrition, sat, List.of(), tags);
	}

	@SafeVarargs
	YHFood(FoodType type, int nutrition, float sat, EffectEntry eff, TagKey<Item>... tags) {
		this(type, nutrition, sat, List.of(eff), tags);
	}

	public static void register() {

	}

}
