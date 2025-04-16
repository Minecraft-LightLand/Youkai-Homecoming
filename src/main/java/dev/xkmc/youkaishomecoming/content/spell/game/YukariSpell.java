package dev.xkmc.youkaishomecoming.content.spell.game;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.DanmakuHelper;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.IYHDanmaku;
import dev.xkmc.youkaishomecoming.content.spell.mover.CompositeMover;
import dev.xkmc.youkaishomecoming.content.spell.mover.PolarMover;
import dev.xkmc.youkaishomecoming.content.spell.mover.RectMover;
import dev.xkmc.youkaishomecoming.content.spell.mover.ZeroMover;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.ActualSpellCard;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.CardHolder;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.Ticker;
import dev.xkmc.youkaishomecoming.init.data.YHDamageTypes;
import dev.xkmc.youkaishomecoming.init.registrate.YHDanmaku;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

@SerialClass
public class YukariSpell extends ActualSpellCard {

	@SerialClass.SerialField
	private int cooldown, vulnerable;

	@Override
	public DamageSource getDanmakuDamageSource(IYHDanmaku danmaku) {
		return YHDamageTypes.abyssal(danmaku);
	}

	@Override
	public void tick(CardHolder holder) {
		super.tick(holder);
		if (cooldown > 0) cooldown--;
		if (vulnerable > 0) vulnerable--;
		var target = holder.target();
		if (target == null) return;
		var pos = holder.center();
		var vel = holder.targetVelocity();
		double dist = target.distanceTo(pos);
		if (vel != null && dist > 40 && tick % 5 == 0) {
			if (vel.length() < 0.5 || vel.length() > 1) {
				var dst = target.add(target.subtract(pos).normalize().scale(32));
				if (teleport(holder.self(), dst)) {
					hidden(holder, dst, target);
					vulnerable = 5;
					return;
				}
			} else {
				var dst = target.add(target.subtract(pos).normalize().scale(-16));
				if (teleport(holder.self(), dst)) {
					hidden(holder, dst, target);
					vulnerable = 5;
					return;
				}
			}
			cooldown = 5;
			return;
		}
		if (cooldown > 0) return;
		if (dist < 20) {
			launchButterfly(holder, YHDanmaku.Bullet.BUTTERFLY, DyeColor.CYAN, 1);
			launchButterfly(holder, YHDanmaku.Bullet.BUTTERFLY, DyeColor.MAGENTA, -1);
			cooldown = 60;
			vulnerable = 20;
			return;
		}
		addTicker(new LaserAdder());
		cooldown = 120;
		vulnerable = 40;

	}

	@Override
	public void hurt(CardHolder holder, DamageSource source, float amount) {
		if (vulnerable <= 0) {
			//TODO immune
		}
		var target = holder.target();
		if (target == null) return;
		if (teleportRandom(holder, target)) {
			hidden(holder, holder.center(), target);
		}
	}

	private void hidden(CardHolder holder, Vec3 pos, Vec3 target) {
		cooldown = 20;
		hiddenImpl(holder, pos, target);
		double dist = pos.distanceTo(target);
		Vec3 dir = pos.subtract(target).normalize();
		var ori = DanmakuHelper.getOrientation(dir);
		for (int i = 1; i < 7; i++) {
			var dst = target.add(ori.rotateDegrees(360d / 7 * i).scale(dist));
			var ans = new DelayedHidden();
			ans.pos = dst;
			ans.tick = -10 * i;
			addTicker(ans);
		}
	}

	private void hiddenImpl(CardHolder holder, Vec3 pos, Vec3 target) {
		Vec3 dir = target.subtract(pos).normalize();
		var ori = DanmakuHelper.getOrientation(dir);
		for (int i = 0; i < 6; i++) {
			var vec = ori.rotateDegrees(60 * i);
			var laser = holder.prepareLaser(120, pos, vec, 80, YHDanmaku.Laser.LASER, DyeColor.MAGENTA);
			laser.setupTime(2, 8, 100, 10);
			holder.shoot(laser);
		}

		for (int i = 0; i < 6; i++) {
			var vec = ori.rotateDegrees(60 * i);
			var e = holder.prepareDanmaku(40, Vec3.ZERO, YHDanmaku.Bullet.BUBBLE, DyeColor.PURPLE);
			e.setPos(pos);
			e.mover = new RectMover(pos, vec.scale(2), Vec3.ZERO);
			holder.shoot(e);
		}

		for (int i = -3; i <= 3; i++) {
			for (int k = -2; k <= 2; k++) {
				var vec = ori.rotateDegrees(10 * i, k * 10);
				for (int j = 0; j < 3; j++) {
					var e = holder.prepareDanmaku(40, Vec3.ZERO, YHDanmaku.Bullet.BUTTERFLY, DyeColor.PURPLE);
					e.setPos(pos);
					e.mover = new RectMover(pos, vec.scale(1.4 + j * 0.2), Vec3.ZERO);
					holder.shoot(e);
				}
			}
		}
	}

	private void launchButterfly(CardHolder holder, YHDanmaku.Bullet type, DyeColor color, int dire) {
		var r = holder.random();
		var pos = holder.center();
		DanmakuHelper.Orientation o0 = DanmakuHelper.getOrientation(holder.forward());

		int n = 100;
		int mrange = 12, vrange = 8;
		int t0 = 40;
		int t1 = 10;
		double tvr = 0.8;
		int t2 = 10;
		int t3 = 30;
		int t4 = 40;
		double avar = Math.PI / 4;

		float wvr = (float) (tvr / mrange) * dire;
		int total = t0 + t1 + t2 + t3 + t4;
		for (int i = 0; i < n; i++) {
			double a0 = 2 * Math.PI / n * i;
			double ver = (r.nextDouble() * 2 - 1) * avar;
			Vec3 a1 = o0.rotate(a0, ver);
			Vec3 vn = o0.rotate(a0, ver + Math.PI / 2);
			float range = mrange + vrange * (float) (r.nextDouble() * 2 - 1);
			float va = range * 2 / (t0 * t0);
			float vr = va * t0;
			var mover = new CompositeMover();
			var a2 = PolarMover.ofPlane(pos, a1, vn)
					.radial(range, 0, 0).angular(0, wvr, 0).dir(0)
					.scale(100).normalize();
			var polar0 = PolarMover.ofPlane(pos, a1, vn)
					.radial(range, 0, 0).angular(0, 0, wvr / t2);
			var polar1 = polar0.copy().atTime(t2).clearAccel();
			var rect = polar1.copy().atTime(t3).toRect();
			var v1 = a1.scale(vr);
			mover.add(t0, new RectMover(pos, v1, a1.scale(-va)));
			mover.add(t1, new ZeroMover(a1, a2, t1));
			mover.add(t2, polar0);
			mover.add(t3, polar1);
			mover.add(t4, rect);
			var danmaku = holder.prepareDanmaku(total + r.nextInt(40), v1, type, color);
			danmaku.mover = mover;
			holder.shoot(danmaku);
		}
	}

	private static boolean teleportRandom(CardHolder holder, Vec3 target) {
		double dist = holder.center().distanceTo(target);
		var r = holder.random();
		for (int i = 0; i < 16; i++) {
			Vec3 dir = new Vec3(r.nextGaussian(), Math.abs(r.nextGaussian()), r.nextGaussian());
			Vec3 pos = dir.scale(Math.min(32, dist * (0.8 + r.nextDouble() * 0.4))).add(target);
			if (teleport(holder.self(), pos)) {
				return true;
			}
		}
		return false;
	}

	private static boolean teleport(LivingEntity mob, Vec3 target) {
		Vec3 old = mob.position();
		mob.teleportTo(target.x(), target.y(), target.z());
		if (!mob.level().noCollision(mob)) {
			mob.teleportTo(old.x(), old.y(), old.z());
			return false;
		}
		mob.level().broadcastEntityEvent(mob, EntityEvent.TELEPORT);
		mob.level().gameEvent(GameEvent.TELEPORT, mob.position(), GameEvent.Context.of(mob));
		if (!mob.isSilent()) {
			mob.level().playSound(null, mob.xo, mob.yo, mob.zo, SoundEvents.ENDERMAN_TELEPORT, mob.getSoundSource(), 1.0F, 1.0F);
			mob.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
		}
		return true;
	}

	@SerialClass
	public static class LaserAdder extends Ticker<YukariSpell> {

		@SerialClass.SerialField
		private Vec3 pos, forward;

		@Override
		public boolean tick(CardHolder holder, YukariSpell card) {
			if (tick == 0) {
				forward = holder.forward();
				forward = forward.multiply(1, 0.5, 1).normalize();
				pos = holder.center();
			}
			var ori = DanmakuHelper.getOrientation(forward);
			var r = holder.random();
			addLaserBeams(holder, pos, ori.rotateDegrees(-45), 1 + tick * 0.5, r.nextDouble(), DyeColor.RED);
			addLaserBeams(holder, pos, ori.rotateDegrees(45), 1 + tick * 0.5, r.nextDouble(), DyeColor.BLUE);
			if (tick == 20) {
				shootGroup(holder, DyeColor.RED);
			}
			if (tick == 40) {
				shootGroup(holder, DyeColor.BLUE);
			}
			super.tick(holder, card);
			return tick > 120;
		}

		private void shootGroup(CardHolder holder, DyeColor color) {
			double speed = 1;
			double dv = 0.5;
			double dev = 30;
			int n0 = 5;
			int n1 = 50;
			int life = 60;
			int dl = 20;

			var forward = holder.forward();
			var rand = holder.random();
			var ori = DanmakuHelper.getOrientation(forward);

			for (int i = 0; i < n0; i++) {
				double d0 = (rand.nextDouble() * 2 - 1) * dev * i / n0;
				double d1 = (rand.nextDouble() * 2 - 1) * dev * i / n0;
				double sp = speed - dv / n0 * i;
				var vec = ori.rotateDegrees(d0, d1).scale(sp);
				int lf = life + rand.nextInt(dl);
				holder.shoot(holder.prepareDanmaku(lf, vec, YHDanmaku.Bullet.BUBBLE, color));
			}
			for (int i = 0; i < n1; i++) {
				double d0 = (rand.nextDouble() * 2 - 1) * dev * i / n1;
				double d1 = (rand.nextDouble() * 2 - 1) * dev * i / n1;
				double sp = speed - dv / n1 * i;
				var vec = ori.rotateDegrees(d0, d1).scale(sp);
				int lf = life + rand.nextInt(dl);
				holder.shoot(holder.prepareDanmaku(lf, vec, YHDanmaku.Bullet.MENTOS, color));
			}
		}

		private void addLaserBeams(CardHolder holder, Vec3 pos, Vec3 dir, double step, double r, DyeColor color) {
			Vec3 p = pos.add(dir.scale(step));
			var ori = DanmakuHelper.getOrientation(dir).rotate(Math.PI / 2, r * Math.PI * 2);
			holder.shoot(holder.prepareLaser(100, p, ori, 80, YHDanmaku.Laser.LASER, color));
		}

	}

	@SerialClass
	public static class DelayedHidden extends Ticker<YukariSpell> {

		@SerialClass.SerialField
		public Vec3 pos = Vec3.ZERO;

		@Override
		public boolean tick(CardHolder holder, YukariSpell card) {
			if (tick == 0) {
				var target = holder.target();
				if (target != null) {
					card.hiddenImpl(holder, pos, target);
				}
			}
			super.tick(holder, card);
			return tick > 0;
		}
	}

}
