package dev.xkmc.youkaishomecoming.content.entity.rumia;

import dev.xkmc.youkaishomecoming.content.entity.damaku.DamakuHelper;
import dev.xkmc.youkaishomecoming.content.entity.damaku.ItemDamakuEntity;
import dev.xkmc.youkaishomecoming.content.entity.floating.FloatingYoukaiAttackGoal;
import dev.xkmc.youkaishomecoming.init.registrate.YHDamaku;
import dev.xkmc.youkaishomecoming.init.registrate.YHEntities;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.phys.Vec3;

public class RumiaAttackGoal extends FloatingYoukaiAttackGoal<RumiaEntity> {

	private static final int BALL_RANGE = 8, APPROACH_RANGE = 16, SHOOT_FREQUENCY = 40, SHOOT_LIFE = 80;
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
			youkai.state.startAttack(youkai);
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
		double dx = target.getX() - youkai.getX();
		double dy = target.getY(0.5D) - youkai.getY(0.5D);
		double dz = target.getZ() - youkai.getZ();
		var vec = new Vec3(dx, dy, dz).normalize();
		var ori = DamakuHelper.getOrientation(vec);
		int off = youkai.getRandom().nextFloat() < 0.5 ? -ANGLE : ANGLE;
		float dmg = (float) youkai.getAttributeValue(Attributes.ATTACK_DAMAGE);
		for (int i = 0; i < 3; i++) {
			float speed = SPEED + i * 0.08f;
			int angle = off * (i - 1);
			shoot(dmg, ori.rotate(-SEPARATION + angle).scale(speed), DyeColor.RED);
			shoot(dmg, ori.rotate(angle).scale(speed), DyeColor.BLACK);
			shoot(dmg, ori.rotate(SEPARATION + angle).scale(speed), DyeColor.RED);
		}
		return SHOOT_FREQUENCY;
	}

	private void shoot(float dmg, Vec3 vec, DyeColor color) {
		ItemDamakuEntity damaku = new ItemDamakuEntity(YHEntities.ITEM_DAMAKU.get(), youkai, youkai.level());
		damaku.setItem(YHDamaku.SIMPLE.get(color).asStack());
		damaku.setup(dmg, SHOOT_LIFE, true, vec);
		youkai.level().addFreshEntity(damaku);
	}
}
