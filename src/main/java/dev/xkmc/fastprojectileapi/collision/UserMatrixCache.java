package dev.xkmc.fastprojectileapi.collision;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class UserMatrixCache implements IEntityCache {

	private static final int R = 5;

	private final EntityStorageCache parent;
	final ServerLevel sl;
	final long time;
	private final SectionCache[][][] cache;
	private final int x0, y0, z0;

	public UserMatrixCache(ServerLevel sl, int x0, int y0, int z0) {
		this.time = sl.getGameTime();
		this.sl = sl;
		parent = EntityStorageCache.get(sl);
		this.x0 = x0;
		this.y0 = y0;
		this.z0 = z0;
		int d = R * 2 + 1;
		cache = new SectionCache[d][d][d];
	}

	protected void checkSection(int x, int y, int z) {
		int ix = x - x0 + R;
		int iy = y - y0 + R;
		int iz = z - z0 + R;
		if (ix >= 0 && ix < R * 2 + 1 && iy >= 0 && iy < R * 2 + 1 && iz >= 0 && iz < R * 2 + 1) {
			if (cache[ix][iy][iz] == null) {
				parent.checkSection(x, y, z);
				cache[ix][iy][iz] = parent.map.get(x, y, z);
			}
		} else parent.checkSection(x, y, z);

	}

	SectionCache get(int x, int y, int z) {
		int ix = x - x0 + R;
		int iy = y - y0 + R;
		int iz = z - z0 + R;
		if (ix >= 0 && ix < R * 2 + 1 && iy >= 0 && iy < R * 2 + 1 && iz >= 0 && iz < R * 2 + 1) {
			return cache[ix][iy][iz];
		} else return parent.map.get(x, y, z);
	}

	public List<Entity> foreach(AABB aabb, Predicate<Entity> filter) {
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
					for (var e : get(x, y, z).intersect(aabb)) {
						var ebox = e.getBoundingBox().expandTowards(e.getDeltaMovement());
						if (aabb.intersects(ebox) && filter.test(e)) {
							list.add(e);
						}
					}
				}
			}
		}
		return list;
	}

}
