package dev.xkmc.youkaishomecoming.content.capability;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.codec.TagCodec;
import dev.xkmc.youkaishomecoming.init.registrate.YHSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.DustColorTransitionOptions;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ClientCapHandler {

	public static void frogUpdate(FrogSyncPacket packet) {
		Level level = Minecraft.getInstance().level;
		if (level != null) {
			Entity entity = level.getEntity(packet.id);
			if (entity instanceof Frog frog) {
				if (FrogGodCapability.HOLDER.isProper(frog)) {
					TagCodec.fromTag(packet.tag, FrogGodCapability.class,
							FrogGodCapability.HOLDER.get(frog),
							SerialClass.SerialField::toClient);
				}
			}
		}
	}

	public static void koishiAttack(KoishiStartPacket.Type type, Vec3 pos) {
		Player player = Minecraft.getInstance().player;
		if (player == null) return;
		if (type == KoishiStartPacket.Type.START)
			player.playSound(YHSounds.KOISHI_RING.get(), 1, 1);
		else if (type == KoishiStartPacket.Type.PARTICLE) {
			KoishiAttackCapability.HOLDER.get(player).startParticle(pos);
		}
	}

	public static void showParticle(Player player, Vec3 source) {
		var r = player.level().random;
		var p = source.add(new Vec3(r.nextDouble() * 2 - 1, r.nextDouble() * 2 - 1, r.nextDouble() * 2 - 1)
				.normalize().scale(0.3));
		player.level().addAlwaysVisibleParticle(DustColorTransitionOptions.SCULK_TO_REDSTONE, p.x, p.y, p.z, 0, 0, 0);
	}

	public static void playGraze() {
		var player = Minecraft.getInstance().player;
		if (player == null) return;
		var r = player.getRandom();
		player.playSound(YHSounds.GRAZE.get(), r.nextFloat() * 0.2f + 1, r.nextFloat() * 0.2f + 1f);
	}

	public static void playMiss() {
		var player = Minecraft.getInstance().player;
		if (player == null) return;
		player.playSound(YHSounds.MISS.get(), 1, 1);
	}

}
