package dev.xkmc.youkaishomecoming.compat.touhoulittlemaid.spell;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.DanmakuHelper;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.ActualSpellCard;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.CardHolder;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.Ticker;
import dev.xkmc.youkaishomecoming.init.data.YHModConfig;
import dev.xkmc.youkaishomecoming.init.registrate.YHDanmaku;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.phys.Vec3;

@SerialClass
public class MediumFairySpell extends ActualSpellCard {

	@SerialClass.SerialField
	protected DyeColor primary = DyeColor.RED, secondary = DyeColor.BLUE;

	public MediumFairySpell init(DyeColor col, DyeColor sec) {
		this.primary = col;
		this.secondary = sec;
		return this;
	}

	@Override
	public void tick(CardHolder holder) {
		if (tick % 10 == 0) {
			int step = tick / 10;
			int round = step / 2 % 7;
			if (step % 2 == 0 && (round == 0 || round == 3)) {
				int n = YHModConfig.COMMON.smallFairyStrength.get();
				int dur = 5 * (n + 2);
				double angle = 180d / dur;
				addTicker(new Round().init(primary, YHDanmaku.Bullet.BALL, holder.forward(),
						round == 0 ? angle : -angle, 0.3 + n * 0.1, dur));
			} else if (round == 1 || round == 4) {
				var r = holder.random();
				var o = DanmakuHelper.getOrientation(holder.forward());
				int n = YHModConfig.COMMON.smallFairyStrength.get();
				for (int i = -n; i <= n; i++) {
					for (int j = 0; j <= n; j++) {
						var dir = o.rotateDegrees(i * 15, r.nextInt(-3, 3));
						double v = 0.5 + j * 0.3 - n * 0.1;
						var vel = dir.scale(v);
						int life = (int) (40 / v * (1 + r.nextDouble() * 0.5));
						var e = holder.prepareDanmaku(life, vel,
								YHDanmaku.Bullet.CIRCLE, secondary);
						holder.shoot(e);
					}
				}
			}
		}
		super.tick(holder);
	}

	@SerialClass
	public static class Round extends Ticker<MediumFairySpell> {

		@SerialClass.SerialField
		private DyeColor color = DyeColor.RED;

		@SerialClass.SerialField
		private YHDanmaku.Bullet type = YHDanmaku.Bullet.BALL;

		@SerialClass.SerialField
		private double w = 3, v = 0.5;

		@SerialClass.SerialField
		private int duration;

		@SerialClass.SerialField
		private Vec3 dir = new Vec3(1, 0, 0);

		public Round init(DyeColor color, YHDanmaku.Bullet type, Vec3 dir, double w, double v, int duration) {
			this.color = color;
			this.type = type;
			this.dir = dir;
			this.w = w;
			this.v = v;
			this.duration = duration;
			return this;
		}

		@Override
		public boolean tick(CardHolder holder, MediumFairySpell card) {
			var r = holder.random();
			var o = DanmakuHelper.getOrientation(dir);
			double hor = (tick - duration / 2d) * w;
			var dir = o.rotateDegrees(hor, r.nextInt(-3, 3));
			var vel = dir.scale(v);
			int life = (int) (40 / v * (1 + r.nextDouble() * 0.5));
			var e = holder.prepareDanmaku(life, vel, type, color);
			holder.shoot(e);
			super.tick(holder, card);
			return tick >= duration;
		}
	}

}
