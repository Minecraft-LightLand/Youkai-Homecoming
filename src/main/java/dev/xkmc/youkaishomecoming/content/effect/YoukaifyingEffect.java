package dev.xkmc.youkaishomecoming.content.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class YoukaifyingEffect extends MobEffect {

	public YoukaifyingEffect(MobEffectCategory category, int color) {
		super(category, color);
	}

	@Override
	public List<ItemStack> getCurativeItems() {
		return List.of();
	}
	
}
