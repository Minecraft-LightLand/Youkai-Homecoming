package dev.xkmc.youkaishomecoming.content.spell.mover;

import dev.xkmc.fastprojectileapi.entity.ProjectileMovement;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.world.phys.Vec3;

@SerialClass
public class FixedDirMover extends DanmakuMover {

	@SerialClass.SerialField
	private DanmakuMover mover;
	@SerialClass.SerialField
	private Vec3 dir;

	public FixedDirMover() {
	}

	public FixedDirMover(DanmakuMover mover, Vec3 dir) {
		this.mover = mover;
		this.dir = dir;
	}

	@Override
	public ProjectileMovement move(MoverInfo info) {
		return new ProjectileMovement(mover.move(info).vec(),
				ProjectileMovement.of(dir).rot());
	}

}
