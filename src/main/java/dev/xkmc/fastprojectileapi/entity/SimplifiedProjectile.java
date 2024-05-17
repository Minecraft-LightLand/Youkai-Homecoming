package dev.xkmc.fastprojectileapi.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;

import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.UUID;

public abstract class SimplifiedProjectile extends SimplifiedEntity implements TraceableEntity, IEntityAdditionalSpawnData {

	@Nullable
	private UUID ownerUUID;
	@Nullable
	private Entity cachedOwner;

	public SimplifiedProjectile(EntityType<?> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	public boolean canHitEntity(Entity target) {
		if (!target.canBeHitByProjectile()) {
			return false;
		} else {
			Entity entity = getOwner();
			if (entity == null || entity == target) return false;
			if (entity.isPassenger() || target.isPassenger()) {
				if (entity.isPassengerOfSameVehicle(target)) {
					return false;
				}
			}
			return !entity.isAlliedTo(target);
		}
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

	public void setOwner(@Nullable Entity pOwner) {
		if (pOwner != null) {
			ownerUUID = pOwner.getUUID();
			cachedOwner = pOwner;
		}
	}

	@Nullable
	public Entity getOwner() {
		if (cachedOwner != null && !cachedOwner.isRemoved()) {
			return cachedOwner;
		} else if (ownerUUID != null && level() instanceof ServerLevel) {
			cachedOwner = ((ServerLevel) level()).getEntity(ownerUUID);
			return cachedOwner;
		} else {
			return null;
		}
	}

	@OverridingMethodsMustInvokeSuper
	protected void addAdditionalSaveData(CompoundTag nbt) {
		if (ownerUUID != null) {
			nbt.putUUID("Owner", ownerUUID);
		}
		nbt.putInt("Age", tickCount);
	}

	@OverridingMethodsMustInvokeSuper
	protected void readAdditionalSaveData(CompoundTag nbt) {
		if (nbt.hasUUID("Owner")) {
			ownerUUID = nbt.getUUID("Owner");
			cachedOwner = null;
		}
		tickCount = nbt.getInt("Age");
	}

	@OverridingMethodsMustInvokeSuper
	@Override
	public void writeSpawnData(FriendlyByteBuf buffer) {
		buffer.writeLong(level().getGameTime() - tickCount);
		var owner = getOwner();
		buffer.writeInt(owner == null ? -1 : owner.getId());
	}

	@OverridingMethodsMustInvokeSuper
	@Override
	public void readSpawnData(FriendlyByteBuf additionalData) {
		tickCount = (int) (level().getGameTime() - additionalData.readInt());
		int id = additionalData.readInt();
		if (id >= 0) {
			var e = level().getEntity(id);
			if (e != null) {
				setOwner(e);
			}
		}
	}

}
