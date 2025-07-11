package dev.xkmc.youkaishomecoming.content.item;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public class CaffeinatedEffect extends MobEffect {

	public CaffeinatedEffect(MobEffectCategory pCategory, int pColor) {
		super(pCategory, pColor);
	}

	@Override
	public void applyEffectTick(LivingEntity e, int pAmplifier) {
		if (e.hasEffect(MobEffects.CONFUSION))
			e.removeEffect(MobEffects.CONFUSION);
	}

	@Override
	public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
		return true;
	}

}
