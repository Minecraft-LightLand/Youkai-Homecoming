package dev.xkmc.youkaishomecoming.content.entity.rumia;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.projectile.SmallFireball;

import java.util.EnumSet;

class RumiaAttackGoal extends Goal {
	private final Rumia rumia;
	private int attackStep;
	private int attackTime;
	private int lastSeen;

	public RumiaAttackGoal(Rumia pBlaze) {
		rumia = pBlaze;
		setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
	}

	public boolean canUse() {
		LivingEntity livingentity = rumia.getTarget();
		return livingentity != null && livingentity.isAlive() && rumia.canAttack(livingentity);
	}

	public void start() {
		attackStep = 0;
	}

	public void stop() {
		rumia.setCharged(false);
		lastSeen = 0;
	}

	public boolean requiresUpdateEveryTick() {
		return true;
	}

	public void tick() {
		--attackTime;
		LivingEntity livingentity = rumia.getTarget();
		if (livingentity != null) {
			boolean flag = rumia.getSensing().hasLineOfSight(livingentity);
			if (flag) {
				lastSeen = 0;
			} else {
				++lastSeen;
			}
			double d0 = rumia.distanceToSqr(livingentity);
			if (d0 < 4.0D) {
				if (!flag) {
					return;
				}
				if (attackTime <= 0) {
					attackTime = 20;
					rumia.doHurtTarget(livingentity);
				}
				rumia.getMoveControl().setWantedPosition(livingentity.getX(), livingentity.getY(), livingentity.getZ(), 1.0D);
			} else if (d0 < getFollowDistance() * getFollowDistance() && flag) {
				double dx = livingentity.getX() - rumia.getX();
				double dy = livingentity.getY(0.5D) - rumia.getY(0.5D);
				double dz = livingentity.getZ() - rumia.getZ();
				if (attackTime <= 0) {
					++attackStep;
					if (attackStep == 1) {
						attackTime = 60;
						rumia.setCharged(true);
					} else if (attackStep <= 4) {
						attackTime = 6;
					} else {
						attackTime = 100;
						attackStep = 0;
						rumia.setCharged(false);
					}

					if (attackStep > 1) {
						double randomness = Math.sqrt(Math.sqrt(d0)) * 0.5D;
						if (!rumia.isSilent()) {
							rumia.level().levelEvent(null, 1018, rumia.blockPosition(), 0);
						}
						shoot(dx, dy, dz, randomness);
					}
				}
				rumia.getLookControl().setLookAt(livingentity, 10.0F, 10.0F);
			} else if (lastSeen < 5) {
				rumia.getMoveControl().setWantedPosition(livingentity.getX(), livingentity.getY(), livingentity.getZ(), 1.0D);
			}
			super.tick();
		}
	}

	private void shoot(double dx, double dy, double dz, double rand) {
		double maxRand = 0.3;
		SmallFireball projectile = new SmallFireball(rumia.level(), rumia,
				rumia.getRandom().triangle(dx, maxRand * rand), dy,
				rumia.getRandom().triangle(dz, maxRand * rand));
		projectile.setPos(projectile.getX(), rumia.getY(0.5D) + 0.5D, projectile.getZ());
		rumia.level().addFreshEntity(projectile);
	}

	private double getFollowDistance() {
		return rumia.getAttributeValue(Attributes.FOLLOW_RANGE);
	}
}
