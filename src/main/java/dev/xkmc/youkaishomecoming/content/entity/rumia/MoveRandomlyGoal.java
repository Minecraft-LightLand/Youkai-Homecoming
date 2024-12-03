package dev.xkmc.youkaishomecoming.content.entity.rumia;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class MoveRandomlyGoal extends RandomStrollGoal {

	public static final float PROBABILITY = 0.001F;
	protected final float probability;

	public MoveRandomlyGoal(PathfinderMob pMob, double pSpeedModifier) {
		this(pMob, pSpeedModifier, PROBABILITY);
	}

	public MoveRandomlyGoal(PathfinderMob pMob, double pSpeedModifier, float pProbability) {
		super(pMob, pSpeedModifier);
		this.probability = pProbability;
	}

	@Nullable
	protected Vec3 getPosition() {
		if (this.mob.isInWaterOrBubble()) {
			Vec3 vec3 = LandRandomPos.getPos(this.mob, 15, 7);
			return vec3 == null ? super.getPosition() : vec3;
		} else {
			return this.mob.getRandom().nextFloat() >= this.probability ? LandRandomPos.getPos(this.mob, 10, 7) : super.getPosition();
		}
	}
}