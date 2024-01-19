package dev.xkmc.youkaihomecoming.content.item;

import dev.xkmc.l2library.util.Proxy;
import dev.xkmc.youkaihomecoming.init.data.YHLangData;
import dev.xkmc.youkaihomecoming.init.data.YHModConfig;
import dev.xkmc.youkaihomecoming.init.registrate.YHEffects;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FleshFoodItem extends YHFoodItem {
	public FleshFoodItem(Properties props, UseAnim anim) {
		super(props, anim);
	}

	public FleshFoodItem(Properties props) {
		super(props);
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
		Player player = Proxy.getPlayer();
		if (player == null) return;
		if (player.hasEffect(YHEffects.YOUKAIFIED.get())) {
			list.add(YHLangData.FLESH_TASTE_YOUKAI.get());
		} else if (player.hasEffect(YHEffects.YOUKAIFYING.get())) {
			list.add(YHLangData.FLESH_TASTE_HALF_YOUKAI.get());
		} else {
			list.add(YHLangData.FLESH_TASTE_HUMAN.get());
		}
	}

	@Override
	public Component getName(ItemStack pStack) {
		Player player = Proxy.getPlayer();
		Component name;
		if (player != null && player.hasEffect(YHEffects.YOUKAIFIED.get())) {
			name = YHLangData.FLESH_NAME_YOUKAI.get();
		} else {
			name = YHLangData.FLESH_NAME_HUMAN.get();
		}
		return Component.translatable(this.getDescriptionId(pStack), name);
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity consumer) {
		ItemStack ans = super.finishUsingItem(stack, worldIn, consumer);
		if (!(consumer instanceof Player)) return ans;
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
		return ans;
	}
}
