package dev.xkmc.youkaishomecoming.init.data;

import dev.xkmc.youkaishomecoming.content.world.NoisePlacement;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.data.worldgen.features.VegetationFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.Musics;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.SurfaceWaterDepthFilter;

import javax.annotation.Nullable;
import java.util.List;

public class YHBiomes {

	public static final ResourceKey<Biome> SAKURA_FOREST = biome("sakura_forest");
	public static final ResourceKey<Biome> SAKURA_TAIGA = biome("sakura_taiga");
	public static final ResourceKey<Biome> SAKURA_BIRCH_FOREST = biome("sakura_birch_forest");
	public static final ResourceKey<Biome> ANIMAL_PATH = biome("animal_path");

	private static final ResourceKey<PlacedFeature> SAKURA_FOREST_CHERRY = place("sakura_forest_cherry");
	private static final ResourceKey<PlacedFeature> SAKURA_FOREST_OAK = place("sakura_forest_oak");
	private static final ResourceKey<PlacedFeature> SAKURA_FOREST_BIRCH = place("sakura_forest_birch");
	private static final ResourceKey<PlacedFeature> SAKURA_FOREST_SPRUCE = place("sakura_forest_spruce");
	private static final ResourceKey<PlacedFeature> SAKURA_FOREST_PINE = place("sakura_forest_pine");
	private static final ResourceKey<PlacedFeature> PATH_OAK = place("animal_path_oak");

	private static ResourceKey<Biome> biome(String id) {
		return ResourceKey.create(Registries.BIOME, YoukaisHomecoming.loc(id));
	}

	private static ResourceKey<PlacedFeature> place(String id) {
		return ResourceKey.create(Registries.PLACED_FEATURE, YoukaisHomecoming.loc(id));
	}

	public static void registerPlacements(BootstapContext<PlacedFeature> ctx) {
		var freg = ctx.lookup(Registries.CONFIGURED_FEATURE);
		PlacementUtils.register(ctx, SAKURA_FOREST_CHERRY, freg.getOrThrow(TreeFeatures.CHERRY_BEES_005),
				VegetationPlacements.treePlacement(PlacementUtils.countExtra(1, 0.5f, 1), Blocks.CHERRY_SAPLING));
		PlacementUtils.register(ctx, SAKURA_FOREST_OAK, freg.getOrThrow(VegetationFeatures.TREES_FLOWER_FOREST),
				VegetationPlacements.treePlacement(PlacementUtils.countExtra(3, 0.5F, 1)));
		PlacementUtils.register(ctx, SAKURA_FOREST_BIRCH, freg.getOrThrow(VegetationFeatures.BIRCH_TALL),
				VegetationPlacements.treePlacement(PlacementUtils.countExtra(3, 0.5F, 1)));
		PlacementUtils.register(ctx, SAKURA_FOREST_SPRUCE, freg.getOrThrow(VegetationFeatures.TREES_OLD_GROWTH_SPRUCE_TAIGA),
				VegetationPlacements.treePlacement(PlacementUtils.countExtra(2, 0.5F, 1)));
		PlacementUtils.register(ctx, SAKURA_FOREST_PINE, freg.getOrThrow(VegetationFeatures.TREES_OLD_GROWTH_PINE_TAIGA),
				VegetationPlacements.treePlacement(PlacementUtils.countExtra(2, 0.5F, 1)));
		PlacementUtils.register(ctx, PATH_OAK, freg.getOrThrow(VegetationFeatures.TREES_FLOWER_FOREST),
				List.of(NoisePlacement.of(YoukaisHomecoming.loc("path"), 0, 0.1, 0.5, 3, 10),
						SurfaceWaterDepthFilter.forMaxDepth(0), PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, BiomeFilter.biome()));
	}

	public static void registerBiomes(BootstapContext<Biome> ctx) {
		var pfreg = ctx.lookup(Registries.PLACED_FEATURE);
		var cwreg = ctx.lookup(Registries.CONFIGURED_CARVER);
		ctx.register(SAKURA_FOREST, forest(pfreg, cwreg, SAKURA_FOREST_CHERRY, SAKURA_FOREST_OAK));
		ctx.register(SAKURA_BIRCH_FOREST, forest(pfreg, cwreg, SAKURA_FOREST_CHERRY, SAKURA_FOREST_BIRCH));
		ctx.register(SAKURA_TAIGA, forest(pfreg, cwreg, SAKURA_FOREST_PINE, SAKURA_FOREST_SPRUCE));
		ctx.register(ANIMAL_PATH, path(pfreg, cwreg, PATH_OAK));
	}


	@SafeVarargs
	public static Biome path(HolderGetter<PlacedFeature> pfreg, HolderGetter<ConfiguredWorldCarver<?>> cwreg, ResourceKey<PlacedFeature>... trees) {
		BiomeGenerationSettings.Builder gen = new BiomeGenerationSettings.Builder(pfreg, cwreg);
		globalOverworldGeneration(gen);
		Music music = Musics.createGameMusic(SoundEvents.MUSIC_BIOME_FLOWER_FOREST);
		BiomeDefaultFeatures.addDefaultOres(gen);
		BiomeDefaultFeatures.addDefaultSoftDisks(gen);
		for (var tree : trees)
			gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, tree);
		gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.FLOWER_FOREST_FLOWERS);
		gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.FLOWER_FLOWER_FOREST);
		gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_PLAIN);

		BiomeDefaultFeatures.addDefaultMushrooms(gen);
		BiomeDefaultFeatures.addDefaultExtraVegetation(gen);
		MobSpawnSettings.Builder mob = new MobSpawnSettings.Builder();
		BiomeDefaultFeatures.commonSpawns(mob);
		return biome(0.7f, 0.8F, mob, gen, music);
	}

	@SafeVarargs
	public static Biome forest(HolderGetter<PlacedFeature> pfreg, HolderGetter<ConfiguredWorldCarver<?>> cwreg, ResourceKey<PlacedFeature>... trees) {
		BiomeGenerationSettings.Builder gen = new BiomeGenerationSettings.Builder(pfreg, cwreg);
		globalOverworldGeneration(gen);
		Music music = Musics.createGameMusic(SoundEvents.MUSIC_BIOME_FLOWER_FOREST);
		BiomeDefaultFeatures.addDefaultOres(gen);
		BiomeDefaultFeatures.addDefaultSoftDisks(gen);
		for (var tree : trees)
			gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, tree);
		gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.FLOWER_FOREST_FLOWERS);
		gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.FLOWER_FLOWER_FOREST);
		gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_PLAIN);
		gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.FLOWER_CHERRY);

		BiomeDefaultFeatures.addDefaultMushrooms(gen);
		BiomeDefaultFeatures.addDefaultExtraVegetation(gen);
		MobSpawnSettings.Builder mob = new MobSpawnSettings.Builder();
		BiomeDefaultFeatures.commonSpawns(mob);
		return biome(0.7f, 0.8F, mob, gen, music);
	}

	private static Biome biome(float temp, float rain, MobSpawnSettings.Builder mob, BiomeGenerationSettings.Builder gen, @Nullable Music bgm) {
		BiomeSpecialEffects.Builder effs = new BiomeSpecialEffects.Builder()
				.waterColor(4159204).waterFogColor(329011)
				.fogColor(12638463).skyColor(calculateSkyColor(temp))
				.ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
				.backgroundMusic(bgm);
		return new Biome.BiomeBuilder().hasPrecipitation(true).temperature(temp).downfall(rain)
				.specialEffects(effs.build()).mobSpawnSettings(mob.build()).generationSettings(gen.build()).build();
	}

	private static int calculateSkyColor(float p_194844_) {
		float $$1 = p_194844_ / 3.0F;
		$$1 = Mth.clamp($$1, -1.0F, 1.0F);
		return Mth.hsvToRgb(0.62222224F - $$1 * 0.05F, 0.5F + $$1 * 0.1F, 1.0F);
	}

	private static void globalOverworldGeneration(BiomeGenerationSettings.Builder builder) {
		BiomeDefaultFeatures.addDefaultCarversAndLakes(builder);
		BiomeDefaultFeatures.addDefaultCrystalFormations(builder);
		BiomeDefaultFeatures.addDefaultMonsterRoom(builder);
		BiomeDefaultFeatures.addDefaultUndergroundVariety(builder);
		BiomeDefaultFeatures.addDefaultSprings(builder);
		BiomeDefaultFeatures.addSurfaceFreezing(builder);
	}

}
