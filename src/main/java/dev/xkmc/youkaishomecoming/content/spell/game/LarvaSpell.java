package dev.xkmc.youkaishomecoming.content.spell.game;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.DanmakuHelper;
import dev.xkmc.youkaishomecoming.content.entity.youkai.YoukaiEntity;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.ActualSpellCard;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.CardHolder;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.Ticker;
import dev.xkmc.youkaishomecoming.init.registrate.YHDanmaku;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.phys.Vec3;

@SerialClass
public class LarvaSpell extends ActualSpellCard {

	@Override
	public void tick(CardHolder holder) {
		var dir = holder.forward();
		var rand = holder.random();
		if (tick % 10 == 0) {
			int step = tick / 10 % 10;
			if (step <= 2) {
				double ver = (rand.nextDouble() * 2 - 1) * 45;
				if (holder.self() instanceof YoukaiEntity ye) {
					var te = ye.getTarget();
					if (te != null) {
						if (te.onGround()) {
							ver = rand.nextDouble() * 10 - 5;
						} else if (te.getDeltaMovement().y > 0.2) {
							ver = (rand.nextDouble() * 2 - 1) * 85;
						}
					}
				}
				var nor = DanmakuHelper.getOrientation(dir).asNormal();
				var n0 = nor.rotateDegrees(ver);
				var n1 = nor.rotateDegrees(-ver);
				addTicker(new Wings().init(dir, n0, 6, 20, 1));
				addTicker(new Wings().init(dir, n1, 6, 20, -1));
			} else if (step >= 5 && step <= 8) {
				var o = DanmakuHelper.getOrientation(dir);
				for (int i = 0; i < 5; i++) {
					var vel = 0.65 + i * 0.1;
					var vec = o.rotateDegrees((rand.nextDouble() * 2 - 1) * 3, (rand.nextDouble() * 2 - 1) * 3);
					int life = (int) (rand.nextInt(40, 80) / vel);
					var e = holder.prepareDanmaku(life, vec.scale(vel), YHDanmaku.Bullet.BUBBLE, DyeColor.LIME);
					holder.shoot(e);
				}
			}
		}
		super.tick(holder);
	}

	@SerialClass
	private static class Wings extends Ticker<LarvaSpell> {

		@SerialClass.SerialField
		private Vec3 dir = new Vec3(1, 0, 0);
		@SerialClass.SerialField
		private Vec3 nor = new Vec3(0, 1, 0);
		@SerialClass.SerialField
		private int n, dur, s;

		public Wings init(Vec3 dir, Vec3 nor, int n, int dur, int s) {
			this.dir = dir;
			this.nor = nor;
			this.n = n;
			this.dur = dur;
			this.s = s;
			return this;
		}

		@Override
		public boolean tick(CardHolder holder, LarvaSpell card) {
			super.tick(holder, card);
			double angle = (1 - Math.sqrt(1d * tick / dur)) * 180 * s;
			var r = holder.random();
			var o = DanmakuHelper.getOrientation(dir, nor);
			var dir = o.rotateDegrees(angle, r.nextInt(-5, 5));
			for (int i = 0; i < n; i++) {
				var vel = 0.3 + i * 0.1;
				int life = (int) (r.nextInt(40, 80) / vel);
				var e = holder.prepareDanmaku(life, dir.scale(vel), YHDanmaku.Bullet.BALL, DyeColor.LIME);
				holder.shoot(e);
			}
			return tick > dur;
		}

	}

}
