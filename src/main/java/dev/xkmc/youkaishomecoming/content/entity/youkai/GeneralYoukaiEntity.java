package dev.xkmc.youkaishomecoming.content.entity.youkai;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.data.YHDamageTypes;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
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
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.Nullable;

@SerialClass
public class GeneralYoukaiEntity extends YoukaiEntity {

	private static final ResourceLocation SPELL = YoukaisHomecoming.loc("ex_rumia");

	private static <T> EntityDataAccessor<T> defineId(EntityDataSerializer<T> ser) {
		return SynchedEntityData.defineId(GeneralYoukaiEntity.class, ser);
	}

	protected static final SyncedData SPELL_DATA = new SyncedData(GeneralYoukaiEntity::defineId, YOUKAI_DATA);

	private static final EntityDataAccessor<String> SPELL_MODEL = SPELL_DATA.define(SyncedData.STRING, "", "modelId");

	private int tickAggressive;

	private final ServerBossEvent bossEvent = new ServerBossEvent(getDisplayName(), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.NOTCHED_20);

	public GeneralYoukaiEntity(EntityType<? extends GeneralYoukaiEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
		setPersistenceRequired();
	}

	@Override
	protected SyncedData data() {
		return SPELL_DATA;
	}

	@Nullable
	public String getModelId() {
		String ans = entityData.get(SPELL_MODEL);
		if (ans.isEmpty()) return null;
		return ans;
	}

	public void syncModel() {
		String model = null;
		if (spellCard != null) model = spellCard.getModelId();
		if (model == null) model = "";
		entityData.set(SPELL_MODEL, model);
	}

	protected void registerGoals() {
		goalSelector.addGoal(4, new YoukaiAttackGoal<>(this, 16));
		goalSelector.addGoal(5, new MoveTowardsRestrictionGoal(this, 1.0));
		goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 0.6));
		goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 24));
		goalSelector.addGoal(8, new RandomLookAroundGoal(this));
		targetSelector.addGoal(1, new MultiHurtByTargetGoal(this, GeneralYoukaiEntity.class));
		targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, LivingEntity.class, true, this::wouldAttack));
	}

	private boolean wouldAttack(LivingEntity entity) {
		return entity.hasEffect(YHEffects.YOUKAIFYING.get());
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes()
				.add(Attributes.MAX_HEALTH, 200)
				.add(Attributes.ATTACK_DAMAGE, 10)
				.add(Attributes.MOVEMENT_SPEED, 0.4)
				.add(Attributes.FLYING_SPEED, 0.4)
				.add(Attributes.FOLLOW_RANGE, 128);
	}

	@Override
	public void tick() {
		super.tick();
		if (level().isClientSide()) {
			if (isAggressive()) {
				if (tickAggressive < 20)
					tickAggressive++;
			} else if (tickAggressive > 0) {
				tickAggressive--;
			}
		}
	}

	@Override
	public boolean shouldShowSpellCircle() {
		return isAggressive();
	}

	@Override
	public @Nullable ResourceLocation getSpellCircle() {
		if (!shouldShowSpellCircle()) {
			return null;
		}
		return SPELL;
	}

	@Override
	public float getCircleSize(float pTick) {
		return tickAggressive == 0 ? 0 : Math.min(1, (tickAggressive + pTick) / 20f);
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

	// BOSS

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

	int noTargetTime;

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