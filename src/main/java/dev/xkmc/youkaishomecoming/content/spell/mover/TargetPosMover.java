package dev.xkmc.youkaishomecoming.content.spell.mover;

import dev.xkmc.fastprojectileapi.entity.ProjectileMovement;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.world.phys.Vec3;

@SerialClass
public abstract class TargetPosMover extends DanmakuMover {

	public abstract Vec3 pos(MoverInfo info);

	public ProjectileMovement move(MoverInfo info) {
		var ans = ProjectileMovement.of(pos(info).subtract(info.prevPos()));
		if (ans.vec().lengthSqr() > 1e-4) {
			return ans;
		}
		return new ProjectileMovement(ans.vec(), info.self().self().rot());
	}

}
