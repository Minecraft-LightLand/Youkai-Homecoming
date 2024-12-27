package dev.xkmc.youkaishomecoming.content.spell.custom.screen;

import dev.xkmc.l2serial.network.SerialPacketBase;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.spell.custom.data.ISpellFormData;
import net.minecraftforge.network.NetworkEvent;

@SerialClass
public class SpellSetToServer extends SerialPacketBase {

	@SerialClass.SerialField
	public Record data;

	public SpellSetToServer() {

	}

	public SpellSetToServer(ISpellFormData<?> data) {
		this.data = data.cast();
	}

	@Override
	public void handle(NetworkEvent.Context context) {
		ServerCustomSpellHandler.handle(context.getSender(), data);
	}

}
