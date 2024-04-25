package dev.xkmc.youkaishomecoming.content.spell.mover;

import dev.xkmc.danmaku.entity.DanmakuMovement;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.world.phys.Vec3;

@SerialClass
public final class ZeroMover extends DanmakuMover {

	@SerialClass.SerialField
	private Vec3 rot0, rot1;
	@SerialClass.SerialField
	private int time;

	@Deprecated
	public ZeroMover() {

	}

	public ZeroMover(Vec3 rot0, Vec3 rot1, int time) {
		this.rot0 = DanmakuMovement.of(rot0).rot();
		this.rot1 = DanmakuMovement.of(rot1).rot();
		this.time = time;
	}

	@Override
	public DanmakuMovement move(int tick, Vec3 prevPos, Vec3 prevVel) {
		return new DanmakuMovement(Vec3.ZERO, rot0.add(rot1.subtract(rot0).scale(1.0 * tick / time)));
	}

}
