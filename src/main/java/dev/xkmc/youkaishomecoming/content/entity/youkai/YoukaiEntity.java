package dev.xkmc.youkaishomecoming.content.entity.youkai;

import dev.xkmc.danmaku.entity.SimplifiedProjectile;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.codec.TagCodec;
import dev.xkmc.l2serial.util.Wrappers;
import dev.xkmc.spellcircle.SpellCircleHolder;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.IYHDanmaku;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.ItemDanmakuEntity;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.ItemLaserEntity;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.CardHolder;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.SpellCardWrapper;
import dev.xkmc.youkaishomecoming.init.data.YHModConfig;
import dev.xkmc.youkaishomecoming.init.registrate.YHDanmaku;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import dev.xkmc.youkaishomecoming.init.registrate.YHEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@SerialClass
public abstract class YoukaiEntity extends Monster implements SpellCircleHolder, CardHolder {

	private static final int GROUND_HEIGHT = 5, ATTEMPT_ABOVE = 3;

	private static <T> EntityDataAccessor<T> defineId(EntityDataSerializer<T> ser) {
		return SynchedEntityData.defineId(YoukaiEntity.class, ser);
	}

	protected static final SyncedData YOUKAI_DATA = new SyncedData(YoukaiEntity::defineId);

	protected static final EntityDataAccessor<Integer> DATA_FLAGS_ID = YOUKAI_DATA.define(SyncedData.INT, 0, "youkai_flags");

	public final MoveControl walkCtrl, flyCtrl;
	public final PathNavigation walkNav, fltNav;

	@SerialClass.SerialField
	public final YoukaiTargetContainer targets;

	@SerialClass.SerialField
	public SpellCardWrapper spellCard;

	public YoukaiEntity(EntityType<? extends YoukaiEntity> pEntityType, Level pLevel) {
		this(pEntityType, pLevel, 10);
	}

	public YoukaiEntity(EntityType<? extends YoukaiEntity> pEntityType, Level pLevel, int maxSize) {
		super(pEntityType, pLevel);
		this.walkCtrl = moveControl;
		this.walkNav = navigation;
		this.flyCtrl = new FlyingMoveControl(this, 10, false);
		this.fltNav = new FlyingPathNavigation(this, level());
		this.targets = new YoukaiTargetContainer(this, maxSize);
	}

	protected SoundEvent getAmbientSound() {
		return SoundEvents.EMPTY;
	}

	protected SoundEvent getHurtSound(DamageSource pDamageSource) {
		return SoundEvents.EMPTY;
	}

	protected SoundEvent getDeathSound() {
		return SoundEvents.EMPTY;
	}

	// base

	protected SyncedData data() {
		return YOUKAI_DATA;
	}

	protected void defineSynchedData() {
		super.defineSynchedData();
		data().register(entityData);
	}

	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putInt("Age", tickCount);
		tag.put("auto-serial", Objects.requireNonNull(TagCodec.toTag(new CompoundTag(), this)));
		data().write(tag, entityData);
	}

	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		tickCount = tag.getInt("Age");
		if (tag.contains("auto-serial")) {
			Wrappers.run(() -> TagCodec.fromTag(tag.getCompound("auto-serial"), getClass(), this, (f) -> true));
		}
		data().read(tag, entityData);
	}

	public boolean getFlag(int flag) {
		return (this.entityData.get(DATA_FLAGS_ID) & flag) != 0;
	}

	public void setFlag(int flag, boolean enable) {
		int b0 = this.entityData.get(DATA_FLAGS_ID);
		if (enable) {
			b0 = (byte) (b0 | flag);
		} else {
			b0 = (byte) (b0 & (-1 - flag));
		}
		this.entityData.set(DATA_FLAGS_ID, b0);
	}

	// features

	@Override
	public boolean isInvulnerableTo(DamageSource pSource) {
		return pSource.is(DamageTypeTags.IS_FALL);
	}

	@Override
	protected boolean canRide(Entity pVehicle) {
		return false;
	}

	@Override
	protected boolean shouldDespawnInPeaceful() {
		return false;
	}

	@Override
	public Vec3 center() {
		return position().add(0, getBbHeight() / 2, 0);
	}

	@Override
	public Vec3 forward() {
		var target = target();
		if (target != null) {
			return target.subtract(center()).normalize();
		}
		return getForward();
	}

	@Override
	public @Nullable Vec3 target() {
		var le = getTarget();
		if (le == null) return null;
		return le.position().add(0, le.getBbHeight() / 2, 0);
	}

	@Override
	public RandomSource random() {
		return random;
	}

	@Override
	public ItemDanmakuEntity prepareDanmaku(int life, Vec3 vec, YHDanmaku.Bullet type, DyeColor color) {
		ItemDanmakuEntity danmaku = new ItemDanmakuEntity(YHEntities.ITEM_DANMAKU.get(), this, level());
		danmaku.setItem(type.get(color).asStack());
		danmaku.setup((float) getAttributeValue(Attributes.ATTACK_DAMAGE),
				life, true, true, vec);
		return danmaku;
	}

	@Override
	public ItemLaserEntity prepareLaser(int life, Vec3 pos, Vec3 vec, int len, YHDanmaku.Laser type, DyeColor color) {
		ItemLaserEntity danmaku = new ItemLaserEntity(YHEntities.ITEM_LASER.get(), this, level());
		danmaku.setItem(type.get(color).asStack());
		danmaku.setup((float) getAttributeValue(Attributes.ATTACK_DAMAGE),
				life, len, true, vec);
		danmaku.setPos(pos);
		return danmaku;
	}

	@Override
	public void shoot(SimplifiedProjectile danmaku) {
		level().addFreshEntity(danmaku);
	}

	public void shoot(float dmg, int life, Vec3 vec, DyeColor color) {
		ItemDanmakuEntity danmaku = new ItemDanmakuEntity(YHEntities.ITEM_DANMAKU.get(), this, level());
		danmaku.setItem(YHDanmaku.Bullet.CIRCLE.get(color).asStack());
		danmaku.setup(dmg, life, true, true, vec);
		level().addFreshEntity(danmaku);
	}

	public void onDanmakuHit(LivingEntity e, IYHDanmaku danmaku) {
		if (e instanceof YoukaiEntity || e.hasEffect(YHEffects.YOUKAIFIED.get())) return;
		if (targets.contains(e)) {
			double heal = YHModConfig.COMMON.danmakuHealOnHitTarget.get();
			heal(getMaxHealth() * (float) heal);
		}
	}

	// flying

	public final void setFlying() {
		setNoGravity(true);
		if (moveControl == flyCtrl) return;
		getNavigation().stop();
		moveControl = flyCtrl;
		navigation = fltNav;
	}

	public final void setWalking() {
		setNoGravity(false);
		if (moveControl == walkCtrl) return;
		getNavigation().stop();
		setYya(0);
		setXxa(0);
		setZza(0);
		moveControl = walkCtrl;
		navigation = walkNav;
	}

	public final boolean isFlying() {
		return moveControl == flyCtrl;
	}

	public void aiStep() {
		if (!level().isClientSide()) {
			if (!this.onGround() && this.getDeltaMovement().y < 0.0D) {
				this.setDeltaMovement(this.getDeltaMovement().multiply(1.0D, 0.6D, 1.0D));
			}
			targets.tick();
			if (spellCard != null) {
				if (isAggressive() && shouldShowSpellCircle()) {
					spellCard.tick(this);
				} else {
					spellCard.reset();
				}
			}
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