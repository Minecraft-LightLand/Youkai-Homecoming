package dev.xkmc.youkaishomecoming.content.effect;

import dev.xkmc.l2library.base.effects.EffectUtil;
import dev.xkmc.l2library.init.events.GeneralEventHandler;
import dev.xkmc.l2library.util.math.MathHelper;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class YoukaifiedEffect extends MobEffect {

	public YoukaifiedEffect(MobEffectCategory category, int color) {
		super(category, color);
		String uuid = MathHelper.getUUIDFromString("youkaified").toString();
		addAttributeModifier(Attributes.MAX_HEALTH, uuid, 20, AttributeModifier.Operation.ADDITION);
		addAttributeModifier(Attributes.ATTACK_DAMAGE, uuid, 0.5, AttributeModifier.Operation.MULTIPLY_BASE);
		addAttributeModifier(Attributes.MOVEMENT_SPEED, uuid, 0.3, AttributeModifier.Operation.MULTIPLY_TOTAL);
	}

	@Override
	public void applyEffectTick(LivingEntity e, int lv) {
		if (e instanceof Player player) {
			player.causeFoodExhaustion(0.02f);
			if (player.getFoodData().getFoodLevel() < 10) {
				EffectUtil.refreshEffect(e, new MobEffectInstance(MobEffects.CONFUSION, 40, 0,
						true, true), EffectUtil.AddReason.SELF, e);
			}
			if (player.getFoodData().getFoodLevel() < 6) {
				GeneralEventHandler.schedule(() -> player.removeEffect(this));
			}
		}
	}

	@Override
	public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
		return true;
	}

	@Override
	public List<ItemStack> getCurativeItems() {
		return List.of();
	}

}
