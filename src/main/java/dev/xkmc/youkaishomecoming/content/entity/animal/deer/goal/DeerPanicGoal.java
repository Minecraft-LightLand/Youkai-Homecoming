package dev.xkmc.youkaishomecoming.content.entity.animal.deer.goal;

import dev.xkmc.youkaishomecoming.content.entity.animal.deer.DeerEntity;
import net.minecraft.world.entity.ai.goal.PanicGoal;

public class DeerPanicGoal extends PanicGoal {

	private final DeerEntity deer;

	public DeerPanicGoal(DeerEntity mob, double speed) {
		super(mob, speed);
		this.deer = mob;
	}

	@Override
	public void start() {
		super.start();
		deer.prop.setPanic(true);
	}

	@Override
	public void stop() {
		super.stop();
		deer.prop.setPanic(false);
	}

}
