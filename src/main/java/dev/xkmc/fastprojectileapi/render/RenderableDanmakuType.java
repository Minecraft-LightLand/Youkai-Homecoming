package dev.xkmc.fastprojectileapi.render;

public interface RenderableDanmakuType<T extends RenderableDanmakuType<T, I>, I> extends RenderableProjectileType<T, I> {

	@Override
	default int order() {
		return display().ordinal();
	}

	DisplayType display();

}