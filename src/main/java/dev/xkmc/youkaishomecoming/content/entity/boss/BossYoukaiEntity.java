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
import net.minecraft.world.entity.*;
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
	private boolean ticking = false;

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
	public void tick() {
		ticking = true;
		double maxSpeed = 0.5;
		if (getDeltaMovement().length() > maxSpeed) {
			setDeltaMovement(getDeltaMovement().normalize().scale(maxSpeed));
		}
		validateData();
		super.tick();
		ticking = false;
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
	private boolean chaotic = false;

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
		if (ticking || source.getEntity() == this) {
			if (!source.is(DamageTypes.GENERIC_KILL))
				return false;
			else {
				var target = getTarget();
				if (target != null && amount >= getCombatProgress() && !isRemoved()) {
					trySummonReinforcementOnDeath(target);
					discard();
				}
			}
		}
		if (source.getEntity() instanceof LivingEntity le) {
			setLastHurtByMob(le);
			targets.checkTarget();
		}
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

	private float illegalDamage = 0;

	protected void notifyIllegalDamage(float amount, @Nullable Entity causer) {
		illegalDamage += amount;
		if (illegalDamage > 200) {
			setFlag(32, true);
		}
	}

	protected float clampDamage(DamageSource source, float amount) {
		if (!hurtCall) {
			notifyIllegalDamage(amount, source.getEntity());
			return 0;
		}
		if (source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
			if (source.getEntity() instanceof LivingEntity le) {
				if (le instanceof ServerPlayer sp) {
					if (sp.isCreative()) {
						return amount;
					}
				}
			} else {
				if (source.is(DamageTypes.FELL_OUT_OF_WORLD)) {
					if (amount > 4) {
						notifyIllegalDamage(amount - 4, source.getEntity());
					}
					return Math.min(4, amount);
				}
				if (source.is(DamageTypes.GENERIC_KILL)) {
					return amount;
				}
			}
		}
		int reduction = 20;
		float ans = Math.min(getMaxHealth() / reduction, amount);
		if (YHModConfig.COMMON.reimuDamageReduction.get() && !source.is(YHDamageTypes.DANMAKU_TYPE))
			ans /= 5;
		if (source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
			notifyIllegalDamage(amount - ans, source.getEntity());
		}
		return ans;
	}

	@Override
	protected final void actuallyHurt(DamageSource source, float amount) {
		if (!hurtCall) {
			notifyIllegalDamage(amount, source.getEntity());
			return;
		}
		super.actuallyHurt(source, amount);
	}

	@Override
	protected void hurtFinal(DamageSource source, float amount) {
		if (!Float.isFinite(amount)) return;
		amount = clampDamage(source, amount);
		if (amount <= 0) return;
		super.hurtFinal(source, amount);
	}

	@Override
	public void setHealth(float val) {
		if (!Float.isFinite(val)) return;
		if (level().isClientSide()) {
			setCombatProgress(val);
		}
		float health = getCombatProgress();
		if (tickCount > 5 && val <= health) {
			notifyIllegalDamage(health - val, null);
			return;
		}
		setCombatProgress(val);
	}

	@Override
	public void setCombatProgress(float amount) {
		if (combatProgress != null) {
			float health = combatProgress.progress;
			if (health > getVanillaProgress()) {
				notifyIllegalDamage(health - getVanillaProgress(), null);
			}
			if (health > getCombatProgress()) {
				notifyIllegalDamage(health - getCombatProgress(), null);
				setFlag(32, true);
			}
		}
		super.setCombatProgress(amount);
	}

	public void heal(float original) {
		var heal = ForgeEventFactory.onLivingHeal(this, original);
		heal = Math.max(original, heal);
		if (heal <= 0) return;
		float f = getCombatProgress();
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
		if (combatProgress != null && getCombatProgress() != combatProgress.progress) {
			bossEvent.setProgress(random().nextFloat());
			return;
		}
		bossEvent.setProgress(getCombatProgress() / getMaxHealth());
		if (getTarget() == null || !getTarget().isAlive()) {
			chaotic = false;
			noTargetTime++;
			boolean doHeal = noTargetTime >= 20 && tickCount % 20 == 0;
			doHeal |= getCombatProgress() < getMaxHealth();
			if (doHeal && getLastHurtByMob() instanceof Player player && player.getAbilities().instabuild) {
				if (tickCount - getLastHurtByMobTimestamp() < 100) {
					doHeal = false;
				}
			}
			if (doHeal) {
				setHealth(getMaxHealth());
				if (getFlag(32)) {
					setFlag(32, false);
				}
			}
		} else {
			noTargetTime = 0;
		}
	}

	public boolean isChaotic() {
		return getFlag(32) || chaotic || combatProgress != null && getCombatProgress() != combatProgress.progress;
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
