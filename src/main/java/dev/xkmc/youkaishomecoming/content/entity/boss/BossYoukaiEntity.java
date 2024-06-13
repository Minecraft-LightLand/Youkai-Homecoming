package dev.xkmc.youkaishomecoming.content.entity.boss;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.entity.youkai.GeneralYoukaiEntity;
import dev.xkmc.youkaishomecoming.content.entity.youkai.YoukaiEntity;
import dev.xkmc.youkaishomecoming.init.data.YHDamageTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.Nullable;

@SerialClass
public class BossYoukaiEntity extends GeneralYoukaiEntity {

	public static AttributeSupplier.Builder createAttributes() {
		return YoukaiEntity.createAttributes()
				.add(Attributes.MAX_HEALTH, 200)
				.add(Attributes.ATTACK_DAMAGE, 10)
				.add(Attributes.FOLLOW_RANGE, 128);
	}

	protected final ServerBossEvent bossEvent = new ServerBossEvent(getDisplayName(), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.NOTCHED_20);

	public BossYoukaiEntity(EntityType<? extends BossYoukaiEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	@Override
	public boolean canBeAffected(MobEffectInstance ins) {
		return false;
	}

	@Override
	public void aiStep() {
		if (hurtCD < 1000) hurtCD++;
		super.aiStep();
		if (!getActiveEffectsMap().isEmpty()) {
			removeAllEffects();
		}
	}

	private int hurtCD = 0;
	private boolean hurtCall = false;

	private int getCD(DamageSource source) {
		if (source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
			return 10;
		} else if (source.is(YHDamageTypes.DANMAKU)) {
			return 20;
		} else if (source.is(DamageTypeTags.BYPASSES_COOLDOWN)) {
			return 40;
		} else {
			return 80;
		}
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (!source.is(DamageTypeTags.BYPASSES_INVULNERABILITY) &&
				!(source.getEntity() instanceof LivingEntity))
			return false;
		int cd = getCD(source);
		if (hurtCD < cd) {
			return false;
		}
		hurtCD = 0;
		hurtCall = true;
		boolean ans = super.hurt(source, amount);
		hurtCall = false;
		return ans;
	}

	protected float clampDamage(DamageSource source, float amount) {
		if (!hurtCall) return 0;
		if (source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
			if (source.getEntity() instanceof LivingEntity le) {
				if (le instanceof ServerPlayer sp) {
					if (sp.isCreative()) {
						return amount;
					}
				}
			} else {
				if (source.is(DamageTypes.FELL_OUT_OF_WORLD))
					return Math.min(4, amount);
				if (source.is(DamageTypes.GENERIC_KILL)) {
					return amount;
				}
			}
		}
		int reduction = 20;
		amount = Math.min(getMaxHealth() / reduction, amount);
		if (!source.is(YHDamageTypes.DANMAKU_TYPE))
			amount /= 5;
		return amount;
	}

	@Override
	protected final void actuallyHurt(DamageSource source, float amount) {
		if (!hurtCall) return;
		super.actuallyHurt(source, amount);
	}

	@Override
	protected void hurtFinal(DamageSource source, float amount) {
		amount = clampDamage(source, amount);
		super.hurtFinal(source, amount);
	}

	@Override
	public void setHealth(float val) {
		if (level().isClientSide()) {
			super.setHealth(val);
		}
		float health = getHealth();
		if (tickCount > 5 && val <= health) return;
		super.setHealth(val);
	}

	public void heal(float original) {
		var heal = ForgeEventFactory.onLivingHeal(this, original);
		heal = Math.max(original, heal);
		if (heal <= 0) return;
		float f = getHealth();
		if (f > 0) {
			setHealth(f + heal);
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		if (hasCustomName()) {
			bossEvent.setName(getDisplayName());
		}
	}

	public void setCustomName(@Nullable Component pName) {
		super.setCustomName(pName);
		bossEvent.setName(getDisplayName());
	}

	protected int noTargetTime;

	@Override
	protected void customServerAiStep() {
		super.customServerAiStep();
		bossEvent.setProgress(getHealth() / getMaxHealth());
		if (getTarget() == null || !getTarget().isAlive()) {
			noTargetTime++;
			if (noTargetTime >= 20 && tickCount % 20 == 0) {
				if (getHealth() < getMaxHealth())
					setHealth(getMaxHealth());
			}
		} else {
			noTargetTime = 0;
		}
	}

	public void startSeenByPlayer(ServerPlayer pPlayer) {
		super.startSeenByPlayer(pPlayer);
		this.bossEvent.addPlayer(pPlayer);
	}

	public void stopSeenByPlayer(ServerPlayer pPlayer) {
		super.stopSeenByPlayer(pPlayer);
		this.bossEvent.removePlayer(pPlayer);
	}

}
