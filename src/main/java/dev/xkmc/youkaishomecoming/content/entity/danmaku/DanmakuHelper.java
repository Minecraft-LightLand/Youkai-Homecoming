package dev.xkmc.youkaishomecoming.content.entity.danmaku;

import net.minecraft.world.phys.Vec3;

public class DanmakuHelper {

	public record Orientation(Vec3 dir, Vec3 ax0, Vec3 ax1) {

		public Vec3 rotateDegrees(double rad) {
			return rotate(rad / 180 * Math.PI);
		}

		public Vec3 rotateDegrees(double rad, double ver) {
			return rotate(rad / 180 * Math.PI, ver / 180 * Math.PI);
		}

		public Vec3 rotate(double rad) {
			return ax1.scale(Math.sin(rad)).add(dir.scale(Math.cos(rad)));
		}

		public Vec3 rotate(double rad, double ver) {
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

	public static Orientation getOrientation(Vec3 dir, Vec3 ax0) {
		Vec3 ax1 = dir.cross(ax0).normalize();
		return new Orientation(dir, ax0, ax1);
	}

}
