package dev.xkmc.youkaishomecoming.content.spell.mover;

import dev.xkmc.danmaku.entity.DanmakuMovement;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.world.phys.Vec3;

@SerialClass
public final class RectMover extends DanmakuMover {

	@SerialClass.SerialField
	private final Vec3 a;

	public RectMover(Vec3 a) {
		this.a = a;
	}

	@Override
	public DanmakuMovement move(int tick, Vec3 prevPos, Vec3 prevVel) {
		return DanmakuMovement.of(prevVel.add(a));
	}

}
