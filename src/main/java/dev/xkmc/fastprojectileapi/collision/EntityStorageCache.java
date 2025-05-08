package dev.xkmc.fastprojectileapi.collision;

import net.minecraft.server.level.ServerLevel;

public class EntityStorageCache implements IEntityCache {

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
	final FastMap<SectionCache> map = FastMapInit.createFastMap();

	public EntityStorageCache(ServerLevel sl) {
		this.sl = sl;
		this.time = sl.getGameTime();
	}

	@Override
	public SectionCache get(int x, int y, int z) {
		var ans = map.get(x, y, z);
		if (ans == null) {
			ans = new SectionCache(sl, x, y, z);
			map.put(x, y, z, ans);
		}
		return ans;
	}

}
