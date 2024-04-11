package dev.xkmc.danmaku.collision;

import dev.xkmc.youkaishomecoming.mixin.PersistentEntitySectionManagerAccessor;
import dev.xkmc.youkaishomecoming.mixin.ServerLevelAccessor;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.HashMap;
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
	private final HashMap<SectionPos, SectionStorage> map = new HashMap<>();

	public EntityStorageCache(ServerLevel sl) {
		this.sl = sl;
		this.time = sl.getGameTime();
	}

	private void checkSection(SectionPos section) {
		if (map.containsKey(section)) return;
		map.put(section, new SectionStorage(section));
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
					var key = SectionPos.of(x, y, z);
					checkSection(key);
					for (var e : map.get(key).intersect(aabb)) {
						if (aabb.intersects(e.getBoundingBox()) && filter.test(e)) {
							list.add(e);
						}
					}
				}
			}
		}
		return list;
	}

	private class SectionStorage {

		private final SectionPos section;
		private final AABB aabb;
		private final List<Entity> all = new ArrayList<>();
		private final List<Entity> margin = new ArrayList<>();

		private SectionStorage(SectionPos section) {
			this.section = section;
			int x = section.getX();
			int y = section.getY();
			int z = section.getZ();
			aabb = new AABB(x << 4, y << 4, z << 4,
					(x + 1) << 4, (y + 1) << 4, (z + 1) << 4);
			var manager = ((ServerLevelAccessor) sl).getEntityManager();
			var storage = ((PersistentEntitySectionManagerAccessor<Entity>) manager).getSectionStorage();
			var sect = storage.getSection(section.asLong());
			if (sect != null) sect.getEntities().forEach(this::add);
		}

		private void add(Entity e) {
			if (!e.isPickable()) return;
			var ebox = e.getBoundingBox();
			if (aabb.minX <= ebox.minX && ebox.maxX <= aabb.maxX &&
					aabb.minY <= ebox.minY && ebox.maxY <= aabb.maxY &&
					aabb.minZ <= ebox.minZ && ebox.maxZ <= aabb.maxZ) {
				all.add(e);
			} else {
				all.add(e);
				margin.add(e);
			}
		}

		public Iterable<Entity> intersect(AABB box) {
			return aabb.intersects(box) ? all : margin;
		}

	}

}
