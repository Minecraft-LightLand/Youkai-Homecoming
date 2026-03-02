package dev.xkmc.youkaishomecoming.events;

import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = YoukaisHomecoming.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MCRFix {

	private static Holder<NoiseGeneratorSettings> temp;

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void pre(ServerAboutToStartEvent event) {
		temp = null;
		MinecraftServer server = event.getServer();
		Registry<DimensionType> types = server.registryAccess().registryOrThrow(Registries.DIMENSION_TYPE);
		Registry<LevelStem> stems = server.registryAccess().registryOrThrow(Registries.LEVEL_STEM);
		for (LevelStem levelStem : stems.stream().toList()) {
			if (levelStem.type().value() == types.getOrThrow(BuiltinDimensionTypes.OVERWORLD)) {
				if (levelStem.generator() instanceof NoiseBasedChunkGenerator noiseGenerator) {
					temp = noiseGenerator.settings;
				}
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void post(ServerAboutToStartEvent event) {
		MinecraftServer server = event.getServer();
		Registry<DimensionType> types = server.registryAccess().registryOrThrow(Registries.DIMENSION_TYPE);
		Registry<LevelStem> stems = server.registryAccess().registryOrThrow(Registries.LEVEL_STEM);
		for (LevelStem levelStem : stems.stream().toList()) {
			if (levelStem.type().value() == types.getOrThrow(BuiltinDimensionTypes.OVERWORLD)) {
				if (levelStem.generator() instanceof NoiseBasedChunkGenerator noiseGenerator) {
					if (temp != null && noiseGenerator.settings instanceof Holder.Direct) {
						noiseGenerator.settings = temp;
					}
				}
			}
		}
		temp = null;
	}


}
