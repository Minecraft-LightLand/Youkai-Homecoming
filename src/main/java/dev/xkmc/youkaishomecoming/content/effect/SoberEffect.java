package dev.xkmc.youkaishomecoming.content.effect;

import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public class SoberEffect extends EmptyEffect {

	public SoberEffect(MobEffectCategory pCategory, int pColor) {
		super(pCategory, pColor);
	}

	@Override
	public void applyEffectTick(LivingEntity e, int pAmplifier) {
		if (e.hasEffect(MobEffects.CONFUSION))
			e.removeEffect(MobEffects.CONFUSION);
		if (e.hasEffect(YHEffects.YOUKAIFYING.get()))
			e.removeEffect(YHEffects.YOUKAIFYING.get());
		if (e.hasEffect(YHEffects.YOUKAIFIED.get()))
			e.removeEffect(YHEffects.YOUKAIFIED.get());
	}

	@Override
	public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
		return true;
	}

}
