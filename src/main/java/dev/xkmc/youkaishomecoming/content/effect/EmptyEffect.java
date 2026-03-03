package dev.xkmc.youkaishomecoming.content.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.EffectCure;

import java.util.List;
import java.util.Set;

public class EmptyEffect extends MobEffect {

	public EmptyEffect(MobEffectCategory category, int color) {
		super(category, color);
	}

	@Override
	public void fillEffectCures(Set<EffectCure> cures, MobEffectInstance effectInstance) {

	}

}
