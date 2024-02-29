package dev.xkmc.youkaishomecoming.content.effect;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class MandrakeEffect extends EmptyEffect {

	public MandrakeEffect(MobEffectCategory category, int color) {
		super(category, color);
	}

	@Override
	public void applyEffectTick(LivingEntity e, int lv) {
		var pos = e.blockPosition();
		if (!e.onGround()) return;
		if (!e.isCrouching()) return;
		if (e.level().getBlockState(pos).isCollisionShapeFullBlock(e.level(), pos))
			return;
		e.moveTo(e.getX(), e.getY() - 0.1f, e.getZ());

	}

	@Override
	public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
		return true;
	}

}
