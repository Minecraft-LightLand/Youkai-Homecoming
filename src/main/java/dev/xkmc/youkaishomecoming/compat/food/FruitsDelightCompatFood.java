package dev.xkmc.youkaishomecoming.compat.food;

import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.fruitsdelight.init.registrate.FDEffects;
import dev.xkmc.youkaishomecoming.compat.diet.DietTagGen;
import dev.xkmc.youkaishomecoming.init.data.YHTagGen;
import dev.xkmc.youkaishomecoming.init.food.EffectEntry;
import dev.xkmc.youkaishomecoming.init.food.FoodType;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import vectorwing.farmersdelight.common.registry.ModEffects;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public enum FruitsDelightCompatFood {
	MOON_ROCKET(FoodType.BOTTLE_FAST, 0, 0,
			new EffectEntry(() -> MobEffects.JUMP, 100, 1)),
	LEMON_BLACK_TEA(FoodType.BOTTLE_FAST, 0, 0, List.of(
			new EffectEntry(YHEffects.TEA::get, 600, 0, 1),
			new EffectEntry(YHEffects.SOBER::get, 600, 0, 1),
			new EffectEntry(YHEffects.THICK::get, 600, 0, 1)
	)),
	PEACH_TAPIOCA(FoodType.BOWL, 6, 0.6f, List.of(
			new EffectEntry(FDEffects.HEAL_AURA::get, 100, 0),
			new EffectEntry(ModEffects.COMFORT, 1200, 0)
	), DietTagGen.FRUITS.tag),
	PEACH_YATSUHASHI(FoodType.SIMPLE, 6, 0.6f, List.of(
			new EffectEntry(FDEffects.HEAL_AURA::get, 100, 0),
			new EffectEntry(ModEffects.NOURISHMENT, 600, 0)
	), DietTagGen.GRAINS.tag, DietTagGen.FRUITS.tag);

	public final ItemEntry<Item> item;

	@SafeVarargs
	FruitsDelightCompatFood(FoodType type, int nutrition, float sat, List<EffectEntry> effs, TagKey<Item>... tags) {
		String name = name().toLowerCase(Locale.ROOT);
		item = type.build("fruitsdelight/", name, nutrition, sat, new TagKey[0], effs);
		YHTagGen.OPTIONAL_TAGS.add(e -> Arrays.stream(tags).forEach(x -> e.addTag(x).addOptional(item.getId())));
	}

	@SafeVarargs
	FruitsDelightCompatFood(FoodType type, int nutrition, float sat, EffectEntry eff, TagKey<Item>... tags) {
		this(type, nutrition, sat, List.of(eff), tags);
	}

	public static void register() {

	}

}
