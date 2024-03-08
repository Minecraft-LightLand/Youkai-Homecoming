package dev.xkmc.youkaishomecoming.init.registrate;

import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import dev.xkmc.youkaishomecoming.content.block.food.EmptySaucerBlock;
import dev.xkmc.youkaishomecoming.content.block.food.FleshFeastBlock;
import dev.xkmc.youkaishomecoming.content.block.food.SurpriseChestBlock;
import dev.xkmc.youkaishomecoming.content.block.food.SurpriseFeastBlock;
import dev.xkmc.youkaishomecoming.content.item.curio.KoishiHatItem;
import dev.xkmc.youkaishomecoming.content.item.curio.SuwakoHatItem;
import dev.xkmc.youkaishomecoming.content.item.food.BloodBottleItem;
import dev.xkmc.youkaishomecoming.content.item.food.FleshBlockItem;
import dev.xkmc.youkaishomecoming.content.item.food.FleshSimpleItem;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.food.*;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.common.Tags;
import org.apache.commons.lang3.StringUtils;
import vectorwing.farmersdelight.common.block.FeastBlock;

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

	public static final ItemEntry<SuwakoHatItem> SUWAKO_HAT;
	public static final ItemEntry<KoishiHatItem> KOISHI_HAT;
	public static final BlockEntry<Block> SOYBEAN_BAG, REDBEAN_BAG, COFFEE_BEAN_BAG,
			BLACK_TEA_BAG, GREEN_TEA_BAG, OOLONG_TEA_BAG, WHITE_TEA_BAG;
	public static final ItemEntry<BloodBottleItem> BLOOD_BOTTLE;
	public static final ItemEntry<Item> SOY_SAUCE_BOTTLE, CLAY_SAUCER,
			COFFEE_BEAN, COFFEE_POWDER, CREAM, MATCHA;
	public static final BlockEntry<SurpriseChestBlock> SURP_CHEST;
	public static final BlockEntry<SurpriseFeastBlock> SURP_FEAST;
	public static final ItemEntry<FleshSimpleItem> RAW_FLESH_FEAST;
	public static final BlockEntry<FleshFeastBlock> FLESH_FEAST;
	public static final CakeEntry RED_VELVET, TARTE_LUNE;
	public static final BlockEntry<EmptySaucerBlock> SAUCER;
	public static final ItemEntry<MobBucketItem> LAMPREY_BUCKET;


	static {
		SUWAKO_HAT = YoukaisHomecoming.REGISTRATE
				.item("suwako_hat", p -> new SuwakoHatItem(p.rarity(Rarity.EPIC)))
				.tag(Tags.Items.ARMORS_HELMETS)
				.register();

		KOISHI_HAT = YoukaisHomecoming.REGISTRATE
				.item("koishi_hat", p -> new KoishiHatItem(p.rarity(Rarity.EPIC)))
				.tag(Tags.Items.ARMORS_HELMETS)
				.register();

		YHCrops.register();
		COFFEE_BEAN = crop("coffee_beans", Item::new);
		COFFEE_POWDER = crop("coffee_powder", Item::new);
		YHTea.register();
		MATCHA = crop("matcha", Item::new);
		SOYBEAN_BAG = YHCrops.SOYBEAN.createBag();
		REDBEAN_BAG = YHCrops.REDBEAN.createBag();
		COFFEE_BEAN_BAG = YHCrops.createBag("coffee_bean");
		BLACK_TEA_BAG = YHTea.BLACK.createBags();
		GREEN_TEA_BAG = YHTea.GREEN.createBags();
		OOLONG_TEA_BAG = YHTea.OOLONG.createBags();
		WHITE_TEA_BAG = YHTea.WHITE.createBags();

		SOY_SAUCE_BOTTLE = YoukaisHomecoming.REGISTRATE
				.item("soy_sauce_bottle", p -> new Item(p.craftRemainder(Items.GLASS_BOTTLE)))
				.register();

		BLOOD_BOTTLE = YoukaisHomecoming.REGISTRATE
				.item("blood_bottle", p -> new BloodBottleItem(p.craftRemainder(Items.GLASS_BOTTLE)))
				.register();

		CREAM = YoukaisHomecoming.REGISTRATE
				.item("bowl_of_cream", p -> new Item(p.craftRemainder(Items.BOWL)))
				.lang("Bowl of Cream")
				.register();

		YHFood.register();

		SURP_CHEST = YoukaisHomecoming.REGISTRATE.block("chest_of_heart_throbbing_surprise", p ->
						new SurpriseChestBlock(BlockBehaviour.Properties.copy(Blocks.BROWN_WOOL)))
				.item().model((ctx, pvd) -> pvd.generated(ctx)).build()
				.blockstate(SurpriseChestBlock::buildModel)
				.register();

		SURP_FEAST = YoukaisHomecoming.REGISTRATE.block("heart_throbbing_surprise", p ->
						new SurpriseFeastBlock(BlockBehaviour.Properties.copy(Blocks.BROWN_WOOL),
								YHFood.BOWL_OF_HEART_THROBBING_SURPRISE.item))
				.blockstate(SurpriseFeastBlock::buildModel)
				.loot(SurpriseFeastBlock::builtLoot)
				.register();

		RAW_FLESH_FEAST = YoukaisHomecoming.REGISTRATE.item("raw_flesh_feast", FleshSimpleItem::new)
				.lang("Raw %1$s Feast")
				.register();

		FLESH_FEAST = YoukaisHomecoming.REGISTRATE.block("flesh_feast", p ->
						new FleshFeastBlock(BlockBehaviour.Properties.copy(Blocks.BROWN_WOOL),
								YHFood.BOWL_OF_FLESH_FEAST.item))
				.blockstate((ctx, pvd) -> pvd.horizontalBlock(ctx.get(), state ->
						FleshFeastBlock.Model.values()[state.getValue(FeastBlock.SERVINGS)].build(pvd)))
				.lang("%1$s Feast")
				.item(FleshBlockItem::new).model((ctx, pvd) -> pvd.generated(ctx)).build()
				.loot(FleshFeastBlock::builtLoot)
				.register();

		RED_VELVET = new CakeEntry("red_velvet", MapColor.COLOR_RED, FoodType.FLESH, 1, 0.8f, true);
		TARTE_LUNE = new CakeEntry("tarte_lune", MapColor.COLOR_PURPLE, FoodType.SIMPLE, 2, 0.5f, false);

		CLAY_SAUCER = YoukaisHomecoming.REGISTRATE.item("clay_saucer", Item::new).register();

		SAUCER = YoukaisHomecoming.REGISTRATE.block("saucer", p -> new EmptySaucerBlock(
						BlockBehaviour.Properties.copy(Blocks.LIGHT_GRAY_WOOL)))
				.blockstate((ctx, pvd) -> pvd.horizontalBlock(ctx.get(),
						state -> state.getValue(EmptySaucerBlock.TYPE).build(pvd)))
				.item().model((ctx, pvd) -> pvd.generated(ctx)).build()
				.register();

		YHDish.register();
		YHCoffee.register();

		LAMPREY_BUCKET = YoukaisHomecoming.REGISTRATE
				.item("lamprey_bucket", p -> new MobBucketItem(
						YHEntities.LAMPREY, () -> Fluids.WATER, () -> SoundEvents.BUCKET_EMPTY_FISH,
						p.stacksTo(1).craftRemainder(Items.BUCKET)))
				.defaultLang()
				.register();
	}

	public static <T extends Item> ItemEntry<T> crop(String id, NonNullFunction<Item.Properties, T> factory) {
		return YoukaisHomecoming.REGISTRATE.item(id, factory)
				.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/crops/" + ctx.getName())))
				.register();
	}

	public static void register() {
	}

}
