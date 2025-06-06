package dev.xkmc.youkaishomecoming.content.entity.youkai;

import dev.xkmc.fastprojectileapi.collision.EntityStorageHelper;
import dev.xkmc.fastprojectileapi.collision.UserCacheHolder;
import dev.xkmc.fastprojectileapi.entity.EntityCachingUser;
import dev.xkmc.fastprojectileapi.entity.SimplifiedProjectile;
import dev.xkmc.fastprojectileapi.render.virtual.DanmakuManager;
import dev.xkmc.fastprojectileapi.spellcircle.SpellCircleHolder;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.codec.TagCodec;
import dev.xkmc.l2serial.util.Wrappers;
import dev.xkmc.youkaishomecoming.compat.touhoulittlemaid.TouhouConditionalSpawns;
import dev.xkmc.youkaishomecoming.content.capability.GrazeCapability;
import dev.xkmc.youkaishomecoming.content.capability.GrazeHelper;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.IYHDanmaku;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.ItemDanmakuEntity;
import dev.xkmc.youkaishomecoming.content.entity.rumia.RestrictData;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.LivingCardHolder;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.SpellCardWrapper;
import dev.xkmc.youkaishomecoming.events.EffectEventHandlers;
import dev.xkmc.youkaishomecoming.events.YoukaiFightEvent;
import dev.xkmc.youkaishomecoming.init.data.YHDamageTypes;
import dev.xkmc.youkaishomecoming.init.data.YHModConfig;
import dev.xkmc.youkaishomecoming.init.data.YHTagGen;
import dev.xkmc.youkaishomecoming.init.registrate.YHDanmaku;
import dev.xkmc.youkaishomecoming.init.registrate.YHEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;

@SerialClass
public abstract class YoukaiEntity extends PathfinderMob
		implements SpellCircleHolder, LivingCardHolder, EntityCachingUser {

	private static final int GROUND_HEIGHT = 5, ATTEMPT_ABOVE = 3;

	private static <T> EntityDataAccessor<T> defineId(EntityDataSerializer<T> ser) {
		return SynchedEntityData.defineId(YoukaiEntity.class, ser);
	}

	protected static final SyncedData YOUKAI_DATA = new SyncedData(YoukaiEntity::defineId);

	protected static final EntityDataAccessor<Integer> DATA_FLAGS_ID = YOUKAI_DATA.define(SyncedData.INT, 0, "youkai_flags");

	public static AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes()
				.add(Attributes.MOVEMENT_SPEED, 0.3)
				.add(Attributes.FLYING_SPEED, 0.4)
				.add(Attributes.FOLLOW_RANGE, 48);
	}

	public final MoveControl walkCtrl, flyCtrl;
	public final PathNavigation walkNav, fltNav;

	@SerialClass.SerialField
	public final YoukaiTargetContainer targets;

	@SerialClass.SerialField
	public SpellCardWrapper spellCard;

	@SerialClass.SerialField
	public CombatProgress combatProgress = new CombatProgress();

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
		combatProgress.init(this);
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

	@Override
	public boolean canBeLeashed(Player pPlayer) {
		return false;
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
		tag.putFloat("Health", this.getCombatProgress());
		tag.putInt("Age", tickCount);
		tag.put("auto-serial", Objects.requireNonNull(TagCodec.toTag(new CompoundTag(), this)));
		data().write(tag, entityData);
		if (hasRestriction()) {
			var data = TagCodec.valueToTag(new RestrictData(getRestrictCenter(), getRestrictRadius()));
			if (data != null) tag.put("Restrict", data);
		}
	}

	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		tickCount = tag.getInt("Age");
		if (tag.contains("auto-serial")) {
			Wrappers.run(() -> TagCodec.fromTag(tag.getCompound("auto-serial"), getClass(), this, (f) -> true));
		}
		data().read(tag, entityData);
		if (getTarget() == null) {
			setWalking();
		}
		var data = tag.get("Restrict");
		if (data != null) {
			RestrictData res = TagCodec.valueFromTag(data, RestrictData.class);
			if (res != null) {
				restrictTo(res.center(), (int) res.radius());
			}
		}
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

	public boolean invalidTarget(LivingEntity e) {
		return !e.isAlive() || !e.isAddedToWorld() || e.level() != level() || e == this || !EntityStorageHelper.isPresent(e);
	}

	public boolean shouldIgnore(LivingEntity e) {
		if (e.getType().is(YHTagGen.YOUKAI_IGNORE))
			return true;
		if (invalidTarget(e))
			return true;
		var event = new YoukaiFightEvent(this, e);
		return MinecraftForge.EVENT_BUS.post(event);
	}

	@Override
	public boolean isInvulnerableTo(DamageSource pSource) {
		if (pSource.getEntity() instanceof LivingEntity le && shouldIgnore(le)) {
			return true;
		}
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
	public float getDamage(YHDanmaku.IDanmakuType type) {
		return (float) getAttributeValue(Attributes.ATTACK_DAMAGE);
	}

	public double getStopRange() {
		return 16;
	}

	@Nullable
	@Override
	public LivingEntity targetEntity() {
		return getTarget();
	}

	@Override
	public LivingEntity shooter() {
		return this;
	}

	public void shoot(float dmg, int life, Vec3 vec, DyeColor color) {
		ItemDanmakuEntity danmaku = new ItemDanmakuEntity(YHEntities.ITEM_DANMAKU.get(), this, level());
		danmaku.setItem(YHDanmaku.Bullet.CIRCLE.get(color).asStack());
		danmaku.setup(dmg, life, true, true, vec);
		level().addFreshEntity(danmaku);
	}

	public void onDanmakuHit(LivingEntity e, IYHDanmaku danmaku) {
		if (EffectEventHandlers.isFullCharacter(e)) return;
		if (targets.contains(e)) {
			double heal = YHModConfig.COMMON.danmakuHealOnHitTarget.get();
			heal(getMaxHealth() * (float) heal);
		}
	}

	public void onDanmakuImmune(LivingEntity e, IYHDanmaku danmaku, DamageSource source) {

	}

	public float percentageDamage(LivingEntity le) {
		if (le instanceof Player)
			return YHModConfig.COMMON.danmakuPlayerPHPDamage.get().floatValue();
		else return YHModConfig.COMMON.danmakuMinPHPDamage.get().floatValue();
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

	// combat

	public void aiStep() {
		if (!level().isClientSide()) {
			if (!this.onGround() && this.getDeltaMovement().y < 0.0D) {
				double fall = getTarget() != null ? 0.6 : 0.8;
				this.setDeltaMovement(this.getDeltaMovement().multiply(1.0D, fall, 1.0D));
			}
			targets.tick(super.getTarget());
			if (spellCard != null) {
				if (getTarget() != null && shouldShowSpellCircle()) {
					spellCard.tick(this);
					tickDanmaku();
				} else {
					spellCard.reset();
					allDanmakus.clear();
					toBeSent.clear();
				}
			}
		}
		super.aiStep();
	}

	public void trySummonReinforcementOnDeath(LivingEntity le) {
		TouhouConditionalSpawns.triggetYukari(le, position());
	}

	@Override
	protected void actuallyHurt(DamageSource source, float amount) {
		if (spellCard != null) spellCard.hurt(this, source, amount);
		actuallyHurtImpl(source, amount);
	}

	protected final void actuallyHurtImpl(DamageSource source, float amount) {
		if (!isInvulnerableTo(source)) {
			amount = ForgeHooks.onLivingHurt(this, source, amount);
			if (amount <= 0) return;
			amount = getDamageAfterArmorAbsorb(source, amount);
			amount = getDamageAfterMagicAbsorb(source, amount);
			amount = ForgeHooks.onLivingDamage(this, source, amount);
			hurtFinal(source, amount);

		}
	}

	protected void hurtFinal(DamageSource source, float amount) {
		if (!Float.isFinite(amount)) return;
		float abs = getAbsorptionAmount();
		if (!Float.isFinite(abs)) return;
		float actual = Math.max(amount - Math.max(0, abs), 0);
		float absorb = amount - actual;
		setAbsorptionAmount(Math.max(0, getAbsorptionAmount() - absorb));
		if (absorb > 0.0F && absorb < 3.4028235E37F) {
			Entity entity = source.getEntity();
			if (entity instanceof ServerPlayer serverplayer) {
				serverplayer.awardStat(Stats.DAMAGE_DEALT_ABSORBED, Math.round(absorb * 10.0F));
			}
		}
		if (actual != 0.0F) {
			getCombatTracker().recordDamage(source, actual);
			hurtFinalImpl(source, actual);
			setAbsorptionAmount(Math.max(0, getAbsorptionAmount() - actual));
			gameEvent(GameEvent.ENTITY_DAMAGE);
		}
	}

	protected void hurtFinalImpl(DamageSource source, float amount) {
		if (combatProgress == null) return;
		if (!source.is(YHDamageTypes.DANMAKU_TYPE) && source.getEntity() instanceof Player player) {
			GrazeCapability.HOLDER.get(player).remove(getUUID());
		}
		setCombatProgress(getCombatProgress() - amount);
		if (combatProgress.progress <= 0) {
			eraseAllDanmaku(null);
			if (source.getEntity() instanceof Player player) {
				GrazeHelper.onDanmakuKill(player, this);
			}
		}
	}

	public void validateData() {
		if (combatProgress == null || getCombatProgress() != combatProgress.progress || getCombatProgress() > 0) {
			if (deathTime > 0) deathTime = 0;
			if (dead) dead = false;
		}
	}

	@Override
	public void setHealth(float amount) {
		if (!Float.isFinite(amount)) return;
		setCombatProgress(amount);
	}

	@Override
	protected boolean isImmobile() {
		if (combatProgress == null || getCombatProgress() != combatProgress.progress)
			return false;
		return this.getCombatProgress() <= 0.0F;
	}

	@Override
	public boolean isDeadOrDying() {
		if (combatProgress == null || getCombatProgress() != combatProgress.progress)
			return false;
		return this.getCombatProgress() <= 0.0F;
	}

	public boolean isAlive() {
		if (isRemoved()) return false;
		if (combatProgress == null || getCombatProgress() != combatProgress.progress)
			return true;
		return this.getCombatProgress() > 0.0F;
	}

	protected float getVanillaProgress() {
		return super.getHealth();
	}

	public void setCombatProgress(float amount) {
		super.setHealth(amount);
		if (combatProgress == null) return;
		if (amount > getMaxHealth()) {
			combatProgress.setMax();
		} else {
			combatProgress.set(this, amount);
		}
	}

	public float getCombatProgress() {
		return combatProgress == null ? super.getHealth() : combatProgress.getProgress();
	}

	@Override
	public float getHealth() {
		return getCombatProgress();
	}

	@Override
	protected void tickDeath() {
		if (combatProgress == null || getCombatProgress() != combatProgress.progress)
			return;
		if (getCombatProgress() > 0) return;
		super.tickDeath();
	}

	@Override
	public void die(DamageSource source) {
		if (combatProgress == null || getCombatProgress() != combatProgress.progress)
			return;
		if (getCombatProgress() > 0) return;
		super.die(source);
	}

	@Override
	public void setTarget(@Nullable LivingEntity e) {
		if (e != null && shouldIgnore(e)) return;
		super.setTarget(e);
	}

	@Nullable
	@Override
	public LivingEntity getTarget() {
		if (targets == null) return null;
		return targets.getTarget();
	}

	public void setTargetAndInitSession(LivingEntity le) {
		if (le instanceof Player player) {
			if (EffectEventHandlers.isFullCharacter(player)) {
				var cap = GrazeCapability.HOLDER.get(player);
				cap.initSession(this);
				return;
			}
		}
		setTarget(le);
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

	public boolean tooHigh() {
		BlockPos pos = getOnPos();
		for (int i = 0; i < GROUND_HEIGHT; i++) {
			BlockPos off = pos.offset(0, -i, 0);
			if (!level().getBlockState(off).isAir()) return false;
		}
		return true;
	}

	public boolean shouldHurt(LivingEntity le) {
		return targets.contains(le);
	}

	@Override
	public LivingEntity self() {
		return super.self();
	}

	@Override
	public DamageSource getDanmakuDamageSource(IYHDanmaku danmaku) {
		if (spellCard != null) return spellCard.card.getDanmakuDamageSource(danmaku);
		return YHDamageTypes.danmaku(danmaku);
	}

	public void resetTarget(Player target) {
		targets.remove(target.getUUID());
		setTarget(null);
		setLastHurtByMob(null);
		setCombatProgress(combatProgress.maxProgress);
	}

	public void danmakuHitTarget(IYHDanmaku self, DamageSource source, LivingEntity target) {
		if (combatProgress.progress <= 0) return;
		if (target instanceof Player player) {
			var graze = GrazeCapability.HOLDER.get(player);
			var type = graze.performErase(this);
			if (type.erase()) {
				eraseAllDanmaku(player);
			}
			if (type.skipDamage()) {
				return;
			}
		}
		float hp = target.getHealth();
		boolean immune = !target.hurt(source, self.damage(target));
		float ahp = target.getHealth();
		if (ahp >= hp && ahp > 0) immune = true;
		onDanmakuHit(target, self);
		if (immune) {
			onDanmakuImmune(target, self, source);
		}
	}

	private final LinkedList<SimplifiedProjectile> allDanmakus = new LinkedList<>();
	private ArrayList<SimplifiedProjectile> temp;
	private final ArrayList<SimplifiedProjectile> toBeSent = new ArrayList<>();

	public void shoot(Entity danmaku) {
		if (danmaku instanceof SimplifiedProjectile proj) {
			if (temp != null) temp.add(proj);
			else allDanmakus.add(proj);
			toBeSent.add(proj);
		} else {
			LivingCardHolder.super.shoot(danmaku);
		}
	}

	private boolean removeDanmaku = false;

	private void tickDanmaku() {
		removeDanmaku = false;
		temp = new ArrayList<>();
		var itr = allDanmakus.iterator();
		while (itr.hasNext()) {
			var e = itr.next();
			if (e.isAddedToWorld() && !e.isRemoved()) continue;
			if (e.isValid()) {
				e.setOldPosAndRot();
				++e.tickCount;
				e.tick();
			}
			if (removeDanmaku) break;
			if (!e.isValid()) {
				itr.remove();
			}
		}
		if (!removeDanmaku) {
			allDanmakus.addAll(temp);
			DanmakuManager.send(this, toBeSent);
		}
		temp = null;
		toBeSent.clear();
	}

	public void eraseAllDanmaku(@Nullable Player player) {
		for (var e : allDanmakus) {
			if (player == null) e.markErased(true);
			else e.erase(player);
		}
		allDanmakus.clear();
		removeDanmaku = true;
	}

	private final UserCacheHolder cache = new UserCacheHolder();

	@Override
	public UserCacheHolder entityCache() {
		return cache;
	}

}