package dev.xkmc.youkaishomecoming.content.capability;

import dev.xkmc.l2serial.network.SerialPacketBase;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraftforge.network.NetworkEvent;

@SerialClass
public class KoishiStartPacket extends SerialPacketBase {

	@Override
	public void handle(NetworkEvent.Context context) {
		ClientCapHandler.koishiAttack();
	}

}
