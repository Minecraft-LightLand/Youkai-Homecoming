package dev.xkmc.fastprojectileapi.collision;

import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;

public class UserCacheHolder {

	private UserMatrixCache cache = null;

	public UserMatrixCache get(ServerLevel sl, LivingEntity user) {
		if (cache != null) {
			if (cache.sl == sl && cache.time == sl.getGameTime()) {
				return cache;
			}
		}
		var bpos = SectionPos.of(user.blockPosition());
		cache = new UserMatrixCache(sl, bpos.x(), bpos.y(), bpos.z());
		return cache;
	}

}
