package dev.xkmc.youkaishomecoming.content.effect;

import dev.xkmc.youkaishomecoming.content.capability.PlayerStatusData;
import net.minecraft.world.effect.MobEffectCategory;

public class StatusTokenEffect extends EmptyEffect {

	public final PlayerStatusData.Status status;

	public StatusTokenEffect(MobEffectCategory category, int color, PlayerStatusData.Status status) {
		super(category, color);
		this.status = status;
	}

}
