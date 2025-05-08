package dev.xkmc.fastprojectileapi.collision;

import org.jetbrains.annotations.Nullable;

public interface FastMap<T> {

	boolean containsKey(int x, int y, int z);

	void put(int x, int y, int z, T t);

	@Nullable
	T get(int x, int y, int z);

}
