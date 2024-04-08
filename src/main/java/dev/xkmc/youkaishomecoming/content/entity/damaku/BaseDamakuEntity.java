
package dev.xkmc.youkaishomecoming.content.entity.damaku;

import dev.xkmc.youkaishomecoming.init.data.YHDamageTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class BaseDamakuEntity extends Projectile {

	private int life = 0;
	public float damage = 0;

	protected BaseDamakuEntity(EntityType<? extends BaseDamakuEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	protected BaseDamakuEntity(EntityType<? extends BaseDamakuEntity> pEntityType, double pX, double pY, double pZ, Level pLevel) {
		this(pEntityType, pLevel);
		this.setPos(pX, pY, pZ);
	}

	protected BaseDamakuEntity(EntityType<? extends BaseDamakuEntity> pEntityType, LivingEntity pShooter, Level pLevel) {
		this(pEntityType, pShooter.getX(), pShooter.getEyeY() - (double) 0.1F, pShooter.getZ(), pLevel);
		this.setOwner(pShooter);
	}

	public void setup(float damage, int life, Vec3 initVec) {
		this.damage = damage;
		this.life = life;
		setDeltaMovement(initVec);
	}

	public boolean shouldRenderAtSqrDistance(double pDistance) {
		double d0 = this.getBoundingBox().getSize() * 4.0D;
		if (Double.isNaN(d0)) {
			d0 = 4.0D;
		}

		d0 *= 64.0D;
		return pDistance < d0 * d0;
	}

	public void tick() {
		super.tick();
		HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
		boolean flag = false;
		if (hitresult.getType() == HitResult.Type.BLOCK) {
			BlockPos blockpos = ((BlockHitResult) hitresult).getBlockPos();
			BlockState blockstate = this.level().getBlockState(blockpos);
			if (blockstate.is(Blocks.NETHER_PORTAL)) {
				this.handleInsidePortal(blockpos);
				flag = true;
			} else if (blockstate.is(Blocks.END_GATEWAY)) {
				BlockEntity blockentity = this.level().getBlockEntity(blockpos);
				if (blockentity instanceof TheEndGatewayBlockEntity && TheEndGatewayBlockEntity.canEntityTeleport(this)) {
					TheEndGatewayBlockEntity.teleportEntity(this.level(), blockpos, blockstate, this, (TheEndGatewayBlockEntity) blockentity);
				}
				flag = true;
			}
		}
		if (hitresult.getType() != HitResult.Type.MISS && !flag && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, hitresult)) {
			this.onHit(hitresult);
		}
		this.checkInsideBlocks();
		damakuMove();
		life--;
		if (life == 0) {
			level().broadcastEntityEvent(this, EntityEvent.DEATH);
			discard();
		}
	}

	protected void onHit(HitResult pResult) {
		super.onHit(pResult);
		if (!level().isClientSide) {
			level().broadcastEntityEvent(this, EntityEvent.DEATH);
			discard();
		}
	}

	@Override
	protected void onHitEntity(EntityHitResult result) {
		result.getEntity().hurt(YHDamageTypes.damaku(this), damage);
	}

	public void handleEntityEvent(byte pId) {
		if (pId == EntityEvent.DEATH) {
			clientDeathParticle();
		}
	}

	protected void clientDeathParticle() {

	}

	protected void damakuMove() {
		Vec3 vec3 = updateVelocity(getDeltaMovement());
		setDeltaMovement(vec3);
		this.updateRotation();
		double d2 = this.getX() + vec3.x;
		double d0 = this.getY() + vec3.y;
		double d1 = this.getZ() + vec3.z;
		this.setPos(d2, d0, d1);
	}

	protected Vec3 updateVelocity(Vec3 vec) {
		return vec;
	}

	@Override
	protected void defineSynchedData() {

	}

}
