package dev.xkmc.youkaishomecoming.compat.gateway;

import dev.xkmc.youkaishomecoming.events.YoukaiFightEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class GatewayEventHandlers {

	@SubscribeEvent
	public static void onFight(YoukaiFightEvent event) {
		if (event.target.getPersistentData().contains("gateways.owner")) {
			event.setCanceled(true);
		}
	}

}
