package dev.xkmc.youkaishomecoming.content.entity.animal.deer.goal;

import dev.xkmc.youkaishomecoming.content.entity.animal.deer.DeerEntity;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;

public class DeerFollowParentGoal extends FollowParentGoal implements DeerStateNotifierGoal {

	public final DeerEntity deer;

	public DeerFollowParentGoal(DeerEntity deer, double speed) {
		super(deer, speed);
		this.deer = deer;
	}

	@Override
	public boolean canUse() {
		return deer.states().isMobile() && super.canUse();
	}

	public boolean shouldStopRelax() {
		return super.canUse();
	}

}
