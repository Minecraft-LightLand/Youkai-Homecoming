package dev.xkmc.fastprojectileapi.render.core;

import dev.xkmc.fastprojectileapi.render.type.RenderableProjectileType;
import dev.xkmc.fastprojectileapi.render.virtual.ClientDanmakuCache;
import dev.xkmc.l2serial.util.Wrappers;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Collection;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = YoukaisHomecoming.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ProjectileRenderHelper {

	private static RenderQueue QUEUE;

	public static void setup() {
		ProjTypeHolder.setup();
		QUEUE = new RenderQueue();
	}

	public static <T extends RenderableProjectileType<T, I>, I> Collection<I> setOf(ProjTypeHolder<T, I> key) {
		return QUEUE.setOf(key);
	}

	public static <T extends RenderableProjectileType<T, I>, I> void add(ProjTypeHolder<T, I> key, I ins) {
		setOf(key).add(ins);
	}

	@SubscribeEvent
	public static void clientTick(TickEvent.LevelTickEvent event) {
		var level = Minecraft.getInstance().level;
		if (level != event.level || event.phase == TickEvent.Phase.START) return;
		var cache = ClientDanmakuCache.get(level);
		cache.tick();
	}

	@SubscribeEvent
	public static void renderLate(RenderLevelStageEvent event) {
		var level = Minecraft.getInstance().level;
		if (level == null) return;
		if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_ENTITIES) {
			var buffer = Minecraft.getInstance().renderBuffers().bufferSource();
			var cache = ClientDanmakuCache.get(level);
			cache.renderAll(event.getCamera(), event.getFrustum(), event.getPoseStack(), event.getPartialTick(), buffer);
			QUEUE.flush(buffer);
			buffer.endLastBatch();
		}
		if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_PARTICLES) {
			var buffer = Minecraft.getInstance().renderBuffers().bufferSource();
			QUEUE.flush(buffer);
			buffer.endLastBatch();
		}
	}

	private static class RenderQueue {

		private final ArrayList<?>[] lists = new ArrayList<?>[ProjTypeHolder.HOLDERS.size()];

		public <I> ArrayList<I> setOf(ProjTypeHolder<?, I> key) {
			if (lists[key.index] == null) {
				lists[key.index] = new ArrayList<>();
			}
			return Wrappers.cast(lists[key.index]);
		}

		public void flush(MultiBufferSource.BufferSource buffer) {
			int n = lists.length;
			for (int i = 0; i < n; i++) {
				var list = lists[i];
				lists[i] = null;
				if (list != null) {
					ProjTypeHolder.HOLDERS.get(i).type.start(buffer, Wrappers.cast(list));
				}
			}
		}

	}

}
