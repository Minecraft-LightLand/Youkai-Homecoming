package dev.xkmc.youkaishomecoming.content.entity.fairy;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.IYHDanmaku;
import dev.xkmc.youkaishomecoming.content.entity.rumia.MoveAroundNestGoal;
import dev.xkmc.youkaishomecoming.content.entity.rumia.RumiaEntity;
import dev.xkmc.youkaishomecoming.content.entity.youkai.IYoukaiMerchant;
import dev.xkmc.youkaishomecoming.content.spell.game.TouhouSpellCards;
import dev.xkmc.youkaishomecoming.events.EffectEventHandlers;
import dev.xkmc.youkaishomecoming.init.data.YHModConfig;
import dev.xkmc.youkaishomecoming.init.data.YHTagGen;
import dev.xkmc.youkaishomecoming.init.food.YHFood;
import dev.xkmc.youkaishomecoming.init.registrate.YHDanmaku;
import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

@SerialClass
public class CirnoEntity extends FairyEntity implements IYoukaiMerchant {

	public static AttributeSupplier.Builder createAttributes() {
		return FairyEntity.createAttributes()
				.add(Attributes.MAX_HEALTH, 40)
				.add(Attributes.ATTACK_DAMAGE, 6);
	}

	public CirnoEntity(EntityType<? extends CirnoEntity> type, Level level) {
		super(type, level);
		setPersistenceRequired();
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		goalSelector.addGoal(6, new MoveAroundNestGoal(this, 1));
		goalSelector.addGoal(5, new TemptGoal(this, 1,
				Ingredient.of(YHTagGen.FROZEN_FROG), false));
	}

	@Override
	public boolean canFreeze() {
		return false;
	}

	@Override
	public boolean isInvulnerableTo(DamageSource source) {
		return super.isInvulnerableTo(source) || source.is(DamageTypeTags.IS_FREEZING);
	}

	@Override
	protected boolean wouldAttack(LivingEntity entity) {
		return super.wouldAttack(entity) || entity instanceof Frog;
	}

	@Override
	public void onDanmakuHit(LivingEntity e, IYHDanmaku danmaku) {
		if (e.getItemBySlot(EquipmentSlot.HEAD).is(Items.LEATHER_HELMET) &&
				e.getItemBySlot(EquipmentSlot.CHEST).is(Items.LEATHER_CHESTPLATE) &&
				e.getItemBySlot(EquipmentSlot.LEGS).is(Items.LEATHER_LEGGINGS) &&
				e.getItemBySlot(EquipmentSlot.FEET).is(Items.LEATHER_BOOTS)) {
			var ice = YHItems.FAIRY_ICE_CRYSTAL.asStack();
			double chance = YHModConfig.COMMON.cirnoFairyDrop.get();
			if (e.getRandom().nextDouble() < chance) {
				if (e instanceof Player pl) {
					pl.getInventory().placeItemBackInInventory(ice);
				} else {
					e.spawnAtLocation(ice);
				}
			}
			return;
		}
		if (EffectEventHandlers.isFullCharacter(e)) return;
		e.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 2));
		if (e.canFreeze()) {
			e.setTicksFrozen(Math.min(200, e.getTicksFrozen() + 120));
		}
	}

	public void initSpellCard() {
		TouhouSpellCards.setCirno(this);
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

	public static boolean checkCirnoSpawnRules(EntityType<CirnoEntity> e, ServerLevelAccessor level, MobSpawnType type,
											   BlockPos pos, RandomSource rand) {
		return checkMobSpawnRules(e, level, type, pos, rand) && YHModConfig.COMMON.cirnoSpawn.get() &&
				level.getEntitiesOfClass(CirnoEntity.class, AABB.ofSize(pos.getCenter(), 48, 24, 48)).isEmpty();
	}

	// merchant

	@Override
	protected InteractionResult mobInteract(Player player, InteractionHand hand) {
		if (isAggressive()) return InteractionResult.PASS;
		if (!EffectEventHandlers.isCharacter(player) &&
				!player.getItemInHand(hand).is(YHTagGen.FROZEN_FROG))
			return InteractionResult.PASS;
		if (player instanceof ServerPlayer sp) openMenu(sp);
		return InteractionResult.SUCCESS;
	}

	public void openMenu(Player player) {
		if (getTradingPlayer() != null && getTradingPlayer().isAlive())
			return;
		tradingOffers = null;
		init(player);
		openTradingScreen(player, getName(), 0);
	}

	private Item getWantedItem() {
		Holder<Biome> holder = level().getBiome(this.blockPosition());
		long day = level().getGameTime() / 24000;
		double rand = RandomSource.create(RandomSource.create(day).nextLong()).nextDouble();
		if (holder.is(BiomeTags.SPAWNS_COLD_VARIANT_FROGS)) {
			return rand < 0.3 ? YHItems.FROZEN_FROG_TEMPERATE.get() : YHItems.FROZEN_FROG_WARM.get();
		} else if (holder.is(BiomeTags.SPAWNS_WARM_VARIANT_FROGS)) {
			return rand < 0.3 ? YHItems.FROZEN_FROG_TEMPERATE.get() : YHItems.FROZEN_FROG_COLD.get();
		} else {
			return rand < 0.5 ? YHItems.FROZEN_FROG_WARM.get() : YHItems.FROZEN_FROG_COLD.get();
		}
	}

	private MerchantOffers getOfferList() {
		MerchantOffers ans = new MerchantOffers();
		var item = getWantedItem();
		ans.add(offer(YHFood.CANDY_APPLE.item.get(), 4, YHFood.FAIRY_CANDY.item.asStack()));
		ans.add(offer(item, 1, YHItems.FAIRY_ICE_CRYSTAL.asStack()));
		ans.add(offer(item, 1, YHItems.ICE_CUBE.asStack(64)));
		ans.add(offer(item, 1, YHDanmaku.Bullet.CIRCLE.get(DyeColor.CYAN).asStack(8)));
		ans.add(offer(item, 1, YHDanmaku.Bullet.BALL.get(DyeColor.CYAN).asStack(8)));
		ans.add(offer(item, 1, YHDanmaku.Bullet.MENTOS.get(DyeColor.CYAN).asStack(4)));
		if (!getFlag(4)) {
			ans.add(new MerchantOffer(new ItemStack(item, 16), YHItems.CIRNO_HAIRBAND.asStack(), 1, 0, 0));
		}
		return ans;
	}

	private static MerchantOffer offer(Item in, int a, ItemStack b) {
		return new MerchantOffer(new ItemStack(in, a), b, 64, 0, 0);
	}

	private MerchantOffers tradingOffers;
	private Player tradingPlayer;

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
		IYoukaiMerchant.super.notifyTrade(offer);
		if (offer.getResult().is(YHItems.CIRNO_HAIRBAND.get())) {
			setFlag(4, true);
		}
	}
}
