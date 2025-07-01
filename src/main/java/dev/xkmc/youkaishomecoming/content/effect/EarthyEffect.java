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
	public void applyEffectTick(LivingEntity e, int amp) {
		if (!(e instanceof Player player)) return;
		var food = player.getFoodData();
		if (food.getFoodLevel() > 18) {
			if (e.getHealth() < e.getMaxHealth()) {
				e.heal(1);
				food.addExhaustion(4);
			}
		}
	}

	@Override
	public boolean isDurationEffectTick(int tick, int amp) {
		return tick % 10 == 0;
	}
}
