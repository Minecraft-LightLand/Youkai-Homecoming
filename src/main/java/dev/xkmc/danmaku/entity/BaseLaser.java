package dev.xkmc.danmaku.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class BaseLaser extends SimplifiedProjectile {

	public BaseLaser(EntityType<?> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	@Override
	protected void defineSynchedData() {

	}

}
