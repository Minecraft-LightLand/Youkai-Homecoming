package dev.xkmc.youkaishomecoming.content.item.food;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public class FleshHelper {

	@Nullable
	public static Player getPlayerOnClient() {
		return Minecraft.getInstance().player;
	}

}
