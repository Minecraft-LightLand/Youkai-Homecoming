package dev.xkmc.youkaishomecoming.content.entity.animal.deer.goal;

import dev.xkmc.youkaishomecoming.content.entity.animal.deer.DeerStateMachine;
import net.minecraft.world.entity.ai.goal.Goal;

public interface DeerStateNotifierGoal {

	boolean shouldStopRelax();

	default Goal register(DeerStateMachine states) {
		states.register(this);
		return (Goal) this;
	}

}
