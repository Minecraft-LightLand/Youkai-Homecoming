package dev.xkmc.youkaishomecoming.init.data;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import vectorwing.farmersdelight.common.tag.ModTags;

public class TagRef {
	public static final TagKey<Item> BERRIES = forgeItemTag("berries");
	public static final TagKey<Item> BREAD = forgeItemTag("bread");
	public static final TagKey<Item> BREAD_WHEAT = forgeItemTag("bread/wheat");
	public static final TagKey<Item> COOKED_BACON = forgeItemTag("cooked_bacon");
	public static final TagKey<Item> COOKED_BEEF = forgeItemTag("cooked_beef");
	public static final TagKey<Item> COOKED_CHICKEN = forgeItemTag("cooked_chicken");
	public static final TagKey<Item> COOKED_PORK = forgeItemTag("cooked_pork");
	public static final TagKey<Item> COOKED_MUTTON = forgeItemTag("cooked_mutton");
	public static final TagKey<Item> COOKED_EGGS = forgeItemTag("cooked_eggs");
	public static final TagKey<Item> COOKED_FISHES = forgeItemTag("cooked_fishes");
	public static final TagKey<Item> COOKED_FISHES_COD = forgeItemTag("cooked_fishes/cod");
	public static final TagKey<Item> COOKED_FISHES_SALMON = forgeItemTag("cooked_fishes/salmon");
	public static final TagKey<Item> CROPS = forgeItemTag("crops");
	public static final TagKey<Item> CROPS_CABBAGE = forgeItemTag("crops/cabbage");
	public static final TagKey<Item> CROPS_ONION = forgeItemTag("crops/onion");
	public static final TagKey<Item> CROPS_RICE = forgeItemTag("crops/rice");
	public static final TagKey<Item> CROPS_TOMATO = forgeItemTag("crops/tomato");
	public static final TagKey<Item> DOUGH = forgeItemTag("dough");
	public static final TagKey<Item> DOUGH_WHEAT = forgeItemTag("dough/wheat");
	public static final TagKey<Item> EGGS = forgeItemTag("eggs");
	public static final TagKey<Item> GRAIN = forgeItemTag("grain");
	public static final TagKey<Item> GRAIN_WHEAT = forgeItemTag("grain/wheat");
	public static final TagKey<Item> GRAIN_RICE = forgeItemTag("grain/rice");
	public static final TagKey<Item> MILK = forgeItemTag("milk");
	public static final TagKey<Item> MILK_BUCKET = forgeItemTag("milk/milk");
	public static final TagKey<Item> MILK_BOTTLE = forgeItemTag("milk/milk_bottle");
	public static final TagKey<Item> PASTA = forgeItemTag("pasta");
	public static final TagKey<Item> PASTA_RAW_PASTA = forgeItemTag("pasta/raw_pasta");
	public static final TagKey<Item> RAW_MEAT = forgeItemTag("raw_meat");
	public static final TagKey<Item> RAW_BACON = forgeItemTag("raw_bacon");
	public static final TagKey<Item> RAW_BEEF = forgeItemTag("raw_beef");
	public static final TagKey<Item> RAW_CHICKEN = forgeItemTag("raw_chicken");
	public static final TagKey<Item> RAW_PORK = forgeItemTag("raw_pork");
	public static final TagKey<Item> RAW_MUTTON = forgeItemTag("raw_mutton");
	public static final TagKey<Item> RAW_FISHES = forgeItemTag("raw_fishes");
	public static final TagKey<Item> RAW_FISHES_COD = forgeItemTag("raw_fishes/cod");
	public static final TagKey<Item> RAW_FISHES_SALMON = forgeItemTag("raw_fishes/salmon");
	public static final TagKey<Item> RAW_FISHES_TROPICAL = forgeItemTag("raw_fishes/tropical_fish");
	public static final TagKey<Item> SALAD_INGREDIENTS = forgeItemTag("salad_ingredients");
	public static final TagKey<Item> FOOD_CABBAGE = forgeItemTag("salad_ingredients/cabbage");
	public static final TagKey<Item> SEEDS = forgeItemTag("seeds");
	public static final TagKey<Item> SEEDS_CABBAGE = forgeItemTag("seeds/cabbage");
	public static final TagKey<Item> SEEDS_RICE = forgeItemTag("seeds/rice");
	public static final TagKey<Item> SEEDS_TOMATO = forgeItemTag("seeds/tomato");
	public static final TagKey<Item> VEGETABLES = forgeItemTag("vegetables");
	public static final TagKey<Item> VEGETABLES_BEETROOT = forgeItemTag("vegetables/beetroot");
	public static final TagKey<Item> VEGETABLES_CARROT = forgeItemTag("vegetables/carrot");
	public static final TagKey<Item> VEGETABLES_ONION = forgeItemTag("vegetables/onion");
	public static final TagKey<Item> VEGETABLES_POTATO = forgeItemTag("vegetables/potato");
	public static final TagKey<Item> VEGETABLES_TOMATO = forgeItemTag("vegetables/tomato");
	public static final TagKey<Item> TOOLS = forgeItemTag("tools");
	public static final TagKey<Item> TOOLS_AXES = forgeItemTag("tools/axes");
	public static final TagKey<Item> TOOLS_KNIVES = forgeItemTag("tools/knives");
	public static final TagKey<Item> TOOLS_PICKAXES = forgeItemTag("tools/pickaxes");
	public static final TagKey<Item> TOOLS_SHOVELS = forgeItemTag("tools/shovels");


	public static final TagKey<Item> SNACKS = modItemTag("snacks");
	public static final TagKey<Item> MEALS = modItemTag("meals");
	public static final TagKey<Item> DRINKS = modItemTag("drinks");
	public static final TagKey<Item> SWEETS = modItemTag("sweets");
	public static final TagKey<Item> FEASTS_ITEM = modItemTag("feasts");
	public static final TagKey<Item> PIES_ITEM = modItemTag("pies");
	public static final TagKey<Item> FLAT_ON_CUTTING_BOARD = modItemTag("flat_on_cutting_board");
	public static final TagKey<Item> SERVING_CONTAINERS = modItemTag("serving_containers");
	public static final TagKey<Item> WILD_CROPS_ITEM = modItemTag("wild_crops");
	
	public static final TagKey<Block> CAMPFIRE_SIGNAL_SMOKE = modBlockTag("campfire_signal_smoke");
	public static final TagKey<Block> COMPOST_ACTIVATORS = modBlockTag("compost_activators");
	public static final TagKey<Block> DROPS_CAKE_SLICE = modBlockTag("drops_cake_slice");
	public static final TagKey<Block> HEAT_CONDUCTORS = modBlockTag("heat_conductors");
	public static final TagKey<Block> HEAT_SOURCES = modBlockTag("heat_sources");
	public static final TagKey<Block> TRAY_HEAT_SOURCES = modBlockTag("tray_heat_sources");
	public static final TagKey<Block> MUSHROOM_COLONY_GROWABLE_ON = modBlockTag("mushroom_colony_growable_on");
	public static final TagKey<Block> PLANTED_FROM_BELOW = modBlockTag("planted_from_below");
	public static final TagKey<Block> FEASTS = modBlockTag("feasts");
	public static final TagKey<Block> PIES = modBlockTag("pies");
	public static final TagKey<Block> STRAW_BLOCKS = modBlockTag("straw_blocks");
	public static final TagKey<Block> TERRAIN = modBlockTag("terrain");
	public static final TagKey<Block> UNAFFECTED_BY_RICH_SOIL = modBlockTag("unaffected_by_rich_soil");
	public static final TagKey<Block> CABINETS = modBlockTag("cabinets");
	public static final TagKey<Block> CABINETS_WOODEN = modBlockTag("cabinets/wooden");
	public static final TagKey<Block> MUSHROOM_COLONIES = modBlockTag("mushroom_colonies");
	public static final TagKey<Block> MINEABLE_WITH_KNIFE = modBlockTag("mineable/knife");
	public static final TagKey<Block> ROPES = modBlockTag("ropes");
	public static final TagKey<Block> WILD_CROPS = modBlockTag("wild_crops");
	
	private static TagKey<Block> forgeBlockTag(String path) {
		return BlockTags.create(new ResourceLocation("forge", path));
	}

	private static TagKey<Item> forgeItemTag(String path) {
		return ItemTags.create(new ResourceLocation("forge", path));
	}

	private static TagKey<Item> modItemTag(String path) {
		return ItemTags.create(new ResourceLocation("farmersdelight", path));
	}

	private static TagKey<Block> modBlockTag(String path) {
		return BlockTags.create(new ResourceLocation("farmersdelight", path));
	}

}
