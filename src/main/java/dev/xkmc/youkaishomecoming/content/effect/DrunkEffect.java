package dev.xkmc.youkaishomecoming.content.effect;

import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class DrunkEffect extends MobEffect {
	public DrunkEffect(MobEffectCategory category, int color) {
		super(category, color);
		addAttributeModifier(Attributes.ATTACK_DAMAGE, YoukaisHomecoming.loc("drunk"), 2,
				AttributeModifier.Operation.ADD_VALUE);
	}

}
