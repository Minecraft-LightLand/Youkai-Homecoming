package dev.xkmc.youkaishomecoming.content.entity.deer;

import dev.xkmc.youkaishomecoming.content.entity.animal.common.MobStateMachine;
import net.minecraft.world.entity.AnimationState;

import static dev.xkmc.youkaishomecoming.content.entity.deer.DeerState.*;

public class DeerStateMachine extends MobStateMachine<DeerEntity, DeerState, DeerStateMachine> {

	public final AnimationState smell = new AnimationState();
	public final AnimationState attack = new AnimationState();
	public final AnimationState eat = new AnimationState();
	public final AnimationState relaxStart = new AnimationState();
	public final AnimationState relaxDur = new AnimationState();
	public final AnimationState relaxEnd = new AnimationState();

	public DeerStateMachine(DeerEntity e) {
		super(e, DeerState.class, DeerState.values());
	}

	@Override
	protected void transitionTo(DeerState data, int tickRemain) {
		super.transitionTo(data, tickRemain);
		mob.refreshDimensions();
	}

	public boolean isMobile() {
		return state().mobile();
	}

	public boolean isRelaxed() {
		return state().isRelaxed();
	}

	public boolean canRelax() {
		return state() == DeerState.IDLE || state().isRelaxed();
	}

	public void startRelax() {
		if (state() == DeerState.IDLE) {
			transitionTo(RELAX_START);
		}
	}

	public boolean mayStopRelax() {
		return state() == RELAX;
	}

	public void stopRelax() {
		transitionTo(RELAX_END);
	}

	public boolean canEat() {
		return state() == DeerState.IDLE || state() == DeerState.RELAX;
	}

	public void startEating() {
		if (state().isRelaxed()) {
			transitionTo(RELAX_EAT);
		} else {
			transitionTo(EAT);
		}
	}

	public void onHurt() {
		mob.eat.stop();
		if (state() != IDLE) {
			transitionTo(IDLE);
		}
	}

	public boolean mustStopRelax() {
		if (mob.notifiers == null) return false;
		for (var e : mob.notifiers) {
			if (e.shouldStopRelax()) {
				return true;
			}
		}
		return false;
	}
}
