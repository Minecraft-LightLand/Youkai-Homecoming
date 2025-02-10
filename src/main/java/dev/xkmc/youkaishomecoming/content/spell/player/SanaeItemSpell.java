package dev.xkmc.youkaishomecoming.content.spell.player;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.DanmakuHelper;
import dev.xkmc.youkaishomecoming.content.spell.item.ItemSpell;
import dev.xkmc.youkaishomecoming.content.spell.mover.CompositeMover;
import dev.xkmc.youkaishomecoming.content.spell.mover.RectMover;
import dev.xkmc.youkaishomecoming.content.spell.mover.ZeroMover;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.CardHolder;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.Ticker;
import dev.xkmc.youkaishomecoming.init.registrate.YHDanmaku;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.phys.Vec3;

@SerialClass
public class SanaeItemSpell extends ItemSpell {

	@SerialClass.SerialField
	private int tick = 0;
	@SerialClass.SerialField
	private double a0;

	@Override
	public boolean tick(Player player) {
		if (tick == 0) {
			a0 = player.getRandom().nextDouble() * 360;
			addTicker(new Star(a0, DyeColor.LIME));
		}
		if (tick == 10) {
			addTicker(new Star(a0 + 120, DyeColor.RED));
		}
		if (tick == 20) {
			addTicker(new Star(a0 + 240, DyeColor.BLUE));
		}
		tick++;
		return super.tick(player);
	}

	@SerialClass
	public static class Star extends Ticker<SanaeItemSpell> {

		@SerialClass.SerialField
		DyeColor color = DyeColor.LIME;
		@SerialClass.SerialField
		private Vec3 dir;
		@SerialClass.SerialField
		private double a0;

		public Star() {

		}

		public Star(double a0, DyeColor color) {
			this.a0 = a0;
			this.color = color;
		}

		@Override
		public boolean tick(CardHolder holder, SanaeItemSpell card) {
			if (dir == null) {
				dir = holder.forward();
			}
			int n = 20, m = 2, t0 = 10;
			double accel = 0.3, dist = 4;
			var o = DanmakuHelper.getOrientation(dir);
			var pos = holder.center().add(0, -0.3, 0);
			for (int i = 0; i < 5; i++) {
				var p0 = o.rotateDegrees(a0 + i * 360d * 2 / 5);
				var p1 = o.rotateDegrees(a0 + (i + 1) * 360d * 2 / 5);
				for (int j = 0; j < m; j++) {
					var d = p0.lerp(p1, (tick + 1d * j / m) / n);
					double acc = d.length() * accel;
					int life = Math.min(50, (int) ((100 - acc * (t0 * t0 * 0.5 + dist / accel)) / (acc * t0)));
					var e = holder.prepareDanmaku(
							life + t0 + 5 + n - tick, Vec3.ZERO,
							YHDanmaku.Bullet.SPARK, color
					);
					var p = pos.add(d.scale(dist));
					e.setPos(p);
					CompositeMover mover = new CompositeMover();
					mover.add(n - tick + 5, new ZeroMover(d, d, n - tick + 5));
					mover.add(t0, new RectMover(p, Vec3.ZERO, d.scale(accel)));
					e.mover = mover;
					holder.shoot(e);
				}
			}
			super.tick(holder, card);
			return tick >= n;
		}

	}

}
