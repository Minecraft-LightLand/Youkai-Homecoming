package dev.xkmc.youkaishomecoming.content.entity.reimu;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.compat.touhoulittlemaid.TouhouConditionalSpawns;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.IYHDanmaku;
import dev.xkmc.youkaishomecoming.content.spell.game.TouhouSpellCards;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.data.YHDamageTypes;
import dev.xkmc.youkaishomecoming.init.data.YHModConfig;
import dev.xkmc.youkaishomecoming.init.data.YHTagGen;
import dev.xkmc.youkaishomecoming.init.registrate.YHCriteriaTriggers;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

import java.util.HashSet;
import java.util.UUID;

@SerialClass
public class ReimuEntity extends MaidenEntity {

	@SerialClass.SerialField
	private final HashSet<UUID> verifiedPlayers = new HashSet<>();
	@SerialClass.SerialField
	private int feedCD = 0;

	public ReimuEntity(EntityType<? extends ReimuEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	@Override
	protected void customServerAiStep() {
		super.customServerAiStep();
		if (feedCD > 0) feedCD--;
		setFlag(16, feedCD > 0);
	}

	@Override
	protected void tickEffects() {
		super.tickEffects();
		if (getFlag(16)) {
			boolean flag;
			if (this.isInvisible()) {
				flag = this.random.nextInt(15) == 0;
			} else {
				flag = this.random.nextBoolean();
			}
			int i = MobEffects.SATURATION.getColor();
			if (flag) {
				double d0 = (i >> 16 & 255) / 255.0D;
				double d1 = (i >> 8 & 255) / 255.0D;
				double d2 = (i & 255) / 255.0D;
				level().addParticle(ParticleTypes.ENTITY_EFFECT,
						getRandomX(0.5D), getRandomY(), getRandomZ(0.5D),
						d0, d1, d2);
			}
		}
	}

	@Override
	protected InteractionResult mobInteract(Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (stack.isEdible()) {
			var food = stack.getFoodProperties(this);
			if (food != null) {
				if (player instanceof ServerPlayer sp && feedCD == 0) {
					if (stack.is(YHTagGen.FLESH_FOOD)) {
						setTarget(player);
						return InteractionResult.SUCCESS;
					}
					ItemStack remain = stack.getCraftingRemainingItem();
					feedTrigger(sp, stack);
					stack.shrink(1);
					feedCD += food.getNutrition() * 100;
					if (food.getEffects().stream().anyMatch(e -> e.getFirst().getEffect() == YHEffects.UDUMBARA.get())) {
						verifiedPlayers.add(sp.getUUID());
						YHCriteriaTriggers.REIMU_HAPPY.trigger(sp);
					}
					if (stack.getUseAnimation() == UseAnim.DRINK)
						playSound(stack.getDrinkingSound());
					else playSound(stack.getEatingSound());
					if (!remain.isEmpty())
						sp.getInventory().placeItemBackInInventory(remain);
				}
				return InteractionResult.SUCCESS;
			}
		}
		return super.mobInteract(player, hand);
	}

	private void feedTrigger(ServerPlayer sp, ItemStack stack) {
		var sv = sp.getServer();
		if (sv == null) return;
		var e = sv.getAdvancements().getAdvancement(YoukaisHomecoming.loc("main/feed_reimu"));
		if (e == null) return;
		var prog = sp.getAdvancements().getOrStartProgress(e);
		float count = prog.getPercent();
		YHCriteriaTriggers.FEED_REIMU.trigger(sp, stack);
		if (prog.getPercent() > count) {
			level().broadcastEntityEvent(this, EntityEvent.IN_LOVE_HEARTS);
		}
	}

	@Override
	public void handleEntityEvent(byte pId) {
		if (pId == EntityEvent.IN_LOVE_HEARTS) {
			for (int i = 0; i < 7; ++i) {
				double d0 = this.random.nextGaussian() * 0.02D;
				double d1 = this.random.nextGaussian() * 0.02D;
				double d2 = this.random.nextGaussian() * 0.02D;
				this.level().addParticle(ParticleTypes.HEART, this.getRandomX(1.0D), this.getRandomY() + 0.5D, this.getRandomZ(1.0D), d0, d1, d2);
			}
		} else super.handleEntityEvent(pId);
	}

	@Override
	public void initSpellCard() {
		TouhouSpellCards.setReimu(this);
	}

	@Override
	public void die(DamageSource source) {
		boolean prev = dead;
		super.die(source);
		var e = source.getEntity();
		if (!prev && dead && e instanceof LivingEntity le && !source.is(YHDamageTypes.DANMAKU_TYPE)) {
			if (!e.isAlive() || !e.isAddedToWorld() || e.isRemoved())
				return;
			TouhouConditionalSpawns.triggetYukari(le, position());
		}
	}

	@Override
	public void onDanmakuImmune(LivingEntity e, IYHDanmaku danmaku, DamageSource source) {
		if (e.tickCount - e.getLastHurtByMobTimestamp() < 20)
			return;
		if (e instanceof Player player && player.getAbilities().instabuild)
			return;
		if (!source.is(DamageTypeTags.BYPASSES_EFFECTS))
			return;
		double rate = e instanceof Player ?
				YHModConfig.COMMON.danmakuPlayerPHPDamage.get() :
				YHModConfig.COMMON.danmakuMinPHPDamage.get();
		double dmg = Math.max(rate * Math.max(e.getHealth(), e.getMaxHealth()), danmaku.damage(e));
		e.setHealth(e.getHealth() - (float) dmg);
		if (e.isDeadOrDying()) {
			e.die(YHDamageTypes.abyssal(danmaku));
		}
	}

}
