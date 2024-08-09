package dev.xkmc.youkaishomecoming.init.registrate;

import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import dev.xkmc.fruitsdelight.init.FruitsDelight;
import dev.xkmc.youkaishomecoming.compat.food.FruitsDelightCompatFood;
import dev.xkmc.youkaishomecoming.content.block.food.EmptySaucerBlock;
import dev.xkmc.youkaishomecoming.content.block.food.FleshFeastBlock;
import dev.xkmc.youkaishomecoming.content.block.food.SurpriseChestBlock;
import dev.xkmc.youkaishomecoming.content.block.food.SurpriseFeastBlock;
import dev.xkmc.youkaishomecoming.content.item.curio.wings.CirnoWingsItem;
import dev.xkmc.youkaishomecoming.content.item.fluid.BottledFluid;
import dev.xkmc.youkaishomecoming.content.item.fluid.SakeBottleItem;
import dev.xkmc.youkaishomecoming.content.item.food.FleshBlockItem;
import dev.xkmc.youkaishomecoming.content.item.food.FleshSimpleItem;
import dev.xkmc.youkaishomecoming.content.item.misc.BloodBottleItem;
import dev.xkmc.youkaishomecoming.content.item.misc.FairyIceItem;
import dev.xkmc.youkaishomecoming.content.item.misc.FrozenFrogItem;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.data.YHTagGen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.animal.FrogVariant;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.Tags;
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

	public static final ItemEntry<StrawHatItem> STRAW_HAT;
	public static final ItemEntry<SuwakoHatItem> SUWAKO_HAT;
	public static final ItemEntry<KoishiHatItem> KOISHI_HAT;
	public static final ItemEntry<RumiaHairbandItem> RUMIA_HAIRBAND;
	public static final ItemEntry<ReimuHairbandItem> REIMU_HAIRBAND;
	public static final ItemEntry<CirnoHairbandItem> CIRNO_HAIRBAND;
	public static final ItemEntry<CirnoWingsItem> CIRNO_WINGS;

	public static final BottledFluid<BloodBottleItem> BLOOD_BOTTLE;
	public static final ItemEntry<FairyIceItem> FAIRY_ICE_CRYSTAL;
	public static final ItemEntry<FrozenFrogItem> FROZEN_FROG_COLD, FROZEN_FROG_WARM, FROZEN_FROG_TEMPERATE;

	public static final ItemEntry<FleshSimpleItem> RAW_FLESH_FEAST;
	public static final BlockEntry<FleshFeastBlock> FLESH_FEAST;
	public static final CakeEntry RED_VELVET;


	static {

		// gears
		{
			STRAW_HAT = YoukaisHomecoming.REGISTRATE
					.item("straw_hat", p -> new StrawHatItem(p.rarity(Rarity.UNCOMMON)))
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/curio/" + ctx.getName())))
					.register();

			SUWAKO_HAT = YoukaisHomecoming.REGISTRATE
					.item("suwako_hat", p -> new SuwakoHatItem(p.rarity(Rarity.EPIC)))
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/curio/" + ctx.getName())))
					.tag(ItemTags.HEAD_ARMOR, YHTagGen.TOUHOU_HAT)
					.register();

			KOISHI_HAT = YoukaisHomecoming.REGISTRATE
					.item("koishi_hat", p -> new KoishiHatItem(p.rarity(Rarity.EPIC)))
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/curio/" + ctx.getName())))
					.tag(ItemTags.HEAD_ARMOR, YHTagGen.TOUHOU_HAT)
					.register();

			RUMIA_HAIRBAND = YoukaisHomecoming.REGISTRATE
					.item("rumia_hairband", p -> new RumiaHairbandItem(p.rarity(Rarity.EPIC)))
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/curio/" + ctx.getName())))
					.tag(ItemTags.HEAD_ARMOR, YHTagGen.TOUHOU_HAT)
					.register();

			REIMU_HAIRBAND = YoukaisHomecoming.REGISTRATE
					.item("reimu_hairband", p -> new ReimuHairbandItem(p.rarity(Rarity.EPIC)))
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/curio/" + ctx.getName())))
					.tag(ItemTags.HEAD_ARMOR, YHTagGen.TOUHOU_HAT)
					.register();


			CIRNO_HAIRBAND = YoukaisHomecoming.REGISTRATE
					.item("cirno_hairband", p -> new CirnoHairbandItem(p.rarity(Rarity.EPIC)))
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/curio/" + ctx.getName())))
					.tag(ItemTags.HEAD_ARMOR, YHTagGen.TOUHOU_HAT)
					.removeTab(YoukaisHomecoming.TAB.getKey())
					.register();

			var back = ItemTags.create(ResourceLocation.fromNamespaceAndPath("curios", "back"));

			CIRNO_WINGS = YoukaisHomecoming.REGISTRATE
					.item("cirno_wings", p -> new CirnoWingsItem(p.rarity(Rarity.EPIC)))
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/curio/" + ctx.getName())))
					.tag(back, YHTagGen.TOUHOU_WINGS)
					.removeTab(YoukaisHomecoming.TAB.getKey())
					.register();

		}

		// ingredients
		{
			BLOOD_BOTTLE = new BottledFluid<>("blood", 0xff772221, () -> Items.GLASS_BOTTLE, "ingredient", BloodBottleItem::new);

			FAIRY_ICE_CRYSTAL = ingredient("fairy_ice_crystal", FairyIceItem::new);
			FROZEN_FROG_COLD = ingredient("frozen_frog_cold", p -> new FrozenFrogItem(p.stacksTo(16), FrogVariant.COLD));
			FROZEN_FROG_WARM = ingredient("frozen_frog_warm", p -> new FrozenFrogItem(p.stacksTo(16), FrogVariant.WARM));
			FROZEN_FROG_TEMPERATE = ingredient("frozen_frog_temperate", p -> new FrozenFrogItem(p.stacksTo(16), FrogVariant.TEMPERATE));
		}

		// feasts
		{

			RAW_FLESH_FEAST = YoukaisHomecoming.REGISTRATE.item("raw_flesh_feast", FleshSimpleItem::new)
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/feast/" + ctx.getName())))
					.lang("Raw %1$s Feast")
					.register();

			FLESH_FEAST = YoukaisHomecoming.REGISTRATE.block("flesh_feast", p ->
							new FleshFeastBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BROWN_WOOL),
									YHFood.BOWL_OF_FLESH_FEAST.item))
					.blockstate((ctx, pvd) -> pvd.horizontalBlock(ctx.get(), state ->
							FleshFeastBlock.Model.values()[state.getValue(FeastBlock.SERVINGS)].build(pvd)))
					.lang("%1$s Feast")
					.item(FleshBlockItem::new).model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/feast/" + ctx.getName()))).build()
					.loot(FleshFeastBlock::builtLoot)
					.register();

			RED_VELVET = new CakeEntry("red_velvet", MapColor.COLOR_RED, FoodType.FLESH, 1, 0.8f, true);
		}

	}

	public static <T extends Item> ItemEntry<T> ingredient(String id, NonNullFunction<Item.Properties, T> factory) {
		return YoukaisHomecoming.REGISTRATE.item(id, factory)
				.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/ingredient/" + ctx.getName())))
				.register();
	}

	public static void register() {
	}

}
