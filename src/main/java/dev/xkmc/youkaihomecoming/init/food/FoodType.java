package dev.xkmc.youkaihomecoming.init.food;

import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.youkaihomecoming.content.item.FleshFoodItem;
import dev.xkmc.youkaihomecoming.content.item.YHFoodItem;
import dev.xkmc.youkaihomecoming.init.YoukaiHomecoming;
import dev.xkmc.youkaihomecoming.init.data.YHTagGen;
import dev.xkmc.youkaihomecoming.init.registrate.YHItems;
import net.minecraft.tags.TagKey;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;

public enum FoodType {
	SIMPLE(YHFoodItem::new, false, false),
	MEAT(YHFoodItem::new, true, false),
	MEAT_SLICE(YHFoodItem::new, true, true),
	STICK(p -> new YHFoodItem(p.craftRemainder(Items.STICK).stacksTo(16)), false, true),
	FLESH(FleshFoodItem::new, true, false, YHTagGen.FLESH_FOOD);

	private final Function<Item.Properties, Item> factory;
	private final boolean meat, fast;

	private final TagKey<Item>[] tags;
	private final EffectEntry[] effs;

	@SafeVarargs
	FoodType(Function<Item.Properties, Item> factory, boolean meat, boolean fast, EffectEntry[] effs, TagKey<Item>... tags) {
		this.factory = factory;
		this.meat = meat;
		this.fast = fast;
		this.tags = tags;
		this.effs = effs;
	}

	@SafeVarargs
	FoodType(Function<Item.Properties, Item> factory, boolean meat, boolean fast, TagKey<Item>... tags) {
		this(factory, meat, fast, new EffectEntry[0], tags);
	}


	public ItemEntry<Item> build(String name, int nutrition, float sat, TagKey<Item>[] tags, List<EffectEntry> effs) {
		var food = new FoodProperties.Builder()
				.nutrition(nutrition).saturationMod(sat);
		if (meat) food.meat();
		if (fast) food.fast();
		for (var e : this.effs) {
			food.effect(e::getEffect, e.chance());
		}
		for (var e : effs) {
			food.effect(e::getEffect, e.chance());
		}
		return YoukaiHomecoming.REGISTRATE
				.item(name, p -> factory.apply(p.food(food.build())))
				.tag(getTags(this.tags, tags))
				.lang(Item::getDescriptionId, makeLang(name))
				.register();
	}

	private String makeLang(String id) {
		String name = YHItems.toEnglishName(id.toLowerCase(Locale.ROOT));
		if (this == FLESH) {
			return name.replaceFirst("Flesh", "%1\\$s");
		} else {
			return name;
		}
	}

	@SuppressWarnings({"unsafe", "unchecked"})
	private static TagKey<Item>[] getTags(TagKey<Item>[] a, TagKey<Item>[] b) {
		var ans = new ArrayList<>(List.of(a));
		ans.addAll(List.of(b));
		return ans.toArray(TagKey[]::new);
	}
}
