package dev.xkmc.youkaishomecoming.content.entity.rumia;

import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

@SerialClass
public class RumiaStateMachine {

	private static final int PREPARE_TIME = 40, FLY_TIME = 100, BLOCK_TIME = 100;

	public enum RumiaStage {
		NONE, PREPARE, FLY, BLOCKED
	}

	@SerialClass.SerialField
	private RumiaStage stage;

	@SerialClass.SerialField
	private int time;

	public void tick(Rumia rumia) {
		if (time > 0) {
			time--;
			if (time == 0) {
				if (stage == RumiaStage.PREPARE) {
					LivingEntity target = rumia.getTarget();
					if (target != null && target.isAlive() && target.canBeSeenAsEnemy()) {
						var src = rumia.position().add(0, rumia.getBbHeight() * 0.5, 0);
						var dst = target.position().add(0, target.getBbHeight() * 0.5, 0);
						var vec = dst.subtract(src).normalize().scale(2);//TODO config
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
				} else if (stage == RumiaStage.BLOCKED) {
					stage = RumiaStage.NONE;
					setBlocked(rumia, false);
				}
			}
		}
	}

	public void startAttack(Rumia rumia) {
		if (stage != RumiaStage.NONE) return;
		rumia.getNavigation().stop();
		stage = RumiaStage.PREPARE;
		time = PREPARE_TIME;
		setCharged(rumia, true);
		rumia.setDeltaMovement(Vec3.ZERO);
	}

	public void onBlocked(Rumia rumia) {
		if (stage != RumiaStage.FLY) return;
		rumia.getNavigation().stop();
		rumia.setDeltaMovement(rumia.getDeltaMovement().scale(-0.5));
		setCharged(rumia, false);
		setBlocked(rumia, true);
		time = BLOCK_TIME;
		stage = RumiaStage.BLOCKED;
	}

	public boolean isCharged(Rumia rumia) {
		return rumia.getFlag(1);
	}

	public boolean isBlocked(Rumia rumia) {
		return rumia.getFlag(2);
	}

	private void setCharged(Rumia rumia, boolean charged) {
		rumia.setFlag(1, charged);
	}

	private void setBlocked(Rumia rumia, boolean blocked) {
		rumia.setFlag(2, blocked);
	}

}
