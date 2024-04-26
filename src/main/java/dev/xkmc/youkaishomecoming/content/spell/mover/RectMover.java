package dev.xkmc.youkaishomecoming.content.spell.mover;

import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.world.phys.Vec3;

@SerialClass
public final class RectMover extends TargetPosMover {

	@SerialClass.SerialField
	private Vec3 pos = Vec3.ZERO, v = Vec3.ZERO, a = Vec3.ZERO;

	@Deprecated
	public RectMover() {

	}

	public RectMover(Vec3 pos, Vec3 v, Vec3 a) {
		this.pos = pos;
		this.v = v;
		this.a = a;
	}

	@Override
	public Vec3 pos(MoverInfo info) {
		return pos(info.tick());
	}

	public Vec3 pos(double tick) {
		return pos.add(v.scale(tick)).add(a.scale(tick * tick * 0.5));
	}

}
