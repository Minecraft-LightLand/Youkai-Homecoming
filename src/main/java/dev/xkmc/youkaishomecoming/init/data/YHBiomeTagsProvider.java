package dev.xkmc.youkaishomecoming.init.data;

import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public final class YHBiomeTagsProvider   {

	public static final ProviderType<RegistrateTagsProvider.Impl<Biome>> TYPE = ProviderType.registerDynamicTag("tags/biome", "biomes", Registries.BIOME);

	public static final TagKey<Biome> CRAB_FISHING = asTag("crab_fishing");
	public static final TagKey<Biome> CHERRY_BIOMES = asTag("cherry_biomes");
	public static final TagKey<Biome> YH_BIOMES = asTag("youkai_biomes");
	public static final TagKey<Biome> LAMPREY = asTag("spawns/lamprey");
	public static final TagKey<Biome> TUNA = asTag("spawns/tuna");
	public static final TagKey<Biome> DEER = asTag("spawns/deer");
	public static final TagKey<Biome> BOAR = asTag("spawns/boar");
	public static final TagKey<Biome> CRAB = asTag("spawns/crab");
	public static final TagKey<Biome> CRAB_MUD = asTag("spawns/crab_mud");
	public static final TagKey<Biome> SOYBEAN = asTag("spawns/soybean");
	public static final TagKey<Biome> REDBEAN = asTag("spawns/redbean");
	public static final TagKey<Biome> TEA = asTag("spawns/tea");
	public static final TagKey<Biome> UDUMBARA = asTag("spawns/udumbara");
	public static final TagKey<Biome> CUCUMBER = asTag("spawns/cucumber");
	public static final TagKey<Biome> GRAPE = asTag("spawns/grape");
	public static final TagKey<Biome> NO_GRAPE = asTag("spawns/no_grape");
	public static final TagKey<Biome> BLACK_GRAPE = asTag("spawns/black_grape");
	public static final TagKey<Biome> WHITE_GRAPE = asTag("spawns/white_grape");

	public static void genTag(RegistrateTagsProvider<Biome> pvd) {
		pvd.addTag(CHERRY_BIOMES).add(YHBiomes.SAKURA_FOREST, YHBiomes.SAKURA_TAIGA, YHBiomes.SAKURA_BIRCH_FOREST);
		pvd.addTag(YH_BIOMES).add(YHBiomes.SAKURA_FOREST, YHBiomes.SAKURA_TAIGA, YHBiomes.SAKURA_BIRCH_FOREST, YHBiomes.ANIMAL_PATH);
		pvd.addTag(Tags.Biomes.IS_MAGICAL);
		pvd.addTag(LAMPREY).add(Biomes.RIVER, Biomes.FROZEN_RIVER).addTag(BiomeTags.IS_OCEAN);
		pvd.addTag(TUNA).addTag(BiomeTags.IS_DEEP_OCEAN);
		pvd.addTag(DEER).addTag(YH_BIOMES);
		pvd.addTag(BOAR).addTag(YH_BIOMES);
		pvd.addTag(CRAB).addTags(BiomeTags.IS_RIVER, BiomeTags.IS_BEACH, Tags.Biomes.IS_SWAMP);
		pvd.addTag(CRAB_MUD).addTags(BiomeTags.IS_RIVER, Tags.Biomes.IS_SWAMP);
		pvd.addTag(CRAB_FISHING).addTags(BiomeTags.IS_OCEAN, CRAB);
		pvd.addTag(SOYBEAN).add(Biomes.DARK_FOREST).addTags(BiomeTags.IS_JUNGLE, Tags.Biomes.IS_SWAMP, YH_BIOMES);
		pvd.addTag(REDBEAN).add(Biomes.SUNFLOWER_PLAINS, Biomes.BAMBOO_JUNGLE, Biomes.OLD_GROWTH_BIRCH_FOREST, Biomes.BIRCH_FOREST,
				YHBiomes.SAKURA_BIRCH_FOREST, YHBiomes.ANIMAL_PATH);
		pvd.addTag(TEA).add(Biomes.FLOWER_FOREST, Biomes.MEADOW, Biomes.CHERRY_GROVE, Biomes.GROVE).addTag(BiomeTags.IS_MOUNTAIN);
		pvd.addTag(UDUMBARA).addTag(Tags.Biomes.IS_SWAMP);
		pvd.addTag(CUCUMBER).addTag(Tags.Biomes.IS_CONIFEROUS_TREE).add(YHBiomes.SAKURA_TAIGA);
		pvd.addTag(GRAPE).addTags(BiomeTags.IS_FOREST, Tags.Biomes.IS_CONIFEROUS_TREE, BiomeTags.IS_TAIGA, YH_BIOMES);
		pvd.addTag(NO_GRAPE).addTags(BiomeTags.IS_JUNGLE, Tags.Biomes.IS_SWAMP, Tags.Biomes.IS_MAGICAL);
		pvd.addTag(BLACK_GRAPE).addTags(Tags.Biomes.IS_CONIFEROUS_TREE, BiomeTags.IS_TAIGA).add(YHBiomes.SAKURA_TAIGA);
		pvd.addTag(WHITE_GRAPE).add(Biomes.OLD_GROWTH_BIRCH_FOREST, Biomes.BIRCH_FOREST, Biomes.FLOWER_FOREST, YHBiomes.SAKURA_BIRCH_FOREST);

		pvd.addTag(BiomeTags.IS_FOREST).addTag(CHERRY_BIOMES);
	}

	public static TagKey<Biome> asTag(String name) {
		return TagKey.create(Registries.BIOME, YoukaisHomecoming.loc(name));
	}

	public static TagKey<Biome> cTag(String name) {
		return TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath("c", name));
	}

}