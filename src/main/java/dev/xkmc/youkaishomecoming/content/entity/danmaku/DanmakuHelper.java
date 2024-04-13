package dev.xkmc.youkaishomecoming.content.entity.danmaku;

import net.minecraft.world.phys.Vec3;

public class DanmakuHelper {

	public record Orientation(Vec3 dir, Vec3 ax0, Vec3 ax1) {

		public Vec3 rotate(double rad) {
			rad = rad / 180 * Math.PI;
			return ax1.scale(Math.sin(rad)).add(dir.scale(Math.cos(rad)));
		}

		public Vec3 rotate(double rad, double ver) {
			rad = rad / 180 * Math.PI;
			ver = ver / 180 * Math.PI;
			return ax1.scale(Math.sin(rad) * Math.cos(ver))
					.add(dir.scale(Math.cos(rad) * Math.cos(ver)))
					.add(ax0.scale(Math.sin(ver)));
		}

	}

	public static Orientation getOrientation(Vec3 dir) {
		double val = (dir.x * dir.x + dir.z * dir.z);
		Vec3 ax0 = val < 1e-4 ? new Vec3(1, 0, 0) :
				new Vec3(-dir.x * dir.y, val, -dir.z * dir.y).normalize();
		Vec3 ax1 = dir.cross(ax0).normalize();
		return new Orientation(dir, ax0, ax1);
	}

}
