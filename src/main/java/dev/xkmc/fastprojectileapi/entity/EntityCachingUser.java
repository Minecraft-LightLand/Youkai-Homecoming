package dev.xkmc.fastprojectileapi.entity;

import dev.xkmc.fastprojectileapi.collision.UserCacheHolder;
import dev.xkmc.l2serial.util.Wrappers;
import net.minecraft.world.entity.LivingEntity;

public interface EntityCachingUser {

	UserCacheHolder entityCache();

	default LivingEntity self() {
		return Wrappers.cast(this);
	}

}
