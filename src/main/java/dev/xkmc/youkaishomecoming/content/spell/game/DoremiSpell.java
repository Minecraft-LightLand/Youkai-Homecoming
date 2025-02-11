package dev.xkmc.youkaishomecoming.content.spell.game;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.DanmakuHelper;
import dev.xkmc.youkaishomecoming.content.entity.youkai.YoukaiEntity;
import dev.xkmc.youkaishomecoming.content.spell.mover.CompositeMover;
import dev.xkmc.youkaishomecoming.content.spell.mover.RectMover;
import dev.xkmc.youkaishomecoming.content.spell.mover.RotateMover;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.ActualSpellCard;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.CardHolder;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.Ticker;
import dev.xkmc.youkaishomecoming.init.registrate.YHDanmaku;
import net.minecraft.util.Mth;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.phys.Vec3;

@SerialClass
public class DoremiSpell extends ActualSpellCard {

	@SerialClass.SerialField
	private int cooldown, mazeTime, madTime, groundTime;

	@Override
	public void tick(CardHolder holder) {
		super.tick(holder);
		boolean onGround = false;
		if (holder.self() instanceof YoukaiEntity e) {
			var le = e.getTarget();
			if (le != null) {
				onGround = le.onGround();
			}
		}
		if (onGround) {
			groundTime++;
			if (groundTime > 20) groundTime = 20;
		} else {
			groundTime--;
			if (groundTime < 0) groundTime = 0;
		}
		if (cooldown > 0)
			cooldown--;
		if (mazeTime > 0)
			mazeTime--;
		if (madTime > 0)
			madTime--;
		if (cooldown <= 100 && mazeTime <= 0 && groundTime == 20) {
			addTicker(new Maze());
			mazeTime = 100;
			madTime = 160;
			cooldown += 180;
			return;
		}
		if (madTime <= 0 && (groundTime == 0 || mazeTime <= 0)) {
			addTicker(new Madness());
			madTime = 100;
			mazeTime = 160;
		}
	}

	@SerialClass
	public static class Maze extends Ticker<DoremiSpell> {

		@SerialClass.SerialField
		private Vec3 pos;
		@SerialClass.SerialField
		private double init;

		@Override
		public boolean tick(CardHolder holder, DoremiSpell card) {
			super.tick(holder, card);
			if (pos == null) {
				pos = holder.target();
				init = holder.random().nextDouble() * 360;
				if (pos == null) return true;
				pos = pos.add(0, -0.3, 0);
				laser(holder, 8, 12, 6);

			}
			if (tick <= 80) {
				add(holder, pos, 9, 10, 8, 0);
			}
			chase(holder);
			return tick > 120;
		}

		private void laser(CardHolder holder, int n, int m, double dist) {
			var o = DanmakuHelper.getOrientation(new Vec3(1, 0, 0));
			for (int s = -1; s <= 1; s += 2) {
				var pz = pos.add(0, 2 * s, 0);
				for (int i = 0; i < n; i++) {
					var pi = pz.add(o.rotateDegrees(360d / n * i).scale(dist));
					double rand = holder.random().nextDouble() * 360;
					for (int j = 0; j < m; j++) {
						var dir = o.rotateDegrees(360d / n * i + 360d / m * j + rand);
						var l = holder.prepareLaser(120, pi, dir, 40,
								YHDanmaku.Laser.LASER, DyeColor.RED);
						l.mover = new RotateMover(dir, i % 2 == 0 ? 3 : -3);
						l.setupTime(0, 0, 100, 20);
						holder.shoot(l);
					}
				}
			}
		}

		private void add(CardHolder holder, Vec3 pos, int step, int n, double dist, double tilt) {
			int t0 = 40, t1 = 20;
			double v0 = 0.05, a0 = 0.1;
			var o = DanmakuHelper.getOrientation(new Vec3(1, 0, 0));
			double angle = init + tick * step;
			var p = pos.add(o.rotateDegrees(angle, tilt).scale(dist));
			var dir = o.rotateDegrees(angle, -tilt);
			for (int i = 0; i < n; i++) {
				int life = 80;
				Vec3 v = DanmakuHelper.getOrientation(dir).rotateDegrees(i * 360d / n);
				var e = holder.prepareDanmaku(life, v,
						YHDanmaku.Bullet.BALL, i % 2 == 0 ? DyeColor.YELLOW : DyeColor.ORANGE);
				var m = new CompositeMover();
				e.setPos(p);
				m.add(t0, new RectMover(p, v.scale(v0), Vec3.ZERO));
				m.add(t1, new RectMover(p.add(v.scale(v0 * t0)), v.scale(v0), v.scale(a0)));
				m.addEnd();
				e.mover = m;
				holder.shoot(e);
			}
		}

		private void chase(CardHolder holder) {
			var target = holder.target();
			if (target != null && tick % 4 == 0 && Math.abs(target.y() - pos.y() - 0.3) > 2) {
				var e = holder.prepareDanmaku(2,
						pos.subtract(target).normalize().scale(0.1),
						YHDanmaku.Bullet.BALL, DyeColor.RED);
				e.setPos(target.add(0, -0.3, 0));
				holder.shoot(e);
			}
		}

	}

	@SerialClass
	public static class Madness extends Ticker<DoremiSpell> {

		@SerialClass.SerialField
		private double[] start, speed, freq, amp, rot, r0;

		@Override
		public boolean tick(CardHolder holder, DoremiSpell card) {
			super.tick(holder, card);
			if (start == null) {
				int n = 7;
				start = new double[n];
				speed = new double[n];
				freq = new double[n];
				amp = new double[n];
				rot = new double[n];
				r0 = new double[n];
				var r = holder.random();
				for (int i = 0; i < n; i++) {
					start[i] = r.nextDouble() * 360;
					speed[i] = r.nextDouble() * 2 + 2;
					amp[i] = r.nextDouble() * 90;
					freq[i] = (r.nextDouble() * 20 + 10) / (Math.max(amp[i] / 30, 1));
					rot[i] = r.nextDouble() * 10 + 10;
					r0[i] = r.nextDouble() * 360;
				}
			}
			var target = holder.target();
			if (target == null) return true;
			int m = 2;
			double dist = 16, v = 0.3;
			int n = start.length;
			var o0 = DanmakuHelper.getOrientation(new Vec3(1, 0, 0));
			for (int i = 0; i < n; i++) {
				var d0 = o0.rotateDegrees(
						start[i] + speed[i] * tick,
						amp[i] * (Math.sin(freq[i] * tick * Mth.DEG_TO_RAD) / 2 + 0.5)
				);
				var p = target.add(d0.scale(dist));
				var o1 = DanmakuHelper.getOrientation(d0);
				double r = rot[i] * tick + r0[i];
				for (int j = 0; j < m; j++) {
					var dir = o1.rotateDegrees(360d / m * j + r);
					var e = holder.prepareDanmaku(100, dir.scale(v),
							YHDanmaku.Bullet.BALL, j % 2 == 0 ? DyeColor.BLUE : DyeColor.MAGENTA);
					e.setPos(p);
					holder.shoot(e);
				}
			}
			return tick > 100;
		}

	}

}
