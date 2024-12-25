package dev.xkmc.youkaishomecoming.content.spell.game;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.DanmakuHelper;
import dev.xkmc.youkaishomecoming.content.spell.mover.AttachedMover;
import dev.xkmc.youkaishomecoming.content.spell.mover.RectMover;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.ActualSpellCard;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.CardHolder;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.Ticker;
import dev.xkmc.youkaishomecoming.init.registrate.YHDanmaku;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;

@SerialClass
public class MarisaSpell extends ActualSpellCard {

	@SerialClass.SerialField
	private int cooldown;

	@Override
	public void tick(CardHolder holder) {
		super.tick(holder);
		var target = holder.target();
		var vec = holder.targetVelocity();
		if (target == null) {
			cooldown = 0;
			return;
		}
		if (cooldown > 0) {
			cooldown--;
			return;
		}
		if (vec == null) return;
		var cen = holder.center();
		double dist = cen.distanceTo(target);
		boolean far = dist > 32;
		boolean close = dist < 8;
		boolean fast = vec.horizontalDistance() > 0.7;
		boolean r0 = holder.random().nextDouble() < 0.3;
		boolean r1 = holder.random().nextDouble() < 0.3;
		if (!r0 && (close || r1 || far && !fast)) {
			addTicker(new MasterSpark(target.subtract(cen).normalize()));
			cooldown = 100;
		} else if (far && fast || r0) {
			addTicker(new EarthLight());
			cooldown = 100;

		} else {
			addTicker(new BlackHole());
			cooldown = 100;

		}
	}

	@SerialClass
	public static class BlackHole extends Ticker<MarisaSpell> {

		private static final DyeColor[] COLOR = {
				DyeColor.RED, DyeColor.YELLOW, DyeColor.GREEN, DyeColor.CYAN, DyeColor.BLUE
		};
		private static final double W0 = 7, V0 = 0.2, W1 = -4, ACC = 0.05, VEC = 0.8;

		@Override
		public boolean tick(CardHolder holder, MarisaSpell card) {
			Vec3 cen = holder.center();
			var tar = holder.target();
			if (tar != null && tar.y > cen.y)
				cen = cen.add(0, tar.y - cen.y, 0);
			cen = cen.add(0, 24, 0);
			var o = DanmakuHelper.getOrientation(new Vec3(1, 0, 0));
			Vec3 acc = new Vec3(0, -ACC, 0);
			for (int i = 0; i < 5; i++) {
				for (int t = 0; t < 3; t++) {
					Vec3 pos = cen.add(o.rotateDegrees(tick * W0 + 72 * i + 24 * t).scale(tick * V0));
					for (int j = 0; j < 5; j++) {
						double v = 0.35 + t * 0.2 + Math.sin(tick * 0.1 + i) * 0.15;
						Vec3 dir = o.rotateDegrees(tick * W1 + 72 * j + 24 * t).scale(v * VEC);
						var e = holder.prepareDanmaku(40, dir, YHDanmaku.Bullet.BALL, COLOR[i]);
						e.setPos(pos);
						e.mover = new RectMover(pos, dir, acc);
						holder.shoot(e);
					}
				}
			}
			super.tick(holder, card);
			return tick > 100;
		}
	}

	@SerialClass
	public static class EarthLight extends Ticker<MarisaSpell> {

		@Override
		public boolean tick(CardHolder holder, MarisaSpell card) {
			var tar = holder.target();
			if (tar == null) return true;
			var rand = holder.random();
			for (int i = 0; i < 2; i++) {
				double x = rand.nextGaussian() * 10;
				double z = rand.nextGaussian() * 10;
				var pos = tar.add(x, 0, z);
				BlockPos air = BlockPos.containing(pos);
				BlockPos ground = holder.self().level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, air);
				if (ground.getY() < air.getY() - 20) {
					pos = pos.add(0, -20, 0);
				} else if (ground.getY() < air.getY()) {
					pos = new Vec3(pos.x, ground.getY() - 1, pos.z);
				}
				Vec3 dir = new Vec3(rand.nextGaussian(), 5, rand.nextGaussian()).normalize();
				var color = rand.nextBoolean() ? DyeColor.RED : DyeColor.BLUE;
				var e = holder.prepareLaser(60, pos, dir, 80,
						YHDanmaku.Laser.LASER, color);
				holder.shoot(e);
			}
			super.tick(holder, card);
			return tick > 100;
		}
	}

	@SerialClass
	public static class MasterSpark extends Ticker<MarisaSpell> {

		@SerialClass.SerialField
		private Vec3 target;

		public MasterSpark() {

		}

		public MasterSpark(Vec3 vec) {
			this.target = vec;
		}

		@Override
		public boolean tick(CardHolder holder, MarisaSpell card) {
			if (target == null) return true;
			var cen = holder.center();
			if (tick == 0) {
				var e = holder.prepareLaser(1, cen, target, 80,
						YHDanmaku.Laser.LASER, DyeColor.YELLOW);
				e.setupTime(20, 1, 1, 1);
				e.mover = new AttachedMover();
				holder.shoot(e);
			}
			if (tick > 20) {
				var tar = holder.target();
				if (tar != null) {
					double maxMove = 0.02;
					var db = tar.subtract(cen).normalize();
					double dist = target.distanceTo(db);
					double perc = dist < maxMove ? 1 : maxMove / dist;
					target = target.lerp(db, perc);
				}
				var rand = holder.random();
				var o = DanmakuHelper.getOrientation(target);
				for (int i = 0; i < 20; i++) {
					var pos = cen.add(target.scale(i * 1.4 + 2));
					double x = rand.nextDouble() * 30 - 15;
					double y = rand.nextDouble() * 30 - 15;
					var vec = o.rotateDegrees(x, y);
					var v = rand.nextDouble() + 2;
					var e = holder.prepareDanmaku(40, vec.scale(v),
							YHDanmaku.Bullet.MENTOS, DyeColor.WHITE);
					e.setPos(pos);
					holder.shoot(e);
				}

				for (int i = 0; i < 10; i++) {
					double x = rand.nextDouble() * 120 - 60;
					double y = rand.nextDouble() * 120 - 60;
					var vec = o.rotateDegrees(x, y);
					var v = rand.nextDouble() * 0.3 + 0.6;
					var e = holder.prepareDanmaku(40, vec.scale(v),
							YHDanmaku.Bullet.BALL, DyeColor.YELLOW);
					e.setPos(cen);
					holder.shoot(e);
				}
			}
			super.tick(holder, card);
			return tick > 100;
		}

	}


}
