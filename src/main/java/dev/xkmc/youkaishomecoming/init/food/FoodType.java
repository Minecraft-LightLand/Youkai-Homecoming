package dev.xkmc.youkaishomecoming.init.food;

import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.l2library.base.L2Registrate;
import dev.xkmc.youkaishomecoming.content.block.food.BowlBlock;
import dev.xkmc.youkaishomecoming.content.item.food.YHDrinkItem;
import dev.xkmc.youkaishomecoming.content.item.food.YHFoodItem;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.data.YHTagGen;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
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
	SIMPLE(YHFoodItem::new, UnaryOperator.identity(), false, false),
	FAST(YHFoodItem::new, UnaryOperator.identity(), true, false),
	STICK(YHFoodItem::new, p -> p.craftRemainder(Items.STICK).stacksTo(16), true, false),
	BOWL(YHFoodItem::new, p -> p.craftRemainder(Items.BOWL).stacksTo(16), false, false),
	SAKE(YHDrinkItem::new, p -> p.craftRemainder(Items.BOWL).stacksTo(16), false, true),
	BOTTLE(YHDrinkItem::new, p -> p.craftRemainder(Items.GLASS_BOTTLE).stacksTo(16), false, true),
	BAMBOO(YHDrinkItem::new, p -> p.craftRemainder(Items.BAMBOO).stacksTo(16), false, true),
	BOTTLE_FAST(YHDrinkItem::new, p -> p.craftRemainder(Items.GLASS_BOTTLE).stacksTo(16), true, true),
	IRON_BOWL(YHFoodItem::new, p -> p.craftRemainder(YHBlocks.IRON_BOWL.asItem()).stacksTo(16), false, false),
	BAMBOO_BOWL(YHFoodItem::new, p -> p.craftRemainder(Items.BAMBOO).stacksTo(16), false, false),
	SAUCER(YHFoodItem::new, p -> p.craftRemainder(YHItems.SAUCER.asItem()).stacksTo(16), false, false),
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
