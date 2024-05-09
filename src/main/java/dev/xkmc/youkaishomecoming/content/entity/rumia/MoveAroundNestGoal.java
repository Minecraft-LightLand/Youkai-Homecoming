package dev.xkmc.youkaishomecoming.content.entity.rumia;

import java.util.EnumSet;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;

public class MoveAroundNestGoal extends Goal {
   private final PathfinderMob mob;
   private double wantedX;
   private double wantedY;
   private double wantedZ;
   private final double speedModifier;

   public MoveAroundNestGoal(PathfinderMob pMob, double pSpeedModifier) {
      this.mob = pMob;
      this.speedModifier = pSpeedModifier;
      this.setFlags(EnumSet.of(Goal.Flag.MOVE));
   }

   /**
    * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
    * method as well.
    */
   public boolean canUse() {
      if (this.mob.isWithinRestriction()) {
         return false;
      } else {
         Vec3 vec3 = DefaultRandomPos.getPosTowards(this.mob, 8, 7, Vec3.atBottomCenterOf(this.mob.getRestrictCenter()), Math.PI / 4F);
         if (vec3 == null) {
            return false;
         } else {
            this.wantedX = vec3.x;
            this.wantedY = vec3.y;
            this.wantedZ = vec3.z;
            return true;
         }
      }
   }

   /**
    * Returns whether an in-progress EntityAIBase should continue executing
    */
   public boolean canContinueToUse() {
      return !this.mob.getNavigation().isDone();
   }

   /**
    * Execute a one shot task or start executing a continuous task
    */
   public void start() {
      this.mob.getNavigation().moveTo(this.wantedX, this.wantedY, this.wantedZ, this.speedModifier);
   }
}