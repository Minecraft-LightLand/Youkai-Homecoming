package dev.xkmc.youkaihomecoming.init.data;

import dev.xkmc.youkaihomecoming.init.YoukaiHomecoming;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public final class YHBiomeTagsProvider extends BiomeTagsProvider {

	public static final TagKey<Biome> LAMPREY = asTag("lamprey_spawns");

	public YHBiomeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> pvd, ExistingFileHelper helper) {
		super(output, pvd, YoukaiHomecoming.MODID, helper);
	}

	@Override
	protected void addTags(HolderLookup.Provider pvd) {
		tag(LAMPREY).add(Biomes.RIVER, Biomes.FROZEN_RIVER,
				Biomes.OCEAN, Biomes.COLD_OCEAN, Biomes.FROZEN_OCEAN,
				Biomes.DEEP_OCEAN, Biomes.DEEP_COLD_OCEAN, Biomes.DEEP_FROZEN_OCEAN);
	}

	public static TagKey<Biome> asTag(String name) {
		return TagKey.create(Registries.BIOME, new ResourceLocation(YoukaiHomecoming.MODID, name));
	}

}