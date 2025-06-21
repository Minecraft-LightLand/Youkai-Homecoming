package dev.xkmc.youkaishomecoming.content.pot.table.food;

import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.youkaishomecoming.compat.diet.DietTagGen;
import dev.xkmc.youkaishomecoming.init.food.EffectEntry;
import dev.xkmc.youkaishomecoming.init.food.FoodType;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.registry.ModEffects;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;

public enum YHRolls {
	SHINNKO_MAKI(FoodType.SIMPLE, 6, 0.8f, FoodModelHelper::hosomaki, List.of(), DietTagGen.GRAINS.tag, DietTagGen.VEGETABLES.tag),
	//KAPPA_MAKI(FoodType.SIMPLE, 10, 0.9f, FoodModelHelper::hosomaki, List.of()),
	TEKKA_MAKI(FoodType.MEAT, 7, 1f, FoodModelHelper::hosomaki, List.of(
			new EffectEntry(() -> MobEffects.DOLPHINS_GRACE, 200, 0, 0.2f)
	), DietTagGen.PROTEINS.tag, DietTagGen.GRAINS.tag),

	EGG_FUTOMAKI(FoodType.SIMPLE, 4, 0.8f, FoodModelHelper::futomaki, List.of(), DietTagGen.PROTEINS.tag, DietTagGen.GRAINS.tag),
	SALMON_FUTOMAKI(FoodType.MEAT, 5, 1f, FoodModelHelper::futomaki, List.of(), DietTagGen.PROTEINS.tag, DietTagGen.GRAINS.tag, DietTagGen.VEGETABLES.tag),
	RAINBOW_FUTOMAKI(FoodType.MEAT, 6, 1f, FoodModelHelper::futomaki, List.of(), DietTagGen.PROTEINS.tag, DietTagGen.GRAINS.tag, DietTagGen.VEGETABLES.tag),

	CALIFORNIA_ROLL(FoodType.MEAT, 5, 0.9f, FoodModelHelper::cali, List.of(),
			DietTagGen.GRAINS.tag, DietTagGen.PROTEINS.tag, DietTagGen.VEGETABLES.tag),
	ROE_CALIFORNIA_ROLL(FoodType.MEAT, 6, 1f, null, List.of(
			new EffectEntry(() -> MobEffects.CONDUIT_POWER, 200, 0, 0.35f),
			new EffectEntry(ModEffects.NOURISHMENT, 600, 0, 1)
	), DietTagGen.GRAINS.tag, DietTagGen.PROTEINS.tag, DietTagGen.VEGETABLES.tag),
	SALMON_LOVER_ROLL(FoodType.MEAT, 7, 1.2f, null, List.of(
			new EffectEntry(() -> MobEffects.CONDUIT_POWER, 200, 0, 0.6f),
			new EffectEntry(ModEffects.NOURISHMENT, 1200, 0, 1)
	), DietTagGen.GRAINS.tag, DietTagGen.PROTEINS.tag, DietTagGen.VEGETABLES.tag),
	VOLCANO_ROLL(FoodType.MEAT, 7, 1.2f, null, List.of(
			new EffectEntry(() -> MobEffects.DOLPHINS_GRACE, 200, 0, 0.6f),
			new EffectEntry(ModEffects.NOURISHMENT, 1200, 0, 1)
	), DietTagGen.GRAINS.tag, DietTagGen.PROTEINS.tag, DietTagGen.VEGETABLES.tag),
	RAINBOW_ROLL(FoodType.MEAT, 8, 1.2f, null, List.of(
			new EffectEntry(() -> MobEffects.CONDUIT_POWER, 200, 0, 0.35f),
			new EffectEntry(() -> MobEffects.DOLPHINS_GRACE, 200, 0, 0.35f),
			new EffectEntry(ModEffects.NOURISHMENT, 1200, 0, 1)
	), DietTagGen.GRAINS.tag, DietTagGen.PROTEINS.tag, DietTagGen.VEGETABLES.tag),
	;

	@Nullable
	public final FoodTableItemHolder model;
	public final ItemEntry<Item> item, slice;

	YHRolls(FoodType type, int nutrition, float sat,
			@Nullable Function<String, FoodTableItemHolder> modelFactory,
			List<EffectEntry> effs, TagKey<Item>... tags) {
		String name = name().toLowerCase(Locale.ROOT);
		int count = getCount();
		int rollNut = nutrition * count * 2 / 3;
		item = type.build(name, rollNut, sat, tags, scale(effs, count, (int) (nutrition * count * (1 + sat * 2))))
				.model((ctx, pvd) ->
						pvd.generated(ctx, pvd.modLoc("item/food/maki/" + ctx.getName())))
				.register();
		type = switch (type) {
			case MEAT -> FoodType.MEAT_SLICE;
			case SIMPLE -> FoodType.FAST;
			case FLESH -> FoodType.FLESH_FAST;
			default -> throw new IllegalArgumentException("State should have fast variant");
		};
		slice = type.build(name + "_slice", nutrition, sat, tags, effs)
				.model((ctx, pvd) ->
						pvd.generated(ctx, pvd.modLoc("item/food/maki/" + ctx.getName())))
				.register();

		if (modelFactory != null) {
			this.model = modelFactory.apply(name);
			FoodModelHelper.map(item.getId(), model);
		} else {
			this.model = null;
		}
	}

	private List<EffectEntry> scale(List<EffectEntry> effs, int n, int sat) {
		List<EffectEntry> ans = new ArrayList<>();
		for (var e : effs) {
			var chance = e.chance();
			var dur = e.duration();
			if (chance * n < 1) {
				ans.add(new EffectEntry(e.eff(), dur, e.amplifier(), chance * n));
			} else {
				ans.add(new EffectEntry(e.eff(), (int) (dur * chance * n), e.amplifier(), 1));
			}
		}
		if (sat > 40) {
			ans.add(new EffectEntry(() -> MobEffects.SATURATION, sat - 20, 0));
		}
		return ans;
	}

	public static void init() {

	}

	public int getCount() {
		return name().endsWith("_MAKI") ? 3 : 6;
	}

	public ItemStack sliceStack() {
		return slice.asStack(getCount());
	}

}
