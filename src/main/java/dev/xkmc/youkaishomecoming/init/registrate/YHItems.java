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
import dev.xkmc.youkaishomecoming.content.item.curio.hat.*;
import dev.xkmc.youkaishomecoming.content.item.curio.wings.CirnoWingsItem;
import dev.xkmc.youkaishomecoming.content.item.danmaku.SpellItem;
import dev.xkmc.youkaishomecoming.content.item.food.FleshBlockItem;
import dev.xkmc.youkaishomecoming.content.item.food.FleshSimpleItem;
import dev.xkmc.youkaishomecoming.content.item.misc.BloodBottleItem;
import dev.xkmc.youkaishomecoming.content.item.misc.FairyIceItem;
import dev.xkmc.youkaishomecoming.content.item.misc.FrozenFrogItem;
import dev.xkmc.youkaishomecoming.content.spell.game.reimu.ReimuSpell;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.data.YHTagGen;
import dev.xkmc.youkaishomecoming.init.food.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.animal.FrogVariant;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fml.ModList;
import org.apache.commons.lang3.StringUtils;
import vectorwing.farmersdelight.common.block.FeastBlock;
import vectorwing.farmersdelight.common.tag.ForgeTags;

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
	public static final ItemEntry<SpellItem> REIMU_SPELL;
	public static final BlockEntry<Block> SOYBEAN_BAG, REDBEAN_BAG, COFFEE_BEAN_BAG,
			TEA_BAG, BLACK_TEA_BAG, GREEN_TEA_BAG, OOLONG_TEA_BAG, WHITE_TEA_BAG;

	public static final ItemEntry<Item> SOY_SAUCE_BOTTLE, CLAY_SAUCER,
			COFFEE_BEAN, COFFEE_POWDER, CREAM, MATCHA,
			STRIPPED_MANDRAKE_ROOT, DRIED_MANDRAKE_FLOWER, CAN, ICE_CUBE;
	public static final ItemEntry<BloodBottleItem> BLOOD_BOTTLE;
	public static final ItemEntry<FairyIceItem> FAIRY_ICE_CRYSTAL;
	public static final ItemEntry<FrozenFrogItem> FROZEN_FROG_COLD, FROZEN_FROG_WARM, FROZEN_FROG_TEMPERATE;

	public static final BlockEntry<SurpriseChestBlock> SURP_CHEST;
	public static final BlockEntry<SurpriseFeastBlock> SURP_FEAST;
	public static final ItemEntry<FleshSimpleItem> RAW_FLESH_FEAST;
	public static final BlockEntry<FleshFeastBlock> FLESH_FEAST;
	public static final CakeEntry RED_VELVET, TARTE_LUNE;
	public static final BlockEntry<EmptySaucerBlock> SAUCER;
	public static final ItemEntry<MobBucketItem> LAMPREY_BUCKET;


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
					.tag(Tags.Items.ARMORS_HELMETS, YHTagGen.TOUHOU_HAT)
					.register();

			KOISHI_HAT = YoukaisHomecoming.REGISTRATE
					.item("koishi_hat", p -> new KoishiHatItem(p.rarity(Rarity.EPIC)))
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/curio/" + ctx.getName())))
					.tag(Tags.Items.ARMORS_HELMETS, YHTagGen.TOUHOU_HAT)
					.register();

			RUMIA_HAIRBAND = YoukaisHomecoming.REGISTRATE
					.item("rumia_hairband", p -> new RumiaHairbandItem(p.rarity(Rarity.EPIC)))
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/curio/" + ctx.getName())))
					.tag(Tags.Items.ARMORS_HELMETS, YHTagGen.TOUHOU_HAT)
					.register();

			REIMU_HAIRBAND = YoukaisHomecoming.REGISTRATE
					.item("reimu_hairband", p -> new ReimuHairbandItem(p.rarity(Rarity.EPIC)))
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/curio/" + ctx.getName())))
					.tag(Tags.Items.ARMORS_HELMETS, YHTagGen.TOUHOU_HAT)
					.register();


			CIRNO_HAIRBAND = YoukaisHomecoming.REGISTRATE
					.item("cirno_hairband", p -> new CirnoHairbandItem(p.rarity(Rarity.EPIC)))
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/curio/" + ctx.getName())))
					.tag(Tags.Items.ARMORS_HELMETS, YHTagGen.TOUHOU_HAT)
					.register();

			CIRNO_WINGS = YoukaisHomecoming.REGISTRATE
					.item("cirno_wings", p -> new CirnoWingsItem(p.rarity(Rarity.EPIC)))
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/curio/" + ctx.getName())))
					.tag(ItemTags.create(new ResourceLocation("curios", "back")))
					.register();

			REIMU_SPELL = YoukaisHomecoming.REGISTRATE
					.item("spell_reimu", p -> new SpellItem(
							p.stacksTo(1), ReimuSpell::new, true,
							() -> YHDanmaku.Bullet.CIRCLE.get(DyeColor.RED).get()))
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/curio/" + ctx.getName())))
					.lang("Reimu's Spellcard \"Innate Dream\"")
					.register();
		}

		// plants
		{
			YHCrops.register();
			COFFEE_BEAN = crop("coffee_beans", Item::new);
			COFFEE_POWDER = crop("coffee_powder", Item::new);
			YHTea.register();
			MATCHA = crop("matcha", Item::new);
			STRIPPED_MANDRAKE_ROOT = crop("stripped_mandrake_root", Item::new);
			DRIED_MANDRAKE_FLOWER = crop("dried_mandrake_flower", Item::new);
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
			SOY_SAUCE_BOTTLE = YoukaisHomecoming.REGISTRATE
					.item("soy_sauce_bottle", p -> new Item(p.craftRemainder(Items.GLASS_BOTTLE)))
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/ingredient/" + ctx.getName())))
					.register();

			BLOOD_BOTTLE = YoukaisHomecoming.REGISTRATE
					.item("blood_bottle", p -> new BloodBottleItem(p.craftRemainder(Items.GLASS_BOTTLE)))
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/ingredient/" + ctx.getName())))
					.register();

			CREAM = YoukaisHomecoming.REGISTRATE
					.item("bowl_of_cream", p -> new Item(p.craftRemainder(Items.BOWL)))
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/ingredient/" + ctx.getName())))
					.lang("Bowl of Cream")
					.register();

			ICE_CUBE = ingredient("ice_cube", Item::new);
			FAIRY_ICE_CRYSTAL = ingredient("fairy_ice_crystal", FairyIceItem::new);
			FROZEN_FROG_COLD = ingredient("frozen_frog_cold", p -> new FrozenFrogItem(p.stacksTo(16), FrogVariant.COLD));
			FROZEN_FROG_WARM = ingredient("frozen_frog_warm", p -> new FrozenFrogItem(p.stacksTo(16), FrogVariant.WARM));
			FROZEN_FROG_TEMPERATE = ingredient("frozen_frog_temperate", p -> new FrozenFrogItem(p.stacksTo(16), FrogVariant.TEMPERATE));
		}

		CAN = YoukaisHomecoming.REGISTRATE.item("can", Item::new).register();

		YHFood.register();

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

			RAW_FLESH_FEAST = YoukaisHomecoming.REGISTRATE.item("raw_flesh_feast", FleshSimpleItem::new)
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/feast/" + ctx.getName())))
					.lang("Raw %1$s Feast")
					.register();

			FLESH_FEAST = YoukaisHomecoming.REGISTRATE.block("flesh_feast", p ->
							new FleshFeastBlock(BlockBehaviour.Properties.copy(Blocks.BROWN_WOOL),
									YHFood.BOWL_OF_FLESH_FEAST.item))
					.blockstate((ctx, pvd) -> pvd.horizontalBlock(ctx.get(), state ->
							FleshFeastBlock.Model.values()[state.getValue(FeastBlock.SERVINGS)].build(pvd)))
					.lang("%1$s Feast")
					.item(FleshBlockItem::new).model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/feast/" + ctx.getName()))).build()
					.loot(FleshFeastBlock::builtLoot)
					.register();

			RED_VELVET = new CakeEntry("red_velvet", MapColor.COLOR_RED, FoodType.FLESH, 1, 0.8f, true);
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
		YHSake.register();

		if (ModList.get().isLoaded(FruitsDelight.MODID)) {
			FruitsDelightCompatFood.register();
		}

		LAMPREY_BUCKET = YoukaisHomecoming.REGISTRATE
				.item("lamprey_bucket", p -> new MobBucketItem(
						YHEntities.LAMPREY, () -> Fluids.WATER, () -> SoundEvents.BUCKET_EMPTY_FISH,
						p.stacksTo(1).craftRemainder(Items.BUCKET)))
				.defaultLang()
				.register();
	}

	public static <T extends Item> ItemEntry<T> seed(String id, NonNullFunction<Item.Properties, T> factory) {
		return YoukaisHomecoming.REGISTRATE.item(id, factory)
				.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/crops/" + ctx.getName())))
				.tag(ForgeTags.SEEDS)
				.register();
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
