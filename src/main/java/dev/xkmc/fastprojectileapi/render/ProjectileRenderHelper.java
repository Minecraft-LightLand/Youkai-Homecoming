package dev.xkmc.fastprojectileapi.render;

import dev.xkmc.l2serial.util.Wrappers;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;


@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = YoukaisHomecoming.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ProjectileRenderHelper {

	private static final Map<RenderLevelStageEvent.Stage, Map<RenderableProjectileType<?, ?>, Collection<?>>> MAP = new LinkedHashMap<>();

	public static <T extends RenderableProjectileType<T, I>, I> Collection<I> setOf(RenderableProjectileType<T, I> key) {
		return Wrappers.cast(MAP.computeIfAbsent(key.stage(), k -> new TreeMap<>())
				.computeIfAbsent(key, l -> new ArrayList<>()));
	}

	public static <T extends RenderableProjectileType<T, I>, I> void add(RenderableProjectileType<T, I> key, I ins) {
		setOf(key).add(ins);
	}

	@SubscribeEvent
	public static void renderLate(RenderLevelStageEvent event) {
		var map = MAP.remove(event.getStage());
		if (map == null || map.isEmpty()) return;
		var buffer = Minecraft.getInstance().renderBuffers().bufferSource();
		for (var ent : map.entrySet()) {
			ent.getKey().start(buffer, Wrappers.cast(ent.getValue()));
		}
		buffer.endLastBatch();
	}

}
