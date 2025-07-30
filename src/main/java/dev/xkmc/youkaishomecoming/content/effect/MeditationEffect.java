package dev.xkmc.youkaishomecoming.content.effect;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class MeditationEffect extends EmptyEffect {

	public MeditationEffect(MobEffectCategory category, int color) {
		super(category, color);
	}

	@Override
	public void applyEffectTick(LivingEntity user, int amp) {
		if (user instanceof Player player) {
			var data = player.getPersistentData();
			long current = player.position().hashCode();
			var prev = data.getLong("MeditationHash");
			if (current == prev) {
				player.giveExperiencePoints(1);
			}
			data.putLong("MeditationHash", current);
		}
	}

	@Override
	public boolean isDurationEffectTick(int tick, int amp) {
		return tick % Math.max(10, 60 / (amp + 1)) == 0;
	}
}
