package dev.xkmc.youkaishomecoming.content.capability;

import dev.xkmc.l2library.capability.player.PlayerCapabilityHolder;
import dev.xkmc.l2library.capability.player.PlayerCapabilityNetworkHandler;
import dev.xkmc.l2library.capability.player.PlayerCapabilityTemplate;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.events.EffectEventHandlers;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

@SerialClass
public class GrazeCapability extends PlayerCapabilityTemplate<GrazeCapability> {

	public static final Capability<GrazeCapability> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
	});

	public static final PlayerCapabilityHolder<GrazeCapability> HOLDER = new PlayerCapabilityHolder<>(
			YoukaisHomecoming.loc("graze"), CAPABILITY,
			GrazeCapability.class, GrazeCapability::new, PlayerCapabilityNetworkHandler::new
	);

	private static final int MAX_GRAZE = 100, MAX_POWER = 400, INIT_BOMB = 3, INIT_LIFE = 5, MAX = 10, CYCLE = 3;

	@SerialClass.SerialField
	public int power, hidden, step, bomb, life;

	@Override
	public void onClone(boolean isWasDeath) {
		if (isWasDeath) {
			power = 0;
			hidden = 0;
			step = 0;
			life = INIT_LIFE;
			bomb = INIT_BOMB;
		}
	}

	public void graze() {
		if (!EffectEventHandlers.isFullCharacter(player)) return;
		if (power < MAX_POWER) {
			power++;
			return;
		}
		if (true) return;//TODO
		hidden++;
		if (hidden < MAX_GRAZE) return;
		hidden -= MAX_GRAZE;
		step++;
		if (step == CYCLE) {
			if (life < MAX)
				life++;
			step = 0;
		} else {
			if (bomb < MAX)
				bomb++;
		}
	}

	public boolean performErase() {
		if (!EffectEventHandlers.isFullCharacter(player)) return false;
		if (bomb <= 0 && life <= 0) return false;
		if (useBomb()) {
			return true;
		}
		life--;
		bomb = INIT_BOMB;
		power -= Math.min(power / 2, MAX_GRAZE);
		return true;
	}

	public boolean useBomb() {
		if (bomb <= 0) return false;
		bomb--;
		//TODO
		return true;
	}

	public static void register() {
	}

	public float powerBonus() {
		if (!EffectEventHandlers.isFullCharacter(player)) return 0;
		return power * 0.0025f;
	}
}
