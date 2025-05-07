package dev.xkmc.fastprojectileapi.entity;

import net.minecraft.world.entity.player.Player;

public interface GrazingEntity {


	default float grazeRange() {
		return 0;
	}

	default void doGraze(Player entity) {

	}

}
