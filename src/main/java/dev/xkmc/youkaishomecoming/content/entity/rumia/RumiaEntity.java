package dev.xkmc.youkaishomecoming.content.entity.rumia;

import dev.xkmc.l2library.util.math.MathHelper;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.entity.damaku.BaseDamakuEntity;
import dev.xkmc.youkaishomecoming.content.entity.damaku.ItemDamakuEntity;
import dev.xkmc.youkaishomecoming.content.entity.youkai.YoukaiEntity;
import dev.xkmc.youkaishomecoming.content.item.damaku.DamakuItem;
import dev.xkmc.youkaishomecoming.init.data.YHDamageTypes;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
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
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@SerialClass
public class RumiaEntity extends YoukaiEntity {

	private static final EntityDimensions FALL = EntityDimensions.scalable(1.7f, 0.4f);
	private static final UUID EXRUMIA = MathHelper.getUUIDFromString("ex_rumia");

	@SerialClass.SerialField
	public final RumiaStateMachine state = new RumiaStateMachine();

	public RumiaEntity(EntityType<? extends RumiaEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
		setPersistenceRequired();
	}

	protected void registerGoals() {
		this.goalSelector.addGoal(3, new RumiaParalyzeGoal(this));
		this.goalSelector.addGoal(4, new RumiaAttackGoal(this));
		this.goalSelector.addGoal(5, new MoveTowardsRestrictionGoal(this, 1.0));
		this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 0.6));
		this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 24));
		this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true, this::wouldAttack));
	}

	private boolean wouldAttack(LivingEntity entity) {
		return entity.hasEffect(YHEffects.YOUKAIFYING.get());
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes()
				.add(Attributes.MAX_HEALTH, 40)
				.add(Attributes.ATTACK_DAMAGE, 6)
				.add(Attributes.MOVEMENT_SPEED, 0.4)
				.add(Attributes.FLYING_SPEED, 0.4)
				.add(Attributes.FOLLOW_RANGE, 48);
	}

	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		state.refreshState(this);
	}

	@Override
	public void aiStep() {
		super.aiStep();
		state.tick(this);
	}

	public boolean isCharged() {
		return state == null ? false : isAlive() && state.isCharged(this);
	}

	public boolean isBlocked() {
		return state == null ? false : isAlive() && state.isBlocked(this);
	}

	public boolean isEx() {
		return getFlag(4);
	}

	public void setEx(boolean ex) {
		var hp = getAttribute(Attributes.MAX_HEALTH);
		var atk = getAttribute(Attributes.ATTACK_DAMAGE);
		assert hp != null && atk != null;
		if (ex) {
			hp.addPermanentModifier(new AttributeModifier(EXRUMIA, "ex_rumia", 4, AttributeModifier.Operation.MULTIPLY_TOTAL));
			atk.addPermanentModifier(new AttributeModifier(EXRUMIA, "ex_rumia", 1, AttributeModifier.Operation.MULTIPLY_TOTAL));
		} else {
			hp.removeModifier(EXRUMIA);
			atk.removeModifier(EXRUMIA);
		}
		setHealth(getMaxHealth());
		setFlag(4, ex);
	}

	@Override
	public void knockback(double pStrength, double pX, double pZ) {
		if (isCharged()) return;
		super.knockback(pStrength, pX, pZ);
	}

	@Override
	public boolean doHurtTarget(Entity target) {
		if (isCharged()) {
			float atk = (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
			if (target instanceof LivingEntity le) {
				atk += EnchantmentHelper.getDamageBonus(getMainHandItem(), le.getMobType());
			}
			int fire = EnchantmentHelper.getFireAspect(this);
			if (fire > 0) {
				target.setSecondsOnFire(fire * 4);
			}
			boolean success = target.hurt(YHDamageTypes.rumia(this), atk);
			if (success) {
				this.doEnchantDamageEffects(this, target);
				this.setLastHurtMob(target);
			}
			return success;
		} else {
			return super.doHurtTarget(target);
		}
	}

	@Override
	protected void actuallyHurt(DamageSource source, float amount) {
		boolean isVoid = source.is(DamageTypeTags.BYPASSES_INVULNERABILITY);
		if (!isVoid && !isEx() && amount >= getMaxHealth()) {//TODO
			setEx(true);
		}
		if (source.getEntity() instanceof LivingEntity le) {
			state.onHurt(this, le, amount);
		}
		if (!isVoid) {
			int reduction = isEx() ? 20 : 5;
			amount = Math.min(getMaxHealth() / reduction, amount);
		}
		super.actuallyHurt(source, amount);
	}

	@Override
	protected float getStandingEyeHeight(Pose pose, EntityDimensions dim) {
		return isBlocked() ? dim.height / 2 : super.getStandingEyeHeight(pose, dim);
	}

	@Override
	public EntityDimensions getDimensions(Pose pPose) {
		return isBlocked() ? FALL.scale(getScale()) : super.getDimensions(pPose);
	}

	public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
		if (DATA_FLAGS_ID.equals(pKey)) {
			this.refreshDimensions();
		}
	}

	@Override
	public void onDamakuHit(LivingEntity e, BaseDamakuEntity damaku) {
		if (e instanceof YoukaiEntity || e.hasEffect(YHEffects.YOUKAIFIED.get())) return;
		if (damaku instanceof ItemDamakuEntity d && d.getItem().getItem() instanceof DamakuItem item) {
			if (item.color == DyeColor.BLACK)
				e.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100, 1));
			if (item.color == DyeColor.RED && e == getTarget()) heal(getMaxHealth() * 0.2f);
		}
	}

	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(
			ServerLevelAccessor level, DifficultyInstance diff, MobSpawnType reason,
			@Nullable SpawnGroupData data, @Nullable CompoundTag nbt) {
		if (reason == MobSpawnType.NATURAL) {
			restrictTo(blockPosition(), 32);
		}
		return super.finalizeSpawn(level, diff, reason, data, nbt);
	}

	public static boolean checkRumiaSpawnRules(EntityType<RumiaEntity> e, ServerLevelAccessor level, MobSpawnType type,
											   BlockPos pos, RandomSource rand) {
		return Monster.checkMonsterSpawnRules(e, level, type, pos, rand) &&
				level.getEntitiesOfClass(RumiaEntity.class, AABB.ofSize(pos.getCenter(), 32, 32, 32)).isEmpty();
	}
}