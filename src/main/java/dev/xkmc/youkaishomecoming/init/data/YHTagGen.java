package dev.xkmc.youkaishomecoming.init.data;

import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateItemTagsProvider;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import dev.xkmc.youkaishomecoming.compat.sereneseasons.SeasonCompat;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.food.YHCrops;
import dev.xkmc.youkaishomecoming.init.food.YHTea;
import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import sereneseasons.core.SereneSeasons;
import vectorwing.farmersdelight.common.registry.ModBlocks;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class YHTagGen {

	public static final TagKey<Item> RAW_EEL = forgeItem("raw_fishes/eel");
	public static final TagKey<Item> COOKED_EEL = forgeItem("cooked_fishes/eel");
	public static final TagKey<Item> RAW_TUNA = forgeItem("raw_fishes/tuna");
	public static final TagKey<Item> COOKED_TUNA = forgeItem("cooked_fishes/tuna");
	public static final TagKey<Item> RAW_VENISON = forgeItem("raw_venison");
	public static final TagKey<Item> COOKED_VENISON = forgeItem("cooked_venison");
	public static final TagKey<Item> RAW_CRAB = forgeItem("raw_crab");
	public static final TagKey<Item> COOKED_CRAB = forgeItem("cooked_crab");
	public static final TagKey<Item> BUTTER = forgeItem("butter");
	public static final TagKey<Item> CUCUMBER = forgeItem("vegetables/cucumber");
	public static final TagKey<Item> CUCUMBER_SLICE = forgeItem("slices/cucumber");
	public static final TagKey<Item> CUCUMBER_SEED = forgeItem("seeds/cucumber");
	public static final TagKey<Item> VEGE = forgeItem("vegetables");
	public static final TagKey<Item> GRAPE = forgeItem("fruits/grape");
	public static final TagKey<Item> FRUIT = forgeItem("fruits");
	public static final TagKey<Item> SEED = forgeItem("seeds");
	public static final TagKey<Item> GRAPE_SEED = item("grape_seed");
	public static final TagKey<Item> TAMAGOYAKI = item("tamagoyaki");
	public static final TagKey<Item> DANGO = item("dango");
	public static final TagKey<Item> TEA_DRINK = item("tea");
	public static final TagKey<Item> SAKE = item("sake");
	public static final TagKey<Item> WINE = item("wine");
	public static final TagKey<Item> BOTTLED = item("bottled");
	public static final TagKey<Item> STEAM_BLOCKER = item("steam_blocker");
	public static final TagKey<Item> IRON_BOWL_FOOD = item("iron_bowl_food");
	public static final TagKey<Block> FARMLAND_SOYBEAN = block("farmland_soybean");
	public static final TagKey<Block> FARMLAND_REDBEAN = block("farmland_redbean");
	public static final TagKey<Block> FARMLAND_TEA = block("farmland_tea");
	public static final TagKey<Block> CRAB_DIGABLE = block("crab_digable");


	public static final TagKey<Item> MATCHA = forgeItem("matcha");
	public static final TagKey<Item> ICE = forgeItem("ice_cubes");

	public static final TagKey<Item> TEA_GREEN = forgeItem("tea_leaves/green");
	public static final TagKey<Item> TEA_BLACK = forgeItem("tea_leaves/black");
	public static final TagKey<Item> TEA_WHITE = forgeItem("tea_leaves/white");
	public static final TagKey<Item> TEA_OOLONG = forgeItem("tea_leaves/oolong");
	public static final TagKey<Item> TEA_DARK = forgeItem("tea_leaves/dark");
	public static final TagKey<Item> TEA_YELLOW = forgeItem("tea_leaves/yellow");
	public static final TagKey<Item> TEA = forgeItem("tea_leaves");

	public static final List<Consumer<RegistrateItemTagsProvider>> OPTIONAL_TAGS = new ArrayList<>();

	public static void onEntityTagGen(RegistrateTagsProvider.IntrinsicImpl<EntityType<?>> pvd) {

	}

	public static void onBlockTagGen(RegistrateTagsProvider.IntrinsicImpl<Block> pvd) {
		pvd.addTag(FARMLAND_SOYBEAN).add(Blocks.FARMLAND, ModBlocks.RICH_SOIL_FARMLAND.get());
		pvd.addTag(FARMLAND_REDBEAN).add(Blocks.CLAY, Blocks.MUD, Blocks.COARSE_DIRT, ModBlocks.RICH_SOIL_FARMLAND.get());
		pvd.addTag(FARMLAND_TEA).add(Blocks.GRASS, ModBlocks.RICH_SOIL.get());
		pvd.addTag(CRAB_DIGABLE).add(Blocks.SAND, Blocks.GRAVEL);
		if (ModList.get().isLoaded(SereneSeasons.MOD_ID)) {
			SeasonCompat.genBlock(pvd);
		}
	}

	@SuppressWarnings("unchecked")
	public static void onItemTagGen(RegistrateItemTagsProvider pvd) {
		pvd.addTag(VEGE).addTag(CUCUMBER);
		pvd.addTag(CUCUMBER).add(YHCrops.CUCUMBER.getFruits());
		pvd.addTag(SEED).addTag(CUCUMBER_SEED);
		pvd.addTag(CUCUMBER_SEED).add(YHCrops.CUCUMBER.getSeed());
		pvd.addTag(GRAPE).add(YHCrops.RED_GRAPE.getFruits(), YHCrops.BLACK_GRAPE.getFruits(), YHCrops.WHITE_GRAPE.getFruits());
		pvd.addTag(FRUIT).addTag(GRAPE);
		pvd.addTag(GRAPE_SEED).add(YHCrops.RED_GRAPE.getSeed(), YHCrops.BLACK_GRAPE.getSeed(), YHCrops.WHITE_GRAPE.getSeed());

		pvd.addTag(MATCHA).add(YHItems.MATCHA.get()).addOptional(new ResourceLocation("delightful", "matcha"));
		pvd.addTag(ICE).add(YHItems.ICE_CUBE.get());
		pvd.addTag(TEA_GREEN).add(YHTea.GREEN.leaves.get());
		pvd.addTag(TEA_BLACK).add(YHTea.BLACK.leaves.get());
		pvd.addTag(TEA_WHITE).add(YHTea.WHITE.leaves.get());
		pvd.addTag(TEA_OOLONG).add(YHTea.OOLONG.leaves.get());
		pvd.addTag(TEA_DARK).add(YHTea.DARK.leaves.get());
		pvd.addTag(TEA_YELLOW).add(YHTea.YELLOW.leaves.get());
		pvd.addTag(TEA).add(YHCrops.TEA.getFruits())
				.addTags(TEA_GREEN, TEA_BLACK, TEA_WHITE, TEA_OOLONG, TEA_DARK, TEA_YELLOW);
		if (ModList.get().isLoaded(SereneSeasons.MOD_ID)) {
			SeasonCompat.genItem(pvd);
		}
		for (var e : OPTIONAL_TAGS) {
			e.accept(pvd);
		}

	}

	public static TagKey<Item> item(String id) {
		return ItemTags.create(YoukaisHomecoming.loc(id));
	}

	public static TagKey<Item> forgeItem(String id) {
		return ItemTags.create(new ResourceLocation("forge", id));
	}

	public static TagKey<Block> block(String id) {
		return BlockTags.create(YoukaisHomecoming.loc(id));
	}

	public static TagKey<EntityType<?>> entity(String id) {
		return TagKey.create(Registries.ENTITY_TYPE, YoukaisHomecoming.loc(id));
	}

	public static TagKey<EntityType<?>> entity(String mod, String id) {
		return TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(mod, id));
	}

}
