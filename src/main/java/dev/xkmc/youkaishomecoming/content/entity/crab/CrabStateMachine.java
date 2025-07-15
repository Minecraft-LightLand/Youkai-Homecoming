package dev.xkmc.youkaishomecoming.content.entity.crab;

import dev.xkmc.youkaishomecoming.content.entity.common.MobStateMachine;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.item.ItemStack;

import static dev.xkmc.youkaishomecoming.content.entity.crab.CrabState.*;

public class CrabStateMachine extends MobStateMachine<CrabEntity, CrabState, CrabStateMachine> {

	public final AnimationState dig = new AnimationState();
	public final AnimationState swing = new AnimationState();
	public final AnimationState flip = new AnimationState();
	public final AnimationState hide = new AnimationState();
	public final AnimationState hideStart = new AnimationState();
	public final AnimationState hideEnd = new AnimationState();

	public CrabStateMachine(CrabEntity e) {
		super(e, CrabState.class, CrabState.values());
	}

	@Override
	public void tick() {
		if (state().isHiding() && !mob.onGround()) {
			transitionTo(IDLE);
		}
		super.tick();
	}

	public void onHurt() {
		if (state() != FLIP) {
			if (mob.getHealth() < mob.getMaxHealth() * 0.5f || mob.getRandom().nextFloat() < 0.3f)
				transitionTo(FLIP);
			else if (state() == HIDE)
				transitionTo(IDLE);
			var stack = mob.getMainHandItem();
			if (!stack.isEmpty()) {
				mob.spawnAtLocation(stack);
				mob.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
			}
		} else {
			transitionTo(IDLE);
		}
	}

	@Override
	protected void transitionTo(CrabState data, int tickRemain) {
		super.transitionTo(data, tickRemain);
		mob.refreshDimensions();
	}

	public boolean isFlipped() {
		return state() == FLIP;
	}

	public void startDigging() {
		transitionTo(DIG);
	}

	public boolean canGrab() {
		return state() == IDLE;
	}

	public boolean canDig() {
		return mob.getHealth() == mob.getMaxHealth() && state() == IDLE && mob.getMainHandItem().isEmpty();
	}

	public void flipBack() {
		transitionTo(IDLE);
	}


	public boolean isHiding() {
		return state().isHiding();
	}

	public boolean canHide() {
		if (!mob.onGround()) return false;
		return state() == IDLE || state().isHiding();
	}

	public void startHiding() {
		if (state() == IDLE) {
			transitionTo(HIDE_START);
		}
	}

	public boolean mayStopHiding() {
		return state() == HIDE;
	}

	public void stopHiding() {
		transitionTo(HIDE_END);
	}

}
