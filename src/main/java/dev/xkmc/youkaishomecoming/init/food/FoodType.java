package dev.xkmc.youkaishomecoming.init.food;

import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.l2library.base.L2Registrate;
import dev.xkmc.youkaishomecoming.content.item.CoffeeFoodItem;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.registrate.CoffeeItems;
import net.minecraft.tags.TagKey;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public enum FoodType {
	SIMPLE(CoffeeFoodItem::new, UnaryOperator.identity(), false, false),
	FAST(CoffeeFoodItem::new, UnaryOperator.identity(), true, false),
	STICK(CoffeeFoodItem::new, p -> p.craftRemainder(Items.STICK).stacksTo(16), true, false),
	BOWL(CoffeeFoodItem::new, p -> p.craftRemainder(Items.BOWL).stacksTo(16), false, false),
	BOTTLE(CoffeeFoodItem::new, p -> p.craftRemainder(Items.GLASS_BOTTLE).stacksTo(16), false, true),
	BOTTLE_FAST(CoffeeFoodItem::new, p -> p.craftRemainder(Items.GLASS_BOTTLE).stacksTo(16), true, true),
	;

	private final Function<Item.Properties, Item> factory;
	private final UnaryOperator<Item.Properties> prop;
	private final boolean fast, alwaysEat;

	private final TagKey<Item>[] tags;

	@SafeVarargs
	FoodType(Function<Item.Properties, Item> factory, UnaryOperator<Item.Properties> prop, boolean fast, boolean alwaysEat, TagKey<Item>... tags) {
		this.factory = factory;
		this.prop = prop;
		this.fast = fast;
		this.alwaysEat = alwaysEat;
		this.tags = tags;
	}

	public ItemEntry<Item> build(String folder, String name, int nutrition, float sat, TagKey<Item>[] tags, List<EffectEntry> effs) {
		return build(factory, folder, name, nutrition, sat, tags, effs);
	}

	public ItemEntry<Item> build(Function<Item.Properties, Item> factory, String folder, String name, int nutrition, float sat, TagKey<Item>[] tags, List<EffectEntry> effs) {
		return build(factory, name, nutrition, sat, tags, effs)
				.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/" + folder + ctx.getName())))
				.register();
	}

	public ItemBuilder<Item, L2Registrate> build(String name, int nutrition, float sat, TagKey<Item>[] tags, List<EffectEntry> effs) {
		return build(factory, name, nutrition, sat, tags, effs);
	}

	public ItemBuilder<Item, L2Registrate> build(Function<Item.Properties, Item> factory, String name, int nutrition, float sat, TagKey<Item>[] tags, List<EffectEntry> effs) {
		return YoukaisHomecoming.REGISTRATE
				.item(name, p -> factory.apply(food(p, nutrition, sat, effs)))
				.tag(getTags(this.tags, tags))
				.lang(Item::getDescriptionId, makeLang(name));
	}


	public Item.Properties food(Item.Properties prop, float edibility, int nutrition, float sat, List<EffectEntry> effs) {
		return edibility <= 0 ? this.prop.apply(prop) :
				edibility < 1 ? food(prop, (int) (nutrition * edibility), sat * edibility, List.of()) :
						food(prop, nutrition, sat, effs);
	}

	public Item.Properties food(Item.Properties prop, int nutrition, float sat, List<EffectEntry> effs) {
		var food = new FoodProperties.Builder()
				.nutrition(nutrition).saturationMod(sat);
		if (fast) food.fast();
		if (alwaysEat) food.alwaysEat();
		for (var e : effs) {
			food.effect(e::getEffect, e.chance());
		}
		return this.prop.apply(prop).food(food.build());
	}

	public String makeLang(String id) {
		String name = CoffeeItems.toEnglishName(id.toLowerCase(Locale.ROOT));
		return CoffeeItems.toEnglishName(name);
	}

	@SuppressWarnings({"unsafe", "unchecked"})
	private static TagKey<Item>[] getTags(TagKey<Item>[] a, TagKey<Item>[] b) {
		var ans = new ArrayList<>(List.of(a));
		ans.addAll(List.of(b));
		return ans.toArray(TagKey[]::new);
	}

}
