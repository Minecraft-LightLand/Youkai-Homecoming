package dev.xkmc.youkaishomecoming.content.effect;

import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class CaffeinatedEffect extends EmptyEffect {

	public CaffeinatedEffect(MobEffectCategory category, int color) {
		super(category, color);
		addAttributeModifier(Attributes.ATTACK_DAMAGE, YoukaisHomecoming.loc("caffeinated"), 0.1f,
				AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
	}

}
