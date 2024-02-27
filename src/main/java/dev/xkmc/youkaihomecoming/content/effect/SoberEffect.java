package dev.xkmc.youkaihomecoming.content.effect;

import dev.xkmc.youkaihomecoming.init.registrate.YHEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.List;

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
