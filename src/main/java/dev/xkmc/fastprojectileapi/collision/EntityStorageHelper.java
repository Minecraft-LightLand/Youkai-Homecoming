package dev.xkmc.fastprojectileapi.collision;

import dev.xkmc.youkaishomecoming.mixin.PersistentEntitySectionManagerAccessor;
import dev.xkmc.youkaishomecoming.mixin.ServerLevelAccessor;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.Visibility;

public class EntityStorageHelper {

	public static boolean isPresent(Entity e) {
		return e.level() instanceof ServerLevel sl && sl.getEntity(e.getUUID()) == e;
	}

	public static boolean isTicking(ServerLevel sl, Entity e) {
		var manager = ((ServerLevelAccessor) sl).getEntityManager();
		var storage = ((PersistentEntitySectionManagerAccessor<Entity>) manager).getSectionStorage();
		var sect = storage.getSection(SectionPos.asLong(e.blockPosition()));
		if (sect == null) return false;
		return e.isAlwaysTicking() || sect.getStatus().isTicking();
	}

	public static void clear(ServerLevel sl, Entity e) {
		var manager = ((ServerLevelAccessor) sl).getEntityManager();
		var storage = ((PersistentEntitySectionManagerAccessor<Entity>) manager).getSectionStorage();
		var sect = storage.getSection(SectionPos.asLong(e.blockPosition()));
		if (sect == null) return;
		sect.remove(e);
		Visibility visibility = e.isAlwaysTicking() ? Visibility.TICKING : sect.getStatus();
		if (visibility.isTicking()) {
			((PersistentEntitySectionManagerAccessor<?>) manager).callStopTicking(e);
		}
		if (visibility.isAccessible()) {
			((PersistentEntitySectionManagerAccessor<?>) manager).callStopTracking(e);
		}
		((PersistentEntitySectionManagerAccessor<?>) manager).getKnownUuids().remove(e.getUUID());
	}

}
