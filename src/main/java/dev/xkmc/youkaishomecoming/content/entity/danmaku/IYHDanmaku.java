package dev.xkmc.youkaishomecoming.content.entity.danmaku;

import dev.xkmc.fastprojectileapi.entity.GrazingEntity;
import dev.xkmc.fastprojectileapi.entity.SimplifiedProjectile;
import dev.xkmc.youkaishomecoming.content.capability.GrazeCapability;
import dev.xkmc.youkaishomecoming.content.entity.youkai.YoukaiEntity;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.CardHolder;
import dev.xkmc.youkaishomecoming.events.GeneralEventHandlers;
import dev.xkmc.youkaishomecoming.init.data.YHDamageTypes;
import dev.xkmc.youkaishomecoming.init.data.YHModConfig;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.entity.PartEntity;
import org.jetbrains.annotations.Nullable;

public interface IYHDanmaku extends GrazingEntity {

	float GRAZE_RANGE = 1.5f;

	float damage(Entity target);

	SimplifiedProjectile self();

	@Override
	default float grazeRange() {
		return GRAZE_RANGE;
	}

	@Override
	default AABB alterHitBox(Entity x, float radius, float graze) {
		if (self().getOwner() instanceof Player player &&
				x instanceof YoukaiEntity youkai &&
				youkai.targets.contains(player)) {
			return youkai.getBoundingBox().inflate(GRAZE_RANGE);
		}
		return alterEntityHitBox(x, radius, graze);
	}

	default boolean shouldHurt(@Nullable Entity owner, Entity e) {
		if (owner == null) return false;
		if (owner instanceof YoukaiEntity youkai) {
			if (e instanceof LivingEntity le) {
				return youkai.shouldHurt(le);
			}
			return false;
		}
		return true;
	}

	default DamageSource source() {
		DamageSource dmgType = YHDamageTypes.danmaku(this);
		if (self().getOwner() instanceof CardHolder youkai) {
			dmgType = youkai.getDanmakuDamageSource(this);
		}
		if (self().getOwner() instanceof LivingEntity le)
			dmgType = GeneralEventHandlers.modifyDamageType(le, dmgType, this);
		return dmgType;
	}

	default void hurtTarget(EntityHitResult result) {
		if (self().level().isClientSide) return;
		var e = result.getEntity();
		var source = source();
		if (e instanceof LivingEntity le) {
			if (le.hurtTime > 0 && (YHModConfig.COMMON.invulFrameForDanmaku.get() || e instanceof Player || e instanceof YoukaiEntity)) {
				DamageSource last = le.getLastDamageSource();
				if (last != null && last.getDirectEntity() instanceof IYHDanmaku) {
					return;
				}
			}
		}
		LivingEntity target = null;
		while (e instanceof PartEntity<?> pe) {
			e = pe.getParent();
		}
		if (e instanceof LivingEntity le) target = le;
		var owner = self().getOwner();
		if (target != null && owner instanceof YoukaiEntity youkai) {
			youkai.danmakuHitTarget(this, source, target);
			return;
		}
		if (owner instanceof Player player) {
			if (e instanceof LivingEntity le) {
				if (!GrazeCapability.HOLDER.get(player).shouldHurt(le)) return;
			}
		}
		e.hurt(source, damage(e));
	}

	static AABB alterEntityHitBox(Entity x, float radius, float graze) {
		var box = x.getBoundingBox();
		if (graze > 0) return box.inflate(radius + graze);
		float shrink = 0;
		if (x instanceof Player player && player.hasEffect(YHEffects.FAIRY.get())) {
			shrink = 0.2f;
		}
		return new AABB(
				box.minX + shrink - radius, box.minY + shrink * 2 - radius, box.minZ + shrink - radius,
				box.maxX - shrink + radius, box.maxY + radius, box.maxZ - shrink + radius
		);
	}

}
