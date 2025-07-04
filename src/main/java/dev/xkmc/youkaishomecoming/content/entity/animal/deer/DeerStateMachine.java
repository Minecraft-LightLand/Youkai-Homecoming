package dev.xkmc.youkaishomecoming.content.entity.animal.deer;

import net.minecraft.world.entity.AnimationState;

import static dev.xkmc.youkaishomecoming.content.entity.animal.deer.DeerStateMachine.ActionState.*;

public class DeerStateMachine {

	private static final byte OFFSET = 80;

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
		tickState(e, run, e.prop.isPanic());
		tickState(e, walk, !e.prop.isPanic() && state.mobile());
		tickState(e, ActionState.EAT, eat, ActionState.IDLE);
		tickState(e, ActionState.RELAX_START, relaxStart, ActionState.RELAX);
		tickState(e, relaxDur, state == ActionState.RELAX || state == ActionState.RELAX_EAT);
		tickState(e, ActionState.RELAX_EAT, eat, ActionState.RELAX);
		tickState(e, RELAX_END, relaxEnd, ActionState.IDLE);
	}

	private void tickState(DeerEntity e, AnimationState state, boolean pred) {
		if (pred) state.startIfStopped(e.tickCount);
		else state.stop();
	}

	private void tickState(DeerEntity e, ActionState current, AnimationState anim, ActionState next) {
		if (state != current) return;
		if (tick > 0) {
			anim.startIfStopped(e.tickCount);
			tick--;
		} else {
			anim.stop();
			state = next;
		}
	}

	public boolean isRelaxed() {
		return state.isRelaxed();
	}

	public boolean canRelax() {
		return state == ActionState.IDLE || state.isRelaxed();
	}

	public void startRelax(DeerEntity mob) {
		if (state == ActionState.IDLE) {
			transitionTo(mob, RELAX_START);
		}
	}

	public boolean mayStopRelax() {
		return state == RELAX;
	}

	public void stopRelax(DeerEntity mob) {
		transitionTo(mob, RELAX_END);
	}

	public boolean canEat() {
		return state == ActionState.IDLE || state == ActionState.RELAX;
	}

	public void startEating(DeerEntity mob) {
		if (state.isRelaxed()) {
			transitionTo(mob, RELAX_EAT);
		} else {
			transitionTo(mob, EAT);
		}
	}

	public void onHurt(DeerEntity mob) {
		mob.eat.stop();
		transitionTo(mob, IDLE);
	}

	private void stopAll() {
		relaxStart.stop();
		relaxDur.stop();
		relaxEnd.stop();
		eat.stop();
	}

	public boolean transitionTo(DeerEntity mob, byte id) {
		if (id >= OFFSET && id - OFFSET < values().length) {
			return transitionTo(mob, values()[id - OFFSET]);
		}
		return false;
	}

	public boolean transitionTo(DeerEntity mob, ActionState data) {
		if (!mob.level().isClientSide()) {
			mob.level().broadcastEntityEvent(mob, data.id());
		}
		if (data == IDLE) {
			stopAll();
		}
		state = data;
		tick = data.tick;
		return true;
	}

	public enum ActionState {
		IDLE(0),
		EAT(34),
		RELAX_START(22),
		RELAX(0),
		RELAX_EAT(34),
		RELAX_END(24),
		ATTACK(0),
		SMELL(0);

		final int tick;

		ActionState(int tick) {
			this.tick = tick;
		}

		public boolean mobile() {
			return this == IDLE;
		}

		public boolean isRelaxed() {
			return this == RELAX_START || this == RELAX || this == RELAX_EAT || this == RELAX_END;
		}

		public byte id() {
			return (byte) (OFFSET + ordinal());
		}
	}

}
