package dev.xkmc.youkaihomecoming.content.effect;

import dev.xkmc.l2library.util.math.MathHelper;
import dev.xkmc.youkaihomecoming.init.registrate.YHEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class CaffeinatedEffect extends MobEffect {

	public CaffeinatedEffect(MobEffectCategory category, int color) {
		super(category, color);
		addAttributeModifier(Attributes.ATTACK_DAMAGE,
				MathHelper.getUUIDFromString("caffeinated").toString(), 0.1f,
				AttributeModifier.Operation.MULTIPLY_BASE);
	}

	@Override
	public void applyEffectTick(LivingEntity e, int pAmplifier) {
		if (e.hasEffect(YHEffects.YOUKAIFYING.get()))
			e.removeEffect(YHEffects.YOUKAIFYING.get());
		if (e.hasEffect(YHEffects.YOUKAIFIED.get()))
			e.removeEffect(YHEffects.YOUKAIFIED.get());
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
