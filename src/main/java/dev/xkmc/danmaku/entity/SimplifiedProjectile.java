package dev.xkmc.danmaku.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.UUID;

public abstract class SimplifiedProjectile extends SimplifiedEntity implements TraceableEntity {

	@Nullable
	private UUID ownerUUID;
	@Nullable
	private Entity cachedOwner;

	public SimplifiedProjectile(EntityType<?> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	/**
	 * Updates the entity motion clientside, called by packets from the server
	 */
	public void lerpMotion(double pX, double pY, double pZ) {
		this.setDeltaMovement(pX, pY, pZ);
		if (this.xRotO == 0.0F && this.yRotO == 0.0F) {
			double d0 = Math.sqrt(pX * pX + pZ * pZ);
			this.setXRot((float) (Mth.atan2(pY, d0) * (double) (180F / (float) Math.PI)));
			this.setYRot((float) (Mth.atan2(pX, pZ) * (double) (180F / (float) Math.PI)));
			this.xRotO = this.getXRot();
			this.yRotO = this.getYRot();
			this.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), this.getXRot());
		}

	}

	protected void updateRotation() {
		Vec3 vec3 = this.getDeltaMovement();
		double d0 = vec3.horizontalDistance();
		this.setXRot(lerpRotation(this.xRotO, (float) (Mth.atan2(vec3.y, d0) * (double) (180F / (float) Math.PI))));
		this.setYRot(lerpRotation(this.yRotO, (float) (Mth.atan2(vec3.x, vec3.z) * (double) (180F / (float) Math.PI))));
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


	public void setOwner(@Nullable Entity pOwner) {
		if (pOwner != null) {
			this.ownerUUID = pOwner.getUUID();
			this.cachedOwner = pOwner;
		}
	}

	/**
	 * Returns null or the entityliving it was ignited by
	 */
	@Nullable
	public Entity getOwner() {
		if (this.cachedOwner != null && !this.cachedOwner.isRemoved()) {
			return this.cachedOwner;
		} else if (this.ownerUUID != null && this.level() instanceof ServerLevel) {
			this.cachedOwner = ((ServerLevel) this.level()).getEntity(this.ownerUUID);
			return this.cachedOwner;
		} else {
			return null;
		}
	}

	protected void addAdditionalSaveData(CompoundTag pCompound) {
		if (this.ownerUUID != null) {
			pCompound.putUUID("Owner", this.ownerUUID);
		}
	}

	protected boolean ownedBy(Entity pEntity) {
		return pEntity.getUUID().equals(this.ownerUUID);
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	protected void readAdditionalSaveData(CompoundTag pCompound) {
		if (pCompound.hasUUID("Owner")) {
			this.ownerUUID = pCompound.getUUID("Owner");
			this.cachedOwner = null;
		}
	}

}
