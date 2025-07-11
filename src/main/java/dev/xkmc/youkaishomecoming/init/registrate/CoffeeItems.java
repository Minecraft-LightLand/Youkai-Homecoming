package dev.xkmc.youkaishomecoming.init.registrate;

import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.food.CoffeeDrinks;
import dev.xkmc.youkaishomecoming.init.food.CoffeeCrops;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class CoffeeItems {

	private static final Set<String> SMALL_WORDS = Set.of("of", "the", "with", "in");

	public static String toEnglishName(String internalName) {
		return Arrays.stream(internalName.split("_"))
				.map(e -> SMALL_WORDS.contains(e) ? e : StringUtils.capitalize(e))
				.collect(Collectors.joining(" "));
	}

	public static final BlockEntry<Block> COFFEE_BEAN_BAG;

	public static final ItemEntry<Item> COFFEE_BEAN, COFFEE_POWDER, CREAM, ICE_CUBE;

	static {
		CoffeeCrops.register();
		COFFEE_BEAN = crop("coffee_beans", Item::new);
		COFFEE_POWDER = crop("coffee_powder", Item::new);
		COFFEE_BEAN_BAG = CoffeeCrops.createBag("coffee_bean");

		CREAM = YoukaisHomecoming.REGISTRATE
				.item("bowl_of_cream", p -> new Item(p.craftRemainder(Items.BOWL)))
				.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/ingredient/" + ctx.getName())))
				.lang("Bowl of Cream")
				.register();
		ICE_CUBE = ingredient("ice_cube", Item::new);

		CoffeeDrinks.register();
	}

	public static <T extends Item> ItemBuilder<T, ?> seed(String id, NonNullFunction<Item.Properties, T> factory) {
		return YoukaisHomecoming.REGISTRATE.item(id, factory)
				.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/crops/" + ctx.getName())));
	}

	public static <T extends Item> ItemEntry<T> crop(String id, NonNullFunction<Item.Properties, T> factory) {
		return YoukaisHomecoming.REGISTRATE.item(id, factory)
				.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/crops/" + ctx.getName())))
				.register();
	}

	public static <T extends Item> ItemEntry<T> ingredient(String id, NonNullFunction<Item.Properties, T> factory) {
		return YoukaisHomecoming.REGISTRATE.item(id, factory)
				.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/ingredient/" + ctx.getName())))
				.register();
	}

	public static void register() {
	}

}
