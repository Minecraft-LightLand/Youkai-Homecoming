package dev.xkmc.youkaishomecoming.content.entity.rumia;

import dev.xkmc.youkaishomecoming.content.entity.danmaku.DanmakuHelper;
import dev.xkmc.youkaishomecoming.content.entity.youkai.FloatingYoukaiAttackGoal;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.phys.Vec3;

public class RumiaAttackGoal extends FloatingYoukaiAttackGoal<RumiaEntity> {

	private static final int BALL_RANGE = 10;
	private static final int APPROACH_RANGE = 16;
	private static final int SHOOT_FREQUENCY = 40;
	private static final int SEPARATION = 12, ANGLE = 3;
	private static final float SPEED = 0.64f, SPEED_VAR = 0.08f;

	public RumiaAttackGoal(RumiaEntity pBlaze) {
		super(pBlaze, APPROACH_RANGE);
	}

	@Override
	public boolean canUse() {
		return !youkai.isBlocked() && super.canUse();
	}

	@Override
	protected boolean specialAction() {
		if (youkai.isCharged()) {
			LivingEntity target = youkai.getTarget();
			if (target != null) {
				boolean sight = youkai.getSensing().hasLineOfSight(target);
				double dist = youkai.distanceToSqr(target);
				if (sight) {
					attack(target, dist, true);
				}
			}
			return true;
		}
		return false;
	}

	@Override
	protected double getMeleeRange() {
		return youkai.isCharged() ? 3 : 2;
	}

	@Override
	protected void attack(LivingEntity target, double dist, boolean sight) {
		if (sight && dist < BALL_RANGE * BALL_RANGE) {
			youkai.state.startChargeAttack(youkai, target);
		}
		super.attack(target, dist, sight);
	}

	@Override
	protected void meleeAttack(LivingEntity target) {
		if (youkai.doHurtTarget(target)) {
			youkai.state.onAttack(youkai, target);
		}
	}

	protected int shoot(LivingEntity target) {
		if (youkai.isCharged()) return 10;
		double range = getShootRange();
		double dx = target.getX() - youkai.getX();
		double dy = target.getY(0.5D) - youkai.getY(0.5D);
		double dz = target.getZ() - youkai.getZ();
		var vec = new Vec3(dx, dy, dz).normalize();
		var ori = DanmakuHelper.getOrientation(vec);
		int off = youkai.getRandom().nextFloat() < 0.5 ? -ANGLE : ANGLE;
		float dmg = (float) youkai.getAttributeValue(Attributes.ATTACK_DAMAGE);
		int round = youkai.isEx() ? 5 : 3;
		for (int i = 0; i < round; i++) {
			float speed = SPEED + i * SPEED_VAR;
			int angle = off * (round - i - 2);
			int life = (int) (range / speed);
			youkai.shoot(dmg, life, ori.rotate(-SEPARATION + angle).scale(speed), DyeColor.RED);
			youkai.shoot(dmg, life, ori.rotate(angle).scale(speed), DyeColor.BLACK);
			youkai.shoot(dmg, life, ori.rotate(SEPARATION + angle).scale(speed), DyeColor.RED);
		}
		int ans = SHOOT_FREQUENCY;
		if (youkai.isEx()) ans /= 2;
		return ans;
	}

}
