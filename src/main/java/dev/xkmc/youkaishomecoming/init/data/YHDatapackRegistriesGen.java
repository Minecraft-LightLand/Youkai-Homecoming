package dev.xkmc.youkaishomecoming.init.data;

import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.food.YHCrops;
import dev.xkmc.youkaishomecoming.init.registrate.YHEntities;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;
import net.minecraft.world.level.levelgen.structure.templatesystem.rule.blockentity.AppendLoot;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.holdersets.AndHolderSet;

import java.util.List;

public class YHDatapackRegistriesGen {

	public static void register() {
		var init = YoukaisHomecoming.REGISTRATE.getDataGenInitializer();
		init.add(Registries.BIOME, YHBiomes::registerBiomes);
		init.add(Registries.CONFIGURED_FEATURE, ctx -> {
			for (var e : YHCrops.values()) {
				e.registerConfigs(ctx);
			}
		});
		init.add(Registries.PLACED_FEATURE, ctx -> {
			for (var e : YHCrops.values()) {
				e.registerPlacements(ctx);
			}
			YHBiomes.registerPlacements(ctx);
		});
		init.add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, YHDatapackRegistriesGen::registerBiomeModifiers);
	}

	private static void registerBiomeModifiers(BootstrapContext<BiomeModifier> ctx) {
		var biomes = ctx.lookup(Registries.BIOME);
		var features = ctx.lookup(Registries.PLACED_FEATURE);
		registerMobSpawn(ctx, YoukaisHomecoming.loc("lamprey"), YHBiomeTagsProvider.LAMPREY, biomes,
				new MobSpawnSettings.SpawnerData(YHEntities.LAMPREY.get(), 5, 3, 5));
		registerMobSpawn(ctx, YoukaisHomecoming.loc("tuna"), YHBiomeTagsProvider.TUNA, biomes,
				new MobSpawnSettings.SpawnerData(YHEntities.TUNA.get(), 10, 1, 1));
		registerMobSpawn(ctx, YoukaisHomecoming.loc("crab"), YHBiomeTagsProvider.CRAB, biomes,
				new MobSpawnSettings.SpawnerData(YHEntities.CRAB.get(), 10, 1, 2));
		registerMobSpawn(ctx, YoukaisHomecoming.loc("deer"), YHBiomeTagsProvider.DEER, biomes,
				new MobSpawnSettings.SpawnerData(YHEntities.DEER.get(), 20, 3, 5));
		registerMobSpawn(ctx, YoukaisHomecoming.loc("boar"), YHBiomeTagsProvider.BOAR, biomes,
				new MobSpawnSettings.SpawnerData(YHEntities.BOAR.get(), 20, 2, 3));
		registerCropBiome(ctx, YHCrops.SOYBEAN, biomes.getOrThrow(YHBiomeTagsProvider.SOYBEAN), features);
		registerCropBiome(ctx, YHCrops.REDBEAN, biomes.getOrThrow(YHBiomeTagsProvider.REDBEAN), features);
		registerCropBiome(ctx, YHCrops.TEA, biomes.getOrThrow(YHBiomeTagsProvider.TEA), features);
		registerCropBiome(ctx, YHCrops.UDUMBARA, biomes.getOrThrow(YHBiomeTagsProvider.UDUMBARA), features);
		registerCropBiome(ctx, YHCrops.CUCUMBER, biomes.getOrThrow(YHBiomeTagsProvider.CUCUMBER), features);
		{
			var grape = biomes.getOrThrow(YHBiomeTagsProvider.GRAPE);
			var noGrape = biomes.getOrThrow(YHBiomeTagsProvider.NO_GRAPE);
			var black = biomes.getOrThrow(YHBiomeTagsProvider.BLACK_GRAPE);
			var white = biomes.getOrThrow(YHBiomeTagsProvider.WHITE_GRAPE);
			var holderWhite = new FilterHolderSet<>(List.of(new AndHolderSet<>(List.of(grape, white)), noGrape));
			var holderBlack = new FilterHolderSet<>(List.of(new AndHolderSet<>(List.of(grape, black)), noGrape));
			var holderRed = new FilterHolderSet<>(List.of(grape, noGrape, black, white));
			registerCropBiome(ctx, YHCrops.RED_GRAPE, holderRed, features);
			registerCropBiome(ctx, YHCrops.BLACK_GRAPE, holderBlack, features);
			registerCropBiome(ctx, YHCrops.WHITE_GRAPE, holderWhite, features);
		}
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

	private static ProcessorRule injectData(Block block, ResourceLocation table) {
		return new ProcessorRule(new BlockMatchTest(block), AlwaysTrueTest.INSTANCE, PosAlwaysTrueTest.INSTANCE,
				block.defaultBlockState(), new AppendLoot(ResourceKey.create(Registries.LOOT_TABLE, table)));
	}

	private static ProcessorRule injectData(Block block, Direction dir, ResourceLocation table) {
		var state = block.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, dir);
		return new ProcessorRule(new BlockStateMatchTest(state), AlwaysTrueTest.INSTANCE, PosAlwaysTrueTest.INSTANCE,
				state, new AppendLoot(ResourceKey.create(Registries.LOOT_TABLE, table)));
	}

}
