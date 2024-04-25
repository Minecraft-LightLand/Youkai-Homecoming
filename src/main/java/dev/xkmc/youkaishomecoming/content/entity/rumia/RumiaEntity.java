package dev.xkmc.youkaishomecoming.content.entity.rumia;

import dev.xkmc.l2library.util.math.MathHelper;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.spellcircle.SpellCircleHolder;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.IYHDanmaku;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.ItemDanmakuEntity;
import dev.xkmc.youkaishomecoming.content.entity.youkai.MultiHurtByTargetGoal;
import dev.xkmc.youkaishomecoming.content.entity.youkai.YoukaiEntity;
import dev.xkmc.youkaishomecoming.content.item.danmaku.DanmakuItem;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.data.YHDamageTypes;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
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
public class RumiaEntity extends YoukaiEntity implements SpellCircleHolder {

	private static final EntityDimensions FALL = EntityDimensions.scalable(1.7f, 0.4f);
	private static final UUID EXRUMIA = MathHelper.getUUIDFromString("ex_rumia");
	private static final ResourceLocation SPELL_RUMIA = YoukaisHomecoming.loc("rumia");
	private static final ResourceLocation SPELL_EX_RUMIA = YoukaisHomecoming.loc("ex_rumia");

	@SerialClass.SerialField
	public final RumiaStateMachine state = new RumiaStateMachine(this);

	private int tickAggressive;

	public RumiaEntity(EntityType<? extends RumiaEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
		setPersistenceRequired();
	}

	@Override
	protected InteractionResult mobInteract(Player player, InteractionHand hand) {
		if (isAggressive()) return InteractionResult.PASS;
		if (!player.hasEffect(YHEffects.YOUKAIFIED.get())) return InteractionResult.PASS;
		if (player instanceof ServerPlayer sp) RumiaMerchant.openMenu(this, sp);
		return InteractionResult.SUCCESS;
	}

	protected void registerGoals() {
		this.goalSelector.addGoal(3, new RumiaParalyzeGoal(this));
		this.goalSelector.addGoal(4, new RumiaAttackGoal(this));
		this.goalSelector.addGoal(5, new MoveTowardsRestrictionGoal(this, 1.0));
		this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 0.6));
		this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 24));
		this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, new MultiHurtByTargetGoal(this, RumiaEntity.class));
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
		state.refreshState();
	}

	@Override
	public boolean isInvulnerableTo(DamageSource source) {
		return super.isInvulnerableTo(source) || source.getEntity() instanceof RumiaEntity;
	}

	@Override
	public void tick() {
		super.tick();
		if (level().isClientSide()) {
			if (isAggressive() && !isBlocked() && !isCharged()) {
				if (tickAggressive < 20)
					tickAggressive++;
			} else if (tickAggressive > 0) {
				tickAggressive--;
			}
		}
	}

	@Override
	public @Nullable ResourceLocation getSpellCircle() {
		if (isBlocked() || isCharged() || tickAggressive == 0) {
			return null;
		}
		return isEx() ? SPELL_EX_RUMIA : SPELL_RUMIA;
	}

	@Override
	public float getCircleSize(float pTick) {
		return isBlocked() || isCharged() || tickAggressive == 0 ? 0 : Math.min(1, (tickAggressive + pTick) / 20f);
	}

	@Override
	public boolean canBeAffected(MobEffectInstance ins) {
		return !isEx() && super.canBeAffected(ins);
	}

	@Override
	public void aiStep() {
		super.aiStep();
		state.tick();
		if (isEx() && !getActiveEffectsMap().isEmpty()) {
			removeAllEffects();
		}
	}

	public boolean isCharged() {
		return state != null && isAlive() && state.isCharged();
	}

	public boolean isBlocked() {
		return state != null && isAlive() && state.isBlocked();
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
		if (!isVoid && !isEx() && amount >= getMaxHealth()) {
			setEx(true);
		}
		if (source.getEntity() instanceof LivingEntity le) {
			state.onHurt(le, amount);
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
	public void onDanmakuHit(LivingEntity e, IYHDanmaku danmaku) {
		if (e instanceof YoukaiEntity || e.hasEffect(YHEffects.YOUKAIFIED.get())) return;
		if (danmaku instanceof ItemDanmakuEntity d && d.getItem().getItem() instanceof DanmakuItem item) {
			if (item.color == DyeColor.BLACK) {
				e.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100, 1));
				if (!isEx()) return;
			}
		}
		super.onDanmakuHit(e, danmaku);
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