package dev.xkmc.fastprojectileapi.render.standalone;

import dev.xkmc.fastprojectileapi.entity.SimplifiedProjectile;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.world.entity.LivingEntity;

public class DanmakuManager {

	public static void send(LivingEntity user, SimplifiedProjectile proj) {
		YoukaisHomecoming.HANDLER.toTrackingPlayers(new DanmakuToClientPacket(proj), user);
		//TODO erase marker
	}

}
