package dev.xkmc.youkaishomecoming.init.food;

import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.youkaishomecoming.content.item.food.YHDrinkItem;
import dev.xkmc.youkaishomecoming.content.item.food.YHFoodItem;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
import net.minecraft.tags.TagKey;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;

public class FoodType {
	public static final FoodType SIMPLE = new FoodType(YHFoodItem::new, false, false);
	public static final FoodType FAST = new FoodType(YHFoodItem::new, true, false);
	public static final FoodType MEAT = new FoodType(YHFoodItem::new, false, false);
	public static final FoodType MEAT_SLICE = new FoodType(YHFoodItem::new, true, false);
	public static final FoodType STICK = new FoodType(p -> new YHFoodItem(p.craftRemainder(Items.STICK).stacksTo(16)), true, false);
	public static final FoodType BOWL = new FoodType(p -> new YHFoodItem(p.craftRemainder(Items.BOWL).stacksTo(16)), false, false);
	public static final FoodType SAKE = new FoodType(p -> new YHDrinkItem(p.craftRemainder(Items.BOWL).stacksTo(16)), false, true);
	public static final FoodType BOTTLE = new FoodType(p -> new YHDrinkItem(p.craftRemainder(Items.GLASS_BOTTLE).stacksTo(16)), false, true);
	public static final FoodType BAMBOO = new FoodType(p -> new YHDrinkItem(p.craftRemainder(Items.BAMBOO).stacksTo(16)), false, true);
	public static final FoodType BOTTLE_FAST = new FoodType(p -> new YHDrinkItem(p.craftRemainder(Items.GLASS_BOTTLE).stacksTo(16)), true, true);
	public static final FoodType BOWL_MEAT = new FoodType(p -> new YHFoodItem(p.craftRemainder(Items.BOWL).stacksTo(16)), false, false);
	//public static final FoodType FLESH = new FoodType(FleshFoodItem::new, true, false, false, YHTagGen.FLESH_FOOD);
	//public static final FoodType FLESH_FAST = new FoodType(FleshFoodItem::new, true, true, false, YHTagGen.FLESH_FOOD);
	//public static final FoodType BOWL_FLESH = new FoodType(p -> new FleshFoodItem(p.craftRemainder(Items.BOWL).stacksTo(16)), false, false, YHTagGen.FLESH_FOOD);
	//public static final FoodType CAN_FLESH = new FoodType(p -> new FleshFoodItem(p.craftRemainder(YHItems.CAN.get()).stacksTo(64)), true, false, YHTagGen.FLESH_FOOD);

	private final Function<Item.Properties, Item> factory;
	private final boolean fast;
	private final boolean alwaysEat;

	private final TagKey<Item>[] tags;
	private final EffectEntry[] effs;

	@SafeVarargs
	FoodType(Function<Item.Properties, Item> factory, boolean fast, boolean alwaysEat, EffectEntry[] effs, TagKey<Item>... tags) {
		this.factory = factory;
		this.fast = fast;
		this.alwaysEat = alwaysEat;
		this.tags = tags;
		this.effs = effs;
	}

	@SafeVarargs
	FoodType(Function<Item.Properties, Item> factory, boolean fast, boolean alwaysEat, TagKey<Item>... tags) {
		this(factory, fast, alwaysEat, new EffectEntry[0], tags);
	}

	public ItemEntry<Item> build(String folder, String name, int nutrition, float sat, TagKey<Item>[] tags, List<EffectEntry> effs) {
		var food = new FoodProperties.Builder()
				.nutrition(nutrition).saturationModifier(sat);
		if (fast) food.fast();
		if (alwaysEat) food.alwaysEdible();
		for (var e : this.effs) {
			food.effect(e::getEffect, e.chance());
		}
		for (var e : effs) {
			food.effect(e::getEffect, e.chance());
		}
		return YoukaisHomecoming.REGISTRATE
				.item(name, p -> factory.apply(p.food(food.build())))
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
		return false;
	}

	@SuppressWarnings({"unsafe", "unchecked"})
	private static TagKey<Item>[] getTags(TagKey<Item>[] a, TagKey<Item>[] b) {
		var ans = new ArrayList<>(List.of(a));
		ans.addAll(List.of(b));
		return ans.toArray(TagKey[]::new);
	}
}
