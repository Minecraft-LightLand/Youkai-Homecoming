package dev.xkmc.youkaishomecoming.content.spell.shooter;

import dev.xkmc.fastprojectileapi.entity.ProjectileMovement;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public abstract class ProjectileHealthEntity extends BaseHealthEntity {

	protected ProjectileHealthEntity(EntityType<? extends LivingEntity> type, Level level) {
		super(type, level);
	}

	public abstract int lifetime();

	public void tick() {
		super.tick();
		if (tickCount >= lifetime()) {
			if (!level().isClientSide()) {
				discard();
			}
		} else {
			projectileMove();
			if (!level().isClientSide()) {
				if (!level().hasChunk(blockPosition().getX() >> 4, blockPosition().getZ() >> 4)) {
					discard();
				}
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

	public void lerpMotion(double pX, double pY, double pZ) {
		setDeltaMovement(pX, pY, pZ);
		if (xRotO == 0.0F && yRotO == 0.0F) {
			double d0 = Math.sqrt(pX * pX + pZ * pZ);
			setXRot((float) -(Mth.atan2(pY, d0) * Mth.RAD_TO_DEG));
			setYRot((float) -(Mth.atan2(pX, pZ) * Mth.RAD_TO_DEG));
			xRotO = getXRot();
			yRotO = getYRot();
			moveTo(getX(), getY(), getZ(), getYRot(), getXRot());
		}

	}

	public Vec3 rot() {
		return new Vec3(getXRot() * Mth.DEG_TO_RAD, getYRot() * Mth.DEG_TO_RAD, 0);
	}

	protected void updateRotation(Vec3 rot) {
		setXRot(lerpRotation(xRotO, (float) (rot.x * Mth.RAD_TO_DEG)));
		setYRot(lerpRotation(yRotO, (float) (rot.y * Mth.RAD_TO_DEG)));
	}

	protected static float lerpRotation(float pCurrentRotation, float pTargetRotation) {
		while (pTargetRotation - pCurrentRotation < -180.0F) {
			pCurrentRotation -= 360.0F;
		}

		while (pTargetRotation - pCurrentRotation >= 180.0F) {
			pCurrentRotation += 360.0F;
		}

		return Mth.lerp(0.2F, pCurrentRotation, pTargetRotation);
	}

}
