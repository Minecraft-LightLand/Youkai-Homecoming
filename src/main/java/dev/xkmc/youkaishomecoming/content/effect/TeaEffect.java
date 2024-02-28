package dev.xkmc.youkaishomecoming.content.effect;

import dev.xkmc.l2library.util.math.MathHelper;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class TeaEffect extends EmptyEffect {

	public TeaEffect(MobEffectCategory category, int color) {
		super(category, color);
		addAttributeModifier(Attributes.ATTACK_SPEED,
				MathHelper.getUUIDFromString("tea").toString(), 0.05f,
				AttributeModifier.Operation.MULTIPLY_BASE);
	}

}
