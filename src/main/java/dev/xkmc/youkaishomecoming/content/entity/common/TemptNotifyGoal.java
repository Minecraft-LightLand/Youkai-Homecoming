package dev.xkmc.youkaishomecoming.content.entity.common;

import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.crafting.Ingredient;

public class TemptNotifyGoal<T extends Animal & StateMachineMob> extends TemptGoal implements INotifyMoveGoal {

	public final T deer;

	public TemptNotifyGoal(T deer, double speed, Ingredient food, boolean canScare) {
		super(deer, speed, food, canScare);
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
