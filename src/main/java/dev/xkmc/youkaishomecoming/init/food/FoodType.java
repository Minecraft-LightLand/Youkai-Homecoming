package dev.xkmc.youkaishomecoming.init.food;

import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.youkaishomecoming.content.item.food.FleshFoodItem;
import dev.xkmc.youkaishomecoming.content.item.food.YHDrinkItem;
import dev.xkmc.youkaishomecoming.content.item.food.YHFoodItem;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.data.YHTagGen;
import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
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
	SIMPLE(YHFoodItem::new, UnaryOperator.identity(), false, false, false),
	FAST(YHFoodItem::new, UnaryOperator.identity(), false, true, false),
	MEAT(YHFoodItem::new, UnaryOperator.identity(), true, false, false),
	MEAT_SLICE(YHFoodItem::new, UnaryOperator.identity(), true, true, false),
	STICK(YHFoodItem::new, p -> p.craftRemainder(Items.STICK).stacksTo(16), false, true, false),
	BOWL(YHFoodItem::new, p -> p.craftRemainder(Items.BOWL).stacksTo(16), false, false, false),
	SAKE(YHDrinkItem::new, p -> p.craftRemainder(Items.BOWL).stacksTo(16), false, false, true),
	BOTTLE(YHDrinkItem::new, p -> p.craftRemainder(Items.GLASS_BOTTLE).stacksTo(16), false, false, true),
	BAMBOO(YHDrinkItem::new, p -> p.craftRemainder(Items.BAMBOO).stacksTo(16), false, false, true),
	BOTTLE_FAST(YHDrinkItem::new, p -> p.craftRemainder(Items.GLASS_BOTTLE).stacksTo(16), false, true, true),
	BOWL_MEAT(YHFoodItem::new, p -> p.craftRemainder(Items.BOWL).stacksTo(16), true, false, false),
	FLESH(FleshFoodItem::new, UnaryOperator.identity(), true, false, false, YHTagGen.FLESH_FOOD),
	FLESH_FAST(FleshFoodItem::new, UnaryOperator.identity(), true, true, false, YHTagGen.FLESH_FOOD),
	BOWL_FLESH(FleshFoodItem::new, p -> p.craftRemainder(Items.BOWL).stacksTo(16), true, false, false, YHTagGen.FLESH_FOOD),
	CAN_FLESH(FleshFoodItem::new, p -> p.craftRemainder(YHItems.CAN.get()).stacksTo(64), true, true, false, YHTagGen.FLESH_FOOD),
	;

	private final Function<Item.Properties, Item> factory;
	private final UnaryOperator<Item.Properties> prop;
	private final boolean meat, fast, alwaysEat;

	private final TagKey<Item>[] tags;
	private final EffectEntry[] effs;

	@SafeVarargs
	FoodType(Function<Item.Properties, Item> factory, UnaryOperator<Item.Properties> prop, boolean meat, boolean fast, boolean alwaysEat, EffectEntry[] effs, TagKey<Item>... tags) {
		this.factory = factory;
		this.prop = prop;
		this.meat = meat;
		this.fast = fast;
		this.alwaysEat = alwaysEat;
		this.tags = tags;
		this.effs = effs;
	}

	@SafeVarargs
	FoodType(Function<Item.Properties, Item> factory, UnaryOperator<Item.Properties> prop, boolean meat, boolean fast, boolean alwaysEat, TagKey<Item>... tags) {
		this(factory, prop, meat, fast, alwaysEat, new EffectEntry[0], tags);
	}

	public ItemEntry<Item> build(String folder, String name, int nutrition, float sat, TagKey<Item>[] tags, List<EffectEntry> effs) {
		return build(factory, folder, name, nutrition, sat, tags, effs);
	}

	public ItemEntry<Item> build(Function<Item.Properties, Item> factory, String folder, String name, int nutrition, float sat, TagKey<Item>[] tags, List<EffectEntry> effs) {
		var food = new FoodProperties.Builder()
				.nutrition(nutrition).saturationMod(sat);
		if (meat) food.meat();
		if (fast) food.fast();
		if (alwaysEat) food.alwaysEat();
		for (var e : this.effs) {
			food.effect(e::getEffect, e.chance());
		}
		for (var e : effs) {
			food.effect(e::getEffect, e.chance());
		}
		return YoukaisHomecoming.REGISTRATE
				.item(name, p -> factory.apply(prop.apply(p).food(food.build())))
				.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/" + folder + ctx.getName())))
				.tag(getTags(this.tags, tags))
				.lang(Item::getDescriptionId, makeLang(name))
				.register();
	}

	public String makeLang(String id) {
		String name = YHItems.toEnglishName(id.toLowerCase(Locale.ROOT));
		if (isFlesh()) {
			name = name.replaceFirst("Flesh", "%1\\$s");
		}
		return YHItems.toEnglishName(name);
	}

	public boolean isFlesh() {
		return this == FLESH || this == BOWL_FLESH || this == FLESH_FAST || this == CAN_FLESH;
	}

	@SuppressWarnings({"unsafe", "unchecked"})
	private static TagKey<Item>[] getTags(TagKey<Item>[] a, TagKey<Item>[] b) {
		var ans = new ArrayList<>(List.of(a));
		ans.addAll(List.of(b));
		return ans.toArray(TagKey[]::new);
	}
}
