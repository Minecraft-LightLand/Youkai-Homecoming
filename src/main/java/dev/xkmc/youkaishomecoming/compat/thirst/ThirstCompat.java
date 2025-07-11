package dev.xkmc.youkaishomecoming.compat.thirst;

import dev.ghen.thirst.foundation.common.event.RegisterThirstValueEvent;
import dev.xkmc.youkaishomecoming.init.food.CoffeeDrinks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ThirstCompat {

	public static void init() {
		MinecraftForge.EVENT_BUS.register(ThirstCompat.class);
	}

	@SubscribeEvent
	public static void compat(RegisterThirstValueEvent event) {
		for (var e : CoffeeDrinks.values()) {
			event.addDrink(e.item.get(), 8, 13);
		}
	}

}
