package dev.xkmc.fastprojectileapi.render.type;

import dev.xkmc.fastprojectileapi.render.core.DisplayType;

public interface RenderableDanmakuType<T extends RenderableDanmakuType<T, I>, I> extends RenderableProjectileType<T, I> {

	@Override
	default int order() {
		return display().ordinal();
	}

	DisplayType display();

}