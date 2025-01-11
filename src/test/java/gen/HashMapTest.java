package gen;

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;

import java.util.HashMap;

public class HashMapTest {

	public static void main(String[] args) {
		hash();
		fast();
	}

	private static void fast() {
		int r = 1;
		long t0 = System.currentTimeMillis();
		// 100k
		for (int k = 0; k < 20; k++) {
			Long2ObjectOpenHashMap<Integer> map = new Long2ObjectOpenHashMap<>();
			for (int i = 0; i < 2000; i++) {
				for (int x = -r; x <= r; x++) {
					for (int y = -r; y <= r; y++) {
						for (int z = -r; z <= r; z++) {
							long val = Vec3i.asLong(x, y, z);
							if (!map.containsKey((long) val)) {
								map.put((long) val, (Integer) 1);
							}
						}
					}
				}
			}
		}
		long t1 = System.currentTimeMillis();
		System.out.println("L2O: " + (t1 - t0));
	}

	private static void hash() {
		int r = 1;
		long t0 = System.currentTimeMillis();
		// 100k
		for (int k = 0; k < 20; k++) {
			HashMap<Vec3i, Integer> map = new HashMap<>();
			for (int i = 0; i < 2000; i++) {
				for (int x = -r; x <= r; x++) {
					for (int y = -r; y <= r; y++) {
						for (int z = -r; z <= r; z++) {
							Vec3i vec = new Vec3i(x, y, z);
							if (!map.containsKey(vec)) {
								map.put(vec, 1);
							}
						}
					}
				}
			}
		}
		long t1 = System.currentTimeMillis();
		System.out.println("Hash: " + (t1 - t0));
	}

	public static void test() {

	}

}
