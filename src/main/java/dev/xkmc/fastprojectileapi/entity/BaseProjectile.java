package dev.xkmc.fastprojectileapi.entity;

import dev.xkmc.fastprojectileapi.collision.ProjectileHitHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public abstract class BaseProjectile extends SimplifiedProjectile {

	protected BaseProjectile(EntityType<? extends BaseProjectile> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	@Override
	protected void defineSynchedData() {

	}

	public abstract boolean checkBlockHit();

	public abstract int lifetime();

	public void tick() {
		super.tick();
		HitResult hitresult = ProjectileHitHelper.getHitResultOnMoveVector(this, checkBlockHit());
		if (hitresult != null) {
			onHit(hitresult);
		}
		projectileMove();
		if (tickCount >= lifetime()) {
			if (!level().isClientSide()) {
				discard();
			}
		}
	}

	protected void projectileMove() {
		ProjectileMovement movement = updateVelocity(getDeltaMovement(), position());
		setDeltaMovement(movement.vec());
		updateRotation(movement.rot());
		double d2 = getX() + movement.vec().x;
		double d0 = getY() + movement.vec().y;
		double d1 = getZ() + movement.vec().z;
		setPos(d2, d0, d1);
	}

	protected ProjectileMovement updateVelocity(Vec3 vec, Vec3 pos) {
		return ProjectileMovement.of(vec);
	}

	public boolean shouldRenderAtSqrDistance(double pDistance) {
		double d0 = getBoundingBox().getSize() * 4;
		if (Double.isNaN(d0)) d0 = 4;
		d0 *= 64;
		return pDistance < d0 * d0;
	}

	protected void onHit(HitResult hitresult) {
		if (hitresult.getType() == HitResult.Type.MISS) return;
		if (hitresult instanceof EntityHitResult ehit) {
			onHitEntity(ehit);
			level().gameEvent(GameEvent.PROJECTILE_LAND, hitresult.getLocation(), GameEvent.Context.of(this, null));
		} else if (hitresult instanceof BlockHitResult bhit) {
			BlockPos pos = bhit.getBlockPos();
			BlockState state = level().getBlockState(pos);
			if (state.is(Blocks.NETHER_PORTAL)) {
				handleInsidePortal(pos);
				return;
			} else if (state.is(Blocks.END_GATEWAY)) {
				BlockEntity be = level().getBlockEntity(pos);
				if (be instanceof TheEndGatewayBlockEntity gate && TheEndGatewayBlockEntity.canEntityTeleport(this)) {
					TheEndGatewayBlockEntity.teleportEntity(level(), pos, state, this, gate);
				}
				return;
			}
			onHitBlock(bhit);
			level().gameEvent(GameEvent.PROJECTILE_LAND, pos, GameEvent.Context.of(this, level().getBlockState(pos)));
		}
	}

	protected void onHitEntity(EntityHitResult pResult) {
	}

	protected void onHitBlock(BlockHitResult pResult) {
	}

}
