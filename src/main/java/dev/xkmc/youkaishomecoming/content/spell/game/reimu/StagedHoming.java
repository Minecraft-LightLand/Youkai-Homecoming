package dev.xkmc.youkaishomecoming.content.spell.game.reimu;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.DanmakuHelper;
import dev.xkmc.youkaishomecoming.content.spell.mover.RectMover;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.ActualSpellCard;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.CardHolder;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.Ticker;
import dev.xkmc.youkaishomecoming.init.registrate.YHDanmaku;
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


	@SerialClass
	public static class StateChange extends Ticker<StagedHoming> {

		@SerialClass.SerialField
		private Vec3 pos, init, normal, target1;

		@Override
		public boolean tick(CardHolder holder, StagedHoming card) {
			step(holder);
			super.tick(holder, card);
			return false;
		}

		private void step(CardHolder holder) {
			var le = holder.target();
			if (le == null) return;

			int n = 20;
			int r0 = 8;
			int t0 = 20;
			int r1 = 6;
			int t1 = 20;
			int t2 = 40;
			int dt = 20;

			var r = holder.random();

			if (tick == 0) {
				pos = holder.center();
				var dir = le.subtract(holder.center()).normalize();
				init = DanmakuHelper.getOrientation(dir).rotateDegrees(90, 60 * r.nextDouble() - 30);
				normal = dir.cross(init);
				DanmakuHelper.Orientation o0 = DanmakuHelper.getOrientation(init, normal);
				double acc = r0 * 2d / t0 / t0;
				for (int i = 0; i < n; i++) {
					var front = o0.rotateDegrees(360.0 / n * i);
					var vec = front.scale(acc * t0);
					var e = holder.prepareDanmaku(t0, vec, YHDanmaku.Bullet.CIRCLE, DyeColor.PURPLE);
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
					var e = holder.prepareDanmaku(t1, vec, YHDanmaku.Bullet.CIRCLE, DyeColor.WHITE);
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
					var e = holder.prepareDanmaku(t, vec, YHDanmaku.Bullet.CIRCLE, DyeColor.RED);
					e.setPos(p1);
					e.mover = new RectMover(p1, vec, Vec3.ZERO);
					holder.shoot(e);
				}
			}
		}

	}

}
