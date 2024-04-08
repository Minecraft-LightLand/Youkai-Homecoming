package dev.xkmc.youkaishomecoming.content.entity.rumia;

import dev.xkmc.youkaishomecoming.content.entity.floating.FloatingYoukaiAttackGoal;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.SmallFireball;

public class RumiaAttackGoal extends FloatingYoukaiAttackGoal<Rumia> {

	private static final int BALL_RANGE = 8;

	public RumiaAttackGoal(Rumia pBlaze) {
		super(pBlaze, 16);
	}

	@Override
	public boolean canUse() {
		return !youkai.isBlocked() && super.canUse();
	}

	public void stop() {
		super.stop();
	}

	@Override
	protected boolean specialAction() {
		if (youkai.isCharged()) {
			LivingEntity target = youkai.getTarget();
			if (target != null) {
				boolean sight = youkai.getSensing().hasLineOfSight(target);
				double dist = youkai.distanceToSqr(target);
				if (sight) {
					attack(target, dist);
				}
			}
			return true;
		}
		return youkai.isBlocked();
	}

	@Override
	protected int getMeleeRange() {
		return youkai.isCharged() ? 3 : 2;
	}

	@Override
	protected void attack(LivingEntity target, double dist) {
		if (dist < BALL_RANGE * BALL_RANGE) {
			youkai.state.startAttack(youkai);
		}
		super.attack(target, dist);
	}

	@Override
	protected void meleeAttack(LivingEntity target) {
		if (youkai.doHurtTarget(target)) {
			youkai.state.onAttack(youkai, target);
		}
	}

	protected int shoot(LivingEntity target) {
		if (youkai.isCharged()) return 10;
		double dx = target.getX() - youkai.getX();
		double dy = target.getY(0.5D) - youkai.getY(0.5D);
		double dz = target.getZ() - youkai.getZ();
		shoot(dx, dy, dz, 1);
		//shoot(dx, dy, dz, 0.7);
		//shoot(dx, dy, dz, 0.4);
		return 20;
	}

	private void shoot(double dx, double dy, double dz, double speed) {
		SmallFireball projectile = new SmallFireball(youkai.level(), youkai, dx, dy, dz);
		projectile.setPos(projectile.getX(), youkai.getY(0.5D) + 0.5D, projectile.getZ());
		projectile.xPower *= speed;
		projectile.yPower *= speed;
		projectile.zPower *= speed;
		youkai.level().addFreshEntity(projectile);
	}
}
