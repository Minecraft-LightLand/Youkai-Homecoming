package dev.xkmc.youkaishomecoming.content.capability;

import dev.xkmc.l2serial.network.SerialPacketBase;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

@SerialClass
public class KoishiStartPacket extends SerialPacketBase {

	public enum Type {
		START,
		PARTICLE
	}

	@SerialClass.SerialField
	public Type type;
	@SerialClass.SerialField
	public Vec3 pos;

	@Deprecated
	public KoishiStartPacket() {
		super();
	}

	public KoishiStartPacket(Type type, Vec3 pos) {
		this.type = type;
		this.pos = pos;
	}

	@Override
	public void handle(NetworkEvent.Context context) {
		ClientCapHandler.koishiAttack(type, pos);
	}

}
