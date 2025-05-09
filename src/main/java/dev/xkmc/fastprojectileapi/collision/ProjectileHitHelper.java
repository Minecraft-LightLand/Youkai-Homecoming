package dev.xkmc.fastprojectileapi.collision;

import dev.xkmc.fastprojectileapi.entity.BaseProjectile;
import dev.xkmc.fastprojectileapi.entity.EntityCachingUser;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ProjectileHitHelper {

	@Nullable
	public static HitResult getHitResultOnMoveVector(BaseProjectile e, boolean checkBlock) {
		Vec3 src = e.position();
		Vec3 v = e.getDeltaMovement();
		Level level = e.level();
		Vec3 dst = src.add(v);
		HitResult hit = null;
		if (checkBlock) {
			hit = level.clip(new ClipContext(src, dst, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, e));
			if (hit.getType() != HitResult.Type.MISS) {
				dst = hit.getLocation();
			}
		}
		if (level instanceof ServerLevel sl) {
			var radius = e.getBbWidth() / 2f;
			var graze = e.grazeRange();
			var box = e.getBoundingBox().expandTowards(v);
			IEntityCache cache = e.getOwner() instanceof EntityCachingUser user ? user.entityCache().get(sl, user.self()) : EntityStorageCache.get(sl);
			var list = cache.foreach(box.inflate(1 + radius + graze), e::canHitEntity);
			double d0 = Double.MAX_VALUE;
			Entity entity = null;
			for (Entity x : list) {
				if (x == e) continue;
				var hpos = checkHit(x, e.reducedRadius(x, radius), src, dst);
				if (hpos != null) {
					double d1 = src.distanceToSqr(hpos);
					if (d1 < d0) {
						entity = x;
						d0 = d1;
					}
				} else if (graze > 0 && x instanceof Player pl) {
					var gr = checkHit(x, radius + graze, src, dst);
					if (gr != null) e.doGraze(pl);
				}
			}
			if (entity != null) {
				hit = new EntityHitResult(entity);
			}
		}
		return hit;
	}

	@Nullable
	public static Vec3 checkHit(Entity e, double radius, Vec3 src, Vec3 dst) {
		Vec3 vel = e.getDeltaMovement();
		double speed = vel.length();
		int n = (int) Math.min(8, Math.floor(speed / 0.5));
		AABB base = e.getBoundingBox().inflate(radius);
		for (int i = 0; i <= n; i++) {
			AABB aabb = n == 0 ? base : base.move(vel.scale(1d * i / n));
			Optional<Vec3> optional = aabb.contains(src) ? Optional.of(src) : aabb.clip(src, dst);
			if (optional.isPresent())
				return optional.get();
		}
		return null;
	}

}
