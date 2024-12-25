package dev.xkmc.youkaishomecoming.content.spell.game;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.DanmakuHelper;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.ActualSpellCard;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.CardHolder;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.Ticker;
import dev.xkmc.youkaishomecoming.init.registrate.YHDanmaku;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.phys.Vec3;

@SerialClass
public class SanaeSpell extends ActualSpellCard {

	@SerialClass.SerialField
	private int selection = 0, groundTime = 0;

	@Override
	public void tick(CardHolder holder) {
		super.tick(holder);
		var le = holder.target();
		if (le == null) return;
		if (holder.self() instanceof Mob mob) {
			var e = mob.getTarget();
			if (e != null) {
				if (e.onGround()) groundTime = 0;
				else groundTime++;
			}
		}
		if (tick % 20 == 0) {
			if (groundTime < 40 && le.distanceTo(holder.center()) < 25) selection = 0;
			else selection = 1;
		}
		if (selection == 0) near(holder, le);
		else far(holder, le);
	}

	private void near(CardHolder holder, Vec3 le) {
		//奇迹「客星辉煌之夜」
		if (tick % 10 == 0) {
			var dir = holder.forward().scale(0.6);
			holder.shoot(holder.prepareDanmaku(80, dir, YHDanmaku.Bullet.CIRCLE, DyeColor.RED));
		}
		if (tick % 40 == 0) {
			var dir = holder.forward().multiply(1, 0, 1).normalize();
			if (dir.length() < 0.1) return;
			var pos = holder.center();
			pos = new Vec3(pos.x, le.y, pos.z);
			var o0 = DanmakuHelper.getOrientation(dir);
			for (int i = -1; i <= 1; i += 2) {
				var ans = new RotatingStar();
				ans.pos = o0.rotateDegrees(i * 45).scale(8).add(pos);
				ans.init = dir;
				ans.start = (i + 1) * 90;
				addTicker(ans);
			}
		}
	}

	private void far(CardHolder holder, Vec3 le) {
		//神德「五谷丰穰米之浴」
		if (tick % 20 == 0) {
			var ans = new ExplosiveGrains();
			ans.pos = holder.center();
			ans.dir = holder.forward();
			ans.target = le;
			double speed = Math.max(1, le.distanceTo(ans.pos) / 30);
			var vel = holder.targetVelocity();
			if (vel != null) {
				speed += vel.length() * 1.5;
			}
			ans.speed = speed;
			addTicker(ans);
		}
	}

	//奇迹「客星辉煌之夜」
	@SerialClass
	public static class RotatingStar extends Ticker<SanaeSpell> {

		@SerialClass.SerialField
		private Vec3 pos, init;
		@SerialClass.SerialField
		private double start;

		public boolean tick(CardHolder holder, SanaeSpell card) {
			step(holder);
			super.tick(holder, card);
			return tick > 40;
		}

		private void step(CardHolder holder) {
			if (pos == null) pos = holder.center();
			if (init == null) init = holder.forward();
			if (tick % 2 != 0) return;
			var dir = DanmakuHelper.getOrientation(init).rotateDegrees(9 * tick + start);
			var e = holder.prepareLaser(40, pos, dir, 40, YHDanmaku.Laser.LASER, DyeColor.LIGHT_BLUE);
			e.setupTime(10, 4, 12, 4);
			holder.shoot(e);
		}
	}

	//神德「五谷丰穰米之浴」
	@SerialClass
	public static class ExplosiveGrains extends Ticker<SanaeSpell> {

		private static final DyeColor[] COLORS = {DyeColor.LIGHT_BLUE, DyeColor.CYAN, DyeColor.LIME, DyeColor.YELLOW, DyeColor.LIGHT_GRAY};

		@SerialClass.SerialField
		private Vec3 pos, dir, target;
		@SerialClass.SerialField
		private double speed;

		public boolean tick(CardHolder holder, SanaeSpell card) {
			step(holder);
			super.tick(holder, card);
			return tick >= 50;
		}

		private void step(CardHolder holder) {
			if (target == null) {
				pos = holder.center();
				dir = holder.forward();
				target = holder.target();
			}
			int ver = 72;
			int off = 18;
			int dis = 12;
			int dur = 25;
			double spf = speed;
			if (tick < 10 && tick % 2 == 0) {
				var o0 = DanmakuHelper.getOrientation(dir).asNormal();
				for (int i = 0; i < 5; i++) {
					var p0 = pos.add(o0.rotateDegrees(i * 72).scale(dis));
					var d0 = target.subtract(p0).normalize();
					var o1 = DanmakuHelper.getOrientation(d0).asNormal();
					for (int j = 0; j < 5; j++) {
						var d1 = o1.rotateDegrees(j * 72 + i * off, ver).scale(spf);
						var e = holder.prepareDanmaku(dur, d1, YHDanmaku.Bullet.CIRCLE, DyeColor.RED);
						e.setPos(p0);
						holder.shoot(e);
					}
					var d1 = d0.scale(speed);
					var e = holder.prepareDanmaku(60, d1, YHDanmaku.Bullet.CIRCLE, DyeColor.RED);
					e.setPos(p0);
					holder.shoot(e);
				}
			}
			if (tick - dur >= 0 && tick - dur < 10 && tick % 2 == 0) {
				var r = holder.random();
				var o0 = DanmakuHelper.getOrientation(dir).asNormal();
				for (int i = 0; i < 5; i++) {
					var p0 = pos.add(o0.rotateDegrees(i * 72).scale(dis));
					var d0 = target.subtract(p0).normalize();
					var o1 = DanmakuHelper.getOrientation(d0).asNormal();
					for (int j = 0; j < 5; j++) {
						var d1 = o1.rotateDegrees(j * 72 + i * off, ver);
						var p1 = p0.add(d1.scale(spf * dur));
						for (int k = 0; k < 3; k++) {
							var d2 = new Vec3(r.nextGaussian(), r.nextGaussian(), r.nextGaussian()).normalize().scale(0.7);
							int life = 60 + r.nextInt(40);
							var e = holder.prepareDanmaku(life, d2, YHDanmaku.Bullet.BALL, COLORS[j]);
							e.setPos(p1);
							holder.shoot(e);
						}
					}
				}
			}
		}
	}

}
