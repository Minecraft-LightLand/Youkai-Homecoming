package dev.xkmc.youkaishomecoming.content.effect;

import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.data.YHModConfig;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class HigiEffect extends MobEffect {

	public HigiEffect(MobEffectCategory category, int color) {
		super(category, color);
		var uuid = YoukaisHomecoming.loc("higi");
		addAttributeModifier(Attributes.ATTACK_DAMAGE, uuid, 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
		addAttributeModifier(Attributes.MOVEMENT_SPEED, uuid, 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
	}

	@Override
	public boolean applyEffectTick(LivingEntity e, int lv) {
		if (!e.level().isClientSide) {
			float amount = 1;
			int period = YHModConfig.COMMON.higiHealingPeriod.get() >> lv;
			if (period < 10) period = 10;
			if (e.tickCount % period == 0)
				e.heal(amount);
		}
		return true;
	}

	@Override
	public boolean shouldApplyEffectTickThisTick(int tick, int lv) {
		return true;
	}

}
