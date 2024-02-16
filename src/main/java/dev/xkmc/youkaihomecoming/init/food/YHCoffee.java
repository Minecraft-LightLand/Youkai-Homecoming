package dev.xkmc.youkaihomecoming.init.food;

import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.youkaihomecoming.init.registrate.YHEffects;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import vectorwing.farmersdelight.common.registry.ModEffects;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public enum YHCoffee {
	CUP_OF_WATER(0, 0, 0),
	CUP_OF_MILK(0, 0, 0),
	CUP_OF_CREAM(0, 0, 0),

	ESPRESSO(2, 0, 0),
	AMERICANO(1, 0, 0),
	RISTRETTO(3, 0, 0),

	LATTE(1, 4, 0),
	AFFOGATO(1, 2, 1),
	CON_PANNA(2, 2, 1),
	CAPPUCCINO(1, 5, 1),
	MACCHIATO(1, 5, 1),
	MOCHA(1, 6, 1),
	;

	public final ItemEntry<Item> item;

	@SafeVarargs
	YHCoffee(int coffee, int food, int cream, TagKey<Item>... tags) {
		FoodType type = coffee > 0 && food >= coffee ? FoodType.BOTTLE_FAST : FoodType.BOTTLE;
		int nutrition = coffee == 0 ? 0 : food;
		float sat = 0.6f;
		List<EffectEntry> list = new ArrayList<>();
		if (cream > 0) list.add(new EffectEntry(ModEffects.COMFORT, 3600, 0, 1));
		if (coffee > 0) {
			list.add(new EffectEntry(YHEffects.CAFFEINATED::get, 1200, coffee - 1, 1));
		}
		String name = name().toLowerCase(Locale.ROOT);
		item = type.build("coffee/", name, nutrition, sat, tags, list);
	}

	public static void register() {

	}

}
