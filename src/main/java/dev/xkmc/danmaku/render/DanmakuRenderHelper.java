package dev.xkmc.danmaku.render;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import dev.xkmc.l2serial.util.Wrappers;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;
import java.util.Set;


@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = YoukaisHomecoming.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DanmakuRenderHelper {

	private static final Map<RenderableDanmakuType<?, ?>, Set<RenderableDanmakuInstance<?>>> MAP = Maps.newConcurrentMap();

	public static void add(RenderableDanmakuInstance<?> ins) {
		MAP.computeIfAbsent(ins.key(), l -> Sets.newConcurrentHashSet()).add(ins);
	}

	@SubscribeEvent
	public static void renderLate(RenderLevelStageEvent event) {
		if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_ENTITIES) return;
		var buffer = Minecraft.getInstance().renderBuffers().bufferSource();
		for (var ent : MAP.entrySet()) {
			ent.getKey().start(buffer, Wrappers.cast(ent.getValue()));
		}
		buffer.endLastBatch();
		MAP.clear();

	}
}
