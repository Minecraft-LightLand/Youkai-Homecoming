package dev.xkmc.youkaishomecoming.content.effect;

import dev.xkmc.youkaishomecoming.init.data.YHModConfig;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class UdumbaraEffect extends EmptyEffect {

	public UdumbaraEffect(MobEffectCategory category, int color) {
		super(category, color);
	}

	@Override
	public void applyEffectTick(LivingEntity e, int lv) {
		if (e.getY() < e.level().getMinBuildHeight()) {
			e.setPos(e.getX(), e.level().getMaxBuildHeight(), e.getZ());
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
