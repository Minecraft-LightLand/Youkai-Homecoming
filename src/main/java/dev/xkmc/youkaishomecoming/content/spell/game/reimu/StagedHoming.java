package dev.xkmc.youkaishomecoming.content.spell.game.reimu;

import dev.xkmc.fastprojectileapi.entity.ProjectileMovement;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.DanmakuHelper;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.IYHDanmaku;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.ItemDanmakuEntity;
import dev.xkmc.youkaishomecoming.content.item.danmaku.DanmakuItem;
import dev.xkmc.youkaishomecoming.content.spell.mover.RectMover;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.ActualSpellCard;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.CardHolder;
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
public class StagedHoming extends ActualSpellCard {

	@SerialClass.SerialField
	private boolean border, abyss;

	@Override
	public void tick(CardHolder holder) {
		super.tick(holder);
		int interval = 10;
		var target = holder.target();
		var dist = target == null ? 0 : holder.center().distanceTo(target);
		boolean far = dist > 40;
		if (tick % interval == 0) {
			int step = tick / interval % 5;
			if (step < 3) {
				shoot(far);
			} else if (dist > 40) {
				intercept(holder, target);
			}
		}
		if (target != null && border)
			border(holder, dist);
	}

	private void shoot(boolean far) {
		var ans = new StateChange();
		ans.r0 = far ? 32 : 8;
		ans.r1 = far ? 32 : 6;
		ans.t0 = far ? 10 : 20;
		ans.t1 = far ? 10 : 20;
		ans.termSpeed = far ? 3 : 1;
		if (abyss) ans.color = DyeColor.BLUE;
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
		var dist = target == null ? 0 : holder.center().distanceTo(target);
		homingReact(holder, dist > 40);
	}

	private void homingReact(CardHolder holder, boolean far) {
		var le = holder.target();
		if (le == null) return;
		var r = holder.random();
		var dir = le.subtract(holder.center()).normalize();
		if (r.nextDouble() < 0.5) dir = new Vec3(1, 0, 0);
		var ori = DanmakuHelper.getOrientation(dir).rotateDegrees(90, 60 * r.nextDouble() - 30);
		var normal = dir.cross(ori);
		int n = 8;
		int s = r.nextDouble() < 0.5 ? -1 : 1;
		for (int i = 0; i <= 5; i++) {
			var ans = new StateChange();
			ans.r0 = far ? 32 : 24;
			ans.r1 = far ? 32 : 18;
			ans.t0 = far ? 10 : 20;
			ans.t1 = far ? 10 : 20;
			ans.termSpeed = far ? 3 : 1;
			ans.n = n;
			ans.bullet = YHDanmaku.Bullet.BUBBLE;
			ans.pos = holder.center();
			ans.init = DanmakuHelper.getOrientation(ori, normal).rotateDegrees(s * (i - 2) * 360d / n / 4);
			ans.normal = normal;
			ans.tick = -i * 2;
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
	public static class StateChange extends Ticker<StagedHoming> {

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
		public boolean tick(CardHolder holder, StagedHoming card) {
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
	public static class Intercept extends Ticker<StagedHoming> {

		@SerialClass.SerialField
		private Vec3 vec = Vec3.ZERO, dir = new Vec3(1, 0, 0);
		@SerialClass.SerialField
		private double dist = 32, w = 360d / 60 * 3, speed = 2, duration = 80;

		@Override
		public boolean tick(CardHolder holder, StagedHoming card) {
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
