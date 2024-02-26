package dev.xkmc.youkaihomecoming.init.data;

import dev.xkmc.youkaihomecoming.init.YoukaiHomecoming;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public final class YHBiomeTagsProvider extends BiomeTagsProvider {

	public static final TagKey<Biome> LAMPREY = asTag("lamprey_spawns");
	public static final TagKey<Biome> SOYBEAN = asTag("soybean_spawns");
	public static final TagKey<Biome> REDBEAN = asTag("redbean_spawns");
	public static final TagKey<Biome> COFFEA = asTag("coffea_spawns");
	public static final TagKey<Biome> TEA = asTag("tea_spawns");
	public static final TagKey<Biome> HAS_NEST = asTag("has_structure/youkai_nest");

	public YHBiomeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> pvd, ExistingFileHelper helper) {
		super(output, pvd, YoukaiHomecoming.MODID, helper);
	}

	@Override
	protected void addTags(HolderLookup.Provider pvd) {
		tag(LAMPREY).add(Biomes.RIVER, Biomes.FROZEN_RIVER,
				Biomes.OCEAN, Biomes.COLD_OCEAN, Biomes.FROZEN_OCEAN,
				Biomes.DEEP_OCEAN, Biomes.DEEP_COLD_OCEAN, Biomes.DEEP_FROZEN_OCEAN);
		tag(SOYBEAN).add(Biomes.JUNGLE, Biomes.DARK_FOREST, Biomes.SWAMP);
		tag(REDBEAN).add(Biomes.SUNFLOWER_PLAINS, Biomes.BAMBOO_JUNGLE, Biomes.OLD_GROWTH_BIRCH_FOREST, Biomes.BIRCH_FOREST);
		tag(COFFEA).add(Biomes.PLAINS);//TODO
		tag(TEA).add(Biomes.PLAINS);//TODO

		tag(HAS_NEST).add(Biomes.PLAINS).addTag(BiomeTags.IS_FOREST);
	}

	public static TagKey<Biome> asTag(String name) {
		return TagKey.create(Registries.BIOME, new ResourceLocation(YoukaiHomecoming.MODID, name));
	}

}