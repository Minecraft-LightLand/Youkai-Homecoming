package dev.xkmc.youkaishomecoming.content.spell.game.youmu;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.DanmakuHelper;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.ActualSpellCard;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.CardHolder;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.Ticker;
import dev.xkmc.youkaishomecoming.init.registrate.YHDanmaku;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.phys.Vec3;

@SerialClass
public class YoumuSlash extends ActualSpellCard {

	@Override
	public void tick(CardHolder holder) {
		super.tick(holder);
		int interval = 40;
		Vec3 target = holder.target();
		if (tick % interval == 0 && target != null) {
			int index = tick / interval;
			boolean first = index % 2 == 0;
			double angle = holder.random().nextDouble();
			Vec3 start = DanmakuHelper.getOrientation(target).asNormal().rotateDegrees(angle * 30 - 15 + 90);
			addTicker(new Slash(first ? 3 : 4, first ? DyeColor.BLUE : DyeColor.RED, holder.center(), start, target));
		}
	}

	@SerialClass
	public static class Slash extends Ticker<YoumuSlash> {

		@SerialClass.SerialField
		public int points;

		@SerialClass.SerialField
		public DyeColor color;

		@SerialClass.SerialField
		public Vec3 ori, start, target;

		public Slash() {
		}

		public Slash(int points, DyeColor color, Vec3 ori, Vec3 start, Vec3 target) {
			this.points = points;
			this.color = color;
			this.ori = ori;
			this.start = start;
			this.target = target;
		}

		@Override
		public boolean tick(CardHolder holder, YoumuSlash card) {
			int length = 8;
			int dur = 8;
			int dist = 60;
			int dl = 10;
			int space = 4;
			double speed = 0.6;
			double dv = 0.1;
			int n = 3;
			var r = holder.random();
			for (int i = 0; i < points; i++) {
				double dt = 1d * i / points;
				double len = ((tick - dt) / dur * 2 - 1) * length;
				Vec3 pos = ori.add(target.subtract(ori).normalize().scale(-1)).add(start.scale(len));
				double offset = space * (i - (points - 1) * 0.5);
				Vec3 tar = target.add(start.scale(offset));
				for (int j = 0; j < n; j++) {
					double sp = speed + j * dv;
					Vec3 vel = tar.subtract(pos).normalize().scale(sp);
					Vec3 actPos = pos.add(vel.scale(dt));
					int lf = (int) Math.round(dist / sp) + r.nextInt(dl);
					var bullet = holder.prepareDanmaku(lf, vel, YHDanmaku.Bullet.CIRCLE, color);
					bullet.setPos(actPos);
					holder.shoot(bullet);
				}
			}

			super.tick(holder, card);
			return tick > dur;
		}

	}

}
