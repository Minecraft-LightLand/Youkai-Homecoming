package dev.xkmc.youkaishomecoming.content.entity.animal.tuna;

import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.network.NetworkHooks;

public class TunaEntity extends AbstractFish {

	public TunaEntity(EntityType<? extends TunaEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	public Packet<ClientGamePacketListener> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	public ItemStack getBucketItemStack() {
		return YHItems.TUNA_BUCKET.asStack();
	}

	protected SoundEvent getAmbientSound() {
		return SoundEvents.SALMON_AMBIENT;
	}

	protected SoundEvent getDeathSound() {
		return SoundEvents.SALMON_DEATH;
	}

	protected SoundEvent getHurtSound(DamageSource pDamageSource) {
		return SoundEvents.SALMON_HURT;
	}

	protected SoundEvent getFlopSound() {
		return SoundEvents.SALMON_FLOP;
	}

	public static AttributeSupplier.Builder createAttributes() {
		AttributeSupplier.Builder builder = Mob.createMobAttributes();
		builder = builder.add(Attributes.MOVEMENT_SPEED, 1.5);
		builder = builder.add(Attributes.MAX_HEALTH, 40);
		builder = builder.add(Attributes.ARMOR, 4);
		builder = builder.add(Attributes.ATTACK_DAMAGE, 8);
		builder = builder.add(Attributes.FOLLOW_RANGE, 16);
		builder = builder.add(ForgeMod.SWIM_SPEED.get(), 1.5);
		return builder;
	}

	public float getSwimSpeed() {
		return (float) getAttributeValue(ForgeMod.SWIM_SPEED.get()) * 0.02f;
	}

	@Override
	protected void registerGoals() {
		goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.5, false));
		goalSelector.addGoal(4, new RandomSwimmingGoal(this, 1, 3));
		targetSelector.addGoal(0, new HurtByTargetGoal(this));
		targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, WaterAnimal.class, true,
				this::wouldAttack));
	}

	public boolean wouldAttack(LivingEntity e) {
		if (hasEffect(MobEffects.SATURATION)) return false;
		if (e instanceof WaterAnimal fish) {
			if (fish.getType() == getType()) {
				return false;
			}
			var box = fish.getBoundingBox();
			return box.getXsize() * box.getYsize() * box.getZsize() <= 0.2;
		}
		return false;
	}

	protected void handleAirSupply(int del) {
		if (isAlive() && !isInWaterOrBubble()) {
			setAirSupply(del - 1);
			if (getAirSupply() <= -15) {
				setAirSupply(0);
				hurt(damageSources().drown(), 4);
			}
		} else {
			setAirSupply(60);
		}

	}

	@Override
	public boolean canBeAffected(MobEffectInstance ins) {
		if (ins.getEffect() == MobEffects.POISON) return false;
		return super.canBeAffected(ins);
	}

	@Override
	public boolean doHurtTarget(Entity e) {
		boolean ans = super.doHurtTarget(e);
		if (ans && !e.isAlive()) {
			if (e instanceof WaterAnimal le) {
				int time = (int) (le.getMaxHealth() * 600);
				addEffect(new MobEffectInstance(MobEffects.SATURATION, time));
			}
			e.remove(Entity.RemovalReason.KILLED);
		}
		return ans;
	}

	@Override
	protected InteractionResult mobInteract(Player player, InteractionHand hand) {
		if (!player.getAbilities().instabuild) return InteractionResult.PASS;
		return super.mobInteract(player, hand);
	}

	public void travel(Vec3 vec) {
		if (isEffectiveAi() && isInWater()) {
			moveRelative(getSwimSpeed(), vec);
			move(MoverType.SELF, getDeltaMovement());
			setDeltaMovement(getDeltaMovement().scale(0.9D));
			if (getTarget() == null) {
				setDeltaMovement(getDeltaMovement().add(0.0D, -0.005D, 0.0D));
			}
		} else {
			super.travel(vec);
		}

	}

}
