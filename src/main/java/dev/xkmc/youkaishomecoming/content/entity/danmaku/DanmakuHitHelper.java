package dev.xkmc.youkaishomecoming.content.entity.danmaku;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Predicate;

public class DanmakuHitHelper {

	public static HitResult getHitResultOnMoveVector(BaseDanmakuEntity e, Predicate<Entity> filter) {
		Vec3 pStartVec = e.position();
		Vec3 pEndVecOffset = e.getDeltaMovement();
		Level pLevel = e.level();
		Vec3 vec3 = pStartVec.add(pEndVecOffset);
		HitResult hitresult = pLevel.clip(new ClipContext(pStartVec, vec3, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, e));
		if (hitresult.getType() != HitResult.Type.MISS) {
			vec3 = hitresult.getLocation();
		}
		HitResult hitresult1 = getEntityHitResult(pLevel, e, pStartVec, vec3,
				e.getBoundingBox().expandTowards(pEndVecOffset).inflate(1.0D), filter,
				e.getBbWidth() / 2f);
		if (hitresult1 != null) {
			hitresult = hitresult1;
		}

		return hitresult;
	}

	@Nullable
	public static EntityHitResult getEntityHitResult(Level level, Entity e, Vec3 src, Vec3 dst, AABB box, Predicate<Entity> filter, float radius) {
		double d0 = Double.MAX_VALUE;
		Entity entity = null;

		for (Entity entity1 : level.getEntities(e, box, filter)) {
			AABB aabb = entity1.getBoundingBox().inflate(radius);
			Optional<Vec3> optional = aabb.clip(src, dst);
			if (optional.isPresent()) {
				double d1 = src.distanceToSqr(optional.get());
				if (d1 < d0) {
					entity = entity1;
					d0 = d1;
				}
			}
		}

		return entity == null ? null : new EntityHitResult(entity);
	}

}
