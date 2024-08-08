package dev.xkmc.youkaishomecoming.content.effect;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;

public class MandrakeEffect extends EmptyEffect {

	public MandrakeEffect(MobEffectCategory category, int color) {
		super(category, color);
	}

	@Override
	public boolean applyEffectTick(LivingEntity e, int lv) {
		if (e instanceof Animal animal) {
			if (animal.getAge() != 0) return true;
			if (animal.getInLoveTime() < 20) {
				animal.setInLoveTime(60);
				animal.level().broadcastEntityEvent(animal, EntityEvent.IN_LOVE_HEARTS);
			}
		}
		return true;
	}

	@Override
	public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
		return true;
	}

}
