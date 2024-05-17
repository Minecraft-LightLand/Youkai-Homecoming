package dev.xkmc.fastprojectileapi.collision;

import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.core.SectionPos;

import java.util.HashMap;

public class FastMapInit {

	private static boolean useFast = true;

	public static <T> FastMap<T> createFastMap() {
		return useFast ? new LongFastMap<>() : new HashFastMap<>();
	}

	public static void init() {
		long hash = hash();
		long fast = fast();
		YoukaisHomecoming.LOGGER.info("HashMap test result: hash=" + hash + ", L2O=" + fast);
		useFast = fast < hash * 0.8;
	}

	private static long fast() {
		int r = 1;
		long t0 = System.currentTimeMillis();
		// 100k
		for (int k = 0; k < 20; k++) {
			Long2ObjectOpenHashMap<Integer> map = new Long2ObjectOpenHashMap<>();
			for (int i = 0; i < 2000; i++) {
				for (int x = -r; x <= r; x++) {
					for (int y = -r; y <= r; y++) {
						for (int z = -r; z <= r; z++) {
							long val = SectionPos.asLong(x, y, z);
							if (!map.containsKey(val)) {
								map.put(val, (Integer) 1);
							}
						}
					}
				}
			}
		}
		long t1 = System.currentTimeMillis();
		return t1 - t0;
	}

	private static long hash() {
		int r = 1;
		long t0 = System.currentTimeMillis();
		// 100k
		for (int k = 0; k < 20; k++) {
			HashMap<SectionPos, Integer> map = new HashMap<>();
			for (int i = 0; i < 2000; i++) {
				for (int x = -r; x <= r; x++) {
					for (int y = -r; y <= r; y++) {
						for (int z = -r; z <= r; z++) {
							SectionPos vec = SectionPos.of(x, y, z);
							if (!map.containsKey(vec)) {
								map.put(vec, 1);
							}
						}
					}
				}
			}
		}
		long t1 = System.currentTimeMillis();
		return t1 - t0;
	}

	private static class LongFastMap<T> implements FastMap<T> {

		private final Long2ObjectOpenHashMap<T> map = new Long2ObjectOpenHashMap<>();

		@Override
		public boolean containsKey(int x, int y, int z) {
			return map.containsKey(SectionPos.asLong(x, y, z));
		}

		@Override
		public void put(int x, int y, int z, T e) {
			map.put(SectionPos.asLong(x, y, z), e);
		}

		@Override
		public T get(int x, int y, int z) {
			return map.get(SectionPos.asLong(x, y, z));
		}
	}

	private static class HashFastMap<T> implements FastMap<T> {

		private final HashMap<SectionPos, T> map = new HashMap<>();

		@Override
		public boolean containsKey(int x, int y, int z) {
			return map.containsKey(SectionPos.of(x, y, z));
		}

		@Override
		public void put(int x, int y, int z, T e) {
			map.put(SectionPos.of(x, y, z), e);
		}

		@Override
		public T get(int x, int y, int z) {
			return map.get(SectionPos.of(x, y, z));
		}

	}

}
