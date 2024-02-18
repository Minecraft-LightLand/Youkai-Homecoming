package dev.xkmc.youkaihomecoming.init.registrate;

import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.youkaihomecoming.content.effect.*;
import dev.xkmc.youkaihomecoming.init.YoukaiHomecoming;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class YHEffects {

	public static final RegistryEntry<YoukaifiedEffect> YOUKAIFIED = genEffect("youkaified",
			() -> new YoukaifiedEffect(MobEffectCategory.NEUTRAL, 0xffffff),
			"You are a Youkai now");

	public static final RegistryEntry<YoukaifyingEffect> YOUKAIFYING = genEffect("youkaifying",
			() -> new YoukaifyingEffect(MobEffectCategory.NEUTRAL, 0xffffff),
			"You are becoming a Youkai");

	public static final RegistryEntry<CaffeinatedEffect> CAFFEINATED = genEffect("caffeinated",
			() -> new CaffeinatedEffect(MobEffectCategory.NEUTRAL, -10667225),
			"Prevents phantom spawn, prevents sleep, prevents youkaization, and boost attack damage");

	public static final RegistryEntry<EmptyEffect> TEA = genEffect("tea_polyphenols",
			() -> new EmptyEffect(MobEffectCategory.BENEFICIAL, -5727850),
			"Prevents nausea, and boost attack speed");

	public static final RegistryEntry<NativeGodBlessEffect> NATIVE = genEffect("native_god_bless",
			() -> new NativeGodBlessEffect(MobEffectCategory.BENEFICIAL, -5727850),
			"Increase movement speed and reach");

	public static final RegistryEntry<EmptyEffect> UNCONSCIOUS = genEffect("unconscious",
			() -> new EmptyEffect(MobEffectCategory.BENEFICIAL, -5522492),
			"You won't be targeted by mobs. Terminates when you attack.");

	private static <T extends MobEffect> RegistryEntry<T> genEffect(String name, NonNullSupplier<T> sup, String desc) {
		return YoukaiHomecoming.REGISTRATE.effect(name, sup, desc).lang(MobEffect::getDescriptionId).register();
	}

	public static void register() {

	}

}
