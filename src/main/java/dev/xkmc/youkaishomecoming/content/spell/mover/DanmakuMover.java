package dev.xkmc.youkaishomecoming.content.spell.mover;

import dev.xkmc.danmaku.entity.DanmakuMovement;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.world.phys.Vec3;

@SerialClass
public abstract class DanmakuMover {

	public abstract DanmakuMovement move(int tick, Vec3 prevPos, Vec3 prevVel);

}
