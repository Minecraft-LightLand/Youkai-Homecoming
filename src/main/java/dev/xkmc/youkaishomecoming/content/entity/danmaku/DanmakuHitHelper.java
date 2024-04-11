package dev.xkmc.youkaishomecoming.content.entity.danmaku;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.function.Predicate;

public class DanmakuHitHelper {

	public static HitResult getHitResultOnMoveVector(Entity e, Predicate<Entity> filter) {
		Vec3 pStartVec = e.position();
		Vec3 pEndVecOffset = e.getDeltaMovement();
		Level pLevel = e.level();
		Vec3 vec3 = pStartVec.add(pEndVecOffset);
		HitResult hitresult = pLevel.clip(new ClipContext(pStartVec, vec3, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, e));
		if (hitresult.getType() != HitResult.Type.MISS) {
			vec3 = hitresult.getLocation();
		}
		HitResult hitresult1 = ProjectileUtil.getEntityHitResult(pLevel, e, pStartVec, vec3,
				e.getBoundingBox().expandTowards(pEndVecOffset).inflate(1.0D), filter,
				e.getBbWidth() / 2f);
		if (hitresult1 != null) {
			hitresult = hitresult1;
		}

		return hitresult;
	}

}
