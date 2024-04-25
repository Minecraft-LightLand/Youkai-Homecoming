package dev.xkmc.youkaishomecoming.content.spell.mover;

import dev.xkmc.danmaku.entity.DanmakuMovement;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.DanmakuHelper;
import net.minecraft.world.phys.Vec3;

@SerialClass
public final class PolarMover extends DanmakuMover {

	@SerialClass.SerialField
	public Vec3 p, v, a, n, f;
	@SerialClass.SerialField
	public double r, vr, va, th, w, b;

	@Deprecated
	public PolarMover() {
	}

	public PolarMover(Vec3 p, Vec3 v, Vec3 a, Vec3 n, Vec3 f) {
		this.p = p;
		this.v = v;
		this.a = a;
		this.n = n;
		this.f = f;
	}

	public static PolarMover ofPlane(Vec3 pos, Vec3 a1, Vec3 a2) {
		var ori = DanmakuHelper.getOrientation(a1, a2);
		return new PolarMover(pos, Vec3.ZERO, Vec3.ZERO, ori.ax0(), ori.dir());
	}

	public Vec3 pos(double tick) {
		Vec3 rect = p.add(v.scale(tick)).add(a.scale(tick * tick * 0.5));
		Vec3 polar = DanmakuHelper.getOrientation(f, n)
				.rotate(th + w * tick + b * tick * tick * 0.5)
				.scale(r + vr * tick + va * tick * tick * 0.5);
		return rect.add(polar);
	}

	public Vec3 dir(double tick) {
		return pos(tick + 1e-3).subtract(pos(tick)).scale(1e3);
	}

	public DanmakuMovement move(int tick, Vec3 prevPos, Vec3 prevVel) {
		Vec3 diff = pos(tick).subtract(prevPos);
		return DanmakuMovement.of(diff);
	}

	public PolarMover copy() {
		return new PolarMover(p, v, a, n, f).radial(r, vr, va).angular(th, w, b);
	}

	public PolarMover radial(double r, double vr, double va) {
		this.r = r;
		this.vr = vr;
		this.va = va;
		return this;
	}

	public PolarMover angular(double th, double w, double b) {
		this.th = th;
		this.w = w;
		this.b = b;
		return this;
	}

	public PolarMover atTime(int tick) {
		p = p.add(v.scale(tick)).add(a.scale(tick * tick * 0.5));
		r += vr * tick + va * tick * tick * 0.5f;
		th += w * tick + b * tick * tick * 0.5f;
		v = v.add(a.scale(tick));
		vr += va * tick;
		w += b * tick;
		return this;
	}

	public PolarMover clearAccel() {
		a = Vec3.ZERO;
		va = b = 0;
		return this;
	}

	public RectMover toRect() {
		return new RectMover(pos(0), dir(0), Vec3.ZERO);
	}

}
