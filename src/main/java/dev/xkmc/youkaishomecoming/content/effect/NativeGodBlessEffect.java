package dev.xkmc.youkaishomecoming.content.effect;

import dev.xkmc.l2library.util.math.MathHelper;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeMod;

import java.util.List;

public class NativeGodBlessEffect extends EmptyEffect {

	public NativeGodBlessEffect(MobEffectCategory category, int color) {
		super(category, color);
		String uuid = MathHelper.getUUIDFromString("native_god_bless").toString();
		addAttributeModifier(ForgeMod.ENTITY_REACH.get(), uuid, 1f,
				AttributeModifier.Operation.ADDITION);
		addAttributeModifier(ForgeMod.BLOCK_REACH.get(), uuid, 1f,
				AttributeModifier.Operation.ADDITION);
		addAttributeModifier(Attributes.MOVEMENT_SPEED, uuid, 0.2f,
				AttributeModifier.Operation.MULTIPLY_BASE);
	}

	@Override
	public void applyEffectTick(LivingEntity e, int pAmplifier) {
	}

	@Override
	public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
		return true;
	}

}
