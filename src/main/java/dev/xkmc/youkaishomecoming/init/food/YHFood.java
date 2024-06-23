package dev.xkmc.youkaishomecoming.init.food;

import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.youkaishomecoming.compat.diet.DietTagGen;
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

	// basic
	RAW_LAMPREY(FoodType.MEAT, 2, 0.3f,
			new EffectEntry(() -> MobEffects.NIGHT_VISION, 2400, 0, 0.5f),
			ItemTags.FISHES, YHTagGen.RAW_EEL, DietTagGen.PROTEINS.tag
	),
	ROASTED_LAMPREY(FoodType.MEAT, 10, 0.8f,
			new EffectEntry(() -> MobEffects.NIGHT_VISION, 2400, 0, 1),
			DietTagGen.PROTEINS.tag
	),
	RAW_LAMPREY_FILLET(FoodType.MEAT_SLICE, 1, 0.3f,
			new EffectEntry(() -> MobEffects.NIGHT_VISION, 1800, 0, 0.5f),
			YHTagGen.RAW_EEL, ModTags.CABBAGE_ROLL_INGREDIENTS, DietTagGen.PROTEINS.tag
	),
	ROASTED_LAMPREY_FILLET(FoodType.MEAT_SLICE, 6, 0.8f,
			new EffectEntry(() -> MobEffects.NIGHT_VISION, 1800, 0, 1),
			DietTagGen.PROTEINS.tag
	),
	FLESH(FoodType.FLESH, 2, 0.3f, YHTagGen.RAW_FLESH, YHTagGen.APPARENT_FLESH_FOOD, DietTagGen.PROTEINS.tag),
	COOKED_FLESH(FoodType.FLESH, 5, 0.8f, YHTagGen.APPARENT_FLESH_FOOD, DietTagGen.PROTEINS.tag),
	COOKED_MANDRAKE_ROOT(FoodType.SIMPLE, 4, 0.6f, DietTagGen.VEGETABLES.tag),

	ROE(FoodType.MEAT, 1, 0.6f, DietTagGen.PROTEINS.tag),
	BUTTER(FoodType.SIMPLE, 3, 0.3f),
	TOFU(FoodType.SIMPLE, 4, 0.5f),
	OILY_BEAN_CURD(FoodType.SIMPLE, 4, 0.8f),

	// mochi
	MOCHI(FoodType.FAST, 4, 0.6f, YHTagGen.DANGO, DietTagGen.GRAINS.tag),
	TSUKIMI_DANGO(FoodType.FAST, 3, 0.6f, YHTagGen.DANGO, DietTagGen.GRAINS.tag),
	COFFEE_MOCHI(FoodType.FAST, 4, 0.6f, List.of(
			new EffectEntry(YHEffects.CAFFEINATED::get, 1200, 0, 1),
			new EffectEntry(YHEffects.SOBER::get, 1200, 0, 1)
	), YHTagGen.DANGO, DietTagGen.GRAINS.tag),
	MATCHA_MOCHI(FoodType.FAST, 4, 0.6f, List.of(
			new EffectEntry(YHEffects.TEA::get, 1200, 0, 1)
	), YHTagGen.DANGO, DietTagGen.GRAINS.tag),
	SAKURA_MOCHI(FoodType.FAST, 4, 0.6f, YHTagGen.DANGO, DietTagGen.GRAINS.tag),
	YASHOUMA_DANGO(FoodType.FAST, 6, 0.6f, YHTagGen.DANGO, DietTagGen.GRAINS.tag),

	// simple
	ONIGILI(FoodType.SIMPLE, 6, 0.6f, DietTagGen.GRAINS.tag),
	SENBEI(FoodType.SIMPLE, 4, 0.6f, DietTagGen.GRAINS.tag),
	SEKIBANKIYAKI(FoodType.SIMPLE, 6, 0.6f, DietTagGen.GRAINS.tag),
	YAKUMO_INARI(FoodType.SIMPLE, 6, 0.6f, DietTagGen.GRAINS.tag),
	KOISHI_MOUSSE(FoodType.SIMPLE, 6, 0.6f,
			new EffectEntry(YHEffects.UNCONSCIOUS::get, 400, 0, 1),
			DietTagGen.SUGARS.tag),
	BUN(FoodType.SIMPLE, 8, 0.8f, DietTagGen.GRAINS.tag),
	OYAKI(FoodType.SIMPLE, 6, 0.6f, DietTagGen.GRAINS.tag),
	PORK_RICE_BALL(FoodType.MEAT, 8, 0.6f, DietTagGen.GRAINS.tag, DietTagGen.PROTEINS.tag),
	TOBIKO_GUNKAN(FoodType.MEAT, 6, 0.8f, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 2400, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 2400, 0, 1)),
			DietTagGen.GRAINS.tag, DietTagGen.PROTEINS.tag),
	TUTU_CONGEE(FoodType.SIMPLE, 6, 0.6f, DietTagGen.GRAINS.tag),
	STEAMED_EGG_IN_BAMBOO(FoodType.MEAT, 6, 0.6f, DietTagGen.PROTEINS.tag),
	DOUGHNUT(FoodType.SIMPLE, 6, 0.6f, DietTagGen.GRAINS.tag),
	HIGI_CHOCOLATE(FoodType.SIMPLE, 3, 0.8f, List.of(
			new EffectEntry(YHEffects.HIGI::get, 1200, 0, 1)
	), DietTagGen.SUGARS.tag),
	HIGI_DOUGHNUT(FoodType.SIMPLE, 8, 0.8f, List.of(
			new EffectEntry(YHEffects.HIGI::get, 1200, 0, 1),
			new EffectEntry(ModEffects.NOURISHMENT, 600, 0, 1)
	), DietTagGen.GRAINS.tag, DietTagGen.SUGARS.tag),

	FAIRY_CANDY(FoodType.FAST, 2, 0.6f, List.of(
			new EffectEntry(YHEffects.FAIRY::get, 1200, 0, 1)
	), DietTagGen.SUGARS.tag),

	// stick

	CANDY_APPLE(FoodType.STICK, 4, 0.3f, DietTagGen.SUGARS.tag, DietTagGen.FRUITS.tag),
	MILK_POPSICLE(FoodType.STICK, 4, 0.3f, DietTagGen.SUGARS.tag),
	BIG_POPSICLE(FoodType.STICK, 2, 0.1f, DietTagGen.SUGARS.tag),
	KINAKO_DANGO(FoodType.STICK, 12, 0.6f, new EffectEntry(ModEffects.NOURISHMENT, 1200, 0, 1), DietTagGen.GRAINS.tag),
	MITARASHI_DANGO(FoodType.STICK, 12, 0.6f, new EffectEntry(ModEffects.NOURISHMENT, 2400, 0, 1), DietTagGen.GRAINS.tag),
	ASSORTED_DANGO(FoodType.STICK, 12, 0.6f, new EffectEntry(ModEffects.NOURISHMENT, 2400, 0, 1), DietTagGen.GRAINS.tag),

	// bottle
	SHAVED_ICE_OVER_RICE(FoodType.BOTTLE, 10, 0.8f, new EffectEntry(ModEffects.NOURISHMENT, 2400, 0, 1), DietTagGen.GRAINS.tag),
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
	CORNFLOWER_TEA(FoodType.BOTTLE, 0, 0, List.of(
			new EffectEntry(() -> MobEffects.REGENERATION, 200, 0, 1)
	)),
	TEA_MOCHA(FoodType.BOTTLE, 4, 0.6f, List.of(
			new EffectEntry(YHEffects.TEA::get, 1200, 0, 1),
			new EffectEntry(YHEffects.SOBER::get, 1200, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 1200, 0, 1)
	)),
	SAIDI_TEA(FoodType.BOTTLE, 0, 0, List.of(
			new EffectEntry(YHEffects.TEA::get, 1200, 0, 1),
			new EffectEntry(YHEffects.SOBER::get, 1200, 0, 1),
			new EffectEntry(() -> MobEffects.MOVEMENT_SPEED, 1200, 0, 1)
	)),
	SAKURA_HONEY_TEA(FoodType.BOTTLE, 0, 0, List.of(
			new EffectEntry(() -> MobEffects.MOVEMENT_SPEED, 400, 0, 1),
			new EffectEntry(() -> MobEffects.REGENERATION, 400, 0, 1)
	)),
	GENMAI_TEA(FoodType.BOTTLE, 0, 0, List.of(
			new EffectEntry(YHEffects.TEA::get, 1200, 1, 1),
			new EffectEntry(YHEffects.SOBER::get, 1200, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 1200, 0, 1)
	)),
	SCARLET_TEA(FoodType.BOTTLE, 0, 0, List.of(
			new EffectEntry(YHEffects.TEA::get, 1200, 0, 1),
			new EffectEntry(YHEffects.THICK::get, 600, 0, 1),
			new EffectEntry(YHEffects.YOUKAIFYING::get, 1200, 0, 1)
	), YHTagGen.FLESH_FOOD),
	GREEN_WATER(FoodType.BOTTLE, 0, 0, List.of(new EffectEntry(YHEffects.TEA::get, 600, 0, 0.1f))),

	// bowl
	APAKI(FoodType.BOWL_MEAT, 12, 0.8f, new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1), DietTagGen.PROTEINS.tag),
	AVGOLEMONO(FoodType.BOWL, 8, 0.6f, List.of(
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1),
			new EffectEntry(() -> MobEffects.GLOWING, 3600, 0, 1)), DietTagGen.FRUITS.tag),
	BLAZING_RED_CURRY(FoodType.BOWL_MEAT, 10, 0.8f, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1),
			new EffectEntry(() -> MobEffects.FIRE_RESISTANCE, 3600, 0, 1)
	), DietTagGen.GRAINS.tag, DietTagGen.PROTEINS.tag, DietTagGen.VEGETABLES.tag),
	GRILLED_EEL_OVER_RICE(FoodType.BOWL_MEAT, 12, 0.8f, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1),
			new EffectEntry(() -> MobEffects.NIGHT_VISION, 3600, 0, 1)
	), DietTagGen.GRAINS.tag, DietTagGen.PROTEINS.tag),
	HIGAN_SOUP(FoodType.BOWL, 6, 0.5f, new EffectEntry(ModEffects.COMFORT, 3600, 0, 1), DietTagGen.VEGETABLES.tag),
	LONGEVITY_NOODLES(FoodType.BOWL_MEAT, 12, 0.8f, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1)
	), DietTagGen.GRAINS.tag, DietTagGen.PROTEINS.tag, DietTagGen.VEGETABLES.tag),
	MISO_SOUP(FoodType.BOWL, 8, 0.5f, new EffectEntry(ModEffects.COMFORT, 6000, 0, 1), DietTagGen.VEGETABLES.tag),
	SEAFOOD_MISO_SOUP(FoodType.BOWL, 12, 0.8f, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 6000, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 6000, 0, 1)),
			DietTagGen.VEGETABLES.tag, DietTagGen.PROTEINS.tag),
	POOR_GOD_SOUP(FoodType.BOWL, 6, 0.5f, List.of(
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1),
			new EffectEntry(() -> MobEffects.UNLUCK, 3600, 0, 0.3f)
	), DietTagGen.VEGETABLES.tag),
	POWER_SOUP(FoodType.BOWL_MEAT, 16, 0.6f, new EffectEntry(ModEffects.COMFORT, 6000, 0, 1),
			DietTagGen.PROTEINS.tag, DietTagGen.VEGETABLES.tag),
	SHIRAYUKI(FoodType.BOWL_MEAT, 12, 0.8f, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1),
			new EffectEntry(() -> MobEffects.NIGHT_VISION, 3600, 0, 1)
	), DietTagGen.PROTEINS.tag, DietTagGen.VEGETABLES.tag),
	SWEET_ORMOSIA_MOCHI_MIXED_BOILED(FoodType.BOWL, 8, 0.8f, new EffectEntry(ModEffects.COMFORT, 3600, 0, 1),
			DietTagGen.GRAINS.tag),
	TUSCAN_SALMON(FoodType.BOWL_MEAT, 12, 0.8f, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1)
	), DietTagGen.PROTEINS.tag, DietTagGen.VEGETABLES.tag),
	MUSHROOM_SOUP(FoodType.BOWL, 8, 0.8f, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 3000, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 6000, 0, 1)
	), DietTagGen.VEGETABLES.tag),
	LIONS_HEAD(FoodType.BOWL_MEAT, 12, 0.8f, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1),
			new EffectEntry(() -> MobEffects.DAMAGE_RESISTANCE, 3600, 0, 1)
	), DietTagGen.PROTEINS.tag, DietTagGen.VEGETABLES.tag),
	MAPO_TOFU(FoodType.BOWL_MEAT, 12, 0.8f, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1),
			new EffectEntry(() -> MobEffects.DAMAGE_BOOST, 3600, 0, 1)
	), DietTagGen.PROTEINS.tag),
	UDUMBARA_CAKE(FoodType.BOWL, 8, 0.6f, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1),
			new EffectEntry(YHEffects.UDUMBARA::get, 3600, 0, 1)), DietTagGen.VEGETABLES.tag),
	BOWL_OF_HEART_THROBBING_SURPRISE(FoodType.BOWL, 12, 0.8f, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 6000, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 6000, 0, 1),
			new EffectEntry(YHEffects.UDUMBARA::get, 2400, 1, 1)
	), DietTagGen.VEGETABLES.tag),
	// flesh
	FLESH_DUMPLINGS(FoodType.FLESH, 2, 0.8f, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 2400, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 2400, 0, 1)
	), DietTagGen.PROTEINS.tag, DietTagGen.GRAINS.tag),
	FLESH_ROLL(FoodType.FLESH, 3, 0.8f,
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1),
			YHTagGen.APPARENT_FLESH_FOOD, DietTagGen.PROTEINS.tag, DietTagGen.GRAINS.tag),
	CANNED_FLESH(FoodType.CAN_FLESH, 4, 0.8f, YHTagGen.APPARENT_FLESH_FOOD, DietTagGen.PROTEINS.tag),
	FLESH_STEW(FoodType.BOWL_FLESH, 7, 0.8f, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 6000, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 6000, 0, 1)
	), YHTagGen.APPARENT_FLESH_FOOD, DietTagGen.PROTEINS.tag, DietTagGen.VEGETABLES.tag),
	BOWL_OF_FLESH_FEAST(FoodType.BOWL_FLESH, 5, 0.8f, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 6000, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 6000, 0, 1)
	), YHTagGen.APPARENT_FLESH_FOOD, DietTagGen.PROTEINS.tag),
	FLESH_CHOCOLATE_MOUSSE(FoodType.FLESH_FAST, 3, 0.5f, DietTagGen.PROTEINS.tag, DietTagGen.SUGARS.tag),
	SCARLET_DEVIL_CAKE(FoodType.FLESH_FAST, 4, 0.5f, DietTagGen.PROTEINS.tag, DietTagGen.SUGARS.tag),
	;


	public final ItemEntry<Item> item;

	private final FoodType type;

	@SafeVarargs
	YHFood(FoodType type, int nutrition, float sat, List<EffectEntry> effs, TagKey<Item>... tags) {
		this.type = type;
		String name = name().toLowerCase(Locale.ROOT);
		String id = "food/simple/";
		if (type == FoodType.BOTTLE) id = "food/bottle/";
		if (type == FoodType.STICK) id = "food/stick/";
		if (type == FoodType.BOWL || type == FoodType.BOWL_MEAT) id = "food/bowl/";
		if (ordinal() <= 16) id = "food/mochi/";
		if (ordinal() <= 10) id = "food/basic/";
		if (type.isFlesh()) id = "food/flesh/";
		item = type.build(id, name, nutrition, sat, tags, effs);
	}

	@SafeVarargs
	YHFood(FoodType type, int nutrition, float sat, TagKey<Item>... tags) {
		this(type, nutrition, sat, List.of(), tags);
	}

	@SafeVarargs
	YHFood(FoodType type, int nutrition, float sat, EffectEntry eff, TagKey<Item>... tags) {
		this(type, nutrition, sat, List.of(eff), tags);
	}

	public boolean isFlesh() {
		return type.isFlesh() || this == SCARLET_TEA;
	}

	public boolean isReimuFood() {
		return !isFlesh() &&
				this != RAW_LAMPREY && this != RAW_LAMPREY_FILLET &&
				this != COOKED_MANDRAKE_ROOT && this != FAIRY_CANDY;
	}

	public static void register() {

	}

}
