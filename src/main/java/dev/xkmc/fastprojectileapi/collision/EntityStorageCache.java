package dev.xkmc.fastprojectileapi.collision;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class EntityStorageCache {

	private static EntityStorageCache CACHE = null;

	public static EntityStorageCache get(ServerLevel sl) {
		if (CACHE != null) {
			if (CACHE.sl == sl && CACHE.time == sl.getGameTime()) {
				return CACHE;
			}
		}
		CACHE = new EntityStorageCache(sl);
		return CACHE;
	}

	private final ServerLevel sl;
	private final long time;
	private final FastMap<SectionCache> map = FastMapInit.createFastMap();

	public EntityStorageCache(ServerLevel sl) {
		this.sl = sl;
		this.time = sl.getGameTime();
	}

	private void checkSection(int x, int y, int z) {
		if (map.containsKey(x, y, z)) return;
		map.put(x, y, z, new SectionCache(sl, x, y, z));
	}

	public Iterable<Entity> foreach(AABB aabb, Predicate<Entity> filter) {
		int x0 = (((int) aabb.minX) >> 4) - 1;
		int y0 = (((int) aabb.minY) >> 4) - 1;
		int z0 = (((int) aabb.minZ) >> 4) - 1;
		int x1 = (((int) aabb.maxX) >> 4) + 1;
		int y1 = (((int) aabb.maxY) >> 4) + 1;
		int z1 = (((int) aabb.maxZ) >> 4) + 1;
		List<Entity> list = new ArrayList<>();
		for (int x = x0; x <= x1; x++) {
			for (int y = y0; y <= y1; y++) {
				for (int z = z0; z <= z1; z++) {
					checkSection(x, y, z);
					for (var e : map.get(x, y, z).intersect(aabb)) {
						if (aabb.intersects(e.getBoundingBox()) && filter.test(e)) {
							list.add(e);
						}
					}
				}
			}
		}
		return list;
	}

}
