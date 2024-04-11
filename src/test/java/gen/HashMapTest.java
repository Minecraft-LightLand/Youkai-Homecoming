package gen;

import java.util.HashMap;

public class HashMapTest {

	public static void main(String[] args) {
		HashMap<Vec3i, Integer> map = new HashMap<>();
		int r = 2;
		long t0 = System.currentTimeMillis();
		// 100k
		for (int i = 0; i < 800; i++) {
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
		long t1 = System.currentTimeMillis();
		System.out.println(t1 - t0);
	}

	public static void test() {

	}

}
