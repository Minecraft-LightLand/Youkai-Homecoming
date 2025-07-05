package dev.xkmc.youkaishomecoming.content.entity.animal.crab;

import dev.xkmc.youkaishomecoming.content.entity.animal.common.MobStateMachine;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.item.ItemStack;

import static dev.xkmc.youkaishomecoming.content.entity.animal.crab.CrabState.*;

public class CrabStateMachine extends MobStateMachine<CrabEntity, CrabState, CrabStateMachine> {

	public final AnimationState dig = new AnimationState();
	public final AnimationState swing = new AnimationState();
	public final AnimationState flip = new AnimationState();

	public CrabStateMachine(CrabEntity e) {
		super(e, CrabState.class, CrabState.values());
	}

	public void onHurt() {
		if (state() != FLIP) {
			transitionTo(CrabState.FLIP);
			var stack = mob.getMainHandItem();
			if (!stack.isEmpty()) {
				mob.spawnAtLocation(stack);
				mob.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
			}
		} else {
			transitionTo(IDLE);
		}
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

}
