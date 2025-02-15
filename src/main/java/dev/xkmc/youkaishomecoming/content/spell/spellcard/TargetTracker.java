package dev.xkmc.youkaishomecoming.content.spell.spellcard;

import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;

@SerialClass
public class TargetTracker {

	@SerialClass.SerialField
	private Vec3 t0, t1, t2;
	@SerialClass.SerialField
	private int flyTime = 0;

	public void tick(int tick, CardHolder holder) {
		if (holder.self() instanceof Mob mob) {
			var e = mob.getTarget();
			if (e != null) {
				if (e.onGround()) {
					flyTime = 0;
				} else {
					flyTime++;
				}
			}
		}
		if (tick % 2 != 0) return;
		var target = holder.target();
		if (target == null) return;
		t0 = t1;
		t1 = t2;
		t2 = target;
	}

	public Vec3 vel() {
		if (t1 == null || t2 == null) return Vec3.ZERO;
		return t2.subtract(t2).scale(0.1);
	}

	public int flyTime() {
		return flyTime;
	}

}
