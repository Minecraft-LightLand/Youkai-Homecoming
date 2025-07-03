package dev.xkmc.youkaishomecoming.content.entity.animal.deer;

import net.minecraft.world.entity.AnimationState;

public class DeerStateMachine {

	public final AnimationState walk = new AnimationState();
	public final AnimationState run = new AnimationState();
	public final AnimationState smell = new AnimationState();
	public final AnimationState attack = new AnimationState();
	public final AnimationState eat = new AnimationState();

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

	}

	public void startEating(DeerEntity mob) {
		mob.eat.eatAnimationTick = 25;
		mob.eat.finishTick = 4;
	}

	public enum ActionState {
		IDLE, ATTACK, SMELL, EAT
	}

}
