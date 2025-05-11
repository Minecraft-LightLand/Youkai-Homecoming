package dev.xkmc.fastprojectileapi.collision;

import dev.xkmc.fastprojectileapi.entity.BaseLaser;
import dev.xkmc.fastprojectileapi.entity.EntityCachingUser;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

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
			var radius = e.getEffectiveHitRadius();
			var graze = e.grazeRange();
			var box = e.getBoundingBox().expandTowards(v);
			IEntityCache cache = e.getOwner() instanceof EntityCachingUser user ? user.entityCache().get(sl, user.self()) : EntityStorageCache.get(sl);
			var list = cache.foreach(box.inflate(1 + radius + graze), e::canHitEntity);
			for (Entity x : list) {
				if (x == e) continue;
				Vec3 hit = ProjectileHitHelper.checkHit(x, e.alterHitBox(x, radius, 0), src, dst);
				if (hit != null) ehit.add(new EntityHitResult(x, hit));
				if (graze > 0 && x instanceof Player pl) {
					Vec3 gr = ProjectileHitHelper.checkHit(x, e.alterHitBox(x, radius, graze), src, dst);
					if (gr != null) e.doGraze(pl);
				}
			}
		}
		return new LaserHitResult(dst, bhit, ehit);
	}


}
