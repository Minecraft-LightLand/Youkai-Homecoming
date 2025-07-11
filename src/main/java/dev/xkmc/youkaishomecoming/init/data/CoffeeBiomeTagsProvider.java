package dev.xkmc.youkaishomecoming.init.data;

import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public final class CoffeeBiomeTagsProvider extends BiomeTagsProvider {

	public static final TagKey<Biome> COFFEA = asTag("spawns/coffea");

	public CoffeeBiomeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> pvd, ExistingFileHelper helper) {
		super(output, pvd, YoukaisHomecoming.MODID, helper);
	}

	@Override
	protected void addTags(HolderLookup.Provider pvd) {
		tag(COFFEA).addTag(BiomeTags.IS_JUNGLE);
	}

	public static TagKey<Biome> asTag(String name) {
		return TagKey.create(Registries.BIOME, YoukaisHomecoming.loc(name));
	}

}