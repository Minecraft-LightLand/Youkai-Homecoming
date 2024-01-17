package dev.xkmc.youkaihomecoming.content.effect;

import dev.xkmc.l2library.util.math.MathHelper;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
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
	public List<ItemStack> getCurativeItems() {
		return List.of();
	}

}
