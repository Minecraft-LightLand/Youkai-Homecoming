package dev.xkmc.youkaishomecoming.init.registrate;

import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import dev.xkmc.fruitsdelight.init.FruitsDelight;
import dev.xkmc.youkaishomecoming.compat.food.FruitsDelightCompatFood;
import dev.xkmc.youkaishomecoming.content.block.food.EmptySaucerBlock;
import dev.xkmc.youkaishomecoming.content.block.food.SurpriseChestBlock;
import dev.xkmc.youkaishomecoming.content.block.food.SurpriseFeastBlock;
import dev.xkmc.youkaishomecoming.content.item.fluid.BottledFluid;
import dev.xkmc.youkaishomecoming.content.item.fluid.SakeBottleItem;
import dev.xkmc.youkaishomecoming.content.item.fluid.SlipBottleItem;
import dev.xkmc.youkaishomecoming.content.pot.table.food.YHRolls;
import dev.xkmc.youkaishomecoming.content.pot.table.food.YHSushi;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.food.*;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.fml.ModList;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class YHItems {

	private static final Set<String> SMALL_WORDS = Set.of("of", "the", "with", "in");

	public static String toEnglishName(String internalName) {
		return Arrays.stream(internalName.split("_"))
				.map(e -> SMALL_WORDS.contains(e) ? e : StringUtils.capitalize(e))
				.collect(Collectors.joining(" "));
	}

	public static final BlockEntry<Block> SOYBEAN_BAG, PODS_CRATE,
			REDBEAN_BAG, COFFEE_BEAN_BAG, CUCUMBER_BAG,
			TEA_BAG, BLACK_TEA_BAG, GREEN_TEA_BAG, OOLONG_TEA_BAG, WHITE_TEA_BAG,
			RED_GRAPE_CRATE, BLACK_GRAPE_CRATE, WHITE_GRAPE_CRATE;

	public static final BottledFluid<SakeBottleItem> SOY_SAUCE_BOTTLE, MAYONNAISE;
	public static final ItemEntry<Item> CLAY_SAUCER,
			COFFEE_BEAN, COFFEE_POWDER, CREAM, MATCHA,
			STRIPPED_MANDRAKE_ROOT, DRIED_MANDRAKE_FLOWER, CAN, ICE_CUBE;
	public static final ItemEntry<SlipBottleItem> SAKE_BOTTLE;
	public static final BlockEntry<SurpriseChestBlock> SURP_CHEST;
	public static final BlockEntry<SurpriseFeastBlock> SURP_FEAST;
	public static final CakeEntry  TARTE_LUNE;
	public static final BlockEntry<EmptySaucerBlock> SAUCER;
	public static final ItemEntry<MobBucketItem> LAMPREY_BUCKET;
	public static final ItemEntry<MobBucketItem> TUNA_BUCKET;

	static {

		// plants
		{
			YHCrops.register();
			COFFEE_BEAN = crop("coffee_beans", Item::new);
			COFFEE_POWDER = crop("coffee_powder", Item::new);
			YHTea.register();
			MATCHA = crop("matcha", Item::new);
			STRIPPED_MANDRAKE_ROOT = crop("stripped_mandrake_root", Item::new);
			DRIED_MANDRAKE_FLOWER = crop("dried_mandrake_flower", Item::new);
			CUCUMBER_BAG = YHCrops.CUCUMBER.createCrate();
			RED_GRAPE_CRATE = YHCrops.RED_GRAPE.createCrate();
			BLACK_GRAPE_CRATE = YHCrops.BLACK_GRAPE.createCrate();
			WHITE_GRAPE_CRATE = YHCrops.WHITE_GRAPE.createCrate();
			PODS_CRATE = YHCrops.createCrate("pod");
			SOYBEAN_BAG = YHCrops.SOYBEAN.createBag();
			REDBEAN_BAG = YHCrops.REDBEAN.createBag();
			COFFEE_BEAN_BAG = YHCrops.createBag("coffee_bean");
			TEA_BAG = YHCrops.createBag("tea_leaf");
			BLACK_TEA_BAG = YHTea.BLACK.createBags();
			GREEN_TEA_BAG = YHTea.GREEN.createBags();
			OOLONG_TEA_BAG = YHTea.OOLONG.createBags();
			WHITE_TEA_BAG = YHTea.WHITE.createBags();
		}

		// ingredients
		{
			SOY_SAUCE_BOTTLE = new BottledFluid<>("soy_sauce", 0xff3B302C, () -> Items.GLASS_BOTTLE, "ingredient", SakeBottleItem::new);
			MAYONNAISE = new BottledFluid<>("mayonnaise", 0xffffffff, () -> Items.GLASS_BOTTLE, "ingredient", SakeBottleItem::new);

			CREAM = YoukaisHomecoming.REGISTRATE
					.item("bowl_of_cream", p -> new Item(p.craftRemainder(Items.BOWL)))
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/ingredient/" + ctx.getName())))
					.lang("Bowl of Cream")
					.register();

			ICE_CUBE = ingredient("ice_cube", Item::new);
		}

		CAN = YoukaisHomecoming.REGISTRATE.item("can", Item::new).register();

		YHFood.register();
		YHSushi.register();
		YHRolls.init();

		// feasts
		{
			SURP_CHEST = YoukaisHomecoming.REGISTRATE.block("chest_of_heart_throbbing_surprise", p ->
							new SurpriseChestBlock(BlockBehaviour.Properties.copy(Blocks.BROWN_WOOL)))
					.item().model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/feast/" + ctx.getName()))).build()
					.blockstate(SurpriseChestBlock::buildModel)
					.register();

			SURP_FEAST = YoukaisHomecoming.REGISTRATE.block("heart_throbbing_surprise", p ->
							new SurpriseFeastBlock(BlockBehaviour.Properties.copy(Blocks.BROWN_WOOL),
									YHFood.BOWL_OF_HEART_THROBBING_SURPRISE.item))
					.blockstate(SurpriseFeastBlock::buildModel)
					.loot(SurpriseFeastBlock::builtLoot)
					.register();

			TARTE_LUNE = new CakeEntry("tarte_lune", MapColor.COLOR_PURPLE, FoodType.SIMPLE, 4, 0.6f, false);
		}

		CLAY_SAUCER = YoukaisHomecoming.REGISTRATE.item("clay_saucer", Item::new).register();

		SAUCER = YoukaisHomecoming.REGISTRATE.block("saucer", p -> new EmptySaucerBlock(
						BlockBehaviour.Properties.copy(Blocks.LIGHT_GRAY_WOOL)))
				.blockstate((ctx, pvd) -> pvd.horizontalBlock(ctx.get(),
						state -> state.getValue(EmptySaucerBlock.TYPE).build(pvd)))
				.item().model((ctx, pvd) -> pvd.generated(ctx)).build()
				.register();

		YHDish.register();
		YHCoffee.register();
		YHDrink.register();

		SAKE_BOTTLE = YoukaisHomecoming.REGISTRATE.item("sake_bottle", SlipBottleItem::new)
				.properties(p -> p.stacksTo(1))
				.model((ctx, pvd) ->
						pvd.generated(ctx, pvd.modLoc("item/sake_bottle"))
								.override().predicate(YoukaisHomecoming.loc("slip"), 1 / 32f)
								.model(pvd.getBuilder(ctx.getName() + "_overlay")
										.parent(new ModelFile.UncheckedModelFile("item/generated"))
										.texture("layer0", pvd.modLoc("item/sake_bottle"))
										.texture("layer1", pvd.modLoc("item/sake_bottle_overlay"))))
				.color(() -> () -> SlipBottleItem::color)
				.register();

		if (ModList.get().isLoaded(FruitsDelight.MODID)) {
			FruitsDelightCompatFood.register();
		}

		LAMPREY_BUCKET = YoukaisHomecoming.REGISTRATE
				.item("lamprey_bucket", p -> new MobBucketItem(
						YHEntities.LAMPREY, () -> Fluids.WATER, () -> SoundEvents.BUCKET_EMPTY_FISH,
						p.stacksTo(1).craftRemainder(Items.BUCKET)))
				.defaultLang()
				.register();

		TUNA_BUCKET = YoukaisHomecoming.REGISTRATE
				.item("tuna_bucket", p -> new MobBucketItem(
						YHEntities.TUNA, () -> Fluids.WATER, () -> SoundEvents.BUCKET_EMPTY_FISH,
						p.stacksTo(1).craftRemainder(Items.BUCKET)))
				.defaultLang()
				.register();

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
