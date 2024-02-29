package dev.xkmc.youkaishomecoming.content.effect;

import dev.xkmc.l2library.util.math.MathHelper;
import dev.xkmc.youkaishomecoming.init.data.YHModConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class TeaEffect extends EmptyEffect {

	public TeaEffect(MobEffectCategory category, int color) {
		super(category, color);
		addAttributeModifier(Attributes.ATTACK_SPEED,
				MathHelper.getUUIDFromString("tea").toString(), 0.05f,
				AttributeModifier.Operation.MULTIPLY_BASE);
	}

	@Override
	public void applyEffectTick(LivingEntity e, int lv) {
		if (e.level().isDay() && !e.level().isClientSide) {
			float f = e.getLightLevelDependentMagicValue();
			BlockPos blockpos = BlockPos.containing(e.getX(), e.getEyeY(), e.getZ());
			if (f > 0.5F && e.level().canSeeSky(blockpos)) {
				e.heal(1 << lv);
			}
		}
	}

	@Override
	public boolean isDurationEffectTick(int tick, int lv) {
		return tick % YHModConfig.COMMON.teaHealingPeriod.get() == 0;
	}

}
