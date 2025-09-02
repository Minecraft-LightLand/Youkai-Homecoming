package dev.xkmc.youkaishomecoming.content.entity.animal.common;

import net.minecraft.world.entity.ai.goal.Goal;

import java.util.List;

public interface INotifyMoveGoal {

	boolean wantsToMove();

	default Goal register(List<INotifyMoveGoal> list) {
		list.add(this);
		return (Goal) this;
	}

}
