package dev.xkmc.youkaishomecoming.content.entity.animal.lampery;

import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.animal.AbstractSchoolingFish;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.network.NetworkHooks;

public class LampreyEntity extends AbstractFish {

	public LampreyEntity(EntityType<? extends LampreyEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	public Packet<ClientGamePacketListener> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	public ItemStack getBucketItemStack() {
		return YHItems.LAMPREY_BUCKET.asStack();
	}

	protected SoundEvent getAmbientSound() {
		return SoundEvents.COD_AMBIENT;
	}

	protected SoundEvent getDeathSound() {
		return SoundEvents.COD_DEATH;
	}

	protected SoundEvent getHurtSound(DamageSource pDamageSource) {
		return SoundEvents.COD_HURT;
	}

	protected SoundEvent getFlopSound() {
		return SoundEvents.COD_FLOP;
	}

	public static AttributeSupplier.Builder createAttributes() {
		AttributeSupplier.Builder builder = Mob.createMobAttributes();
		builder = builder.add(Attributes.MOVEMENT_SPEED, 0.5);
		builder = builder.add(Attributes.MAX_HEALTH, 3.0);
		builder = builder.add(Attributes.ARMOR, 0.0);
		builder = builder.add(Attributes.ATTACK_DAMAGE, 1);
		builder = builder.add(Attributes.FOLLOW_RANGE, 16.0);
		builder = builder.add(ForgeMod.SWIM_SPEED.get(), 0.5);
		return builder;
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		goalSelector.addGoal(1, new MeleeAttackGoal(this, 1, false));
		targetSelector.addGoal(0, new HurtByTargetGoal(this));
		targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, WaterAnimal.class, true,
				this::canAttack));
	}

	@Override
	public boolean canAttack(LivingEntity e) {
		if (e instanceof WaterAnimal fish) {
			if (fish.getType() == getType()) {
				return false;
			}
			if (fish.getHealth() < fish.getMaxHealth()) {
				return false;
			}
		}
		return super.canAttack(e);
	}
}
