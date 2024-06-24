package dev.xkmc.youkaishomecoming.content.entity.rumia;

import dev.xkmc.l2library.util.math.MathHelper;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.codec.TagCodec;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.IYHDanmaku;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.ItemDanmakuEntity;
import dev.xkmc.youkaishomecoming.content.entity.youkai.MultiHurtByTargetGoal;
import dev.xkmc.youkaishomecoming.content.entity.youkai.YoukaiEntity;
import dev.xkmc.youkaishomecoming.content.item.danmaku.DanmakuItem;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.data.YHDamageTypes;
import dev.xkmc.youkaishomecoming.init.food.YHFood;
import dev.xkmc.youkaishomecoming.init.registrate.YHCriteriaTriggers;
import dev.xkmc.youkaishomecoming.init.registrate.YHDanmaku;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
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
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.fluids.FluidType;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@SerialClass
public class RumiaEntity extends YoukaiEntity implements Merchant {

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
		if (player instanceof ServerPlayer sp) openMenu(this, sp);
		return InteractionResult.SUCCESS;
	}

	protected void registerGoals() {
		goalSelector.addGoal(3, new RumiaParalyzeGoal(this));
		goalSelector.addGoal(4, new RumiaAttackGoal(this));
		goalSelector.addGoal(5, new RumiaTemptGoal(this, Ingredient.of(YHFood.FLESH_CHOCOLATE_MOUSSE.item.get())));
		goalSelector.addGoal(6, new FloatGoal(this));
		goalSelector.addGoal(6, new MoveAroundNestGoal(this, 1));
		goalSelector.addGoal(7, new MoveRandomlyGoal(this, 0.8));
		goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 24));
		goalSelector.addGoal(8, new RandomLookAroundGoal(this));
		targetSelector.addGoal(1, new MultiHurtByTargetGoal(this, RumiaEntity.class));
		targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true, this::wouldAttack));
	}

	private boolean wouldAttack(LivingEntity entity) {
		return entity.hasEffect(YHEffects.YOUKAIFYING.get());
	}

	public static AttributeSupplier.Builder createAttributes() {
		return YoukaiEntity.createAttributes()
				.add(Attributes.MAX_HEALTH, 40)
				.add(Attributes.ATTACK_DAMAGE, 6);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		if (hasRestriction()) {
			var data = TagCodec.valueToTag(new RestrictData(getRestrictCenter(), getRestrictRadius()));
			if (data != null) tag.put("Restrict", data);
		}
	}


	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		var data = tag.get("Restrict");
		if (data != null) {
			RestrictData res = TagCodec.valueFromTag(data, RestrictData.class);
			if (res != null) {
				restrictTo(res.center(), (int) res.radius());
			}
		}
	}

	@Override
	public boolean isInvulnerableTo(DamageSource source) {
		if (isEx()) {
			if (!source.is(DamageTypeTags.BYPASSES_INVULNERABILITY) &&
					!(source.getEntity() instanceof LivingEntity))
				return true;
		}
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
	public boolean shouldShowSpellCircle() {
		return !(isBlocked() || isCharged() || tickAggressive == 0);
	}

	@Override
	public @Nullable ResourceLocation getSpellCircle() {
		if (!shouldShowSpellCircle()) {
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
	public boolean canSwimInFluidType(FluidType type) {
		return isEx() || super.canSwimInFluidType(type);
	}

	@Override
	public boolean fireImmune() {
		return isEx();
	}

	@Override
	public void aiStep() {
		super.aiStep();
		state.tick();
		if (isEx() && !getActiveEffectsMap().isEmpty()) {
			removeAllEffects();
		}
	}

	int noTargetTime;

	@Override
	protected void customServerAiStep() {
		super.customServerAiStep();
		if (isEx() && (getTarget() == null || !getTarget().isAlive())) {
			noTargetTime++;
			if (noTargetTime >= 20 && tickCount % 20 == 0) {
				if (getHealth() < getMaxHealth())
					setHealth(getMaxHealth());
			}
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

	private boolean dropHairband = false;

	@Override
	public void die(DamageSource source) {
		dropHairband = isEx() && source.is(YHDamageTypes.DANMAKU_TYPE) && source.getEntity() instanceof Player;
		super.die(source);
	}

	@Override
	protected void dropEquipment() {
		super.dropEquipment();
		if (dropHairband) {
			ItemStack stack = YHItems.RUMIA_HAIRBAND.asStack();
			stack.setDamageValue(stack.getMaxDamage());
			spawnAtLocation(stack);
		}
	}

	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(
			ServerLevelAccessor level, DifficultyInstance diff, MobSpawnType reason,
			@Nullable SpawnGroupData data, @Nullable CompoundTag nbt) {
		if (reason == MobSpawnType.NATURAL || reason == MobSpawnType.STRUCTURE) {
			restrictTo(blockPosition(), 8);
		}
		return super.finalizeSpawn(level, diff, reason, data, nbt);
	}

	public static boolean checkRumiaSpawnRules(EntityType<RumiaEntity> e, ServerLevelAccessor level, MobSpawnType type,
											   BlockPos pos, RandomSource rand) {
		return checkMobSpawnRules(e, level, type, pos, rand) &&
				level.getEntitiesOfClass(RumiaEntity.class, AABB.ofSize(pos.getCenter(), 48, 24, 48)).isEmpty();
	}

	// merchant

	public void openMenu(RumiaEntity rumia, Player player) {
		if (this.tradingPlayer != null && this.tradingPlayer.isAlive())
			return;
		init(player);
		openTradingScreen(player, rumia.getName(), 0);
	}

	private static MerchantOffers getOfferList() {
		MerchantOffers ans = new MerchantOffers();
		ans.add(new MerchantOffer(YHFood.COOKED_FLESH.item.asStack(1), YHFood.FLESH.item.asStack(), 64, 0, 0));
		ans.add(offer(4, YHDanmaku.Bullet.CIRCLE.get(DyeColor.RED).asStack(8)));
		ans.add(offer(4, YHDanmaku.Bullet.BALL.get(DyeColor.RED).asStack(8)));
		ans.add(offer(4, YHDanmaku.Bullet.BUTTERFLY.get(DyeColor.RED).asStack(8)));
		ans.add(offer(8, YHDanmaku.Bullet.MENTOS.get(DyeColor.RED).asStack(8)));
		ans.add(offer(16, YHDanmaku.Bullet.BUBBLE.get(DyeColor.RED).asStack(8)));
		ans.add(offer(32, YHDanmaku.Laser.LASER.get(DyeColor.RED).asStack(8)));
		return ans;
	}

	private static MerchantOffer offer(int a, ItemStack b) {
		return new MerchantOffer(YHFood.FLESH_CHOCOLATE_MOUSSE.item.asStack(a), b, 64, 0, 0);
	}

	private MerchantOffers tradingOffers;
	private Player tradingPlayer;

	public void init(Player player) {
		this.tradingPlayer = player;
	}

	@Override
	public void setTradingPlayer(@Nullable Player player) {
		this.tradingPlayer = player;
	}

	@Nullable
	@Override
	public Player getTradingPlayer() {
		return tradingPlayer;
	}

	@Override
	public MerchantOffers getOffers() {
		if (tradingOffers == null)
			tradingOffers = getOfferList();
		return tradingOffers;
	}

	@Override
	public void overrideOffers(MerchantOffers offers) {
		this.tradingOffers = offers;
	}

	@Override
	public void notifyTrade(MerchantOffer offer) {
		if (tradingPlayer instanceof ServerPlayer sp &&
				offer.getCostA().is(YHFood.FLESH_CHOCOLATE_MOUSSE.item.get()))
			YHCriteriaTriggers.TRADE.trigger(sp);
	}

	@Override
	public void notifyTradeUpdated(ItemStack pStack) {

	}

	@Override
	public int getVillagerXp() {
		return 0;
	}

	@Override
	public void overrideXp(int pXp) {

	}

	@Override
	public boolean showProgressBar() {
		return false;
	}

	@Override
	public SoundEvent getNotifyTradeSound() {
		return SoundEvents.CAT_AMBIENT;
	}

	@Override
	public boolean isClientSide() {
		return false;
	}

}