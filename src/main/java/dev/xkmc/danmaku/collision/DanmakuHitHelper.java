package dev.xkmc.danmaku.collision;

import dev.xkmc.danmaku.entity.BaseDanmaku;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class DanmakuHitHelper {

	public static HitResult getHitResultOnMoveVector(BaseDanmaku e) {
		Vec3 src = e.position();
		Vec3 v = e.getDeltaMovement();
		Level level = e.level();
		Vec3 dst = src.add(v);
		HitResult hit = level.clip(new ClipContext(src, dst, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, e));
		if (hit.getType() != HitResult.Type.MISS) {
			dst = hit.getLocation();
		}
		if (level instanceof ServerLevel sl) {
			HitResult ehit = getEntityHitResult(sl, e, src, dst,
					e.getBoundingBox().expandTowards(v), e.getBbWidth() / 2f);
			if (ehit != null) {
				hit = ehit;
			}
		}
		return hit;
	}

	@Nullable
	public static EntityHitResult getEntityHitResult(ServerLevel level, BaseDanmaku self, Vec3 src, Vec3 dst, AABB box, float radius) {
		double d0 = Double.MAX_VALUE;
		Entity entity = null;

		for (Entity e : EntityStorageCache.get(level).foreach(box.inflate(1 + radius), self::canHitEntity)) {
			if (e == self) continue;
			AABB aabb = e.getBoundingBox().inflate(radius);
			Optional<Vec3> optional = aabb.clip(src, dst);
			if (optional.isPresent()) {
				double d1 = src.distanceToSqr(optional.get());
				if (d1 < d0) {
					entity = e;
					d0 = d1;
				}
			}
		}

		return entity == null ? null : new EntityHitResult(entity);
	}

}
