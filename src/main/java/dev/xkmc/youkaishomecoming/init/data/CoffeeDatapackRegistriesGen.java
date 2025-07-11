package dev.xkmc.youkaishomecoming.init.data;

import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.food.CoffeeCrops;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class CoffeeDatapackRegistriesGen extends DatapackBuiltinEntriesProvider {

	private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
			.add(Registries.CONFIGURED_FEATURE, ctx -> {
				for (var e : CoffeeCrops.values()) {
					e.registerConfigs(ctx);
				}
			})
			.add(Registries.PLACED_FEATURE, ctx -> {
				for (var e : CoffeeCrops.values()) {
					e.registerPlacements(ctx);
				}
			})
			.add(ForgeRegistries.Keys.BIOME_MODIFIERS, CoffeeDatapackRegistriesGen::registerBiomeModifiers);

	private static void registerBiomeModifiers(BootstapContext<BiomeModifier> ctx) {
		var biomes = ctx.lookup(Registries.BIOME);
		var features = ctx.lookup(Registries.PLACED_FEATURE);
		registerCropBiome(ctx, CoffeeCrops.COFFEA, biomes.getOrThrow(CoffeeBiomeTagsProvider.COFFEA), features);
	}

	private static void registerCropBiome(BootstapContext<BiomeModifier> ctx,
										  CoffeeCrops tree, HolderSet<Biome> set,
										  HolderGetter<PlacedFeature> features) {
		ctx.register(ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, YoukaisHomecoming.loc(tree.getName())),
				new ForgeBiomeModifiers.AddFeaturesBiomeModifier(set,
						HolderSet.direct(features.getOrThrow(tree.getPlacementKey())),
						GenerationStep.Decoration.VEGETAL_DECORATION));
	}

	public CoffeeDatapackRegistriesGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries, BUILDER, Set.of("minecraft", YoukaisHomecoming.MODID));
	}

	@NotNull
	public String getName() {
		return "Youkai's Homecoming Data";
	}

}
