package dev.xkmc.youkaishomecoming.init.food;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

import java.util.function.Supplier;

public record EffectEntry(Holder<MobEffect> eff,
						  int duration,
						  int amplifier,
						  float chance) {

	public EffectEntry(Holder<MobEffect> eff,
					   int duration,
					   int amplifier) {
		this(eff, duration, amplifier, 1);
	}

	public EffectEntry(Holder<MobEffect> eff,
					   int duration) {
		this(eff, duration, 0, 1);
	}

	public MobEffectInstance getEffect() {
		return new MobEffectInstance(eff, duration, amplifier);
	}
}
