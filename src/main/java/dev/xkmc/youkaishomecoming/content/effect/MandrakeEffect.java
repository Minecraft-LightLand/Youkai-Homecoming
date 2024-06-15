package dev.xkmc.youkaishomecoming.content.effect;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.npc.Villager;

public class MandrakeEffect extends EmptyEffect {

	public MandrakeEffect(MobEffectCategory category, int color) {
		super(category, color);
	}

	@Override
	public void applyEffectTick(LivingEntity e, int lv) {
		if (e instanceof Animal animal) {
			if (animal.getInLoveTime() < 20) {
				animal.setInLoveTime(60);
				animal.level().broadcastEntityEvent(animal, EntityEvent.IN_LOVE_HEARTS);
			}
		}
	}

	@Override
	public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
		return true;
	}

}
