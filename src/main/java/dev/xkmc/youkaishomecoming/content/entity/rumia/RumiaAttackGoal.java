package dev.xkmc.youkaishomecoming.content.entity.rumia;

import dev.xkmc.youkaishomecoming.content.entity.floating.FloatingYoukaiAttackGoal;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.SmallFireball;

public class RumiaAttackGoal extends FloatingYoukaiAttackGoal<Rumia> {

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
		return youkai.isCharged() || youkai.isBlocked();
	}

	@Override
	protected int getMeleeRange() {
		return youkai.isCharged() ? 3 : 2;
	}

	@Override
	protected void attack(LivingEntity target, double dist) {
		if (dist < 8 * 8) {//TODO
			youkai.state.startAttack(youkai);
		}
		super.attack(target, dist);
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
