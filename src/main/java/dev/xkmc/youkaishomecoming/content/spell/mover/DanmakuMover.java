package dev.xkmc.youkaishomecoming.content.spell.mover;

import dev.xkmc.danmaku.entity.DanmakuMovement;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.IYHDanmaku;
import net.minecraft.world.phys.Vec3;

@SerialClass
public abstract class DanmakuMover {

	public abstract DanmakuMovement move(MoverInfo info);

}
