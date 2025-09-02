package dev.xkmc.youkaishomecoming.content.entity.animal.deer.goal;

import net.minecraft.world.entity.ai.goal.Goal;

import java.util.List;

public interface DeerStateNotifierGoal {

	boolean shouldStopRelax();

	default Goal register(List<DeerStateNotifierGoal> notifiers) {
		notifiers.add(this);
		return (Goal) this;
	}

}
