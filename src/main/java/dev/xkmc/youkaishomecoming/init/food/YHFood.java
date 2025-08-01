package dev.xkmc.youkaishomecoming.init.food;

import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.youkaishomecoming.compat.diet.DietTagGen;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.data.YHTagGen;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.registry.ModEffects;
import vectorwing.farmersdelight.common.tag.ModTags;

import java.util.List;
import java.util.Locale;

public enum YHFood implements ItemLike {

	// basic
	RAW_LAMPREY(FoodType.SIMPLE, 2, 0.3f,
			new EffectEntry(() -> MobEffects.NIGHT_VISION, 2400, 0, 0.5f),
			ItemTags.FISHES, YHTagGen.RAW_EEL, DietTagGen.PROTEINS.tag
	),
	ROASTED_LAMPREY(FoodType.SIMPLE, 10, 0.8f,
			new EffectEntry(() -> MobEffects.NIGHT_VISION, 2400, 0, 1),
			YHTagGen.COOKED_EEL, DietTagGen.PROTEINS.tag
	),
	RAW_LAMPREY_FILLET(FoodType.FAST, 1, 0.3f,
			new EffectEntry(() -> MobEffects.NIGHT_VISION, 1800, 0, 0.5f),
			YHTagGen.RAW_EEL, DietTagGen.PROTEINS.tag
	),
	ROASTED_LAMPREY_FILLET(FoodType.FAST, 6, 0.8f,
			new EffectEntry(() -> MobEffects.NIGHT_VISION, 1800, 0, 1),
			YHTagGen.COOKED_EEL, ModTags.CABBAGE_ROLL_INGREDIENTS, DietTagGen.PROTEINS.tag
	),

	RAW_TUNA(FoodType.SIMPLE, 9, 0.3f,
			new EffectEntry(() -> MobEffects.DOLPHINS_GRACE, 800, 0, 0.3f),
			YHTagGen.RAW_TUNA, DietTagGen.PROTEINS.tag
	),
	SEARED_TUNA(FoodType.SIMPLE, 12, 0.8f,
			new EffectEntry(() -> MobEffects.DOLPHINS_GRACE, 800, 0, 0.4f),
			YHTagGen.COOKED_TUNA, DietTagGen.PROTEINS.tag
	),
	RAW_TUNA_SLICE(FoodType.FAST, 4, 0.3f,
			new EffectEntry(() -> MobEffects.DOLPHINS_GRACE, 400, 0, 0.3f),
			YHTagGen.RAW_TUNA, ModTags.CABBAGE_ROLL_INGREDIENTS, DietTagGen.PROTEINS.tag
	),
	SEARED_TUNA_SLICE(FoodType.FAST, 5, 0.8f,
			new EffectEntry(() -> MobEffects.DOLPHINS_GRACE, 400, 0, 0.4f),
			YHTagGen.COOKED_TUNA, ModTags.CABBAGE_ROLL_INGREDIENTS, DietTagGen.PROTEINS.tag
	),
	OTORO(FoodType.SIMPLE, 9, 0.3f,
			new EffectEntry(() -> MobEffects.DOLPHINS_GRACE, 800, 0, 0.5f),
			YHTagGen.OTORO, DietTagGen.PROTEINS.tag
	),
	CRAB(FoodType.SIMPLE, 2, 0.3f,
			new EffectEntry(YHEffects.CRABY, 600, 0, 0.2f),
			YHTagGen.RAW_CRAB, DietTagGen.PROTEINS.tag
	),
	STEAMED_CRAB(FoodType.SIMPLE, 6, 0.8f,
			new EffectEntry(YHEffects.CRABY, 600, 0, 0.3f),
			YHTagGen.COOKED_CRAB, DietTagGen.PROTEINS.tag
	),
	CRAB_MEAT(FoodType.SIMPLE, 6, 0.8f,
			new EffectEntry(YHEffects.CRABY, 600, 0, 0.5f),
			DietTagGen.PROTEINS.tag
	),
	CRAB_ROE(FoodType.SIMPLE, 6, 0.8f,
			new EffectEntry(YHEffects.CRABY, 600, 1, 0.5f),
			YHTagGen.CRAB_ROE, DietTagGen.PROTEINS.tag
	),
	RAW_VENISON(FoodType.SIMPLE, 4, 0.3f,
			YHTagGen.RAW_VENISON, DietTagGen.PROTEINS.tag
	),
	GRILLED_VENISON(FoodType.SIMPLE, 10, 0.8f,
			YHTagGen.COOKED_VENISON, DietTagGen.PROTEINS.tag
	),
	RAW_VENISON_SLICE(FoodType.FAST, 2, 0.3f,
			YHTagGen.RAW_VENISON, DietTagGen.PROTEINS.tag
	),
	GRILLED_VENISON_SLICE(FoodType.FAST, 6, 0.8f,
			YHTagGen.COOKED_VENISON, ModTags.CABBAGE_ROLL_INGREDIENTS, DietTagGen.PROTEINS.tag
	),
	COOKED_MANDRAKE_ROOT(FoodType.SIMPLE, 4, 0.6f, DietTagGen.VEGETABLES.tag),

	ROE(FoodType.SIMPLE, 1, 0.6f,
			new EffectEntry(() -> MobEffects.CONDUIT_POWER, 400, 0, 0.3f),
			YHTagGen.SALMON_ROE, DietTagGen.PROTEINS.tag),
	BUTTER(FoodType.SIMPLE, 3, 0.3f, YHTagGen.BUTTER),
	TOFU(FoodType.SIMPLE, 4, 0.5f, DietTagGen.PROTEINS.tag),
	OILY_BEAN_CURD(FoodType.SIMPLE, 4, 0.8f, DietTagGen.PROTEINS.tag),
	IMITATION_CRAB(FoodType.SIMPLE, 6, 1f, YHTagGen.IMITATION_CRAB, DietTagGen.PROTEINS.tag, DietTagGen.GRAINS.tag),
	TAMAGOYAKI(FoodType.SIMPLE, 6, 0.6f, YHTagGen.TAMAGOYAKI, DietTagGen.PROTEINS.tag),
	TAMAGOYAKI_SLICE(FoodType.SIMPLE, 3, 0.6f, YHTagGen.TAMAGOYAKI, YHTagGen.TAMAGOYAKI_SLICE, DietTagGen.PROTEINS.tag),
	NATTOU(FoodType.SIMPLE, 4, 0.6f, YHTagGen.NATTOU, DietTagGen.PROTEINS.tag),
	CUCUMBER_SLICE(FoodType.FAST, 1, 0.3f, YHTagGen.CUCUMBER_SLICE, DietTagGen.VEGETABLES.tag),
	RAISIN(FoodType.FAST, 3, 0.6f, DietTagGen.VEGETABLES.tag),

	// mochi
	MOCHI(FoodType.FAST, 4, 0.6f, YHTagGen.DANGO, DietTagGen.GRAINS.tag),
	TSUKIMI_DANGO(FoodType.FAST, 3, 0.6f, YHTagGen.DANGO, DietTagGen.GRAINS.tag),
	COFFEE_MOCHI(FoodType.FAST, 4, 0.6f, List.of(
			new EffectEntry(YHEffects.CAFFEINATED, 1200, 0, 1),
			new EffectEntry(YHEffects.SOBER, 1200, 0, 1)
	), YHTagGen.DANGO, DietTagGen.GRAINS.tag),
	MATCHA_MOCHI(FoodType.FAST, 4, 0.6f, List.of(
			new EffectEntry(YHEffects.TEA, 1200, 0, 1)
	), YHTagGen.DANGO, DietTagGen.GRAINS.tag),
	SAKURA_MOCHI(FoodType.FAST, 4, 0.6f, YHTagGen.DANGO, DietTagGen.GRAINS.tag),
	YASHOUMA_DANGO(FoodType.FAST, 8, 0.6f, YHTagGen.DANGO, DietTagGen.GRAINS.tag),

	// simple
	ONIGILI(FoodType.SIMPLE, 6, 0.6f, DietTagGen.GRAINS.tag),
	SENBEI(FoodType.SIMPLE, 6, 0.6f, DietTagGen.GRAINS.tag),
	SEKIBANKIYAKI(FoodType.SIMPLE, 6, 0.6f, DietTagGen.GRAINS.tag),
	YAKUMO_INARI(FoodType.SIMPLE, 6, 0.6f, DietTagGen.GRAINS.tag),
	KOISHI_MOUSSE(FoodType.SIMPLE, 8, 0.6f,
			new EffectEntry(YHEffects.UNCONSCIOUS, 400, 0, 1),
			DietTagGen.SUGARS.tag),
	MANTOU(FoodType.SIMPLE, 8, 0.6f, DietTagGen.GRAINS.tag),
	BUN(FoodType.SIMPLE, 10, 0.8f, "raw_bun", DietTagGen.GRAINS.tag),
	OYAKI(FoodType.SIMPLE, 8, 0.6f, "raw_oyaki", DietTagGen.GRAINS.tag),
	PORK_RICE_BALL(FoodType.SIMPLE, 10, 0.6f, DietTagGen.GRAINS.tag, DietTagGen.PROTEINS.tag),
	STEAMED_EGG_IN_BAMBOO(FoodType.SIMPLE, 8, 0.6f, DietTagGen.PROTEINS.tag),
	DOUGHNUT(FoodType.SIMPLE, 8, 0.6f, DietTagGen.GRAINS.tag),
	KABAYAKI(FoodType.FAST, 8, 0.8f, List.of(
			new EffectEntry(() -> MobEffects.NIGHT_VISION, 2400, 0, 1),
			new EffectEntry(ModEffects.NOURISHMENT, 2400, 0, 1)),
			YHTagGen.COOKED_EEL, DietTagGen.PROTEINS.tag
	),
	HIGI_CHOCOLATE(FoodType.SIMPLE, 4, 0.8f, List.of(
			new EffectEntry(YHEffects.HIGI, 1200, 0, 1)
	), DietTagGen.SUGARS.tag),
	HIGI_DOUGHNUT(FoodType.SIMPLE, 10, 0.8f, List.of(
			new EffectEntry(YHEffects.HIGI, 1200, 0, 1),
			new EffectEntry(ModEffects.NOURISHMENT, 600, 0, 1)
	), DietTagGen.GRAINS.tag, DietTagGen.SUGARS.tag),

	FAIRY_CANDY(FoodType.FAST, 2, 0.6f, List.of(
			new EffectEntry(YHEffects.FAIRY, 1200, 0, 1)
	), DietTagGen.SUGARS.tag),

	// stick

	CANDY_APPLE(FoodType.STICK, 6, 0.5f, DietTagGen.SUGARS.tag, DietTagGen.FRUITS.tag),
	MILK_POPSICLE(FoodType.STICK, 6, 0.5f, DietTagGen.SUGARS.tag),
	BIG_POPSICLE(FoodType.STICK, 3, 0.1f, DietTagGen.SUGARS.tag),
	KINAKO_DANGO(FoodType.STICK, 12, 0.6f, new EffectEntry(ModEffects.NOURISHMENT, 1200, 0, 1), DietTagGen.GRAINS.tag),
	MITARASHI_DANGO(FoodType.STICK, 12, 0.6f, new EffectEntry(ModEffects.NOURISHMENT, 2400, 0, 1), DietTagGen.GRAINS.tag),
	ASSORTED_DANGO(FoodType.STICK, 12, 0.6f, new EffectEntry(ModEffects.NOURISHMENT, 2400, 0, 1), DietTagGen.GRAINS.tag),

	LAMPREY_SKEWER(FoodType.STICK, 14, 0.8f, List.of(
			new EffectEntry(() -> MobEffects.NIGHT_VISION, 4800, 0, 1),
			new EffectEntry(ModEffects.NOURISHMENT, 4800, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 4800, 0, 1)),
			DietTagGen.PROTEINS.tag
	),
	// bottle
	SHAVED_ICE_OVER_RICE(FoodType.BOTTLE, 10, 0.8f, new EffectEntry(ModEffects.NOURISHMENT, 2400, 0, 1), DietTagGen.GRAINS.tag),

	// bowl
	APAKI(FoodType.BOWL, 12, 0.8f, new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1), DietTagGen.PROTEINS.tag),
	AVGOLEMONO(FoodType.BOWL, 8, 0.6f, List.of(
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1),
			new EffectEntry(() -> MobEffects.GLOWING, 3600, 0, 1)), DietTagGen.FRUITS.tag),
	BLAZING_RED_CURRY(FoodType.BOWL, 10, 0.8f, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1),
			new EffectEntry(() -> MobEffects.FIRE_RESISTANCE, 3600, 0, 1)
	), DietTagGen.GRAINS.tag, DietTagGen.PROTEINS.tag, DietTagGen.VEGETABLES.tag),
	GRILLED_EEL_OVER_RICE(FoodType.BOWL, 12, 0.8f, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1),
			new EffectEntry(() -> MobEffects.NIGHT_VISION, 3600, 0, 1)
	), DietTagGen.GRAINS.tag, DietTagGen.PROTEINS.tag),
	LONGEVITY_NOODLES(FoodType.BOWL, 12, 0.8f, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1)
	), DietTagGen.GRAINS.tag, DietTagGen.PROTEINS.tag, DietTagGen.VEGETABLES.tag),
	SHIRAYUKI(FoodType.BOWL, 12, 0.8f, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 6000, 0, 1),
			new EffectEntry(() -> MobEffects.WATER_BREATHING, 6000, 0, 1)
	), DietTagGen.PROTEINS.tag, DietTagGen.VEGETABLES.tag),
	HAN_PALACE(FoodType.BOWL, 12, 0.8f, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 6000, 0, 1),
			new EffectEntry(() -> MobEffects.NIGHT_VISION, 6000, 0, 1)
	), DietTagGen.PROTEINS.tag, DietTagGen.VEGETABLES.tag),
	TOFU_CRAB_STEW(FoodType.BOWL, 12, 0.8f, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 6000, 0, 1),
			new EffectEntry(YHEffects.CRABY, 6000, 0, 1)
	), DietTagGen.PROTEINS.tag, DietTagGen.VEGETABLES.tag),
	SWEET_ORMOSIA_MOCHI_MIXED_BOILED(FoodType.BOWL, 8, 0.8f, new EffectEntry(ModEffects.COMFORT, 3600, 0, 1),
			DietTagGen.GRAINS.tag),
	TUSCAN_SALMON(FoodType.BOWL, 12, 0.8f, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1)
	), DietTagGen.PROTEINS.tag, DietTagGen.VEGETABLES.tag),
	HONEY_GLAZED_CUCUMBER(FoodType.BOWL, 10, 0.8f, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1)
	), DietTagGen.VEGETABLES.tag),
	LIONS_HEAD(FoodType.BOWL, 12, 0.8f, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1),
			new EffectEntry(() -> MobEffects.DAMAGE_RESISTANCE, 3600, 0, 1)
	), DietTagGen.PROTEINS.tag, DietTagGen.VEGETABLES.tag),
	MAPO_TOFU(FoodType.BOWL, 12, 0.8f, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1),
			new EffectEntry(() -> MobEffects.DAMAGE_BOOST, 3600, 0, 1)
	), DietTagGen.PROTEINS.tag),
	UDUMBARA_CAKE(FoodType.BOWL, 8, 0.6f, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1),
			new EffectEntry(YHEffects.UDUMBARA, 3600, 0, 1)), DietTagGen.VEGETABLES.tag),
	BOWL_OF_HEART_THROBBING_SURPRISE(FoodType.BOWL, 12, 0.8f, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 6000, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 6000, 0, 1),
			new EffectEntry(YHEffects.UDUMBARA, 2400, 1, 1)
	), DietTagGen.VEGETABLES.tag),
	// flesh

	FLESH(FoodType.FLESH, 2, 0.3f, YHTagGen.RAW_FLESH, YHTagGen.APPARENT_FLESH_FOOD, DietTagGen.PROTEINS.tag),
	COOKED_FLESH(FoodType.FLESH, 5, 0.8f, YHTagGen.APPARENT_FLESH_FOOD, DietTagGen.PROTEINS.tag),
	FLESH_SLICE(FoodType.FLESH_FAST, 1, 0.3f, YHTagGen.RAW_FLESH, YHTagGen.APPARENT_FLESH_FOOD, DietTagGen.PROTEINS.tag),
	COOKED_FLESH_SLICE(FoodType.FLESH_FAST, 2, 0.8f, YHTagGen.APPARENT_FLESH_FOOD, DietTagGen.PROTEINS.tag),
	FLESH_DUMPLINGS(FoodType.FLESH, 2, 0.8f, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 2400, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 2400, 0, 1)
	), DietTagGen.PROTEINS.tag, DietTagGen.GRAINS.tag),
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


	private final String id;
	public final ItemEntry<Item> raw, item;

	private final FoodType type;

	@SafeVarargs
	YHFood(FoodType type, int nutrition, float sat, @Nullable String raw, List<EffectEntry> effs, TagKey<Item>... tags) {
		this.type = type;
		String name = name().toLowerCase(Locale.ROOT);
		id = FoodRegistryHelper.getId(type, tags);
		if (raw == null) this.raw = null;
		else {
			String rid = "item/" + id + raw;
			this.raw = YoukaisHomecoming.REGISTRATE.item(raw, Item::new)
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc(rid)))
					.register();
		}
		item = type.build(id, name, nutrition, sat, tags, effs);
	}

	@SafeVarargs
	YHFood(FoodType type, int nutrition, float sat, List<EffectEntry> effs, TagKey<Item>... tags) {
		this(type, nutrition, sat, null, effs, tags);
	}

	@SafeVarargs
	YHFood(FoodType type, int nutrition, float sat, EffectEntry eff, TagKey<Item>... tags) {
		this(type, nutrition, sat, List.of(eff), tags);
	}

	@SafeVarargs
	YHFood(FoodType type, int nutrition, float sat, String raw, TagKey<Item>... tags) {
		this(type, nutrition, sat, raw, List.of(), tags);

	}

	@SafeVarargs
	YHFood(FoodType type, int nutrition, float sat, TagKey<Item>... tags) {
		this(type, nutrition, sat, List.of(), tags);
	}

	@Override
	public Item asItem() {
		return item.asItem();
	}

	private boolean isFlesh() {
		return type.isFlesh();
	}

	private boolean isUnappealing() {
		if (id.equals("food/basic/")) return true;
		return this == MANTOU;
	}

	public boolean isReimuFood() {
		return !isFlesh() && !isUnappealing();
	}

	public static void register() {

	}

}
