package dev.xkmc.youkaishomecoming.init.data;

import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.food.YHCrops;
import dev.xkmc.youkaishomecoming.init.registrate.YHEntities;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.List;

public class YHDynamicGen {

	public static void registerBiomeModifiers(BootstrapContext<BiomeModifier> ctx) {
		var biomes = ctx.lookup(Registries.BIOME);
		var features = ctx.lookup(Registries.PLACED_FEATURE);
		registerMobSpawn(ctx, YoukaisHomecoming.loc("lamprey"), YHBiomeTagsProvider.LAMPREY, biomes,
				new MobSpawnSettings.SpawnerData(YHEntities.LAMPREY.get(), 5, 3, 5));
		//TODO registerMobSpawn(ctx, YoukaisHomecoming.loc("cirno"), YHBiomeTagsProvider.CIRNO, biomes, new MobSpawnSettings.SpawnerData(YHEntities.CIRNO.get(), 5, 1, 1));
		registerCropBiome(ctx, YHCrops.SOYBEAN, biomes.getOrThrow(YHBiomeTagsProvider.SOYBEAN), features);
		registerCropBiome(ctx, YHCrops.REDBEAN, biomes.getOrThrow(YHBiomeTagsProvider.REDBEAN), features);
		registerCropBiome(ctx, YHCrops.COFFEA, biomes.getOrThrow(YHBiomeTagsProvider.COFFEA), features);
		registerCropBiome(ctx, YHCrops.TEA, biomes.getOrThrow(YHBiomeTagsProvider.TEA), features);
		registerCropBiome(ctx, YHCrops.MANDRAKE, biomes.getOrThrow(YHBiomeTagsProvider.MANDRAKE), features);
		registerCropBiome(ctx, YHCrops.UDUMBARA, biomes.getOrThrow(YHBiomeTagsProvider.UDUMBARA), features);
	}


	private static void registerCropBiome(BootstrapContext<BiomeModifier> ctx,
										  YHCrops tree, HolderSet<Biome> set,
										  HolderGetter<PlacedFeature> features) {
		ctx.register(ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, YoukaisHomecoming.loc(tree.getName())),
				new BiomeModifiers.AddFeaturesBiomeModifier(set,
						HolderSet.direct(features.getOrThrow(tree.getPlacementKey())),
						GenerationStep.Decoration.VEGETAL_DECORATION));
	}

	private static void registerMobSpawn(
			BootstrapContext<BiomeModifier> ctx, ResourceLocation rl,
			TagKey<Biome> tag, HolderGetter<Biome> biomes,
			MobSpawnSettings.SpawnerData entity) {
		ctx.register(ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, rl),
				new BiomeModifiers.AddSpawnsBiomeModifier(biomes.getOrThrow(tag), List.of(entity)));
	}


}
