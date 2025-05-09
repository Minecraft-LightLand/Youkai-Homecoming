package dev.xkmc.fastprojectileapi.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public interface GrazingEntity {


	default float grazeRange() {
		return 0;
	}

	default void doGraze(Player entity) {

	}

	default double reducedRadius(Entity x, float radius) {
		return radius;
	}

}
