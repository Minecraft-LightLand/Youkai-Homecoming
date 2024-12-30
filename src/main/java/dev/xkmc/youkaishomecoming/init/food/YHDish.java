package dev.xkmc.youkaishomecoming.init.food;

import com.tterrag.registrate.util.entry.BlockEntry;
import dev.xkmc.youkaishomecoming.content.block.food.FoodSaucerBlock;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import vectorwing.farmersdelight.common.registry.ModEffects;

import java.util.Locale;

public enum YHDish implements IYHDish {
	BAMBOO_MIZUYOKAN(Saucer.SAUCER_1, 6, 0.6f, 6,
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1)),
	DRIED_FISH(Saucer.SAUCER_2, 8, 0.8f, 3,
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1)),
	IMITATION_BEAR_PAW(Saucer.SAUCER_4, 12, 0.8f, 3,
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1),
			new EffectEntry(MobEffects.DAMAGE_BOOST, 3600, 1, 1),
			new EffectEntry(MobEffects.DAMAGE_RESISTANCE, 3600, 0, 1)),
	PASTITSIO(Saucer.SAUCER_2, 12, 0.8f, 3,
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1)),
	SAUCE_GRILLED_FISH(Saucer.SAUCER_4, 12, 0.8f, 3,
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1)),
	STINKY_TOFU(Saucer.SAUCER_1, 8, 0.6f, 6,
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1)),
	TOFU_BURGER(Saucer.SAUCER_2, 8, 0.6f, 3,
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1)),
	SEVEN_COLORED_YOKAN(Saucer.SAUCER_1, 8, 0.8f, 6,
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1),
			new EffectEntry(YHEffects.UDUMBARA, 3600, 1, 1)),
	;

	public final Saucer base;
	public final int height;

	public final BlockEntry<FoodSaucerBlock> block;

	YHDish(Saucer base, int nutrition, float sat, int height, EffectEntry... effs) {
		this.base = base;
		this.height = height;
		var builder = new FoodProperties.Builder()
				.nutrition(nutrition).saturationModifier(sat);
		for (var e : effs) {
			builder.effect(e::getEffect, e.chance());
		}
		var food = builder.build();
		block = buildBlock(YoukaisHomecoming.REGISTRATE, food);
	}

	@Override
	public Saucer base() {
		return base;
	}

	@Override
	public int height() {
		return height;
	}

	@Override
	public BlockEntry<FoodSaucerBlock> block() {
		return block;
	}

	public String getName() {
		return name().toLowerCase(Locale.ROOT);
	}

	public static void register() {
	}

}
