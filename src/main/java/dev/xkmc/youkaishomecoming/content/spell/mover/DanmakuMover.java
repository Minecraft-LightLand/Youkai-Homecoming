package dev.xkmc.youkaishomecoming.content.spell.mover;

import dev.xkmc.fastprojectileapi.entity.ProjectileMovement;
import dev.xkmc.l2serial.serialization.SerialClass;

@SerialClass
public abstract class DanmakuMover {

	public abstract ProjectileMovement move(MoverInfo info);

}
