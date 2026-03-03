package dev.xkmc.youkaishomecoming.content.item.food;

import dev.xkmc.l2core.util.Proxy;
import dev.xkmc.youkaishomecoming.init.data.YHLangData;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TooltipUtil {

	public static @Nullable Player getPlayer() {
		return Proxy.getPlayer();
	}


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
		var food = stack.getFoodProperties(getPlayer());
		if (food == null) return;
		getFoodEffects(food, list);
	}

	public static void getFoodEffects(FoodProperties food, List<Component> list) {
		for (var eff : food.effects()) {
			int chance = Math.round(eff.probability() * 100);
			if (eff.effect() == null) continue; //I hate stupid modders
			Component ans = getTooltip(eff.effect());
			if (chance == 100) {
				list.add(ans);
			} else {
				list.add(YHLangData.CHANCE_EFFECT.get(ans, chance));
			}
		}
	}


}
