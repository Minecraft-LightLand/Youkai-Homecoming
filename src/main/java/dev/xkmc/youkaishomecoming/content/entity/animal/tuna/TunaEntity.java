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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.AbstractSchoolingFish;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.network.NetworkHooks;

public class TunaEntity extends AbstractSchoolingFish {

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
		builder = builder.add(Attributes.MOVEMENT_SPEED, 0.7);
		builder = builder.add(Attributes.MAX_HEALTH, 40);
		builder = builder.add(Attributes.ARMOR, 4);
		builder = builder.add(Attributes.ATTACK_DAMAGE, 8);
		builder = builder.add(Attributes.FOLLOW_RANGE, 16);
		builder = builder.add(ForgeMod.SWIM_SPEED.get(), 0.7);
		return builder;
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		goalSelector.addGoal(1, new MeleeAttackGoal(this, 1, false));
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
			if (fish.getBoundingBox().getSize() > 0.2) {
				return false;
			}
		}
		return false;
	}

	protected void handleAirSupply(int del) {
		if (this.isAlive() && !this.isInWaterOrBubble()) {
			this.setAirSupply(del - 1);
			if (this.getAirSupply() <= -15) {
				this.setAirSupply(0);
				this.hurt(this.damageSources().drown(), 4);
			}
		} else {
			this.setAirSupply(60);
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
				int time = (int) (le.getMaxHealth() * 20);
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

}
