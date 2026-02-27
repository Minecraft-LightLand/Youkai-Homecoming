package dev.xkmc.youkaishomecoming.init.data;

import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateItemTagsProvider;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import dev.xkmc.youkaishomecoming.compat.sereneseasons.SeasonCompat;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.food.YHCrops;
import dev.xkmc.youkaishomecoming.init.food.YHTea;
import dev.xkmc.youkaishomecoming.init.registrate.YHDanmaku;
import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
import mezz.jei.api.constants.ModIds;
import mezz.jei.api.constants.Tags;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import sereneseasons.core.SereneSeasons;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class YHTagGen {


	public static final ProviderType<RegistrateTagsProvider.IntrinsicImpl<MobEffect>> EFF_TAGS =
			ProviderType.register("tags/mob_effect",
					type -> (p, e) -> new RegistrateTagsProvider.IntrinsicImpl<>(p, type, "mob_effects",
							e.getGenerator().getPackOutput(), Registries.MOB_EFFECT, e.getLookupProvider(),
							ench -> ResourceKey.create(ForgeRegistries.MOB_EFFECTS.getRegistryKey(),
									ForgeRegistries.MOB_EFFECTS.getKey(ench)),
							e.getExistingFileHelper()));

	public static final TagKey<Item> RAW_EEL = forgeItem("raw_fishes/eel");
	public static final TagKey<Item> COOKED_EEL = forgeItem("cooked_fishes/eel");
	public static final TagKey<Item> RAW_TUNA = forgeItem("raw_fishes/tuna");
	public static final TagKey<Item> COOKED_TUNA = forgeItem("cooked_fishes/tuna");
	public static final TagKey<Item> RAW_VENISON = forgeItem("raw_venison");
	public static final TagKey<Item> COOKED_VENISON = forgeItem("cooked_venison");
	public static final TagKey<Item> RAW_BOAR = forgeItem("raw_boar");
	public static final TagKey<Item> COOKED_BOAR = forgeItem("cooked_boar");
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
	public static final TagKey<Item> SOY_SAUCE_BOTTLE = forgeItem("bottles/soy_sauce");
	public static final TagKey<Item> MAYONNAISE_BOTTLE = forgeItem("bottles/mayonnaise");
	public static final TagKey<Item> SALMON_ROE = forgeItem("roes/salmon");
	public static final TagKey<Item> CRAB_ROE = forgeItem("roes/crab");
	public static final TagKey<Item> NATTOU = forgeItem("nattou");
	public static final TagKey<Item> OTORO = forgeItem("otoro");
	public static final TagKey<Item> SOYBEAN = forgeItem("soybean");
	public static final TagKey<Item> REDBEAN = forgeItem("redbean");
	public static final TagKey<Item> IMITATION_CRAB = forgeItem("imitation_crab");
	public static final TagKey<Item> TAMAGOYAKI_SLICE = forgeItem("slices/tamagoyaki");
	public static final TagKey<Item> GRAPE_SEED = item("grape_seed");
	public static final TagKey<Item> RAW_FLESH = item("raw_flesh");
	public static final TagKey<Item> TAMAGOYAKI = item("tamagoyaki");
	public static final TagKey<Item> COOKED_RICE = item("cuisine/cooked_rice");
	public static final TagKey<Item> DRIED_KELP = item("cuisine/dried_kelp");
	public static final TagKey<Item> BAMBOO = item("cuisine/bamboo");
	public static final TagKey<Item> CARROT = item("cuisine/carrot");
	public static final TagKey<Item> BROWN_MUSHROOM = item("cuisine/brown_mushroom");
	public static final TagKey<Item> DANGO = item("dango");
	public static final TagKey<Item> TEA_DRINK = item("tea");
	public static final TagKey<Item> SAKE = item("sake");
	public static final TagKey<Item> WINE = item("wine");
	public static final TagKey<Item> BOTTLED = item("bottled");
	public static final TagKey<Item> STEAM_BLOCKER = item("steam_blocker");
	public static final TagKey<Item> PLACE_WITH_CONTAINER = item("place_with_container");
	public static final TagKey<Item> IRON_BOWL_FOOD = item("iron_bowl_food");
	public static final TagKey<Item> FLESH_FOOD = item("flesh_food");
	public static final TagKey<Item> APPARENT_FLESH_FOOD = item("apparent_flesh_food");
	public static final TagKey<Item> CUSTOM_SPELL = item("custom_spell");
	public static final TagKey<Item> PRESET_SPELL = item("preset_spell");
	public static final TagKey<Item> DANMAKU = item("danmaku");
	public static final TagKey<Item> LASER = item("laser");
	public static final TagKey<Item> DANMAKU_SHOOTER = item("danmaku_shooter");
	public static final TagKey<Item> FROZEN_FROG = item("frozen_frog");
	public static final TagKey<Block> FARMLAND_SOYBEAN = block("farmland_soybean");
	public static final TagKey<Block> FARMLAND_REDBEAN = block("farmland_redbean");
	public static final TagKey<Block> FARMLAND_TEA = block("farmland_tea");
	public static final TagKey<Block> FARMLAND_COFFEA = block("farmland_coffea");
	public static final TagKey<Block> CRAB_DIGABLE = block("crab_digable");
	public static final TagKey<EntityType<?>> FLESH_SOURCE = entity("flesh_source");

	public static final TagKey<EntityType<?>> SKULL_SOURCE = entity("drops_skeleton_skull");
	public static final TagKey<EntityType<?>> WITHER_SOURCE = entity("drops_wither_skull");
	public static final TagKey<EntityType<?>> ZOMBIE_SOURCE = entity("drops_zombie_head");
	public static final TagKey<EntityType<?>> CREEPER_SOURCE = entity("drops_creeper_head");
	public static final TagKey<EntityType<?>> PIGLIN_SOURCE = entity("drops_piglin_head");

	public static final TagKey<EntityType<?>> YOUKAI_IGNORE = entity("youkai_ignore");
	public static final TagKey<EntityType<?>> BOSS = entity("cannot_capture");

	public static final TagKey<Item> MATCHA = forgeItem("matcha");
	public static final TagKey<Item> ICE = forgeItem("ice_cubes");

	public static final TagKey<Item> TEA_GREEN = forgeItem("tea_leaves/green");
	public static final TagKey<Item> TEA_BLACK = forgeItem("tea_leaves/black");
	public static final TagKey<Item> TEA_WHITE = forgeItem("tea_leaves/white");
	public static final TagKey<Item> TEA_OOLONG = forgeItem("tea_leaves/oolong");
	public static final TagKey<Item> TEA_DARK = forgeItem("tea_leaves/dark");
	public static final TagKey<Item> TEA_YELLOW = forgeItem("tea_leaves/yellow");
	public static final TagKey<Item> TEA = forgeItem("tea_leaves");

	public static final TagKey<Item> TOUHOU_HAT = item("touhou_hat");
	public static final TagKey<Item> TOUHOU_WINGS = item("touhou_wings");

	public static final List<Consumer<RegistrateItemTagsProvider>> OPTIONAL_TAGS = new ArrayList<>();

	public static void onEffectTagGen(RegistrateTagsProvider.IntrinsicImpl<MobEffect> pvd) {
	}

	public static void onEntityTagGen(RegistrateTagsProvider.IntrinsicImpl<EntityType<?>> pvd) {
		pvd.addTag(FLESH_SOURCE).add(EntityType.EVOKER, EntityType.PILLAGER, EntityType.VINDICATOR, EntityType.ILLUSIONER, EntityType.WITCH,
				EntityType.VILLAGER, EntityType.WANDERING_TRADER, EntityType.PLAYER);

		pvd.addTag(SKULL_SOURCE).add(EntityType.SKELETON, EntityType.STRAY);
		pvd.addTag(WITHER_SOURCE).add(EntityType.WITHER_SKELETON, EntityType.WITHER);
		pvd.addTag(ZOMBIE_SOURCE).add(EntityType.ZOMBIE, EntityType.ZOMBIE_VILLAGER, EntityType.HUSK, EntityType.DROWNED);
		pvd.addTag(CREEPER_SOURCE).add(EntityType.CREEPER);
		pvd.addTag(PIGLIN_SOURCE).add(EntityType.PIGLIN, EntityType.PIGLIN_BRUTE);

		pvd.addTag(YOUKAI_IGNORE).add(EntityType.ENDER_DRAGON);

		pvd.addTag(entity("ars_nouveau", "jar_blacklist")).addTag(BOSS);
		pvd.addTag(entity("ars_nouveau", "rewind_blacklist")).addTag(BOSS);
	}

	public static void onBlockTagGen(RegistrateTagsProvider.IntrinsicImpl<Block> pvd) {
		pvd.addTag(FARMLAND_SOYBEAN).add(Blocks.FARMLAND, ModBlocks.RICH_SOIL_FARMLAND.get());
		pvd.addTag(FARMLAND_REDBEAN).add(Blocks.CLAY, Blocks.MUD, Blocks.COARSE_DIRT, ModBlocks.RICH_SOIL_FARMLAND.get());
		pvd.addTag(FARMLAND_TEA).add(Blocks.GRASS_BLOCK, ModBlocks.RICH_SOIL.get());
		pvd.addTag(FARMLAND_COFFEA).add(Blocks.PODZOL, Blocks.MUD, Blocks.SOUL_SOIL);
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
		pvd.addTag(SOY_SAUCE_BOTTLE).add(YHItems.SOY_SAUCE_BOTTLE.asItem());
		pvd.addTag(MAYONNAISE_BOTTLE).add(YHItems.MAYONNAISE.asItem());
		pvd.addTag(SOYBEAN).add(YHCrops.SOYBEAN.getSeed());
		pvd.addTag(REDBEAN).add(YHCrops.REDBEAN.getSeed());

		pvd.addTag(COOKED_RICE).add(ModItems.COOKED_RICE.get());
		pvd.addTag(DRIED_KELP).add(Items.DRIED_KELP);
		pvd.addTag(BAMBOO).add(Items.BAMBOO);
		pvd.addTag(CARROT).add(Items.CARROT);
		pvd.addTag(BROWN_MUSHROOM).add(Items.BROWN_MUSHROOM);

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
		var danmaku = pvd.addTag(DANMAKU);
		for (var e : YHDanmaku.Bullet.values()) {
			danmaku.addTag(e.tag);
		}
		var laser = pvd.addTag(LASER);
		for (var e : YHDanmaku.Laser.values()) {
			laser.addTag(e.tag);
		}
		pvd.addTag(DANMAKU_SHOOTER).addTags(DANMAKU, LASER, CUSTOM_SPELL, PRESET_SPELL);
		pvd.addTag(ItemTags.create(Tags.HIDDEN_FROM_RECIPE_VIEWERS))
				.addTags(DANMAKU, LASER);
		if (ModList.get().isLoaded(SereneSeasons.MOD_ID)) {
			SeasonCompat.genItem(pvd);
		}
		for (var e : OPTIONAL_TAGS) {
			e.accept(pvd);
		}
		pvd.addTag(FROZEN_FROG).add(YHItems.FROZEN_FROG_COLD.get(), YHItems.FROZEN_FROG_WARM.get(), YHItems.FROZEN_FROG_TEMPERATE.get());

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
