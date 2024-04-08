package dev.xkmc.youkaishomecoming.content.entity.floating;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public abstract class FloatingYoukaiAttackGoal<T extends FloatingYoukai> extends Goal {

	protected final T youkai;
	private final int range;
	private int meleeTime;
	private int shootTime;

	public FloatingYoukaiAttackGoal(T youkai, int range) {
		this.youkai = youkai;
		this.range = range;
		setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
	}

	public boolean canUse() {
		LivingEntity livingentity = youkai.getTarget();
		return livingentity != null && livingentity.isAlive() && youkai.canAttack(livingentity);
	}

	public void start() {
		meleeTime = 0;
		youkai.setAggressive(true);
		youkai.setFlying();
	}

	public void stop() {
		youkai.setAggressive(false);
		youkai.setWalking();
	}

	public boolean requiresUpdateEveryTick() {
		return true;
	}

	public void tick() {
		if (shootTime > 0) {
			shootTime--;
		}
		if (meleeTime > 0) {
			meleeTime--;
		}
		LivingEntity target = youkai.getTarget();
		if (target == null) return;
		boolean sight = youkai.getSensing().hasLineOfSight(target);
		double dist = youkai.distanceToSqr(target);
		if (!sight) {
			if (dist < getFollowDistance() * getFollowDistance()) {
				youkai.getNavigation().moveTo(target.getX(), target.getY(), target.getZ(), 1.0D);
			}
			return;
		} else {
			if (dist * 2 < range * range) {
				youkai.getNavigation().stop();
			}
		}
		if (dist > range * range && youkai.getNavigation().isDone()) {
			youkai.getNavigation().moveTo(target.getX(), target.getY(), target.getZ(), 2.0D);
		}
		if (dist < 4.0D && meleeTime <= 0) {
			meleeTime = 20;
			youkai.doHurtTarget(target);
			return;
		}
		if (dist < getFollowDistance() * getFollowDistance()) {
			if (shootTime <= 0) {
				shootTime = shoot(target);
			}
			youkai.getLookControl().setLookAt(target, 10.0F, 10.0F);
		}
	}

	protected abstract int shoot(LivingEntity target);

	private double getFollowDistance() {
		return youkai.getAttributeValue(Attributes.FOLLOW_RANGE);
	}
}
