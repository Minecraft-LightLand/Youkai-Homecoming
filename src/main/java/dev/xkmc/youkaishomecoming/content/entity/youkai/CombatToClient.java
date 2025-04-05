package dev.xkmc.youkaishomecoming.content.entity.youkai;

import dev.xkmc.l2serial.network.SerialPacketBase;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.events.ClientEventHandlers;
import net.minecraftforge.network.NetworkEvent;

@SerialClass
public class CombatToClient extends SerialPacketBase {

	@SerialClass.SerialField
	public int id;
	@SerialClass.SerialField
	public CombatProgress progress;

	public CombatToClient() {

	}

	public CombatToClient(int id, CombatProgress progress) {
		this.id = id;
		this.progress = progress;
	}

	@Override
	public void handle(NetworkEvent.Context context) {
		ClientEventHandlers.setProgress(id, progress);
	}
}
