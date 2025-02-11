package dev.xkmc.youkaishomecoming.content.spell.mover;

import dev.xkmc.fastprojectileapi.entity.ProjectileMovement;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.DanmakuHelper;
import net.minecraft.world.phys.Vec3;

@SerialClass
public final class RotateMover extends DanmakuMover {

	@SerialClass.SerialField
	private Vec3 dir;
	@SerialClass.SerialField
	private double rate;

	@Deprecated
	public RotateMover() {

	}

	public RotateMover(Vec3 dir, double rate) {
		this.dir = dir;
		this.rate = rate;
	}

	@Override
	public ProjectileMovement move(MoverInfo info) {
		var forward = DanmakuHelper.getOrientation(dir).rotateDegrees(rate * info.tick());
		return new ProjectileMovement(Vec3.ZERO, ProjectileMovement.of(forward).rot());
	}

}
