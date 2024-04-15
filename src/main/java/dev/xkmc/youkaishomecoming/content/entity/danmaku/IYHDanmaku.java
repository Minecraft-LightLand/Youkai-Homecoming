package dev.xkmc.youkaishomecoming.content.entity.danmaku;

import dev.xkmc.danmaku.entity.SimplifiedProjectile;
import dev.xkmc.youkaishomecoming.content.entity.youkai.YoukaiEntity;
import dev.xkmc.youkaishomecoming.init.data.YHDamageTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.entity.PartEntity;

public interface IYHDanmaku {

	float damage(Entity target);

	default SimplifiedProjectile self() {
		return (SimplifiedProjectile) this;
	}

	default void hurtTarget(EntityHitResult result) {
		if (self().level().isClientSide) return;
		var e = result.getEntity();
		if (e instanceof LivingEntity le) {
			if (le.hurtTime > 0) {
				DamageSource source = le.getLastDamageSource();
				if (source != null && source.getDirectEntity() == this) {
					return;
				}
			}
		}
		if (!e.hurt(YHDamageTypes.danmaku(this), damage(e))) return;
		LivingEntity target = null;
		while (e instanceof PartEntity<?> pe) {
			e = pe.getParent();
		}
		if (e instanceof LivingEntity le) target = le;
		if (target != null) {
			if (self().getOwner() instanceof YoukaiEntity youkai) {
				youkai.onDanmakuHit(target, this);
			}
		}
	}

}
