package dev.xkmc.fastprojectileapi.collision;

import dev.xkmc.fastprojectileapi.entity.BaseLaser;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LaserHitHelper {

	public record LaserHitResult(Vec3 dst, @Nullable BlockHitResult bhit, List<EntityHitResult> ehit) {

	}

	public static LaserHitResult getHitResultOnProjection(BaseLaser e, boolean checkBlock, boolean checkEntity) {
		Vec3 src = e.position().add(0, e.getBbHeight() / 2f, 0);
		Vec3 v = e.getForward().scale(e.getLength());
		Level level = e.level();
		Vec3 dst = src.add(v);
		BlockHitResult bhit = null;
		if (checkBlock) {
			bhit = level.clip(new ClipContext(src, dst, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, e));
			if (bhit.getType() != HitResult.Type.MISS) {
				dst = bhit.getLocation();
			} else bhit = null;
		}
		List<EntityHitResult> ehit = new ArrayList<>();
		if (checkEntity && level instanceof ServerLevel sl) {
			getEntityHitResult(ehit, sl, e, src, dst,
					e.getBoundingBox().expandTowards(v), e.getEffectiveHitRadius());
		}
		return new LaserHitResult(dst, bhit, ehit);
	}

	public static void getEntityHitResult(List<EntityHitResult> ans, ServerLevel level, BaseLaser self, Vec3 src, Vec3 dst, AABB box, float radius) {
		for (Entity e : EntityStorageCache.get(level).foreach(box.inflate(1 + radius), self::canHitEntity)) {
			if (e == self) continue;
			AABB aabb = e.getBoundingBox().inflate(radius);
			Optional<Vec3> optional = aabb.clip(src, dst);
			optional.ifPresent(vec3 -> ans.add(new EntityHitResult(e, vec3)));
		}
	}

}
