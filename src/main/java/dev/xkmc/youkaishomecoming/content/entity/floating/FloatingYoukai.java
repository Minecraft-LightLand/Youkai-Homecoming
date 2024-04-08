package dev.xkmc.youkaishomecoming.content.entity.floating;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.codec.TagCodec;
import dev.xkmc.l2serial.util.Wrappers;
import dev.xkmc.youkaishomecoming.content.entity.rumia.Rumia;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Objects;

@SerialClass
public class FloatingYoukai extends Monster {

	private static final int GROUND_HEIGHT = 5, ATTEMPT_ABOVE = 3;

	public final MoveControl walkCtrl, flyCtrl;
	public final PathNavigation walkNav, fltNav;

	private static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(FloatingYoukai.class, EntityDataSerializers.BYTE);

	public FloatingYoukai(EntityType<? extends FloatingYoukai> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
		this.walkCtrl = moveControl;
		this.walkNav = navigation;
		this.flyCtrl = new FlyingMoveControl(this, 10, false);
		this.fltNav = new FlyingPathNavigation(this, level());
	}

	protected SoundEvent getAmbientSound() {
		return SoundEvents.CAT_AMBIENT;
	}

	protected SoundEvent getHurtSound(DamageSource pDamageSource) {
		return SoundEvents.CAT_HURT;
	}

	protected SoundEvent getDeathSound() {
		return SoundEvents.CAT_DEATH;
	}

	// base

	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(DATA_FLAGS_ID, (byte) 0);
	}

	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.put("auto-serial", Objects.requireNonNull(TagCodec.toTag(new CompoundTag(), this)));

	}

	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		if (tag.contains("auto-serial")) {
			Wrappers.run(() -> TagCodec.fromTag(tag.getCompound("auto-serial"), getClass(), this, (f) -> true));
		}

	}

	public boolean getFlag(int flag) {
		return (this.entityData.get(DATA_FLAGS_ID) & flag) != 0;
	}

	public void setFlag(int flag, boolean enable) {
		byte b0 = this.entityData.get(DATA_FLAGS_ID);
		if (enable) {
			b0 = (byte) (b0 | flag);
		} else {
			b0 = (byte) (b0 & (-1 - flag));
		}
		this.entityData.set(DATA_FLAGS_ID, b0);
	}

	// flying

	public final void setFlying() {
		if (moveControl == flyCtrl) return;
		getNavigation().stop();
		moveControl = flyCtrl;
		navigation = fltNav;
		setNoGravity(true);
	}

	public final void setWalking() {
		if (moveControl == walkCtrl) return;
		getNavigation().stop();
		moveControl = walkCtrl;
		navigation = walkNav;
		setNoGravity(false);
	}

	@Override
	public boolean isInvulnerableTo(DamageSource pSource) {
		return pSource.is(DamageTypeTags.IS_FALL);
	}

	public void aiStep() {
		if (!this.onGround() && this.getDeltaMovement().y < 0.0D) {
			this.setDeltaMovement(this.getDeltaMovement().multiply(1.0D, 0.6D, 1.0D));
		}
		super.aiStep();
	}

	protected void customServerAiStep() {
		LivingEntity target = this.getTarget();
		if (target != null && this.canAttack(target) && moveControl == flyCtrl) {
			boolean tooHigh = tooHigh();
			int expectedHeight = tooHigh ? 0 : ATTEMPT_ABOVE;
			double low = -0.5;
			double high = tooHigh ? 0 : 0.5;
			double diff = target.getEyeY() + expectedHeight - getEyeY();
			Vec3 vec3 = this.getDeltaMovement();
			double moveY = vec3.y * 0.5 + diff * 0.02;
			if (getEyeY() < target.getEyeY() + expectedHeight + low) {
				setY(Math.max(vec3.y, moveY));
			}
			if (getEyeY() > target.getEyeY() + expectedHeight + high) {
				if (diff < -1) {
					setY(Math.min(vec3.y, moveY));
				} else if (tooHigh) {
					setY(Math.min(vec3.y, -0.01));
				}
			}
		}

		super.customServerAiStep();
	}

	private void setY(double vy) {
		Vec3 vec3 = getDeltaMovement();
		setDeltaMovement(vec3.x, vy, vec3.z);
		if (vy > 0 && onGround()) {
			hasImpulse = true;
		}
	}

	private boolean tooHigh() {
		BlockPos pos = getOnPos();
		for (int i = 0; i < GROUND_HEIGHT; i++) {
			BlockPos off = pos.offset(0, -i, 0);
			if (!level().getBlockState(off).isAir()) return false;
		}
		return true;
	}

}