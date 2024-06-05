package dev.xkmc.youkaishomecoming.content.effect;

import dev.xkmc.l2library.util.math.MathHelper;
import dev.xkmc.youkaishomecoming.init.data.YHModConfig;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class HigiEffect extends MobEffect {

	public HigiEffect(MobEffectCategory category, int color) {
		super(category, color);
		var uuid = MathHelper.getUUIDFromString("higi").toString();
		addAttributeModifier(Attributes.ATTACK_DAMAGE, uuid, 0.1, AttributeModifier.Operation.MULTIPLY_BASE);
		addAttributeModifier(Attributes.MOVEMENT_SPEED, uuid, 0.1, AttributeModifier.Operation.MULTIPLY_BASE);
	}

	@Override
	public void applyEffectTick(LivingEntity e, int lv) {
		if (!e.level().isClientSide) {
			e.heal(1 << lv);
		}
	}

	@Override
	public boolean isDurationEffectTick(int tick, int lv) {
		return tick % YHModConfig.COMMON.higiHealingPeriod.get() == 0;
	}


}
