package dev.xkmc.youkaishomecoming.content.entity.rumia;

import dev.xkmc.youkaishomecoming.events.EffectEventHandlers;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.Ingredient;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class RumiaTemptGoal extends Goal {

	private static final TargetingConditions TEMP_TARGETING = TargetingConditions.forNonCombat().range(10.0D).ignoreLineOfSight();

	private final TargetingConditions targetingConditions;
	protected final PathfinderMob mob;
	private final double speedModifier;
	@Nullable
	protected Player player;
	private int calmDown;
	private final Ingredient items;

	public RumiaTemptGoal(PathfinderMob pMob, Ingredient pItems) {
		mob = pMob;
		speedModifier = 1;
		items = pItems;
		setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
		targetingConditions = TEMP_TARGETING.copy().selector(this::shouldFollow);
	}

	public boolean canUse() {
		if (calmDown > 0) {
			--calmDown;
			return false;
		} else {
			player = mob.level().getNearestPlayer(targetingConditions, mob);
			return player != null;
		}
	}

	private boolean shouldFollow(LivingEntity le) {
		return EffectEventHandlers.isFullCharacter(le) &&
				(items.test(le.getMainHandItem()) || items.test(le.getOffhandItem()));
	}

	public void stop() {
		player = null;
		mob.getNavigation().stop();
		calmDown = reducedTickDelay(100);
	}

	public void tick() {
		if (player == null) return;
		mob.getLookControl().setLookAt(player, (float) (mob.getMaxHeadYRot() + 20), (float) mob.getMaxHeadXRot());
		if (mob.distanceToSqr(player) < 6.25D) {
			mob.getNavigation().stop();
		} else {
			mob.getNavigation().moveTo(player, speedModifier);
		}
	}

}