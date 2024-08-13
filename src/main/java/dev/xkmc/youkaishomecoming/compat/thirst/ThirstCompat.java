package dev.xkmc.youkaishomecoming.compat.thirst;

import dev.ghen.thirst.foundation.common.event.RegisterThirstValueEvent;
import dev.xkmc.fruitsdelight.init.FruitsDelight;
import dev.xkmc.youkaishomecoming.compat.food.FruitsDelightCompatFood;
import dev.xkmc.youkaishomecoming.init.food.YHCoffee;
import dev.xkmc.youkaishomecoming.init.food.YHFood;
import dev.xkmc.youkaishomecoming.init.food.YHSake;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;

public class ThirstCompat {

	public static void init() {
		MinecraftForge.EVENT_BUS.register(ThirstCompat.class);
	}

	@SubscribeEvent
	public static void compat(RegisterThirstValueEvent event) {
		for (var e : YHCoffee.values()) {
			event.addDrink(e.item.get(), 8, 13);
		}
		for (var e : YHSake.values()) {
			event.addDrink(e.item.get(), 8, 13);
		}

		event.addDrink(YHFood.BLACK_TEA.item.get(), 8, 13);
		event.addDrink(YHFood.GREEN_TEA.item.get(), 8, 13);
		event.addDrink(YHFood.OOLONG_TEA.item.get(), 8, 13);
		event.addDrink(YHFood.WHITE_TEA.item.get(), 8, 13);
		event.addDrink(YHFood.CORNFLOWER_TEA.item.get(), 8, 13);
		event.addDrink(YHFood.SAIDI_TEA.item.get(), 8, 13);
		event.addDrink(YHFood.SAKURA_HONEY_TEA.item.get(), 8, 13);
		event.addDrink(YHFood.GENMAI_TEA.item.get(), 8, 13);
		event.addDrink(YHFood.SCARLET_TEA.item.get(), 8, 13);
		event.addDrink(YHFood.GREEN_WATER.item.get(), 8, 13);
		event.addDrink(YHFood.TEA_MOCHA.item.get(), 6, 10);

		event.addDrink(YHFood.MILK_POPSICLE.item.get(), 6, 10);
		event.addDrink(YHFood.BIG_POPSICLE.item.get(), 6, 10);

		event.addDrink(YHFood.AVGOLEMONO.item.get(), 6, 10);
		event.addDrink(YHFood.SHIRAYUKI.item.get(), 6, 10);

		event.addDrink(YHFood.HIGAN_SOUP.item.get(), 8, 13);
		event.addDrink(YHFood.POOR_GOD_SOUP.item.get(), 8, 13);
		event.addDrink(YHFood.POWER_SOUP.item.get(), 8, 13);
		event.addDrink(YHFood.MUSHROOM_SOUP.item.get(), 8, 13);
		event.addDrink(YHFood.MISO_SOUP.item.get(), 8, 13);
		event.addDrink(YHFood.SEAFOOD_MISO_SOUP.item.get(), 8, 13);

		if (ModList.get().isLoaded(FruitsDelight.MODID)) {
			event.addDrink(FruitsDelightCompatFood.LEMON_BLACK_TEA.item.get(), 8, 13);
			event.addDrink(FruitsDelightCompatFood.MOON_ROCKET.item.get(), 8, 13);
			event.addDrink(FruitsDelightCompatFood.PEACH_TAPIOCA.item.get(), 8, 13);
		}


	}

}
