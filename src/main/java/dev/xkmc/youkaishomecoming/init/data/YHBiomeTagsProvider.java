package dev.xkmc.youkaishomecoming.init.data;

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
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public final class YHBiomeTagsProvider extends BiomeTagsProvider {

	public static final TagKey<Biome> LAMPREY = asTag("spawns/lamprey");
	public static final TagKey<Biome> TUNA = asTag("spawns/tuna");
	public static final TagKey<Biome> SOYBEAN = asTag("spawns/soybean");
	public static final TagKey<Biome> REDBEAN = asTag("spawns/redbean");
	public static final TagKey<Biome> COFFEA = asTag("spawns/coffea");
	public static final TagKey<Biome> TEA = asTag("spawns/tea");
	public static final TagKey<Biome> MANDRAKE = asTag("spawns/mandrake");
	public static final TagKey<Biome> UDUMBARA = asTag("spawns/udumbara");
	public static final TagKey<Biome> CUCUMBER = asTag("spawns/cucumber");
	public static final TagKey<Biome> GRAPE = asTag("spawns/grape");
	public static final TagKey<Biome> NO_GRAPE = asTag("spawns/no_grape");
	public static final TagKey<Biome> BLACK_GRAPE = asTag("spawns/black_grape");
	public static final TagKey<Biome> WHITE_GRAPE = asTag("spawns/white_grape");
	public static final TagKey<Biome> HAS_RUMIA_NEST = asTag("has_structure/youkai_nest");
	public static final TagKey<Biome> HAS_CIRNO_NEST = asTag("has_structure/cirno_nest");
	public static final TagKey<Biome> HAS_SHRINE = asTag("has_structure/hakurei_shrine");

	public YHBiomeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> pvd, ExistingFileHelper helper) {
		super(output, pvd, YoukaisHomecoming.MODID, helper);
	}

	@Override
	protected void addTags(HolderLookup.Provider pvd) {
		tag(Tags.Biomes.IS_MAGICAL);
		tag(LAMPREY).add(Biomes.RIVER, Biomes.FROZEN_RIVER).addTag(BiomeTags.IS_OCEAN);
		tag(TUNA).addTag(BiomeTags.IS_DEEP_OCEAN);
		tag(SOYBEAN).add(Biomes.DARK_FOREST).addTags(BiomeTags.IS_JUNGLE, Tags.Biomes.IS_SWAMP);
		tag(REDBEAN).add(Biomes.SUNFLOWER_PLAINS, Biomes.BAMBOO_JUNGLE, Biomes.OLD_GROWTH_BIRCH_FOREST, Biomes.BIRCH_FOREST);
		tag(COFFEA).addTag(BiomeTags.IS_JUNGLE);
		tag(TEA).add(Biomes.FLOWER_FOREST, Biomes.MEADOW, Biomes.CHERRY_GROVE, Biomes.GROVE).addTag(BiomeTags.IS_MOUNTAIN);
		tag(MANDRAKE).add(Biomes.DARK_FOREST).addTag(Tags.Biomes.IS_MAGICAL);
		tag(UDUMBARA).addTag(Tags.Biomes.IS_SWAMP);
		tag(CUCUMBER).addTag(Tags.Biomes.IS_CONIFEROUS);
		tag(GRAPE).addTags(BiomeTags.IS_FOREST, Tags.Biomes.IS_CONIFEROUS, BiomeTags.IS_TAIGA);
		tag(NO_GRAPE).addTags(BiomeTags.IS_JUNGLE, Tags.Biomes.IS_SWAMP, Tags.Biomes.IS_MAGICAL);
		tag(BLACK_GRAPE).addTags(Tags.Biomes.IS_CONIFEROUS, BiomeTags.IS_TAIGA);
		tag(WHITE_GRAPE).add(Biomes.OLD_GROWTH_BIRCH_FOREST, Biomes.BIRCH_FOREST, Biomes.FLOWER_FOREST);
		tag(HAS_RUMIA_NEST).addTag(Tags.Biomes.IS_PLAINS).addTag(BiomeTags.IS_FOREST);
		tag(HAS_CIRNO_NEST).addTag(Tags.Biomes.IS_SNOWY)
				.addOptionalTag(new ResourceLocation("c:is_snowy_plains"));
		tag(HAS_SHRINE).add(Biomes.CHERRY_GROVE).addOptional(new ResourceLocation("wythers:sakura_forest"))
				.addOptionalTag(new ResourceLocation("c:is_cherry_forest"))
				.addOptionalTag(new ResourceLocation("c:is_sakura_forest"));
	}

	public static TagKey<Biome> asTag(String name) {
		return TagKey.create(Registries.BIOME, YoukaisHomecoming.loc(name));
	}

}