
package dev.xkmc.youkaishomecoming.content.item.food;

import dev.xkmc.youkaishomecoming.init.data.YHLangData;
import dev.xkmc.youkaishomecoming.init.data.YHModConfig;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.Configuration;

import java.util.List;

public class YHFoodItem extends Item {

	private static Component getTooltip(MobEffectInstance eff) {
		MutableComponent ans = Component.translatable(eff.getDescriptionId());
		MobEffect mobeffect = eff.getEffect();
		if (eff.getAmplifier() > 0) {
			ans = Component.translatable("potion.withAmplifier", ans,
					Component.translatable("potion.potency." + eff.getAmplifier()));
		}

		if (eff.getDuration() > 20) {
			ans = Component.translatable("potion.withDuration", ans,
					MobEffectUtil.formatDuration(eff, 1));
		}

		return ans.withStyle(mobeffect.getCategory().getTooltipFormatting());
	}

	public static void getFoodEffects(ItemStack stack, List<Component> list) {
		var food = stack.getFoodProperties(FleshHelper.getPlayer());
		if (food == null) return;
		getFoodEffects(food, list);
	}

	public static void getFoodEffects(FoodProperties food, List<Component> list) {
		for (var eff : food.getEffects()) {
			int chance = Math.round(eff.getSecond() * 100);
			if (eff.getFirst() == null) continue; //I hate stupid modders
			Component ans = getTooltip(eff.getFirst());
			if (chance == 100) {
				list.add(ans);
			} else {
				list.add(YHLangData.CHANCE_EFFECT.get(ans, chance));
			}
		}
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity consumer) {
		ItemStack itemStack = getCraftingRemainingItem(stack);
		boolean youkai = consumer.hasEffect(YHEffects.YOUKAIFYING.get());
		super.finishUsingItem(stack, worldIn, consumer);
		if (!youkai && consumer.hasEffect(YHEffects.YOUKAIFYING.get())) {
			int dur = YHModConfig.COMMON.youkaifyingConfusionTime.get();
			consumer.addEffect(new MobEffectInstance(MobEffects.CONFUSION,
					dur, 0, false, false, true));
		}
		if (itemStack.isEmpty()) {
			return stack;
		}
		if (stack.isEmpty()) {
			return itemStack;
		}
		if (consumer instanceof Player player && !player.getAbilities().instabuild) {
			if (!player.getInventory().add(itemStack)) {
				player.drop(itemStack, false);
			}
		}

		return stack;
	}

	private final UseAnim anim;

	public YHFoodItem(Properties props, UseAnim anim) {
		super(props);
		this.anim = anim;
	}

	public YHFoodItem(Properties props) {
		this(props, UseAnim.EAT);
	}

	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return anim;
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
		if (Configuration.FOOD_EFFECT_TOOLTIP.get())
			getFoodEffects(stack, list);
	}

}
