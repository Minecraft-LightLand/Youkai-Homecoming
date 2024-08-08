package dev.xkmc.youkaishomecoming.content.effect;

import dev.xkmc.youkaishomecoming.init.data.YHModConfig;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public class UdumbaraEffect extends MobEffect {

	public UdumbaraEffect(MobEffectCategory category, int color) {
		super(category, color);
	}

	@Override
	public boolean applyEffectTick(LivingEntity e, int lv) {
		if (e.getY() < e.level().getMinBuildHeight()) {
			e.moveTo(e.getX(), e.level().getMaxBuildHeight(), e.getZ());
			e.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 6000));
		}
		if (e.tickCount % YHModConfig.COMMON.udumbaraHealingPeriod.get() == 0 &&
				e.level().isNight() &&
				(e.level().canSeeSky(e.blockPosition().above()) || hasLantern(e))) {
			e.heal(1 << lv);
		}
		return true;
	}

	public static boolean hasLantern(LivingEntity player) {
		return player.getMainHandItem().is(YHBlocks.MOON_LANTERN.asItem()) ||
				player.getOffhandItem().is(YHBlocks.MOON_LANTERN.asItem());
	}

	@Override
	public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
		return true;
	}

}
