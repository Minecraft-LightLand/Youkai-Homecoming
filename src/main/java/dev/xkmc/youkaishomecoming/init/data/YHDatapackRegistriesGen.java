package dev.xkmc.youkaishomecoming.init.data;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.food.YHCrops;
import dev.xkmc.youkaishomecoming.init.loot.YHLootGen;
import dev.xkmc.youkaishomecoming.init.registrate.YHEntities;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.heightproviders.ConstantHeight;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.StructureSpawnOverride;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;
import net.minecraft.world.level.levelgen.structure.templatesystem.rule.blockentity.AppendLoot;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.holdersets.AndHolderSet;
import org.jetbrains.annotations.NotNull;
import vectorwing.farmersdelight.common.registry.ModBlocks;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class YHDatapackRegistriesGen extends DatapackBuiltinEntriesProvider {

	private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
			.add(Registries.BIOME, YHBiomes::registerBiomes)
			.add(Registries.CONFIGURED_FEATURE, ctx -> {
				for (var e : YHCrops.values()) {
					e.registerConfigs(ctx);
				}
			})
			.add(Registries.PLACED_FEATURE, ctx -> {
				for (var e : YHCrops.values()) {
					e.registerPlacements(ctx);
				}
				YHBiomes.registerPlacements(ctx);
			})
			.add(ForgeRegistries.Keys.BIOME_MODIFIERS, YHDatapackRegistriesGen::registerBiomeModifiers);

	private static void registerBiomeModifiers(BootstapContext<BiomeModifier> ctx) {
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

	private static void registerCropBiome(BootstapContext<BiomeModifier> ctx,
										  YHCrops tree, HolderSet<Biome> set,
										  HolderGetter<PlacedFeature> features) {
		ctx.register(ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, YoukaisHomecoming.loc(tree.getName())),
				new ForgeBiomeModifiers.AddFeaturesBiomeModifier(set,
						HolderSet.direct(features.getOrThrow(tree.getPlacementKey())),
						GenerationStep.Decoration.VEGETAL_DECORATION));
	}

	private static void registerMobSpawn(
			BootstapContext<BiomeModifier> ctx, ResourceLocation rl,
			TagKey<Biome> tag, HolderGetter<Biome> biomes,
			MobSpawnSettings.SpawnerData entity) {
		ctx.register(ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, rl),
				new ForgeBiomeModifiers.AddSpawnsBiomeModifier(biomes.getOrThrow(tag), List.of(entity)));
	}

	public YHDatapackRegistriesGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries, BUILDER, Set.of("minecraft", YoukaisHomecoming.MODID));
	}

	@NotNull
	public String getName() {
		return "Youkai's Homecoming Data";
	}

	private static ProcessorRule injectData(Block block, ResourceLocation table) {
		return new ProcessorRule(new BlockMatchTest(block), AlwaysTrueTest.INSTANCE, PosAlwaysTrueTest.INSTANCE,
				block.defaultBlockState(), new AppendLoot(table));
	}

	private static ProcessorRule injectData(Block block, Direction dir, ResourceLocation table) {
		var state = block.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, dir);
		return new ProcessorRule(new BlockStateMatchTest(state), AlwaysTrueTest.INSTANCE, PosAlwaysTrueTest.INSTANCE,
				state, new AppendLoot(table));
	}

	private static class SinglePiece extends SinglePoolElement {

		protected SinglePiece(ResourceLocation template, Holder<StructureProcessorList> list, StructureTemplatePool.Projection proj) {
			super(Either.left(template), list, proj);
		}

	}

}
