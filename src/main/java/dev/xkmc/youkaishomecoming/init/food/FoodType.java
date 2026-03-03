package dev.xkmc.youkaishomecoming.init.food;

import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import dev.xkmc.youkaishomecoming.content.block.food.BowlBlock;
import dev.xkmc.youkaishomecoming.content.item.food.YHDrinkItem;
import dev.xkmc.youkaishomecoming.content.item.food.YHFoodItem;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
import net.minecraft.tags.TagKey;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;

public enum FoodType {
	SIMPLE(YHFoodItem::new, false, false),
	FAST(YHFoodItem::new, true, false),
	STICK(YHFoodItem::new, () -> Items.STICK, 16, true, false),
	BOWL(YHFoodItem::new, () -> Items.BOWL, 16, false, false),
	SAKE(YHDrinkItem::new, () -> Items.BOWL, 16, false, true),
	BOTTLE(YHDrinkItem::new, Items.GLASS_BOTTLE, 16, false, true),
	BAMBOO(YHDrinkItem::new, () -> Items.BAMBOO, 16, false, true),
	BOTTLE_FAST(YHDrinkItem::new, Items.GLASS_BOTTLE, 16, true, true),
	IRON_BOWL(YHFoodItem::new, YHBlocks.IRON_BOWL, 16, false, false),
	BAMBOO_BOWL(YHFoodItem::new, () -> Items.BAMBOO, 16, false, false),
	SAUCER(YHFoodItem::new, YHItems.SAUCER, 16, false, false),
	;

	private final Function<Item.Properties, Item> factory;
	private final ItemLike container;
	private final int count;
	private final boolean fast, alwaysEat;

	private final TagKey<Item>[] tags;

	@SafeVarargs
	FoodType(Function<Item.Properties, Item> factory, boolean fast, boolean alwaysEat, TagKey<Item>... tags) {
		this(factory, Items.AIR, 64, fast, alwaysEat, tags);
	}

	@SafeVarargs
	FoodType(Function<Item.Properties, Item> factory, ItemLike container, int stack, boolean fast, boolean alwaysEat, TagKey<Item>... tags) {
		this.factory = factory;
		this.container = container;
		this.count = stack;
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
		if (container.asItem() != Items.AIR) prop.craftRemainder(container.asItem());
		if (count < 64) prop.stacksTo(count);
		return edibility <= 0 ? prop :
				edibility < 1 ? food(prop, (int) (nutrition * edibility), sat * edibility, List.of()) :
						food(prop, nutrition, sat, effs);
	}

	public Item.Properties food(Item.Properties prop, int nutrition, float sat, List<EffectEntry> effs) {
		var food = new FoodProperties.Builder()
				.nutrition(nutrition).saturationModifier(sat);
		if (fast) food.fast();
		if (alwaysEat) food.alwaysEdible();
		for (var e : effs) {
			food.effect(e::getEffect, e.chance());
		}
		if (container.asItem() != Items.AIR) {
			prop.craftRemainder(container.asItem());
			food.usingConvertsTo(container);
		}
		if (count < 64) prop.stacksTo(count);

		return prop.food(food.build());
	}

	public BlockBuilder<BowlBlock, L2Registrate> bowl(String name, boolean raw) {
		if (this == IRON_BOWL)
			return BowlBlock.ironBowlFood(name);
		if (this == BAMBOO_BOWL)
			return raw ? BowlBlock.rawBambooBowl(name) : BowlBlock.bambooBowl(name);
		return BowlBlock.woodBowlFood(name);
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
