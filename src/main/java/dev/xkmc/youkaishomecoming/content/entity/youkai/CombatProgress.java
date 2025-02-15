package dev.xkmc.youkaishomecoming.content.entity.youkai;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.world.entity.LivingEntity;

@SerialClass
public class CombatProgress {

	@SerialClass.SerialField
	public float maxProgress;
	@SerialClass.SerialField
	public float progress;
	@SerialClass.SerialField
	public float oldProgress;

	public void init(YoukaiEntity e) {
		if (maxProgress <= 0) maxProgress = e.getMaxHealth();
		if (progress <= 0) progress = maxProgress;
	}

	public float getMaxProgress(float def) {
		return maxProgress <= 0 ? def : maxProgress;
	}

	public float getProgress() {
		return progress;
	}

	public void set(LivingEntity e, float amount) {
		progress = amount;
		if (progress != oldProgress) {
			oldProgress = progress;
			YoukaisHomecoming.HANDLER.toTrackingPlayers(new CombatToClient(e.getId(), this), e);
		}
	}

	public void setMax() {
		progress = maxProgress;
	}
}
