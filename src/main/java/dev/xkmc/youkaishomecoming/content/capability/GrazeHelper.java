package dev.xkmc.youkaishomecoming.content.capability;

import dev.xkmc.fastprojectileapi.entity.GrazingEntity;
import dev.xkmc.l2serial.network.SerialPacketBase;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.events.DanmakuGrazeEvent;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.network.NetworkEvent;

public class GrazeHelper {

	public static void graze(Player entity, GrazingEntity e) {
		MinecraftForge.EVENT_BUS.post(new DanmakuGrazeEvent(entity, e));
		var prev = entity.getPersistentData().getLong("GrazeTimeStamp");
		if (entity.level().getGameTime() > prev) {
			entity.getPersistentData().putLong("GrazeTimeStamp", entity.level().getGameTime());
			if (entity instanceof ServerPlayer sp) {
				YoukaisHomecoming.HANDLER.toClientPlayer(new GrazeToClient(), sp);
			}
		}
		GrazeCapability.HOLDER.get(entity).graze();
	}

	@SerialClass
	public static class GrazeToClient extends SerialPacketBase {

		@Override
		public void handle(NetworkEvent.Context context) {
			ClientCapHandler.playGraze();
		}

	}

}
