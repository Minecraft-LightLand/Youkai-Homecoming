package dev.xkmc.youkaishomecoming.content.item.food;

import dev.xkmc.youkaishomecoming.init.data.YHLangData;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import vectorwing.farmersdelight.common.Configuration;

import java.util.List;

public class YHFoodItem extends Item {

	private static Component getTooltip(MobEffectInstance eff) {
		MutableComponent ans = Component.translatable(eff.getDescriptionId());
		MobEffect mobeffect = eff.getEffect().value();
		if (eff.getAmplifier() > 0) {
			ans = Component.translatable("potion.withAmplifier", ans,
					Component.translatable("potion.potency." + eff.getAmplifier()));
		}

		if (eff.getDuration() > 20) {
			ans = Component.translatable("potion.withDuration", ans,
					MobEffectUtil.formatDuration(eff, 1, 20));
		}

		return ans.withStyle(mobeffect.getCategory().getTooltipFormatting());
	}

	public static void getFoodEffects(ItemStack stack, List<Component> list) {
		var food = stack.getFoodProperties(null);
		if (food == null) return;
		getFoodEffects(food, list);
	}

	public static void getFoodEffects(FoodProperties food, List<Component> list) {
		for (var eff : food.effects()) {
			int chance = Math.round(eff.probability() * 100);
			Component ans = getTooltip(eff.effect());
			if (chance == 100) {
				list.add(ans);
			} else {
				list.add(YHLangData.CHANCE_EFFECT.get(ans, chance));
			}
		}
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
	public void appendHoverText(ItemStack stack, TooltipContext level, List<Component> list, TooltipFlag flag) {
		if (Configuration.FOOD_EFFECT_TOOLTIP.get())
			getFoodEffects(stack, list);
	}

}
