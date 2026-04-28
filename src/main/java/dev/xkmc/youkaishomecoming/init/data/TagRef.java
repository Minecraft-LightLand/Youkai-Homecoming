package dev.xkmc.youkaishomecoming.init.data;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import vectorwing.farmersdelight.common.tag.CommonTags;
import vectorwing.farmersdelight.common.tag.ModTags;

public class TagRef {

	public static final TagKey<Item> BERRIES = Tags.Items.FOODS_BERRY;
	public static final TagKey<Item> BREAD = Tags.Items.FOODS_BREAD;
	public static final TagKey<Item> BREAD_WHEAT = Tags.Items.FOODS_BREAD;
	public static final TagKey<Item> CROPS = Tags.Items.CROPS;
	public static final TagKey<Item> EGGS = Tags.Items.EGGS;
	public static final TagKey<Item> SEEDS = Tags.Items.SEEDS;
	public static final TagKey<Item> VEGETABLES = YHTagGen.VEGE;
	public static final TagKey<Item> VEGETABLES_BEETROOT = Tags.Items.CROPS_BEETROOT;
	public static final TagKey<Item> VEGETABLES_CARROT = Tags.Items.CROPS_CARROT;
	public static final TagKey<Item> VEGETABLES_POTATO = Tags.Items.CROPS_POTATO;
	public static final TagKey<Item> TOOLS_PICKAXES = ItemTags.PICKAXES;
	public static final TagKey<Item> TOOLS_SHOVELS = ItemTags.SHOVELS;


	public static final TagKey<Item> CROPS_TOMATO = commonItemTag("crops/tomato");
	public static final TagKey<Item> CROPS_ONION = commonItemTag("crops/onion");
	public static final TagKey<Item> GRAIN_RICE = commonItemTag("crops/rice");
	public static final TagKey<Item> CROPS_GRAIN = commonItemTag("crops/grain");
	public static final TagKey<Item> FOOD_CABBAGE = commonItemTag("foods/cabbage");
	public static final TagKey<Item> VEGETABLES_TOMATO = commonItemTag("foods/tomato");
	public static final TagKey<Item> FOODS_ONION = commonItemTag("foods/onion");
	public static final TagKey<Item> FOODS_LEAFY_GREEN = commonItemTag("foods/leafy_green");
	public static final TagKey<Item> DOUGH = commonItemTag("foods/dough");
	public static final TagKey<Item> DOUGH_WHEAT = commonItemTag("foods/dough/wheat");
	public static final TagKey<Item> PASTA = commonItemTag("foods/pasta");
	public static final TagKey<Item> FOODS_RAW_BACON = commonItemTag("foods/raw_bacon");
	public static final TagKey<Item> RAW_BEEF = commonItemTag("foods/raw_beef");
	public static final TagKey<Item> RAW_CHICKEN = commonItemTag("foods/raw_chicken");
	public static final TagKey<Item> RAW_PORK = commonItemTag("foods/raw_pork");
	public static final TagKey<Item> FOODS_RAW_MUTTON = commonItemTag("foods/raw_mutton");
	public static final TagKey<Item> RAW_FISHES = commonItemTag("foods/safe_raw_fish");
	public static final TagKey<Item> RAW_FISHES_COD = commonItemTag("foods/raw_cod");
	public static final TagKey<Item> RAW_FISHES_SALMON = commonItemTag("foods/raw_salmon");
	public static final TagKey<Item> TOOLS_KNIFE = commonItemTag("tools/knife");
	public static final TagKey<Item> VEGETABLES_ONION = CROPS_ONION;
	public static final TagKey<Item> MILK_BOTTLE = Tags.Items.DRINKS_MILK;
	public static final TagKey<Item> RAW_MEAT = Tags.Items.FOODS_RAW_MEAT;


	public static final TagKey<Item> SNACKS = modItemTag("snacks");
	public static final TagKey<Item> MEALS = modItemTag("meals");
	public static final TagKey<Item> DRINKS = modItemTag("drinks");
	public static final TagKey<Item> SWEETS = modItemTag("sweets");
	public static final TagKey<Item> FEASTS_ITEM = modItemTag("feasts");
	public static final TagKey<Item> PIES_ITEM = modItemTag("pies");
	public static final TagKey<Item> WILD_CROPS_ITEM = modItemTag("wild_crops");

	public static final TagKey<Block> HEAT_CONDUCTORS = modBlockTag("heat_conductors");
	public static final TagKey<Block> HEAT_SOURCES = modBlockTag("heat_sources");
	public static final TagKey<Block> TRAY_HEAT_SOURCES = modBlockTag("tray_heat_sources");
	public static final TagKey<Block> WILD_CROPS = modBlockTag("wild_crops");
	public static final TagKey<Block> ROPES = modBlockTag("ropes");

	private static TagKey<Block> commonBlockTag(String path) {
		return BlockTags.create(ResourceLocation.fromNamespaceAndPath("c", path));
	}

	private static TagKey<Item> commonItemTag(String path) {
		return ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", path));
	}

	private static TagKey<Item> modItemTag(String path) {
		return ItemTags.create(ResourceLocation.fromNamespaceAndPath("farmersdelight", path));
	}

	private static TagKey<Block> modBlockTag(String path) {
		return BlockTags.create(ResourceLocation.fromNamespaceAndPath("farmersdelight", path));
	}


}
