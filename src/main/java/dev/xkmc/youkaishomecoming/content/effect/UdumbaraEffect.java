package dev.xkmc.youkaishomecoming.content.effect;

import dev.xkmc.youkaishomecoming.init.data.YHModConfig;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public class UdumbaraEffect extends EmptyEffect {

	public UdumbaraEffect(MobEffectCategory category, int color) {
		super(category, color);
	}

	@Override
	public void applyEffectTick(LivingEntity e, int lv) {
		if (e.getY() < e.level().getMinBuildHeight()) {
			e.moveTo(e.getX(), e.level().getMaxBuildHeight(), e.getZ());
			e.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 6000));
		}
		if (e.tickCount % YHModConfig.COMMON.udumbaraHealingPeriod.get() == 0 &&
				e.level().isNight() && e.level().canSeeSky(e.blockPosition().above())) {
			e.heal(1 << lv);
		}
	}

	@Override
	public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
		return true;
	}

}
