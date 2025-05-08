package dev.xkmc.fastprojectileapi.collision;

import net.minecraft.server.level.ServerLevel;

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

	public SectionCache get(int x, int y, int z) {
		int ix = x - x0 + R;
		int iy = y - y0 + R;
		int iz = z - z0 + R;
		if (ix >= 0 && ix < R * 2 + 1 && iy >= 0 && iy < R * 2 + 1 && iz >= 0 && iz < R * 2 + 1) {
			if (cache[ix][iy][iz] == null) {
				cache[ix][iy][iz] = parent.get(x, y, z);
			}
			return cache[ix][iy][iz];
		} else {
			return parent.get(x, y, z);
		}
	}

}
