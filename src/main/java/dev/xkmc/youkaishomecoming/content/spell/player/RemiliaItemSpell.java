package dev.xkmc.youkaishomecoming.content.spell.player;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.DanmakuHelper;
import dev.xkmc.youkaishomecoming.content.spell.item.ItemSpell;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.CardHolder;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.Ticker;
import dev.xkmc.youkaishomecoming.init.registrate.YHDanmaku;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

@SerialClass
public class RemiliaItemSpell extends ItemSpell {

	@Override
	public void start(Player player, @Nullable LivingEntity target) {
		super.start(player, target);
		var nor = DanmakuHelper.getOrientation(dir).normal();
		addTicker(new Sweep().init(dir, nor, -90,
				15, 8, 1.6, 2.4,
				20, 15, 80
		));
	}

	@SerialClass
	public static class Sweep extends Ticker<RemiliaItemSpell> {

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
		public boolean tick(CardHolder holder, RemiliaItemSpell card) {
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


}
