package dev.xkmc.youkaihomecoming.init.registrate;

import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.youkaihomecoming.content.effect.EmptyEffect;
import dev.xkmc.youkaihomecoming.init.YoukaiHomecoming;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class YHEffects {

	public static final RegistryEntry<EmptyEffect> YOUKAIFIED = genEffect("youkaified",
			() -> new EmptyEffect(MobEffectCategory.NEUTRAL, 0xffffff),
			"You are a Youkai now");

	public static final RegistryEntry<EmptyEffect> YOUKAIFYING = genEffect("youkaifying",
			() -> new EmptyEffect(MobEffectCategory.NEUTRAL, 0xffffff),
			"You are becoming a Youkai");

	private static <T extends MobEffect> RegistryEntry<T> genEffect(String name, NonNullSupplier<T> sup, String desc) {
		return YoukaiHomecoming.REGISTRATE.effect(name, sup, desc).lang(MobEffect::getDescriptionId).register();
	}

	public static void register() {

	}

}
