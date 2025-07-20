package dev.xkmc.youkaishomecoming.content.spell.game;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.DanmakuHelper;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.ActualSpellCard;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.CardHolder;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.Ticker;
import dev.xkmc.youkaishomecoming.init.registrate.YHDanmaku;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

@SerialClass
public class RemiliaSpell extends ActualSpellCard {

	@Override
	public void tick(CardHolder holder) {
		super.tick(holder);
		var dir = holder.forward();
		var target = holder.target();
		if (target == null) return;
		if (tick % 20 == 0) {
			int step = tick / 20;
			double dist = holder.center().distanceTo(target);
			var rand = holder.random();
			if (step % 5 < 3) {
				double v = Math.max(1, dist / 20);

				var nor = DanmakuHelper.getOrientation(dir).asNormal()
						.rotateDegrees((rand.nextDouble() * 2 - 1) * 60);
				double ver = 5;
				var tv = holder.targetVelocity();
				if (tv != null) ver += Math.min(10, tv.length() * 5f);
				addTicker(new Sweep().init(dir, nor,
						180,
						15, ver, v * 0.8, v * 1.2,
						20, (int) (15 * v), (int) Math.max(60, dist * 2)
				));
			}
			if (step % 5 == 3) {
				var vel = holder.targetVelocity();
				if (vel != null && vel.length() > 1) {
					addTicker(new Lasers().init(20, 4, 3, 25, 40, 140));
				}
			}
			if (step % 5 == 4 && dist >= 40) {
				shootSpear(holder, target, dir);
			}
		}

	}

	private void shootSpear(CardHolder holder, Vec3 target, Vec3 dir) {
		var rand = holder.random();
		var self = holder.self();
		var tar = holder.center().lerp(target, 0.5);
		var hit = self.level().clip(new ClipContext(holder.center(), tar, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, self));
		if (hit.getType() == HitResult.Type.MISS) {
			self.moveTo(tar);
		} else {
			self.moveTo(holder.center().lerp(hit.getLocation(), 0.9));
		}
		double dist = holder.center().distanceTo(target);
		int n = (int) Math.max(100, dist * 5);
		var o = DanmakuHelper.getOrientation(dir).asNormal();
		for (int i = 0; i < n; i++) {
			double p = 1d * i / n;
			double dr = rand.nextDouble() * p * (1 - p) * 4 * Math.max(1, dist / 20);
			var pos = holder.center().lerp(target, p * 1.2)
					.add(o.rotateDegrees(rand.nextDouble() * 360).scale(dr));
			var e = holder.prepareDanmaku(30, dir.scale(3), YHDanmaku.Bullet.MENTOS, DyeColor.RED);
			e.setPos(pos);
			holder.shoot(e);
		}
	}

	@SerialClass
	public static class Sweep extends Ticker<RemiliaSpell> {

		@SerialClass.SerialField
		private Vec3 dir, nor;
		@SerialClass.SerialField
		private double initialAngle, horSpread, verSpread, lowSpeed, highSpeed;
		@SerialClass.SerialField
		private int duration, count, range;

		public Sweep init(
				Vec3 dir, Vec3 nor,
				double initialAngle, double horSpread, double verSpread,
				double lowSpeed, double highSpeed,
				int duration, int count, int range
		) {
			this.dir = dir;
			this.nor = nor;
			this.initialAngle = initialAngle;
			this.horSpread = horSpread;
			this.verSpread = verSpread;
			this.lowSpeed = lowSpeed;
			this.highSpeed = highSpeed;
			this.duration = duration;
			this.count = count;
			this.range = range;
			return this;
		}

		@Override
		public boolean tick(CardHolder holder, RemiliaSpell card) {
			double a0 = initialAngle + 360d / duration * tick;
			var rand = holder.random();
			var o = DanmakuHelper.getOrientation(dir, nor);
			for (int i = 0; i < count; i++) {
				double a1 = a0 + horSpread * (rand.nextDouble() * 2 - 1);
				double a2 = verSpread * rand.nextGaussian();
				double v0 = lowSpeed + (highSpeed - lowSpeed) * rand.nextDouble();
				int t = (int) Math.ceil(range / v0 * (1 + rand.nextDouble() * 0.1));
				var d = o.rotateDegrees(a1, a2);
				var e = holder.prepareDanmaku(t, d.scale(v0), YHDanmaku.Bullet.BUBBLE, DyeColor.RED);
				holder.shoot(e);

				double mid = 0.6 + 0.3 * rand.nextDouble();
				e = holder.prepareDanmaku((int) (t / mid * 0.8), d.scale(v0 * mid), YHDanmaku.Bullet.MENTOS, DyeColor.RED);
				holder.shoot(e);

				double low = 0.3 + 0.3 * rand.nextDouble();
				e = holder.prepareDanmaku((int) (t / low * 0.6), d.scale(v0 * low), YHDanmaku.Bullet.BALL, DyeColor.RED);
				holder.shoot(e);
			}
			super.tick(holder, card);
			return tick > duration;
		}
	}

	@SerialClass
	public static class Lasers extends Ticker<RemiliaSpell> {

		@SerialClass.SerialField
		public int duration, count, spread, minLen, maxLen, life;

		public Lasers init(int duration, int count, int spread, int minLen, int maxLen, int life) {
			this.duration = duration;
			this.count = count;
			this.spread = spread;
			this.minLen = minLen;
			this.maxLen = maxLen;
			this.life = life;
			return this;
		}

		@Override
		public boolean tick(CardHolder holder, RemiliaSpell card) {
			var rand = holder.random();
			var cen = holder.center();
			for (int i = 0; i < count; i++) {
				Vec3 dir = new Vec3(rand.nextGaussian(), rand.nextGaussian(), rand.nextGaussian()).normalize();
				int len = rand.nextInt(minLen, maxLen);
				var l0 = holder.prepareLaser(life, cen, dir, len, YHDanmaku.Laser.LASER, DyeColor.LIGHT_BLUE);
				l0.setupTime(10, 10, life, 10);
				holder.shoot(l0);
				var pos = cen.add(dir.scale(len));
				var o = DanmakuHelper.getOrientation(dir).asNormal();
				double angle = rand.nextDouble() * 360;
				for (int j = 0; j < spread; j++) {
					Vec3 dst = o.rotateDegrees(angle + j * 120, 45);
					var l1 = holder.prepareLaser(life, pos, dst, 80, YHDanmaku.Laser.LASER, DyeColor.LIGHT_BLUE);
					l0.setupTime(20, 10, life, 10);
					holder.shoot(l1);
				}
			}
			super.tick(holder, card);
			return tick > duration;
		}
	}

}
