package dev.xkmc.fastprojectileapi.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

public interface GrazingEntity {


	default float grazeRange() {
		return 0;
	}

	default void doGraze(Player entity) {

	}

	default AABB alterHitBox(Entity x, float radius, float graze) {
		return x.getBoundingBox().inflate(radius + graze);
	}

}
