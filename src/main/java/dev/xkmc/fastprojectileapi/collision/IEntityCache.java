package dev.xkmc.fastprojectileapi.collision;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public interface IEntityCache {

	SectionCache get(int x, int y, int z);

	default List<Entity> foreach(AABB aabb, Predicate<Entity> filter) {
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
