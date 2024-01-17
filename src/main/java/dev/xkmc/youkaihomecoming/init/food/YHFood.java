package dev.xkmc.youkaihomecoming.init.food;

import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import vectorwing.farmersdelight.common.registry.ModEffects;

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
	COOKED_FLESH(FoodType.FLESH, 5, 0.6f),

	APAKI(FoodType.BOWL_MEAT, 12, 0.8f, new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1)),
	BLAZING_RED_CURRY(FoodType.BOWL_MEAT, 10, 0.8f, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1),
			new EffectEntry(() -> MobEffects.FIRE_RESISTANCE, 3600, 0, 1)
	)),
	GRILLED_EEL_OVER_RICE(FoodType.BOWL_MEAT, 12, 0.8f, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1),
			new EffectEntry(() -> MobEffects.NIGHT_VISION, 3600, 0, 1)
	)),
	HIGAN_SOUP(FoodType.BOWL, 6, 0.5f, new EffectEntry(ModEffects.COMFORT, 3600, 0, 1)),

	LONGEVITY_NOODLES(FoodType.BOWL_MEAT, 12, 0.8f, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1)
	)),
	MISO_SOUP(FoodType.BOWL, 8, 0.5f, new EffectEntry(ModEffects.COMFORT, 6000, 0, 1)),
	POOR_GOD_SOUP(FoodType.BOWL, 6, 0.5f, new EffectEntry(ModEffects.COMFORT, 1200, 0, 1)),
	POWER_SOUP(FoodType.BOWL_MEAT, 16, 0.8f, new EffectEntry(ModEffects.COMFORT, 6000, 0, 1)),
	SHIRAYUKI(FoodType.BOWL_MEAT, 12, 0.8f, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1),
			new EffectEntry(() -> MobEffects.NIGHT_VISION, 3600, 0, 1)
	)),
	SWEET_ORMOSIA_MOCHI_MIXED_BOILED(FoodType.BOWL, 8, 0.8f, new EffectEntry(ModEffects.COMFORT, 3600, 0, 1)),


	;


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
