package dev.xkmc.youkaishomecoming.content.spell.game;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.DanmakuHelper;
import dev.xkmc.youkaishomecoming.content.spell.mover.RectMover;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.ActualSpellCard;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.CardHolder;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.Ticker;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.TrailAction;
import dev.xkmc.youkaishomecoming.init.registrate.YHDanmaku;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.phys.Vec3;

@SerialClass
public class CirnoSpell extends ActualSpellCard {

	@Override
	public void tick(CardHolder holder) {
		super.tick(holder);
		if (tick % 10 == 0) {
			if (holder.self() instanceof Mob mob && mob.getTarget() instanceof Frog) {
				holder.shoot(holder.prepareDanmaku(40, holder.forward(), YHDanmaku.Bullet.CIRCLE, DyeColor.LIGHT_BLUE));
				return;
			}
			var le = holder.target();
			if (le == null) return;
			var dir = holder.forward();
			if (dir.multiply(1, 0, 1).length() > 0.1) {
				dir = dir.multiply(1, 0, 1).normalize();
			}
			var ori = DanmakuHelper.getOrientation(dir);
			var ans = new StateChange();
			ans.pos = holder.center();
			ans.init = ori.rotateDegrees(180);
			ans.normal = ori.normal();
			addTicker(ans);
		}
	}

	@SerialClass
	public static class StateChange extends Ticker<CirnoSpell> {

		@SerialClass.SerialField
		private Vec3 pos, init, normal;
		@SerialClass.SerialField
		private int r0 = 12, n = 3, m = 4;
		@SerialClass.SerialField
		private int t0 = 20, t2 = 40, dt = 20;
		@SerialClass.SerialField
		private double termSpeed = 1, dr = 20;

		@Override
		public boolean tick(CardHolder holder, CirnoSpell card) {
			step(holder);
			super.tick(holder, card);
			return tick > 0;
		}

		private void step(CardHolder holder) {
			var le = holder.target();
			if (le == null) return;
			var r = holder.random();
			if (init == null) {
				pos = holder.center();
				var dir = le.subtract(holder.center()).normalize();
				init = DanmakuHelper.getOrientation(dir).rotateDegrees(90, 0);
				normal = dir.cross(init);
			}
			if (tick < 0) return;
			if (tick == 0) {
				var o0 = DanmakuHelper.getOrientation(init, normal);
				double acc = r0 * 2d / t0 / t0;
				for (int i = 0; i < n; i++) {
					var front = o0.rotateDegrees(360.0 / n * i);
					var vec = front.scale(acc * t0);
					var e = holder.prepareDanmaku(t0, vec, YHDanmaku.Bullet.MENTOS, DyeColor.LIGHT_BLUE);
					e.mover = new RectMover(pos, vec, front.scale(-acc));
					e.afterExpiry = new IcePopsicle().setup(m, t2 + r.nextInt(dt), dr, termSpeed);
					holder.shoot(e);
				}
			}
		}

	}

	@SerialClass
	public static class IcePopsicle extends TrailAction {

		@SerialClass.SerialField
		private int count, life;
		@SerialClass.SerialField
		private double dr, speed;

		public IcePopsicle setup(int count, int life, double angle, double speed) {
			this.count = count;
			this.life = life;
			this.dr = angle;
			this.speed = speed;
			return this;
		}

		@Override
		public void execute(CardHolder holder, Vec3 pos, Vec3 dir) {
			var le = holder.target();
			if (le == null) return;
			var f1 = le.subtract(pos).normalize();
			var o1 = DanmakuHelper.getOrientation(f1);
			for (int j = 0; j < count; j++) {
				var vec = o1.rotateDegrees((j - (count - 1) * 0.5) * dr).scale(speed);
				var e = holder.prepareDanmaku(life, vec, YHDanmaku.Bullet.BALL, DyeColor.LIGHT_BLUE);
				e.setPos(pos);
				e.mover = new RectMover(pos, vec, Vec3.ZERO);
				holder.shoot(e);
			}
		}

	}


}
