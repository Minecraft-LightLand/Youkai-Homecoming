package dev.xkmc.youkaishomecoming.init.food;

import com.tterrag.registrate.util.entry.BlockEntry;
import dev.xkmc.youkaishomecoming.compat.diet.DietTagGen;
import dev.xkmc.youkaishomecoming.content.block.food.BowlBlock;
import dev.xkmc.youkaishomecoming.content.item.food.FoodBlockItem;
import dev.xkmc.youkaishomecoming.init.data.YHTagGen;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import vectorwing.farmersdelight.common.registry.ModEffects;

import java.util.List;
import java.util.Locale;

public enum YHBowl implements ItemLike {

	// bowl
	MISO_SOUP(FoodType.BOWL, 8, 0.5f,
			new EffectEntry(ModEffects.COMFORT, 6000, 0, 1),
			DietTagGen.VEGETABLES.tag),
	SEAFOOD_MISO_SOUP(FoodType.BOWL, 12, 0.8f, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 6000, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 6000, 0, 1)),
			DietTagGen.VEGETABLES.tag, DietTagGen.PROTEINS.tag),
	POOR_GOD_SOUP(FoodType.BOWL, 6, 0.5f, List.of(
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1),
			new EffectEntry(() -> MobEffects.UNLUCK, 3600, 0, 0.3f)
	), DietTagGen.VEGETABLES.tag),

	HIGAN_SOUP(FoodType.IRON_BOWL, 8, 0.5f,
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1),
			YHTagGen.IRON_BOWL_FOOD, DietTagGen.VEGETABLES.tag),
	MUSHROOM_SOUP(FoodType.IRON_BOWL, 8, 0.8f, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1)
	), YHTagGen.IRON_BOWL_FOOD, DietTagGen.VEGETABLES.tag),
	POWER_SOUP(FoodType.IRON_BOWL, 12, 0.8f, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 6000, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 6000, 0, 1),
			new EffectEntry(() -> MobEffects.DAMAGE_BOOST, 6000, 0, 1)
	), YHTagGen.IRON_BOWL_FOOD, DietTagGen.PROTEINS.tag, DietTagGen.VEGETABLES.tag),
	POTATO_SOUP(FoodType.IRON_BOWL, 12, 0.8f, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 6000, 0, 1)
	), YHTagGen.IRON_BOWL_FOOD, DietTagGen.GRAINS.tag, DietTagGen.VEGETABLES.tag, DietTagGen.PROTEINS.tag),
	BORSCHT(FoodType.IRON_BOWL, 8, 0.8f, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 3000, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 6000, 0, 1)
	), YHTagGen.IRON_BOWL_FOOD, DietTagGen.VEGETABLES.tag),
	SIGNATURE_MUSHROOM_STEW(FoodType.IRON_BOWL, 6, 0.8f, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 2000, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 3000, 0, 1)
	), YHTagGen.IRON_BOWL_FOOD, DietTagGen.VEGETABLES.tag),
	HOKKAIDO_SALMON_HOTPOT(FoodType.IRON_BOWL, 14, 0.8f, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 6000, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 6000, 0, 1),
			new EffectEntry(() -> MobEffects.DIG_SPEED, 6000, 0, 1)
	), YHTagGen.IRON_BOWL_FOOD, DietTagGen.VEGETABLES.tag, DietTagGen.PROTEINS.tag, DietTagGen.GRAINS.tag),

	TUTU_CONGEE(FoodType.BAMBOO_BOWL, 8, 0.6f, List.of(
			new EffectEntry(ModEffects.COMFORT, 1200, 0, 1)
	), YHTagGen.STEAM_BLOCKER, DietTagGen.VEGETABLES.tag, DietTagGen.GRAINS.tag),
	RICE_POWDER_PORK(FoodType.BAMBOO_BOWL, 14, 0.8f, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 3000, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 6000, 0, 1)
	), YHTagGen.STEAM_BLOCKER, DietTagGen.VEGETABLES.tag, DietTagGen.GRAINS.tag, DietTagGen.PROTEINS.tag),
	KAGUYA_HIME(FoodType.BAMBOO_BOWL, 14, 0.8f, List.of(
			new EffectEntry(ModEffects.COMFORT, 3000, 0, 1),
			new EffectEntry(YHEffects.SMOOTHING, 3000, 0, 1)
	), YHTagGen.STEAM_BLOCKER, DietTagGen.VEGETABLES.tag, DietTagGen.PROTEINS.tag),


	;

	private final String id;
	public final BlockEntry<BowlBlock> raw, item;

	private final FoodType type;

	@SafeVarargs
	YHBowl(FoodType type, int nutrition, float sat, List<EffectEntry> effs, TagKey<Item>... tags) {
		this.type = type;
		String name = name().toLowerCase(Locale.ROOT);
		id = FoodRegistryHelper.getId(type, tags);
		if (type == FoodType.BAMBOO_BOWL) {
			raw = buildBlock("raw_" + name, true, nutrition, sat, effs, tags);
		} else raw = null;
		item = buildBlock(name, false, nutrition, sat, effs, tags);
	}

	@SafeVarargs
	YHBowl(FoodType type, int nutrition, float sat, EffectEntry eff, TagKey<Item>... tags) {
		this(type, nutrition, sat, List.of(eff), tags);
	}

	private BlockEntry<BowlBlock> buildBlock(String name, boolean raw, int nutrition, float sat, List<EffectEntry> effs, TagKey<Item>... tags) {
		return type.bowl(name, raw)
				.item((block, p) -> new FoodBlockItem(block, type.food(p, raw ? 0 : 1, nutrition, sat, effs)))
				.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/" + id + ctx.getName())))
				.tag(tags).build()
				.register();
	}

	@Override
	public Item asItem() {
		return item.asItem();
	}

	private boolean isFlesh() {
		return type.isFlesh();
	}

	private boolean isUnappealing() {
		return switch (this) {
			case POOR_GOD_SOUP, HIGAN_SOUP -> true;
			default -> false;
		};
	}

	public boolean isReimuFood() {
		return !isFlesh() && !isUnappealing();
	}

	public static void register() {

	}
}
