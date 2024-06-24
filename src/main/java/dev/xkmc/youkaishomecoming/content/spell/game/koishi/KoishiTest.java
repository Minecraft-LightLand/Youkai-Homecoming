package dev.xkmc.youkaishomecoming.content.spell.game.koishi;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.DanmakuHelper;
import dev.xkmc.youkaishomecoming.content.spell.mover.RectMover;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.ActualSpellCard;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.CardHolder;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.Ticker;
import dev.xkmc.youkaishomecoming.init.registrate.YHDanmaku;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.phys.Vec3;

@SerialClass
public class KoishiTest extends ActualSpellCard {

	@Override
	public void tick(CardHolder holder) {
		super.tick(holder);
		Vec3 target = holder.target();
		if (target != null) {
			double t = 0.024 * tick;
			double dist = holder.center().distanceTo(target);
			for (int i = 0; i < 10; i++) {
				addLaser(holder, target, t + i * 17);
			}
			homing(holder, dist);
			var vec = holder.targetVelocity();
			if (dist > 26 && vec != null) {
				if (dist < 32) {
					if (holder.random().nextDouble() > 0.2 + 1 / (32 - dist))
						return;
				}
				border(holder, target, vec);
				if (holder.self() instanceof Mob mob) {
					var tar = mob.getTarget();
					if (tar != null) {
						Vec3 diff = tar.position().subtract(holder.center());
						if (diff.length() > 32) {
							tar.moveTo(diff.normalize().scale(32).add(holder.center()));
							tar.setDeltaMovement(diff.normalize().scale(-1));
							tar.hasImpulse = true;
							if (tar instanceof Player)
								tar.hurtMarked = true;
						}
					}
				}
			}
		}
	}

	private void homing(CardHolder holder, double dist) {
		if (tick % 4 == 0) {
			var le = holder.target();
			if (le == null) return;
			var ans = new StateChange();
			double a = tick * Math.PI / 20;
			ans.pos = holder.center();
			ans.front = new Vec3(Math.cos(a), 0, Math.sin(a));
			addTicker(ans);
		}
		if (tick % 10 == 0) {
			double speed = Math.max(0.6, dist / 40);
			int n = 24;
			var o0 = DanmakuHelper.getOrientation(holder.forward());
			double rand = holder.random().nextDouble() * 360;
			for (int i = 0; i < n; i++) {
				var dir = o0.rotateDegrees(360d / n * i + rand).scale(speed);
				var e = holder.prepareDanmaku(40, dir, YHDanmaku.Bullet.BALL, DyeColor.RED);
				holder.shoot(e);
			}
		}
	}

	private void addLaser(CardHolder holder, Vec3 target, double t) {
		double a = 1.47;
		double b = 4;
		double c = 3;
		double r = 32;
		double x0 = r * Math.cos(a * t) * Math.cos(t);
		double z0 = r * Math.cos(a * t) * Math.sin(t);
		double x1 = Math.cos(b * t);
		double z1 = Math.cos(b * t);
		var pos = holder.center();
		pos = new Vec3(pos.x + x0, Math.min(pos.y - 15, target.y - 10), pos.z + z0);
		var dir = new Vec3(x1, c, z1).normalize();
		var e = holder.prepareLaser(40, pos, dir, 60, YHDanmaku.Laser.LASER, DyeColor.BLUE);
		e.setupTime(10, 4, 20, 4);
		holder.shoot(e);
	}

	private void border(CardHolder holder, Vec3 target, Vec3 vel) {
		Vec3 fut = target.add(vel.scale(4));
		Vec3 cen = holder.center();
		double dist = target.distanceTo(cen);
		double min = Math.max(0, 32 - dist);
		double max = Math.max(min, fut.distanceTo(cen) - dist);
		int n = 4;

		var dir = fut.subtract(cen).normalize();
		var o0 = DanmakuHelper.getOrientation(dir).asNormal();
		for (int i = 0; i < n; i++) {
			double d = holder.random().nextDouble() * (max - min) + min + dist;
			var pos = dir.scale(d + 0.3).add(cen);
			var forward = o0.rotateDegrees(holder.random().nextDouble() * 360, -30).scale(0.1);
			var e = holder.prepareDanmaku(40, forward, YHDanmaku.Bullet.CIRCLE, DyeColor.PINK);
			e.setPos(pos);
			holder.shoot(e);
		}
	}

	@SerialClass
	public static class StateChange extends Ticker<KoishiTest> {

		@SerialClass.SerialField
		private Vec3 pos, front;
		@SerialClass.SerialField
		private int r0 = 4;
		@SerialClass.SerialField
		private int t0 = 20, t2 = 40, dt = 20;
		@SerialClass.SerialField
		private double termSpeed = 1, dr = 20;

		@Override
		public boolean tick(CardHolder holder, KoishiTest card) {
			step(holder);
			super.tick(holder, card);
			return false;
		}

		private void step(CardHolder holder) {
			var le = holder.target();
			if (le == null) return;
			var r = holder.random();
			if (front == null) {
				pos = holder.center();
				front = le.subtract(holder.center()).normalize();
			}
			if (tick < 0) return;
			if (tick == 0) {
				double acc = r0 * 2d / t0 / t0;
				var vec = front.scale(acc * t0);
				var e = holder.prepareDanmaku(t0, vec, YHDanmaku.Bullet.MENTOS, DyeColor.RED);
				e.mover = new RectMover(pos, vec, front.scale(-acc));
				holder.shoot(e);

			}
			if (tick == t0) {
				var p0 = pos.add(front.scale(r0));
				var vec = le.subtract(p0).normalize().scale(termSpeed);
				int t = t2 + r.nextInt(dt);
				var e = holder.prepareDanmaku(t, vec, YHDanmaku.Bullet.MENTOS, DyeColor.RED);
				e.setPos(p0);
				e.mover = new RectMover(p0, vec, Vec3.ZERO);
				holder.shoot(e);

			}
		}

	}

}
