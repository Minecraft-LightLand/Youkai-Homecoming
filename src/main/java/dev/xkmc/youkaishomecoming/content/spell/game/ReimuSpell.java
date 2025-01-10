package dev.xkmc.youkaishomecoming.content.spell.game;

import dev.xkmc.fastprojectileapi.entity.ProjectileMovement;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.DanmakuHelper;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.IYHDanmaku;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.ItemDanmakuEntity;
import dev.xkmc.youkaishomecoming.content.item.danmaku.DanmakuItem;
import dev.xkmc.youkaishomecoming.content.spell.mover.RectMover;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.ActualSpellCard;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.CardHolder;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.TargetTracker;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.Ticker;
import dev.xkmc.youkaishomecoming.init.data.YHDamageTypes;
import dev.xkmc.youkaishomecoming.init.registrate.YHDanmaku;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

@SerialClass
public class ReimuSpell extends ActualSpellCard {

	@SerialClass.SerialField
	private boolean border, abyss;

	@SerialClass.SerialField
	private TargetTracker tracker = new TargetTracker();

	@Override
	public void tick(CardHolder holder) {
		super.tick(holder);
		int interval = 10;
		if (tick > 2400) {
			abyss = true;
		}
		var target = holder.target();
		if (target == null) return;
		tracker.tick(tick, holder);
		var dist = holder.center().distanceTo(target);
		if (tick % interval == 0) {
			int step = tick / interval % 5;
			if (step < 3) {
				shoot(holder, dist);
			} else if (dist > 40) {
				intercept(holder, target);
			} else if (step == 3 && abyss && tracker.flyTime() > 20) {
				var dir = target.subtract(holder.center()).normalize();
				var ori = DanmakuHelper.getOrientation(dir).rotateDegrees(holder.random().nextDouble() * 120 + 30, 0);
				var sec = DanmakuHelper.getOrientation(ori).rotateDegrees(90, holder.random().nextDouble() * 120 - 60);
				sequence(holder, dist, ori, sec, 10, -0.6, 1, 5, 10, 2d, YHDanmaku.Bullet.BALL);
			}
		}
		if (border)
			border(holder, dist);
	}

	@Override
	public void reset() {
		super.reset();
		border = false;
		abyss = false;
		tracker = new TargetTracker();
	}

	private void shoot(CardHolder holder, double dist) {
		var target = holder.target();
		var vel = tracker.vel();
		if (target == null) return;
		var ans = new StateChange();
		double perc = Mth.clamp((dist - 16) / 24, 0, 1);
		ans.r0 = (int) Mth.lerp(perc, 8, 20);
		ans.r1 = (int) Mth.lerp(perc, 6, 18);
		ans.t0 = (int) Mth.lerp(perc, 20, 10);
		ans.t1 = (int) Mth.lerp(perc, 20, 10);
		ans.termSpeed = (int) Mth.lerp(perc, 1, 3);
		if (abyss) ans.color = DyeColor.BLUE;

		var diff = target.subtract(holder.center());
		var r = holder.random();
		ans.pos = holder.center();
		if (Math.abs(diff.y) > 6 && vel.length() > 0.2) {
			var a = diff.normalize();
			var b = diff.add(vel.scale(20)).normalize();
			var c = a.cross(b).normalize();
			var ori = DanmakuHelper.getOrientation(a, c);
			ans.init = ori.rotateDegrees(90, r.nextDouble() * 360);
			ans.normal = a.cross(ans.init);
		} else {
			var dir = diff.normalize();
			var tilt = 60 * r.nextDouble() - 30;
			ans.init = DanmakuHelper.getOrientation(dir).rotateDegrees(90, tilt);
			ans.normal = dir.cross(ans.init);
		}

		addTicker(ans);
	}

	private void intercept(CardHolder holder, Vec3 target) {
		var dir = holder.targetVelocity();
		if (dir == null) return;
		double speed = dir.length();
		dir = dir.normalize();
		if (dir.length() < 0.1) return;
		double dist = Math.max(24, speed * 20);
		var dst = target.add(dir.scale(dist));
		if (dst.y < holder.self().level().getMinBuildHeight()) {
			dst = new Vec3(dst.x, target.y, dst.z);
		}
		if (!teleport(holder.self(), dst)) return;
		if (speed < 0.5) return;
		Intercept ans = new Intercept();
		ans.vec = target;
		ans.dir = dir;
		addTicker(ans);
	}

	private void border(CardHolder holder, double dist) {
		var forward = holder.forward();
		var ori = DanmakuHelper.getOrientation(forward);
		double angle = ProjectileMovement.of(forward).rot().y * Mth.RAD_TO_DEG;
		double speed = Mth.clamp(dist / 30, 1.5, 3);
		for (int i = 0; i < 8; i++) {
			var dir = ori.rotateDegrees(360d / 8 * i - angle);
			var e = holder.prepareDanmaku(40, dir.scale(speed), YHDanmaku.Bullet.BALL, DyeColor.YELLOW);
			holder.shoot(e);
		}
	}

	@Override
	public DamageSource getDanmakuDamageSource(IYHDanmaku danmaku) {
		if (danmaku instanceof ItemDanmakuEntity e) {
			if (e.getItem().getItem() instanceof DanmakuItem i) {
				if (i.color == DyeColor.BLUE || i.color == DyeColor.YELLOW) {
					return YHDamageTypes.abyssal(danmaku);
				}
			}
		}
		return super.getDanmakuDamageSource(danmaku);
	}

	@Override
	public void hurt(CardHolder holder, DamageSource source, float amount) {
		super.hurt(holder, source, amount);
		if (source.getEntity() != null) border = true;
		float hp = holder.self().getHealth(), mhp = holder.self().getMaxHealth();
		if (hp < mhp / 2) {
			abyss = true;
		}
		var target = holder.target();
		if (target == null) return;
		if (abyss) {
			var dist = holder.center().distanceTo(target);
			var dir = target.subtract(holder.center()).normalize();
			Vec3 ori, sec;
			int n = 6, m = 5;
			for (int i = 0; i < 3; i++) {
				ori = DanmakuHelper.getOrientation(dir).rotateDegrees(120 * (i + 0.5), 0);
				sec = DanmakuHelper.getOrientation(ori).rotateDegrees(90, -45);
				sequence(holder, dist, ori, sec, 6, 0, 2, n, m, 360d / n / m, YHDanmaku.Bullet.BUBBLE);
			}
		} else {
			var dist = holder.center().distanceTo(target);
			var dir = target.subtract(holder.center()).normalize();
			var ori = DanmakuHelper.getOrientation(dir).rotateDegrees(holder.random().nextDouble() * 120 + 30, 0);
			var sec = DanmakuHelper.getOrientation(ori).rotateDegrees(90, holder.random().nextDouble() * 60 - 30);
			sequence(holder, dist, ori, sec, 6, 0, 2, 8, 5, 360d / 8 / 5, YHDanmaku.Bullet.BUBBLE);
		}
	}

	private void sequence(
			CardHolder holder, double dist, Vec3 dir, Vec3 ori,
			double base, double bv, int delay, int n, int step, double angle, YHDanmaku.Bullet bullet
	) {
		var le = holder.target();
		if (le == null) return;
		var r = holder.random();
		var normal = dir.cross(ori);
		int s = r.nextDouble() < 0.5 ? -1 : 1;
		for (int i = 0; i <= step; i++) {
			var ans = new StateChange();
			double perc = Mth.clamp((dist - 16) / 24, 0, 1);
			double b = base + bv * i;
			ans.r0 = (int) Mth.lerp(perc, b * 4, b * 10);
			ans.r1 = (int) Mth.lerp(perc, b * 3, b * 8);
			ans.t0 = (int) Mth.lerp(perc, 20, 10);
			ans.t1 = (int) Mth.lerp(perc, 20, 10);
			ans.termSpeed = (int) Mth.lerp(perc, 1, 3);
			ans.n = n;
			ans.bullet = bullet;
			ans.pos = holder.center();
			ans.init = DanmakuHelper.getOrientation(ori, normal).rotateDegrees(s * (i - 2) * angle);
			ans.normal = normal;
			ans.tick = -i * delay;
			if (abyss) ans.color = DyeColor.BLUE;
			addTicker(ans);
		}
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
	public static class StateChange extends Ticker<ReimuSpell> {

		@SerialClass.SerialField
		private Vec3 pos, init, normal, target1;
		@SerialClass.SerialField
		private int r0 = 8, r1 = 6, n = 20;
		@SerialClass.SerialField
		private int t0 = 20, t1 = 20, t2 = 40, dt = 20;
		@SerialClass.SerialField
		private double termSpeed = 1;
		@SerialClass.SerialField
		private YHDanmaku.Bullet bullet = YHDanmaku.Bullet.CIRCLE;
		@SerialClass.SerialField
		private DyeColor color = DyeColor.RED;

		@Override
		public boolean tick(CardHolder holder, ReimuSpell card) {
			step(holder);
			super.tick(holder, card);
			return tick > t0 + t1 || holder.target() == null;
		}

		private void step(CardHolder holder) {
			var le = holder.target();
			if (le == null) return;
			var r = holder.random();
			if (init == null) {
				pos = holder.center();
				var dir = le.subtract(holder.center()).normalize();
				init = DanmakuHelper.getOrientation(dir).rotateDegrees(90, 60 * r.nextDouble() - 30);
				normal = dir.cross(init);
			}
			if (tick < 0) return;
			if (tick == 0) {
				DanmakuHelper.Orientation o0 = DanmakuHelper.getOrientation(init, normal);
				double acc = r0 * 2d / t0 / t0;
				for (int i = 0; i < n; i++) {
					var front = o0.rotateDegrees(360.0 / n * i);
					var vec = front.scale(acc * t0);
					var e = holder.prepareDanmaku(t0, vec, bullet, DyeColor.LIGHT_GRAY);
					e.mover = new RectMover(pos, vec, front.scale(-acc));
					holder.shoot(e);
				}
			}
			if (tick == t0) {
				target1 = le;
				DanmakuHelper.Orientation o0 = DanmakuHelper.getOrientation(init, normal);
				double acc = r1 * 2d / t1 / t1;
				for (int i = 0; i < n; i++) {
					var f0 = o0.rotateDegrees(360.0 / n * i);
					var p0 = pos.add(f0.scale(r0));
					var f1 = target1.subtract(p0).normalize();
					var vec = f1.scale(acc * t1);
					var e = holder.prepareDanmaku(t1, vec, bullet, DyeColor.PURPLE);
					e.setPos(p0);
					e.mover = new RectMover(p0, vec, f1.scale(-acc));
					holder.shoot(e);
				}
			}
			if (tick == t0 + t1 && target1 != null) {
				DanmakuHelper.Orientation o0 = DanmakuHelper.getOrientation(init, normal);
				for (int i = 0; i < n; i++) {
					var f0 = o0.rotateDegrees(360.0 / n * i);
					var p0 = pos.add(f0.scale(r0));
					var f1 = target1.subtract(p0).normalize();
					var p1 = p0.add(f1.scale(r1));
					var f2 = le.subtract(p1).normalize();
					var vec = f2.scale(termSpeed);
					int t = t2 + r.nextInt(dt);
					var e = holder.prepareDanmaku(t, vec, bullet, color);
					e.setPos(p1);
					e.mover = new RectMover(p1, vec, Vec3.ZERO);
					holder.shoot(e);
				}
			}
		}

	}

	@SerialClass
	public static class Intercept extends Ticker<ReimuSpell> {

		@SerialClass.SerialField
		private Vec3 vec = Vec3.ZERO, dir = new Vec3(1, 0, 0);
		@SerialClass.SerialField
		private double dist = 32, w = 360d / 60 * 3, speed = 2, duration = 80;

		@Override
		public boolean tick(CardHolder holder, ReimuSpell card) {
			var target = holder.target();
			if (target != null) vec = vec.scale(0.95).add(target.scale(0.05));
			var ori = DanmakuHelper.getOrientation(dir);
			for (int i = 0; i < 8; i++) {
				Vec3 off = ori.rotateDegrees(360d / 8 * i);
				Vec3 pos = vec.add(dir.add(off.scale(dist)));
				var nor = DanmakuHelper.getOrientation(off).asNormal();
				for (int j = 0; j < 8; j++) {
					Vec3 vel = nor.rotateDegrees(360d / 8 * j + tick * w).scale(speed)
							.add(off.scale(-1));
					int life = (int) Math.min(duration - tick, 40) + holder.random().nextInt(10);
					var e = holder.prepareDanmaku(life, vel,
							YHDanmaku.Bullet.BUBBLE, DyeColor.YELLOW);
					e.setPos(pos);
					holder.shoot(e);
				}
			}
			super.tick(holder, card);
			return tick > duration;
		}

	}

}
