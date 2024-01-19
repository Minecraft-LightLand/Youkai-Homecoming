package dev.xkmc.youkaihomecoming.init.food;

import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.youkaihomecoming.init.data.YHTagGen;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import vectorwing.farmersdelight.common.registry.ModEffects;
import vectorwing.farmersdelight.common.tag.ModTags;

import java.util.List;
import java.util.Locale;

public enum YHFood {
	RAW_LAMPREY(FoodType.MEAT, 2, 0.3f,
			new EffectEntry(() -> MobEffects.NIGHT_VISION, 2400, 0, 0.5f),
			ItemTags.FISHES, YHTagGen.RAW_EEL
	),
	ROASTED_LAMPREY(FoodType.MEAT, 10, 0.8f,
			new EffectEntry(() -> MobEffects.NIGHT_VISION, 2400, 0, 1)
	),
	RAW_LAMPREY_FILLET(FoodType.MEAT_SLICE, 1, 0.3f,
			new EffectEntry(() -> MobEffects.NIGHT_VISION, 1800, 0, 0.5f),
			YHTagGen.RAW_EEL, ModTags.CABBAGE_ROLL_INGREDIENTS
	),
	ROASTED_LAMPREY_FILLET(FoodType.MEAT_SLICE, 6, 0.8f,
			new EffectEntry(() -> MobEffects.NIGHT_VISION, 1800, 0, 1)
	),
	FLESH(FoodType.FLESH, 2, 0.3f, YHTagGen.RAW_FLESH),
	COOKED_FLESH(FoodType.FLESH, 5, 0.8f),

	// simple
	BUTTER(FoodType.SIMPLE, 3, 0.3f),
	TOFU(FoodType.SIMPLE, 4, 0.5f),
	OILY_BEAN_CURD(FoodType.SIMPLE, 4, 0.8f),
	MOCHI(FoodType.SIMPLE, 4, 0.6f, YHTagGen.DANGO),
	SAKURA_MOCHI(FoodType.SIMPLE, 4, 0.6f, YHTagGen.DANGO),
	ONIGILI(FoodType.SIMPLE, 6, 0.6f),
	SENBEI(FoodType.SIMPLE, 4, 0.6f),
	SEKIBANKIYAKI(FoodType.SIMPLE, 6, 0.6f),
	YAKUMO_INARI(FoodType.SIMPLE, 6, 0.6f),
	KOISHI_MOUSSE(FoodType.SIMPLE, 6, 0.6f),//TODO effect
	BUN(FoodType.SIMPLE, 8, 0.6f),
	OYAKI(FoodType.SIMPLE, 6, 0.6f),
	TUTU_CONGEE(FoodType.SIMPLE, 6, 0.6f),

	// stick

	CANDY_APPLE(FoodType.STICK, 4, 0.3f),
	MILK_POPSICLE(FoodType.STICK, 4, 0.3f),
	BIG_POPSICLE(FoodType.STICK, 2, 0.1f),
	KINAKO_DANGO(FoodType.STICK, 12, 0.6f, new EffectEntry(ModEffects.NOURISHMENT, 1200, 0, 1)),
	MITARASHI_DANGO(FoodType.STICK, 12, 0.6f, new EffectEntry(ModEffects.NOURISHMENT, 2400, 0, 1)),
	ASSORTED_DANGO(FoodType.STICK, 12, 0.6f, new EffectEntry(ModEffects.NOURISHMENT, 2400, 0, 1)),

	// bottle
	SHAVED_ICE_OVER_RICE(FoodType.BOTTLE, 10, 0.8f, new EffectEntry(ModEffects.NOURISHMENT, 2400, 0, 1)),

	// bowl
	APAKI(FoodType.BOWL_MEAT, 12, 0.8f, new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1)),
	AVGOLEMONO(FoodType.BOWL, 8, 0.6f, List.of(
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1),
			new EffectEntry(() -> MobEffects.GLOWING, 3600, 0, 1))),
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
	POOR_GOD_SOUP(FoodType.BOWL, 6, 0.5f, List.of(
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1),
			new EffectEntry(() -> MobEffects.UNLUCK, 3600, 0, 0.3f)
	)),
	POWER_SOUP(FoodType.BOWL_MEAT, 16, 0.6f, new EffectEntry(ModEffects.COMFORT, 6000, 0, 1)),
	SHIRAYUKI(FoodType.BOWL_MEAT, 12, 0.8f, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1),
			new EffectEntry(() -> MobEffects.NIGHT_VISION, 3600, 0, 1)
	)),
	SWEET_ORMOSIA_MOCHI_MIXED_BOILED(FoodType.BOWL, 8, 0.8f, new EffectEntry(ModEffects.COMFORT, 3600, 0, 1)),

	// flesh
	FLESH_DUMPLINGS(FoodType.FLESH, 5, 0.8f, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 2400, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 2400, 0, 1)
	)),
	FLESH_ROLL(FoodType.FLESH, 3, 0.8f,
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1)),
	FLESH_STEW(FoodType.BOWL_FLESH, 7, 0.8f, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 6000, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 6000, 0, 1)
	)),
	BOWL_OF_FLESH_FEAST(FoodType.BOWL_FLESH, 5, 0.8f, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 6000, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 6000, 0, 1)
	)),
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
