package dev.xkmc.youkaishomecoming.content.entity.animal.common;

import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.animal.Animal;

public class FollowParentNotifyGoal<T extends Animal & StateMachineMob> extends FollowParentGoal implements INotifyMoveGoal {

	public final T mob;

	public FollowParentNotifyGoal(T mob, double speed) {
		super(mob, speed);
		this.mob = mob;
	}

	@Override
	public boolean canUse() {
		return mob.states().isMobile() && super.canUse();
	}

	public boolean wantsToMove() {
		return super.canUse();
	}

}
