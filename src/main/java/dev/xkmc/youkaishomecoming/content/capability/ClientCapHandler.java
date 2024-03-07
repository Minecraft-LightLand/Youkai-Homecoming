package dev.xkmc.youkaishomecoming.content.capability;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.codec.TagCodec;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.level.Level;

public class ClientCapHandler {
	public ClientCapHandler() {
	}

	public static void handle(FrogSyncPacket packet) {
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
}
