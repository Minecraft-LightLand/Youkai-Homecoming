package dev.xkmc.youkaishomecoming.init.data;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import vectorwing.farmersdelight.common.tag.ForgeTags;

public class TagRef {

	public static final TagKey<Item> BERRIES = ForgeTags.BERRIES;
	public static final TagKey<Item> BREAD = ForgeTags.BREAD;
	public static final TagKey<Item> BREAD_WHEAT = ForgeTags.BREAD_WHEAT;
	public static final TagKey<Item> COOKED_BACON = ForgeTags.COOKED_BACON;
	public static final TagKey<Item> COOKED_BEEF = ForgeTags.COOKED_BEEF;
	public static final TagKey<Item> COOKED_CHICKEN = ForgeTags.COOKED_CHICKEN;
	public static final TagKey<Item> COOKED_PORK = ForgeTags.COOKED_PORK;
	public static final TagKey<Item> COOKED_MUTTON = ForgeTags.COOKED_MUTTON;
	public static final TagKey<Item> COOKED_EGGS = ForgeTags.COOKED_EGGS;
	public static final TagKey<Item> COOKED_FISHES = ForgeTags.COOKED_FISHES;
	public static final TagKey<Item> COOKED_FISHES_COD = ForgeTags.COOKED_FISHES_COD;
	public static final TagKey<Item> COOKED_FISHES_SALMON = ForgeTags.COOKED_FISHES_SALMON;
	public static final TagKey<Item> CROPS = ForgeTags.CROPS;
	public static final TagKey<Item> CROPS_CABBAGE = ForgeTags.CROPS_CABBAGE;
	public static final TagKey<Item> CROPS_ONION = ForgeTags.CROPS_ONION;
	public static final TagKey<Item> CROPS_RICE = ForgeTags.CROPS_RICE;
	public static final TagKey<Item> CROPS_TOMATO = ForgeTags.CROPS_TOMATO;
	public static final TagKey<Item> DOUGH = ForgeTags.DOUGH;
	public static final TagKey<Item> DOUGH_WHEAT = ForgeTags.DOUGH_WHEAT;
	public static final TagKey<Item> EGGS = ForgeTags.EGGS;
	public static final TagKey<Item> GRAIN = ForgeTags.GRAIN;
	public static final TagKey<Item> GRAIN_WHEAT = ForgeTags.GRAIN_WHEAT;
	public static final TagKey<Item> GRAIN_RICE = ForgeTags.GRAIN_RICE;
	public static final TagKey<Item> MILK = ForgeTags.MILK;
	public static final TagKey<Item> MILK_BUCKET = ForgeTags.MILK_BUCKET;
	public static final TagKey<Item> MILK_BOTTLE = ForgeTags.MILK_BOTTLE;
	public static final TagKey<Item> PASTA = ForgeTags.PASTA;
	public static final TagKey<Item> PASTA_RAW_PASTA = ForgeTags.PASTA_RAW_PASTA;
	public static final TagKey<Item> RAW_BACON = ForgeTags.RAW_BACON;
	public static final TagKey<Item> RAW_BEEF = ForgeTags.RAW_BEEF;
	public static final TagKey<Item> RAW_CHICKEN = ForgeTags.RAW_CHICKEN;
	public static final TagKey<Item> RAW_PORK = ForgeTags.RAW_PORK;
	public static final TagKey<Item> RAW_MUTTON = ForgeTags.RAW_MUTTON;
	public static final TagKey<Item> RAW_FISHES = ForgeTags.RAW_FISHES;
	public static final TagKey<Item> RAW_FISHES_COD = ForgeTags.RAW_FISHES_COD;
	public static final TagKey<Item> RAW_FISHES_SALMON = ForgeTags.RAW_FISHES_SALMON;
	public static final TagKey<Item> RAW_FISHES_TROPICAL = ForgeTags.RAW_FISHES_TROPICAL;
	public static final TagKey<Item> SALAD_INGREDIENTS = ForgeTags.SALAD_INGREDIENTS;
	public static final TagKey<Item> FOOD_CABBAGE = ForgeTags.SALAD_INGREDIENTS_CABBAGE;
	public static final TagKey<Item> SEEDS = ForgeTags.SEEDS;
	public static final TagKey<Item> VEGETABLES = ForgeTags.VEGETABLES;
	public static final TagKey<Item> VEGETABLES_BEETROOT = ForgeTags.VEGETABLES_BEETROOT;
	public static final TagKey<Item> VEGETABLES_CARROT = ForgeTags.VEGETABLES_CARROT;
	public static final TagKey<Item> VEGETABLES_ONION = ForgeTags.VEGETABLES_ONION;
	public static final TagKey<Item> VEGETABLES_POTATO = ForgeTags.VEGETABLES_POTATO;
	public static final TagKey<Item> VEGETABLES_TOMATO = ForgeTags.VEGETABLES_TOMATO;
	public static final TagKey<Item> TOOLS = ForgeTags.TOOLS;
	public static final TagKey<Item> TOOLS_KNIVES = ForgeTags.TOOLS_KNIVES;
	public static final TagKey<Item> TOOLS_PICKAXES = ForgeTags.TOOLS_PICKAXES;
	public static final TagKey<Item> TOOLS_SHOVELS = ForgeTags.TOOLS_SHOVELS;

	public static TagKey<Item> forgeItemTag(String id) {
		return ItemTags.create(new ResourceLocation("forge", id));
	}

}
