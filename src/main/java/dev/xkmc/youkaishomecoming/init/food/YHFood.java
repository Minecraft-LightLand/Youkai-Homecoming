package dev.xkmc.youkaishomecoming.init.food;

import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.youkaishomecoming.init.data.YHTagGen;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
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
	FLESH(FoodType.FLESH, 2, 0.3f, YHTagGen.RAW_FLESH, YHTagGen.APPARENT_FLESH_FOOD),
	COOKED_FLESH(FoodType.FLESH, 5, 0.8f, YHTagGen.APPARENT_FLESH_FOOD),
	COOKED_MANDRAKE_ROOT(FoodType.SIMPLE, 4, 0.6f,
			new EffectEntry(YHEffects.MANDRAKE::get, 200, 0, 1)),

	// simple
	ROE(FoodType.MEAT, 1, 0.6f),
	BUTTER(FoodType.SIMPLE, 3, 0.3f),
	TOFU(FoodType.SIMPLE, 4, 0.5f),
	OILY_BEAN_CURD(FoodType.SIMPLE, 4, 0.8f),
	MOCHI(FoodType.FAST, 4, 0.6f, YHTagGen.DANGO),
	TSUKIMI_DANGO(FoodType.FAST, 3, 0.6f, YHTagGen.DANGO),
	COFFEE_MOCHI(FoodType.FAST, 4, 0.6f, List.of(
			new EffectEntry(YHEffects.CAFFEINATED::get, 1200, 0, 1),
			new EffectEntry(YHEffects.SOBER::get, 1200, 0, 1)
	), YHTagGen.DANGO),
	MATCHA_MOCHI(FoodType.FAST, 4, 0.6f, List.of(
			new EffectEntry(YHEffects.TEA::get, 1200, 0, 1)
	), YHTagGen.DANGO),
	SAKURA_MOCHI(FoodType.FAST, 4, 0.6f, YHTagGen.DANGO),
	YASHOUMA_DANGO(FoodType.FAST, 6, 0.6f, YHTagGen.DANGO),
	ONIGILI(FoodType.SIMPLE, 6, 0.6f),
	SENBEI(FoodType.SIMPLE, 4, 0.6f),
	SEKIBANKIYAKI(FoodType.SIMPLE, 6, 0.6f),
	YAKUMO_INARI(FoodType.SIMPLE, 6, 0.6f),
	KOISHI_MOUSSE(FoodType.SIMPLE, 6, 0.6f, new EffectEntry(YHEffects.UNCONSCIOUS::get, 2400, 0, 1)),
	BUN(FoodType.SIMPLE, 8, 0.6f),
	OYAKI(FoodType.SIMPLE, 6, 0.6f),
	PORK_RICE_BALL(FoodType.MEAT, 8, 0.6f),
	TOBIKO_GUNKAN(FoodType.MEAT, 6, 0.8f, new EffectEntry(ModEffects.NOURISHMENT, 2400, 0, 1)),
	TUTU_CONGEE(FoodType.SIMPLE, 6, 0.6f),
	STEAMED_EGG_IN_BAMBOO(FoodType.MEAT, 6, 0.6f),

	// stick

	CANDY_APPLE(FoodType.STICK, 4, 0.3f),
	MILK_POPSICLE(FoodType.STICK, 4, 0.3f),
	BIG_POPSICLE(FoodType.STICK, 2, 0.1f),
	KINAKO_DANGO(FoodType.STICK, 12, 0.6f, new EffectEntry(ModEffects.NOURISHMENT, 1200, 0, 1)),
	MITARASHI_DANGO(FoodType.STICK, 12, 0.6f, new EffectEntry(ModEffects.NOURISHMENT, 2400, 0, 1)),
	ASSORTED_DANGO(FoodType.STICK, 12, 0.6f, new EffectEntry(ModEffects.NOURISHMENT, 2400, 0, 1)),

	// bottle
	SHAVED_ICE_OVER_RICE(FoodType.BOTTLE, 10, 0.8f, new EffectEntry(ModEffects.NOURISHMENT, 2400, 0, 1)),
	GREEN_TEA(FoodType.BOTTLE, 0, 0, List.of(
			new EffectEntry(YHEffects.TEA::get, 1200, 1, 1),
			new EffectEntry(YHEffects.SOBER::get, 1200, 0, 1)
	)),
	WHITE_TEA(FoodType.BOTTLE, 0, 0, List.of(
			new EffectEntry(YHEffects.TEA::get, 1200, 0, 1),
			new EffectEntry(YHEffects.SOBER::get, 1200, 0, 1),
			new EffectEntry(YHEffects.REFRESHING::get, 1200, 0, 1)
	)),
	BLACK_TEA(FoodType.BOTTLE, 0, 0, List.of(
			new EffectEntry(YHEffects.TEA::get, 1200, 0, 1),
			new EffectEntry(YHEffects.SOBER::get, 1200, 0, 1),
			new EffectEntry(YHEffects.THICK::get, 600, 0, 1)
	)),
	OOLONG_TEA(FoodType.BOTTLE, 0, 0, List.of(
			new EffectEntry(YHEffects.TEA::get, 1200, 0, 1),
			new EffectEntry(YHEffects.SOBER::get, 1200, 0, 1),
			new EffectEntry(YHEffects.SMOOTHING::get, 600, 0, 1)
	)),

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
	TUSCAN_SALMON(FoodType.BOWL_MEAT, 12, 0.8f, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1)
	)),
	MUSHROOM_SOUP(FoodType.BOWL, 8, 0.8f, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 3000, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 6000, 0, 1)
	)),
	LIONS_HEAD(FoodType.BOWL_MEAT, 12, 0.8f, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1),
			new EffectEntry(() -> MobEffects.DAMAGE_RESISTANCE, 3600, 0, 1)
	)),
	MAPO_TOFU(FoodType.BOWL_MEAT, 12, 0.8f, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1),
			new EffectEntry(() -> MobEffects.DAMAGE_BOOST, 3600, 0, 1)
	)),
	UDUMBARA_CAKE(FoodType.BOWL, 8, 0.6f, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1),
			new EffectEntry(YHEffects.UDUMBARA::get, 3600, 0, 1))),
	BOWL_OF_HEART_THROBBING_SURPRISE(FoodType.BOWL, 12, 0.8f, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 6000, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 6000, 0, 1),
			new EffectEntry(YHEffects.UDUMBARA::get, 2400, 1, 1)
	)),
	// flesh
	FLESH_DUMPLINGS(FoodType.FLESH, 5, 0.8f, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 2400, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 2400, 0, 1)
	)),
	FLESH_ROLL(FoodType.FLESH, 3, 0.8f,
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1)
			, YHTagGen.APPARENT_FLESH_FOOD),
	FLESH_STEW(FoodType.BOWL_FLESH, 7, 0.8f, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 6000, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 6000, 0, 1)
	), YHTagGen.APPARENT_FLESH_FOOD),
	BOWL_OF_FLESH_FEAST(FoodType.BOWL_FLESH, 5, 0.8f, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 6000, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 6000, 0, 1)
	), YHTagGen.APPARENT_FLESH_FOOD),
	;


	public final ItemEntry<Item> item;

	@SafeVarargs
	YHFood(FoodType type, int nutrition, float sat, List<EffectEntry> effs, TagKey<Item>... tags) {
		String name = name().toLowerCase(Locale.ROOT);
		item = type.build("food/", name, nutrition, sat, tags, effs);
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
