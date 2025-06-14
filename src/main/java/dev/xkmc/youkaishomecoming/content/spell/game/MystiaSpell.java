package dev.xkmc.youkaishomecoming.content.spell.game;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.DanmakuHelper;
import dev.xkmc.youkaishomecoming.content.spell.mover.CompositeMover;
import dev.xkmc.youkaishomecoming.content.spell.mover.RectMover;
import dev.xkmc.youkaishomecoming.content.spell.mover.ZeroMover;
import dev.xkmc.youkaishomecoming.content.spell.shooter.ShooterData;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.ActualSpellCard;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.CardHolder;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.Ticker;
import dev.xkmc.youkaishomecoming.init.registrate.YHDanmaku;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.phys.Vec3;

@SerialClass
public class MystiaSpell extends ActualSpellCard {

	@Override
	public void tick(CardHolder holder) {
		if (tick % 200 == 0) {
			addTicker(new SphereShooters());
		}
		super.tick(holder);
	}

	@SerialClass
	public static class SweepLarge extends Ticker<MystiaSpell> {

		@SerialClass.SerialField
		private Vec3 dir;

		@SerialClass.SerialField
		private int rot;

		public SweepLarge() {

		}

		public SweepLarge(int rot) {
			this.rot = rot;
		}

		@Override
		public boolean tick(CardHolder holder, MystiaSpell card) {
			if (dir == null) dir = holder.forward().normalize();
			int n = 15, dt = 2, dy = 3;
			if (tick % dt == 0) {
				var o = DanmakuHelper.getOrientation(dir);
				for (int i = 0; i < dy; i++) {
					double index = rot * (1d * tick / dt - (n - 0.5) / 2);
					var vec = o.rotateDegrees(index * 10, (i - (dy - 0.5) / 2) * 10);
					for (int j = 0; j < 10; j++) {
						int t0 = 15 - j / 2;
						int t1 = 30 - j - t0;
						double speed = 1.6;
						var e = holder.prepareDanmaku(80, vec, YHDanmaku.Bullet.MENTOS, DyeColor.LIME);
						var mover = new CompositeMover();
						mover.add(j + 1, new ZeroMover(vec, vec, j + 1));
						var p0 = holder.center();
						var v = vec.scale(speed);
						var a = vec.scale(-speed / t0);
						mover.add(t0, new RectMover(p0, v, a));
						var p1 = p0.add(v.scale(t0 * 0.5));
						mover.add(t1, new ZeroMover(vec, vec, t1));
						mover.add(20, new RectMover(p1, Vec3.ZERO, a.scale(-1)));
						mover.addEnd();
						e.mover = mover;
						holder.shoot(e);
					}
				}
			}
			super.tick(holder, card);
			return tick >= n * dt;
		}
	}
	@SerialClass
	public static class SphereShooters extends Ticker<MystiaSpell> {

		@SerialClass.SerialField
		private DyeColor color;

		@SerialClass.SerialField
		private int shooterCount = 0; // 当前已生成的shooter数量
		@SerialClass.SerialField
		private final int maxShooters = 32; // shooter总量上限

		@Override
		public boolean tick(CardHolder holder, MystiaSpell card) {
			super.tick(holder, card);
			var target = holder.target();
			if (target == null) return true;
			if (color == null) color = holder.random().nextBoolean() ? DyeColor.GREEN : DyeColor.CYAN;

			double speed = 0.5; // shooter的飞行速度
			int life = 60; // shooter的生命周期

			Vec3 center = holder.center();
			Vec3 direction = target.subtract(center).normalize();

			// 每10 tick生成一个shooter，直到达到上限
			if (tick % 3 == 0 && shooterCount < maxShooters) {
				// 生成球状分布的shooter
				double theta = Math.random() * Math.PI * 2; // 随机角度
				double phi = Math.acos(2 * Math.random() - 1); // 随机仰角
				double radius = 3.0; // 初始半径

				Vec3 offset = new Vec3(
						Math.sin(phi) * Math.cos(theta) * radius,
						Math.sin(phi) * Math.sin(theta) * radius,
						Math.cos(phi) * radius
				);
				Vec3 pos = center.add(offset);

				// 创建shooter
				var e = holder.prepareShooter(
						new ShooterData(40, holder.getDamage(YHDanmaku.Bullet.CIRCLE), life),
						new MystiaSpell.SphereShooters.SubSpell().init(life, color, direction)
				);
				e.setPos(pos);
				e.mover = new RectMover(pos, offset.normalize().scale(speed), Vec3.ZERO);
				holder.shoot(e);

				shooterCount++;
			}

			return shooterCount >= maxShooters && tick > 40;
		}

		@SerialClass
		public class SubSpell extends ActualSpellCard {

			@SerialClass.SerialField
			private int life;
			@SerialClass.SerialField
			private DyeColor color;
			@SerialClass.SerialField
			private Vec3 direction;

			public SubSpell init(int life, DyeColor color, Vec3 direction) {
				this.life = life;
				this.color = color;
				this.direction = direction;
				return this;
			}

			@Override
			public void tick(CardHolder holder) {
				super.tick(holder);
				if (direction == null) return;

				int bulletLife = 80;
				double bulletSpeed = 0.6;

				life--;
				if (life > 0) {
					// 每tick发射一个追踪玩家方向的弹幕
					var target = holder.target();
					if (target != null) {
						Vec3 bulletDirection = target.subtract(holder.center()).normalize();
						holder.shoot(holder.prepareDanmaku(bulletLife, bulletDirection.scale(bulletSpeed),
								YHDanmaku.Bullet.CIRCLE, color));
					}
				}
			}
		}
	}
}
