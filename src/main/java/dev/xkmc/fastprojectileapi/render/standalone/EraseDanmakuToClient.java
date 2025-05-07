package dev.xkmc.fastprojectileapi.render.standalone;

import dev.xkmc.fastprojectileapi.entity.SimplifiedProjectile;
import dev.xkmc.l2serial.network.SerialPacketBase;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraftforge.network.NetworkEvent;

@SerialClass
public class EraseDanmakuToClient extends SerialPacketBase {

	@SerialClass.SerialField
	private int id;

	public EraseDanmakuToClient() {
	}

	public EraseDanmakuToClient(SimplifiedProjectile e) {
		id = e.getId();
	}

	@Override
	public void handle(NetworkEvent.Context context) {
		DanmakuClientHandler.erase(id);
	}
}
