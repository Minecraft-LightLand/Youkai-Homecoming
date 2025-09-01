package dev.xkmc.youkaishomecoming.content.entity.deer.goal;

import dev.xkmc.youkaishomecoming.content.entity.deer.DeerEntity;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.item.crafting.Ingredient;

public class DeerTemptGoal extends TemptGoal implements DeerStateNotifierGoal{

	public final DeerEntity deer;

	public DeerTemptGoal(DeerEntity deer, double speed, Ingredient food, boolean canScare) {
		super(deer, speed, food, canScare);
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
