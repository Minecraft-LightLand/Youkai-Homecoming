package dev.xkmc.youkaishomecoming.content.spell.game;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.DanmakuHelper;
import dev.xkmc.youkaishomecoming.content.spell.mover.RectMover;
import dev.xkmc.youkaishomecoming.content.spell.shooter.ShooterData;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.ActualSpellCard;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.CardHolder;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.Ticker;
import dev.xkmc.youkaishomecoming.init.registrate.YHDanmaku;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.phys.Vec3;

@SerialClass
public class KisinSpell extends ActualSpellCard {

	@SerialClass.SerialField
	private int cooldown;

	@Override
	public void tick(CardHolder holder) {
		super.tick(holder);
		if (cooldown > 0) cooldown--;
		if (cooldown > 0) return;
		var target = holder.target();
		if (target == null) return;
		var center = holder.center();
		double dist = center.distanceTo(target);
		if (dist < 10) {
			addTicker(new SummonNear());
			cooldown = 60;
			return;
		}
		if (dist < 40 && holder.random().nextBoolean()) {
			addTicker(new Wing());
			cooldown = 60;
			return;
		}
		addTicker(new SummonFar());
		cooldown = 80;
	}

	@SerialClass
	public static class SummonNear extends Ticker<KisinSpell> {

		@Override
		public boolean tick(CardHolder holder, KisinSpell card) {
			super.tick(holder, card);
			var target = holder.target();
			if (target == null) return true;

			int life = 60;

			if (tick % 2 == 0) {
				var o = DanmakuHelper.getOrientation(target.subtract(holder.center()).normalize());
				var x = holder.random().nextDouble() * 60 - 30;
				var y = holder.random().nextDouble() * 60 - 30;
				var dir = o.rotateDegrees(x, y);
				var e = holder.prepareShooter(
						new ShooterData(40, holder.getDamage(YHDanmaku.Bullet.CIRCLE), life),
						new SubSpell().init(dir, life, tick / 2 % 2 == 0 ? DyeColor.YELLOW : DyeColor.ORANGE)
				);
				e.mover = new RectMover(holder.center(), dir.scale(0.5), Vec3.ZERO);
				holder.shoot(e);
			}
			return tick > 40;
		}

		@SerialClass
		public class SubSpell extends ActualSpellCard {

			@SerialClass.SerialField
			private Vec3 dir;
			@SerialClass.SerialField
			private int life;
			@SerialClass.SerialField
			private DyeColor color;

			public SubSpell init(Vec3 dir, int life, DyeColor color) {
				this.dir = dir;
				this.life = life;
				this.color = color;
				return this;
			}

			@Override
			public void tick(CardHolder holder) {
				super.tick(holder);

				int bulletLife = 40;
				double forward = 0.8;
				double backward = -0.3;

				life--;
				if (life > 0) {
					holder.shoot(holder.prepareDanmaku(bulletLife, dir.scale(forward),
							YHDanmaku.Bullet.CIRCLE, color));
					holder.shoot(holder.prepareDanmaku(bulletLife, dir.scale(backward),
							YHDanmaku.Bullet.CIRCLE, color));
				}
			}

		}

	}

	@SerialClass
	public static class SummonFar extends Ticker<KisinSpell> {

		@SerialClass.SerialField
		private DyeColor color;

		@Override
		public boolean tick(CardHolder holder, KisinSpell card) {
			super.tick(holder, card);
			var target = holder.target();
			if (target == null) return true;
			if (color == null) color = holder.random().nextBoolean() ? DyeColor.MAGENTA : DyeColor.BLUE;

			int life = 40;
			double speed = 0.5;

			var x = holder.random().nextGaussian() * 20;
			var z = holder.random().nextGaussian() * 20;
			var y = 20;
			var pos = target.add(x, y, z);
			var e = holder.prepareShooter(
					new ShooterData(40, holder.getDamage(YHDanmaku.Bullet.CIRCLE), life),
					new SubSpell().init(life, color, card)
			);
			e.setPos(pos);
			e.mover = new RectMover(pos, new Vec3(0, -speed, 0), Vec3.ZERO);
			holder.shoot(e);

			return tick > 40;
		}

		@SerialClass
		public class SubSpell extends ActualSpellCard {

			@SerialClass.SerialField
			private int life;
			@SerialClass.SerialField
			private DyeColor color;

			private KisinSpell root;

			public SubSpell init(int life, DyeColor color, KisinSpell root) {
				this.life = life;
				this.color = color;
				this.root = root;
				return this;
			}

			@Override
			public void tick(CardHolder holder) {
				super.tick(holder);
				if (root == null) return;
				int bulletLife = 40;
				life--;
				if (life > 0) {
					root.addTicker(new Delayed().init(holder.center(), bulletLife, color));
				}
			}

			@SerialClass
			public static class Delayed extends Ticker<KisinSpell> {

				@SerialClass.SerialField
				private int life;
				@SerialClass.SerialField
				private Vec3 pos;
				@SerialClass.SerialField
				private DyeColor color;

				public Delayed init(Vec3 pos, int life, DyeColor color) {
					this.pos = pos;
					this.life = life;
					this.color = color;
					return this;
				}

				@Override
				public boolean tick(CardHolder holder, KisinSpell card) {
					var target = holder.target();
					if (target == null) return true;
					int l = life;
					double speed = 1;
					if (tick == 0) {
						var e = holder.prepareDanmaku(l, Vec3.ZERO,
								YHDanmaku.Bullet.CIRCLE, color);
						e.setPos(pos);
						holder.shoot(e);
					} else if (tick == life) {
						var dir = target.subtract(pos).normalize();
						var e = holder.prepareDanmaku(l, dir.scale(speed),
								YHDanmaku.Bullet.CIRCLE, color);
						e.setPos(pos);
						holder.shoot(e);
						return true;
					}
					tick++;
					return false;
				}
			}

		}

	}

	@SerialClass
	public static class Wing extends Ticker<KisinSpell> {

		@SerialClass.SerialField
		public Vec3 pos, dir;

		@Override
		public boolean tick(CardHolder holder, KisinSpell card) {
			var target = holder.target();
			if (target == null) return true;
			if (pos == null || dir == null) {
				pos = holder.center();
				dir = target.subtract(pos).normalize();
				int s = holder.random().nextBoolean() ? 1 : -1;
				dir = DanmakuHelper.getOrientation(dir).rotateDegrees(s * 90);
			}

			double v0 = 0.7;
			int life = 80;

			Vec3 p = pos.add(dir.scale(v0 * tick)).add(
					holder.random().nextGaussian(),
					holder.random().nextGaussian(),
					holder.random().nextGaussian()
			);
			var d0 = target.subtract(p).normalize();
			var x = holder.random().nextDouble() * 20 - 10;
			var y = holder.random().nextDouble() * 20 - 10;
			for (int i = -1; i <= 1; i++) {
				var d1 = DanmakuHelper.getOrientation(d0).rotateDegrees(x + i * 30, y);
				var l = holder.prepareLaser(life, p, d1, 80,
						YHDanmaku.Laser.LASER, DyeColor.YELLOW);
				l.setupTime(5, 5, life, 10);
				holder.shoot(l);
			}

			tick++;
			return tick > 40;
		}

	}

}


