package dev.xkmc.youkaishomecoming.content.capability;

import dev.xkmc.l2library.capability.player.PlayerCapabilityHolder;
import dev.xkmc.l2library.capability.player.PlayerCapabilityNetworkHandler;
import dev.xkmc.l2library.capability.player.PlayerCapabilityTemplate;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.events.EffectEventHandlers;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

import java.util.List;

@SerialClass
public class GrazeCapability extends PlayerCapabilityTemplate<GrazeCapability> {

	public static final Capability<GrazeCapability> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
	});

	public static final PlayerCapabilityHolder<GrazeCapability> HOLDER = new PlayerCapabilityHolder<>(
			YoukaisHomecoming.loc("graze"), CAPABILITY,
			GrazeCapability.class, GrazeCapability::new, PlayerCapabilityNetworkHandler::new
	);

	private static final int MAX_GRAZE = 100, MAX_POWER = 400, INIT_BOMB = 15, INIT_LIFE = 25, MAX = 50, CYCLE = 3;

	@SerialClass.SerialField
	public int power, hidden, step, bomb, life;

	private boolean dirty = false;

	@Override
	public void onClone(boolean isWasDeath) {
		if (isWasDeath) {
			power = 0;
			hidden = 0;
			step = 0;
			life = INIT_LIFE;
			bomb = INIT_BOMB;
			dirty = true;
		}
	}

	@Override
	public void tick() {
		if (dirty)
			sync();
		dirty = false;
	}

	public void graze() {
		if (!EffectEventHandlers.isFullCharacter(player)) return;
		dirty = true;
		if (power < MAX_POWER) {
			power++;
			return;
		}
		hidden++;
		if (hidden < MAX_GRAZE) return;
		hidden -= MAX_GRAZE;
		step++;
		if (step == CYCLE) {
			if (life < MAX)
				life++;
			else if (bomb < MAX)
				bomb++;
			step = 0;
		} else {
			if (bomb < MAX)
				bomb++;
		}
	}

	public boolean performErase() {
		if (!EffectEventHandlers.isFullCharacter(player)) return false;
		if (bomb < 5 && life < 5) return false;
		if (useBomb()) {
			return true;
		}
		life -= 5;
		bomb = INIT_BOMB;
		power -= Math.min(power / 2, MAX_GRAZE);
		dirty = true;
		return true;
	}

	public boolean useBomb() {
		if (bomb < 5) return false;
		bomb -= 5;
		dirty = true;
		return true;
	}

	public float powerBonus() {
		if (!EffectEventHandlers.isFullCharacter(player)) return 0;
		return power * 0.0025f;
	}

	public List<InfoLine> getInfoLines() {
		if (!EffectEventHandlers.isFullCharacter(player)) return List.of();
		var icon = new InfoIcon(
				YoukaisHomecoming.loc("textures/gui/elements.png"),
				20, 20
		);
		return List.of(
				new InfoLine("%.1f".formatted(life * 0.2), icon, 0, 10),
				new InfoLine("%.1f".formatted(bomb * 0.2), icon, 0, 0),
				new InfoLine("%.2f".formatted(power * 0.01), icon, 10, 10),
				new InfoLine("%.2f".formatted(hidden * 0.01), icon, 10, 0)
		);
	}

	public void sync() {
		if (player instanceof ServerPlayer sp)
			HOLDER.network.toClientSyncAll(sp);
	}

	public static void register() {
	}

	public record InfoLine(String text, InfoIcon icon, int x, int y) {

	}

	public record InfoIcon(ResourceLocation loc, int w, int h) {

	}

}
