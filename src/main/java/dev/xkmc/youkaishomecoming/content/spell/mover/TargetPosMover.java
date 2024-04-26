package dev.xkmc.youkaishomecoming.content.spell.mover;

import dev.xkmc.danmaku.entity.DanmakuMovement;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.world.phys.Vec3;

@SerialClass
public abstract class TargetPosMover extends DanmakuMover {

	public abstract Vec3 pos(MoverInfo info);

	public DanmakuMovement move(MoverInfo info) {
		var ans = DanmakuMovement.of(pos(info).subtract(info.prevPos()));
		if (ans.vec().lengthSqr() > 1e-4) {
			return ans;
		}
		return new DanmakuMovement(ans.vec(), info.self().self().rot());
	}

}
