package dev.xkmc.fastprojectileapi.collision;

public interface FastMap<T> {

	boolean containsKey(int x, int y, int z);

	void put(int x, int y, int z, T t);

	T get(int x, int y, int z);

}
