package dev.xkmc.youkaishomecoming.content.entity.damaku;

import net.minecraft.world.phys.Vec3;

public class DamakuHelper {

	public record Orientation(Vec3 dir, Vec3 ax0, Vec3 ax1) {

		public Vec3 rotate(double rad) {
			rad = rad / 180 * Math.PI;
			return ax1.scale(Math.sin(rad)).add(dir.scale(Math.cos(rad)));
		}

	}

	public static Orientation getOrientation(Vec3 dir) {
		double val = (dir.x * dir.x + dir.z * dir.z);
		Vec3 ax0 = val < 1e-6 ? new Vec3(1, 0, 0) :
				new Vec3(-dir.x * dir.y, val, -dir.z * dir.y).normalize();
		Vec3 ax1 = dir.cross(ax0).normalize();
		return new Orientation(dir, ax0, ax1);
	}

}
