package dev.xkmc.youkaishomecoming.content.item.food;

import dev.xkmc.l2library.util.Proxy;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.DistExecutor;

public class FleshHelper {

	public static Player getPlayer() {
		return DistExecutor.unsafeRunForDist(() -> () -> (Player) Proxy.getClientPlayer(), () -> () -> null);
	}

}
