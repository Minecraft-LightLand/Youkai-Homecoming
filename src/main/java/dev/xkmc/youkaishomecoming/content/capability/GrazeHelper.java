package dev.xkmc.youkaishomecoming.content.capability;

import dev.xkmc.fastprojectileapi.entity.GrazingEntity;
import dev.xkmc.l2serial.network.SerialPacketBase;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.entity.youkai.YoukaiEntity;
import dev.xkmc.youkaishomecoming.events.DanmakuGrazeEvent;
import dev.xkmc.youkaishomecoming.events.EffectEventHandlers;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.data.YHModConfig;
import dev.xkmc.youkaishomecoming.init.registrate.YHAttributes;
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

	public static void addSession(Player player, LivingEntity target) {
		if (player.level().isClientSide()) return;
		if (!EffectEventHandlers.isFullCharacter(player)) return;
		var cap = GrazeCapability.HOLDER.get(player);
		if (!(target instanceof YoukaiEntity e)) return;
		if (e.targets.contains(player)) return;
		cap.initSession(e);
	}

	public static boolean forbidDanmaku(Player player) {
		var cap = GrazeCapability.HOLDER.get(player);
		return cap.isInvul() || cap.isWeak();
	}

	public static void onDanmakuKill(Player player, YoukaiEntity e) {
		 GrazeCapability.HOLDER.get(player).stopSession(e.getUUID());
	}

	public static int getInitialResource(Player player) {
		return YHModConfig.COMMON.initialResource.get() +
				(int) player.getAttributeValue(YHAttributes.INITIAL_RESOURCE.get());
	}

	public static int getInitialPower(Player player) {
		return YHModConfig.COMMON.initialPower.get() +
				(int) player.getAttributeValue(YHAttributes.INITIAL_POWER.get());
	}

	public static int getMaxPower(Player player) {
		return YHModConfig.COMMON.danmakuMaxPower.get() +
				(int) player.getAttributeValue(YHAttributes.MAX_POWER.get());
	}

	public static double getGrazeEffectiveness(Player player) {
		return YHModConfig.COMMON.grazeEffectiveness.get() +
				player.getAttributeValue(YHAttributes.GRAZE_EFFECTIVENESS.get());
	}

	public static int getMaxResource(Player player) {
		return YHModConfig.COMMON.danmakuMaxResource.get() +
				(int) player.getAttributeValue(YHAttributes.MAX_RESOURCE.get());
	}

	public static float getHitBoxDelta(Player player) {
		return (float) player.getAttributeValue(YHAttributes.HITBOX.get());
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
