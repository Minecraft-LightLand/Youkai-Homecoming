package dev.xkmc.youkaishomecoming.content.capability;

import dev.xkmc.fastprojectileapi.entity.GrazingEntity;
import dev.xkmc.l2serial.network.SerialPacketBase;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.events.DanmakuGrazeEvent;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.Nullable;

public class GrazeHelper {

	public static int globalInvulTime = 0;
	public static int globalForbidTime = 0;

	public static void graze(Player entity, GrazingEntity e) {
		var graze = GrazeCapability.HOLDER.get(entity);
		if (graze.isInvul()) return;
		if (MinecraftForge.EVENT_BUS.post(new DanmakuGrazeEvent(entity, e)))
			return;
		if (graze.graze() && entity instanceof ServerPlayer sp) {
			YoukaisHomecoming.HANDLER.toClientPlayer(new GrazeToClient().set(0), sp);
		}
	}

	@Nullable
	public static LivingEntity getTarget(Player player) {
		return GrazeCapability.HOLDER.get(player).findAny(player).orElse(null);
	}

	public static boolean forbidDanmaku(Player player) {
		return GrazeCapability.HOLDER.get(player).forbidDanmaku();
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
