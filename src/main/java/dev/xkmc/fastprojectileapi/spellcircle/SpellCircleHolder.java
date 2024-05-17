package dev.xkmc.fastprojectileapi.spellcircle;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public interface SpellCircleHolder {

	boolean shouldShowSpellCircle();

	@Nullable
	ResourceLocation getSpellCircle();

	float getCircleSize(float pTick);

}
