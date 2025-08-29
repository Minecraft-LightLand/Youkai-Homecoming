package dev.xkmc.youkaishomecoming.content.entity.animal.boar;

import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.ai.goal.Goal;

public class BoarWobbleGoal extends Goal {

	private static final int TIME = 40;

	private final BoarEntity boar;

	private int lastTime = 0;

	public BoarWobbleGoal(BoarEntity boar) {
		super();
		this.boar = boar;
	}

	@Override
	public boolean canUse() {
		return lastTime + TIME <= boar.tickCount && boar.getRandom().nextInt(boar.prop.wobbliness()) == 0 && boar.getNavigation().isDone();
	}

	@Override
	public void start() {
		super.start();
		lastTime = boar.tickCount;
		boar.level().broadcastEntityEvent(boar, EntityEvent.ARMORSTAND_WOBBLE);
	}

}
