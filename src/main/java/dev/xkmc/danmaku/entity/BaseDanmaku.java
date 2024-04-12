package dev.xkmc.danmaku.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public abstract class BaseDanmaku extends SimplifiedProjectile {

   protected BaseDanmaku(EntityType<? extends BaseDanmaku> pEntityType, Level pLevel) {
      super(pEntityType, pLevel);
   }

   public void tick() {
      super.tick();
   }

   protected void onHit(HitResult pResult) {
      HitResult.Type hitresult$type = pResult.getType();
      if (hitresult$type == HitResult.Type.ENTITY) {
         this.onHitEntity((EntityHitResult) pResult);
         this.level().gameEvent(GameEvent.PROJECTILE_LAND, pResult.getLocation(), GameEvent.Context.of(this, null));
      } else if (hitresult$type == HitResult.Type.BLOCK) {
         BlockHitResult blockhitresult = (BlockHitResult) pResult;
         this.onHitBlock(blockhitresult);
         BlockPos blockpos = blockhitresult.getBlockPos();
         this.level().gameEvent(GameEvent.PROJECTILE_LAND, blockpos, GameEvent.Context.of(this, this.level().getBlockState(blockpos)));
      }

   }

   /**
    * Called when the arrow hits an entity
    */
   protected void onHitEntity(EntityHitResult pResult) {
   }

   protected void onHitBlock(BlockHitResult pResult) {
   }

   public boolean canHitEntity(Entity target) {
      if (!target.canBeHitByProjectile()) {
         return false;
      } else {
         Entity entity = this.getOwner();
         if (entity == null) return false;
         if (entity.isPassenger() || target.isPassenger()) {
            return !entity.isPassengerOfSameVehicle(target);
         }
         return !entity.isAlliedTo(target);
      }
   }

}
