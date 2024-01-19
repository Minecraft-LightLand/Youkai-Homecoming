package dev.xkmc.youkaihomecoming.init.registrate;

import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.youkaihomecoming.content.block.EmptySaucerBlock;
import dev.xkmc.youkaihomecoming.content.block.FleshFeastBlock;
import dev.xkmc.youkaihomecoming.content.item.FleshBlockItem;
import dev.xkmc.youkaihomecoming.content.item.FleshSimpleItem;
import dev.xkmc.youkaihomecoming.content.item.SuwakoHatItem;
import dev.xkmc.youkaihomecoming.init.YoukaiHomecoming;
import dev.xkmc.youkaihomecoming.init.food.YHCrops;
import dev.xkmc.youkaihomecoming.init.food.YHDish;
import dev.xkmc.youkaihomecoming.init.food.YHFood;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.Tags;
import org.apache.commons.lang3.StringUtils;
import vectorwing.farmersdelight.common.block.FeastBlock;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class YHItems {

	private static final Set<String> SMALL_WORDS = Set.of("of", "the", "with");

	public static String toEnglishName(String internalName) {
		return Arrays.stream(internalName.split("_"))
				.map(e -> SMALL_WORDS.contains(e) ? e : StringUtils.capitalize(e))
				.collect(Collectors.joining(" "));
	}

	public static final ItemEntry<SuwakoHatItem> SUWAKO_HAT;
	public static final ItemEntry<Item> BLOOD_BOTTLE, SOY_SAUCE_BOTTLE, CLAY_SAUCER;
	public static final ItemEntry<FleshSimpleItem> RAW_FLESH_FEAST;
	public static final BlockEntry<FleshFeastBlock> FLESH_FEAST;
	public static final BlockEntry<EmptySaucerBlock> SAUCER;
	public static final ItemEntry<MobBucketItem> LAMPREY_BUCKET;


	static {
		SUWAKO_HAT = YoukaiHomecoming.REGISTRATE
				.item("suwako_hat", p -> new SuwakoHatItem(p.rarity(Rarity.EPIC)))
				.tag(Tags.Items.ARMORS_HELMETS)
				.register();

		YHCrops.register();

		SOY_SAUCE_BOTTLE = YoukaiHomecoming.REGISTRATE
				.item("soy_sauce_bottle", p -> new Item(p.craftRemainder(Items.GLASS_BOTTLE)))
				.register();

		BLOOD_BOTTLE = YoukaiHomecoming.REGISTRATE
				.item("blood_bottle", p -> new Item(p.craftRemainder(Items.GLASS_BOTTLE)))
				.register();

		YHFood.register();

		RAW_FLESH_FEAST = YoukaiHomecoming.REGISTRATE.item("raw_flesh_feast", FleshSimpleItem::new)
				.lang("Raw %1$s Feast")
				.register();

		FLESH_FEAST = YoukaiHomecoming.REGISTRATE
				.block("flesh_feast", p -> new FleshFeastBlock(BlockBehaviour.Properties.copy(Blocks.BROWN_WOOL),
						YHFood.BOWL_OF_FLESH_FEAST.item))
				.blockstate((ctx, pvd) -> pvd.horizontalBlock(ctx.get(), state ->
						FleshFeastBlock.Model.values()[state.getValue(FeastBlock.SERVINGS)].build(pvd)))
				.lang("%1$s Feast")
				.item(FleshBlockItem::new).model((ctx, pvd) -> pvd.generated(ctx)).build()
				.register();

		CLAY_SAUCER = YoukaiHomecoming.REGISTRATE.item("clay_saucer", Item::new).register();

		SAUCER = YoukaiHomecoming.REGISTRATE.block("saucer", p -> new EmptySaucerBlock(
						BlockBehaviour.Properties.copy(Blocks.LIGHT_GRAY_WOOL)))
				.blockstate((ctx, pvd) -> pvd.horizontalBlock(ctx.get(),
						state -> state.getValue(EmptySaucerBlock.TYPE).build(pvd)))
				.item().model((ctx, pvd) -> pvd.generated(ctx)).build()
				.register();

		YHDish.register();

		LAMPREY_BUCKET = YoukaiHomecoming.REGISTRATE
				.item("lamprey_bucket", p -> new MobBucketItem(
						YHEntities.LAMPREY, () -> Fluids.WATER, () -> SoundEvents.BUCKET_EMPTY_FISH,
						p.stacksTo(1).craftRemainder(Items.BUCKET)))
				.defaultLang()
				.register();
	}

	public static void register() {
	}

}
