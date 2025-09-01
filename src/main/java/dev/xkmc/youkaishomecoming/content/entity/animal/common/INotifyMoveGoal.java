package dev.xkmc.youkaishomecoming.content.entity.animal.common;

import net.minecraft.world.entity.ai.goal.Goal;

public interface INotifyMoveGoal {

	boolean wantsToMove();

	default Goal register(MobStateMachine<?, ?, ?> states) {
		states.register(this);
		return (Goal) this;
	}

}
