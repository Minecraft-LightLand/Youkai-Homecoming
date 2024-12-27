package dev.xkmc.youkaishomecoming.content.spell.custom.screen;

import dev.xkmc.youkaishomecoming.content.spell.custom.data.ISpellFormData;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.client.Minecraft;

public class ClientCustomSpellHandler {

	public static void open(ISpellFormData<?> data) {
		Minecraft.getInstance().setScreen(new EditorScreen(data));
	}

	public static void sendToPlayer(ISpellFormData<?> val) {
		YoukaisHomecoming.HANDLER.toServer(new SpellSetToServer(val));

	}

}
