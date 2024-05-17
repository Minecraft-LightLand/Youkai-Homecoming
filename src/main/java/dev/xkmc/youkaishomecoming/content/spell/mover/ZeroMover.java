package dev.xkmc.youkaishomecoming.content.spell.mover;

import dev.xkmc.danmaku.entity.ProjectileMovement;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.world.phys.Vec3;

@SerialClass
public final class ZeroMover extends DanmakuMover {

	private static double closest(double a, double b) {
		double step = Math.PI * 2;
		if (Math.abs(b - a) > Math.abs(b - a + step))
			return b + step;
		if (Math.abs(b - a) > Math.abs(b - a - step))
			return b - step;
		return b;
	}

	private static Vec3 closest(Vec3 a, Vec3 b) {
		return new Vec3(closest(a.x, b.x), closest(a.y, b.y), closest(a.z, b.z));
	}

	@SerialClass.SerialField
	private Vec3 rot0, rot1;
	@SerialClass.SerialField
	private int time;

	@Deprecated
	public ZeroMover() {

	}

	public ZeroMover(Vec3 rot0, Vec3 rot1, int time) {
		this.rot0 = ProjectileMovement.of(rot0).rot();
		this.rot1 = closest(rot0, ProjectileMovement.of(rot1).rot());
		this.time = time;
	}

	@Override
	public ProjectileMovement move(MoverInfo info) {
		return new ProjectileMovement(Vec3.ZERO, rot0.add(rot1.subtract(rot0).scale(1.0 * info.tick() / time)));
	}

}
