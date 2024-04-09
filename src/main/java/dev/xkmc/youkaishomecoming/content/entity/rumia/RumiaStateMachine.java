package dev.xkmc.youkaishomecoming.content.entity.rumia;

import dev.xkmc.l2library.util.math.MathHelper;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.entity.damaku.DamakuHelper;
import dev.xkmc.youkaishomecoming.content.entity.youkai.YoukaiEntity;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

@SerialClass
public class RumiaStateMachine {

	private static final int PREPARE_TIME = 20, FLY_TIME = 60, BLOCK_TIME = 100;
	private static final int SUCCESS_DELAY = 100, BLOCK_DELAY = 200;
	private static final float SPEED = 2, KNOCK = 3;
	private static final UUID ATK = MathHelper.getUUIDFromString("rumia_charge_attack");

	public enum RumiaStage {
		NONE, PREPARE, FLY, BLOCKED
	}

	@SerialClass.SerialField
	private RumiaStage stage = RumiaStage.NONE;

	@SerialClass.SerialField
	private int time = 0, ballDelay = 0;

	public void refreshState(RumiaEntity rumia) {
		if (rumia.getTarget() == null) {
			rumia.setWalking();
		}
	}

	public void tick(RumiaEntity rumia) {
		if (rumia.getTarget() == null) {
			ballDelay = SUCCESS_DELAY;
		} else if (ballDelay > 0) {
			ballDelay--;
		}
		if (time > 0) {
			time--;
			if (time == 0) {
				if (stage == RumiaStage.PREPARE) {
					LivingEntity target = rumia.getTarget();
					if (target != null && target.isAlive() && target.canBeSeenAsEnemy()) {
						var src = rumia.position().add(0, rumia.getBbHeight() * 0.5, 0);
						var dst = target.position().add(0, target.getBbHeight() * 0.5, 0);
						var vec = dst.subtract(src).normalize().scale(SPEED);
						rumia.setDeltaMovement(vec);
						stage = RumiaStage.FLY;
						time = FLY_TIME;
					} else {
						stage = RumiaStage.NONE;
						setCharged(rumia, false);
					}
				} else if (stage == RumiaStage.FLY) {
					stage = RumiaStage.NONE;
					setCharged(rumia, false);
					ballDelay = SUCCESS_DELAY;
				} else if (stage == RumiaStage.BLOCKED) {
					stage = RumiaStage.NONE;
					setBlocked(rumia, false);
					ballDelay = BLOCK_DELAY;
				}
			}
		}
	}

	public void startChargeAttack(RumiaEntity rumia, LivingEntity target) {
		if (stage != RumiaStage.NONE) return;
		if (ballDelay > 0) return;
		if (target instanceof YoukaiEntity || target.hasEffect(YHEffects.YOUKAIFIED.get()))
			return;
		rumia.getNavigation().stop();
		stage = RumiaStage.PREPARE;
		time = PREPARE_TIME;
		setCharged(rumia, true);
		rumia.setDeltaMovement(Vec3.ZERO);
	}

	public void onAttack(RumiaEntity rumia, LivingEntity target) {
		if (stage != RumiaStage.FLY) return;
		if (target.isAlive()) {
			target.setDeltaMovement(rumia.getDeltaMovement()
					.normalize().scale(KNOCK)
					.multiply(1, 0, 1)
					.add(0, 0.4, 0));
			target.hasImpulse = true;
		}
		rumia.setDeltaMovement(Vec3.ZERO);
		stage = RumiaStage.NONE;
		setCharged(rumia, false);
		ballDelay = SUCCESS_DELAY;
	}

	public void onHurt(RumiaEntity rumia, LivingEntity le, float amount) {
		if (stage == RumiaStage.BLOCKED) {
			if (amount >= 2) {
				time = time / 2 + 1;
			}
		} else if (stage != RumiaStage.FLY) {
			if (amount >= 2) shoot(rumia, le);
		}
	}

	private void shoot(RumiaEntity rumia, LivingEntity target) {
		double dx = target.getX() - rumia.getX();
		double dy = target.getY(0.5D) - rumia.getY(0.5D);
		double dz = target.getZ() - rumia.getZ();
		var vec = new Vec3(dx, dy, dz).normalize();
		var ori = DamakuHelper.getOrientation(vec);
		float dmg = (float) rumia.getAttributeValue(Attributes.ATTACK_DAMAGE);
		for (int i = 0; i < 12; i++) {
			rumia.shoot(dmg, 40, ori.rotate(30 * i).scale(0.8), DyeColor.RED);
		}
	}

	public void onBlocked(RumiaEntity rumia) {
		if (stage != RumiaStage.FLY) return;
		rumia.getNavigation().stop();
		rumia.setDeltaMovement(rumia.getDeltaMovement().scale(-0.5));
		setCharged(rumia, false);
		setBlocked(rumia, true);
		time = BLOCK_TIME;
		stage = RumiaStage.BLOCKED;
		if (rumia.isEx()) time /= 2;
	}

	public boolean isCharged(RumiaEntity rumia) {
		return rumia.getFlag(1);
	}

	public boolean isBlocked(RumiaEntity rumia) {
		return rumia.getFlag(2);
	}

	private void setCharged(RumiaEntity rumia, boolean charged) {
		var attr = rumia.getAttribute(Attributes.ATTACK_DAMAGE);
		assert attr != null;
		if (charged) {
			attr.addPermanentModifier(new AttributeModifier(ATK, "rumia_charge_attack", 34, AttributeModifier.Operation.ADDITION));
		} else {
			attr.removeModifier(ATK);
		}
		rumia.setFlag(1, charged);
	}

	private void setBlocked(RumiaEntity rumia, boolean blocked) {
		rumia.setFlag(2, blocked);
		rumia.refreshDimensions();
	}

}
