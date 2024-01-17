package dev.xkmc.youkaihomecoming.init.data;

import dev.xkmc.youkaihomecoming.init.YoukaiHomecoming;
import dev.xkmc.youkaihomecoming.init.food.YHCrops;
import dev.xkmc.youkaihomecoming.init.registrate.YHEntities;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class YHDatapackRegistriesGen extends DatapackBuiltinEntriesProvider {

	private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
			.add(Registries.CONFIGURED_FEATURE, ctx -> {
				YHCrops.SOYBEAN.registerConfigs(ctx);
				YHCrops.REDBEAN.registerConfigs(ctx);
			})
			.add(Registries.PLACED_FEATURE, ctx -> {
				YHCrops.SOYBEAN.registerPlacements(ctx);
				YHCrops.REDBEAN.registerPlacements(ctx);
			})
			.add(ForgeRegistries.Keys.BIOME_MODIFIERS, YHDatapackRegistriesGen::registerBiomeModifiers);

	private static void registerBiomeModifiers(BootstapContext<BiomeModifier> ctx) {
		var biomes = ctx.lookup(Registries.BIOME);
		var features = ctx.lookup(Registries.PLACED_FEATURE);
		HolderSet<Biome> set = biomes.getOrThrow(YHBiomeTagsProvider.LAMPREY);
		ctx.register(ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, new ResourceLocation(YoukaiHomecoming.MODID, "lamprey")),
				new ForgeBiomeModifiers.AddSpawnsBiomeModifier(set, List.of(new MobSpawnSettings.SpawnerData(
						YHEntities.LAMPREY.get(), 12, 3, 5)
				)));
		registerTreeBiome(ctx, YHCrops.SOYBEAN, biomes.getOrThrow(YHBiomeTagsProvider.SOYBEAN), features);
		registerTreeBiome(ctx, YHCrops.REDBEAN, biomes.getOrThrow(YHBiomeTagsProvider.REDBEAN), features);
	}


	private static void registerTreeBiome(BootstapContext<BiomeModifier> ctx,
										  YHCrops tree,
										  HolderSet<Biome> set,
										  HolderGetter<PlacedFeature> features) {
		ctx.register(ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, new ResourceLocation(YoukaiHomecoming.MODID,
				tree.getName())), new ForgeBiomeModifiers.AddFeaturesBiomeModifier(set,
				HolderSet.direct(features.getOrThrow(tree.getPlacementKey())),
				GenerationStep.Decoration.VEGETAL_DECORATION));
	}

	public YHDatapackRegistriesGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries, BUILDER, Set.of("minecraft", YoukaiHomecoming.MODID));
	}

	@NotNull
	public String getName() {
		return "Youkai's Homecoming Data";
	}

}
