package dev.xkmc.youkaishomecoming.content.entity.animal.boar;

import dev.xkmc.youkaishomecoming.content.entity.animal.common.MobStateMachine;
import net.minecraft.world.entity.AnimationState;

import static dev.xkmc.youkaishomecoming.content.entity.animal.boar.BoarState.*;

public class BoarStateMachine extends MobStateMachine<BoarEntity, BoarState, BoarStateMachine> {

	public final AnimationState smell = new AnimationState();
	public final AnimationState attack = new AnimationState();
	public final AnimationState dig = new AnimationState();
	public final AnimationState eat = new AnimationState();
	public final AnimationState sleepStart = new AnimationState();
	public final AnimationState sleepDur = new AnimationState();
	public final AnimationState sleepEnd = new AnimationState();

	public BoarStateMachine(BoarEntity e) {
		super(e, BoarState.class, BoarState.values());
	}

	@Override
	protected void transitionTo(BoarState data, int tickRemain) {
		super.transitionTo(data, tickRemain);
		mob.refreshDimensions();
	}

	public boolean isMobile() {
		return state().mobile();
	}

	public boolean isSleeping() {
		return state().isSleeping();
	}

	public boolean canSleep() {
		return state() == BoarState.IDLE || state().isSleeping();
	}

	public void startSleep() {
		if (state() == BoarState.IDLE) {
			transitionTo(SLEEP_START);
		}
	}

	public boolean mayWakeUp() {
		return state() == SLEEP;
	}

	public void wakeUp() {
		transitionTo(SLEEP_END);
	}

	public boolean canEat() {
		return state() == BoarState.IDLE || state() == BoarState.SLEEP;
	}

	public void startEating() {
		transitionTo(EAT);
	}

	public void onHurt() {
		mob.eat.stop();
		if (state() != IDLE) {
			transitionTo(IDLE);
		}
	}

}
