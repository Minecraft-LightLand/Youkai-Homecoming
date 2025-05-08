package dev.xkmc.fastprojectileapi.collision;

import dev.xkmc.youkaishomecoming.mixin.PersistentEntitySectionManagerAccessor;
import dev.xkmc.youkaishomecoming.mixin.ServerLevelAccessor;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.List;

public class SectionCache {

	private final AABB aabb;
	private final List<Entity> all = new ArrayList<>();
	private final List<Entity> margin = new ArrayList<>();

	SectionCache(ServerLevel sl, int x, int y, int z) {
		aabb = new AABB(x << 4, y << 4, z << 4,
				(x + 1) << 4, (y + 1) << 4, (z + 1) << 4);
		var manager = ((ServerLevelAccessor) sl).getEntityManager();
		var storage = ((PersistentEntitySectionManagerAccessor<Entity>) manager).getSectionStorage();
		var sect = storage.getSection(SectionPos.asLong(x, y, z));
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
