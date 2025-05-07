package dev.xkmc.fastprojectileapi.render.virtual;

import dev.xkmc.fastprojectileapi.entity.SimplifiedProjectile;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.world.entity.LivingEntity;

public class DanmakuManager {

	public static void send(LivingEntity user, SimplifiedProjectile proj) {
		YoukaisHomecoming.HANDLER.toTrackingPlayers(new DanmakuToClientPacket(proj), user);
	}

	public static void erase(LivingEntity user, SimplifiedProjectile proj, boolean kill) {
		YoukaisHomecoming.HANDLER.toTrackingPlayers(new EraseDanmakuToClient(proj, kill), user);
	}

}
