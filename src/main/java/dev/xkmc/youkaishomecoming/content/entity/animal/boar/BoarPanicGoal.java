package dev.xkmc.youkaishomecoming.content.entity.animal.boar;

import net.minecraft.world.entity.ai.goal.PanicGoal;

public class BoarPanicGoal extends PanicGoal {

	private final BoarEntity deer;

	public BoarPanicGoal(BoarEntity mob, double speed) {
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
