package dev.xkmc.youkaihomecoming.content.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class EmptyEffect extends MobEffect {

	public EmptyEffect(MobEffectCategory category, int color) {
		super(category, color);
	}

	@Override
	public final List<ItemStack> getCurativeItems() {
		return List.of();
	}

}
