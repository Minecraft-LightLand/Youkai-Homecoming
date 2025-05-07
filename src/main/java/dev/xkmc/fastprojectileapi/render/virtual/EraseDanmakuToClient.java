package dev.xkmc.fastprojectileapi.render.virtual;

import dev.xkmc.fastprojectileapi.entity.SimplifiedProjectile;
import dev.xkmc.l2serial.network.SerialPacketBase;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraftforge.network.NetworkEvent;

@SerialClass
public class EraseDanmakuToClient extends SerialPacketBase {

	@SerialClass.SerialField
	private int id;
	@SerialClass.SerialField
	private boolean kill;

	public EraseDanmakuToClient() {
	}

	public EraseDanmakuToClient(SimplifiedProjectile e, boolean kill) {
		id = e.getId();
		this.kill = kill;
	}

	@Override
	public void handle(NetworkEvent.Context context) {
		DanmakuClientHandler.erase(id, kill);
	}
}
