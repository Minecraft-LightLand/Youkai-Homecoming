package dev.xkmc.youkaishomecoming.content.item.food;

import dev.xkmc.youkaishomecoming.content.item.curio.hat.TouhouHatItem;
import dev.xkmc.youkaishomecoming.events.ReimuEventHandlers;
import dev.xkmc.youkaishomecoming.init.data.YHLangData;
import dev.xkmc.youkaishomecoming.init.data.YHModConfig;
import dev.xkmc.youkaishomecoming.init.data.YHTagGen;
import dev.xkmc.youkaishomecoming.init.food.YHFood;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface IFleshFoodItem extends ItemLike {

	@Nullable
	static Player getPlayer() {
		if (FMLEnvironment.dist == Dist.CLIENT) {
			return FleshHelper.getPlayerOnClient();
		}
		return null;
	}

	default @Nullable FoodProperties getFleshFoodProps(@Nullable FoodProperties old, @Nullable LivingEntity entity) {
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

	default void appendFleshText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
		Player player = getPlayer();
		if (player == null) return;
		if (player.hasEffect(YHEffects.YOUKAIFIED.get())) {
			list.add(YHLangData.FLESH_TASTE_YOUKAI.get());
		} else if (player.hasEffect(YHEffects.YOUKAIFYING.get())) {
			list.add(YHLangData.FLESH_TASTE_HALF_YOUKAI.get());
		} else if (asItem().getDefaultInstance().is(YHTagGen.APPARENT_FLESH_FOOD)) {
			list.add(YHLangData.FLESH_TASTE_HUMAN.get());
		}
		if (asItem() == YHFood.FLESH.item.get()) {
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

	default Component getFleshName(ItemStack pStack) {
		Player player = getPlayer();
		Component name;
		if (player != null && player.hasEffect(YHEffects.YOUKAIFIED.get())) {
			name = YHLangData.FLESH_NAME_YOUKAI.get();
		} else {
			name = YHLangData.FLESH_NAME_HUMAN.get();
		}
		return Component.translatable(asItem().getDescriptionId(pStack), name);
	}

	default void consume(Player consumer) {
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
		if (asItem().getDefaultInstance().is(YHTagGen.APPARENT_FLESH_FOOD) && consumer instanceof ServerPlayer sp) {
			ReimuEventHandlers.triggerReimuResponse(sp, 24, true);
		}
	}

}
