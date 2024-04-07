package dev.xkmc.youkaishomecoming.content.entity.rumia;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class Rumia extends Monster {

	private static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(Rumia.class, EntityDataSerializers.BYTE);

	private float allowedHeightOffset = 0.5F;
	private int nextHeightOffsetChangeTick;

	public Rumia(EntityType<? extends Rumia> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	protected void registerGoals() {
		this.goalSelector.addGoal(4, new RumiaAttackGoal(this));
		this.goalSelector.addGoal(5, new MoveTowardsRestrictionGoal(this, 1.0));
		this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 0.8));
		this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 24));
		this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes().add(Attributes.ATTACK_DAMAGE, 6.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.23)
				.add(Attributes.FOLLOW_RANGE, 48);
	}

	@Override
	protected void actuallyHurt(DamageSource pDamageSource, float amount) {
		super.actuallyHurt(pDamageSource, Math.max(8, amount));//TODO config
	}

	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(DATA_FLAGS_ID, (byte) 0);
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

	public void aiStep() {
		if (!this.onGround() && this.getDeltaMovement().y < 0.0D) {
			this.setDeltaMovement(this.getDeltaMovement().multiply(1.0D, 0.6D, 1.0D));
		}
		super.aiStep();
	}

	protected void customServerAiStep() {
		--this.nextHeightOffsetChangeTick;
		if (this.nextHeightOffsetChangeTick <= 0) {
			this.nextHeightOffsetChangeTick = 100;
			this.allowedHeightOffset = (float) this.random.triangle(0.5D, 6.891D);
		}

		LivingEntity livingentity = this.getTarget();
		if (livingentity != null && livingentity.getEyeY() > this.getEyeY() + (double) this.allowedHeightOffset && this.canAttack(livingentity)) {
			Vec3 vec3 = this.getDeltaMovement();
			this.setDeltaMovement(this.getDeltaMovement().add(0.0D, ((double) 0.3F - vec3.y) * (double) 0.3F, 0.0D));
			this.hasImpulse = true;
		}

		super.customServerAiStep();
	}

	private boolean isCharged() {
		return (this.entityData.get(DATA_FLAGS_ID) & 1) != 0;
	}

	void setCharged(boolean charged) {
		byte b0 = this.entityData.get(DATA_FLAGS_ID);
		if (charged) {
			b0 = (byte) (b0 | 1);
		} else {
			b0 = (byte) (b0 & -2);
		}
		this.entityData.set(DATA_FLAGS_ID, b0);
	}

}