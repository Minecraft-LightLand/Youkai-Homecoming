package dev.xkmc.youkaishomecoming.content.entity.animal.deer;

import net.minecraft.world.entity.AnimationState;

public class DeerStateMachine {

	public static final byte RESET = 80, EAT = 81, RELAX = 82;

	public final AnimationState walk = new AnimationState();
	public final AnimationState run = new AnimationState();
	public final AnimationState smell = new AnimationState();
	public final AnimationState attack = new AnimationState();
	public final AnimationState eat = new AnimationState();
	public final AnimationState relaxStart = new AnimationState();
	public final AnimationState relaxDur = new AnimationState();
	public final AnimationState relaxEnd = new AnimationState();

	private ActionState state = ActionState.IDLE;
	private int tick;

	public void tick(DeerEntity e) {
		if (e.level().isClientSide()) {
			tickAnimation(e);
		}
	}

	public void tickAnimation(DeerEntity e) {
		if (e.isPanic()) {
			run.startIfStopped(e.tickCount);
			walk.stop();
		} else {
			walk.startIfStopped(e.tickCount);
			run.stop();
		}
		if (state == ActionState.EAT) {
			if (tick > 0) {
				eat.startIfStopped(e.tickCount);
				tick--;
				if (tick <= 0) {
					eat.stop();
					state = ActionState.IDLE;
				}
			}
		}
		if (state == ActionState.RELAX) {
			if (tick > 24 + 25) {
				relaxStart.startIfStopped(e.tickCount);
			} else if (tick > 25) {
				relaxStart.stop();
				relaxDur.startIfStopped(e.tickCount);
				eat.startIfStopped(e.tickCount);
			} else {
				relaxDur.stop();
				eat.stop();
				relaxEnd.startIfStopped(e.tickCount);
			}
			if (tick > 0) {
				tick--;
				if (tick <= 0) {
					relaxEnd.stop();
					state = ActionState.IDLE;
				}
			}
		}
	}

	public void startEating(DeerEntity mob) {
		if (mob.getRandom().nextFloat() < mob.relaxChance()) {
			mob.eat.eatAnimationTick = 22 + 24 + 25;
			mob.eat.finishTick = 24;
			mob.level().broadcastEntityEvent(mob, RELAX);
		} else {
			mob.eat.eatAnimationTick = 25;
			mob.eat.finishTick = 4;
			mob.level().broadcastEntityEvent(mob, EAT);
		}
	}

	public void onHurt(DeerEntity mob) {
		mob.eat.stop();
		mob.level().broadcastEntityEvent(mob, RESET);
	}

	private void stopAll() {
		relaxStart.stop();
		relaxDur.stop();
		relaxEnd.stop();
		eat.stop();
	}

	public boolean handleEntityEvent(byte data) {
		if (data == RESET) {
			state = ActionState.IDLE;
			tick = 0;
			stopAll();
			return true;
		}
		if (data == EAT) {
			state = ActionState.EAT;
			tick = 25;
			return true;
		}
		if (data == RELAX) {
			state = ActionState.RELAX;
			tick = 22 + 24 + 25;
			return true;
		}
		return false;
	}

	public enum ActionState {
		IDLE, EAT, RELAX, ATTACK, SMELL
	}

}
