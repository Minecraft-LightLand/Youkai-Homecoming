package dev.xkmc.youkaishomecoming.init.food;

import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.youkaishomecoming.init.registrate.CoffeeEffects;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import vectorwing.farmersdelight.common.registry.ModEffects;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public enum CoffeeDrinks {
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
	CoffeeDrinks(int coffee, int food, int cream, TagKey<Item>... tags) {
		FoodType type = coffee > 0 && food >= coffee ? FoodType.BOTTLE_FAST : FoodType.BOTTLE;
		int nutrition = coffee == 0 ? 0 : food;
		float sat = 0.6f;
		List<EffectEntry> list = new ArrayList<>();
		if (cream > 0) list.add(new EffectEntry(ModEffects.COMFORT, 3600, 0, 1));
		if (coffee > 0) {
			list.add(new EffectEntry(CoffeeEffects.CAFFEINATED, 1200 * coffee, 0, 1));
		}
		String name = name().toLowerCase(Locale.ROOT);
		item = type.build("coffee/", name, nutrition, sat, tags, list);
	}

	public static void register() {

	}

}
