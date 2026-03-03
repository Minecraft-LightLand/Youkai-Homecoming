package dev.xkmc.youkaishomecoming.content.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class EarthyEffect extends MobEffect {

	public EarthyEffect(MobEffectCategory category, int col) {
		super(category, col);
	}

	@Override
	public boolean applyEffectTick(LivingEntity e, int amp) {
		if (!(e instanceof Player player)) return false;
		var food = player.getFoodData();
		if (food.getFoodLevel() > 18) {
			if (e.getHealth() < e.getMaxHealth()) {
				e.heal(1);
				food.addExhaustion(4);
			}
		}
		return true;
	}

	@Override
	public boolean shouldApplyEffectTickThisTick(int tick, int amp) {
		return tick % Math.max(1, 10 / (amp + 1)) == 0;
	}
}
