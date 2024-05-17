package dev.xkmc.fastprojectileapi.entity;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public record ProjectileMovement(Vec3 vec, Vec3 rot) {

	public static ProjectileMovement of(Vec3 vec) {
		double d0 = vec.horizontalDistance();
		Vec3 rot = new Vec3(-Mth.atan2(vec.y, d0), -Mth.atan2(vec.x, vec.z), 0);
		return new ProjectileMovement(vec, rot);
	}

}
