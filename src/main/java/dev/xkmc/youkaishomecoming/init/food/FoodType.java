package dev.xkmc.youkaishomecoming.init.food;

import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import dev.xkmc.youkaishomecoming.content.item.food.YHDrinkItem;
import dev.xkmc.youkaishomecoming.content.item.food.YHFoodItem;
import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
import net.minecraft.tags.TagKey;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;

public class FoodType {
	public static final FoodType SIMPLE = new FoodType(YHFoodItem::new, false, false, null);
	public static final FoodType FAST = new FoodType(YHFoodItem::new, true, false, null);
	public static final FoodType MEAT = new FoodType(YHFoodItem::new, false, false, null);
	public static final FoodType MEAT_SLICE = new FoodType(YHFoodItem::new, true, false, null);
	public static final FoodType STICK = new FoodType(YHFoodItem::new, true, false, Items.STICK);
	public static final FoodType BOWL = new FoodType(YHFoodItem::new, false, false, Items.BOWL);
	public static final FoodType SAKE = new FoodType(YHDrinkItem::new, false, true, Items.BOWL);
	public static final FoodType BOTTLE = new FoodType(YHDrinkItem::new, false, true, Items.GLASS_BOTTLE);
	public static final FoodType BAMBOO = new FoodType(YHDrinkItem::new, false, true, Items.BAMBOO);
	public static final FoodType BOTTLE_FAST = new FoodType(YHDrinkItem::new, true, true, Items.GLASS_BOTTLE);
	public static final FoodType BOWL_MEAT = new FoodType(YHFoodItem::new, false, false, Items.BOWL);

	private final Function<Item.Properties, Item> factory;
	private final boolean fast;
	private final boolean alwaysEat;
	private final ItemLike container;
	private final TagKey<Item>[] tags;
	private final EffectEntry[] effs;


	@SafeVarargs
	public FoodType(Function<Item.Properties, Item> factory, boolean fast, boolean alwaysEat, @Nullable ItemLike container, EffectEntry[] effs, TagKey<Item>... tags) {
		this.factory = factory;
		this.fast = fast;
		this.alwaysEat = alwaysEat;
		this.container = container;
		this.tags = tags;
		this.effs = effs;
	}

	@SafeVarargs
	public FoodType(Function<Item.Properties, Item> factory, boolean fast, boolean alwaysEat, @Nullable ItemLike container, TagKey<Item>... tags) {
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
			food.usingConvertsTo(container);
			prop.craftRemainder(container.asItem());
			prop.stacksTo(16);
		}
		prop.food(food.build());
		return prop;
	}

	public ItemEntry<Item> build(L2Registrate reg, String folder, String name, int nutrition, float sat, TagKey<Item>[] tags, List<EffectEntry> effs) {
		return build(reg, factory, folder, name, nutrition, sat, tags, effs);
	}

	public ItemEntry<Item> build(L2Registrate reg, Function<Item.Properties, Item> factory, String folder, String name, int nutrition, float sat, TagKey<Item>[] tags, List<EffectEntry> effs) {
		boolean opt = folder.startsWith("?");
		var path = opt ? folder.substring(1) : folder;
		return reg.item(name, p -> factory.apply(food(p, nutrition, sat, effs)))
				.transform(e -> opt ? e.asOptional() : e)
				.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/" + path + ctx.getName())))
				.tag(getTags(this.tags, tags))
				.lang(Item::getDescriptionId, makeLang(name))
				.register();
	}

	public String makeLang(String id) {
		String name = YHItems.toEnglishName(id.toLowerCase(Locale.ROOT));
		return YHItems.toEnglishName(name);
	}

	@SuppressWarnings({"unsafe", "unchecked"})
	private static TagKey<Item>[] getTags(TagKey<Item>[] a, TagKey<Item>[] b) {
		var ans = new ArrayList<>(List.of(a));
		ans.addAll(List.of(b));
		return ans.toArray(TagKey[]::new);
	}
}
