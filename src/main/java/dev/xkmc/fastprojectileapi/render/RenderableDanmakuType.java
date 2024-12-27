package dev.xkmc.fastprojectileapi.render;

import net.minecraftforge.client.event.RenderLevelStageEvent;

public interface RenderableDanmakuType<T extends RenderableDanmakuType<T, I>, I> extends RenderableProjectileType<T, I> {

	@Override
	default int order() {
		return display().ordinal();
	}

	@Override
	default RenderLevelStageEvent.Stage stage() {
		return RenderLevelStageEvent.Stage.AFTER_PARTICLES;
	}

	DisplayType display();

}