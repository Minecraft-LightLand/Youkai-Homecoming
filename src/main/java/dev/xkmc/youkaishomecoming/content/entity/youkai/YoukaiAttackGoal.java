package dev.xkmc.youkaishomecoming.content.entity.youkai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;
import java.util.List;

public class YoukaiAttackGoal<T extends YoukaiEntity> extends Goal {

	protected final T youkai;
	private int meleeTime;
	private int shootTime;

	public YoukaiAttackGoal(T youkai) {
		this.youkai = youkai;
		setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
	}

	public boolean canUse() {
		LivingEntity livingentity = youkai.getTarget();
		return livingentity != null && livingentity.isAlive() && youkai.canAttack(livingentity);
	}

	public void start() {
		meleeTime = 10;
		shootTime = 20;
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
		youkai.setAggressive(true);
		if (shootTime > 0) {
			shootTime--;
		}
		if (meleeTime > 0) {
			meleeTime--;
		}
		if (specialAction()) {
			return;
		}
		LivingEntity target = youkai.getTarget();
		if (target == null) return;
		boolean sight = youkai.getSensing().hasLineOfSight(target);
		double dist = youkai.distanceToSqr(target);
		double follow = getShootRange();
		double range = Math.min(follow, youkai.getStopRange());
		if (!sight) {
			if (dist < follow * follow && youkai.getNavigation().isDone()) {
				youkai.getNavigation().moveTo(target.getX(), target.getY(), target.getZ(), 1.0D);
			}
		}
		if (sight && dist * 2 < range * range) {
			youkai.getNavigation().stop();
		}
		if (dist > range * range && youkai.getNavigation().isDone()) {
			youkai.getNavigation().moveTo(target.getX(), target.getY(), target.getZ(), 2.0D);
		}
		if (dist < follow * follow) {
			if (youkai.spellCard == null)
				attack(target, dist, sight);
			youkai.getLookControl().setLookAt(target, 10.0F, 10.0F);
		}
	}


	protected void attack(LivingEntity target, double dist, boolean sight) {
		double melee = getMeleeRange();
		if (sight && dist < melee * melee) {
			if (meleeTime <= 0) {
				meleeTime = 20;
				meleeAttack(target);
			}
		}
		if (shootTime <= 0) {
			shootTime = shoot(target, youkai.targets.getTargets());
		}
	}

	protected void meleeAttack(LivingEntity target) {
		youkai.doHurtTarget(target);
	}

	protected boolean specialAction() {
		return false;
	}

	protected int shoot(LivingEntity target, List<LivingEntity> all) {
		return 20;
	}

	protected double getMeleeRange() {
		return 2;
	}

	public double getShootRange() {
		return youkai.getAttributeValue(Attributes.FOLLOW_RANGE);
	}
}
