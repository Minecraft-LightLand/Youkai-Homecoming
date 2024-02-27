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

public class CaffeinatedEffect extends EmptyEffect {

	public CaffeinatedEffect(MobEffectCategory category, int color) {
		super(category, color);
		addAttributeModifier(Attributes.ATTACK_DAMAGE,
				MathHelper.getUUIDFromString("caffeinated").toString(), 0.1f,
				AttributeModifier.Operation.MULTIPLY_BASE);
	}

}
