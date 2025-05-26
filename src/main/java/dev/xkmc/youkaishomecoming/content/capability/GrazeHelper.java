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

	public static int globalInvulTime = 0;

	public static void graze(Player entity, GrazingEntity e) {
		var graze = GrazeCapability.HOLDER.get(entity);
		if (graze.invul > 0) return;
		if (MinecraftForge.EVENT_BUS.post(new DanmakuGrazeEvent(entity, e)))
			return;
		graze.graze();
		var prev = entity.getPersistentData().getLong("GrazeTimeStamp");
		if (entity.level().getGameTime() > prev) {
			entity.getPersistentData().putLong("GrazeTimeStamp", entity.level().getGameTime());
			if (entity instanceof ServerPlayer sp) {
				YoukaisHomecoming.HANDLER.toClientPlayer(new GrazeToClient().set(0), sp);
			}
		}
	}

	@SerialClass
	public static class GrazeToClient extends SerialPacketBase {

		@SerialClass.SerialField
		public int type;

		@Override
		public void handle(NetworkEvent.Context context) {
			if (type == 0)
				ClientCapHandler.playGraze();
			else ClientCapHandler.playMiss();
		}

		public GrazeToClient set(int i) {
			type = i;
			return this;
		}
	}

}
