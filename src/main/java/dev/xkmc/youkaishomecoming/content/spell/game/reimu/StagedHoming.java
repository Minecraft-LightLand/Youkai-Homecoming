package dev.xkmc.youkaishomecoming.content.spell.game.reimu;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.DanmakuHelper;
import dev.xkmc.youkaishomecoming.content.spell.mover.RectMover;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.ActualSpellCard;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.CardHolder;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.Ticker;
import dev.xkmc.youkaishomecoming.init.registrate.YHDanmaku;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.phys.Vec3;

@SerialClass
public class StagedHoming extends ActualSpellCard {

	@Override
	public void tick(CardHolder holder) {
		super.tick(holder);
		int interval = 10;
		if (tick % interval == 0) {
			int step = tick / interval % 5;
			if (step >= 3) return;
			addTicker(new StateChange());
		}
	}

	@Override
	public void hurt(CardHolder holder, DamageSource source, float amount) {
		super.hurt(holder, source, amount);
		var le = holder.target();
		if (le == null) return;
		var r = holder.random();
		var dir = le.subtract(holder.center()).normalize();
		if (r.nextDouble() < 0.5) dir = new Vec3(1, 0, 0);
		var ori = DanmakuHelper.getOrientation(dir).rotateDegrees(90, 60 * r.nextDouble() - 30);
		var normal = dir.cross(ori);
		int n = 8;
		int s = r.nextDouble() < 0.5 ? -1 : 1;
		for (int i = 0; i <= 5; i++) {
			var ans = new StateChange();
			ans.r0 = 24;
			ans.r1 = 18;
			ans.n = n;
			ans.bullet = YHDanmaku.Bullet.BUBBLE;
			ans.pos = holder.center();
			ans.init = DanmakuHelper.getOrientation(ori, normal).rotateDegrees(s * (i - 2) * 360d / n / 4);
			ans.normal = normal;
			ans.tick = -i * 2;
			addTicker(ans);
		}
	}

	@SerialClass
	public static class StateChange extends Ticker<StagedHoming> {

		@SerialClass.SerialField
		private Vec3 pos, init, normal, target1;
		@SerialClass.SerialField
		private int r0 = 8, r1 = 6, n = 20;
		@SerialClass.SerialField
		private YHDanmaku.Bullet bullet = YHDanmaku.Bullet.CIRCLE;

		@Override
		public boolean tick(CardHolder holder, StagedHoming card) {
			step(holder);
			super.tick(holder, card);
			return false;
		}

		private void step(CardHolder holder) {
			var le = holder.target();
			if (le == null) return;

			int t0 = 20;
			int t1 = 20;
			int t2 = 40;
			int dt = 20;

			var r = holder.random();
			if (init == null) {
				pos = holder.center();
				var dir = le.subtract(holder.center()).normalize();
				init = DanmakuHelper.getOrientation(dir).rotateDegrees(90, 60 * r.nextDouble() - 30);
				normal = dir.cross(init);
			}
			if (tick < 0) return;
			if (tick == 0) {
				DanmakuHelper.Orientation o0 = DanmakuHelper.getOrientation(init, normal);
				double acc = r0 * 2d / t0 / t0;
				for (int i = 0; i < n; i++) {
					var front = o0.rotateDegrees(360.0 / n * i);
					var vec = front.scale(acc * t0);
					var e = holder.prepareDanmaku(t0, vec, bullet, DyeColor.PURPLE);
					e.mover = new RectMover(pos, vec, front.scale(-acc));
					holder.shoot(e);
				}
			}
			if (tick == t0) {
				target1 = le;
				DanmakuHelper.Orientation o0 = DanmakuHelper.getOrientation(init, normal);
				double acc = r1 * 2d / t1 / t1;
				for (int i = 0; i < n; i++) {
					var f0 = o0.rotateDegrees(360.0 / n * i);
					var p0 = pos.add(f0.scale(r0));
					var f1 = target1.subtract(p0).normalize();
					var vec = f1.scale(acc * t1);
					var e = holder.prepareDanmaku(t1, vec, bullet, DyeColor.WHITE);
					e.setPos(p0);
					e.mover = new RectMover(p0, vec, f1.scale(-acc));
					holder.shoot(e);
				}
			}
			if (tick == t0 + t1) {
				DanmakuHelper.Orientation o0 = DanmakuHelper.getOrientation(init, normal);
				for (int i = 0; i < n; i++) {
					var f0 = o0.rotateDegrees(360.0 / n * i);
					var p0 = pos.add(f0.scale(r0));
					var f1 = target1.subtract(p0).normalize();
					var p1 = p0.add(f1.scale(r1));
					var f2 = le.subtract(p1).normalize();
					var vec = f2.scale(1);
					int t = t2 + r.nextInt(dt);
					var e = holder.prepareDanmaku(t, vec, bullet, DyeColor.RED);
					e.setPos(p1);
					e.mover = new RectMover(p1, vec, Vec3.ZERO);
					holder.shoot(e);
				}
			}
		}

	}

}
