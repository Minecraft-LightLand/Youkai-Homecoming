package dev.xkmc.youkaishomecoming.content.entity.boss;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.entity.youkai.GeneralYoukaiEntity;
import dev.xkmc.youkaishomecoming.content.entity.youkai.YoukaiEntity;
import dev.xkmc.youkaishomecoming.init.data.YHDamageTypes;
import dev.xkmc.youkaishomecoming.init.data.YHModConfig;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.BossEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fluids.FluidType;
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

	protected boolean wouldAttack(LivingEntity entity) {
		if (shouldIgnore(entity)) return false;
		return entity.hasEffect(YHEffects.YOUKAIFIED.get());
	}

	@Override
	public boolean shouldHurt(LivingEntity le) {
		if (shouldIgnore(le)) return false;
		return le instanceof Enemy || super.shouldHurt(le);
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
		if (!YHModConfig.COMMON.reimuExtraDamageCoolDown.get())
			return 10;
		if (source.is(DamageTypeTags.BYPASSES_INVULNERABILITY))
			return 10;
		if (source.getEntity() instanceof Player pl && pl.getAbilities().instabuild)
			return 10;
		if (source.is(YHDamageTypes.DANMAKU))
			return 20;
		if (source.is(DamageTypeTags.BYPASSES_COOLDOWN))
			return 40;
		return 80;
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (!source.is(DamageTypes.GENERIC_KILL) || source.getEntity() != null) {
			if (!source.is(DamageTypeTags.BYPASSES_INVULNERABILITY) &&
					!(source.getEntity() instanceof LivingEntity))
				return false;
			if (source.getEntity() instanceof LivingEntity le) {
				if (shouldIgnore(le)) return false;
			}
			int cd = getCD(source);
			if (hurtCD < cd) {
				return false;
			}
		}
		hurtCD = 0;
		hurtCall = true;
		boolean ans = super.hurt(source, amount);
		hurtCall = false;
		return ans;
	}

	@Override
	public boolean canSwimInFluidType(FluidType type) {
		return true;
	}

	@Override
	public boolean fireImmune() {
		return true;
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
		if (YHModConfig.COMMON.reimuDamageReduction.get() && !source.is(YHDamageTypes.DANMAKU_TYPE))
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

	@SerialClass.SerialField
	protected int noTargetTime;

	@Override
	protected void customServerAiStep() {
		super.customServerAiStep();
		bossEvent.setProgress(getHealth() / getMaxHealth());
		if (getTarget() == null || !getTarget().isAlive()) {
			noTargetTime++;
			boolean doHeal = noTargetTime >= 20 && tickCount % 20 == 0;
			doHeal |= getHealth() < getMaxHealth();
			if (doHeal && getLastHurtByMob() instanceof Player player && player.getAbilities().instabuild) {
				if (tickCount - getLastHurtByMobTimestamp() < 100) {
					doHeal = false;
				}
			}
			if (doHeal) {
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

	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
		initSpellCard();
		return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
	}

	public void initSpellCard() {
	}

}