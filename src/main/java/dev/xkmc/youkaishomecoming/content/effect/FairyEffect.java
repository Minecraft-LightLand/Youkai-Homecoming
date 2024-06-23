package dev.xkmc.youkaishomecoming.content.effect;

import dev.xkmc.l2library.util.math.MathHelper;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class FairyEffect extends MobEffect {

	public FairyEffect(MobEffectCategory category, int color) {
		super(category, color);
		String uuid = MathHelper.getUUIDFromString("fairy").toString();
		addAttributeModifier(Attributes.MAX_HEALTH, uuid, -0.5, AttributeModifier.Operation.MULTIPLY_TOTAL);
		addAttributeModifier(Attributes.MOVEMENT_SPEED, uuid, 0.2, AttributeModifier.Operation.MULTIPLY_TOTAL);
	}

	@Override
	public void applyEffectTick(LivingEntity e, int lv) {

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
