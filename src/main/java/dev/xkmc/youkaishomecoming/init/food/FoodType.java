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
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.function.Supplier;

public class FoodType {
	public static final FoodType SIMPLE = new FoodType(YHFoodItem::new, false, false, null);
	public static final FoodType FAST = new FoodType(YHFoodItem::new, true, false, null);
	public static final FoodType MEAT = new FoodType(YHFoodItem::new, false, false, null);
	public static final FoodType MEAT_SLICE = new FoodType(YHFoodItem::new, true, false, null);
	public static final FoodType STICK = new FoodType(YHFoodItem::new, true, false, () -> Items.STICK);
	public static final FoodType BOWL = new FoodType(YHFoodItem::new, false, false, () -> Items.BOWL);
	public static final FoodType SAKE = new FoodType(YHDrinkItem::new, false, true, () -> Items.BOWL);
	public static final FoodType BOTTLE = new FoodType(YHDrinkItem::new, false, true, () -> Items.GLASS_BOTTLE);
	public static final FoodType BAMBOO = new FoodType(YHDrinkItem::new, false, true, () -> Items.BAMBOO);
	public static final FoodType BOTTLE_FAST = new FoodType(YHDrinkItem::new, true, true, () -> Items.GLASS_BOTTLE);
	public static final FoodType BOWL_MEAT = new FoodType(YHFoodItem::new, false, false, () -> Items.BOWL);
	//public static final FoodType FLESH = new FoodType(FleshFoodItem::new, true, false, false, YHTagGen.FLESH_FOOD);
	//public static final FoodType FLESH_FAST = new FoodType(FleshFoodItem::new, true, true, false, YHTagGen.FLESH_FOOD);
	//public static final FoodType BOWL_FLESH = new FoodType(p -> new FleshFoodItem(p.craftRemainder(Items.BOWL).stacksTo(16)), false, false, YHTagGen.FLESH_FOOD);
	//public static final FoodType CAN_FLESH = new FoodType(p -> new FleshFoodItem(p.craftRemainder(YHItems.CAN.get()).stacksTo(64)), true, false, YHTagGen.FLESH_FOOD);

	private final Function<Item.Properties, Item> factory;
	private final boolean fast;
	private final boolean alwaysEat;
	private final Supplier<Item> container;
	private final TagKey<Item>[] tags;
	private final EffectEntry[] effs;


	@SafeVarargs
	FoodType(Function<Item.Properties, Item> factory, boolean fast, boolean alwaysEat, @Nullable Supplier<Item> container, EffectEntry[] effs, TagKey<Item>... tags) {
		this.factory = factory;
		this.fast = fast;
		this.alwaysEat = alwaysEat;
		this.container = container;
		this.tags = tags;
		this.effs = effs;
	}

	@SafeVarargs
	FoodType(Function<Item.Properties, Item> factory, boolean fast, boolean alwaysEat, @Nullable Supplier<Item> container, TagKey<Item>... tags) {
		this(factory, fast, alwaysEat, container, new EffectEntry[0], tags);
	}

	private Item.Properties food(Item.Properties prop, int nutrition, float sat, List<EffectEntry> effs) {
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
		if (container != null) {
			food.usingConvertsTo(container.get());
			prop.craftRemainder(container.get());
			prop.stacksTo(16);
		}
		prop.food(food.build());
		return prop;
	}

	public ItemEntry<Item> build(String folder, String name, int nutrition, float sat, TagKey<Item>[] tags, List<EffectEntry> effs) {
		return build(factory, folder, name, nutrition, sat, tags, effs);
	}

	public ItemEntry<Item> build(Function<Item.Properties, Item> factory, String folder, String name, int nutrition, float sat, TagKey<Item>[] tags, List<EffectEntry> effs) {
		boolean opt = folder.startsWith("?");
		var path = opt ? folder.substring(1) : folder;
		return YoukaisHomecoming.REGISTRATE
				.item(name, p -> factory.apply(food(p, nutrition, sat, effs)))
				.transform(e -> opt ? e.asOptional() : e)
				.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/" + path + ctx.getName())))
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
