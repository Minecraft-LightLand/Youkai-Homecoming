package dev.xkmc.youkaishomecoming.content.entity.common;

import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.animal.Animal;

public class BreedNotifyGoal<T extends Animal & StateMachineMob> extends BreedGoal implements INotifyMoveGoal {

	public final T deer;

	public BreedNotifyGoal(T deer, double speed) {
		super(deer, speed);
		this.deer = deer;
	}

	@Override
	public boolean canUse() {
		return deer.states().isMobile() && super.canUse();
	}

	public boolean wantsToMove() {
		return super.canUse();
	}

}
