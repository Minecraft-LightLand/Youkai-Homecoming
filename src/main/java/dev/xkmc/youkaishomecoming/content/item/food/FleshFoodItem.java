package dev.xkmc.youkaishomecoming.content.item.food;

import dev.xkmc.youkaishomecoming.content.item.curio.TouhouHatItem;
import dev.xkmc.youkaishomecoming.init.data.YHLangData;
import dev.xkmc.youkaishomecoming.init.data.YHModConfig;
import dev.xkmc.youkaishomecoming.init.data.YHTagGen;
import dev.xkmc.youkaishomecoming.init.food.YHFood;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.SpawnUtil;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.sensing.GolemSensor;
import net.minecraft.world.entity.ai.village.ReputationEventType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FleshFoodItem extends YHFoodItem {

	public FleshFoodItem(Properties props) {
		super(props);
	}

	@Nullable
	public static Player getPlayer() {
		if (FMLEnvironment.dist == Dist.CLIENT) {
			return FleshHelper.getPlayerOnClient();
		}
		return null;
	}

	@Override
	public @Nullable FoodProperties getFoodProperties(ItemStack stack, @Nullable LivingEntity entity) {
		FoodProperties old = super.getFoodProperties(stack, entity);
		if (old == null) return null;
		int factor = 1;
		if (entity != null) {
			if (entity.hasEffect(YHEffects.YOUKAIFIED.get())) {
				factor = 3;
			} else if (entity.hasEffect(YHEffects.YOUKAIFYING.get())) {
				factor = 2;
			}
		}
		var builder = new FoodProperties.Builder();
		builder.nutrition(old.getNutrition() * factor);
		builder.saturationMod(old.getSaturationModifier());
		if (old.canAlwaysEat()) builder.alwaysEat();
		if (old.isFastFood()) builder.fast();
		if (old.isMeat()) builder.meat();
		for (var ent : old.getEffects()) {
			if (!ent.getFirst().getEffect().isBeneficial() || factor > 1) {
				builder.effect(ent::getFirst, ent.getSecond());
			}
		}
		return builder.build();
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(stack, level, list, flag);
		Player player = getPlayer();
		if (player == null) return;
		if (player.hasEffect(YHEffects.YOUKAIFIED.get())) {
			list.add(YHLangData.FLESH_TASTE_YOUKAI.get());
		} else if (player.hasEffect(YHEffects.YOUKAIFYING.get())) {
			list.add(YHLangData.FLESH_TASTE_HALF_YOUKAI.get());
		} else {
			list.add(YHLangData.FLESH_TASTE_HUMAN.get());
		}
		if (this == YHFood.FLESH.item.get()) {
			boolean obtain = TouhouHatItem.showTooltip();
			Component obt;
			if (obtain) {
				var fying = Component.translatable(YHEffects.YOUKAIFYING.get().getDescriptionId());
				var fied = Component.translatable(YHEffects.YOUKAIFIED.get().getDescriptionId());
				obt = YHLangData.OBTAIN_FLESH.get(fying, fied);
			} else {
				obt = YHLangData.UNKNOWN.get();
			}
			list.add(YHLangData.OBTAIN.get().append(obt));
		}
	}

	@Override
	public Component getName(ItemStack pStack) {
		Player player = getPlayer();
		Component name;
		if (player != null && player.hasEffect(YHEffects.YOUKAIFIED.get())) {
			name = YHLangData.FLESH_NAME_YOUKAI.get();
		} else {
			name = YHLangData.FLESH_NAME_HUMAN.get();
		}
		return Component.translatable(this.getDescriptionId(pStack), name);
	}

	public void consume(Player consumer) {
		if (consumer.level().isClientSide()) return;
		if (consumer.hasEffect(YHEffects.YOUKAIFIED.get())) {
			var eff = consumer.getEffect(YHEffects.YOUKAIFIED.get());
			if (eff != null) {
				int dur = eff.getDuration() + YHModConfig.COMMON.youkaifiedProlongation.get();
				consumer.addEffect(new MobEffectInstance(YHEffects.YOUKAIFIED.get(),
						dur, 0, true, false, true));
			}
		} else if (consumer.hasEffect(YHEffects.YOUKAIFYING.get())) {
			var eff = consumer.getEffect(YHEffects.YOUKAIFYING.get());
			if (eff != null) {
				int dur = eff.getDuration() + YHModConfig.COMMON.youkaifyingTime.get();
				if (dur > YHModConfig.COMMON.youkaifyingThreshold.get()) {
					dur = YHModConfig.COMMON.youkaifiedDuration.get();
					consumer.removeEffect(YHEffects.YOUKAIFYING.get());
					consumer.addEffect(new MobEffectInstance(YHEffects.YOUKAIFIED.get(),
							dur, 0, true, false, true));
				} else {
					consumer.addEffect(new MobEffectInstance(YHEffects.YOUKAIFYING.get(),
							dur, 0, false, false, false));
				}
			}
		} else {
			if (consumer.getRandom().nextDouble() < YHModConfig.COMMON.youkaifyingChance.get()) {
				int dur = YHModConfig.COMMON.youkaifyingTime.get();
				consumer.addEffect(new MobEffectInstance(YHEffects.YOUKAIFYING.get(),
						dur, 0, false, false, false));
				dur = YHModConfig.COMMON.youkaifyingConfusionTime.get();
				consumer.addEffect(new MobEffectInstance(MobEffects.CONFUSION,
						dur, 0, false, false, true));
			}
		}
		if (getDefaultInstance().is(YHTagGen.APPARENT_FLESH_FOOD) && consumer instanceof ServerPlayer sp) {
			AABB aabb = sp.getBoundingBox().inflate(20);
			var list = sp.serverLevel().getEntities(EntityTypeTest.forClass(Villager.class), aabb, e -> e.hasLineOfSight(sp));
			if (!list.isEmpty()) {
				var opt = SpawnUtil.trySpawnMob(EntityType.IRON_GOLEM, MobSpawnType.MOB_SUMMONED, sp.serverLevel(), sp.blockPosition(),
						10, 8, 6, SpawnUtil.Strategy.LEGACY_IRON_GOLEM);
				if (opt.isPresent()) {
					int time = 3 * 60 * 20;
					list.forEach(GolemSensor::golemDetected);
					opt.get().addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, time, 4));
					opt.get().addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, time, 2));
				}
			}
			for (var e : list) {
				sp.serverLevel().broadcastEntityEvent(e, EntityEvent.VILLAGER_ANGRY);
				sp.serverLevel().onReputationEvent(ReputationEventType.VILLAGER_KILLED, sp, e);
			}
		}
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity consumer) {
		ItemStack ans = super.finishUsingItem(stack, worldIn, consumer);
		if (!(consumer instanceof Player player)) return ans;
		consume(player);
		return ans;
	}

	@Override
	public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
		super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
	}

}
